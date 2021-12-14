import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        Graph graph = Graph.generateRandomHamiltonianGraph(50, 1000);
//        Graph graph = Graph.generateNotHamiltonianGraph(100);
//        System.out.println(graph.getEdges());
        List<Integer> hamiltonianCycle = new ArrayList<>();
        AtomicBoolean result = new AtomicBoolean(false);

        for (int index = 0; index < graph.getTotalVertices(); index++){
            pool.submit(new CycleFinder(graph, index, result, hamiltonianCycle));
        }

        pool.shutdown();

        try {
            pool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (result.get()){
            System.out.println("Hamiltonian cycle is "+hamiltonianCycle);
        }
        else {
            System.out.println("There is no hamiltonian cycle");
        }
    }
}
