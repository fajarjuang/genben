package at.tuwien.genben;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestInfo
{
    private static String currentTestCase;
    private static String currentTestDriver;
    private static String currentTestExecutionDirectory;

    public static void createCurrentTestExecutionDirectory() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        Date date = new Date();
        currentTestExecutionDirectory = dateFormat.format(date);
        File file = new File(currentTestExecutionDirectory);
        file.mkdir();
    }

    public static void setCurrentTestExecutionDirectory(String directory) {
        currentTestExecutionDirectory = directory;
    }

    public static String getCurrentTestCase() {
        return currentTestCase;
    }

    public static String getCurrentTestDriver() {
        return currentTestDriver;
    }

    public static void setCurrentTestCase(String currentTestCase) {
        TestInfo.currentTestCase = currentTestCase;
    }

    public static void setCurrentTestDriver(String currentTestDriver) {
        TestInfo.currentTestDriver = currentTestDriver;
    }

    public static String buildCsvFileName(String variableName) {
        return currentTestExecutionDirectory + "/" + TestInfo.currentTestDriver.trim() + TestInfo.currentTestCase.trim() + variableName + ".csv";
    }

    public static String buildChartName(String graphName) {
        return currentTestExecutionDirectory + "/" + graphName + ".png";
    }
}
