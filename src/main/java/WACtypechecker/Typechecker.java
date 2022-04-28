package WACtypechecker;

import WAClexer.*;
import WACparser.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Typechecker {

    // Helpers
//    public Type typeofVariable(final VariableExp exp,
//                               final Map<Variable, Type> typeEnvironment) throws TypeErrorException {
//        final Type mapType = typeEnvironment.get(exp.variable);
//        if (mapType == null) {
//            throw new TypeErrorException("Used variable not in scope: " + exp.variable.name);
//        } else {
//            return mapType;
//        }
//    }   // typeofVariable

//    public Type typeofThis(final ClassName classWeAreIn) throws TypeErrorException {
//        if (classWeAreIn == null) {
//            throw new TypeErrorException("this used in the entry point");
//        } else {
//            return new ClassNameType(classWeAreIn);
//        }
//    }   // typeofThis

    public Type typeofOp(final OpExp exp,
                         final Map<Variable, Type> typeEnvironment,
                         final Classname classWeAreIn) throws TypeErrorException {
        final Type leftType = typeof(exp.left, typeEnvironment, classWeAreIn);
        final Type rightType = typeof(exp.right, typeEnvironment, classWeAreIn);
        // (leftType, exp.op, rightType) match {
        //   case (IntType, PlusOp, IntType) => IntType
        //   case (IntType, LessThanOp | EqualsOp, IntType) => Booltype
        //   case _ => throw new TypeErrorException("Operator mismatch")
        // }
        // Comparison Exp
        if (exp.op instanceof PlusOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new TypeErrorException("Operand type mismatch for +");
            }
        } else if (exp.op instanceof LessThanOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BooleanType();
            } else {
                throw new TypeErrorException("Operand type mismatch for <");
            }
        } else if (exp.op instanceof GreaterThanOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BooleanType();
            } else {
                throw new TypeErrorException("Operand type mismatch for >");
            }
        } else if (exp.op instanceof EqualEqualsOp) {
            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BooleanType();
            } else if(leftType instanceof StringType && rightType instanceof StringType) {
                return new BooleanType();
            } else {
                throw new TypeErrorException("Operand type mismatch for ==");
            }
        } else {
            throw new TypeErrorException("Unsupported operation: " + exp.op);
        }
    }   // typeofOp

    public Type typeof(final Exp exp,
                       final Map<Variable, Type> typeEnvironment,
                       final Classname classWeAreIn) throws TypeErrorException {
        if (exp instanceof IntegerExp) {
            return new IntType();
        }
//        else if (exp instanceof VariableExp) {
//            return typeofVariable((VariableExp)exp, typeEnvironment);
//        } else if (exp instanceof BoolLiteralExp) {
//            return new BoolType();
//        } else if (exp instanceof ThisExp) {
//            return typeofThis(classWeAreIn);
//        }
        else if (exp instanceof OpExp) {
            return typeofOp((OpExp)exp, typeEnvironment, classWeAreIn);
        }
//        Commented out to work on Comparison Exp
//        else if (exp instanceof MethodCallExp) {
//            return typeofMethodCall((MethodCallExp)exp, typeEnvironment, classWeAreIn);
//        } else if (exp instanceof NewExp) {
//            return typeofNew((NewExp)exp, typeEnvironment, classWeAreIn);
//        }
        else {
            throw new TypeErrorException("Unrecognized expression: " + exp);
        }
    }   //typeof

}   // Typechecker