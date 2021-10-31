import java.util.ArrayList;

public class Producer implements Runnable {
    private ProducerConsumerQueue producerConsumerQueue;
    private ArrayList<Integer> vector1;
    private ArrayList<Integer> vector2;

    public Producer(ProducerConsumerQueue producerConsumerQueue, ArrayList<Integer> vector1, ArrayList<Integer> vector2)
    {
        this.producerConsumerQueue = producerConsumerQueue;
        this.vector1 = vector1;
        this.vector2 = vector2;
    }


    @Override
    public void run() {
        int size = this.vector1.size();
        for (int index=0; index < size; index++)
        {
            System.out.println("Send product of "+this.vector1.get(index)+" and "+this.vector2.get(index));
            this.producerConsumerQueue.enqueue(this.vector1.get(index)*this.vector2.get(index));
        }
    }
}
