package WACparser;

public class Classname {
    public final String name;

    public Classname(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof Classname &&
                name.equals(((Classname) other).name));
    }

    public String toString() {
        return "Classname(" + name + ")";
    }
}
