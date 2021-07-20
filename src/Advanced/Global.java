package Advanced;


/**
 * Functions as central memory and thread coordinator
 */
public class Global {
    private final Worker[] workers;
    private final Algorithm aj;
    private long startTime;
    private final double mergeRate; // must be < 0.5 as the last merge is never executed
    private final long duration;
    private Path bestPath;
    private long bestTime;
    private Worker bestWorker;
    private int bestDistance;
    static boolean doWork = true; //When false pause execution of worker threads

    public Global(String file, int nrWorkers, int duration, int population, int swapChance, double mergeRate){
        this.workers = new Worker[nrWorkers];
        this.aj = new Algorithm(file, population, swapChance);
        this.mergeRate = mergeRate > 0 ? mergeRate : 1;
        this.duration = duration*1000;
        bestPath=null;
        bestDistance=999999999;
        bestTime=999999999;
        bestWorker = null;

        for (int i = 0; i<workers.length; i++) {
            String name = "Thread-"+i;
            Worker thread = new Worker(this);
            thread.setName(name);
            workers[i] = thread;
        }
    }

    public void startWorkers(){
        startTime = System.currentTimeMillis();
        for(int i = 0; i< workers.length; i++){
            workers[i].start();
        }
    }

    /**
     * Stops workers and prints best results
     */
    public void stopWorkers() throws InterruptedException {

        System.out.println("\nSTOPPING WORKERS\n");
        for(int i = 0; i<workers.length; i++){
            workers[i].interrupt();
        }
        for(int i = 0; i<workers.length; i++){
            workers[i].join();
        }
        print();
    }

    public void print(){
        for(Worker w : workers){
            System.out.println("Best path found by "+w.getName()+": "+ w.getBestDistance() +
                    " in " + w.getTime() + " milliseconds and "+w.getIterationBest()+" iterations");
        }
        System.out.println();
        System.out.println("Best path found:\n" + bestPath + "\nwith distance: " + getBestDistance() +
                "\nby "+bestWorker.getName()+" in "+ bestTime + " milliseconds and "+ bestWorker.getIterationBest()+" iterations");

    }

    public synchronized int getBestDistance(){
        return this.bestDistance;
    }
    public synchronized long getBestTime(){
        return this.bestTime;
    }
    /**
     * Updates central data with the data of the thread with the best path
     */
    public synchronized void setBestPath(Worker thread){
        if(thread.getBestDistance() < getBestDistance() || (thread.getBestDistance() == getBestDistance() && thread.getTime()<getBestTime())){
                bestWorker = thread;
                bestPath = thread.getBestPath();
                bestTime = thread.getTime();
                bestDistance= thread.getBestDistance();
        }
    }

    public Worker[] getWorkers(){
        return workers;
    }

    /**
     * Returns period of merge rate
     */
    public double getPeriod(){
        return mergeRate * duration;
    }

    public Algorithm getAlgorithm(){
        return aj;
    }


    public long getDuration(){
        return duration;
    }

    public long getStartTime(){
        return startTime;
    }

}

