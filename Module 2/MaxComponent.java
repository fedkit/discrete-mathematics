import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;

public class MaxComponent {
    public static ArrayList<ArrayList<Integer>> connectivityComponents = new ArrayList<>();
    public static int minVertex(ArrayList<Integer> component) {
        int minVertex = Integer.MAX_VALUE;
        for (int vertex : component) {
            if (vertex < minVertex) {
                minVertex = vertex;
            }
        }
        return minVertex;
    }
    public static int indexLargestComponent(Graph graph) {
        int indexAns = -1;
        int sizeNow = -1;
        int edgeCountNow = -1;
        int minVertexNow = Integer.MAX_VALUE;
        for (int i = 0; i < connectivityComponents.size(); i++) {
            ArrayList<Integer> component = connectivityComponents.get(i);
            int size = component.size();
            int edgeCount = calculateEdgeCount(component, graph);
            if (size > sizeNow || (size == sizeNow && edgeCount > edgeCountNow) ||
                    (size == sizeNow && edgeCount == edgeCountNow && minVertex(component) < minVertexNow)) {
                sizeNow = size;
                edgeCountNow = edgeCount;
                minVertexNow = minVertex(component);
                indexAns = i;
            }
        }
        return indexAns;
    }
    public static int calculateEdgeCount(ArrayList<Integer> component, Graph graph) {
        int edgeCount = 0;
        for (int vertex : component) {
            int neighbors = graph.graph.get(vertex).size();
            edgeCount += neighbors;
        }
        return edgeCount / 2;
    }
    public static void dfs(Graph graph, int startVertex, ArrayList<Integer> buf) {
        Stack<Integer> stack = new Stack<>();
        stack.push(startVertex);
        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!graph.visitedVertex[vertex]) {
                graph.visitedVertex[vertex] = true;
                buf.add(vertex);
                for (int neighbor : graph.graph.get(vertex)) {
                    if (!graph.visitedVertex[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
    }
    public static void outputFormatDOT(ArrayList<Integer> component, Graph graph) {
        System.out.println("graph {");
        for (int i = 0; i < graph.vertex.length; i++) {
            if (component.contains(i)) {
                System.out.println("    " + i + " [color = red]");
            } else {
                System.out.println("    " + i);
            }
        }
        for (int i = 0; i < graph.vertex.length; i++) {
            ArrayList<Integer> neighbors = graph.graph.get(i);
            for (int neighbor : neighbors) {
                if (neighbor > i && component.contains(i) && component.contains(neighbor)) {
                    System.out.println("    " + i + " -- " + neighbor + " [color = red]");
                } else if (neighbor > i) {
                    System.out.println("    " + i + " -- " + neighbor);
                }
            }
        }
        System.out.println("}");
    }

    public static class Graph {
        private int[] vertex;
        private boolean[] visitedVertex;
        private HashMap<Integer, ArrayList<Integer>> graph;

        public Graph(int n) {
            vertex = new int[n];
            visitedVertex = new boolean[n];
            graph = new HashMap<>();
            for (int i = 0; i < n; i++) {
                vertex[i] = i;
                visitedVertex[i] = false;
                graph.put(i, new ArrayList<>());
            }
        }

        public void add(int v1, int v2) {
            graph.get(v1).add(v2);
            graph.get(v2).add(v1);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Graph graph = new Graph(n);
        for (int i = 0; i < m; i++) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            graph.add(v1, v2);
        }
        scanner.close();
        for (int i = 0; i < n; i++) {
            if (!graph.visitedVertex[i]) {
                ArrayList<Integer> buf = new ArrayList<>();
                dfs(graph, i, buf);
                connectivityComponents.add(buf);
            }
        }
        int largestComponentIndex = indexLargestComponent(graph);
        outputFormatDOT(connectivityComponents.get(largestComponentIndex), graph);
    }
}
