package WACparser;

// made for the 'exp ::= new classname(exp*)' situation
public class NewClassExp implements Exp {
    public final Exp left;
    public final Exp varName;
    public final Exp inParens;

    public NewClassExp(final Exp left, final Exp varName, final Exp inParens) {
        this.left = left;
        this.varName = varName;
        this.inParens = inParens;
    }

    public boolean equals(final Object other) {
        if (other instanceof NewClassExp) {
            final NewClassExp otherNewClassExp = (NewClassExp) other;
            return (left.equals(otherNewClassExp.left) && varName.equals(otherNewClassExp.varName) && inParens.equals(otherNewClassExp.inParens));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (left.hashCode() + varName.hashCode() + inParens.hashCode());
    }

    public String toString() {
        return ("NewClassExp(" + left.toString() + ", " + varName.toString() + ", " + inParens.toString() + ")");
    }

}