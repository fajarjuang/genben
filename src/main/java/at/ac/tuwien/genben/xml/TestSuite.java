package at.ac.tuwien.genben.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * The TestSuite covers the complete configuration for a performance test run
 */
@Root
public class TestSuite {
	@Attribute
	private String name;

	@ElementList(inline = true, entry = "Driver")
	private List<Driver> drivers;

	@ElementList(inline = true, entry = "Parameter", required = false)
	private List<Parameter> parameters;

	@ElementList(inline = true, entry = "TestCase")
	private List<TestCase> testCases;

	@ElementList(inline = true, entry = "Graph", required = false)
	private List<Graph> graphs;

	public List<Driver> getDrivers() {
		return drivers;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public List<Graph> getGraphs() {
		if (graphs == null) {
			return new ArrayList<Graph>(); //return empty list
		}
		return graphs;
	}

}
