package at.tuwien.genben.sensors;


import at.tuwien.genben.TestInfo;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * This sensor measures the committed, max, used and init size of the memory for the current state
 * it does not trigger a garbage collection and also doesn't care about it
 * memory measurements in java should always be treated carefully as they are not deterministic
 * through the non deterministic behaviour of the garbage collector
 */
public class MemorySensor
{
    private MemoryMXBean memoryMXBean;
    private CSVWriter csvWriter;

    public MemorySensor(String name) {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();

        try {
            csvWriter = new CSVWriter(new FileWriter(new File(TestInfo.buildCsvFileName(name)).getAbsoluteFile()));
            String entries[] = {"Committed", "Max", "Used", "Init"};
            csvWriter.writeNext(entries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        String entries[] = {Long.toString(memoryUsage.getCommitted()/1000), Long.toString(memoryUsage.getMax()/1000),
                Long.toString(memoryUsage.getUsed()/1000), Long.toString(memoryUsage.getInit()/1000)};
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
