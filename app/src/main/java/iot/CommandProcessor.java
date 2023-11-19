package iot;

import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    public static final String[] commands = {"NODES", "HEAT", "COOL", "OFF", "SENSOR", "VENTILATOR", "AUTO", "MANUAL","HELP", "EXIT"};
    public static final Map<String, String> commandDescription = new HashMap<String, String>() {{
        put("NODES", "Prints the list of registered nodes");
        put("HEAT", "Turns on the heater of (index) node (Works in manual mode).");
        put("COOL", "Turns on the cooler of (index) node (Works in manual mode).");
        put("OFF", "Turns off the ventilator of (index) node (Works in manual mode).");
        put("SENSOR", "Reads the status of (index) sensory node");
        put("VENTILATOR", "Reads the status of (index) ventilator node");
        put("AUTO", "Sets the ventilator (index) to automatic mode");
        put("MANUAL", "Sets the ventilator (index) to manual mode");
        put("HELP", "Displays command help");
        put("EXIT", "Run to exit the program");
        //etc
    }};

    public static int extractIndex(String string_command, int upper_bound) {
        String[] splitted = string_command.split(" ");
        if (splitted.length > 1) {

            int index = Integer.parseInt(splitted[1]);
            if (index >= -1 && index < upper_bound)
                return index;
            else
                System.err.println("Index out of range");
        } else
            System.err.println("Invalid string command, Index not found.");

        return -2;
    }

    public static String extractCommand(String command) {
        command = command.toUpperCase();
        for (String s : commands) {
            if (command.contains(s)) {
                return s;
            }
        }
        return "invalid";
    }

    public static void printCommandsHelp() {
        System.out.println("----------------------------------------------------------------------------------------------------------------\n");
        System.out.println("For all command the format is as follow:");
        System.out.println("<command name> <index> (index = -1 means all nodes) ");
        System.out.println("----------------------------------------------------------------------------------------------------------------\n");
        for (Map.Entry<String, String> entry : commandDescription.entrySet()) {
            System.out.println(entry.getKey() + "\t\t\t\t" + entry.getValue());
        }
    }

}
