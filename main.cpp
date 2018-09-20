// ----------------------------------------------------------------------------
// Copyright 2016-2018 ARM Ltd.
//
// SPDX-License-Identifier: Apache-2.0
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ----------------------------------------------------------------------------
#include <cstdlib>
#include "simplem2mclient.h"
#ifdef TARGET_LIKE_MBED
#include "mbed.h"
#endif

#include "application_init.h"
#include "mcc_common_button_and_led.h"
#include "blinky.h"
#include "DHT.h"

#include "CCS811.h"
#include "GroveGPS.h"

#define	 CDS_DATA_PIN A1
#define  DHT_DATA_PIN D14
 
// event based LED blinker, controlled via pattern_resource
static Blinky blinky;

Serial gps_serial(PF_7, PF_6, 115200);
 
GroveGPS gps;
DHT sensor(DHT_DATA_PIN,DHT11);
AnalogIn illuminance_sensor(CDS_DATA_PIN);
CCS811 ccs811(PF_0, PF_1);


static void main_application(void);


int main(void)
{
	
    mcc_platform_run_program(main_application);
}

// Pointers to the resources that will be created in main_application().
static M2MResource* button_res;

static M2MResource* temperature_celsius_res;
static M2MResource* humidity_res;
static M2MResource* illuminance_res;
static M2MResource* voc_res;
static M2MResource* gps_res;

static M2MResource* pattern_res;
static M2MResource* blink_res;

// Pointer to mbedClient, used for calling close function.
static SimpleM2MClient *client;

void pattern_updated(const char *)
{
    printf("PUT received, new value: %s\n", pattern_res->get_value_string().c_str());
}

void blinky_completed(void)
{
    printf("Blinky completed \n");

    // Send response to backend
    blink_res->send_delayed_post_response();
}

void blink_callback(void *)
{
    String pattern_string = pattern_res->get_value_string();
    const char *pattern = pattern_string.c_str();
    printf("LED pattern = %s\n", pattern);

    // The pattern is something like 500:200:500, so parse that.
    // LED blinking is done while parsing.
    const bool restart_pattern = false;
    if (blinky.start((char*)pattern_res->value(), pattern_res->value_length(), restart_pattern, blinky_completed) == false) {
        printf("out of memory error\n");
    }
}

void button_status_callback(const M2MBase& object,
                            const M2MBase::MessageDeliveryStatus status,
                            const M2MBase::MessageType /*type*/)
{
    switch(status) {
        case M2MBase::MESSAGE_STATUS_BUILD_ERROR:
            printf("Message status callback: (%s) error when building CoAP message\n", object.uri_path());
            break;
        case M2MBase::MESSAGE_STATUS_RESEND_QUEUE_FULL:
            printf("Message status callback: (%s) CoAP resend queue full\n", object.uri_path());
            break;
        case M2MBase::MESSAGE_STATUS_SENT:
            printf("Message status callback: (%s) Message sent to server\n", object.uri_path());
            break;
        case M2MBase::MESSAGE_STATUS_DELIVERED:
            printf("Message status callback: (%s) Message delivered\n", object.uri_path());
            break;
        case M2MBase::MESSAGE_STATUS_SEND_FAILED:
            printf("Message status callback: (%s) Message sending failed\n", object.uri_path());
            break;
        case M2MBase::MESSAGE_STATUS_SUBSCRIBED:
            printf("Message status callback: (%s) subscribed\n", object.uri_path());
            break;
        case M2MBase::MESSAGE_STATUS_UNSUBSCRIBED:
            printf("Message status callback: (%s) subscription removed\n", object.uri_path());
            break;
        case M2MBase::MESSAGE_STATUS_REJECTED:
            printf("Message status callback: (%s) server has rejected the message\n", object.uri_path());
            break;
        default:
            break;
    }
}

// This function is called when a POST request is received for resource 5000/0/1.
void unregister(void *)
{
    printf("Unregister resource executed\n");
    client->close();
}

