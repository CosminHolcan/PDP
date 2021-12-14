import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CycleFinder implements Runnable {
    private Graph graph;
    private int startingNode;
    private List<Integer> path;
    private AtomicBoolean result;
    private List<Integer> hamiltonianCycle;
    private Lock lock;

    CycleFinder(Graph graph, int startingNode, AtomicBoolean result, List<Integer> hamiltonianCycle) {
        this.graph = graph;
        this.startingNode = startingNode;
        this.path = new ArrayList<>();
        this.result = result;
        this.hamiltonianCycle = hamiltonianCycle;
        this.lock = new ReentrantLock();
    }

    private void visit(int source) {
        path.add(source);
        if (!result.get()) {
            if (path.size() == graph.getTotalVertices()) {
                if (graph.getVerticesStartingFrom(source).contains(startingNode)) {
                    result.set(true);
                    this.lock.lock();
                    this.path.add(startingNode);
                    hamiltonianCycle.clear();
                    hamiltonianCycle.addAll(this.path);
                    this.lock.unlock();
                }
                return;
            }

            List<Integer> nodesToVisit = graph.getVerticesStartingFrom(source);
            for (int target : nodesToVisit) {
                if (!this.path.contains(target)) {
                    visit(target);
                }
            }
        }
    }

    @Override
    public void run() {
        visit(startingNode);
    }
}
