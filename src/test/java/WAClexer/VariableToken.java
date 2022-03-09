package WAClexer;

public class VariableToken implements Token {
	public final String name;
	
	public VariableToken(final String name) {
		this.name = name;
	}
	
	public int hashCode() {
		return name.hashCode();
	}
}