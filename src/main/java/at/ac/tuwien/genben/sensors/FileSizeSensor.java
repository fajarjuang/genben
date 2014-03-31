package at.ac.tuwien.genben.sensors;

import at.ac.tuwien.genben.TestInfo;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * this sensor measures the size of the files which get supplied and writes them into the csv file
 */
public class FileSizeSensor {
	private CSVWriter csvWriter;
	private String files[];

	public FileSizeSensor(String name, String file) {
		this.files = new String[]{file};
		createCSVWriter(name);
	}

	public FileSizeSensor(String name, String files[]) {
		this.files = files;
		createCSVWriter(name);
	}

	private void createCSVWriter(String name) {
		try {
			csvWriter = new CSVWriter(new FileWriter(new File(TestInfo.buildCsvFileName(name))));
			String entries[] = {"FileSize"};
			csvWriter.writeNext(entries);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void execute() {
		long fileSize = 0;
		for (String fileName : files) {
			File file = new File(fileName);
			if (file.isDirectory()) {
				fileSize += FileUtils.sizeOfDirectory(file);
			} else {
				fileSize += file.length();
			}
		}

		String entries[] = {Long.toString(fileSize / 1000)};
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
