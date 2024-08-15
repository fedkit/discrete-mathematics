import java.util.Scanner;
import java.util.ArrayList;

public class BridgeNum {
    private static boolean[] used;
    private static int[] up;
    private static int timer = 0;
    private static int[] tin;
    private static int ans;
    private static void dfs(ArrayList<Integer>[] graph, int v, int p){
        used[v] = true;
        tin[v] = timer;
        up[v] = timer;
        timer++;
        for(int i : graph[v]){
            if(i == p){
                continue;
            } else if(used[i]){
                up[v] = Math.min(up[v], tin[i]);
            } else {
                dfs (graph, i, v);
                up[v] = Math.min(up[v], up[i]);
                if (up[i] > tin[v]){
                    ans++;
                }
            }
        }
    }
    private static void findBridge(ArrayList<Integer>[] graph){
        ans = 0;
        for(int i = 0; i < graph.length; i++){
            if(!used[i]){
                dfs(graph, i, -1);
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] graph = new ArrayList[n];
        used = new boolean[n];
        up = new int[n];
        tin = new int[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            used[i] = false;
            up[i] = 0;
            tin[i] = 0;
        }
        for (int i = 0; i < m; i++) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            graph[v1].add(v2);
            graph[v2].add(v1);
        }
        findBridge(graph);
        System.out.println(ans);
    }
}
