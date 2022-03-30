package WACparser;

//represents parsing failure
public class ParseException extends Exception {
	public ParseException(final String message) {
		super(message);
	}
}