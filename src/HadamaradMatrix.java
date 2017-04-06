/**
 * Created by Filip on 04.04.2017.
 */
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class HadamaradMatrix implements Runnable{
    public boolean[][] hadaMatrix;
    private CyclicBarrier barrier1;
    private CyclicBarrier barrier2;

    public HadamaradMatrix(int size,CyclicBarrier barrier1,CyclicBarrier barrier2){
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
        hadaMatrix = new boolean[size][size];
    }

    public void createHadamaradMatrix(){
        Random myRand = new Random();
        for(int i = 0; i < hadaMatrix.length; i++) {
            for (int j = 0; j < hadaMatrix[0].length; j++) {
                hadaMatrix[i][j] = (myRand.nextInt(2) == 1) ? true : false;
            }
        }
    }

    @Override
    public void run() {
        createHadamaradMatrix();
        try {
            this.barrier1.await();
            this.barrier2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
