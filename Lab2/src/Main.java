import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> vector1 = new ArrayList<>(Arrays.asList(1,2,3,4,1,4,2,6,5,2,8,4,3,6,1,5,8,6,7));
        ArrayList<Integer> vector2 = new ArrayList<>(Arrays.asList(8,7,5,6,1,2,3,5,6,8,4,2,6,9,5,3,2,4,5));
        var size = vector1.size();

        ProducerConsumerQueue producerConsumerQueue = new ProducerConsumerQueue();

        Thread producer = new Thread(new Producer(producerConsumerQueue, vector1, vector2));
        Thread consumer = new Thread(new Consumer(producerConsumerQueue, size));

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
            producerConsumerQueue.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
