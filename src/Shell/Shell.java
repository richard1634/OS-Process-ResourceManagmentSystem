package Shell;

import Manager.ProcessManager;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Shell {
    ProcessManager manager;

    public Shell() {
        // TODO Auto-generated method stub
        ProcessManager m = new ProcessManager();
        String name;
        Scanner in = new Scanner(System.in);

        do{
            System.out.print("$ ");
            name = in.nextLine();
            try{

                String[] commands = name.split(" ");
                String command = commands[0];
                if (command.equals("cr") && (commands.length == 2)){
                    if (Pattern.compile("1|2").matcher(commands[1]).matches()){
                        int priority = Integer.parseInt(commands[1]);
                        m.create(priority);
                    }
                    else{
                        System.out.println("-1 ");
                    }
                }
                else if (command.equals("rq") && (commands.length == 3)){
                    if (Pattern.compile("0|1|2|3").matcher(commands[1]).matches()) {
                        int resource_index = Integer.parseInt(commands[1]);
                        int req_units = Integer.parseInt(commands[2]);
                        m.request(resource_index,req_units);
                    }
                    else{
                        System.out.println(" -1 ");
                    }
                }
                else if (command.equals("rl")){
                    if (Pattern.compile("0|1|2|3").matcher(commands[1]).matches()){
                        int resource_index= Integer.parseInt(commands[1]);
                        int released_units = Integer.parseInt(commands[2]);
                        m.release(resource_index, released_units);
                    }
                    else{
                        System.out.println("invalid priorities");
                    }

                }
                else if (command.equals("de") && commands.length == 2)
                {
                    int process_index = Integer.parseInt(commands[1]);
                    m.destroy(process_index, true);
                }
                else if (command.equals("to")){
                    m.timeout();
                }
                else if (command.equals("in")){
                    m.init();
                }
                else if (command.equals("exit")){
                    System.out.println("Good-Bye");
                    break;
                }
                else{
                    continue;
                }
            }catch(Exception e){
                System.out.println("illegal input");
                e.printStackTrace();
            }
        } while(name != null);
    }

    public static void main(String[] args){
        Shell driver = new Shell();
    }

}


