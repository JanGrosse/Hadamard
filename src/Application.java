/**
 * Created by Filip on 04.04.2017.
 */
import java.util.ArrayList;
import java.util.concurrent.*;


public class Application {
    private static int counter = 0;

    public static void main(String[]args){

        // Starting HSQL Database
  //      ConnectDatabase.instance.startup();
 //       ConnectDatabase.instance.init();

        // Starting calculation of Walish Matrix
   //     initWalishMatrix();
        // Starting random creation of 1.000.000.000 668 x 668 Matrix
        try {
            initHadaMatrix();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shutting down HSQL Database
 //        ConnectDatabase.instance.shutdown();
    }




    public static void initWalishMatrix(){
        ArrayList<WalishMatrix> walishMatrixes = new ArrayList<>();


        // Barrier Action running when all the Threads reached the barrier.
        Runnable barrierAction = new Runnable() {
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
        CyclicBarrier barrier = new CyclicBarrier(7,barrierAction);

        // Creating the 7 Parties
        WalishMatrix wm1 = new WalishMatrix(1);
        wm1.setBarriers(barrier);
        walishMatrixes.add(wm1);
        WalishMatrix wm2 = new WalishMatrix(2);
        wm2.setBarriers(barrier);
        walishMatrixes.add(wm2);
        WalishMatrix wm3 = new WalishMatrix(3);
        wm3.setBarriers(barrier);
        walishMatrixes.add(wm3);
        WalishMatrix wm4 = new WalishMatrix(4);
        wm4.setBarriers(barrier);
        walishMatrixes.add(wm4);
        WalishMatrix wm5 = new WalishMatrix(5);
        wm5.setBarriers(barrier);
        walishMatrixes.add(wm5);
        WalishMatrix wm6 = new WalishMatrix(6);
        wm6.setBarriers(barrier);
        walishMatrixes.add(wm6);
        WalishMatrix wm7 = new WalishMatrix(7);
        wm7.setBarriers(barrier);
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
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4,8,0L,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>());


        // Creating 4 instances of our HadamardMatrix class
        ArrayList<HadamardMatrix> matrixList = new ArrayList<>();
        HadamardMatrix hm1 = new HadamardMatrix(668);
        HadamardMatrix hm2 = new HadamardMatrix(668);
        HadamardMatrix hm3 = new HadamardMatrix(668);
        HadamardMatrix hm4 = new HadamardMatrix(668);

        matrixList.add(hm1);
        matrixList.add(hm2);
        matrixList.add(hm3);
        matrixList.add(hm4);
        // Starting our four Threads
        HadamardMatrix matrixDummy;
        for(int j = 0; j < 250000000;j++){
            for(int i = 0; i < 4; i++){
                matrixDummy = matrixList.get(i);
                threadPool.execute(matrixDummy);
                matrixDummy.createRandomAndTransportMatrix();
            }
        }
        threadPool.shutdown();
    }
}

// Testing purpose
/*

        HadamardMatrix hmTest = new HadamardMatrix(64);
        WalishMatrix wm = new WalishMatrix(6);
        CyclicBarrier wmBarrier = new CyclicBarrier(2);
        wm.setBarriers(wmBarrier);
        new Thread(wm).start();
        wmBarrier.await();
        hmTest.setRandomMatrix(wm.getWalishMatrix());
        new Thread(hmTest).start();

 */