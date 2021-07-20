package Advanced;

import java.util.List;

/**
 * Class defining each thread that will be working independently from each other
 */
public class Worker extends Thread {

    private List<Path> paths;
    private Path bestPath;
    private final Global global;
    private long time; //time of best path found
    private int iterationBest; //iteration where best path found
    private int iterations;

    Worker(Global global) {
        this.global = global;
        paths = global.getAlgorithm().initPaths();
        bestPath = paths.get(0);
        time = 0;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while(Global.doWork){
                try {
                    global.getAlgorithm().runAlgorithm(this, global);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //update central path if this thread's path beats it
        global.setBestPath(this);
    }

    public int getIterationBest() {
        return iterationBest;
    }

    public void setIterationBest(int iterationBest) {
        this.iterationBest = iterationBest;
    }

    public int getIterations() {
        return iterations;
    }

    public void incrementIterations() {
        this.iterations++;
    }

    public int getPopSize() {
        return paths.size();
    }

    public List<Path> getPaths() {return paths;}

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public Path getBestPath() {
        return bestPath;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public int getBestDistance() {
        return bestPath.getDistance();
    }

    public void setBestPath(Path bestPath) {
        this.bestPath = bestPath;
    }

}
