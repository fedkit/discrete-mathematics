import java.util.*;

public class GraphBase {
    static class Graph {
        private int countVertex;
        private LinkedList<Integer>[] graph;

        public Graph(int n) {
            countVertex = n;
            graph = new LinkedList[countVertex];
            for (int i = 0; i < countVertex; i++) {
                graph[i] = new LinkedList<>();
            }
        }

        public void addEdge(int vertex1, int vertex2) {
            graph[vertex1].add(vertex2);
        }
    }

    static class Condenser {
        private int countVertex;
        private HashMap<Integer, ArrayList<Integer>> condenser;

        public Condenser(int n) {
            countVertex = n;
            condenser = new HashMap<>();
        }

        public void addEdge(int vertex1, int vertex2) {
            condenser.putIfAbsent(vertex1, new ArrayList<>());
            condenser.get(vertex1).add(vertex2);
        }
    }


    private static void DFS(int v, boolean[] visited,
                            Stack<Integer> stack, LinkedList<Integer>[] adj) {
        visited[v] = true;
        for (int n : adj[v]) {
            if (!visited[n]) {
                DFS(n, visited, stack, adj);
            }
        }
        stack.push(v);
    }

    private static void DFSU2(int v, boolean[] visited,
                                LinkedList<Integer>[] adj, List<Integer> component) {
        visited[v] = true;
        component.add(v);
        for (int n : adj[v]) {
            if (!visited[n]) {
                DFSU2(n, visited, adj, component);
            }
        }
    }

    public static List<List<Integer>> getSCCs(Graph graph) {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[graph.countVertex];
        Arrays.fill(visited, false);
        for (int i = 0; i < graph.countVertex; i++) {
            if (!visited[i]) {
                DFS(i, visited, stack, graph.graph);
            }
        }
        Graph gTrans = new Graph(graph.countVertex);
        for (int v = 0; v < graph.countVertex; v++) {
            for (int i : graph.graph[v]) {
                gTrans.addEdge(i, v);
            }
        }
        Graph graphDop = gTrans;
        Arrays.fill(visited, false);
        List<List<Integer>> sccs = new ArrayList<>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                DFSU2(v, visited, graphDop.graph, component);
                sccs.add(component);
            }
        }
        return sccs;
    }

    public static Condenser transform(Graph graph, List<List<Integer>> sccs) {
        Map<Integer, Integer> nodeToSCC = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (int node : sccs.get(i)) {
                nodeToSCC.put(node, i);
            }
        }
        Condenser condenser = new Condenser(sccs.size());
        for (int u = 0; u < graph.countVertex; u++) {
            for (int v : graph.graph[u]) {
                int uSCC = nodeToSCC.get(u);
                int vSCC = nodeToSCC.get(v);
                if (uSCC != vSCC) {
                    condenser.addEdge(uSCC, vSCC);
                }
            }
        }
        return condenser;
    }

    public static List<Integer> findBase(Graph graph) {
        List<List<Integer>> sccs = getSCCs(graph);
        Condenser condenser = transform(graph, sccs);
        int[] inDegree = new int[sccs.size()];
        Arrays.fill(inDegree, 0);
        for (int u : condenser.condenser.keySet()) {
            for (int v : condenser.condenser.get(u)) {
                inDegree[v]++;
            }
        }
        List<Integer> base = new ArrayList<>();
        for (int i = 0; i < sccs.size(); i++) {
            if (inDegree[i] == 0) {
                base.add(Collections.min(sccs.get(i)));
            }
        }
        Collections.sort(base);
        return base;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Graph graph = new Graph(n);
        int m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            int vertex1 = scanner.nextInt();
            int vertex2 = scanner.nextInt();
            graph.addEdge(vertex1, vertex2);
        }
        List<Integer> base = findBase(graph);
        for (int vertex : base) {
            System.out.print(vertex + " ");
        }
    }
}
