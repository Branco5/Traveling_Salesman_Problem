package Advanced;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Define métodos e atributos do algoritmo a correr por cada thread
 */
public class Algorithm {
    private final Random rand;
    private final int[][] matrix;
    private final int popSize;
    private final int swapChance;


    public Algorithm(String file, int popSize, int swapChance){
        rand = new Random();
        this.popSize = popSize;
        matrix = convertArray(getMatrixFromFile(file));
        this.swapChance = swapChance;
    }

    /**
     * Algoritmo a executar por cada thread
     * Remover print final para ver apenas resultados finais
     */
    public void runAlgorithm(Worker thread, Global global) throws Exception {

        List<Path> threadPaths = thread.getPaths();

        int parent1[] = thread.getPaths().get(0).getPath();
        int parent2[] = thread.getPaths().get(1).getPath();

        int child1[] = new int[matrix.length];
        int child2[] = new int[matrix.length];

        pmxCrossover(parent1, parent2, child1, child2, matrix.length, rand);
        swap(child1, swapChance);
        swap(child2, swapChance);
        thread.getPaths().add(new Path(child1, matrix));
        thread.getPaths().add(new Path(child2, matrix));
        removeWorstPaths(thread.getPaths());
        thread.incrementIterations();

        if(threadPaths.get(0).getDistance()<thread.getBestDistance()){
            thread.setTime(System.currentTimeMillis() - global.getStartTime());
            thread.setIterationBest(thread.getIterations());
            thread.setBestPath(threadPaths.get(0));

            //Remover para ver apenas resultados finais
            System.out.println("Best path: " + thread.getBestDistance() +
                    " found in " + thread.getTime() + " milliseconds" + " by "+thread.getName());
        }

    }

    public List<Path> initPaths(){

        List<Path> ps = new ArrayList<>();

        for(int i = 0; i< popSize; i++){
            Path path = initialPath();
            path = randomPath(path);
            path.setDistance(matrix);
            ps.add(i,path);
        }

        sortPaths(ps);

        return ps;
    }

    /**
     * Sorts paths from best to worst according to distance
     */
    public static void sortPaths(List<Path> paths){
        paths.sort(Comparator.comparing(Path::getDistance));
    }

    public void removeWorstPaths(List<Path> paths){
        sortPaths(paths);
        int size = paths.size();
        paths.remove(size-1);
        paths.remove(size-2);
    }

    public Path initialPath(){
        int[] path = new int[matrix.length];

        for(int i=0; i<matrix.length;i++){
            path[i]=i+1;
        }

        return new Path(path);
    }

    public Path randomPath(Path path){
        int size = path.size();
        for (int i = 0; i<size; i++){
            int a = rand.nextInt(size) % size;
            int tmp = path.getPath()[i];
            path.getPath()[i]=path.getPath()[a];
            path.getPath()[a]=tmp;
        }
        return path;
    }

    /**
     * Swaps 2 random elements of a path according to swap chance desired
     */
    public void swap(int[] path, int percentage) throws Exception {
        int size = path.length;
        if(rollDice(percentage)){
            int a = rand.nextInt(size) % size;
            int b = rand.nextInt(size) % size;
            int tmp = path[a];
            path[a]=path[b];
            path[b]=tmp;
        }
    }

    /**
     * Returns true if random number generated is below percentage in parameter
     * Auxiliary to swap method
     */
    private boolean rollDice(int percentage) throws Exception {
        return rand.nextInt(100) < percentage;
    }

    /**
     * Generates matrix from file
     */
    public static String[][] getMatrixFromFile(String filename) {
        String name = "tsp_testes/" + filename;
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            int size = Integer.parseInt(String.valueOf(br.readLine()));
            System.out.println("Matrix size: "+size);
            String[][] matrix = new String[size][size];

            int count = 0;
            while (count<size) {
                line = br.readLine();
                String[] values = line.split("\\s+");
                List<String> list = new ArrayList<>(Arrays.asList(values));
                if(list.get(0).equals("")){
                    for(int i=0;i< values.length-1;i++){
                        list.set(i, list.get(i+1));
                    }
                    list.remove(size-1);
                }
                matrix[count] = list.toArray(new String[0]);
                count++;
            }
            return matrix;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts String array to int array
     */
    public int[][] convertArray(String arr[][]){
        int size = arr.length;
        int[][] aux = new int[size][size];
        for (int i = 0; i < size; i++) {
            for(int j=0; j<size; j++){
                aux[i][j] = Integer.parseInt(arr[i][j]);
            }
        }
        return aux;
    }

    public int getPopSize() {
        return popSize;
    }

    static void pmxCrossover(int parent1[],int parent2[],
                             int offSpring1[],int offSpring2[],int n,Random rand) {
        int replacement1[] = new int[n+1];
        int replacement2[] = new int[n+1];
        int i, n1, m1, n2, m2;
        int swap;

        int cuttingPoint1 = rand.nextInt(n);
        int cuttingPoint2 = rand.nextInt(n);

        while (cuttingPoint1 == cuttingPoint2) {
            cuttingPoint2 = rand.nextInt(n);
        }
        if (cuttingPoint1 > cuttingPoint2) {
            swap = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = swap;
        }

        for (i=0; i < n+1; i++) {
            replacement1[i] = -1;
            replacement2[i] = -1;
        }

        for (i=cuttingPoint1; i <= cuttingPoint2; i++) {
            offSpring1[i] = parent2[i];
            offSpring2[i] = parent1[i];
            replacement1[parent2[i]] = parent1[i];
            replacement2[parent1[i]] = parent2[i];
        }

        for (i = 0; i < n; i++) {
            if ((i < cuttingPoint1) || (i > cuttingPoint2)) {
                n1 = parent1[i];
                m1 = replacement1[n1];
                n2 = parent2[i];
                m2 = replacement2[n2];
                while (m1 != -1) {
                    n1 = m1;
                    m1 = replacement1[m1];
                }
                while (m2 != -1) {
                    n2 = m2;
                    m2 = replacement2[m2];
                }
                offSpring1[i] = n1;
                offSpring2[i] = n2;
            }
        }
    }
}

