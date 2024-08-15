import java.util.Scanner;

public class MapRoute {
    public static int sizeMatrix;
    public static int[][] mapRoute;
    public static boolean[][] usedDot;
    public static int[][] mapPoint;
    public static class Dot {
        private int x, y;
        public Dot(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static class PriorityQueue {
        private Dot[] heap;
        private int size;
        public PriorityQueue(int capacity) {
            heap = new Dot[capacity];
            size = 0;
        }
        private void swap(int i, int j) {
            Dot t = heap[i];
            heap[i] = heap[j];
            heap[j] = t;
        }
        private void heapifyUp(int index) {
            int parent = (index - 1) / 2;
            while (index > 0 && mapPoint[heap[index].x][heap[index].y] < mapPoint[heap[parent].x][heap[parent].y]) {
                swap(parent, index);
                index = parent;
                parent = (index - 1) / 2;
            }
        }
        public void push(int x, int y) {
            Dot k = new Dot(x, y);
            heap[size] = k;
            heapifyUp(size);
            size++;
        }
        private void heapifyDown(int index) {
            int smallest = index;
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            if (leftChild < size &&
                    mapPoint[heap[leftChild].x][heap[leftChild].y] < mapPoint[heap[smallest].x][heap[smallest].y]) {
                smallest = leftChild;
            }
            if (rightChild < size &&
                    mapPoint[heap[rightChild].x][heap[rightChild].y] < mapPoint[heap[smallest].x][heap[smallest].y]) {
                smallest = rightChild;
            }
            if (smallest != index) {
                swap(smallest, index);
                heapifyDown(smallest);
            }
        }
        public Dot pop() {
            if (size == 0) {
                return new Dot(-1, -1);
            }
            Dot root = heap[0];
            heap[0] = heap[size - 1];
            size--;
            heapifyDown(0);
            return root;
        }
    }
    public static int findMinRoad(int[][] matrix) {
        PriorityQueue priorityQueue = new PriorityQueue(sizeMatrix * sizeMatrix);
        priorityQueue.push(1, 1);
        mapPoint[1][1] = matrix[1][1];
        while (true) {
            Dot k = priorityQueue.pop();
            if (k.x == -1) break;
            usedDot[k.x][k.y] = true;
            int[][] neighbors = {{k.x - 1, k.y}, {k.x, k.y - 1}, {k.x + 1, k.y}, {k.x, k.y + 1}};
            for (int[] i : neighbors) {
                int newX = i[0];
                int newY = i[1];
                if (newX >= 1 && newX <= sizeMatrix - 2 && newY >= 1 && newY <= sizeMatrix - 2
                                                                        && !usedDot[newX][newY]) {
                    int newDist = mapPoint[k.x][k.y] + matrix[newX][newY];
                    if (newDist < mapPoint[newX][newY] || mapPoint[newX][newY] == 0) {
                        mapPoint[newX][newY] = newDist;
                        priorityQueue.push(newX, newY);
                    }
                }
            }
        }
        return mapPoint[sizeMatrix - 2][sizeMatrix - 2];
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean allZero = true;
        int n = scanner.nextInt();
        sizeMatrix = n + 2;
        mapRoute = new int[sizeMatrix][sizeMatrix];
        usedDot = new boolean[sizeMatrix][sizeMatrix];
        mapPoint = new int[sizeMatrix][sizeMatrix];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                mapRoute[i][j] = scanner.nextInt();
                if (mapRoute[i][j] != 0){
                    allZero = false;
                }
            }
        }
        if(allZero){
            System.out.println(0);
        } else {
            int answer = findMinRoad(mapRoute);
            System.out.println(answer);
        }
    }
}
