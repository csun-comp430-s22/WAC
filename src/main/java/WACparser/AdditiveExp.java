package WACparser;

public class AdditiveExp extends Comparison_Exp {

    public final Comparison_Exp additive_exp;

    public AdditiveExp(final Comparison_Exp additive_exp) {
        this.additive_exp = additive_exp;
    }

    public boolean equals(final Object other) {
        if (other instanceof AdditiveExp) {
            final AdditiveExp otherAdditiveExp = (AdditiveExp) other;
            return (additive_exp.equals(otherAdditiveExp.additive_exp));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (additive_exp.hashCode());
    }

    public String toString() {
        return ("AdditiveExp(" + additive_exp + ")");
    }

}