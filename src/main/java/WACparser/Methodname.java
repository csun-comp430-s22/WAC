package WACparser;

public class Methodname {
    public final String name;

    public Methodname(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof Methodname &&
                name.equals(((Methodname) other).name));
    }

    public String toString() {
        return "Methodname(" + name + ")";
    }
}