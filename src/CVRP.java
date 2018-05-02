import module.Algorithms.Algorithm;
import module.Algorithms.GeneticAlgorithm;
import module.Algorithms.SimulatedAnnealing;
import module.Node;
import module.NodeReader;
import module.utils.Helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class CVRP {

	public static void main(String[] args) {
		Random random = Helpers.random;
		System.out.println(random.nextInt(56));



		//String str = "";
		//String str = getSimulationCSV();
		//String str = getAnotherSimulationCSV("data/data01.txt");
		//String str = differenceTest("data/data01.txt");
		String str = getSimulationCSVForGenetic();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("result2.csv"));
			writer.write(str);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



	}

	public static String differenceTest(String dataSet)
	{
		int nbTests = 10;

		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("##.00");

		sb.append("Data Set,");sb.append(dataSet);sb.append("\n\n");

		List<Algorithm> algorithms = new ArrayList<>();
		sb.append("Iteration,");

		int maxIterations = 1000;

		sb.append(",Test (BSR: 0.3 | CR: 0.6 | MR: 0.1 | Pop: 150)");
		GeneticAlgorithm algorithm = new GeneticAlgorithm(maxIterations, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(maxIterations);
		algorithm.setBestSelectionRate(0.3);
		algorithm.setCrossoverRate(0.6);
		algorithm.setMutationRate(0.1);
		algorithm.setPopulationSize(150);
		algorithms.add(algorithm);

		sb.append(",Test (BSR: 0.3 | CR: 0.6 | MR: 0.3 | Pop: 150)");
		algorithm = new GeneticAlgorithm(maxIterations, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(maxIterations);
		algorithm.setBestSelectionRate(0.3);
		algorithm.setCrossoverRate(0.6);
		algorithm.setMutationRate(0.3);
		algorithm.setPopulationSize(150);
		algorithms.add(algorithm);

		sb.append(",Test (BSR: 0.3 | CR: 0.6 | MR: 0.1 | Pop: 200)");
		algorithm = new GeneticAlgorithm(maxIterations, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(maxIterations);
		algorithm.setBestSelectionRate(0.3);
		algorithm.setCrossoverRate(0.6);
		algorithm.setMutationRate(0.1);
		algorithm.setPopulationSize(200);
		algorithms.add(algorithm);

		sb.append(",Test (BSR: 0.3 | CR: 0.6 | MR: 0.3 | Pop: 200)");
		algorithm = new GeneticAlgorithm(maxIterations, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(maxIterations);
		algorithm.setBestSelectionRate(0.3);
		algorithm.setCrossoverRate(0.6);
		algorithm.setMutationRate(0.3);
		algorithm.setPopulationSize(200);
		algorithms.add(algorithm);


		/*//// ----- /////
		sb.append(",Test (10 | 0.999)");
		SimulatedAnnealing algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(10);
		algorithm.setMu(0.999);
		algorithms.add(algorithm);

		//// ----- /////
		sb.append(",Test (50 | 0.999)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(50);
		algorithm.setMu(0.999);
		algorithms.add(algorithm);

		//// ----- /////
		sb.append(",Test (100 | 0.999)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(100);
		algorithm.setMu(0.999);
		algorithms.add(algorithm);

		//// ----- /////
		sb.append(",Test (10 | 0.9995)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(10);
		algorithm.setMu(0.9995);
		algorithms.add(algorithm);

		//// ----- /////
		sb.append(",Test (50 | 0.9995)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(50);
		algorithm.setMu(0.9995);
		algorithms.add(algorithm);

		//// ----- /////
		sb.append(",Test (100 | 0.9995)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(100);
		algorithm.setMu(0.9995);
		algorithms.add(algorithm);


		//// ----- /////
		sb.append(",Test (10 | 0.9999)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(10);
		algorithm.setMu(0.9999);
		algorithms.add(algorithm);

		//// ----- /////
		sb.append(",Test (50 | 0.9999)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(50);
		algorithm.setMu(0.9999);
		algorithms.add(algorithm);

		//// ----- /////
		sb.append(",Test (100 | 0.9999)");
		algorithm = new SimulatedAnnealing(1000000, NodeReader.getNodes(dataSet));
		algorithm.setMaxStep(1000000);
		algorithm.setCurrentTemperature(100);
		algorithm.setMu(0.9999);
		algorithms.add(algorithm);*/

		sb.append("\n");

		long iteration = 0;
		while(algorithms.stream().anyMatch(Algorithm::hasNext))
		{
			if(iteration == 0 || iteration % 10 == 0)
			{
				System.out.println("iteration: " + iteration);
				sb.append(iteration);
				sb.append(",");
			}

			for(Algorithm algo : algorithms)
			{
				if(algo.hasNext())
				{
					algo.next();
				}

				if(iteration == 0 || iteration % 10 == 0)
				{
					sb.append(",");
					sb.append(format.format(algo.getBestSolution().getFitness()));
				}
			}
			if(iteration == 0 || iteration % 10 == 0)
			{
				sb.append("\n");
			}

			iteration++;
		}
		return sb.toString();
	}

	public static String getAnotherSimulationCSV(String dataSet)
	{
		double temp = 5;
		double mu = 0.9999;
		int nbTests = 10;

		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("##.00");

		sb.append("Temperature,");sb.append(temp);sb.append("\n");
		sb.append("Mu,");sb.append(mu);sb.append("\n");
		sb.append("Data Set,");sb.append(dataSet);sb.append("\n\n");

		List<Algorithm> algorithms = new ArrayList<>();
		sb.append("Iteration,");
		for(int i = 0; i<nbTests; i++)
		{
			sb.append(",Test ");
			sb.append(i+1);

			List<Node> nodes = NodeReader.getNodes(dataSet);
			SimulatedAnnealing algorithm = new SimulatedAnnealing(1000000, nodes);
			algorithm.setMaxStep(1000000);
			algorithm.setCurrentTemperature(temp);
			algorithm.setMu(mu);
			algorithms.add(algorithm);


		}

		sb.append(",,min,max,avg,med\n");
		long iteration = 0;
		while(algorithms.stream().anyMatch(Algorithm::hasNext))
		{
			if(iteration == 0 || iteration % 10 == 0)
			{
				System.out.println("iteration: " + iteration);
				sb.append(iteration);
				sb.append(",");
			}

			for(Algorithm algorithm : algorithms)
			{
				if(algorithm.hasNext())
				{
					algorithm.next();
				}

				if(iteration == 0 || iteration % 10 == 0)
				{
					sb.append(",");
					sb.append(format.format(algorithm.getBestSolution().getFitness()));
				}
			}
			if(iteration == 0 || iteration % 10 == 0)
			{
				sb.append("\n");
			}

			iteration++;
		}
		return sb.toString();
	}

	public static String getSimulationCSV()
	{
		List<String> dataSets = new ArrayList<String>(){{
			//add("data/data01.txt");
			//add("data/data02.txt");
			//add("data/data03.txt");
			//add("data/data04.txt");
			add("data/data05.txt");
		}};

		List<Integer> iterations = new ArrayList<Integer>(){{
			add(1000);
			add(10000);
			add(50000);
			add(100000);
			add(500000);
			add(1000000);
			//add(10000000);

		}};

		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("##.00");

		double temp = 44;
		double mu = 0.99998;
		sb.append("Temperature,");sb.append(temp);sb.append("\n");
		sb.append("Mu,");sb.append(mu);sb.append("\n\n");
		sb.append("Data Set,Iterations,");
		for(int i = 0; i<10; i++)
		{
			sb.append(",Test ");
			sb.append(i+1);
		}
		sb.append(",,min,max,avg,median\n");

		boolean highestIttReached = false;

		for(String dataSet : dataSets)
		{
			highestIttReached = false;
			for(int iteration : iterations)
			{
				if(highestIttReached){continue;}
				System.out.println("\nDataSet: " + dataSet);
				System.out.println("Iterations: " + iteration);

				double sum = 0;

				for(int i=0; i<10; i++)
				{
					List<Node> nodes = NodeReader.getNodes(dataSet);
					SimulatedAnnealing algorithm = new SimulatedAnnealing(1000000, nodes);
					algorithm.setMaxStep(iteration);
					algorithm.setCurrentTemperature(temp);
					algorithm.setMu(mu);

					while(algorithm.hasNext())
					{
						algorithm.next();
					}

					if(i==0)
					{
						sb.append(dataSet);
						sb.append(",");
						sb.append(algorithm.getSteps());
						sb.append(",");
					}
					sb.append(",");
					sum+=algorithm.getBestSolution().getFitness();
					sb.append(format.format(algorithm.getBestSolution().getFitness()));
					if(algorithm.getSteps() < iteration)
					{
						//highestIttReached = true;
					}
				}
				System.out.println(sum/10);
				sb.append("\n");
			}
		}

		return sb.toString();
	}



	public static String getSimulationCSVForGenetic()
	{
		String dataSet = "data/data04.txt";
		int maxIterations = 1000;
		int verificationNumber = 5;

		List<List<Algorithm>> algorithms = new ArrayList<>();
		List<String> algoLabels = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("##.00");


		GeneticAlgorithm algorithm;
		List<Algorithm> algos;

		List<Map<String,Double>> algoParams = new ArrayList<>();
		Map<String,Double> algoParamsUnit;

		algoParamsUnit = new HashMap<>();
		algoParamsUnit.put("BSR", 0.4);
		algoParamsUnit.put("CR", 0.6);
		algoParamsUnit.put("MR", 0.3);
		algoParamsUnit.put("Pop", 100.0);
		algoParams.add(algoParamsUnit);

		algoParamsUnit = new HashMap<>();
		algoParamsUnit.put("BSR", 0.4);
		algoParamsUnit.put("CR", 0.6);
		algoParamsUnit.put("MR", 0.3);
		algoParamsUnit.put("Pop", 150.0);
		algoParams.add(algoParamsUnit);


		algoParamsUnit = new HashMap<>();
		algoParamsUnit.put("BSR", 0.4);
		algoParamsUnit.put("CR", 0.6);
		algoParamsUnit.put("MR", 0.3);
		algoParamsUnit.put("Pop", 200.0);
		algoParams.add(algoParamsUnit);


		algoParamsUnit = new HashMap<>();
		algoParamsUnit.put("BSR", 0.4);
		algoParamsUnit.put("CR", 0.6);
		algoParamsUnit.put("MR", 0.3);
		algoParamsUnit.put("Pop", 250.0);
		algoParams.add(algoParamsUnit);


		for(Map<String,Double> param : algoParams)
		{
			algoLabels.add("Test (BSR: " + df.format(param.get("BSR"))
					+ " | CR: " + df.format(param.get("CR"))
					+ " | MR: " + df.format(param.get("MR"))
					+ " | Pop: " + param.get("Pop").intValue() + ")");
			algos = new ArrayList<>();
			for(int i=0; i<verificationNumber; i++)
			{

				algorithm = new GeneticAlgorithm(maxIterations, NodeReader.getNodes(dataSet));
				algorithm.setMaxStep(maxIterations);
				algorithm.setBestSelectionRate(param.get("BSR"));
				algorithm.setCrossoverRate(param.get("CR"));
				algorithm.setMutationRate(param.get("MR"));
				algorithm.setPopulationSize(param.get("Pop").intValue());
				algos.add(algorithm);

			}
			algorithms.add(algos);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Data Set: " + dataSet);
		sb.append("\nIterations: " + maxIterations + "\n\n");


		sb.append("Parameters,");
		for(int i=1; i<=verificationNumber; i++)
		{
			sb.append(",Test " + i);
		}

		sb.append(",,min,max,avg,med");

		sb.append("\n\n");

		int ittNumber = 0;

		while(algorithms.stream().anyMatch(al -> al.stream().anyMatch(a -> a.hasNext())))
		{
			ittNumber++;
			System.out.println(ittNumber + "/" + maxIterations);
			for(List<Algorithm> algoList : algorithms)
			{
				for(Algorithm algo : algoList)
				{
					if(algo.hasNext()){algo.next();}
				}
			}
		}

		for(int j=0; j<algorithms.size(); j++)
		{
			sb.append(algoLabels.get(j));
			sb.append(",,");
			for(Algorithm algo : algorithms.get(j))
			{
				sb.append(df.format(algo.getBestSolution().getFitness()));
				sb.append(",");
			}
			sb.append("\n");
		}


		return sb.toString();
	}

	public void findBest()
	{
		int maxIterations = 1000;

		int populationSize = 200;
		int bestSelectionRate = 10;
		int crossOverRate = 10;
		int mutationRate = 10;

		List<Double> test = new ArrayList<>();
		test.add(0D);
		test.add(3D);
		test.add(2D);

		List<List<Double>> bestParams = new ArrayList<>();

		while(populationSize < 201)
		{
			bestSelectionRate = 10;
			crossOverRate = 10;
			mutationRate = 10;
			while(bestSelectionRate < 100)
			{
				crossOverRate = 10;
				mutationRate = 10;
				while(crossOverRate < 100 && bestSelectionRate + crossOverRate <= 100)
				{
					mutationRate = 10;
					while(mutationRate < 100)
					{
						GeneticAlgorithm algorithm = new GeneticAlgorithm(maxIterations, NodeReader.getNodes("data/data01.txt"));
						algorithm.setMaxStep(maxIterations);
						algorithm.setPopulationSize(populationSize);
						algorithm.setBestSelectionRate(bestSelectionRate/100);
						algorithm.setCrossoverRate(crossOverRate/100);
						algorithm.setMutationRate(mutationRate/100);

						while(algorithm.hasNext())
						{
							algorithm.next();
						}
						System.out.print("(" + populationSize + ", " + (bestSelectionRate/100D) + ", " + (crossOverRate/100D) + ", " + (mutationRate/100D) + ")");
						System.out.println(" = " + algorithm.getBestSolution().getFitness());

						if(bestParams.size() < 10 || bestParams.get(bestParams.size()-1).get(4) > algorithm.getBestSolution().getFitness())
						{
							List<Double> newResult = new ArrayList<>();
							newResult.add((double)populationSize);
							newResult.add(bestSelectionRate/100D);
							newResult.add(crossOverRate/100D);
							newResult.add(mutationRate/100D);
							newResult.add(algorithm.getBestSolution().getFitness());

							if(bestParams.size() < 10)
							{
								bestParams.add(newResult);
							}
							else
							{
								bestParams.set(bestParams.size()-1, newResult);
							}


							bestParams.sort(Comparator.comparingDouble(l -> l.get(4)));
							System.out.println("new best");
							System.out.println("the bests are: ");
							for(List<Double> l : bestParams)
							{
								System.out.println("\t" + l.get(0) + ", " + l.get(1) + ", " + l.get(2) + ", " + l.get(3) + " = " + l.get(4));
							}

						}

						//System.out.println(populationSize + ", " + (bestSelectionRate/100D) + ", " + (crossOverRate/100D) + ", " + (mutationRate/100D));
						mutationRate+=10;
					}
					crossOverRate+=10;
				}
				bestSelectionRate+=10;
			}
			populationSize+=2;
		}


		System.out.println("the bests are: ");
		for(List<Double> l : bestParams)
		{
			System.out.println("\t" + l.get(0) + ", " + l.get(1) + ", " + l.get(2) + ", " + l.get(3) + " = " + l.get(4));
		}
	}
}
