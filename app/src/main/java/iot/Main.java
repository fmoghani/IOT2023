package iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class Main {

    public static List<NodeResource> nodeResources = new ArrayList<NodeResource>();
    public static List<ObservingClient> obsClients = new ArrayList<ObservingClient>();


    public static void main(String[] args) throws IOException, InterruptedException {

        /* Starting the registration server in a thread */
        new Thread() {
            public void run() {
                RegistrationServer server = new RegistrationServer(4456);
                server.startServer();
            }
        }.start();

        /* Preparing to start a loop to read from command line and execute some actions on the nodes */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CommandProcessor.printCommandsHelp();

        while (true) {
            try {
                String command_string = br.readLine();

                String command = CommandProcessor.extractCommand(command_string);
                //command 0 is NODES that prints the list of registered nodes
                if (command.equals(CommandProcessor.commands[0])) {
                    listRegisteredNode();
                } else if (command.equals(CommandProcessor.commands[1]) | command.equals(CommandProcessor.commands[2]) | command.equals(CommandProcessor.commands[3])) {
                    int index = CommandProcessor.extractIndex(command_string, nodeResources.size());
                    if (index >= 0) {
                        NodeResource r = nodeResources.get(index);
                        setVentilationAttribute(command, "mode", r);
                    } else if (index == -1) { // all nodes
                        for (NodeResource r : nodeResources)
                            if (r.getPath().contains("actuator"))
                                setVentilationAttribute(command, "mode", r);
                    }
                } else if (command.equals(CommandProcessor.commands[4]) | command.equals(CommandProcessor.commands[5])) {
                    // showing the status of observed sensors or actuators
                    int index = CommandProcessor.extractIndex(command_string, nodeResources.size());
                    if (index >= 0) {
                        NodeResource r = nodeResources.get(index);
                        showResourceStatus(command, r);
                    } else if (index == -1) {
                        for (NodeResource r : nodeResources)
                            if (command.equals(CommandProcessor.commands[4])) {
                                if (r.getPath().contains("sensor"))
                                    showResourceStatus(command, r);
                            } else {
                                if (r.getPath().contains("actuator"))
                                    showResourceStatus(command, r);
                            }
                    }

                } else if (command.equals(CommandProcessor.commands[6]) | command.equals(CommandProcessor.commands[7])) {
                    int index = CommandProcessor.extractIndex(command_string, nodeResources.size());
                    if (index >= 0) {
                        NodeResource r = nodeResources.get(index);
                        setVentilationAttribute(command, "automatic", r);
                    } else if (index == -1) { // all bulbs ON
                        for (NodeResource r : nodeResources)
                            if (r.getPath().contains("actuator"))
                                setVentilationAttribute(command, "automatic", r);
                    }
                } else if (command.equals(CommandProcessor.commands[8])) {
                    CommandProcessor.printCommandsHelp();

                } else if (command.equals(CommandProcessor.commands[9])) {
                    System.out.println("Terminating the application");
                    System.exit(0);

                } else {
                    System.out.println("Unable to process the command");
                }
            } catch (Exception e) {
                System.err.println("Unrecognized command please press enter and try again...");
                e.printStackTrace();
            }


        }

    }


    public static void listRegisteredNode() {
        if (nodeResources.isEmpty())
            System.out.println("No registered node found.");
        for (int i = 0; i < nodeResources.size(); i++) {
            NodeResource r = nodeResources.get(i);
            System.out.println("(" + i + ")\t" + r.toDetailedString());
        }
        System.out.println("");
    }


    public static void showResourceStatus(String nodeType, NodeResource r) {
        if (r.getPath().contains(nodeType.toLowerCase())) {
            int index = nodeResources.indexOf(r);
            System.out.print("(" + index + ") " + r.toString());
            System.out.println("\tTIMESTAMP\t\tVALUE");
            Map<Timestamp, String> v = r.getValues();
            for (Timestamp key : v.keySet()) {
                System.out.println("\t\t\t\t" + key + "   " + v.get(key));
            }
            System.out.println("");
        } else
            System.err.println("Wrong index: not " + nodeType);
    }

    public static void setVentilationAttribute(String value, String attribute, NodeResource res) {
        CoapClient client = new CoapClient(res.getCoapURI());
        if (res.getPath().contains("actuator")) {
            CoapResponse response = client.post(attribute + "=" + value, MediaTypeRegistry.TEXT_PLAIN);
            String code = response.getCode().toString();
            if (code.startsWith("2")) {
                System.out.println("[INFO] The mode " + attribute + " attribute is now " + value);

            } else {
                System.err.println("[MAIN] Response Code " + code);
            }

        } else {
            System.err.println("Addressing Error");
        }
    }


}


