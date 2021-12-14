import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.shuffle;

public class Graph {
    private int totalVertices;
    private List<List<Integer>> edges;

    public Graph(int totalVertices) {
        this.totalVertices = totalVertices;
        this.edges = new ArrayList<>();
        for (int index = 0; index < totalVertices; index++) {
            this.edges.add(new ArrayList<>());
        }
    }

    public int getTotalVertices() {
        return totalVertices;
    }

    public List<List<Integer>> getEdges() {
        return edges;
    }

    public List<Integer> getVerticesStartingFrom(int node) {
        return this.edges.get(node);
    }

    public static Graph generateRandomHamiltonianGraph(int totalVertices, int totalRandomEdges) {
        Graph graph = new Graph(totalVertices);

        List<Integer> nodes = new ArrayList<>();
        for(int index=0; index < totalVertices; index++)
            nodes.add(index);

        shuffle(nodes);

        for (int index = 0; index < totalVertices - 1; index++) {
            graph.edges.get(nodes.get(index)).add(nodes.get(index + 1));
        }
        graph.edges.get(nodes.get(totalVertices - 1)).add(nodes.get(0));

        for (int index =0; index < totalRandomEdges; index++){
            Random random = new Random();
            int source = random.nextInt(totalVertices - 1);
            int target = random.nextInt(totalVertices - 1);
            while (source == target || graph.getEdges().get(source).contains(target))
                target = random.nextInt(totalVertices - 1);
            graph.getEdges().get(source).add(target);
        }

        return graph;
    }

    public static Graph generateNotHamiltonianGraph(int totalVertices){
        Graph graph = new Graph(totalVertices);

        List<Integer> nodes = new ArrayList<>();
        for(int index=0; index < totalVertices; index++)
            nodes.add(index);

        shuffle(nodes);

        for (int index=1; index < totalVertices; index++)
            graph.edges.get(nodes.get(0)).add(nodes.get(index));

        for (int index=2; index < totalVertices; index++)
            graph.edges.get(nodes.get(1)).add(nodes.get(index));

        for (int index=3; index < totalVertices; index++)
            graph.edges.get(nodes.get(2)).add(nodes.get(index));

        return graph;
    }
}
