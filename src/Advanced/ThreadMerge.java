package Advanced;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ThreadMerge extends Thread {
    private Global global;
    private long period;

    public ThreadMerge(Global global){
        this.period = (long) global.getPeriod();
        this.global = global;
    }

    /**
     * Merges best paths of all threads and assigns to each thread in each iteration of desired period
     * Does not execute the last iteration
     * If rate >= 0.5, does not execute
     */
    @Override
    public void run() {
        long time = global.getStartTime()+ global.getDuration()-period*2;
        //System.out.println(period);
        while (System.currentTimeMillis() <= time) {
            try {
                sleep(period);
                Global.doWork=false;
                sleep(100);
                System.out.println("\nMERGING POPULATIONS");
                List<Path> merged = new ArrayList<>();
                for(Worker w : global.getWorkers()){
                    merged.addAll(w.getPaths());
                }
                merged.sort(Comparator.comparing(Path::getDistance));

                int size = global.getAlgorithm().getPopSize();

                for(Worker w : global.getWorkers()){
                    List<Path> newList = new ArrayList<>(merged.subList(0,size));
                    w.setPaths(newList);
                    //System.out.println(w.getName() + " - " + w.getPaths().size());
                }

                System.out.println("RESUMING WORK\n");
                Global.doWork=true;


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
