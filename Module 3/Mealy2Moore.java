import java.util.*;

public class Mealy2Moore {
    public static class State {
        int stateId;
        int outputSignal;
        State(int stateId, int outputSignal) {
            this.stateId = stateId;
            this.outputSignal = outputSignal;
        }
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;
            State state = (State) o;
            return stateId == state.stateId && outputSignal == state.outputSignal;
        }
        public int hashCode() {
            return Objects.hash(stateId, outputSignal);
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int countSymbolsInput = scanner.nextInt();
        String[] symbolsInput = new String[countSymbolsInput];
        for (int i = 0; i < countSymbolsInput; i++) {
            symbolsInput[i] = scanner.next();
        }
        int countSymbolsOutput = scanner.nextInt();
        String[] symbolsOutput = new String[countSymbolsOutput];
        for (int i = 0; i < countSymbolsOutput; i++) {
            symbolsOutput[i] = scanner.next();
        }
        int n = scanner.nextInt();
        int[][] delta = new int[n][countSymbolsInput];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < countSymbolsInput; j++) {
                delta[i][j] = scanner.nextInt();
            }
        }
        int[][] fi = new int[n][countSymbolsInput];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < countSymbolsInput; j++) {
                fi[i][j] = scanner.nextInt();
            }
        }
        Map<State, Integer> mooreStateMap = new HashMap<>();
        List<State> mooreStates = new ArrayList<>();
        int stateCounter = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < countSymbolsInput; j++) {
                int nextState = delta[i][j];
                int outputSignal = fi[i][j];
                State nextStateOutput = new State(nextState, outputSignal);
                if (!mooreStateMap.containsKey(nextStateOutput)) {
                    mooreStateMap.put(nextStateOutput, stateCounter++);
                    mooreStates.add(nextStateOutput);
                }
            }
        }
        System.out.println("digraph {");
        System.out.println("    rankdir = LR");
        for (State state : mooreStates) {
            int stateId = mooreStateMap.get(state);
            int mooreState = state.stateId;
            String mooreOutput = symbolsOutput[state.outputSignal];
            System.out.print("    ");
            System.out.print(stateId);
            System.out.print(" [label = \"(");
            System.out.print(mooreState);
            System.out.print(",");
            System.out.print(mooreOutput);
            System.out.println(")\"]");
        }
        for (State state : mooreStates) {
            int stateId = mooreStateMap.get(state);
            int mooreState = state.stateId;
            for (int j = 0; j < countSymbolsInput; j++) {
                int nextState = delta[mooreState][j];
                int outputSignal = fi[mooreState][j];
                State nextStateId = new State(nextState, outputSignal);
                int nextStateIdValue = mooreStateMap.get(nextStateId);
                System.out.print("    ");
                System.out.print(stateId);
                System.out.print(" -> ");
                System.out.print(nextStateIdValue);
                System.out.print(" [label = \"");
                System.out.print(symbolsInput[j]);
                System.out.println("\"]");
            }
        }
        System.out.println("}");
    }
}
