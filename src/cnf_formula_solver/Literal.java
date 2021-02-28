package cnf_formula_solver;

import java.util.Objects;

/**
 * Represents a single Literal in a CNF Formula
 * 
 * @author MrMutart
 */
public class Literal {
    private String name;
    private boolean isNegated;

    
    /**
     * Constructor for only one parameter
     * 
     * @param name the String representation of the Literal
     */
    public Literal(String name) {
        if (name.charAt(0) == 'n') {
            this.name = name.substring(1, name.length());
            this.isNegated = true;
        }
        else {
            this.name = name;
            this.isNegated = false;
        }
    }
    
    
    /**
     * Overloaded constructor
     * 
     * @param name the String representation of the Literal
     * @param isNegated a boolean value representing whether negated or not
     */
    public Literal(String name, boolean isNegated) {
        this.name = name;
        this.isNegated = isNegated;
    }

    
    /**
     * Getter for Literal name
     * 
     * @return name - the Literal String name
     */
    public String getName() {
        return name;
    }

    
    /**
     * Getter for Literal negation value
     * 
     * @return isNegated - boolean value for if Literal is negated
     */
    public boolean isNegated() {
        return isNegated;
    }

    
    /**
     * HashCode method
     * 
     * @return hash - computed hash value for this specific Literal
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + (this.isNegated ? 1 : 0);
        return hash;
    }

    
    /**
     * Compares Literal objects to determine if equal based on the String name
     * 
     * @param other the other Literal object which is being compared
     * @return true if equal, false otherwise
     */
    public boolean equals(Literal other) {
        if (other == null) return false;
        
        return (this.name.equals(other.name));
    }

}
