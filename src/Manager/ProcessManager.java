package Manager;

import Manager.Process;

// READY LIST IMPLEMENTED AS LL OF PROCESSESS
// PROCESSES STORE THE PCB INDICES INSTEAD
import java.awt.color.ProfileDataException;
import java.util.LinkedList;
import java.util.ListIterator;

public class ProcessManager {
    int num_processes = 16;
    int num_resources = 4;
    Process currentProcess;
    Process[] processes;
    Resource[] resources;
    List[] ready_list;

    public void init() {
        processes = new Process[num_processes];
        for (int i = 0; i < processes.length; i++) {
            processes[i] = new Process();
        }
        resources = new Resource[num_resources];
        resources[0] = new Resource(1,0);
        resources[1] = new Resource(1,1);
        resources[2] = new Resource(2,2);
        resources[3] = new Resource(3,3);

        ready_list = new List[3]; // Create ready list
        for (int i = 0; i < 3; i++) {
            ready_list[i] = new List();
        }
        ready_list[0] = new List();
        processes[0] = new Process(1, -1, 0, 0);  // State, Parent, Prior
        ready_list[0].insert(processes[0]);  // Add process[0] to the ready list
        currentProcess = processes[0];
        System.out.print("\n");
        System.out.print("0 ");
    }
    //get highest priority to compare to current process in scheduler
    public Process highest_priority() {
        for (int priority = 2; priority >= 0; priority--) {
            if (ready_list[priority].size() > 0) {
                return ready_list[priority].getFirst();
            }
        }
        return null;
    }
    public void scheduler(){
        Process highest_priority = highest_priority();
        if(highest_priority.priority >= currentProcess.priority){
            currentProcess = highest_priority;
            System.out.print(Integer.toString(highest_priority.index)+ " ");
        }
        else{
            System.out.print(Integer.toString(currentProcess.index) + " ");
        }
    }
    //Find a Free PCB index     #USED IN CREATE
    public int find_free() {
        int index = -1;
        for (int i = 0; i < processes.length; i++) {
            if (processes[i].used == false) {
                index = i;
                break;
            }
        }
        return index;
    }
    // Create a process from a free PCB index    CHECKED
    public void create(int priority) {
        int j = find_free(); //Free PCB index
        if (j == -1) {System.out.println("There are no free PCB indices");}  //ALL INDICES FULL
        int parent = currentProcess.index;
        //Allocate new PCB[J], state ready, parent
        processes[j] = new Process(1, parent, priority,j);   //state,parent,priority,PCB index
        currentProcess.add_child(j);   //insert j into children of i
        ready_list[priority].insert(processes[j]); //insert j into ready list
  //      System.out.println("process " + Integer.toString(j) + " created");
        scheduler();
    }

    public void destroy (int j, boolean flag2){
        int counter = 0;
        boolean flag = false;
        if (flag2 == true){
            flag = true;
        }
        if (processes[j].used && j != currentProcess.index) {   //the process has to be used before you destroy it
            if (processes[j].get_children() != null) {  // destory children
                LinkedList<Integer> children = processes[j].get_children();
                ListIterator child_iterator = children.listIterator();
                while (child_iterator.hasNext()) {
                    int child_index = (int) child_iterator.next();
                    destroy(child_index,false);
                }
            }
            //processes[currentProcess]   remove j from list of children of i
            currentProcess.remove_child(j);
            // remove j from RL or waiting list  ###############
            if (processes[j].state == 1) { // if it's free remove from RL, if blocked remove from waiting list
                ready_list[processes[j].priority].remove(processes[j]);   // REMOVE FROM READY LIST
            } else {
                for (int i = 0; i < 4; i++) {  //remove from waitlist
                    boolean in_there = resources[i].in_there(processes[j]);
                    if (in_there) {
                        resources[i].remove_from_waitlist(processes[j]);
                    }
                }
            }
            //release all resources of j
            LinkedList<Integer> resource_ll = processes[j].resources;  //release resources
            ListIterator resource_iterator = resource_ll.listIterator();
            while (resource_iterator.hasNext()) {
                int resource_index = (int) resource_iterator.next();
                release2(resource_index, processes[j].acquired_units[resource_index]);
            }
            //Free PCB of j
            processes[j] = new Process();
            if (flag == true){
                scheduler();
            }
        }
        else{
            if(flag == true) {
                System.out.println("-1 ");
            }
            flag = true;
        }
    }


