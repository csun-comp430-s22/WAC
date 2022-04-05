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
			throw new ParseException("expected statement; received: " + token);
		}
	}
	 
}