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
			final String name = ((VariableToken) token).name;
			return new ParseResult<Type>(new ClassnameType(new Classname(name)),
					position + 1);
		} else {
			throw new ParseException("");
		}
	}

	// primary_exp ::= var | str | int | true | false
	public ParseResult<Exp> parsePrimaryExp(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken) token).name;
			return new ParseResult<Exp>(new VariableExp(new Variable(name)), position + 1);
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

	// multiplicative_op ::= * | /
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

	// multiplicative_exp ::= primary_exp(multiplicative_op primary_exp)*
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
			}
		}

		return current;
	}

	// additive_op ::= + | /
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

	// additive_exp ::= multiplicative_exp (additive_op multiplicative_exp)*
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
			}
		}

		return current;
	}

	// comparison_op ::= < | > | == | !=
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
			throw new ParseException("expected <, >, == or !=; received: " + token);
		}
	}

	// comparsion_exp ::= additive_exp | additive_exp comparison_op additive_exp
	public ParseResult<Exp> parseComparisonExp(final int position) throws ParseException {
		ParseResult<Exp> current = parseAdditiveExp(position);
		try {
			final Token token = getToken(position + 1);
			if ((token instanceof lessThanToken) || (token instanceof greaterThanToken)
					|| (token instanceof equalEqualToken) || (token instanceof notEqualToken)) {
				final ParseResult<Op> comparisonOp = parseComparisonOp(current.position);
				final ParseResult<Exp> anotherAdditive = parseAdditiveExp(comparisonOp.position);
				current = new ParseResult<Exp>(new OpExp(current.result,
						comparisonOp.result,
						anotherAdditive.result),
						anotherAdditive.position);

			} else {
				return current;
			}
		} catch (final ParseException e) {
			return current;
		}
		return current;
	}

	// new classname(exp*)
	public ParseResult<Exp> parseNewClassExp(final int position) throws ParseException {
		Token token = getToken(position + 1); // should be the classname
		final String name = ((VariableToken) token).name;
		assertTokenHereIs(position + 1, new VariableToken(name));
		final ParseResult<Exp> className = parsePrimaryExp(position + 1);
		/*
		 * final ParseResult<Exp> className = parsePrimaryExp(position + 1);
		 * final ClassnameExp cN = new ClassnameExp(className.result);
		 */
		assertTokenHereIs(className.position, new OpenparToken());
		List<Exp> inParens = new ArrayList();
		int newPosition = className.position + 1;
		token = getToken(newPosition);
		final ParseResult<Exp> result;
		if (token instanceof CloseparToken) {
			result = new ParseResult<Exp>(new NewClassExp(className.result, inParens), newPosition + 1);
			// result = new ParseResult<Exp>(new NewClassExp(cN, inParens), newPosition +
			// 1);
		} else {
			boolean shouldRun = true;
			while (shouldRun) {
				try {
					final ParseResult<Exp> exp = parseExp(newPosition);
					inParens.add(exp.result);
					newPosition = exp.position;
				} catch (final ParseException e) {
					shouldRun = false;
				}
			}
			assertTokenHereIs(newPosition, new CloseparToken());
			result = new ParseResult<Exp>(new NewClassExp(className.result, inParens), newPosition + 1);
			// result = new ParseResult<Exp>(new NewClassExp(cN, inParens), newPosition +
			// 1);
		}
		return result;
	}

	// var.methodname(exp*)
	public ParseResult<Exp> parseVarMethodCall(final int position) throws ParseException {
		ParseResult<Exp> varName = parsePrimaryExp(position);
		final Token token = getToken(position + 2);
		final String name = ((VariableToken) token).name;
		assertTokenHereIs(position + 2, new VariableToken(name));
		ParseResult<Exp> methodName = parsePrimaryExp(position + 2);
		assertTokenHereIs(position + 3, new OpenparToken());
		List<Exp> inside = new ArrayList();
		Token token2 = getToken(position + 4);
		if (token2 instanceof CloseparToken) {
			return new ParseResult<Exp>(new VarMethodCall(varName.result, methodName.result, inside), position + 5);
		} else if (token2 instanceof VariableToken) {
			ParseResult<Exp> exp = parsePrimaryExp(position + 4);
			inside.add(exp.result);
			int iter = position + 5;
			token2 = getToken(iter);
			while (token2 instanceof VariableToken) { // cant use next
				ParseResult<Exp> exp2 = parsePrimaryExp(position + iter);
				inside.add(exp2.result);
				iter = iter + 1;
				token2 = getToken(iter);
			}
			assertTokenHereIs(position + 5, new CloseparToken());
			return new ParseResult<Exp>(new VarMethodCall(varName.result, methodName.result, inside), position + 6);
		} else {
			throw new ParseException("");
		}
	}

	// exp ::= var.methodname(exp*) | new classname(exp*) | comparison_exp
	public ParseResult<Exp> parseExp(final int position) throws ParseException {
		final Token token = getToken(position);

		if (token instanceof VariableToken) { // needs to assert that the next token is a .
			try {
				final Token token2 = getToken(position + 1);
				if (token2 instanceof PeriodToken) {
					return parseVarMethodCall(position);
				} else {
					return parseComparisonExp(position);
				}
			} catch (final ParseException e) {
				return parseComparisonExp(position);
			}
		} else if (token instanceof NewToken) {
			return parseNewClassExp(position); // new classname(exp*)
		} else {
			return parseComparisonExp(position); // comparison_exp
		}
	} // parseExp

	// vardec ::= type var = exp;
	public ParseResult<Vardec> parseVardec(final int position) throws ParseException {
		final Token token = getToken(position); // get type
		if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken)
				|| (token instanceof VariableToken)) {
			final Token token2 = getToken(position + 1); // get var
			final String name = ((VariableToken) token2).name;
			assertTokenHereIs(position + 1, new VariableToken(name));
			final Token token3 = getToken(position + 2);
			if (token3 instanceof EqualToken) {
				final ParseResult<Type> type = parseType(position);
				final ParseResult<Exp> variable = parsePrimaryExp(position + 1);
				final ParseResult<Exp> exp = parseExp(position + 3);
				assertTokenHereIs(position + 4, new SemicolToken());
				return new ParseResult<Vardec>(new VariableDeclaration(type.result, variable.result, exp.result),
						position + 5);
			} else {
				throw new ParseException("");
			}
		} else {
			throw new ParseException("");
		}
	}

	// param ::= type var
	// still needs to be implemented
	public ParseResult<Param> parseParam(final int position) throws ParseException {
		final Token token = getToken(position);
		if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken)
				|| (token instanceof VariableToken)) {
			final ParseResult<Type> type = parseType(position);
			final Token token2 = getToken(position + 1);
			final String name = ((VariableToken) token2).name;
			assertTokenHereIs(position + 1, new VariableToken(name));
			final ParseResult<Exp> varName = parsePrimaryExp(position + 1);
			return new ParseResult<Param>(new Parameter(type.result, varName.result), position + 2);
		} else {
			throw new ParseException("");
		}
	}
	
	
	//helper method for parseStmt
	public ParseResult<Stmt> parseBreakStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		if(token instanceof BreakToken ) {
			final Token token2 = getToken(position+1);
			final String b = ((BreakToken) token).toString();
			assertTokenHereIs(position + 1,new SemicolToken() );
			final String s = ((SemicolToken)token2).toString();
			return new ParseResult<Stmt>(new BreakStmt(b,s), position+2 );
			
		}
		else {
			throw new ParseException("Break token not found");
		}
	}

	
	//helper method for parseStmt
	public ParseResult<Stmt> parseSuperStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		if(token instanceof SuperToken) {
			assertTokenHereIs( position+1, new OpenparToken());
			final Token token2 = getToken(position + 2); // get var
			final String var = ((VariableToken) token2).name;
			assertTokenHereIs(position+2, new VariableToken(var) );
			assertTokenHereIs(position+3, new CloseparToken());
			assertTokenHereIs(position+4, new SemicolToken());
			return new ParseResult<Stmt>(new SuperStmt(((SuperToken)token).toString(),new VariableExp(new Variable(var))),position+5);
		}
		else {
			throw new ParseException("SuperToken not found");
		}
	}
	
	//helper method for parseStmt
	public ParseResult<Stmt> parseThisStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		if(token instanceof ThisToken) {
			assertTokenHereIs(position+1, new PeriodToken());
			final Token token2 = getToken(position + 2); // get var
			final String var = ((VariableToken) token2).name;
			assertTokenHereIs(position+2, new VariableToken(var));
			assertTokenHereIs(position+3, new EqualToken());
			final Token token3 = getToken(position + 4); // get var2
			final String var2 = ((VariableToken) token3).name;
			assertTokenHereIs(position+4,new VariableToken(var2));
			assertTokenHereIs(position+5, new SemicolToken());
			return new ParseResult<Stmt>(new ThisStmt(new VariableExp(new Variable(var)), new VariableExp(new Variable(var2))),position+6);

		}
		else {
			throw new ParseException("ThisToken not found");
		}

	}

	 
	// stmt ::= var = exp; | vardec | while (exp) stmt | break; | if (exp) stmt else stmt | return exp;
	//			| {stmt*} | println(exp*) | super(var); | this.var = var; | exp;
	public ParseResult<Stmt> parseStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		// vardec
		if ((token instanceof VariableToken) && (getToken(position + 1) instanceof EqualToken)) {
			final ParseResult<Exp> variable = parsePrimaryExp(position);
			final ParseResult<Exp> exp = parseExp(position + 2);
			assertTokenHereIs(exp.position, new SemicolToken());
			return new ParseResult<Stmt>(new VariableValueChange(variable.result, exp.result), exp.position + 1);
		} else if( (token instanceof IntToken) || (token instanceof BooleanToken) || (token
				 instanceof StringToken)  || (token instanceof VariableToken)) {
			final Token token2 = getToken(position + 1);
			if (token2 instanceof VariableToken) {
				final String token2Name = ((VariableToken)token2).name;
				assertTokenHereIs(position + 1, new VariableToken(token2Name));
				final ParseResult<Vardec> declare = parseVardec(position);
				return new ParseResult<Stmt>(new VardecStmt(declare), declare.position);
			} else {
				throw new ParseException("");
			}
		} else if (token instanceof WhileToken) {
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> guard = parseExp(position + 2);
			assertTokenHereIs(guard.position, new CloseparToken());
			final ParseResult<Stmt> whileStmt = parseStmt(guard.position + 1);
			return new ParseResult<Stmt>(new WhileStmt(guard.result, whileStmt.result), whileStmt.position);
		} else if (token instanceof BreakToken ) {
			final ParseResult<Stmt> breakResult;
			breakResult = parseBreakStmt(position);
			return breakResult;
		} else if(token instanceof IfToken) {
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> ifGuard = parseExp(position + 2);
			assertTokenHereIs(ifGuard.position, new CloseparToken());
			final ParseResult<Stmt> trueBranch = parseStmt(ifGuard.position + 1);
			assertTokenHereIs(trueBranch.position, new ElseToken());
			final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
			return new ParseResult<Stmt>(new IfStmt(ifGuard.result, trueBranch.result, falseBranch.result), falseBranch.position);
		} else if (token instanceof ReturnToken) {
			final ParseResult<Exp> retExp = parseExp(position + 1);
			assertTokenHereIs(retExp.position, new SemicolToken());
			return new ParseResult<Stmt>(new ReturnStmt(retExp.result), retExp.position + 1);
		} else if (token instanceof leftCurlyToken) {
			List<Stmt> stmts = new ArrayList();
			Token nextToken = getToken(position + 1);
			if (nextToken instanceof rightCurlyToken) {
				return new ParseResult<Stmt>(new BlockStmt(stmts), position + 2);
			} else {
				ParseResult<Stmt> stmt1 = parseStmt(position + 1);
				stmts.add(stmt1.result);
				int iter = stmt1.position;
				nextToken = getToken(iter);
				while (!(nextToken instanceof rightCurlyToken)) {
					ParseResult<Stmt> stmt2 = parseStmt(iter);
					stmts.add(stmt2.result);
					iter = iter + 1;
					nextToken = getToken(iter);
				}
				assertTokenHereIs(iter, new rightCurlyToken());
				return new ParseResult<Stmt>(new BlockStmt(stmts), iter + 1);
			}
		}
