import Advanced.Global;
import Advanced.ThreadMerge;
import Advanced.ThreadWait;
import java.util.Scanner;

public class main {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String[] arr;

        System.out.println();
        System.out.println("Insert 'tsp2' and algorithm parameters in this order: " +
                "file - threads - duration(seconds) - population - swap chance(%) - merge rate(decimal)");
        System.out.print("Example: tsp2 dantzig42.txt 5 10 80 5 0.3\n> ");
        
        String command = scanner.nextLine().toLowerCase();
        arr = command.trim().split("\\s");

        int nrWorkers = Integer.parseInt(arr[2]);
        int duration = Integer.parseInt(arr[3]);
        int population = Integer.parseInt(arr[4]);
        int swap = Integer.parseInt(arr[5]);
        double merge = Double.parseDouble(arr[6]);

        Global adv = new Global(arr[1], nrWorkers, duration, population, swap, merge);
        ThreadMerge tm = new ThreadMerge(adv);
        ThreadWait tw = new ThreadWait(adv);

        adv.startWorkers();
        tw.start();
        tm.start();

    }
}



