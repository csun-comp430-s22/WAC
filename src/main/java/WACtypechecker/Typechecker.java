package WACtypechecker;

import WACparser.*;

public class Typechecker {
	
	public static Type typeOf(final Exp exp) throws TypeErrorException {
		if (exp instanceof TrueExp) {
			return new BooleanType();
		}
		else {
			throw new TypeErrorException("");
		}
	}
}

        
