public class Consumer implements Runnable{
    private ProducerConsumerQueue producerConsumerQueue;
    private int sizeVector;
    private Integer value;

    public Consumer(ProducerConsumerQueue producerConsumerQueue, int sizeVector)
    {
        this.producerConsumerQueue = producerConsumerQueue;
        this.sizeVector = sizeVector;
    }

    @Override
    public void run() {
        int sum = 0;
        for(int index=0; index< sizeVector; index++)
        {
            int val = this.producerConsumerQueue.dequeue();
            System.out.println("Receive value "+val);
            sum += val;
        }
        System.out.println("Sum is "+sum);
    }
}
