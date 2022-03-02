import java.util.List;
import java.util.ArrayList;

public class Tokenizer {
	public static List<Token> tokenize(final String input) 
		throws TokenizerException {
		final List<Token> tokens = new ArrayList<Token>();
		int offset = 0;
		
		while (offset < input.length()) {
			//skip whitespace - breaks for trailing whitespace
			while (Character.isWhitespace(input.charAt(offset))) {
				offset++;
			}
			
			if (input.startsWith("Int", offset)) {
				tokens.add(new IntToken());
				offset += 3;
			} else if (input.startsWith("Boolean", offset)) {
				tokens.add(new BooleanToken());
				offset += 7;
			} else {
				throw new TokenizerException();
			}
		}
		return tokens;
	}
}

//this is adapted from Kyle's example following it up to the end of the video for February 16th.