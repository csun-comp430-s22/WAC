package WACparser;

//represents success in parsing
public class ParseResult<A> {
	public final A result;
	public final int position;
	
	public ParseResult(final A result, final int position) {
		this.result = result;
		this.position = position;
	}
	
	public boolean equals(final Object other){
		if (other instanceof ParseResult) {
			final ParseResult otherPR = (ParseResult)other;
			return (result.equals(otherPR.result) && position == otherPR.position);
		} else {
			return false;
		}
	}
}