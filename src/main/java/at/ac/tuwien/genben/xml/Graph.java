package at.ac.tuwien.genben.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Triggers the creation of a graph
 * can be used do display the data
 */
@Root
public class Graph {
	@Attribute
	private String name;

	@Attribute
	private String type;

	@Attribute
	private String function;

	@Attribute(required = false)
	private String xunit;

	@Attribute(required = false)
	private String yunit;

	@Attribute(required = false)
	private String xname;

	@Attribute(required = false)
	private String yname;

	@Attribute(required = false)
	private int group = 4;

	public String getName() {
		return name;
	}

	@ElementList(inline = true, entry = "Variable")
	private List<Variable> variables;

	public List<Variable> getVariables() {
		return variables;
	}

	public String getFunction() {
		return function;
	}

	public String getType() {
		return type;
	}

	public boolean hasXUnit() {
		return xunit != null;
	}

	public String getXUnit() {
		return xunit;
	}

	public boolean hasYUnit() {
		return yunit != null;
	}

	public String getYUnit() {
		return yunit;
	}

	public boolean hasXName() {
		return xname != null;
	}

	public String getXName() {
		return xname;
	}

	public boolean hasYName() {
		return yname != null;
	}

	public String getYName() {
		return yname;
	}

	public int getGroup() {
		return group;
	}
}
