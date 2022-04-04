package WACparser;

// Additive Expression rule thats under Comparison_Expression in our grammar
// comparison_exp ::= additive_exp
public class CompToAddExp extends Comparison_Exp {

    public final Additive_Exp additive_exp;

    public CompToAddExp(final Additive_Exp additive_exp) {
        this.additive_exp = additive_exp;
    }

    public boolean equals(final Object other) {
        if (other instanceof CompToAddExp) {
            final CompToAddExp otherAdditiveExp = (CompToAddExp) other;
            return (additive_exp.equals(otherAdditiveExp.additive_exp));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (additive_exp.hashCode());
    }

    public String toString() {
        return ("CompToAddExp(" + additive_exp.toString() + ")");
    }

}