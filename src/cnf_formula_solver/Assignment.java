package cnf_formula_solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents the Assignment object that maintain literal boolean
 * value assignments given by user
 *
 * @author MrMutart
 */
public class Assignment {
    private HashMap<Literal, Boolean> assignments;
    
    
    /**
     * Constructor
     * 
     * @param formula the CNF Formula
     */
    public Assignment(CnfFormula formula) {
        assignments = new HashMap<>();
        
        for (Literal l : formula.getBaseLiterals()) {
            assignments.put(l, Boolean.FALSE);
        }
    }
    
    
    /**
     * Returns the boolean value associated with the Literal sharing the same
     * name as the var parameter
     * 
     * @param var the String representing a Literal name
     * @return the Literal boolean value if found, false otherwise
     */
    public boolean getValue(String var) {
        for (Literal key : assignments.keySet()) {
            if (key.getName().equals(var)) {
                return assignments.get(key); // return assigned boolean value
            }
        }
        return false;
    }
    
    
    /**
     * Updates the key:value pair var:val in the HashMap if exists, where var
     * is the String name of a Literal key
     * 
     * @param var the String representing a Literal name
     * @param val a boolean value to become Literal var new value
     */
    public void setValue(String var, boolean val) {
        // must loop through all keys to check if names match
        // cannot look up Literal string names in HashMap
        for (Literal key : assignments.keySet()) {
            if (key.getName().equals(var)) {
                assignments.put(key, val);
            }
        }
    }
    
    
    /**
     * Returns a String array of Literal names as required by the View
     * 
     * @return a String[] of all Literal names
     */
    public String[] literals() {
        String[] literals = new String[assignments.keySet().size()];
        int i = 0;
        
        for (Literal key : assignments.keySet()) {
            literals[i++] = key.getName();
        }
        
        Arrays.sort(literals);

        return literals;
    }
    
    
    /**
     * Prints the assignment in formatted String, for use when an assignment
     * satisfies a particular CNF Formula and needs to be displayed to user
     * 
     * @return output - the formatted String
     */
    @Override
    public String toString() {
        String output = "";
        
        // loop through all entries of Assignment and concat to output String
        for (Map.Entry<Literal, Boolean> entry : assignments.entrySet()) {
            output += "(" + entry.getKey().getName() + " = " + entry.getValue() + ")  ";
        }
        
        return output;
    }
    
}
