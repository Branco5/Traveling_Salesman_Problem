package Advanced;

import java.util.Arrays;

/**
 * Defines attributes and methods of individual paths
 */
public class Path {
    private int[] path;
    private int distance;

    public Path(int[] path, int matrix[][]){
        this.path=path;
        setDistance(matrix);
    }

    public Path(int[] path){
        this.path=path;
    }

    public void setDistance(int matrix[][]){
        int dist = 0;
        int size = path.length;

        for(int i = 0; i<size-1; i++){
            int cur = path[i]-1;
            int next = path[i+1]-1;
            dist+=matrix[cur][next];
        }
        int last = path[size-1]-1;
        int first = path[0]-1;
        dist += matrix[last][first];

        this.distance = dist;
    }

    public int getDistance(){
        return distance;
    }

    public int size(){
        return path.length;
    }

    public int[] getPath(){
        return path;
    }

    @Override
    public String toString() {
        return "Path{" + Arrays.toString(path) +
                ", distance=" + distance +
                '}';
    }
}
