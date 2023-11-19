#include "ventilation.h"
#include "contiki.h"
#include <stdbool.h>
#include <stdio.h>

static bool automatic = 1;
static bool HEATER = 0;
static bool COOLER = 0;
process_event_t AUTOMATIC_VENT_EVENT ;
/* Node process */
extern struct process node_process;

void init_vent(){
    AUTOMATIC_VENT_EVENT = process_alloc_event();
}

void switch_heater_on(){
    HEATER = 1;
    COOLER = 0;
}

void switch_ventilator_off(){
    HEATER = 0;
    COOLER = 0;
}

void switch_heater_off(){
    HEATER = 0;
}

void switch_cooler_on(){
    COOLER = 1;
    HEATER = 0;
}

void switch_cooler_off(){
    COOLER = 0;
}


int get_vent_mode(){
    if (HEATER)
        return 1;
    else if (COOLER)
        return 2;
    else if (!(COOLER & HEATER))
        return 0;
    else
        return -1;
}

bool heater_is_on(){
    return HEATER == 1;
}

bool heater_is_off(){
    return HEATER == 0;
}

bool cooler_is_on(){
    return COOLER == 1;
}

bool cooler_is_off(){
    return COOLER == 0;
}


bool vent_is_automatic(){
    return automatic;
}

void set_vent_automatic(){

    automatic = 1;
    process_post(&node_process,AUTOMATIC_VENT_EVENT,NULL);
}

void set_vent_manual(){
    automatic = 0;
}

