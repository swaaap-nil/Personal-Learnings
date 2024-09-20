import java.util.Scanner;

public class GridPath {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int N = scanner.nextInt();
        int[][] arr = new int[N][2];
        for (int i = 0; i < arr.length; i++) {
            for(int j=0 ; j < arr[0].length ; j++){
                arr[i][j] = scanner.nextInt();
            }
        }
        int longest =  Integer.MIN_VALUE;
        for(int i = 0; i< arr.length; i++){
            int value = findOptimalPathFor(arr, 0, i,-1) ;
            if( value > longest)
            longest = value;
        }
        System.out.println(longest);
        scanner.close();
    }

    public static int findOptimalPathFor(int[][] arr, int row,int col,int prevValue){
        if(row>=arr.length) return 0;
        if(arr[row][col] <= prevValue ) return 0;
        int firstCol = arr[row][col] + findOptimalPathFor(arr, row+1, 0,arr[row][col]) ;
        int secondCol = arr[row][col] + findOptimalPathFor(arr, row+1, 1,arr[row][col]) ;
        return Math.max(firstCol,secondCol);
    }
}
