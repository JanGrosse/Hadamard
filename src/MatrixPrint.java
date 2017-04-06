/**
 * Created by Filip on 04.04.2017.
 */
public class MatrixPrint {
    public MatrixPrint(){

    }

    public static void printMatrix(boolean[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                if(matrix[i][j]) System.out.print("*");
                else System.out.print("-");
            }
            System.out.println();
        }
    }
}
