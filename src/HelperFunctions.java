import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Filip on 04.04.2017.
 */
public class HelperFunctions implements Runnable {
    private static boolean[][] hadaMatrix;
    private CyclicBarrier barrier1;

    public HelperFunctions(boolean[][] hada, CyclicBarrier barrier1) {
        this.barrier1 = barrier1;
        hadaMatrix = hada;
    }

    public HelperFunctions(boolean[][] walishMatrix) {
        transportMatrix(walishMatrix);
    }


    private void transportMatrix(boolean[][] matrix){
        boolean[][] transportedMatrix = new boolean[matrix.length][matrix.length];
        for(int i = 0; i < matrix.length;i++){
            for(int j = 0; j < matrix.length;j++){
                transportedMatrix[i][j] = matrix[j][i];
            }
        }
        matrixMultiplikation(matrix,transportedMatrix);
    }




    private void matrixMultiplikation(boolean[][] matrix1,boolean[][] matrix2){
        int[][] calculateMultiplikation = new int[matrix1.length][matrix2[0].length];
        boolean[][] transformedMatrix = new boolean[matrix1.length][matrix2[0].length];
        int value1 = 0;
        int value2 = 0;
        int value3 = 0;
        for(int i = 0; i < matrix1.length; i++){
            for(int j =0; j< matrix2[0].length; j++){
                for(int k = 0; k < matrix1[0].length; k++){
                    value1 = (matrix1[i][k])? 1 : -1;
                    value2 = (matrix2[k][j])? 1 : -1;
                    calculateMultiplikation[i][j] += value1*value2;
                }
                value3 = calculateMultiplikation[i][j];
                if(value3 != 0 && (i != j)) return;
                else if(i == j && value3 == matrix1.length) transformedMatrix[i][j] = true;
                else if(i != j && value3 == 0) transformedMatrix[i][j] = false;
            }
        }
        MatrixPrint.printMatrix(transformedMatrix);
    }


    @Override
    public void run() {
        transportMatrix(hadaMatrix);
        try {
            barrier1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
