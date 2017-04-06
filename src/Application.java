/**
 * Created by Filip on 04.04.2017.
 */
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Application {
    private static int counter = 0;

    public static void main(String[]args){

        // Starting HSQL Database
        ConnectDatabase.instance.startup();
        ConnectDatabase.instance.init();

        // Starting calculation of Walish Matrix
        initWalishMatrix();
        // Starting random creation of 1.000.000.000 668 x 668 Matrix
        try {
            initHadaMatrix();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shutting down HSQL Database
         ConnectDatabase.instance.shutdown();
    }




    public static void initWalishMatrix(){
        ArrayList<WalishMatrix> walishMatrixes = new ArrayList<>();
        Runnable barrier1Action = new Runnable() {
            @Override
            public void run()
            {
                System.out.println("All Threads are finished");
                System.out.println("Writing results into DB");
                for(WalishMatrix wm : walishMatrixes){
                    ConnectDatabase.instance.addData(wm.getSize(),wm.toString());
                }
            }
        };

        // Creating a barrier with 7 Parties ( 2^1 ... 2^7 )
        CyclicBarrier barrier1 = new CyclicBarrier(7,barrier1Action);
        WalishMatrix wm1 = new WalishMatrix(1);
        wm1.setBarriers(barrier1);
        walishMatrixes.add(wm1);
        WalishMatrix wm2 = new WalishMatrix(2);
        wm2.setBarriers(barrier1);
        walishMatrixes.add(wm2);
        WalishMatrix wm3 = new WalishMatrix(3);
        wm3.setBarriers(barrier1);
        walishMatrixes.add(wm3);
        WalishMatrix wm4 = new WalishMatrix(4);
        wm4.setBarriers(barrier1);
        walishMatrixes.add(wm4);
        WalishMatrix wm5 = new WalishMatrix(5);
        wm5.setBarriers(barrier1);
        walishMatrixes.add(wm5);
        WalishMatrix wm6 = new WalishMatrix(6);
        wm6.setBarriers(barrier1);
        walishMatrixes.add(wm6);
        WalishMatrix wm7 = new WalishMatrix(7);
        wm7.setBarriers(barrier1);
        walishMatrixes.add(wm7);

        // Starting the Threads
        new Thread(wm1).start();
        new Thread(wm2).start();
        new Thread(wm3).start();
        new Thread(wm4).start();
        new Thread(wm5).start();
        new Thread(wm6).start();
        new Thread(wm7).start();
    }


    private static void initHadaMatrix() throws BrokenBarrierException, InterruptedException {
        ArrayList<HadamaradMatrix> hadams = new ArrayList<>();
        CyclicBarrier mainbarrier = new CyclicBarrier(9);
        for(int j= 0; j < 250000000; j++){
            System.out.println("Iteration :"+j*4);
            CyclicBarrier barrier1 = new CyclicBarrier(5);
            hadams.clear();
            for(int i = 0; i < 4; i++){
                HadamaradMatrix hadaMa = new HadamaradMatrix(668,barrier1,mainbarrier);
                hadams.add(hadaMa);
                new Thread(hadaMa).start();
            }
            barrier1.await();
            for(int i = 0; i < 4; i++){
              HelperFunctions hf = new HelperFunctions(hadams.get(i).hadaMatrix,mainbarrier);
              new Thread(hf).start();
            }
            mainbarrier.await();
        }

    }
}
