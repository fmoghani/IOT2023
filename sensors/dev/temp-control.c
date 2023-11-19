#include "temp-control.h"
#include "random.h"
#include <stdbool.h>

static double min_threshold = 40.0;
static double max_threshold = 60.0;
static double TEMP_VALUE = 50;

double get_temp_value(){

    return TEMP_VALUE;

}

bool temp_is_down_threshold(){

    return TEMP_VALUE <= min_threshold;

}

bool temp_is_over_threshold(){

    return TEMP_VALUE >= max_threshold;

}

bool temp_is_normal(){

    return (TEMP_VALUE < max_threshold) & (TEMP_VALUE > min_threshold);

}

/* Function to randomly generate a simulated value of the sensor */
double collect_temp_data() {
    double maxValue = 100.00;
    double e = 15.0;
    double lowerBound = ((TEMP_VALUE-e) < 0)? 0 : (-e);
    double upperBound = ((TEMP_VALUE+e) > maxValue)? (TEMP_VALUE+e-maxValue) : (e);

    /* generating a float in range lowerBound to upperBound */
    double delta = ((double)(random_rand()%(int)((upperBound-lowerBound)*maxValue)))/maxValue + lowerBound;
    /* adding the delta to the old value to generate a realistic new value */
    double new_value = TEMP_VALUE + delta ; //float in range 0 to 100
    TEMP_VALUE = new_value;
    return new_value;
}
