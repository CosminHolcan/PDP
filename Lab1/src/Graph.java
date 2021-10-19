import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class Graph {
    private int PRIMARY_NODES_NUMBER = 10000;
    private int SECONDARY_NODES_NUMBER = 30000;
    private int MAX_VALUE_FOR_INPUT_VARIABLE = 1000;
    private int MAX_NUMBER_PARENTS_NODE = 10;
    private int MIN_NUMBER_PARENTS_NODE = 2;
    private ArrayList<Node> nodes;

    public Graph()
    {
        this.nodes = new ArrayList<>();
    }

    public void createGraph(String fileName)
    {
        this.nodes = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        buffer.append(PRIMARY_NODES_NUMBER).append(" ").append(SECONDARY_NODES_NUMBER).append("\n");
        for (int id = 0; id < PRIMARY_NODES_NUMBER; id++)
        {
            int value = new Random().nextInt(MAX_VALUE_FOR_INPUT_VARIABLE + 1);
            Node node = new Node(id, value);
            buffer.append(value).append("\n");
            this.nodes.add(node);
        }
        int currentId = PRIMARY_NODES_NUMBER - 1;
        for (int indexSecondaryNode = 0; indexSecondaryNode < SECONDARY_NODES_NUMBER; indexSecondaryNode++)
        {
            currentId++;
            buffer.append(currentId).append(" ");
            Node node = new Node(currentId);
            int parentsNumber = new Random().nextInt(MAX_NUMBER_PARENTS_NODE - 1) + MIN_NUMBER_PARENTS_NODE;
            HashSet<Integer> parentsIds = new HashSet<>();
            for (int parentIndex = 0; parentIndex < parentsNumber; parentIndex++)
                parentsIds.add(new Random().nextInt(currentId-1));
            parentsIds.forEach(id -> {node.addParent(this.nodes.get(id)); this.nodes.get(id).addChild(node);});
            buffer.append(parentsIds.size()).append(" ");
            for (var id : parentsIds)
                buffer.append(id).append(" ");
            buffer.append("\n");
            this.nodes.add(node);
        }
        try {
            FileWriter file = new FileWriter(fileName);
            file.write(buffer.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readGraph(String fileName)
    {
        this.nodes = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PRIMARY_NODES_NUMBER = scanner.nextInt();
        SECONDARY_NODES_NUMBER = scanner.nextInt();
        for (int id = 0; id < PRIMARY_NODES_NUMBER; id++)
        {
            int value = scanner.nextInt();
            Node node = new Node(id, value);
            this.nodes.add(node);
        }
        for (int indexSecondaryNode = 0; indexSecondaryNode < SECONDARY_NODES_NUMBER; indexSecondaryNode++)
        {
            int currentId = scanner.nextInt();
            Node node = new Node(currentId);
            int parentsNumber = scanner.nextInt();
            for (int parentIndex=0; parentIndex < parentsNumber; parentIndex++)
            {
                int parentId = scanner.nextInt();
                node.addParent(this.nodes.get(parentId));
                this.nodes.get(parentId).addChild(node);
            }
            this.nodes.add(node);
        }
    }

    public boolean checkConsistency()
    {
        for (Node node : this.nodes)
        {
            node.lock();
        }
        for (Node node1 : this.nodes)
            if (!node1.isConsistent()) {
                System.out.println("We found inconsistency");
                for (Node node2 : this.nodes)
                {
                    node2.unlock();
                }
                return false;
            }
        System.out.println("The graph is consistent");
        for (Node node3 : this.nodes)
        {
            node3.unlock();
        }
        return true;
    }

    public void changeRandomValuePrimaryNode()
    {
        int nodeId = new Random().nextInt(PRIMARY_NODES_NUMBER);
        int newValue = new Random().nextInt(MAX_VALUE_FOR_INPUT_VARIABLE + 1);
        this.nodes.get(nodeId).modifyValue(newValue);
    }
}