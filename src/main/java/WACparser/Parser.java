package WACparser;

import WAClexer.*;

import java.util.List;
import java.util.ArrayList;

public class Parser {
	private final List<Token> tokens;

	public Parser(final List<Token> tokens) {
		this.tokens = tokens;
	}

	public Token getToken(final int position) throws ParseException {
		if (position >= 0 && position < tokens.size()) {
			return tokens.get(position);
		} else {
			throw new ParseException("Invalid token position: " + position);
		}
	}

	public void assertTokenHereIs(final int position, final Token expected) throws ParseException {
		final Token received = getToken(position);
		if (!expected.equals(received)) {
			throw new ParseException("expected: " + expected + "; received: " + received);
		}
	}

	public ParseResult<Op> parseAdditiveOp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof PlusToken) {
			return new ParseResult<Op>(new PlusOp(), position + 1);
		} else if (token instanceof MinusToken) {
			return new ParseResult<Op>(new MinusOp(), position + 1);
		} else {
			throw new ParseException("expected + or -; received: " + token);
		}
	}
	
	//start of Sarah's methods
	
/* 	public ParseResult<Vardec> parseVardec(final int position) throws ParseException {
		final Token token = getToken(position);
		if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken)) {
			final ParseResult<Type> type = parseType(position);
			assertTokenHereIs(type.position, new VariableToken("hi")); //CHECK THIS LATER WITH TESTING
			final ParseResult<Exp> varName = parsePrimaryExp(type.position);
			assertTokenHereIs(varName.position, new EqualToken());
			// MISSING parseExp!!!
			final ParseResult<Exp> value = parseExp(varName.position + 1);
			assertTokenHereIs(value.position, new SemicolToken());
			return new ParseResult<Vardec>(new VariableDeclaration(type.result, varName.result, value.result), value.position + 1);
		} else {
			throw new ParseException("Expected a variable declaration but received: " +  token);
		}
	} */
	
/* 	public ParseResult<Classdef> parseClassdef(final int position) throws ParseException {
		final Token token = getToken(position);
		if  (token instanceof ClassToken) {
			final ParseResult<Exp> classname = parsePrimaryExp(position + 1);	//parse in classname
			token = getToken(classname.position);
			position = position + 2;	//sets position to thing after classname
			if (token instanceof ExtendsToken) {	//if the class extends another class
				assertTokenHereIs(classname.position + 1, VariableToken());
				final ParseResult<Exp> extendsClassname = parsePrimaryExp(classname.position + 1);	//parse in name of class it's extending
				token = getToken(extendsClassname.position);	//update the next token to be read
				position = position + 2;	//position is now set the thing after the extends classname
			}
			
			//here need to parse in the 0 or more vardec = exp;
			if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken)) {	//if we read in a type
				assertTokenHereIs(position + 1, VariableToken());	//assert the next thing is a variable
				assertTokenHereIs(position + 2, EqualToken());	//assert the next thing is assignment operator (ensures that this is a variable instantiation) we know we have at least one variable instantiation
				boolean shouldRun = true;
				final List<ParseResult> vardecs = new ArrayList<ParseResult>();
				while (shouldRun) {
					try {
						final ParseResult<Vardec> vardec = parseVardec(position);	//calls parseVardec method to parse the "type var" section
						vardecs.add(vardec.result);
						position = vardec.position;
					} catch (final ParseException e) {
						shouldRun = false;
					}
				}
			}
			
			//here is where a constructor might appear
			if (token instanceof VariableToken) {
				assertTokenHereIs(position, OpenparToken());	//we know that we've hit a constructor
				boolean shouldRun1 = true;
				final List<ParseResult> constructorVariables = new ArrayList<ParseResult>();
				while (shouldRun1) {	// loop to read in the vardecs inside the constructor
					try {
						final ParseResult<Vardec> vardec1 = parseVardec(position + 1);
						constructorVariables.add(vardec1.result);
						position = vardec1.position;
					} catch (final ParseException e) {
						shouldRun1 = false;
					}
				}
				assertTokenHereIs(position, CloseparToken());	//end of vardecs
				final List<stmt> stmts = new ArrayList<Stmt>();		//can't figure out how to make condition for 0 stmts so maybe will rely on parseexceptions?
				boolean shouldRun2 = true;
				while (shouldRun2) {
					try {
						final ParseResult<Stmt> stmt = parseStmt(position + 1);	//dependent on parseStmt, yet to be made
						stmts.add(stmt.result);
						position = stmt.position;
					} catch (final ParseException e) {
						shouldRun2 = false;
					}
				}
				//should be end of stmts and now there are optional methoddefs
				//again, can't figure out how to make condition for 0 stmts so maybe will rely on exceptions?
				final List<Methoddef> methoddefs = new ArrayList<Methoddef>();	//need to make Methoddef interface still
				boolean shouldRun3 = true;
				while (shouldRun3) {
					try {
						final ParseResult<Methoddef> methoddef = parseMethodDef(position);	//still need parseMethodDef method
						methoddefs.add(methoddef.result);
						position = methoddef.postion;
					} catch (final ParseException e) {
						shouldRun3 = false;
					}
				}
			}
		}
		else {
			throw ParseException("");
		}
	} */
	
	//still need parseMethoddef
	
	//end of Sarah's methods
	
	
