package WACparser;

public class Parameter {
	
	public final Type parameterType;
	
	public final Exp variable;
	
	public Parameter (final Type parameterType,final Exp variable ) 
	    {
		  this.parameterType = parameterType;
		  this.variable = variable;
	    }
		
	public boolean equals(final Object other) {
		if( other instanceof Parameter) 
		{
		    final Parameter otherParameter = (Parameter)other;
		    return(parameterType.equals(otherParameter.parameterType) && variable.equals(otherParameter.variable));
		}
		else 
		{
			return false;
		}
		
	}
	public int hashCode() {
        return (parameterType.hashCode() + variable.hashCode());
    }
	public String toString() {
        return ( parameterType.toString()  +" "+ variable.toString() );
    }
}
