package WACparser;

public class Variable {
    public final String name;

    public Variable(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(final Object other) {
    	
        if(other instanceof Variable) {
        	return true;
        }
        else {
        	return false;
        }
               
    }

    public String toString() {
        return "Variable(" + name + ")";
    }
}
