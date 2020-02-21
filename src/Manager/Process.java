package Manager;

import java.util.LinkedList;
public class Process {
    int state;
    int priority;
    int parent;
    int index;
    int requested_units;
    int[] acquired_units = new int[4];
    LinkedList<Integer> children = new LinkedList<Integer>();
    LinkedList<Integer> resources = new LinkedList<Integer>();
    boolean used;

    public Process(){
        this.used = false;
    }
    public Process(int state, int parent, int priority, int index) {
        this.state = state;
        this.priority = priority;
        this.parent = parent;
//        this.children = null;
//        this.resources = null;
        this.used = true;
        this.index = index;
        this.requested_units = 0;
        for (int i = 0; i < 4; i++){
            acquired_units[i] = 0;
        }
    }

    public void add_child(int child_index){
        this.children.add(child_index);
    }
    public LinkedList<Integer> get_children(){
        if (this.children == null){
            return null;
        }
        LinkedList<Integer> temp = this.children;
        return temp;
    }

    public void remove_child(Integer child_index){
        this.children.remove(child_index);
    }

    public void add_resource(int resource_index){
        this.resources.add(resource_index);
    }

    public void remove_resource(Integer resource_index){
        this.resources.remove(resource_index);
    }

    public boolean has_child(Integer child_index){
        return this.children.contains(child_index);
    }
}

