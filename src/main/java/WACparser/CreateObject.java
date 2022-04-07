package WACparser;

import java.util.List;

public class CreateObject implements Exp {
	public final ClassnameExp classname;
	public final List<Exp> exps;
	
	public CreateObject(final ClassnameExp classname, final List<Exp> exps) {
		this.classname = classname;
		this.exps = exps;
	}
}