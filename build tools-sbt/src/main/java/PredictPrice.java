import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jfree.ui.RefineryUtilities;

public class PredictPrice {

	public List<double[]> readData(String fileName) {
		List<double[]> list = new ArrayList<double[]>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream
					.filter(line -> !line.contains("NA"))
					.map(line -> line.split(","))
					.filter(input -> input.length > 1)
					.map(input -> Arrays.stream(input)
							.mapToDouble(Double::parseDouble).toArray())
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public HashMap<String, List<double[]>> crossValidation(List<double[]> data,
			double ratio) {
		List<double[]> train = new ArrayList<double[]>();
		List<double[]> test = new ArrayList<double[]>();

		Random random = new Random();

		for (Iterator<double[]> iterator = data.iterator(); iterator.hasNext();) {
			if (random.nextDouble() < ratio)
				train.add(iterator.next());
			else
				test.add(iterator.next());
		}

		HashMap<String, List<double[]>> splitedData = new HashMap<String, List<double[]>>();
		splitedData.put("train", train);
		splitedData.put("test", test);
		return splitedData;
	}

	public MultipleLinearRegression getRegression(List<double[]> data) {
		int size = data.get(0).length - 1;
		double[][] x = new double[data.size()][size];
		double[] y = new double[data.size()];
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < size; j++)
				x[i][0] = data.get(i)[j];
			y[i] = data.get(i)[size];
		}
		MultipleLinearRegression regression = new MultipleLinearRegression(x, y);
		return regression;
	}

	public List<double[]> getPredict(List<double[]> test,
			MultipleLinearRegression regression) {
		List<double[]> predict = new ArrayList<double[]>();
		int size = test.get(0).length;
		for (int i = 0; i < test.size(); i++) {
			double[] vector = new double[size];
			for (int j = 0; j < size - 1; j++)
				vector[j] = test.get(i)[j];
			vector[size - 1] = regression.predict(Arrays.copyOfRange(vector, 0,
					size - 1));
			predict.add(vector);
		}
		return predict;
	}

	public double getError(List<double[]> test, List<double[]> predict) {
		double error = 0.0;
		for (int i = 0; i < test.size(); i++) {
			error += Math.pow(
					test.get(i)[test.get(i).length - 1]
							- predict.get(i)[predict.get(i).length - 1], 2);
		}
		return Math.sqrt(error / test.size());
	}

	public static void main(String[] args) {
		PredictPrice pridePrice = new PredictPrice();
		List<double[]> data = pridePrice
				.readData("/mnt/storage/Documents/DE/birth-life-2010.csv");

		HashMap<String, List<double[]>> splitedData = pridePrice
				.crossValidation(data, 0.75);
//
		XYLineChart_AWT chart = new XYLineChart_AWT("Generated data",
				"Predict price of new value", data, splitedData.get("train"),
				splitedData.get("test"));
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
		MultipleLinearRegression regression = pridePrice
				.getRegression(splitedData.get("train"));
		List<double[]> predict = pridePrice.getPredict(splitedData.get("test"),
				regression);

		chart = new XYLineChart_AWT("Generated data",
				"Predict price of new value", data, splitedData.get("test"),
				predict);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
//
//		System.out
//				.println(pridePrice.getError(splitedData.get("test"), predict));

//		data = pridePrice
//				.readData("/mnt/storage/Documents/DE/train-multivariable.csv");
//		splitedData = pridePrice.crossValidation(data, 0.75);
//		regression = pridePrice.getRegression(splitedData.get("train"));
//		predict = pridePrice.getPredict(splitedData.get("test"), regression);
//		System.out
//				.println(pridePrice.getError(splitedData.get("test"), predict));
	}

}
