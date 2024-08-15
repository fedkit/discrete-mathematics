import java.util.*;

public class Loops {
    static class Graph {
        int n;
        List<Vertex> vertices;
        Graph() {
            this.vertices = new ArrayList<>();
        }
        static class Vertex {
            List<Vertex> ingoingEdges = new ArrayList<>();
            List<Vertex> edges = new ArrayList<>();
            List<Vertex> bucket = new ArrayList<>();
            Vertex ancestor, label, semiDominator, dominator, parent;
            boolean visited = false;
            boolean hasOperand = false;
            int depth = 0;
            int operandValue = 0;

            Vertex() {
                this.semiDominator = this;
                this.label = this;
            }
        }
        void constructGraph(Scanner scanner) {
            n = scanner.nextInt();
            if (n < 0) n = 0;
            for (int i = 0; i < n; i++) {
                vertices.add(new Vertex());
            }
            Map<Integer, Vertex> indexToVertex = new HashMap<>();
            for (int i = 0; i < n; i++) {
                int index = scanner.nextInt();
                if (index < 0) index = 0;
                String command = scanner.next();
                if (command == null) command = "";
                indexToVertex.put(index, vertices.get(i));
                if ("ACTION".equals(command)) {
                    linkVertex(i);
                } else if ("BRANCH".equals(command) || "JUMP".equals(command)) {
                    int operand = scanner.nextInt();
                    if (operand < 0) operand = 0;
                    vertices.get(i).hasOperand = true;
                    vertices.get(i).operandValue = operand;
                    if ("BRANCH".equals(command)) {
                        linkVertex(i);
                    }
                }
            }
            linkOperands(indexToVertex);
        }
        void linkVertex(int index) {
            if (index < vertices.size() - 1) {
                vertices.get(index).edges.add(vertices.get(index + 1));
                if (vertices.get(index + 1) != null) {
                    vertices.get(index + 1).ingoingEdges.add(vertices.get(index));
                }
            }
        }
        void linkOperands(Map<Integer, Vertex> indexToVertex) {
            for (Vertex vertex : vertices) {
                if (vertex.hasOperand) {
                    if (indexToVertex.get(vertex.operandValue) != null) {
                        vertex.edges.add(indexToVertex.get(vertex.operandValue));
                        indexToVertex.get(vertex.operandValue).ingoingEdges.add(vertex);
                    }
                }
            }
        }
        int getLoopsCount() {
            int[] depth = {0};
            if (vertices.get(0) != null) {
                visitVerticesDFS(vertices.get(0), depth);
            }
            filterAndProcessVertices();
            dominators();
            int loopsCount = 0;
            for (Vertex vertex : vertices) {
                for (Vertex u : vertex.ingoingEdges) {
                    Vertex temp = u;
                    while (temp != vertex && temp != null) {
                        temp = temp.dominator;
                    }
                    if (temp == vertex) {
                        loopsCount++;
                        break;
                    }
                }
            }
            return loopsCount;
        }
        void visitVerticesDFS(Vertex start, int[] depth) {
            Stack<Vertex> stack = new Stack<>();
            if (start != null) {
                stack.push(start);
            }
            while (!stack.isEmpty()) {
                Vertex vertex = stack.pop();
                if (!vertex.visited) {
                    vertex.depth = depth[0];
                    vertex.visited = true;
                    depth[0]++;
                    for (Vertex u : vertex.edges) {
                        if (!u.visited) {
                            u.parent = vertex;
                            stack.push(u);
                        }
                    }
                }
            }
        }
        void filterAndProcessVertices() {
            List<Vertex> filteredGraph = new ArrayList<>();
            for (Vertex vertex : vertices) {
                if (vertex.visited) {
                    filteredGraph.add(vertex);
                }
            }
            vertices.clear();
            if (filteredGraph.size() > 0) {
                vertices.addAll(filteredGraph);
            }
            for (Vertex vertex : vertices) {
                List<Vertex> filteredIngoingEdges = new ArrayList<>();
                for (Vertex u : vertex.ingoingEdges) {
                    if (u.visited) {
                        filteredIngoingEdges.add(u);
                    }
                }
                vertex.ingoingEdges = filteredIngoingEdges;
            }
        }
        void dominators() {
            if (vertices.size() > 0) {
                quickSort(vertices, Comparator.comparingInt(a -> a.depth));
            }
            int n = vertices.size();
            for (int i = n - 1; i > 0; i--) {
                Vertex vertex = vertices.get(i);
                for (Vertex u : vertex.ingoingEdges) {
                    Vertex minVertex = findMin(u);
                    if (minVertex.semiDominator.depth < vertex.semiDominator.depth) {
                        vertex.semiDominator = minVertex.semiDominator;
                    }
                }
                vertex.ancestor = vertex.parent;
                vertex.semiDominator.bucket.add(vertex);
                for (Vertex u : vertex.parent.bucket) {
                    Vertex minVertex = findMin(u);
                    if (minVertex.semiDominator == u.semiDominator) {
                        u.dominator = vertex.parent;
                    } else {
                        u.dominator = minVertex;
                    }
                }
                vertex.parent.bucket.clear();
            }
            for (int i = 1; i < n; i++) {
                if (vertices.get(i).dominator != vertices.get(i).semiDominator) {
                    vertices.get(i).dominator = vertices.get(i).dominator.dominator;
                }
            }
            if (vertices.size() > 0) {
                vertices.get(0).dominator = null;
            }
        }
        void quickSort(List<Vertex> graph, Comparator<Vertex> comparator) {
            Stack<Integer> stack = new Stack<>();
            stack.push(0);
            stack.push(graph.size() - 1);
            while (!stack.isEmpty()) {
                int end = stack.pop();
                int start = stack.pop();
                if (start < end) {
                    int pivotIndex = partition(graph, start, end, comparator);
                    if (pivotIndex - 1 > start) {
                        stack.push(start);
                        stack.push(pivotIndex - 1);
                    }
                    if (pivotIndex + 1 < end) {
                        stack.push(pivotIndex + 1);
                        stack.push(end);
                    }
                }
            }
        }
        int partition(List<Vertex> graph, int start, int end, Comparator<Vertex> comparator) {
            Vertex pivot = graph.get(end);
            int i = start - 1;
            for (int j = start; j < end; j++) {
                if (comparator.compare(graph.get(j), pivot) <= 0) {
                    i++;
                    Collections.swap(graph, i, j);
                }
            }
            Collections.swap(graph, i + 1, end);
            return i + 1;
        }
        Vertex findMin(Vertex vertex) {
            if (vertex != null) {
                searchAndCut(vertex);
            }
            return vertex.label;
        }
        Vertex searchAndCut(Vertex vertex) {
            Deque<Vertex> stack = new ArrayDeque<>();
            Vertex current = vertex;
            while (current.ancestor != null) {
                stack.push(current);
                current = current.ancestor;
            }
            Vertex root = current;
            while (!stack.isEmpty()) {
                Vertex v = stack.pop();
                if (v.ancestor.label.semiDominator.depth < v.label.semiDominator.depth) {
                    v.label = v.ancestor.label;
                }
                v.ancestor = root;
            }
            return root;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph();
        if (graph != null) {
            graph.constructGraph(scanner);
            System.out.println(graph.getLoopsCount());
        }
    }
}
