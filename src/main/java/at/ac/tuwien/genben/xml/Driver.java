package at.ac.tuwien.genben.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 */
@Root
public class Driver {
	@Attribute
	private String name;

	@Attribute
	private String driverClass;

	public String getName() {
		return name;
	}

	public String getDriverClass() {
		return driverClass;
	}
}
