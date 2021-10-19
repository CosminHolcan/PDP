import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private int id;
    private int value;
    private ReentrantLock mutex;
    private ArrayList<Node> parents;
    private ArrayList<Node> children;

    public Node(int id, int value) {
        this.id = id;
        this.value = value;
        this.mutex = new ReentrantLock();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public Node(int id) {
        this.id = id;
        this.value = 0;
        this.mutex = new ReentrantLock();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public int getValue() {
        this.mutex.lock();
        int toReturn = value;
        this.mutex.unlock();

        return toReturn;
    }

    public void modifyValue(int newValue) {
        this.mutex.lock();
        if (this.parents.size() == 0)
            this.children.forEach(node -> node.modifyValue(newValue - this.value));
        else
            this.children.forEach(node -> node.modifyValue(newValue));

        if (this.parents.size() == 0)
        {
            System.out.println("Node with id "+id+" changed : old value "+value+" -> new value "+newValue);
            this.value = newValue;
        }
        else
            this.value += newValue;
        this.mutex.unlock();
    }

    public int getId() {
        return id;
    }

    public void addChild(Node node)
    {
        this.children.add(node);
    }

    public void addParent(Node node)
    {
        this.parents.add(node);
        this.value = this.value + node.value;
    }

    public boolean isConsistent(){
        if (this.parents.size() == 0)
            return true;

        return  this.value == this.parents.stream().reduce(0, (partialSum,node)-> partialSum + node.value, Integer::sum);
    }

    public void lock()
    {
        this.mutex.lock();
    }

    public void unlock()
    {
        this.mutex.unlock();
    }
}
