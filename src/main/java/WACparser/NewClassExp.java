package WACparser;

import java.util.List;

// made for the 'exp ::= new classname(exp*)' situation
public class NewClassExp implements Exp {
    public final Exp className;
	public final List<Exp> inParens;

    public NewClassExp(final Exp className, final List<Exp> inParens) {
        this.className = className;
        this.inParens = inParens;
    }

    public boolean equals(final Object other) {
        if (other instanceof NewClassExp) {
            final NewClassExp otherNewClassExp = (NewClassExp)other;
            return (className.equals(otherNewClassExp.className) && inParens.equals(otherNewClassExp.inParens));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (className.hashCode() + inParens.hashCode());
    }

    public String toString() {
        return ("NewClassExp(" + className.toString() + ", " + inParens.toString() + ")");
    }

}