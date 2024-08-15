import java.util.*;

//Экзаменационная задача
public class Curricula {
    public static class Graph {
        public static class Vertex {
            public boolean visited;
            public int number;
            public ArrayList<Vertex> neighbors;
            public Vertex(int k) {
                this.number = k;
                visited = false;
                neighbors = new ArrayList<>();
            }
        }
        public static int countVertex;
        public static Vertex[] graph;
        public Graph(int n) {
            countVertex = n;
            graph = new Vertex[n];
            for (int i = 0; i < n; i++) {
                graph[i] = new Vertex(i);
            }
        }
        public void addVertex(int from, int to) {
            graph[from].neighbors.add(graph[to]);
        }
    }
    private static boolean detectCycleUtil(Graph.Vertex vertex, boolean[] visited, boolean[] recStack) {
        if (recStack[vertex.number]) {
            return true;
        }
        if (visited[vertex.number]) {
            return false;
        }
        visited[vertex.number] = true;
        recStack[vertex.number] = true;
        for (Graph.Vertex neighbor : vertex.neighbors) {
            if (detectCycleUtil(neighbor, visited, recStack)) {
                return true;
            }
        }
        recStack[vertex.number] = false;
        return false;
    }
    public static boolean detectCycle(Graph graph) {
        boolean[] visited = new boolean[graph.countVertex];
        boolean[] rec = new boolean[graph.countVertex];
        for (int i = 0; i < graph.countVertex; i++) {
            if (detectCycleUtil(graph.graph[i], visited, rec)) {
                return true;
            }
        }
        return false;
    }
    public static int findSemesters(Graph graph) {
        int[] semester = new int[graph.countVertex];
        Arrays.fill(semester, -1);
        Queue<Graph.Vertex> queue = new LinkedList<>();
        int[] inDegree = new int[graph.countVertex];
        for (Graph.Vertex vertex : graph.graph) {
            for (Graph.Vertex neighbor : vertex.neighbors) {
                inDegree[neighbor.number]++;
            }
        }
        for (int i = 0; i < graph.countVertex; i++) {
            if (inDegree[i] == 0) {
                queue.add(graph.graph[i]);
                semester[i] = 1;
            }
        }
        while (!queue.isEmpty()) {
            Graph.Vertex v = queue.poll();
            for (Graph.Vertex neighbor : v.neighbors) {
                inDegree[neighbor.number]--;
                if (inDegree[neighbor.number] == 0) {
                    queue.add(neighbor);
                }
                semester[neighbor.number] = Math.max(semester[neighbor.number], semester[v.number] + 1);
            }
        }
        int maxSemester = 0;
        for (int sem : semester) {
            maxSemester = Math.max(maxSemester, sem);
        }
        return maxSemester;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Graph graph = new Graph(n);
        for (int i = 0; i < n; i++) {
            int m = scanner.nextInt();
            for (int j = 0; j < m; j++) {
                int prerequisite = scanner.nextInt();
                graph.addVertex(prerequisite - 1, i);
            }
        }
        scanner.close();
        if (detectCycle(graph)) {
            System.out.println("cycle");
        } else {
            System.out.println(findSemesters(graph));
        }
    }
}
