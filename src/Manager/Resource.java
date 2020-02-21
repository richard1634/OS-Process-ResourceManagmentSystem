package Manager;

import java.util.LinkedList;

public class Resource {
    int available_units; // # of availiable units
    int total_units;  // initial number of units
    LinkedList<Process> waitlist = new LinkedList<Process>(); ; // also include units requested
    int id;

    public Resource(int units, int id2) {
        available_units = units;  // Free or allocated, 1 and 0
        id = id2;
        total_units = units;
    }

    public void add_to_waitlist(Process i){
        this.waitlist.add(i);
    }

    public void remove_from_waitlist(Process j){
        this.waitlist.remove(j);
    }

    public Process get_first_waitlist() {

        if (this.waitlist.isEmpty()){
            return null;
        }
        else{
            return this.waitlist.getFirst();
        }
    }

    public boolean in_there(Process j){
        if (this.waitlist.contains(j)){
            return true;
        }
        return false;
    }
}
