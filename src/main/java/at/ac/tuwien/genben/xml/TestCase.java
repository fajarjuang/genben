package at.ac.tuwien.genben.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A TestCase covers all parameters which are imported for a test run
 */
@Root
public class TestCase {
	private Map<String, Parameter> parameterMap;

	@Attribute
	private String name;

	@ElementList(inline = true, entry = "Parameter")
	private List<Parameter> parameters;

	public TestCase() {
		parameterMap = new HashMap<String, Parameter>();
	}

	@Commit
	public void build() {
		for (Parameter parameter : parameters) {
			parameterMap.put(parameter.getName(), parameter);
		}
	}

	public void appendParameters(List<Parameter> parameters) {
		if (parameters != null) {
			this.parameters.addAll(parameters);
			build();
		}
	}

	public Parameter getParameter(String name) {
		return parameterMap.get(name);
	}

	public String getName() {
		return name;
	}
}
