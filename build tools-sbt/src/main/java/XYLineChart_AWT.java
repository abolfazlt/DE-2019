import java.awt.Color;
import java.awt.BasicStroke;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {

	public XYLineChart_AWT(String applicationTitle, String chartTitle,
			List<double[]> allData, List<double[]> train, List<double[]> test) {
		super(applicationTitle);
		JFreeChart xylineChart = ChartFactory.createXYLineChart(chartTitle,
				"Mileage", "Price", setDataset(allData, train, test),
				PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		final XYPlot plot = xylineChart.getXYPlot();

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesPaint(1, Color.GREEN);
		renderer.setSeriesPaint(2, Color.YELLOW);
		renderer.setSeriesStroke(0, new BasicStroke(4.0f));
		renderer.setSeriesStroke(1, new BasicStroke(3.0f));
		renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		plot.setRenderer(renderer);
		setContentPane(chartPanel);
	}

	public XYDataset setDataset(List<double[]> allData, List<double[]> train,
			List<double[]> test) {
		final XYSeries allDataSeries = new XYSeries("allData");
		for (Iterator<double[]> iterator = allData.iterator(); iterator
				.hasNext();) {
			double[] point = iterator.next();
			allDataSeries.add(point[0], point[1]);
		}

		final XYSeries trainSeries = new XYSeries("train");
		for (Iterator<double[]> iterator = train.iterator(); iterator.hasNext();) {
			double[] point = iterator.next();
			trainSeries.add(point[0], point[1]);
		}

		final XYSeries testSeries = new XYSeries("test");
		for (Iterator<double[]> iterator = test.iterator(); iterator.hasNext();) {
			double[] point = iterator.next();
			testSeries.add(point[0], point[1]);
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		// dataset.addSeries(allDataSeries);
		dataset.addSeries(trainSeries);
		dataset.addSeries(testSeries);
		return dataset;
	}

}