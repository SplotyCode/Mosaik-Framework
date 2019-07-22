import io.github.splotycode.mosaik.util.collection.ArrayUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class DistributionTest {

    public static final int TRIES = 5_000_000;

    private static ArrayList<Function<Integer, String>> providers = new ArrayList<>();
    private static ArrayList<Function<String, Integer>> hashFunctions = new ArrayList<>();

    public static void main(String[] args) {
        doTest(integer -> "hey.names." + integer);
        doTest(integer -> "hey.names." + ThreadLocalRandom.current().nextInt(4, 80000));

        String[] paths = new String[] {"name", "age", "a", "this_is_a_very_extremely_long_key"};
        doTest(integer -> {
            int id = ThreadLocalRandom.current().nextInt(4, 80000);
            return "db." + id + "." + paths[ThreadLocalRandom.current().nextInt(0, paths.length - 1)];
        });

        addAlgorithm(String::hashCode);
        addAlgorithm(s -> {
            int var1;
            return(var1 = s.hashCode()) ^ var1 >>> 16;
        });
        addAlgorithm(s -> {
            int h = s.hashCode();
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        });

        System.out.println("Algorithms: " + hashFunctions.size() + " hash functions " + providers.size() + " providers");
        System.out.println("Dataset: " + TRIES + " quarries");
        System.out.println();

        runTests(2);
        runTests(3);
        runTests(5);
        runTests(20);
        runTests(30);
        runTests(100);
        runTests(800);
    }

    public static void addAlgorithm(Function<String, Integer> algorith) {
        hashFunctions.add(algorith);
    }

    private static DecimalFormat df = new DecimalFormat("00.000");

    private static HashMap<Function<Integer, String>, String[]> dataSets = new HashMap<>();

    public static void runTests(int servers) {
        double optimal = TRIES / servers;
        System.out.println("Servers: " + servers + ": ");


        for (Function<Integer, String> provider : providers) {
            String[] dataSet = dataSets.computeIfAbsent(provider, pro -> {
                String[] newDataSet = new String[TRIES];
                for (int i = 0; i < TRIES; i++) {
                    newDataSet[i] = pro.apply(i);
                }
                return newDataSet;
            });

            int f = 0;
            for (Function<String, Integer> function : hashFunctions) {
                double[] split = new double[servers];
                for (int i = 0; i < TRIES; i++) {
                    String pathStr = dataSet[i];
                    int hash = function.apply(pathStr);
                    split[Math.abs(hash % servers)]++;
                }
                for (int i = 0; i < split.length; i++) {
                    split[i] = Math.abs(optimal - split[i]);
                }
                double av = ArrayUtil.sum(split) / 2 / TRIES * 100;
                System.out.print("F" + f + " -> " + av + " | ");
                f++;
            }
            //System.out.print(df.format(av / f / optimal * 100) + " | ");
            System.out.println();
        }
        System.out.println();
    }

    private static void doTest(Function<Integer, String> path) {
        providers.add(path);
    }

}
