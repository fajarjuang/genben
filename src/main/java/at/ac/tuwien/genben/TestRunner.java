package at.ac.tuwien.genben;

import at.ac.tuwien.genben.xml.Driver;
import at.ac.tuwien.genben.xml.Graph;
import at.ac.tuwien.genben.xml.TestCase;
import at.ac.tuwien.genben.xml.TestSuite;
import at.ac.tuwien.genben.xml.Variable;
import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.simpleframework.xml.core.Persister;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestRunner {
	public static void main(String args[]) {
		Options options = new Options();
		options.addOption("g", false, "Only create graphes");
		options.addOption("d", true, "Directory where the results are stored (only needed for graph creation)");


		CommandLineParser parser = new PosixParser();
		try {
			CommandLine commandLine = parser.parse(options, args);

			String[] rest = commandLine.getArgs();
			if (rest.length == 0) {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("genben", options);
				return;
			}

			//the rest should be the options file
			Persister persister = new Persister();
			TestSuite testSuite = persister.read(TestSuite.class, new File(rest[0]));


			if (!commandLine.hasOption("g")) {
				TestInfo.createCurrentTestExecutionDirectory();

				for (Driver driver : testSuite.getDrivers()) {
					TestInfo.setCurrentTestDriver(driver.getName());
					System.out.println("Using driver: " + driver.getName() + " (" + driver.getDriverClass() + ")");

					for (TestCase testCase : testSuite.getTestCases()) {
						TestInfo.setCurrentTestCase(testCase.getName());
						System.out.println("Executing test case: " + testCase.getName());
						testCase.appendParameters(testSuite.getParameters()); //append the parameters

						TestDriver testDriver = (TestDriver) Class.forName(driver.getDriverClass()).newInstance(); //create a new instance for every execution
						testDriver.prepare(testCase);
						testDriver.warmup(testCase);

						//GCUtil.stabilizeMemory();
						testDriver.run(testCase);
						testDriver.finish(testCase);
					}
				}
			}

			if (commandLine.hasOption("d")) {
				TestInfo.setCurrentTestExecutionDirectory(commandLine.getOptionValue("d"));
			}

			for (Graph graph : testSuite.getGraphs()) {
				System.out.println("Creating graph: " + graph.getName());
				if (graph.getType().equals("line")) {
					createLineChart(graph, testSuite);
				}
				if (graph.getType().equals("bar")) {
					createBarChart(graph, testSuite);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static StandardChartTheme createChartTheme() {
		String fontName = "Lucida Sans";
		StandardChartTheme theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();
		theme.setTitlePaint(Color.black);
		theme.setExtraLargeFont(new Font(fontName, Font.PLAIN, 16)); //title
		theme.setLargeFont(new Font(fontName, Font.BOLD, 15)); //axis-title
		theme.setRegularFont(new Font(fontName, Font.PLAIN, 11));
		theme.setRangeGridlinePaint(Color.decode("#C0C0C0"));
		theme.setPlotBackgroundPaint(Color.white);
		theme.setChartBackgroundPaint(Color.white);
		theme.setGridBandPaint(Color.red);
		theme.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
		theme.setBarPainter(new StandardBarPainter());
		theme.setAxisLabelPaint(Color.decode("#666666"));
		return theme;
	}

	private static void createBarChart(Graph graph, TestSuite testSuite) throws Exception {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int groupCount = 0;

		for (TestCase testCase : testSuite.getTestCases()) {
			groupCount++;

			for (Driver driver : testSuite.getDrivers()) {
				TestInfo.setCurrentTestDriver(driver.getName());

				TestInfo.setCurrentTestCase(testCase.getName());

				for (Variable variable : graph.getVariables()) {
					CSVReader reader = new CSVReader(new FileReader(TestInfo.buildCsvFileName(variable.getName())));
					double factor = variable.getFactor() == 0 ? 1 : variable.getFactor();
					String line[] = reader.readNext();
					int column = 0;
					int count = 0;

					if (variable.hasColumn()) {
						int index = 0;
						for (String col : line) {
							if (col.equals(variable.getColumn())) {
								column = index;
								break;
							}
							index++;
						}
					}

					double result = 0;
					while ((line = reader.readNext()) != null) {
						if (graph.getFunction().equals("sum") || graph.getFunction().equals("avg")) {
							result += Double.parseDouble(line[column]) * factor;
						}
						count++;
					}

					if (graph.getFunction().equals("avg")) {
						result = result / (double) count;
					}

					dataset.addValue(result, driver.getName(), testCase.getName());
				}
			}

			if ((groupCount % graph.getGroup()) == 0) {
				writeBarChart(graph, dataset, groupCount);
				dataset = new DefaultCategoryDataset();
			}
		}

		if ((groupCount % graph.getGroup()) != 0) {
			writeBarChart(graph, dataset, groupCount);
		}
	}

	private static void writeBarChart(Graph graph, DefaultCategoryDataset dataset, int groupNr) throws IOException {
		String xname = (graph.hasXName() ? graph.getXName() : "X") + (graph.hasXUnit() ? " (" + graph.getXUnit() + ")" : "");
		String yname = (graph.hasYName() ? graph.getYName() : "X") + (graph.hasYUnit() ? " (" + graph.getYUnit() + ")" : "");

		JFreeChart chart = ChartFactory.createBarChart(graph.getName(), xname, yname, dataset, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(Color.white);

		createChartTheme().apply(chart);
		CategoryPlot categoryPlot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
		renderer.setSeriesPaint(0, new Color(197, 0, 11));
		renderer.setSeriesPaint(1, new Color(0, 132, 209));
		renderer.setSeriesPaint(2, new Color(87, 157, 28));

		chart.setAntiAlias(true);
		ChartUtilities.saveChartAsPNG(new File(TestInfo.buildChartName(graph.getName() + groupNr)), chart, 1000, 500);
	}

	private static void createLineChart(Graph graph, TestSuite testSuite) throws Exception {
		String xname = (graph.hasXName() ? graph.getXName() : "X") + (graph.hasXUnit() ? " (" + graph.getXUnit() + ")" : "");
		String yname = (graph.hasYName() ? graph.getYName() : "X") + (graph.hasYUnit() ? " (" + graph.getYUnit() + ")" : "");

		for (TestCase testCase : testSuite.getTestCases()) {
			XYSeriesCollection dataset = new XYSeriesCollection();
			TestInfo.setCurrentTestCase(testCase.getName());
			for (Driver driver : testSuite.getDrivers()) {
				TestInfo.setCurrentTestDriver(driver.getName());

				for (Variable variable : graph.getVariables()) {
					String variableName = variable.hasDisplayName() ? variable.getDisplayName() : variable.getName();
					double factor = variable.getFactor() == 0 ? 1 : variable.getFactor();
					XYSeries series = new XYSeries(driver.getName() + " " + variableName);
					CSVReader reader = new CSVReader(new FileReader(TestInfo.buildCsvFileName(variable.getName())));
					String line[] = reader.readNext();
					int column = 0;
					int count = 0;
					if (variable.hasColumn()) {
						int index = 0;
						for (String col : line) {
							if (col.equals(variable.getColumn())) {
								column = index;
								break;
							}
							index++;
						}
					}

					while ((line = reader.readNext()) != null) {
						series.add(count, Double.parseDouble(line[column]) * factor);
						count++;
					}
					dataset.addSeries(series);
				}
			}

			JFreeChart chart = ChartFactory.createXYLineChart(graph.getName(), xname, yname, dataset, PlotOrientation.VERTICAL, true, true, false);
			createChartTheme().apply(chart);
			chart.setAntiAlias(true);
			ChartUtilities.saveChartAsPNG(new File(TestInfo.buildChartName(testCase.getName() + " " + graph.getName())), chart, 1000, 500);
		}
	}
}
