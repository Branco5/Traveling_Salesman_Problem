package Advanced;


public class ThreadWait extends Thread {
    private Global global;

    public ThreadWait(Global global) {
        this.global = global;
    }

    /**
     * Sleeps for duration desired then stops program execution
     */
    @Override
    public void run(){
        try {
            sleep(global.getDuration());
            Global.doWork=false;
            global.stopWorkers();
            System.out.print("\nAlgorithm finished\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



