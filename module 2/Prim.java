import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class Prim {
    public static class Pair {
        private int value;
        private int priority;
        public Pair(int value, int priority) {
            this.value = value;
            this.priority = priority;
        }
    }
    public static class PriorityQueue {
        private Pair[] heap;
        private int size;
        public PriorityQueue(int capacity) {
            heap = new Pair[capacity];
            size = 0;
        }
        public  void swap(int i, int j) {
            Pair t = heap[i];
            heap[i] = heap[j];
            heap[j] = t;
        }
        public void heapifyUp(int index) {
            int parent = (index - 1) / 2;
            while (index > 0 && heap[index].priority < heap[parent].priority) {
                swap(parent, index);
                index = parent;
                parent = (index - 1) / 2;
            }
        }
        public void push(int value, int priority) {
            Pair k = new Pair(value, priority);
            heap[size] = k;
            heapifyUp(size);
            size++;
        }
        public void heapifyDown(int index) {
            int smallest = index;
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            if (leftChild < size && heap[leftChild].priority < heap[smallest].priority) {
                smallest = leftChild;
            }
            if (rightChild < size && heap[rightChild].priority < heap[smallest].priority) {
                smallest = rightChild;
            }
            if (smallest != index) {
                swap(smallest, index);
                heapifyDown(smallest);
            }
        }
        public Pair pop() {
            Pair root = heap[0];
            heap[0] = heap[size - 1];
            size--;
            heapifyDown(0);
            return root;
        }
    }
    public static class Graph {
        private int countVisitedVertex;
        private int[] vertex;
        private boolean[] visitedVertex;
        private HashMap<Integer, ArrayList<int[]>> graph;
        public Graph(int n) {
            vertex = new int[n];
            countVisitedVertex = 0;
            visitedVertex = new boolean[n];
            graph = new HashMap<>();
            for (int i = 0; i < n; i++) {
                vertex[i] = i;
                visitedVertex[i] = false;
                graph.put(i, new ArrayList<>());
            }
        }
        public void add(int v1, int v2, int d) {
            if (v1 >= 0 && v1 < vertex.length && v2 >= 0 && v2 < vertex.length) {
                graph.get(v1).add(new int[]{v2, d});
                graph.get(v2).add(new int[]{v1, d});
            }
        }
        private void visitVertex(int v) {
            if (!visitedVertex[v]) {
                visitedVertex[v] = true;
                countVisitedVertex++;
            }
        }
    }
    public static int prim(Graph graph) {
        int answer = 0;
        PriorityQueue pq = new PriorityQueue(10000000);
        pq.push(0, 0);
        while (graph.countVisitedVertex != graph.vertex.length) {
            Pair pair = pq.pop();
            int v = pair.value;
            int priority = pair.priority;
            if (!graph.visitedVertex[v]) {
                graph.visitVertex(v);
                answer += priority;
                ArrayList<int[]> edges = graph.graph.get(v);
                for (int[] edge : edges) {
                    int to = edge[0];
                    int weight = edge[1];
                    if (!graph.visitedVertex[to]) {
                        pq.push(to, weight);
                    }
                }
            }
        }
        return answer;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Graph graph = new Graph(n);
        for (int i = 0; i < m; i++) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            int d = scanner.nextInt();
            graph.add(v1, v2, d);
        }
        scanner.close();
        int answer = prim(graph);
        System.out.println(answer);
    }
}
