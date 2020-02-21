package Manager;
import java.util.LinkedList;
public class List {
    LinkedList<Process> head = new LinkedList<Process>();

    public void insert(Process process){
        head.add(process);
    }

    public void insert_end(Process process){
        head.addLast(process);
    }
    public int size() {
        return head.size();
    }

    public Process getFirst() {
        return head.getFirst();
    }
    public void remove(Process j){
        head.remove(j);
    }
}
