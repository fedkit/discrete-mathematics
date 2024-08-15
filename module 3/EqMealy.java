import java.util.*;

public class EqMealy {
    public static class MealyMachine {
        int n, m, q0;
        int[][] delta;
        String[][] fi;
        public MealyMachine(Scanner scanner) {
            this.n = scanner.nextInt();
            this.m = scanner.nextInt();
            this.q0 = scanner.nextInt();
            delta = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    delta[i][j] = scanner.nextInt();
                }
            }
            fi = new String[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    fi[i][j] = scanner.next();
                }
            }
        }
    }
    public static boolean equalMachine(MealyMachine machine1, MealyMachine machine2) {
        if (machine1.m != machine2.m) {
            return false;
        }
        Queue<int[]> stateQueue = new LinkedList<>();
        Set<String> visitedStates = new HashSet<>();
        stateQueue.add(new int[]{machine1.q0, machine2.q0});
        visitedStates.add(machine1.q0 + "," + machine2.q0);
        while (!stateQueue.isEmpty()) {
            int[] currentStatePair = stateQueue.poll();
            int s1 = currentStatePair[0];
            int s2 = currentStatePair[1];
            for (int i = 0; i < machine1.m; i++) {
                int currentState1 = machine1.delta[s1][i];
                String output1 = machine1.fi[s1][i];
                int currentState2 = machine2.delta[s2][i];
                String output2 = machine2.fi[s2][i];
                if (!output1.equals(output2)) {
                    return false;
                }
                String statePair = currentState1 + "," + currentState2;
                if (!visitedStates.contains(statePair)) {
                    stateQueue.add(new int[]{currentState1, currentState2});
                    visitedStates.add(statePair);
                }
            }
        }
        return true;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MealyMachine machine1 = new MealyMachine(scanner);
        MealyMachine machine2 = new MealyMachine(scanner);
        if (equalMachine(machine1, machine2)) {
            System.out.println("EQUAL");
        } else {
            System.out.println("NOT EQUAL");
        }
    }
}
