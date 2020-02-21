package Shell;

import java.io.*;

import Manager.ProcessManager;

import java.util.regex.Pattern;
public class ShellFileReader {
    BufferedReader reader;
    ProcessManager m = new ProcessManager();
    public ShellFileReader()
    {
        try {
            String filename = "/Users/richardle/Downloads/sample-input.txt";
            reader = new BufferedReader(new FileReader(filename));
            String name = reader.readLine();
            while (name != null) {

                String[] commands = name.split(" ");
                String command = commands[0];
                if (command.equals("cr") && (commands.length == 2)) {
                    if (Pattern.compile("1|2").matcher(commands[1]).matches()) {
                        int priority = Integer.parseInt(commands[1]);
                        m.create(priority);
                    } else {
                        System.out.print("-1 ");
                    }
                } else if (command.equals("rq") && (commands.length == 3)) {
                    if (Pattern.compile("0|1|2|3").matcher(commands[1]).matches()) {
                        int resource_index = Integer.parseInt(commands[1]);
                        int req_units = Integer.parseInt(commands[2]);
                        m.request(resource_index, req_units);
                    } else {
                        System.out.print("-1 ");
                    }
                } else if (command.equals("rl")) {
                    if (Pattern.compile("0|1|2|3").matcher(commands[1]).matches()) {
                        int resource_index = Integer.parseInt(commands[1]);
                        int released_units = Integer.parseInt(commands[2]);
                        m.release(resource_index, released_units);
                    } else {
                        System.out.print("-1 ");
                    }

                } else if (command.equals("de") && commands.length == 2) {
                    int process_index = Integer.parseInt(commands[1]);

                    m.destroy(process_index,true);
                } else if (command.equals("to")) {
                    m.timeout();
                } else if (command.equals("in")) {
                    m.init();


                } else if (command.equals("exit")) {
                    System.out.println("Good-Bye");
                    break;
                }
                else {
                    //System.out.println("Invalid Output");
                }
                name = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("illegal input");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream o = new PrintStream(new File("A.txt"));

        // Store current System.out before assigning a new value
        PrintStream console = System.out;

        // Assign o to output stream
        System.setOut(o);
        ShellFileReader driver = new ShellFileReader();
    }

}
