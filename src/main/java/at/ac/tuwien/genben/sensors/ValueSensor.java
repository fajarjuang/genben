package at.ac.tuwien.genben.sensors;

import at.ac.tuwien.genben.TestInfo;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ValueSensor {
	private CSVWriter csvWriter;

	public ValueSensor(String name) {
		try {
			csvWriter = new CSVWriter(new FileWriter(new File(TestInfo.buildCsvFileName(name)).getAbsoluteFile()));
			String entries[] = {"Value"};
			csvWriter.writeNext(entries);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void execute(String value) {
		String entries[] = {value};
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
