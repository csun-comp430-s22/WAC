package WACparser;

// Comparison Expression rule thats under Comparison_Expression in our grammar
// comparison_exp ::= additive_exp comparison_op additive_exp
public class AddCompOp extends Comparison_Exp {

    public final Additive_Exp left;
    public final Comparison_Op compOp;
    public final Additive_Exp right;

    public AddCompOp(final Additive_Exp left, final Comparison_Op compOp, final Additive_Exp right) {
        this.left = left;
        this.compOp = compOp;
        this.right = right;
    }

    public boolean equals(final Object other) {
        if (other instanceof AddCompOp) {
            final AddCompOp otherCompExp = (AddCompOp) other;
            return (left.equals(otherCompExp.left) && compOp.equals(otherCompExp.addOp) && right.equals(otherCompExp.right));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (left.hashCode() + compOp.hashCode() + right.hashCode());
    }

    public String toString() {
        return ("AddCompOp(" + left.toString() + ", " + compOp.toString() + ", " + right.toString() + ")");
    }

}