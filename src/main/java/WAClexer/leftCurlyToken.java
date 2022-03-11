package WAClexer;

public class leftCurlyToken implements Token{

		public boolean eqauls(final Object other) {
			return other instanceof leftCurlyToken;
		}
		
		public int hashCode() {
			return 24;
		}
		
		public String toString() {
			return "{";
		}
	
}
