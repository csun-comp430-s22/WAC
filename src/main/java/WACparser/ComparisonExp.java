package WACparser;

// comparitive Expression under Exp (not the interface)
public class ComparisonExp implements Exp {

	public final Exp comparison_exp;

	public ComparisonExp(final Exp comparison_exp) {
		this.comparison_exp = comparison_exp;
	}

	public boolean equals(final Object other) {
		if (other instanceof ComparisonExp) {
			final ComparisonExp otherExp = (ComparisonExp)other;
			return (comparison_exp.equals(otherExp.comparison_exp));
		} else {
			return false;
		}
	}

	public int hashCode() {
		return (comparison_exp.hashCode());
	}

	public String toString() {
		return ("OpExp(" + comparison_exp + ")");
	}

	// old code from professor's example
//	public final Exp left;
//	public final Op op;
//	public final Exp right;
//
//	public OpExp(final Exp left, final Op op, final Exp right) {
//		this.left = left;
//		this.op = op;
//		this.right = right;
//	}
//
//	public boolean equals(final Object other) {
//		if (other instanceof OpExp) {
//			final OpExp otherExp = (OpExp)other;
//			return (left.equals(otherExp.left) && op.equals(otherExp.op) && right.equals(otherExp.right));
//		} else {
//			return false;
//		}
//	}
//
//	public int hashCode() {
//		return (left.hashCode() + op.hashCode() + right.hashCode());
//	}
//
//	public String toString() {
//		return ("OpExp(" + left.toString() + ", " + op.toString() + ", " + right.toString() + ")");
//	}

}