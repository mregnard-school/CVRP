package module.utils;

import module.Algorithms.Algorithm;
import module.Algorithms.GeneticAlgorithm;
import module.Algorithms.SimulatedAnnealing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import static javax.swing.UIManager.put;

public class Helpers {

    public final static Random random = new Random(
            Seeder.SEED
    );

    public final static HashMap<String, Class<? extends Algorithm>> algorithms;
    static
    {
        algorithms = new HashMap<>();
        algorithms.put("Genetic Algorithm", GeneticAlgorithm.class);
        algorithms.put("Simulated Annealing", SimulatedAnnealing.class);
    }

    public final static Map<String, Map<String,String>> algoSettings = new HashMap<String, Map<String,String>>() {
        {
            put("Genetic Algorithm", new HashMap<String,String>() {
                {
                    put("Max iterations", "10000");
                }
            });
            put("Simulated Annealing", new HashMap<String,String>() {
                {
                    put("Max iterations", "100000");
                }
            });
        }
    };

    public final static void makeChangeToAlgorithm(Algorithm algo, String key, String value)
    {

    }
}
