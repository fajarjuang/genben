package at.tuwien.genben.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 */
@Root
public class Variable {
    @Attribute
    private String name;

    @Attribute(required = false)
    private String displayName;

    @Attribute(required = false)
    private String column;

    @Attribute
    private double factor;

    public String getName() {
        return name;
    }

    public double getFactor() {
        return factor;
    }

    public boolean hasColumn() {
        return column != null;
    }

    public String getColumn() {
        return column;
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    public String getDisplayName() {
        return displayName;
    }
}