/* 	  else if (token instanceof PrintlnToken) {
		  assertTokenHereIs(position + 1, new OpenparToken());
		  final ParseResult<Exp> exp = parseExp(position + 2);
		  assertTokenHereIs(exp.position, new CloseparToken());
		  assertTokenHereIs(exp.position + 1, new SemicolToken());
		  return new ParseResult<Stmt>(new PrintlnStmt(exp.result), exp.position + 2);
		  } */
		 else if (token instanceof SuperToken) {
			 final ParseResult<Stmt> superStmt = parseSuperStmt(position);
			 return superStmt;
		 } else if (token instanceof ThisToken) {
			 final ParseResult<Stmt> thisStmt = parseThisStmt(position);
			 return thisStmt;
		 } else {
			final ParseResult<Exp> compExp = parseComparisonExp(position);
			final ParseResult<Stmt> compStmt = new ParseResult<Stmt>(new ExpStmt(compExp.result), compExp.position);
			return compStmt;
		}
	}
	
	

	/*
	 * // methoddef ::= type methodname(param*) stmt
	 * public ParseResult<Methoddef> parseMethodDef(final int position) throws
	 * ParseException {
	 * final Token token = getToken(position);
	 * if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token
	 * instanceof StringToken) || (token instanceof VariableToken)) {
	 * final ParseResult<Type> type = parseType(position); // parse in the type
	 * final Token token2 = getToken(position + 1);
	 * final String nameToken2 = ((VariableToken)token2).name;
	 * assertTokenHereIs(position + 1, new VariableToken(name));
	 * final ParseResult<Exp> methodName = parsePrimaryExp(position + 1); // parse
	 * in the methodname
	 * assertTokenHereIs(position + 2, new OpenparToken());
	 * //here is where I'll deal with the list of params but let me try without it
	 * first
	 * //so currently we can only pass an empty list of params
	 * assertTokenHereIs(position + 3, new CloseparToken());
	 * final ParseResult<Stmt> stmt = new parseStmt(position + 4); //isn't gonna
	 * work until stmt is made
	 * } else {
	 * throw new ParseException("");
	 * }
	 * }
	 */

	// classdef ::= class classname extends classname {
	// vardec*
	// constructor(param*) stmt
	// methoddef*
	// }
	/*
	 * public ParseResult<Classdef> parseClassdef(final int position) throws
	 * ParseException {
	 * final Token token = getToken(position);
	 * if (token instanceof ClassToken) {
	 * final ParseResult<Exp> classname = parsePrimaryExp(position + 1); //parse in
	 * classname
	 * token = getToken(classname.position);
	 * position = position + 2; //sets position to thing after classname
	 * if (token instanceof ExtendsToken) { //if the class extends another class
	 * assertTokenHereIs(classname.position + 1, VariableToken());
	 * final ParseResult<Exp> extendsClassname = parsePrimaryExp(classname.position
	 * + 1); //parse in name of class it's extending
	 * token = getToken(extendsClassname.position); //update the next token to be
	 * read
	 * position = position + 2; //position is now set the thing after the extends
	 * classname
	 * }
	 *
	 * //here need to parse in the 0 or more vardec = exp;
	 * if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token
	 * instanceof StringToken)) { //if we read in a type
	 * assertTokenHereIs(position + 1, VariableToken()); //assert the next thing is
	 * a variable
	 * assertTokenHereIs(position + 2, EqualToken()); //assert the next thing is
	 * assignment operator (ensures that this is a variable instantiation) we know
	 * we have at least one variable instantiation
	 * boolean shouldRun = true;
	 * final List<ParseResult> vardecs = new ArrayList<ParseResult>();
	 * while (shouldRun) {
	 * try {
	 * final ParseResult<Vardec> vardec = parseVardec(position); //calls parseVardec
	 * method to parse the "type var" section
	 * vardecs.add(vardec.result);
	 * position = vardec.position;
	 * } catch (final ParseException e) {
	 * shouldRun = false;
	 * }
	 * }
	 * }
	 *
	 * //here is where a constructor might appear
	 * if (token instanceof VariableToken) {
	 * assertTokenHereIs(position, OpenparToken()); //we know that we've hit a
	 * constructor
	 * boolean shouldRun1 = true;
	 * final List<ParseResult> constructorVariables = new ArrayList<ParseResult>();
	 * while (shouldRun1) { // loop to read in the vardecs inside the constructor
	 * try {
	 * final ParseResult<Vardec> vardec1 = parseVardec(position + 1);
	 * constructorVariables.add(vardec1.result);
	 * position = vardec1.position;
	 * } catch (final ParseException e) {
	 * shouldRun1 = false;
	 * }
	 * }
	 * assertTokenHereIs(position, CloseparToken()); //end of vardecs
	 * final List<stmt> stmts = new ArrayList<Stmt>(); //can't figure out how to
	 * make condition for 0 stmts so maybe will rely on parseexceptions?
	 * boolean shouldRun2 = true;
	 * while (shouldRun2) {
	 * try {
	 * final ParseResult<Stmt> stmt = parseStmt(position + 1); //dependent on
	 * parseStmt, yet to be made
	 * stmts.add(stmt.result);
	 * position = stmt.position;
	 * } catch (final ParseException e) {
	 * shouldRun2 = false;
	 * }
	 * }
	 * //should be end of stmts and now there are optional methoddefs
	 * //again, can't figure out how to make condition for 0 stmts so maybe will
	 * rely on exceptions?
	 * final List<Methoddef> methoddefs = new ArrayList<Methoddef>(); //need to make
	 * Methoddef interface still
	 * boolean shouldRun3 = true;
	 * while (shouldRun3) {
	 * try {
	 * final ParseResult<Methoddef> methoddef = parseMethodDef(position); //still
	 * need parseMethodDef method
	 * methoddefs.add(methoddef.result);
	 * position = methoddef.postion;
	 * } catch (final ParseException e) {
	 * shouldRun3 = false;
	 * }
	 * }
	 * }
	 * }
	 * else {
	 * throw ParseException("");
	 * }
	 * }
	 */
	 
	 
	  
	  

	// still won't work because we need to finish the other methods it calls
	/*
	 * //new code
	 * public ParseResult<Program> parseProgram(final int position) throws
	 * ParseException {
	 * //mine will be different than Kyle's because we have two lists instead of one
	 * stmt
	 * final List<Classdef> classes = new ArrayList<Classdef>();
	 * final List<Stmt> stmts = new ArrayList<Stmt>();
	 * boolean shouldRunClasses = true;
	 * boolean shouldRunStmts = true;
	 * int currentPos = position;
	 * while (shouldRunClasses) {
	 * try {
	 * final ParseResult<Classdef> theClass = parseClassdef(currentPos);
	 * classes.add(theClass);
	 * currentPos = theClass.position;
	 * } catch (final ParseException e) {
	 * shouldRunClasses = false;
	 * }
	 * }
	 * while (shouldRunStmts) {
	 * try {
	 * final ParseResult<Stmt> stmt = parseStmt(currentPos);
	 * stmts.add(stmt);
	 * currentPos = stmt.position;
	 * } catch (final ParseException e) {
	 * shouldRunStmts = false;
	 * }
	 * }
	 * return new ParseResult<Program>(new Program(classes, stmts), currentPos);
	 * }
	 *
	 *
	 * public Program parseProgram() throws ParseException {
	 * final ParseResult<Program> program = parseProgram(0);
	 * if (program.position == tokens.size()) {
	 * return program.result;
	 * } else {
	 * throw new ParseException("Remaining tokens at end");
	 * }
	 * }
	 */

}