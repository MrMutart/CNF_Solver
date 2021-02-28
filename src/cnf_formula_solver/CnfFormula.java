package cnf_formula_solver;

import java.util.ArrayList;

/**
 * Represents the overall CNF Formula including its Clauses and all regular
 * Literals
 * 
 * @author MrMutart
 */
public class CnfFormula {
    private ArrayList<Literal> baseLiterals;  // base literals without negations
    private ArrayList<Clause> clauses;
    
    
    /**
     * Constructor
     * 
     * @param clauses an ArrayList of Clause objects
     * @param baseLiterals an ArrayList of Literal objects
     */
    public CnfFormula(ArrayList<Clause> clauses, ArrayList<Literal> baseLiterals) {
        this.clauses = clauses;
        this.baseLiterals = baseLiterals;
    }
    
    
    /**
     * Returns the base (non-negated) literals
     * 
     * @return baseLiterals - an ArrayList of Literals
     */
    public ArrayList<Literal> getBaseLiterals() {
        return baseLiterals;
    }

    
    /**
     * Returns the list of Clauses for the formula
     * 
     * @return clauses - an ArrayList of Clauses
     */
    public ArrayList<Clause> getClauses() {
        return clauses;
    }
    
    
    /**
     * Returns the actual literals found in each clause (+ negated literals)
     * 
     * @return an Array of Literals from all Clauses within formula
     */
    public Literal[] literals() {
        ArrayList<Literal> literalList = new ArrayList<>();
        
        // loop through all clauses and get literals within each
        // add literals from each clause to Literal[]
        for (int i = 0; i < clauses.size(); i++) {
            literalList.addAll(clauses.get(i).literals());
        }
        
        return literalList.toArray(new Literal[literalList.size()]);
    }
    
    
    /**
     * Verifies that the assignment satisfies the formula
     * 
     * @param assignment the Assignment object containing literal boolean values
     * @return true if all clauses satisfied by assignment, false otherwise
     */
    public boolean verify(Assignment assignment) {
        // loop through clauses verifying each clause
        // return false if any clause is NOT satisfied, otherwise true
        for (Clause c : clauses) {
            if (!c.verify(assignment)) return false;
        }
        return true;
    }
}
