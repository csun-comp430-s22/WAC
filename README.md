# WAC
<pre>
A compiler from a high-level Java like language to C, written in Java.
Our language supports class based inheritance, subtyping, and method overloading.

Full documentation available here:
https://docs.google.com/document/d/1D54NprSTMD1AnkS6pM9GCNO9lue8hvOgruq8MsPTTIs/edit?usp=sharing
</pre>

# Abstract Syntax:
<pre>
var is a variable
int is an integer
str is a string
methodname is the name of a method
classname is the name of the class
contructor is the name of the object
'*' represents the traditional Kleene star meaning

type ::= Int | Boolean | String | classname	// basic types
primary_exp ::= var | str | int | true | false			//base case values
multiplicative_op ::= * | /		// highest precedence operators
multiplicative_exp ::= primary_exp (multiplicative_op primary_exp)*	// highest precedence expressions
additive_op ::= + | -					// middle precedence operators
additive_exp ::= multiplicative_exp (additive_op multiplicative_exp)*	 // middle precedence expressions
comparison_op ::= < | > | == | !=
comparison_exp ::= additive_exp | additive_exp comparison_op additive_exp 	//lowest precedence expressions
exp ::= comparison_exp | var.methodname(primary_exp*) | new classname(exp*) 	    // recursive expressions | non recursive expressions
	                                  					    // both lists are comma seperated
vardec ::= type var = exp;		// variable declaration
param ::= type var			// parameters
stmt ::= vardec | 	// variable declaration
	 var = exp; |		// changing the value of a previously instantiated variable
         while (exp)  stmt  | 		// while loops
         break; | 			// break
         if (exp) stmt else stmt | 	// if/else
         return exp; | 		// return an expression
         { stmt* } 			// block
         println(exp*); |	// printing expression
				        // exps are comma seperated
         super(var); | 		// Invoke parent constructor
         this.var = var; | 		// refers to the current instance
	 exp;				// gives entry to exp and includes ;
	 
methoddef ::= type methodname(param*) stmt 	// method declaration
                                                //params are comma seperated
classdef ::= class classname extends classname {
                    vardec*
                    constructor(param*) stmt	// params are comma seperated
                    methoddef*
         }					// creates new class instance (extends classname is optional)
program ::== classdef* stmt*
</pre>
