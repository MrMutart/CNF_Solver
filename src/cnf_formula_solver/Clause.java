package cnf_formula_solver;

import java.util.ArrayList;

/**
 * Represents a single Clause in a CNF Formula
 * 
 * @author MrMutart
 */
public class Clause {
    private ArrayList<Literal> literals;
    
    
    /**
     * Constructor - parses its own contents and initializes the List of real
     * Literals (including negation literals)
     * 
     * @param expression the String representation of a formula clause
     */
    public Clause(String expression) {
        literals = new ArrayList<>();
        
        // if bracketed, split and parse pieces
        if (expression.charAt(0) == '(') {
            String[] pieces = expression.substring(1, expression.length() - 1).split("v");
        
            for (int i = 0; i < pieces.length; i++) {
                pieces[i] = pieces[i].trim();
                literals.add(new Literal(pieces[i]));
            }
        }
        else {
            literals.add(new Literal(expression.trim()));
        }
    }
        

    /**
     * Verifies that given literal assignments satisfy the clause
     * 
     * @param assignment the Assignment object containing literal boolean values
     * @return true if satisfied, false otherwise
     */
    public boolean verify(Assignment assignment) {
        boolean satisfied = false;
        for (Literal l : literals) {
            if (l.isNegated()) {
                satisfied = !assignment.getValue(l.getName());
            }
            else {
                satisfied = assignment.getValue(l.getName());
            }
            
            // only need one literal to evaluate TRUE in order to satisfy clause
            if (satisfied) return true;
        }
        
        return false;
    }
    
    
    /**
     * Returns a List of all literals within clause
     * 
     * @return literals - an ArrayList of Literals in the clause
     */
    public ArrayList<Literal> literals() {
        return literals;
    }
    
}
