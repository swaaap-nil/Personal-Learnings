import java.util.ArrayList;
import java.util.Scanner;

public class Subsequence {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int N = scanner.nextInt();
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = scanner.nextInt();
        }
        ArrayList<Integer> longestList = new ArrayList<>();
        track(arr, 0, new ArrayList<>(), longestList);
        System.out.println("The longest bitwise subsequence has length: " + longestList.size());
        System.out.println("The subsequence is: " + longestList);
        
        scanner.close();
    }

    static void track(int[] arr, int index, ArrayList<Integer> currentList, ArrayList<Integer> longestList){
        if(index >= arr.length){
            if (currentList.size() > longestList.size()) {
                System.out.println(currentList);
                longestList.clear();
                longestList.addAll(currentList);
            }
            return;
        }

        // Include the current element and check the condition
        boolean shouldInclude = false;
        if(currentList.isEmpty() || ((currentList.get(currentList.size() - 1) & arr[index]) * 2 < (currentList.get(currentList.size() - 1) | arr[index]))){
            shouldInclude = true;
        }

        if(shouldInclude){
            currentList.add(arr[index]);
            track(arr, index + 1, currentList, longestList);
            currentList.remove(currentList.size() - 1); // Backtrack to explore other subsequences
        }

        // Exclude the current element
        track(arr, index + 1, currentList, longestList);
    }
}
