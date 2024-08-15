import java.util.*;

public class Ideal {
    private static class Graph {
        private HashMap<Integer, ArrayList<int[]>> graph;
        public Graph(int countVertex) {
            graph = new HashMap<>();
            for (int i = 0; i < countVertex; i++) {
                graph.put(i, new ArrayList<>());
            }
        }
        private void add(int vertex1, int vertex2, int colour) {
            int[] edge1 = {vertex1, colour};
            int[] edge2 = {vertex2, colour};
            graph.get(vertex1).add(edge2);
            graph.get(vertex2).add(edge1);
        }
    }
    static int lexicographicalCompare(List<Integer> list1, List<Integer> list2) {
        int size1 = list1.size();
        int size2 = list2.size();
        int minSize = Math.min(size1, size2);
        for (int i = 0; i < minSize; i++) {
            if ((list1.get(i)  - list2.get(i)) != 0) {
                return list1.get(i)  - list2.get(i);
            }
        }
        return 0;
    }
    static void bfs(int start, int finish, int[] d, List<List<Integer>> path, Graph graph) {
        Queue<Integer> q = new LinkedList<>();
        d[start] = 0;
        q.add(start);
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int[] edge : graph.graph.get(v)) {
                int u = edge[0];
                int color = edge[1];
                if (d[u] == Integer.MAX_VALUE) {
                    d[u] = d[v] + 1;
                    q.add(u);
                    path.set(u, new ArrayList<>(path.get(v)));
                    path.get(u).add(color);
                } else if (d[u] == d[v] + 1) {
                    List<Integer> newPath = new ArrayList<>(path.get(v));
                    newPath.add(color);
                    if (lexicographicalCompare(newPath, path.get(u)) < 0) {
                        path.set(u, newPath);
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Graph graph = new Graph(n);
        for (int i = 0; i < m; i++) {
            int vertex1 = scanner.nextInt() - 1;
            int vertex2 = scanner.nextInt() - 1;
            int colour = scanner.nextInt();
            graph.add(vertex1, vertex2, colour);
        }
        int[] distances = new int[n];
        for (int i = 0; i < n; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        List<List<Integer>> paths = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            paths.add(new ArrayList<>());
        }
        bfs(0, n - 1, distances, paths, graph);
        System.out.println(distances[n - 1]);
        for (int color : paths.get(n - 1)) {
            System.out.print(color + " ");
        }
        System.out.println();
    }
}
