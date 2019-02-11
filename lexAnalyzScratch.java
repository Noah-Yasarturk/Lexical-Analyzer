import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class lexAnalyzScratch {
/*
 * lexAnalyz.java will read an inputed Java file (inputed on line !$!TO ADD!$!) line by line,
 * remove the comments, tokenize the input data, and send the resulting token sequence to be
 * printed and saved to a text file.
 * 
 *  We tested it on a simple program that prints the Fibonacci sequence up to 1,000.
 * 
 */
	public static void main(String[] args) {
		// Main method
		System.out.println("This program conducts lexical analysis on our sample program,");
		System.out.println("sample4analysis.java, which prints the Fibonacci sequence up to 1000.");
		System.out.println();
		
		String filePath = "C:\\Users\\nyasa\\workspace\\Lexical Analyzer\\src\\sample4analysis.java";
		anlyzFromFile(filePath);
	}
	
	private static void anlyzFromFile(String file){
		/* We learned how to read a Java file sequentially with LineNumberReader from here:
		 * https://howtodoinjava.com/java/io/java-io-linenumberreader-example-to-read-file-line-by-line/
		 * This method commences analysis of a Java file line by line. It skips whitespace and comments
		 * and pushes each line off for analysis using the breakLnDown() method.
		 */
		
		LineNumberReader lnRdr = null;
		try
	      {
	         //Construct the LineNumberReader object
	         lnRdr = new LineNumberReader(new FileReader(file));
	          
	         //Setting initial line number
	         lnRdr.setLineNumber(0);
	          
	         //Read all lines now; Every read increase the line number by 1
	         String line = null;
	         while ((line = lnRdr.readLine()) != null){	        	 
	        	 //Check if line is a comment; if it is, skip it
	        	 if (commentCheck(line) == false){
		        	 System.out.println("Line "+lnRdr.getLineNumber()+": "+line.trim());		        	 
	        		 breakLnDown(line);
		        	 System.out.println();
	        	 }
	         }
	      }
	      catch (Exception ex)
	      {
	         ex.printStackTrace();
	      } finally
	      {
	         //Close the LineNumberReader
	         try {
	            if (lnRdr != null){
	               lnRdr.close();
	            }
	         } catch (IOException ex){
	            ex.printStackTrace();
	         }
	      }
	}
	
	
	private static boolean commentCheck(String javaLine){
		/* This method evaluates whether a given Java line is a comment or empty space.
		 * A line is a comment if it begins with /*, //, * , or */
		
		boolean chk = false;
		String text = javaLine.trim();
		//check if it isn't empty
		if ((text.isEmpty() == true) || (text == null) || (text.equals("\n") == true)){
			chk = true;
		}else{
			char[] ca = text.toCharArray();
			if ((ca[0] == '/') && ((ca[1] == '*')||(ca[1] == '/'))){
				chk = true;
			}
			if ((ca[0] == '*') && ((ca[1] == ' ')||(ca[1] == '/'))){
				chk = true;
			}
		}
		//Check if comment pattern is matched
		
		return chk;
	}
	
	private static void breakLnDown(String javaLine){
		/* This method takes a given Java file line and separates out the lexemes;
		 * lexemes are then printed in a table-like format under "LEXEMES:"
		 * along with their classification under "TOKENS:"
		 * upon analysis using additional methods.
		 */
		
		//Start table for this Java line
		System.out.println("LEXEMES:                      TOKENs:"); 

		String term = new String();
		String lx, tk = "tba";
		StringTokenizer st = new StringTokenizer(javaLine);
		//iterate through line
		while(st.hasMoreTokens() == true){
			//analyze
			term = st.nextToken();
			//separate out term into its constituent tokens
			ArrayList<String> lxs = new ArrayList<String>();
			lxs = termParse(term);
			
			for (int i =0; i<lxs.size(); i++){
				if (lxs.size()==0){
					lx = "space";
					tk = "space";
				}
				lx = lxs.get(i);
				//Check if lexeme is an operator; if so, assign tk to operator; Operator classifier tba
				if (opCheck(lx)[0] != -1){
					tk = "Operator";
				}
				//print result with help from:https://stackoverflow.com/questions/5249566/adding-whitespace-in-java
				String lxFormat = String.format("%-30s", lx); 
				System.out.println(lxFormat+tk);
				//Reset token value
				tk = "tba";
			}
		}
	}
	
	private static ArrayList<String> termParse(String term){
		/* This method takes a "token" from our StringTokenizer and identifies any tokens it has missed.
		 * Total tokens are then returned as a String array.
		 */
		term = term.trim();
		
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> opTemp = new ArrayList<String>(); //store separation following Operator find
		ArrayList<String> sepTemp = new ArrayList<String>();//store separation following Separator find
		ArrayList<String> litTemp = new ArrayList<String>();//store separation following Literal find
		ArrayList<String> keyTemp = new ArrayList<String>();//store separation following Keyword find
		ArrayList<String> idTemp = new ArrayList<String>();//store separation following Identifier find
		
		boolean fullDone = false; //finished upon total analysis
		boolean opDone = false;//finished upon finding all operators
		String endPart = "";
		
		while (fullDone == false){
			//Determine if term is 1 character
			
			if(term.length() == 1){
				result.add(term);
				opDone = true;
				fullDone = true;
			}
			
			//Parse operators
			while (opDone == false){
				int begOps = opCheck(term)[0]; //stores index of first char of operator
				int endOps = (opCheck(term)[0] + opCheck(term)[1] - 1); //stores index of last char of operator
				//Check if no operators exist
				if(begOps == -1){
					opTemp.add(term);
					opDone = true;
				}
				//Check if operator is found at start of term
				else if(begOps == 0){
					//if term is only an operator
					if(opCheck(term)[1] == term.length()){
						opTemp.add(term);
						opDone = true;
					}else{
					
					//Add match operator to temp
					String[] opSplit1 = term.split(Character.toString(term.charAt(endOps+1)));
					opTemp.add(opSplit1[0]);
					//Reprocess rest
					String[] opSplit2 = term.split(Character.toString(term.charAt(endOps)));
					term = (opSplit2[1]);
					}
				}
				//Check if operator is found later in term
				else if((begOps !=0) && (begOps != -1)){
					//Add beginning portion to temp
					String[] opSplit1 = term.split(Character.toString(term.charAt(begOps)));
					opTemp.add(opSplit1[0]);
					//Add operator to temp
					String op = term.substring(begOps, (begOps + endOps + 1));
					opTemp.add(op);
					//Reprocess remaining portion
					term = term.split(op)[1];
				}
			}
			
			
			//Check if term is fully processed
			if (opDone == true){ //also add additional checks
				fullDone = true;
			}
		}
		
		
		result.addAll(opTemp);		
		return(result);
	}
	
	private static int[] opCheck(String lex){
		/* This method checks whether a given inputed String contains a Java operator.
		 * The result is -1 in the first index if the String contains no operator or the index where 
		 * the first operator begins if the String does.
		 * The second index indicates the length of the matched String.
		 */
		int[] ind = {-1, -1};
		//need to check longer operators first so that first match is longest operator possible
		String[] operators = {"==", "<=", ">=", "!=", "&&", "||", "++", "--",
				"<<=", ">>=",  ">>>",">>>=",
				"<<", ">>",
				"+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", 
				"=", ">", "<", "!", "~", "?", ":",
				"+", "-", "*", "/", "&", "|", "^", "%"
				};
		for (int i = 0; i<operators.length; i++){
			ind[0] = lex.indexOf(operators[i]);
			ind[1] = operators[i].length();
			if (ind[0] != -1){
				break;
			}
		}
		return(ind);
	}


}
