package at.ac.tuwien.genben.sensors;

import at.ac.tuwien.genben.TestInfo;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * measures the time needed to execute the runnable instance which gets
 * supplied with the execute call
 */
public class TimeSensor {
	private CSVWriter csvWriter;

	public TimeSensor(String name) {
		try {
			csvWriter = new CSVWriter(new FileWriter(new File(TestInfo.buildCsvFileName(name)).getAbsoluteFile()));
			String entries[] = {"MilliSeconds"};
			csvWriter.writeNext(entries);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void execute(Runnable task) {
		long start = System.nanoTime();
		task.run();
		long end = System.nanoTime();
		String entries[] = {Double.toString((end - start) / 1000000.0)};
		csvWriter.writeNext(entries);
	}

	public void close() {
		try {
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