// This function is called when a POST request is received for resource 5000/0/2.
void factory_reset(void *)
{
    printf("Factory reset resource executed\n");
    client->close();
    kcm_status_e kcm_status = kcm_factory_reset();
    if (kcm_status != KCM_STATUS_SUCCESS) {
        printf("Failed to do factory reset - %d\n", kcm_status);
    } else {
        printf("Factory reset completed. Now restart the device\n");
    }
}









void main_application(void)
{
	ccs811.init();
	
	
    // https://github.com/ARMmbed/sd-driver/issues/93 (IOTMORF-2327)
    // SD-driver initialization can fails with bd->init() -5005. This wait will
    // allow the board more time to initialize.
#ifdef TARGET_LIKE_MBED
    wait(2);
	
	
#endif
    // Initialize trace-library first
    if (application_init_mbed_trace() != 0) {
        printf("Failed initializing mbed trace\n" );
        return;
    }
	
    // Initialize storage
    if (mcc_platform_storage_init() != 0) {
        printf("Failed to initialize storage\n" );
        return;
    }

    // Initialize platform-specific components
    if(mcc_platform_init() != 0) {
        printf("ERROR - platform_init() failed!\n");
        return;
    }

    // Print platform information
    mcc_platform_sw_build_info();

    // Print some statistics of the object sizes and their heap memory consumption.
    // NOTE: This *must* be done before creating MbedCloudClient, as the statistic calculation
    // creates and deletes M2MSecurity and M2MDevice singleton objects, which are also used by
    // the MbedCloudClient.
#ifdef MBED_HEAP_STATS_ENABLED
    print_m2mobject_stats();
#endif

    // SimpleClient is used for registering and unregistering resources to a server.
    SimpleM2MClient mbedClient;

    // application_init() runs the following initializations:
    //  1. platform initialization
    //  2. print memory statistics if MBED_HEAP_STATS_ENABLED is defined
    //  3. FCC initialization.
    if (!application_init()) {
        printf("Initialization failed, exiting application!\n");
        return;
    }

    // Save pointer to mbedClient so that other functions can access it.
    client = &mbedClient;

#ifdef MBED_HEAP_STATS_ENABLED
    printf("Client initialized\r\n");
    print_heap_stats();
#endif
#ifdef MBED_STACK_STATS_ENABLED
    print_stack_statistics();
#endif



	
			  
    // Create resource for button count. Path of this resource will be: 3200/0/5501.
    button_res = mbedClient.add_cloud_resource(3200, 0, 5501, "button_resource", M2MResourceInstance::INTEGER,
                              M2MBase::GET_ALLOWED, 0, true, NULL, (void*)button_status_callback);

							  
							  
							  
	//Create resource for temperature. Path of this resource will be: 3303/0/5700.
	temperature_celsius_res = mbedClient.add_cloud_resource(3303, 0, 5700, "temperature_celsius_resource", M2MResourceInstance::FLOAT,
                              M2MBase::GET_ALLOWED, 0, true, NULL, (void*)button_status_callback);
	
	//Create resource for humidity. Path of this resource will be: 3304/0/5700.
	humidity_res = mbedClient.add_cloud_resource(3304, 0, 5700, "humidity_resource", M2MResourceInstance::FLOAT,
                              M2MBase::GET_ALLOWED, 0, true, NULL, (void*)button_status_callback);							  
	
	// Create resource for cds. Path of this resource will be: 3301/0/5700.						  
	illuminance_res = mbedClient.add_cloud_resource(3301, 0, 5700, "illuminance_resource", M2MResourceInstance::FLOAT,
                              M2MBase::GET_ALLOWED, 0, true, NULL, (void*)button_status_callback);
							  
	// Create resource for voc. Path of this resource will be: 20001/0/5997.
	voc_res = mbedClient.add_cloud_resource(20001, 0, 5997, "voc_resource", M2MResourceInstance::INTEGER,
                              M2MBase::GET_ALLOWED, 0, true, NULL, (void*)button_status_callback);
	
	// Create resource for voc. Path of this resource will be: 20003/0/5999.
	gps_res = mbedClient.add_cloud_resource(20003, 0, 5999, "voc_resource", M2MResourceInstance::STRING,
                              M2MBase::GET_ALLOWED, 0, true, NULL, (void*)button_status_callback);
	
	
    // Create resource for led blinking pattern. Path of this resource will be: 3201/0/5853.
    pattern_res = mbedClient.add_cloud_resource(3201, 0, 5853, "pattern_resource", M2MResourceInstance::STRING,
                               M2MBase::GET_PUT_ALLOWED, "500:500:500:500", false, (void*)pattern_updated, NULL);					   
	
    // Create resource for starting the led blinking. Path of this resource will be: 3201/0/5850.
    blink_res = mbedClient.add_cloud_resource(3201, 0, 5850, "blink_resource", M2MResourceInstance::STRING,
                             M2MBase::POST_ALLOWED, "", false, (void*)blink_callback, (void*)button_status_callback);
    // Use delayed response
    blink_res->set_delayed_response(true);

    // Create resource for unregistering the device. Path of this resource will be: 5000/0/1.
    mbedClient.add_cloud_resource(5000, 0, 1, "unregister", M2MResourceInstance::STRING,
                 M2MBase::POST_ALLOWED, NULL, false, (void*)unregister, NULL);

    // Create resource for running factory reset for the device. Path of this resource will be: 5000/0/2.
    mbedClient.add_cloud_resource(5000, 0, 2, "factory_reset", M2MResourceInstance::STRING,
                 M2MBase::POST_ALLOWED, NULL, false, (void*)factory_reset, NULL);

    mbedClient.register_and_connect();

    // Check if client is registering or registered, if true sleep and repeat.
    while (mbedClient.is_register_called()) {
		
        static char latBuffer[16], lonBuffer[16],old_latBuffer[16],old_lonBuffer[16],gpsBuffer[40];
		
		float val;
            
        //initialize static variables
		static float old_celsius_value = 0.0f, celsius_value = 0.0f,
		old_humidity_value = 0.1f, humidity_value = 0.1f;
		static float illuminance_value = 0.0f, old_illuminance_value = 1.0f;
		illuminance_value = illuminance_sensor.read()*10;
		static uint16_t eco2 = 0.0f, tvoc = 0.0f, old_tvoc = 0.0f;
		ccs811.readData(&eco2, &tvoc);
		
		gps.getLatitude(latBuffer);
        gps.getLongitude(lonBuffer);
		
		
		if (!((bool)sensor.readData())) {
			celsius_value = sensor.ReadTemperature(CELCIUS);
			humidity_value = sensor.ReadHumidity();
			if (old_celsius_value != celsius_value) {
				old_celsius_value = celsius_value;
				printf("temperature : %f\n",celsius_value);
				temperature_celsius_res->set_value(celsius_value);
			}
			if (old_humidity_value != humidity_value) {
				old_humidity_value = humidity_value;
				printf("humidity : %f\n",humidity_value);
				humidity_res->set_value(humidity_value);
			}
        }
		if(strncmp(old_latBuffer,latBuffer,10) 
			|| strncmp(old_lonBuffer,lonBuffer,10)){
						
			strcpy(old_lonBuffer,lonBuffer);
			strcpy(old_latBuffer,latBuffer);
		
			
			sprintf(gpsBuffer,"%f, %s",latBuffer,lonBuffer);
			gps_res->set_value(gpsBuffer);
		}	
		
		
		
		if(old_tvoc != tvoc){
			old_tvoc = tvoc;
			printf("TVOC: %d \n", tvoc);
			 voc_res->set_value(tvoc);
		}
		
		
		
		
		
		if(old_illuminance_value != illuminance_value){
			old_illuminance_value = illuminance_value;
			printf("illuminance : %f\n",illuminance_value);
			illuminance_res->set_value(illuminance_value);
		}
		
		
		
        static int button_count = 0;
        mcc_platform_do_wait(100);
		
        if (mcc_platform_button_clicked()) {
            button_res->set_value(++button_count);
        }
		
    }

    // Client unregistered, exit program.
}
