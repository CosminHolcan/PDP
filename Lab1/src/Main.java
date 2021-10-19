import java.util.ArrayList;

public class Main {
    private static int NUMBER_THREADS = 10000;

    public static void main(String[] args) {
        Graph graph = new Graph();
//        graph.createGraph("file1.txt");
        graph.readGraph("file1.txt");
        ArrayList<Thread> threads = new ArrayList<>();
        for (int threadIndex=0; threadIndex < NUMBER_THREADS; threadIndex++)
        {
            if (threadIndex%10==0)
            {
                Thread thread = new Thread(() -> {
                    graph.checkConsistency();
                    if (!graph.checkConsistency())
                        threads.forEach(Thread::stop);
                });
                threads.add(thread);
                continue;
            }
            Thread thread = new Thread(() -> {
                graph.changeRandomValuePrimaryNode();
            });
            threads.add(thread);
        }
        threads.forEach(thread -> thread.start());
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        new Thread(() -> graph.checkConsistency()).start();
    }
}