/* 	 
	// stmt ::=  vardec ; |var = exp; | while (exp)  stmt |  break; |  if (exp) stmt else stmt | return exp; |   stmt* |println(exp*); |
	//           super(var); |this.var = var; | exp;	 
	public ParseResult<Stmt> parseStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		if( (token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken) ) {
			final ParseResult<Vardec>> declare = parseVardec(position + 1);
			assertTokenHereIs(declare.position, new VariableToken(token.toString()));
			return new ParseResult<Stmt>>(new VariableDeclaration(), declare.position);
		 }
		else if((token instanceof VariableToken) && !((getToken(position - 1) instanceof IntToken) || (getToken(position - 1) instanceof BooleanToken) || (getToken(position - 1) instanceof StringToken))){
			assertTokenHereIs(position + 1 , new EqualToken());
			final ParseResult<Exp> guard = parseExp(position + 2);
			assertTokenHereIs(guard.position , new SemicolToken());
			return new ParseResult<Stmt>(new VariableValueChange(), guard.position + 1);
		}
		else if(token instanceof WhileToken) {
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> guard = parseExp(position + 2);
			assertTokenHereIs(guard.position, new CloseparToken());
			final ParseResult<Stmt> loopBranch = parseStmt(guard.position + 1);
			return new ParseResult<Stmt>(new WhileStmt(), loopBranch.position);
		}
		else if (token instanceof BreakToken ) {
			assertTokenHereIs(position + 1, new SemicolToken());
			return new ParseResult<Stmt>(new BreakStmt(), position);
		}
		
		else if (token instanceof IfToken) {
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> guard = parseExp(position + 2);
			assertTokenHereIs(guard.position, new CloseparToken());
			final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
			assertTokenHereIs(trueBranch.position, new ElseToken());
			final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
			return new ParseResult<Stmt>(new IfStmt(guard.result, trueBranch.result, falseBranch.result), falseBranch.position);
		}
		else if (token instanceof ReturnToken) {
			final ParseResult<Exp> guard = parseExp(position + 1);
			assertTokenHereIs(guard.position, new SemicolToken());
			return new ParseResult<Stmt>(new ReturnStmt(), guard.position + 1);
		}
		else if (token instanceof leftCurlyToken) {
			final List<Stmt> stmts = new ArrayList<Stmt>();
			int curPosition = position + 1;
			boolean shouldRun = true;
			while (shouldRun) {
				try {
					final ParseResult<Stmt> stmt = parseStmt(curPosition);
					stmts.add(stmt.result);
					curPosition = stmt.position;
				} catch (final ParseException e) {
					shouldRun = false;
				}
			}
			return new ParseResult<Stmt>(new BlockStmt(stmts), curPosition);
		} 
		else if (token instanceof PrintlnToken) {
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> exp = parseExp(position + 2);
			assertTokenHereIs(exp.position, new CloseparToken());
			assertTokenHereIs(exp.position + 1, new SemicolToken());
			return new ParseResult<Stmt>(new PrintlnStmt(exp.result), exp.position + 2);
		} 
		else if (token instanceof SuperToken) {
			assertTokenHereIs(position + 1, new SuperToken());
			assertTokenHereIs(position + 2, new OpenparToken());
			assertTokenHereIs(position + 3, new VariableToken(token.toString()) );
			assertTokenHereIs(position + 4, new SemicolToken());
			return new ParseResult<Stmt>(new SuperStmt(), position +5);
		}
		else if(token instanceof ThisToken) {
			assertTokenHereIs(position + 1 , new PeriodToken());
			assertTokenHereIs(position + 2, new VariableToken(token.toString()) );
			assertTokenHereIs(position + 3 , new EqualToken());
			assertTokenHereIs(position + 4 , new VariableToken(token.toString()));
			return new ParseResult<Stmt>(new ThisStmt(), position +5);
		}
		
		
		else {
			throw new ParseException("expected statement; received: " + token); */

	
	//start of Ruben's methods


	// type ::= Int | Boolean | String | classname
	public ParseResult<Type> parseType(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof IntToken) {
			return new ParseResult<Type>(new IntType(), position + 1);
		} else if (token instanceof BooleanToken) {
			return new ParseResult<Type>(new BooleanType(), position + 1);
		} else if (token instanceof StringToken) {
			return new ParseResult<Type>(new StringType(), position + 1);
		} else if (token instanceof VariableToken) {
			return new ParseResult<Type>(new ClassnameType(new VariableExp(token.toString())), position + 1);
		} else {
			throw new ParseException("");
		}
	}

	// primary_exp ::= var | str | int | true | false
	public ParseResult<Exp> parsePrimaryExp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken) token).name;
			return new ParseResult<Exp>(new VariableExp(name), position + 1);
		} else if (token instanceof strToken) {
			final String value = ((strToken) token).value;
			return new ParseResult<Exp>(new StrExp(value), position + 1);
		} else if (token instanceof IntegerToken) {
			final int value = ((IntegerToken) token).value;
			return new ParseResult<Exp>(new IntegerExp(value), position + 1);
		} else if (token instanceof trueToken) {
			return new ParseResult<Exp>(new TrueExp(), position + 1);
		} else if (token instanceof falseToken) {
			return new ParseResult<Exp>(new FalseExp(), position + 1);
		} else {
			throw new ParseException("");
		}
	}

	public ParseResult<Op> parseMultiplicativeOp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof MultiplicationToken) {
			return new ParseResult<Op>(new MultiplicationOp(), position + 1);
		} else if (token instanceof DivisionToken) {
			return new ParseResult<Op>(new DivisionOp(), position + 1);
		} else {
			throw new ParseException("expected * or /; received: " + token);
		}
	}

	public ParseResult<Exp> parseMultiplicativeExp(final int position) throws ParseException {
		ParseResult<Exp> current = parsePrimaryExp(position);
		boolean shouldRun = true;

		while (shouldRun) {
			try {
				final ParseResult<Op> multiplicativeOp = parseMultiplicativeOp(current.position);
				final ParseResult<Exp> anotherPrimary = parsePrimaryExp(multiplicativeOp.position);
				current = new ParseResult<Exp>(new OpExp(current.result,
						multiplicativeOp.result,
						anotherPrimary.result),
						anotherPrimary.position);
			} catch (final ParseException e) {
				shouldRun = false;
				throw new ParseException("");
			}
		}

		return current;
	}

	// additive expression parsing
	public ParseResult<Exp> parseAdditiveExp(final int position) throws ParseException {
		ParseResult<Exp> current = parseMultiplicativeExp(position);
		boolean shouldRun = true;

		while (shouldRun) {
			try {
				final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
				final ParseResult<Exp> anotherMultiplicative = parseMultiplicativeExp(additiveOp.position);
				current = new ParseResult<Exp>(new OpExp(current.result,
						additiveOp.result,
						anotherMultiplicative.result),
						anotherMultiplicative.position);
			} catch (final ParseException e) {
				shouldRun = false;
				throw new ParseException("");
			}
		}

		return current;
	}

	public ParseResult<Op> parseComparisonOp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof lessThanToken) {
			return new ParseResult<Op>(new LessThanOp(), position + 1);
		} else if (token instanceof greaterThanToken) {
			return new ParseResult<Op>(new GreaterThanOp(), position + 1);
		} else if (token instanceof equalEqualToken) {
			return new ParseResult<Op>(new EqualEqualsOp(), position + 1);
		} else if (token instanceof notEqualToken) {
			return new ParseResult<Op>(new NotEqualsOp(), position + 1);
		} else {
			throw new ParseException("expected * or /; received: " + token);
		}
	}

	public ParseResult<Exp> parseComparisonExp(final int position) throws ParseException {
		ParseResult<Exp> current = parseAdditiveExp(position);
		final Token token = getToken(position);

		if ((token instanceof lessThanToken) || (token instanceof greaterThanToken) || (token instanceof equalEqualToken) || (token instanceof notEqualToken)) {
			final ParseResult<Op> comparisonOp = parseComparisonOp(current.position);
			final ParseResult<Exp> anotherAdditive = parseAdditiveExp(comparisonOp.position);
			current = new ParseResult<Exp>(new OpExp(current.result,
					comparisonOp.result,
					anotherAdditive.result),
					anotherAdditive.position);
		} else {
			return current;
		}
		return current;
	}

	// end of Ruben's methods
	
	
	//helpful comments down here

	/*
	 * // additive_op ::= + | -
	 * public ParseResult<Op> parseAdditiveOp(final int position) throws
	 * ParseException {
	 * final Token token = getToken(position);
	 * if (token instanceof PlusToken) {
	 * return new ParseResult<Op>(new PlusOp(), position + 1);
	 * } else if (token instanceof MinusToken) {
	 * return new ParseResult<Op>(new MinusOp(), position + 1);
	 * } else {
	 * throw new ParseException("expected + or -; received: " + token);
	 * }
	 * }
	 * 
	 * //additive_exp ::= primary_exp (additive_op primary_exp)*
	 * public ParseResult<Exp> parseAdditiveExp(final int position) throws
	 * ParseException {
	 * ParseResult<Exp> current = parsePrimaryExp(position);
	 * boolean shouldRun = true;
	 * 
	 * while (shouldRun) {
	 * try {
	 * final ParseResult<Op> additiveOp = parseAdditiveOp(current.position);
	 * final ParseResult<Exp> anotherPrimary = parsePrimaryExp(additiveOp.position);
	 * current = new ParseResult<Exp>(new OpExp(current.result, additiveOp.result,
	 * anotherPrimary.result), anotherPrimary.position);
	 * } catch (final ParseException e) {
	 * shouldRun = false;
	 * }
	 * }
	 * return current;
	 * }
	 * 
	 * // stmt ::= if (exp) stmt else stmt | { stmt }* | println(exp);
	 * public ParseResult<Stmt> parseStmt(final int position) throws ParseException
	 * {
	 * final Token token = getToken(position);
	 * if (token instanceof IfToken) {
	 * assertTokenHereIs(position + 1, new OpenparToken());
	 * final ParseResult<Exp> guard = parseExp(position + 2);
	 * assertTokenHereIs(guard.position, new CloseparToken());
	 * final ParseResult<Stmt> trueBranch = parseStmt(guard.position + 1);
	 * assertTokenHereIs(trueBranch.position, new ElseToken());
	 * final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
	 * return new ParseResult<Stmt>(new IfExp(guard.result, trueBranch.result,
	 * falseBranch.result), falseBranch.position);
	 * } else if (token instanceof leftCurlyToken) {
	 * final List<Stmt> stmts = new ArrayList<Stmt>();
	 * int curPosition = position + 1;
	 * boolean shouldRun = true;
	 * while (shouldRun) {
	 * try {
	 * final ParseResult<Stmt> stmt = parseStmt(curPosition);
	 * stmts.add(stmt.result);
	 * curPosition = stmt.position;
	 * } catch (final ParseException e) {
	 * shouldRun = false;
	 * }
	 * }
	 * return new ParseResult<Stmt>(new BlockStmt(stmts), curPosition);
	 * } else if (token instanceof PrintlnToken) {
	 * assertTokenHereIs(position + 1, new OpenparToken());
	 * final ParseResult<Exp> exp = parseExp(position + 2);
	 * assertTokenHereIs(exp.position, new CloseparToken());
	 * assertTokenHereIs(exp.position + 1, new SemicolToken());
	 * return new ParseResult<Stmt>(new PrintlnStmt(exp.result), exp.position + 2);
	 * } else {
	 * throw new ParseException("expected statement; received: " + token);
	 * }
	 * }
	 */

	// end of helpful comments

	public ParseResult parseSingle() throws ParseException {
		ParseResult retval;
		retval = parseAdditiveOp(0);
		return retval;
	}

	public List<ParseResult> parse() throws ParseException {
		final List<ParseResult> results = new ArrayList<ParseResult>();
		ParseResult<Op> result1 = parseSingle();
		results.add(result1);
		return results;
	}
}