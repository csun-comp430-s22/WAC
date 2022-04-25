package WAClexer;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TokenizerTest {
	// This method starts the procces of checking the input for tokens
    public void assertTokenizes(final String input, final Token[] expected) throws TokenizerException {
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> received = tokenizer.tokenize();
        assertArrayEquals(expected,received.toArray(new Token[received.size()]));     
        }
   

	// annotation
	@Test
	public void testEmptyString() throws TokenizerException {
		// check that tokenizing empty string works
		assertTokenizes("", new Token[0]);

		// Tokenizer tokenizer = new Tokenizer("");
		// List<Token> tokens = tokenizer.tokenize();
		// assertEquals(0, tokens.size());
	}

	@Test
	public void testOnlyWhitespace() throws TokenizerException {
		assertTokenizes("    ", new Token[0]);

		// Tokenizer tokenizer = new Tokenizer(" ");
		// List<Token> tokens = tokenizer.tokenize();
		// assertEquals(0, tokens.size());
	}
	
    @Test
    public void testIntByItself() throws TokenizerException {
        assertTokenizes("Int", new Token[] { new IntToken() });
						
/* 		Tokenizer tokenizer = new Tokenizer("Int");
		List<Token> tokens = tokenizer.tokenize();
		assertEquals(1, tokens.size());
		Token intToken = tokens.get(0);
		assertTrue(intToken instanceof IntToken); */
    }

	@Test
	public void testIntSpaceIntAreIntTokens() throws TokenizerException {
		assertTokenizes("Int Int", new Token[] { new IntToken(), new IntToken() });
	}

	@Test
	public void testBooleanByItself() throws TokenizerException {
		assertTokenizes("Boolean", new Token[] { new BooleanToken() });
	}

	@Test
	public void testStringByItself() throws TokenizerException {
		assertTokenizes("String", new Token[] { new StringToken() });
	}

	@Test
	public void testPlusByItself() throws TokenizerException {
		assertTokenizes("+", new Token[] { new PlusToken() });
	}

	@Test
	public void testMinusByItself() throws TokenizerException {
		assertTokenizes("-", new Token[] { new MinusToken() });
	}

	@Test
	public void testMultiplicationByItself() throws TokenizerException {
		assertTokenizes("*", new Token[] { new MultiplicationToken() });
	}

	@Test
	public void testDivisionByItself() throws TokenizerException {
		assertTokenizes("/", new Token[] { new DivisionToken() });
	}

	@Test
	public void testEqualByItself() throws TokenizerException {
		assertTokenizes("=", new Token[] { new EqualToken() });
	}

	@Test
	public void testSuperByItself() throws TokenizerException {
		assertTokenizes("super", new Token[] { new SuperToken() });
	}

	// Start of Ruben's token tests

	@Test
	public void testRightCurlyByItself() throws TokenizerException {
		assertTokenizes("}", new Token[] { new rightCurlyToken() });
	}

	@Test
	public void testtrueByItself() throws TokenizerException {
		assertTokenizes("true", new Token[] { new trueToken() });
	}

	@Test
	public void testFalseByItself() throws TokenizerException {
		assertTokenizes("false", new Token[] { new falseToken() });
	}

	@Test
	public void testLessThanByItself() throws TokenizerException {
		assertTokenizes("<", new Token[] { new lessThanToken() });
	}

	@Test
	public void testGreaterThanByItself() throws TokenizerException {
		assertTokenizes(">", new Token[] { new greaterThanToken() });
	}

	@Test
	public void testEqualEqualByItself() throws TokenizerException {
		assertTokenizes("==", new Token[] { new equalEqualToken() });
	}

	@Test
	public void testNotEqualByItself() throws TokenizerException {
		assertTokenizes("!=", new Token[] { new notEqualToken() });
	}

	// @Test I guess not was deleted by looking at the tokens doc
	// public void testNotByItself() throws TokenizerException {
	// assertTokenizes("!", new Token[] { new notToken() });
	// }

	// End of Ruben's test tokens

	// start of testing variables

	@Test
	public void testVariable() throws TokenizerException {
		assertTokenizes("foo", new Token[] { new VariableToken("foo") });
	}

	@Test
	public void testIntIntIsVariable() throws TokenizerException {
		assertTokenizes("IntInt", new Token[] { new VariableToken("IntInt") });
	}


	// 'this' token test
	@Test
	public void testThisByItself() throws TokenizerException {
		assertTokenizes("this", new Token[] { new ThisToken() });
	}

	// 'println' token test
	@Test
	public void testPrintlnByItself() throws TokenizerException {
		assertTokenizes("println", new Token[] { new PrintlnToken() });
	}

	// '(' token test
	@Test
	public void testOpenparByItself() throws TokenizerException {
		assertTokenizes("(", new Token[] { new OpenparToken() });
	}

	// ')' token test
	@Test
	public void testCloseparByItself() throws TokenizerException {
		assertTokenizes(")", new Token[] { new CloseparToken() });
	}

	// ';' token test
	@Test
	public void testSemicolByItself() throws TokenizerException {
		assertTokenizes(";", new Token[] { new SemicolToken() });
	}
	
	// '.' token test
	@Test
	public void testPeriodByItself() throws TokenizerException {
		assertTokenizes(".", new Token[] { new PeriodToken() });
	}
	
	
	// ',' token test
	@Test
	public void testCommaByItself() throws TokenizerException {
		assertTokenizes(",", new Token[] { new CommaToken() });
	}

	// 'new' token test
	@Test
	public void testNewByItself() throws TokenizerException {
		assertTokenizes("new", new Token[] { new NewToken() });
	}

	// 'class' token test
	@Test
	public void testClassByItself() throws TokenizerException {
		assertTokenizes("class", new Token[] { new ClassToken() });
	}

	// 'extends' token test
	@Test
	public void testExtendsByItself() throws TokenizerException {
		assertTokenizes("extends", new Token[] { new ExtendsToken() });
	}
  
  @Test
	public void testWhileByItself() throws TokenizerException {
    assertTokenizes("while", new Token[]{ new WhileToken() });
	}
  
	@Test
	public void testBreakByItself() throws TokenizerException {
		assertTokenizes("break", new Token[]{ new BreakToken() });
	}
	@Test
	public void testIfByItself() throws TokenizerException {
		assertTokenizes("if", new Token[]{ new IfToken() });
	}
	@Test
	public void testElseByItself() throws TokenizerException {
		assertTokenizes("else", new Token[]{ new ElseToken() });
	}
	@Test
	public void testReturnByItself() throws TokenizerException {
		assertTokenizes("return", new Token[]{ new ReturnToken() });
	}
	@Test
	public void testLeftCurlyByItself() throws TokenizerException {
		assertTokenizes("{", new Token[]{ new leftCurlyToken() });
	}
	
	//start of testing integers
	
	@Test
	public void testSingleDigitInteger() throws TokenizerException {
		assertTokenizes("1", new Token[] { new IntegerToken(1) });
	}

	@Test
	public void testMultiDigitInteger() throws TokenizerException {
		assertTokenizes("123", new Token[] { new IntegerToken(123) });
	}

	// start of testing invalid input

	@Test(expected = TokenizerException.class)
	public void testInvalid() throws TokenizerException {
		assertTokenizes("$", null);
	}
	
	@Test(expected = TokenizerException.class)
	public void testInvalidStr() throws TokenizerException {
		assertTokenizes("\"a", null);
	}
	
	@Test(expected = org.junit.internal.ArrayComparisonFailure.class)
	public void testInvalidInteger() throws TokenizerException {
		assertTokenizes("12a", new Token[] { new IntegerToken(12) });
	}
	
	//start of testing strings
	
	@Test
	public void testStrByItself() throws TokenizerException {
		assertTokenizes("\"hi\"", new Token[]{ new strToken("\"hi\"") });
	}
}