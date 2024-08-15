import java.util.Scanner;
import java.util.Comparator;
import java.util.Arrays;

public class Kruskal {
    public static int countVertex;
    public static int[][] vertex;
    public static double[][] edgeAndVertex;
    public static double answer;

    public static void kruskal(double[][] graph) {
        int[][] usedVertex = new int[countVertex][2];
        for (int i = 0; i < countVertex; i++) {
            usedVertex[i][0] = i;
            usedVertex[i][1] = -1;
        }
        int k = 0;
        int countEdge = 0;
        for (int i = 0; i < countVertex * (countVertex - 1) / 2; i++) {
            if (usedVertex[(int) graph[i][1]][1] == -1 && usedVertex[(int) graph[i][2]][1] == -1) {
                answer += graph[i][0];
                usedVertex[(int) graph[i][1]][1] = k;
                usedVertex[(int) graph[i][2]][1] = k;
                k++;
                countEdge++;
            } else if (usedVertex[(int) graph[i][1]][1] == -1 && usedVertex[(int) graph[i][2]][1] != -1) {
                answer += graph[i][0];
                usedVertex[(int) graph[i][1]][1] = usedVertex[(int) graph[i][2]][1];
                countEdge++;
            } else if (usedVertex[(int) graph[i][1]][1] != -1 && usedVertex[(int) graph[i][2]][1] == -1) {
                answer += graph[i][0];
                usedVertex[(int) graph[i][2]][1] = usedVertex[(int) graph[i][1]][1];
                countEdge++;
            } else if (usedVertex[(int) graph[i][1]][1] != usedVertex[(int) graph[i][2]][1]) {
                answer += graph[i][0];
                int oldComponent = usedVertex[(int) graph[i][1]][1];
                int newComponent = usedVertex[(int) graph[i][2]][1];
                for (int j = 0; j < countVertex; j++) {
                    if (usedVertex[j][1] == oldComponent) {
                        usedVertex[j][1] = newComponent;
                    }
                }
                countEdge++;
            }
            if (countEdge == countVertex - 1) {
                break;
            }
        }
    }

    public static double calculateLength(int[] point1, int[] point2) {
        return Math.sqrt(Math.pow((point1[0] - point2[0]), 2) + Math.pow((point1[1] - point2[1]), 2));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        if (n == 2) {
            int x1 = scanner.nextInt();
            int y1 = scanner.nextInt();
            int x2 = scanner.nextInt();
            int y2 = scanner.nextInt();
            answer = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        } else {
            countVertex = n;
            vertex = new int[n][2];
            edgeAndVertex = new double[n * (n - 1) / 2][3];
            for (int i = 0; i < n; i++) {
                vertex[i][0] = scanner.nextInt();
                vertex[i][1] = scanner.nextInt();
            }
            int index = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    edgeAndVertex[index][0] = calculateLength(vertex[i], vertex[j]);
                    edgeAndVertex[index][1] = i;
                    edgeAndVertex[index][2] = j;
                    index++;
                }
            }
            Arrays.sort(edgeAndVertex, Comparator.comparingDouble(a -> a[0]));
            kruskal(edgeAndVertex);
        }
        System.out.printf("%.2f%n", answer);
    }
}
