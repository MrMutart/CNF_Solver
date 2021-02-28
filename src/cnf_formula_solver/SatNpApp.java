package cnf_formula_solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Loads a CNF Formula from a user selected file, then tests user
 * assigned literal boolean values to see if they satisfy the formula
 * 
 * @author MrMutart
 */
public class SatNpApp {

    
    /**
     * Prompts the user for a file, loads CNF Formula from file, and checks
     * user boolean value assignments to see if they satisfy the formula
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AssignmentView view = new AssignmentView();
        File readFile = chooseFileLocation();
        
        try {
            CnfFormula formula = loadCnfFormula(readFile);
            
            do {
                Assignment assignment = new Assignment(formula);
                view.setModel(assignment);
                view.setVisible(true);
                
                if (verify(assignment, formula)) {
                    JOptionPane.showMessageDialog(null, "Satisfied", "Result",
                      JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Not Satisfied", 
                          "Result", JOptionPane.PLAIN_MESSAGE);
                }
            } while (trueFalsePrompt("Test again with different assignments?"));
            
            Assignment solution = isSatisfiable(formula);
            
            if (solution != null) {
                JOptionPane.showMessageDialog(null, "CNF formula IS "
                      + "satisfiable!\n\nSolution:\n" + solution.toString(), 
                      "Satisfiability", JOptionPane.PLAIN_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "CNF Formula is NOT "
                      + "satisfiable\n\nThere is no working assignment to "
                      + "satisfy this formula", "Satisfiability",
                      JOptionPane.PLAIN_MESSAGE);
            }
            // user selected no to prompt, so exit
            System.exit(0);
            
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(0); // exit after parse error
        }
    }
    
    
    
    /**
     * Verifies whether or not the user decided assignment satisfies the CNF
     * formula
     * 
     * @param assignment the user defined literal assignments
     * @param formula the CNF formula read from file
     * @return true if assignment satisfies the formula, false otherwise
     */
    public static boolean verify(Assignment assignment, CnfFormula formula) {
        return formula.verify(assignment);
    }
    
    
    
    /**
     * Determines if the CNF formula provided is satisfiable and if so
     * returns the very first satisfying assignment it discovers
     * 
     * @param formula
     * @return the satisfying assignment if found, null otherwise
     */
    public static Assignment isSatisfiable(CnfFormula formula) {
        Assignment assignment = new Assignment(formula);
        ArrayList<Literal> literals = formula.getBaseLiterals();
        
        // loop through permutations of assignments checking satisfiability
        for (int i = 0; i < literals.size() - 1; i++ ) {
            assignment.setValue(literals.get(i).getName(), true);
            if (formula.verify(assignment)) return assignment;
            
            // loop through with outer loop set true
            for (int j = 1; j < literals.size(); j++) {
                if (j == i) continue;  // do not overwrite i's assignment
                assignment.setValue(literals.get(j).getName(), true);
                if (formula.verify(assignment)) return assignment;
                assignment.setValue(literals.get(j).getName(), false);
                if (formula.verify(assignment)) return assignment; // redundant
            }
            assignment.setValue(literals.get(i).getName(), false); // set false
            // add in an verify statement here
            
            // loop through with outer loop set false
            for (int k = 1; k < literals.size(); k++) {
                if (k == i) continue;  // do not overwrite i's assignment
                assignment.setValue(literals.get(k).getName(), true);
                if (formula.verify(assignment)) return assignment;
                assignment.setValue(literals.get(k).getName(), false);
                if (formula.verify(assignment)) return assignment; // redundant
            }
        }
        return null;
    }
    
    
    
    /**
     * Prompts user for a file containing a CNF Formula
     * 
     * @return readFile - the file containing the CNF Formula
     */
    public static File chooseFileLocation() {
        File readFile = null;
        JFileChooser fileChooser = new JFileChooser();        
        FileNameExtensionFilter filter = 
              new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter); //only show text files in chooser
        boolean validSelection = false;
        
        // prompt user
        JOptionPane.showMessageDialog(null, "Please select a file containing "
              + "the CNF Formula\n\n", "Select a CNF Formula", 
              JOptionPane.INFORMATION_MESSAGE);
        
        // loop until file is selected or user indicates desire to exit program
        while (!validSelection) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                validSelection = true;
                readFile = fileChooser.getSelectedFile();
            }
            else {
                // user did not select file, prompt to try again or exit
                int choice = JOptionPane.showConfirmDialog(null, "No File "
                      + "Selected - Try Again?\n\n", "ERROR - Try Again?",
                      JOptionPane.YES_NO_OPTION);
                
                // only check if No indicating desire to exit
                if (choice == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        }
        
        return readFile;
    }
    
    
    
    /**
     * Parses the file and returns a new CnfFormula object based on the
     * file specified CNF Formula
     * 
     * @param readFile the file selected by the user containing CNF formula
     * @return the CnFFormula object created from readFile
     * @throws cnf_formula_solver.ParseException 
     */
    public static CnfFormula loadCnfFormula(File readFile) throws ParseException {
        String formula = null;
        
        // open file and read in line containing CNF Formula
        try (Scanner reader = new Scanner(readFile)) {
            formula = reader.nextLine();            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex, "ERROR",
                      JOptionPane.ERROR_MESSAGE);
        }
        
        // make sure formula received a String value from scanner
        if (formula == null || formula.length() == 0) return null;
        
        ArrayList<Clause> clauses = new ArrayList<>();
        String[] pieces = formula.split("\\^");

        // add clauses to list
        for (String clause : pieces) {
            clauses.add(new Clause(clause.trim()));
        }
        
        // strip down formula to only bare (non-negated) literals
        String strippedFormula = formula.replace("(", "");  //strip lparenth
        strippedFormula = strippedFormula.replace(")", ""); //strip rparenth
        strippedFormula = strippedFormula.replace("n", ""); //strip negations
        strippedFormula = strippedFormula.replace("v", ""); //strip "OR" 'v'
        strippedFormula = strippedFormula.replace("^", ""); //strip "AND" carat
        String[] ltrls = strippedFormula.split("\\s+");     //split on whitespace
        ArrayList<Literal> literalList = new ArrayList<>();
        
        // add all regular (non-negated) literals to list
        for (String s : ltrls) {
            literalList.add(new Literal(s));
        }
        

        // mark duplicates for removal by saving to list
        ArrayList<Literal> indexes = new ArrayList<>();
        
        for (int i = 0; i < literalList.size(); i++) {
            for (int j = literalList.size() - 1; j > i; j--) {
                if (literalList.get(i).equals(literalList.get(j))) {
                    indexes.add(literalList.get(j));
                }
            }
        }
        
        // remove all duplicates
        for (Literal l : indexes) {
            literalList.remove(l);
        }

        return new CnfFormula(clauses, literalList);
    }
    
    
    
    /**
     * Prompts user if they would like to continue
     * 
     * @param message the message for the prompt
     * @return true if user selects YES button, false otherwise
     */
    public static boolean trueFalsePrompt(String message) {
        int response = JOptionPane.showConfirmDialog(null, message, 
              "Try Again?", JOptionPane.YES_NO_OPTION);
        
        return (response == JOptionPane.YES_OPTION);
    }
    
}
