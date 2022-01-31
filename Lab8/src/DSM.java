import mpi.MPI;
import message.CloseMessage;
import message.Message;
import message.SubscribeMessage;
import message.UpdateMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DSM {

    public Map<String, Set<Integer>> subscribers;
    public int a, b, c;
    public static final Lock lock = new ReentrantLock();

    public DSM() {
        a = 0;
        b = 1;
        c = 2;
        subscribers = new ConcurrentHashMap<>();
        subscribers.put("a", new HashSet<>());
        subscribers.put("b", new HashSet<>());
        subscribers.put("c", new HashSet<>());
    }

    public void subscribeTo(String variable) {
        Set<Integer> subs = this.subscribers.get(variable);
        subs.add(MPI.COMM_WORLD.Rank());
        this.subscribers.put(variable, subs);
        this.sendAll(new SubscribeMessage(variable, MPI.COMM_WORLD.Rank()));
    }

    public void syncSubscription(String variable, int rank) {
        Set<Integer> subs = this.subscribers.get(variable);
        subs.add(rank);
        this.subscribers.put(variable, subs);
    }

    public void setVariable(String variable, int value) {
        if (variable.equals("a"))
            this.a = value;
        if (variable.equals("b"))
            this.b = value;
        if (variable.equals("c"))
            this.c = value;
    }

    public void updateVariable(String variable, int value) {
        lock.lock();
        this.setVariable(variable, value);
        Message message = new UpdateMessage(variable, value);
        this.sendToSubscribers(variable, message);
        lock.unlock();
    }

    public void compareAndExchange(String variable, int oldValue, int newValue) {
        if (variable.equals("a") && this.a == oldValue){
            updateVariable("a", newValue);
        }
        if (variable.equals("b")&& this.b == oldValue){
            updateVariable("b", newValue);
        }
        if (variable.equals("c") && this.c == oldValue){
            updateVariable("c", newValue);
        }
    }

    public void sendToSubscribers(String variable, Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() != i && subscribers.get(variable).contains(i))
                MPI.COMM_WORLD.Send(new Object[]{message}, 0, 1, MPI.OBJECT, i, 0);
        }
    }

    private void sendAll(Message message) {
        for (int i = 0; i < MPI.COMM_WORLD.Size(); i++) {
            if (MPI.COMM_WORLD.Rank() != i || message instanceof CloseMessage)
                MPI.COMM_WORLD.Send(new Object[]{message},0,1,MPI.OBJECT, i, 0);
        }
    }

    public void close() {
        this.sendAll(new CloseMessage());
    }

    public static void printDSMInfo(DSM dsm) {
        StringBuilder sb = new StringBuilder();
        sb.append("DSM Info for rank ").append(MPI.COMM_WORLD.Rank()).append("\n");
        sb.append("Values : a = ").append(dsm.a).append(" b = ").append(dsm.b).append(" c = ").append(dsm.c).append("\n");
        sb.append("Subscribers: \n");
        for (String variable : dsm.subscribers.keySet()) {
            sb.append(variable).append("->").append(dsm.subscribers.get(variable).toString()).append("\n");
        }
        System.out.println(sb);
    }

}