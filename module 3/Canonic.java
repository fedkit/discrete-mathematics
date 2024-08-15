import java.util.*;

public class Canonic {
    private static void dfs(int start, int[][] delta, boolean[] visited, List<Integer> canonical) {
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                visited[v] = true;
                canonical.add(v);
                for (int i = delta[v].length - 1; i >= 0; i--) {
                    stack.push(delta[v][i]);
                }
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int q0 = scanner.nextInt();
        int[][] delta = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                delta[i][j] = scanner.nextInt();
            }
        }
        String[][] fi = new String[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                fi[i][j] = scanner.next();
            }
        }
        List<Integer> canonical = new ArrayList<>();
        boolean[] visited = new boolean[n];
        dfs(q0, delta, visited, canonical);
        Map<Integer, Integer> canonicalMapping = new HashMap<>();
        for (int i = 0; i < canonical.size(); i++) {
            canonicalMapping.put(canonical.get(i), i);
        }
        System.out.println(n);
        System.out.println(m);
        System.out.println(canonicalMapping.get(q0));
        for (int i = 0; i < canonical.size(); i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(canonicalMapping.get(delta[canonical.get(i)][j]) + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < canonical.size(); i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(fi[canonical.get(i)][j] + " ");
            }
            System.out.println();
        }
        scanner.close();
    }
}
