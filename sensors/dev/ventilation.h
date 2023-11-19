#include <stdbool.h>

void init_vent();

void switch_heater_on();

void switch_heater_off();

void switch_cooler_on();

void switch_cooler_off();

void switch_ventilator_off();

int get_vent_mode();

bool heater_is_on();

bool heater_is_off();

bool cooler_is_on();

bool cooler_is_off();

bool vent_is_automatic();

void set_vent_automatic();

void set_vent_manual();
