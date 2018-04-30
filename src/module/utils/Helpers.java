package module.utils;

import module.Algorithms.Algorithm;
import module.Algorithms.GeneticAlgorithm;
import module.Algorithms.SimulatedAnnealing;

import java.util.*;

import static javax.swing.UIManager.put;

public class Helpers {

    public final static Random random = new Random(
            Seeder.SEED
    );

    public final static Map<String, Class<? extends Algorithm>> algorithms;
    static
    {
        algorithms = new LinkedHashMap<>();
        algorithms.put("Genetic Algorithm", GeneticAlgorithm.class);
        algorithms.put("Simulated Annealing", SimulatedAnnealing.class);
    }

    public final static Map<String, Map<String,String>> algoSettings = new LinkedHashMap<String, Map<String,String>>() {
        {
            put("Genetic Algorithm", new LinkedHashMap<String,String>() {
                {
                    put("Max iterations", "10000");
                    put("Max capacity for each truck", "100");
                }
            });
            put("Simulated Annealing", new LinkedHashMap<String,String>() {
                {
                    put("Max iterations", "100000");
                    put("Max capacity for each truck", "100");
                    put("Initial temperature", "100");
                    put("mu", "0.9999");
                }
            });
        }
    };

    public final static Map<String, String> getAlgoInfo(Algorithm algo)
    {
        Map<String,String> info = new LinkedHashMap<>();

        if(algo instanceof  GeneticAlgorithm)
        {
            info.put("Step: ", Integer.toString(algo.getSteps()));
        }
        else if(algo instanceof  SimulatedAnnealing)
        {
            double fitness = algo.getCurrentSolution().getFitness();
            info.put("Step: ", Integer.toString(algo.getSteps()));
            info.put("Current temperature: ", Double.toString(((SimulatedAnnealing)algo).getCurrentTemperature()));
            info.put("Current solution: ", (fitness == Double.MAX_VALUE ? "Not possible" : Double.toString(fitness)));
        }


        return info;
    }

    public final static void makeChangeToAlgorithm(Algorithm algo, String key, String value)
    {
        if(key.equals("Max iterations"))
        {
            algo.setMaxStep((int)Double.parseDouble(value));
        }
        else if(key.equals("Max capacity for each truck"))
        {
            algo.setMAX_CAPACITY((int)Double.parseDouble(value));
        }
        else if(algo instanceof  GeneticAlgorithm)
        {

        }
        else if(algo instanceof SimulatedAnnealing)
        {
            if(key.equals("mu"))
            {
                ((SimulatedAnnealing)algo).setMu(Double.parseDouble(value));
            }
            if(key.equals("Initial temperature"))
            {
                ((SimulatedAnnealing)algo).setCurrentTemperature(Double.parseDouble(value));
            }
        }
        algo.forceGFXpdate();
    }
}
