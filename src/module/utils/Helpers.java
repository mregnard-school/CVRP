package module.utils;

import module.Algorithms.Algorithm;
import module.Algorithms.GeneticAlgorithm;
import module.Algorithms.SimulatedAnnealing;
import module.Solutions.Solution;

import java.util.*;
import java.util.stream.Stream;

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
                    put("Best Selection Rate", "0.2");
                    put("Crossover Rate", "0.7");
                    put("Mutation Rate", "0.8");
                    put("Population Size", "10");
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
            info.put("Best Selection Rate: ", Double.toString(((GeneticAlgorithm)algo).getBestSelectionRate()));
            info.put("Crossover Rate: ", Double.toString(((GeneticAlgorithm)algo).getCrossoverRate()));
            info.put("Mutation Rate: ", Double.toString(((GeneticAlgorithm)algo).getMutationRate()));
            info.put("Population Size: ", Double.toString(((GeneticAlgorithm)algo).getPopulationSize()));
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
            if(key.equals("Best Selection Rate"))
            {
                ((GeneticAlgorithm)algo).setBestSelectionRate(Double.parseDouble(value));
            }
            else if(key.equals("Crossover Rate"))
            {
                ((GeneticAlgorithm)algo).setCrossoverRate(Double.parseDouble(value));
            }
            else if(key.equals("Mutation Rate"))
            {
                ((GeneticAlgorithm)algo).setMutationRate(Double.parseDouble(value));
            }
            else if(key.equals("Population Size"))
            {
                ((GeneticAlgorithm)algo).setPopulationSize((int)Double.parseDouble(value));
            }
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

    public static Stream<Map.Entry<Double, Solution>> cumulativeSum(Stream<Map.Entry<Double, Solution>> stream) {
        List<Double> doubles = new ArrayList<>();
        doubles.add(0d);
       return stream.flatMap(entry -> {
            Map.Entry<Double, Solution> result;
            double sum = doubles.get(0) + entry.getKey();
            doubles.set(0, sum);
            result = new AbstractMap.SimpleEntry<>(sum, entry.getValue());
            return Stream.of(result);
        });
    }
}
