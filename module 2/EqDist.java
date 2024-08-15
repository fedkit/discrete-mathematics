import java.util.*;

public class EqDist {
    static List<Integer>[] edges;
    static int[][] distancesToRoots;
    static void calculateDistances(int rootIndex, int rootRelativeIndex) {
        int[] queue = new int[edges.length];
        int front = 0, rear = 0;
        queue[rear++] = rootIndex;
        boolean[] visited = new boolean[edges.length];
        while (front != rear) {
            int index = queue[front++];
            visited[index] = true;
            for (int uIndex : edges[index]) {
                if (!visited[uIndex]) {
                    visited[uIndex] = true;
                    distancesToRoots[uIndex][rootRelativeIndex] = distancesToRoots[index][rootRelativeIndex] + 1;
                    queue[rear++] = uIndex;
                }
            }
        }
    }
    static boolean isEquidistant(int index) {
        int referenceDistance = distancesToRoots[index][0];
        for (int j = 1; j < distancesToRoots[index].length; j++) {
            if (distancesToRoots[index][j] != referenceDistance || referenceDistance == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        edges = new List[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            edges[v1].add(v2);
            edges[v2].add(v1);
        }
        int k = scanner.nextInt();
        distancesToRoots = new int[n][k];
        for (int i = 0; i < k; i++) {
            int index = scanner.nextInt();
            calculateDistances(index, i);
        }
        boolean hasEquidistant = false;
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (isEquidistant(i)) {
                answer.append(i).append("\n");
                hasEquidistant = true;
            }
        }
        if (!hasEquidistant) {
            System.out.println("-");
        } else {
            System.out.println(answer);
        }
    }
}
