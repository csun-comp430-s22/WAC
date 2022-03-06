package WAClexcer;


	public class WhileToken implements Token {
		public boolean eqauls(final Object other) {
			return other instanceof WhileToken;
		}
		
		public int hashCode() {
			return 19;
		}
		
		public String toString() {
			return "while";
		}
	}


