package WACparser;

import WAClexer.*;

import java.util.List;
import java.util.ArrayList;
//import java.util.*;

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
			throw new ParseException("Expected IntToken, BooleanToken, StringToken, or VariableToken but received: " + token.toString());
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
			throw new ParseException("Expected a primary expression but received: " + token.toString());
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

	
	//helper method to get back the ClassnameExp
	public ParseResult<ClassnameExp> parseClassName(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken)token).name;
			return new ParseResult<ClassnameExp>(new ClassnameExp(new Classname(name)), position + 1);
		} else {
			throw new ParseException("Expected a VariableToken but recieved: " + token.toString());
		}
	}


	// new classname(exp*)
	public ParseResult<Exp> parseNewClassExp(final int position) throws ParseException {
		Token token = getToken(position + 1); // should be the classname
		final String name = ((VariableToken) token).name;
		assertTokenHereIs(position + 1, new VariableToken(name));
		//final ParseResult<Exp> className = parsePrimaryExp(position + 1);
		final ParseResult<ClassnameExp> className = parseClassName(position + 1);
		assertTokenHereIs(className.position, new OpenparToken());
		List<Exp> inParens = new ArrayList();
		int newPosition = className.position + 1;
		token = getToken(newPosition);
		final ParseResult<Exp> result;
		if (token instanceof CloseparToken) {
			result = new ParseResult<Exp>(new NewClassExp(className.result, inParens), newPosition + 1);
		} else {
			boolean shouldRun = true;
			while (shouldRun) {
				try {
					final ParseResult<Exp> exp = parseExp(newPosition);
					inParens.add(exp.result);
					Token testToken = getToken(exp.position);
					if (testToken instanceof CommaToken) {
						newPosition = exp.position + 1;
					} else {
						newPosition = exp.position;
					}
				} catch (final ParseException e) {
					shouldRun = false;
				}
			}
			assertTokenHereIs(newPosition, new CloseparToken());
			result = new ParseResult<Exp>(new NewClassExp(className.result, inParens), newPosition + 1);
		}
		return result;
	}


	//helper method to get back the MethodNameExp
	public ParseResult<MethodNameExp> parseMethodName(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken)token).name;
			return new ParseResult<MethodNameExp>(new MethodNameExp(new Methodname(name)), position + 1);
		} else {
			throw new ParseException("Expected a VariableToken but recieved: " + token.toString());
		}
	}

	// var.methodname(primary_exp*)
	public ParseResult<Exp> parseVarMethodCall(final int position) throws ParseException {
		ParseResult<Exp> varName = parsePrimaryExp(position);
		final Token token = getToken(position + 2);
		final String name = ((VariableToken) token).name;
		assertTokenHereIs(position + 2, new VariableToken(name));
		//ParseResult<Exp> methodName = parsePrimaryExp(position + 2);		//parse in methodname
		ParseResult<MethodNameExp> methodName = parseMethodName(position + 2);
		assertTokenHereIs(position + 3, new OpenparToken());
		List<Exp> inside = new ArrayList();
		Token token2 = getToken(position + 4);
		if (token2 instanceof CloseparToken) {
			//return new ParseResult<Exp>(new VarMethodCall(varName.result, methodName.result, inside), position + 5);
			return new ParseResult<Exp>(new VarMethodCall(varName.result, methodName.result, inside), position + 5);
		} else if ((token2 instanceof VariableToken) || (token2 instanceof IntegerToken) || (token2 instanceof strToken) 
					|| (token2 instanceof trueToken) || (token2 instanceof falseToken)) {
			ParseResult<Exp> exp = parsePrimaryExp(position + 4);
			inside.add(exp.result);
			int iter = position + 5;
			token2 = getToken(iter);
			if (!(token2 instanceof CloseparToken)) {
				Token commaToken = getToken(position + 5);
				iter = position + 6;
				token2 = getToken(iter);
				while ((commaToken instanceof CommaToken) && ((token2 instanceof VariableToken) || (token2 instanceof IntegerToken) || (token2 instanceof strToken) 
						|| (token2 instanceof trueToken) || (token2 instanceof falseToken))) {
					ParseResult<Exp> exp2 = parsePrimaryExp(iter);
					inside.add(exp2.result);
					iter = iter + 1;
					token2 = getToken(iter);
					if (!(token2 instanceof CloseparToken)) {
						iter = iter + 1;
						commaToken = getToken(iter-1);
						token2 = getToken(iter);
					}
				}
			}
			assertTokenHereIs(iter, new CloseparToken());
			return new ParseResult<Exp>(new VarMethodCall(varName.result, methodName.result, inside), iter + 1);
		} else {
			throw new ParseException("Expected a Closepar or a Primary Expression but received: " + token.toString());
		}
	}

	// exp ::= var.methodname(exp*) | new classname(exp*) | comparison_exp
	public ParseResult<Exp> parseExp(final int position) throws ParseException {
		final Token token = getToken(position);

		if (token instanceof VariableToken) {
			try {
				final Token token2 = getToken(position + 1);
				assertTokenHereIs(position + 1, new PeriodToken());
				return parseVarMethodCall(position);
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
	public ParseResult<VariableDeclaration> parseVardec(final int position) throws ParseException {
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
				assertTokenHereIs(exp.position, new SemicolToken());
				return new ParseResult<VariableDeclaration>(new VariableDeclaration(type.result, variable.result, exp.result),
						exp.position + 1);
			} else {
				throw new ParseException("Expected an = token but received: " + token3.toString());
			}
		} else {
			throw new ParseException("Expected a type but received: " + token.toString());
		}
	}

	// param ::= type var
	// still needs to be implemented
	public ParseResult<Parameter> parseParam(final int position) throws ParseException {
		final Token token = getToken(position);
		if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken)
				|| (token instanceof VariableToken)) {
			final ParseResult<Type> type = parseType(position);
			final Token token2 = getToken(position + 1);
			final String name = ((VariableToken) token2).name;
			assertTokenHereIs(position + 1, new VariableToken(name));
			final ParseResult<Exp> varName = parsePrimaryExp(position + 1);
			return new ParseResult<Parameter>(new Parameter(type.result, varName.result), position + 2);
		} else {
			throw new ParseException("Expected a type but recived: " + token.toString());
		}
	}
	
	
	//helper method for parseStmt
	public ParseResult<Stmt> parseBreakStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		final Token token2 = getToken(position+1);
		final String b = ((BreakToken) token).toString();
		assertTokenHereIs(position + 1,new SemicolToken() );
		final String s = ((SemicolToken)token2).toString();
		return new ParseResult<Stmt>(new BreakStmt(b,s), position+2 );
	}

	
	//helper method for parseStmt
	public ParseResult<Stmt> parseSuperStmt(final int position) throws ParseException {
		final Token token = getToken(position);
					assertTokenHereIs( position+1, new OpenparToken());
		final Token token2 = getToken(position + 2); // get var
		final String var = ((VariableToken) token2).name;
		assertTokenHereIs(position+2, new VariableToken(var) );
		assertTokenHereIs(position+3, new CloseparToken());
		assertTokenHereIs(position+4, new SemicolToken());
		return new ParseResult<Stmt>(new SuperStmt(new VariableExp(new Variable(var))), position+5);
	}
	
	
	//helper method for parseStmt
	public ParseResult<Stmt> parseThisStmt(final int position) throws ParseException {
		final Token token = getToken(position);
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

	 
	// stmt ::= var = exp; | vardec | while (exp) stmt | break; | if (exp) stmt else stmt | return exp;
	//			| {stmt*} | println(exp*); | super(var); | this.var = var; | exp;
	public ParseResult<Stmt> parseStmt(final int position) throws ParseException {
		final Token token = getToken(position);
		if ((token instanceof VariableToken) && (getToken(position + 1) instanceof EqualToken)) {	// var = exp;
			final ParseResult<Exp> variable = parsePrimaryExp(position);
			final ParseResult<Exp> exp = parseExp(position + 2);
			assertTokenHereIs(exp.position, new SemicolToken());
			return new ParseResult<Stmt>(new VariableValueChange(variable.result, exp.result), exp.position + 1);
		} else if( (token instanceof IntToken) || (token instanceof BooleanToken) || (token
				 instanceof StringToken)  || (token instanceof VariableToken)) {		// vardec
			final Token token2 = getToken(position + 1);
			if (token2 instanceof VariableToken) {
				final String token2Name = ((VariableToken)token2).name;
				assertTokenHereIs(position + 1, new VariableToken(token2Name));
				final ParseResult<VariableDeclaration> declare = parseVardec(position);
				return new ParseResult<Stmt>(new VardecStmt(declare), declare.position);
			} else {
				//throw new ParseException("");
				final ParseResult<Exp> tryExp = parseExp(position);
				final ParseResult<Stmt> tryExpStmt = new ParseResult<Stmt>(new ExpStmt(tryExp.result), tryExp.position + 1                                                                                                                                                                                                                                       );
				return tryExpStmt;
			}	
		} else if (token instanceof WhileToken) {		// while (exp) stmt
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> guard = parseExp(position + 2);
			assertTokenHereIs(guard.position, new CloseparToken());
			final ParseResult<Stmt> whileStmt = parseStmt(guard.position + 1);
			return new ParseResult<Stmt>(new WhileStmt(guard.result, whileStmt.result), whileStmt.position);
		} else if (token instanceof BreakToken ) {		// break;
			final ParseResult<Stmt> breakResult;
			breakResult = parseBreakStmt(position);
			return breakResult;
		} else if(token instanceof IfToken) {			// if (exp) stmt else stmt
			assertTokenHereIs(position + 1, new OpenparToken());
			final ParseResult<Exp> ifGuard = parseExp(position + 2);
			assertTokenHereIs(ifGuard.position, new CloseparToken());
			final ParseResult<Stmt> trueBranch = parseStmt(ifGuard.position + 1);
			assertTokenHereIs(trueBranch.position, new ElseToken());
			final ParseResult<Stmt> falseBranch = parseStmt(trueBranch.position + 1);
			return new ParseResult<Stmt>(new IfStmt(ifGuard.result, trueBranch.result, falseBranch.result), falseBranch.position);
		} else if (token instanceof ReturnToken) {			// return exp;
			final ParseResult<Exp> retExp = parseExp(position + 1);
			assertTokenHereIs(retExp.position, new SemicolToken());
			return new ParseResult<Stmt>(new ReturnStmt(retExp.result), retExp.position + 1);
		} else if (token instanceof leftCurlyToken) {		// { stmt* }
			List<Stmt> stmts = new ArrayList();
			Token nextToken = getToken(position + 1);
			if (nextToken instanceof rightCurlyToken) {
				return new ParseResult<Stmt>(new BlockStmt(stmts), position + 2);
			} else {
				ParseResult<Stmt> stmt1 = parseStmt(position + 1);
				stmts.add(stmt1.result);
				int count = stmt1.position;
				Token theNextToken = getToken(count);
				while (!(theNextToken instanceof rightCurlyToken)) {
					ParseResult<Stmt> stmt2 = parseStmt(count);
					stmts.add(stmt2.result);
					count = stmt2.position;
					theNextToken = getToken(count);
				}
				assertTokenHereIs(count, new rightCurlyToken());
				return new ParseResult<Stmt>(new BlockStmt(stmts), count + 1);
			}
		} else if (token instanceof PrintlnToken) {			// println(exp*);
			assertTokenHereIs(position + 1, new OpenparToken());
			List<Exp> exps = new ArrayList();
			Token theNextToken = getToken(position + 2);
			if (theNextToken instanceof CloseparToken) {
				assertTokenHereIs(position + 3, new SemicolToken());
				return new ParseResult<Stmt>(new PrintlnStmt(exps), position + 4);
			} else {
				ParseResult<Exp> exp1 = parseExp(position + 2);
				exps.add(exp1.result);
				int iter1 = exp1.position;
				theNextToken = getToken(iter1);
				if (theNextToken instanceof CommaToken) {
					iter1 = iter1 + 1;
				}
				while (!(theNextToken instanceof CloseparToken)) {
					ParseResult<Exp> exp2 = parseExp(iter1);
					exps.add(exp2.result);
					Token testToken = getToken(exp2.position);
					if (testToken instanceof CommaToken) {
						iter1 = exp2.position + 1;
					} else {
						iter1 = exp2.position;
					}
					theNextToken = getToken(iter1);
				}
				assertTokenHereIs(iter1, new CloseparToken());
				assertTokenHereIs(iter1 + 1, new SemicolToken());
				return new ParseResult<Stmt>(new PrintlnStmt(exps), iter1 + 2);
			}
		} else if (token instanceof SuperToken) {			// super(var);
			 final ParseResult<Stmt> superStmt = parseSuperStmt(position);
			 return superStmt;
		} else if (token instanceof ThisToken) {			// this.var = var;
			 final ParseResult<Stmt> thisStmt = parseThisStmt(position);
			 return thisStmt;
		} else {											// exp;
			final ParseResult<Exp> theExp = parseExp(position);
			assertTokenHereIs(theExp.position, new SemicolToken());
			final ParseResult<Stmt> theExpStmt = new ParseResult<Stmt>(new ExpStmt(theExp.result), theExp.position + 1);
			return theExpStmt;
		}
	}
	
	// helper method to parse in a methodname
	public ParseResult<Methodname> parseMethodname(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken)token).name;
			return new ParseResult<Methodname>(new Methodname(name), position + 1);
		} else {
			throw new ParseException("Expected a VariableToken but recieved: " + token.toString());
		}
	}
	
	
	// methoddef ::= type methodname(param*) stmt
	public ParseResult<MethodDefinition> parseMethodDef(final int position) throws ParseException {
		final Token token = getToken(position);
		if ((token instanceof IntToken) || (token instanceof BooleanToken) || (token instanceof StringToken) || (token instanceof VariableToken)) {
			final ParseResult<Type> type = parseType(position);
			final Token token2 = getToken(position + 1);
			final String nameToken2 = ((VariableToken)token2).name;
			assertTokenHereIs(position + 1, new VariableToken(nameToken2));
			//final ParseResult<Exp> methodname = parsePrimaryExp(position + 1);
			final ParseResult<Methodname> methodname = parseMethodname(position + 1);
			assertTokenHereIs(position + 2, new OpenparToken());
			final List<Parameter> params = new ArrayList<Parameter>();
			Token nextToken = getToken(position + 3);
			if (nextToken instanceof CloseparToken) {
				final ParseResult<Stmt> stmt = parseStmt(position + 4);
				final ParseResult<MethodDefinition> result = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname.result, params, stmt.result), stmt.position);
				return result;
			}
			else {
				ParseResult<Parameter> param1 = parseParam(position + 3);
				params.add(param1.result);
				int iter = param1.position;
				nextToken = getToken(iter);
				if (nextToken instanceof CommaToken) {
					iter = iter + 1;
				}
				while (!(nextToken instanceof CloseparToken)) {
					ParseResult<Parameter> param2 = parseParam(iter);
					params.add(param2.result);
					Token tryToken = getToken(param2.position);
					if (tryToken instanceof CommaToken) {
						iter = param2.position + 1;
					} else {
						iter = param2.position;
					}
					nextToken = getToken(iter);
				}
				assertTokenHereIs(iter, new CloseparToken());
				final ParseResult<Stmt> stmt1 = parseStmt(iter + 1);
				final ParseResult<MethodDefinition> result1 = new ParseResult<MethodDefinition>(new MethodDefinition(type.result, methodname.result, params, stmt1.result), stmt1.position);
				return result1;
			}
		} else {
			throw new ParseException("Expected a type but received: " + token.toString());
		}
	}
	
	
	// helper method to parse in a classname
	public ParseResult<Classname> parseClassname(final int position) throws ParseException {
		final Token token = getToken(position);
		if (token instanceof VariableToken) {
			final String name = ((VariableToken)token).name;
			return new ParseResult<Classname>(new Classname(name), position + 1);
		} else {
			throw new ParseException("Expected a VariableToken but recieved: " + token.toString());
		}
	}
	
	// classdef ::= class classname extends classname {
	// vardec*
	// constructor(param*) stmt
	// methoddef*
	// }
	public ParseResult<ClassDefinition> parseClassdef(final int position) throws ParseException {
		final List<Parameter> params = new ArrayList<Parameter>();
		final Token token = getToken(position);
		if (token instanceof ClassToken) {
			final Token token1 = getToken(position + 1);
			final String name = ((VariableToken)token1).name;
			assertTokenHereIs(position + 1, new VariableToken(name));
			//final ParseResult<Exp> classname = parsePrimaryExp(position + 1);	//parse in the classname
			final ParseResult<Classname> classname = parseClassname(position + 1);
			final Token token2 = getToken(classname.position);
			if (token2 instanceof ExtendsToken) {		// case where we have an extends and secondary classname
				final Token token3 = getToken(classname.position + 1);
				final String name1 = ((VariableToken)token3).name;
				assertTokenHereIs(classname.position + 1, new VariableToken(name1));
				//final ParseResult<Exp> extendsClassname = parsePrimaryExp(classname.position + 1);	//parse in the secondary classname if it exists
				final ParseResult<Classname> extendsClassname = parseClassname(classname.position + 1);
				assertTokenHereIs(extendsClassname.position, new leftCurlyToken());
				final List<VariableDeclaration> vardecs = new ArrayList<VariableDeclaration>();
				int keepTrack = extendsClassname.position + 1;
				Token nextToken = getToken(keepTrack);
				Token nextNextToken = getToken(keepTrack + 1);
 				while (((nextToken instanceof IntToken) || (nextToken instanceof BooleanToken) || (nextToken instanceof StringToken) ||(nextToken instanceof VariableToken))
						&& (nextNextToken instanceof VariableToken)) {		//we know we have at least one vardec
					final ParseResult<VariableDeclaration> vardec = parseVardec(keepTrack);
					vardecs.add(vardec.result);
					keepTrack = vardec.position;
					nextToken = getToken(keepTrack);
					nextNextToken = getToken(keepTrack + 1);
				}
				//we either have no vardecs or have finished reading in all of the vardecs at this point
				if ((nextToken instanceof VariableToken) && (nextNextToken instanceof OpenparToken)) {	// we have a constructor (WHICH IS GOOD BCUZ IT'S REQUIRED)
					assertTokenHereIs(keepTrack, new VariableToken(name));	//checking to make sure the constructor name is the same as the class name
					keepTrack = keepTrack + 2;
					Token token4 = getToken(keepTrack);
					while (!(token4 instanceof CloseparToken)) {	// we have at least one parameter
						final ParseResult<Parameter> param = parseParam(keepTrack);
						params.add(param.result);
						Token ughToken0 = getToken(param.position);
						if (ughToken0 instanceof CommaToken) {
							keepTrack = param.position + 1;
						} else {
							keepTrack = param.position;
						}
						token4 = getToken(keepTrack);
					}			
				} else {
					throw new ParseException("Expected start of a constructor but received: " + nextToken.toString() + "," + nextNextToken.toString());
				}
				assertTokenHereIs(keepTrack, new CloseparToken());
				final ParseResult<Stmt> stmt = parseStmt(keepTrack + 1);	// parse in the stmt
				keepTrack = stmt.position;
				final List<MethodDefinition> methoddefs = new ArrayList<MethodDefinition>();
				Token token5 = getToken(keepTrack);
				while (!(token5 instanceof rightCurlyToken)) {
					final ParseResult<MethodDefinition> methoddef = parseMethodDef(keepTrack);
					methoddefs.add(methoddef.result);
					keepTrack = methoddef.position;
					token5 = getToken(keepTrack);
				}
				assertTokenHereIs(keepTrack, new rightCurlyToken());
				return new ParseResult<ClassDefinition>(new ClassDefinition(classname.result, extendsClassname.result, vardecs, params, stmt.result, methoddefs), keepTrack + 1);
			} else if (token2 instanceof leftCurlyToken) {	// case where we don't have an extends and secondary classname
				assertTokenHereIs(classname.position, new leftCurlyToken());
				final List<VariableDeclaration> vardecs1 = new ArrayList<VariableDeclaration>();
				int keepTrack1 = classname.position + 1;
				Token nextToken1 = getToken(keepTrack1);
				Token nextNextToken1 = getToken(keepTrack1 + 1);
 				while (((nextToken1 instanceof IntToken) || (nextToken1 instanceof BooleanToken) || (nextToken1 instanceof StringToken) ||(nextToken1 instanceof VariableToken))
						&& (nextNextToken1 instanceof VariableToken)) {		//we know we have at least one vardec
					final ParseResult<VariableDeclaration> vardec1 = parseVardec(keepTrack1);
					vardecs1.add(vardec1.result);
					keepTrack1 = vardec1.position;
					nextToken1 = getToken(keepTrack1);
					nextNextToken1 = getToken(keepTrack1 + 1);
				}
				//we either have no vardecs or have finished reading in all of the vardecs at this point
				if ((nextToken1 instanceof VariableToken) && (nextNextToken1 instanceof OpenparToken)) {	// we have a constructor (WHICH IS GOOD BCUZ IT'S REQUIRED)
					assertTokenHereIs(keepTrack1, new VariableToken(name));	//checking to make sure the constructor name is the same as the class name
					keepTrack1 = keepTrack1 + 2;
					Token token4v2 = getToken(keepTrack1);
					while (!(token4v2 instanceof CloseparToken)) {	// we have at least one parameter
						final ParseResult<Parameter> param1 = parseParam(keepTrack1);
						params.add(param1.result);
						Token ughToken = getToken(param1.position);
						if (ughToken instanceof CommaToken) {
							keepTrack1 = param1.position + 1;
						} else {
							keepTrack1 = param1.position;
						}
						token4v2 = getToken(keepTrack1);
					}			
				} else {
					throw new ParseException("Expected start of a constructor but received: " + nextToken1.toString() + "," + nextNextToken1.toString());
				}
				assertTokenHereIs(keepTrack1, new CloseparToken());
				final ParseResult<Stmt> stmt1 = parseStmt(keepTrack1 + 1);	// parse in the stmt
				keepTrack1 = stmt1.position;
				final List<MethodDefinition> methoddefs1 = new ArrayList<MethodDefinition>();
				Token token5v2 = getToken(keepTrack1);
				while (!(token5v2 instanceof rightCurlyToken)) {
					final ParseResult<MethodDefinition> methoddef1 = parseMethodDef(keepTrack1);
					methoddefs1.add(methoddef1.result);
					keepTrack1 = methoddef1.position;
					token5v2 = getToken(keepTrack1);
				}
				assertTokenHereIs(keepTrack1, new rightCurlyToken());
				return new ParseResult<ClassDefinition>(new ClassDefinition(classname.result, new Classname(""), vardecs1, params, stmt1.result, methoddefs1), keepTrack1 + 1);	//since no extends it's empty string
			} else {
				throw new ParseException("Expecting either extends or a left curly token but recieved: " + token2.toString());
			}
		} else {
			throw new ParseException("expected a class token but recieved: " + token.toString());
		}
	}

	
	//new code
	public ParseResult<Program> parseProgram(final int position) throws ParseException {	//mine will be different than Kyle's because we have two lists instead of one stmt
		final List<ClassDefinition> classes = new ArrayList<ClassDefinition>();
		final List<Stmt> stmts = new ArrayList<Stmt>();
		boolean shouldRunClasses = true;
		boolean shouldRunStmts = true;
		int newPosition = position;
		while (shouldRunClasses) {
			try {
				final ParseResult<ClassDefinition> theClass = parseClassdef(newPosition);
				classes.add(theClass.result);
				newPosition = theClass.position;
			} catch (final ParseException e) {
				shouldRunClasses = false;
			}
		}
		while (shouldRunStmts) {
			try {
				final ParseResult<Stmt> stmt = parseStmt(newPosition);
				stmts.add(stmt.result);
				newPosition = stmt.position;
			} catch (final ParseException e) {
				shouldRunStmts = false;
			}
		}
		return new ParseResult<Program>(new Program(classes, stmts), newPosition);
	}
	
	
	public Program parseProgram() throws ParseException {
		final ParseResult<Program> program = parseProgram(0);
		if (program.position == tokens.size()) {
			return program.result;
		} else {	//I don't think this can ever be reachable but Kyle had his like this so I'll leave it
			throw new ParseException("Remaining tokens at end");
		}
	 }
	

}