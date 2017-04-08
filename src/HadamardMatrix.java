import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SplittableRandom;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by filip on 07.04.2017.
 */
public class HadamardMatrix implements Runnable {
    private int size;
    private boolean[][] randomMatrix;
    private boolean[][] transportedMatrix;
    private int[][] identityCheckMatrix;

    public HadamardMatrix(int size ){
        this.size = size;

        randomMatrix = new boolean[size][size];
        transportedMatrix = new boolean[size][size];
        identityCheckMatrix = new int[size][size];

        createRandomAndTransportMatrix();
    }

    public void setRandomMatrix(boolean[][] givenMatrix){
        this.size = givenMatrix.length;
        for(int i = 0; i < givenMatrix.length; i++){
            for(int j = 0; j < givenMatrix.length; j++){
                randomMatrix[i][j] = givenMatrix[i][j];
                transportedMatrix[j][i] = givenMatrix[i][j];
            }
        }
    }

    public void createRandomAndTransportMatrix(){
        SplittableRandom random = new SplittableRandom();
        for(int i = 0; i < size;i++){
            for(int j = 0; j < size; j++){
                randomMatrix[i][j] = random.nextBoolean();
                transportedMatrix[j][i] = randomMatrix[i][j];
            }
        }
    }

    private void matrixMultiplication(){
        int valueOfRandomMatrix = 0;
        int valueOfTransportedMatrix = 0;
        int valueOfIdentity = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    valueOfRandomMatrix = (randomMatrix[i][k])? 1 : -1;
                    valueOfTransportedMatrix = (transportedMatrix[k][j])? 1 : -1;
                    identityCheckMatrix[i][j] += valueOfRandomMatrix *valueOfTransportedMatrix;
                }
                valueOfIdentity = identityCheckMatrix[i][j];
                //Checking if calculated value is correct.
                //For an Identity Matrix ( which should be the outcome of H(m) * H^T(m)) all values must be 0,
                //except for the main diagonal where all values must be n*1 (in our case 668).
                //If we find an other value we can cancel the calculation at this point.
                if(valueOfIdentity != 0 && i != j) return;
                else if(valueOfIdentity != size && i == j) return;
            }
        }
        String filename = "D:\\test\\filename.txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for(int i = 0; i < size;i++){
                for(int j = 0; j < size;j++){
                    if(randomMatrix[i][j]) bw.write("*");
                    else bw.write("-");
                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Matrix found");
        MatrixPrint.printMatrix(randomMatrix);
    }

    @Override
    public void run() {
        matrixMultiplication();
    }
}
