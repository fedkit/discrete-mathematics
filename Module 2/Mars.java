import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mars {
    public static class Enemy {
        private final int x;
        private final int y;
        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }
    private static void findGroup(int currentIndex, List<Integer> bestGroup,
                                  int totalElements, int currentLength, int currentGroup, List<Enemy> enemies) {
        if ((currentLength >= totalElements / 2) || (currentIndex >= totalElements))
            return;
        currentGroup +=  (int) Math.pow(2, currentIndex);;
        int isValid = 1;
        for (Enemy enemy : enemies) {
            if (((currentGroup >> enemy.getX()) & 1) == ((currentGroup >> enemy.getY()) & 1)) {
                isValid = 0;
                break;
            }
        }
        int previousLength = currentLength;
        currentLength++;
        if ((isValid == 1) && (currentLength > bestGroup.size())) {
            bestGroup.clear();
            for (int i = 0; i < totalElements; i++) {
                if (((currentGroup >> i) & 1) != 0) {
                    bestGroup.add(i + 1);
                }
            }
        }
        findGroup(currentIndex + 1, bestGroup, totalElements, currentLength, currentGroup, enemies);
        currentGroup -=  (int) Math.pow(2, currentIndex);
        findGroup(currentIndex + 1, bestGroup, totalElements, previousLength, currentGroup, enemies);
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Enemy> enemies = new ArrayList<>();
        int n = scanner.nextInt();
        char[][] map = new char[n][n];
        List<Integer> group = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j] = scanner.next().charAt(0);
                if (map[i][j] == '+')
                    enemies.add(new Enemy(i, j));
            }
        }
        findGroup(0, group, n, 0, 0, enemies);
        if (group.isEmpty()) {
            System.out.println("No Solution");
        } else {
            for (int i : group) {
                System.out.print(i + " ");
            }
        }
    }
}
