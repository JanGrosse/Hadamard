import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Filip on 04.04.2017.
 */
public class WalishMatrix implements Runnable {
    private int size;
    private boolean[][] walishMatrix;
    private CyclicBarrier barrier;

    public WalishMatrix(int powerOf){
        this.size = powerOf;
    }

    public void setBarriers(CyclicBarrier barrier){
        this.barrier = barrier;
    }
    public int getSize(){
        return this.size;
    }
    private boolean[][] createWalishMatrix(int size){
        // The "standard" Hadamarad-Matrix with only one Item is just one single true H(1) = | * |
        if(size <= 0){
            return new boolean[][] {{true}};
        } else {
            // Like a recursive fibonacci function we go back till we reach our defined H(1)
            boolean[][] recursiveMatrix = createWalishMatrix(size -1);
            int dimensions = recursiveMatrix.length;
            /*
            The scheme is relatively easy to understand - if H(1) is a Hadamarad Matrix,
            than H(2) = | H(1)  H(1) |
                        | H(1) -H(1) |
            will also be a Hadamarad Matrix.
            And H(4) =  | H(2)  H(2) |
                        | H(2) -H(2) |
            and so on. So we just build up recursively from our H(1) and always double the size of our Matrix.
            */
            boolean[][] hamadaran = new boolean[2*dimensions][2*dimensions];
            for(int i = 0; i < dimensions; ++i){
                for(int j = 0; j < dimensions; ++j){
                    hamadaran[i][j] = recursiveMatrix[i][j];
                    hamadaran[i][j+dimensions] = recursiveMatrix[i][j];
                    hamadaran[i+dimensions][j] = recursiveMatrix[i][j];
                    // Since we have the negation of H(x) in our right corner, we have to negate
                    // our array entries as well to match the pattern
                    hamadaran[i+dimensions][j+dimensions] = !recursiveMatrix[i][j];
                }
            }
            return hamadaran;
        }
    }

    @Override
    public String toString(){
        StringBuilder walishMatrixString = new StringBuilder();
        for(int i = 0; i < this.walishMatrix.length; i++){
            for(int j = 0; j < this.walishMatrix.length; j++){
                if(this.walishMatrix[i][j]) walishMatrixString.append("*");
                else walishMatrixString.append("-");
            }
            walishMatrixString.append(" ");
        }
        return walishMatrixString.toString();
    }

    @Override
    public void run() {
        walishMatrix = createWalishMatrix(this.size);
        try {
            this.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
