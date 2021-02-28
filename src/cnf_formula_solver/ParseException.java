package cnf_formula_solver;

/**
 * Exception for handling parsing errors
 * 
 * @author MrMutart
 */
@SuppressWarnings("serial")
public class ParseException extends Exception {
    
    public ParseException(String message) {
        super(message);
    }
    
    public ParseException(String exp, String actual) {
        super("\nERROR: Expected '" + exp + "', Got '" + actual + "'");
    }
}
