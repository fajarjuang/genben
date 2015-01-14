package at.tuwien.genben.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.File;

/**
 * One Parameter
 */
@Root
public class Parameter
{
    @Attribute
    private String name;

    @Attribute
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(value);
    }

    public int getIntegerValue() {
        return Integer.parseInt(value);
    }

    public double getDoubleValue() {
        return Double.parseDouble(value);
    }

    public File getFileValue() {
        return new File(value);
    }
}
