import java.util.ArrayList;
import java.util.Scanner;

public class Dividers {
    private static ArrayList<Long> buildDividers(long n){
        ArrayList<Long> ans = new ArrayList<>();
        for(long i = 1; i * i <= n; i++){
            if(n % i == 0){
                ans.add(i);
                if(n / i != i){
                    ans.add(n / i);
                }
            }
        }
        return ans;
    }
    private static void swap(ArrayList<Long> div, int i, int j) {
        long temp = div.get(i);
        div.set(i, div.get(j));
        div.set(j, temp);
    }
    private static boolean prime(long x){
        if(x <= 1)
            return false;
        for(long i = 2; i * i <= x; i++){
            if(x % i == 0){
                return false;
            }
        }
        return true;
    }
    private static StringBuilder DOTAnswer(ArrayList<Long> div){
        int n = div.size();
        StringBuilder ans = new StringBuilder("graph {\n");
        for(int i = 0; i < n; i++){
            ans.append("\t").append(div.get(i)).append("\n");
        }
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                long number1 = div.get(i);
                long number2 = div.get(j);
                if(number2 % number1 == 0 && prime(number2 / number1) ||
                        (number1 % number2 == 0 && prime(number1 / number2))){
                    ans.append("\t").append(number1).append("--").append(number2).append("\n");
                }
            }
        }
        ans.append("}");
        return ans;
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        long n = scan.nextLong();
        scan.close();
        ArrayList<Long> dividers = buildDividers(n);
        StringBuilder s = DOTAnswer(dividers);
        System.out.println(s);
    }
}
