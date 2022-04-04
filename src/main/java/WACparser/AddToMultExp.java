package WACparser;

// Additive Expression that is
// multiplicative_exp (additive_op multiplicative_exp)*
public class AddToMultExp extends Additive_Exp {

    public final Multiplicative_Exp left;
    public final Additive_Op addOp;
    public final Multiplicative_Exp right;

    public AddToMultExp(final Multiplicative_Exp left, final Additive_Op addOp, final Multiplicative_Exp right) {
        this.left = left;
        this.addOp = addOp;
        this.right = right;
    }

    public boolean equals(final Object other) {
        if (other instanceof AddToMultExp) {
            final AddToMultExp otherAddExp = (AddToMultExp) other;
            return (left.equals(otherAddExp.left) && addOp.equals(otherAddExp.addOp) && right.equals(otherAddExp.right));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (left.hashCode() + addOp.hashCode() + right.hashCode());
    }

    public String toString() {
        return ("AddToMultExp(" + left.toString() + ", " + addOp.toString() + ", " + right.toString() + ")");
    }

}