    public void request (int r, int k){
        if (k + currentProcess.acquired_units[r] <=  resources[r].total_units) {   //req units + units held by process cant exceed initial inventory of reosurce
            if (resources[r].available_units >= k) {  // state of r is free
                if (currentProcess.has_child(r))
                {
                    resources[r].available_units -= k;
                    currentProcess.acquired_units[r] += k;
                }
                else{
                    resources[r].available_units -= k;
                    currentProcess.add_resource(r);
                    currentProcess.acquired_units[r] = k;
                }
           //     System.out.println("resources " + Integer.toString(r) + " allocated");
            } else { //blocked
                currentProcess.state = 0; // state of current process is blocked
                ready_list[currentProcess.priority].remove(currentProcess);//get rid of it from ready list
                currentProcess.requested_units = k; // requested units
                currentProcess.priority = 0;  //GOTTA SET THIS OR IT WON'T GO TO LOWER PRIORITY SINCE CURR IS HIGHER THAN LOWER
                resources[r].add_to_waitlist(currentProcess);  // put on waitlist
          //      System.out.println("process " + Integer.toString(currentProcess.index) + " blocked");
            }
        }
        else{
            System.out.print("-1 ");
        }
        scheduler();
    }

    //might have done this wrong what is r, resource index or is it the actualy resource
    public void release(int r, int k){  // resource r   and number of released units
        currentProcess.remove_resource(r);
        if (k <= currentProcess.acquired_units[r]){ // num of units released cant exceed num units held
            currentProcess.acquired_units[r] -= k; //release these units from process
            resources[r].available_units += k; // add released units back to resource[r]

            while (!resources[r].waitlist.isEmpty()) {
                if (resources[r].available_units >= resources[r].get_first_waitlist().requested_units) {
                    Process first_waitlist = resources[r].get_first_waitlist();
                    resources[r].remove_from_waitlist(first_waitlist);
                    ready_list[first_waitlist.priority].insert(processes[first_waitlist.index]);
                    first_waitlist.state = 1;
                    first_waitlist.add_resource(r);
                    resources[r].available_units -= first_waitlist.requested_units;

                }
                else{
                    break;
                }
            }
        }
        else{
            System.out.print("-1 ");
            return;
        }
        scheduler();
    //    System.out.println("resource " + Integer.toString(r) + " released");
    }

    //might have done this wrong what is r, resource index or is it the actualy resource
    public void release2(int r, int k){  // resource r   and number of released units
        currentProcess.remove_resource(r);
        if (k <= currentProcess.acquired_units[r]){ // num of units released cant exceed num units held
            currentProcess.acquired_units[r] -= k; //release these units from process
            resources[r].available_units += k; // add released units back to resource[r]

            while (!resources[r].waitlist.isEmpty()) {
                if (resources[r].available_units >= resources[r].get_first_waitlist().requested_units) {
                    Process first_waitlist = resources[r].get_first_waitlist();
                    resources[r].remove_from_waitlist(first_waitlist);
                    ready_list[first_waitlist.priority].insert(processes[first_waitlist.index]);
                    first_waitlist.state = 1;
                    first_waitlist.add_resource(r);
                    resources[r].available_units -= first_waitlist.requested_units;

                }
            }
        }
        else{
            return;
        }
        scheduler();
        //    System.out.println("resource " + Integer.toString(r) + " released");
    }

    public void timeout(){
        ready_list[currentProcess.priority].remove(currentProcess);
        ready_list[currentProcess.priority].insert_end(processes[currentProcess.index]);
        scheduler();
    }

}


