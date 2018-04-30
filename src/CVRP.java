import module.Algorithms.Algorithm;
import module.Algorithms.SimulatedAnnealing;
import module.Node;
import module.NodeReader;
import module.utils.Helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CVRP {

	public static void main(String[] args) {
		Random random = Helpers.random;
		System.out.println(random.nextInt(56));





		String str = getSimulationCSV();
		//String str = getAnotherSimulationCSV("data/data01.txt");
		//String str = differenceTest("data/data01.txt");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("result.csv"));
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

		//// ----- /////
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
		algorithms.add(algorithm);

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
			add("data/data01.txt");
			//add("data/data02.txt");
			//add("data/data03.txt");
			//add("data/data04.txt");
			//add("data/data05.txt");
		}};

		List<Integer> iterations = new ArrayList<Integer>(){{
			//add(1000);
			//add(10000);
			//add(50000);
			add(100000);
			add(1000000);

		}};

		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("##.00");

		double temp = 100;
		double mu = 0.9999;
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
}
