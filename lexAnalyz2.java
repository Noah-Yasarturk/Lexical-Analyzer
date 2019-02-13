import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;

public class lexAnalyz2 {

	public static void main(String[] args) {
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
		try {
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
	      catch (Exception ex){
	         ex.printStackTrace();
	      } finally {
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
	
	private static void breakLnDown(String line){
		/* This method takes a given Java file line and separates out the lexemes;
		 * lexemes are then printed in a table-like format under "LEXEMES:"
		 * along with their classification under "TOKENS:"
		 * upon analysis using additional methods.
		 */
		//Start table for this Java line
		System.out.println("LEXEMES:                      TOKENs:");
		
		String term = new String();
		String lx, tk = "tba";
		StringTokenizer st = new StringTokenizer(line);
		//iterate through line
		while(st.hasMoreTokens() == true){
			//analyze
			term = st.nextToken();
			//separate out term into its constituent tokens
			ArrayList<String> lxs = new ArrayList<String>();
			lxs = termParse(term);
			
			for (int i =0; i<lxs.size(); i++){
				lx = lxs.get(i);
				//Check if lexeme is an operator; if so, assign tk to operator; Operator classifier tba
				if (opCheck(lx)[0] != -1){
					tk = "Operator";
				}
				//Check if lexeme is a separator; if so, assign tk to separator; Separator classifier tba
				if (sepCheck(lx) != -1){
					tk = "Separator";
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
		/*This takes as input the result of StringTokenizer and 
		 * returns the fully separated lexemes.
		 */
		ArrayList<String> result = new ArrayList<String>();
		/* Check if term contains a query:
		 *  1.) an operator
		 *  2.) a separator
		 *  3.) a literal
		 *  4.) a keyword
		 *  5.) an identifier
		 *  
		 *  Find whichever query is earliest; split there.
		 *  Add preceding portion to result, then the query itself.
		 *  Run remaining portion of term back through to search for 
		 *  rest of queries.
		 */
		boolean termDone = false;
		//To store indices of found queries
		int opInd = -1;
		int sepInd = -1;
		int litInd = -1;
		int keyInd = -1;
		int idInd = -1;
		//Store lowest 
		int earliest = 1000;
		//Store case for reprocessing
		String rest = term;
		
		
		while(termDone == false){
			opInd = opCheck(rest)[0];
			sepInd = sepCheck(rest);
			//+ litInd set
			//+ keyInd set
			//+ idInd set
			int qF = qFirst(opInd, sepInd, litInd, keyInd, idInd);
			
			//Find lowest index of found query, excluding -1
			if(qF == -1){
				//No queries found; add to result 
				result.add(term);
				termDone = true;
			}
			
			if(qF == 1){
				//Operator is first query found; add preceding portion and then the operator to result
				String op = rest.substring(opCheck(rest)[0], (opCheck(rest)[0] + opCheck(rest)[1]));
				try{
					String[] opSplit = rest.split(op,2);
					if (!(opSplit[0].equals(""))){
						//Operator not first
						result.add(opSplit[0]);
						result.add(op);
						if(!(opSplit[1].equals(""))){
							//Check if later portion exists; if it doesn't, stop analyzing term
							rest = opSplit[1];
						} else{
							termDone = true;
						}
						
					}else{
						//Operator is first element
						if (rest.length() > op.length()){
							result.add(op);
							rest = opSplit[1];
						} else{
							result.add(op);
							termDone = true;
						}
					}
				} catch (PatternSyntaxException p){
					//We're splitting by a metacharacter
					String[] opSplit = rest.split(("\\"+op),2);
					if (!(opSplit[0].equals(""))){
						//Operator not first
						result.add(opSplit[0]);
						result.add(op);
						if(!(opSplit[1].equals(""))){
							//Check if later portion exists; if it doesn't, stop analyzing term
							rest = opSplit[1];
						} else{
							termDone = true;
						}
						
					}else{
						//Operator is first element
						if (rest.length() > op.length()){
							result.add(op);
							rest = opSplit[1];
						} else{
							result.add(op);
							termDone = true;
						}
					}
				}
				
			}
			
			if(qF == 2){
				//Separator is first query found; add preceding portion and then the separator to result
				String sep = Character.toString(rest.charAt(sepCheck(rest)));
				String[] sepSplit = rest.split(("\\"+sep), 2);
				if (!(sepSplit[0].equals(""))){
					//Separator not first
					result.add(sepSplit[0]);
					result.add(sep);
					if (!(sepSplit[1].equals(""))){
						//Check if later portion exists; if it doesn't, stop analyzing term
						rest = sepSplit[1];
					} else{
						termDone = true;
					}
				}else{
					//Separator is first element
					if(rest.length() > 1){
						result.add(sep);
						rest = sepSplit[1];
					}else{
						result.add(sep);
						termDone = true;
						}
					}
			}
			
			//+ handle literal check
			
			//+ handle keyword check
			
			//+ handle identifier check
			
			
		}
		
		
		return(result);
	}
	
	private static int qFirst(int opInd, int sepInd, int litEnd, int keyInd, int idInd){
		/* Returns:
		 * -1 if all values are -1
		 * 0 if error
		 * 1 if least is opInd
		 * 2 if least is sepInd
		 * 3 if least is litEnd
		 * 4 if least is keyInd
		 * 5 if least is idInd
		 */
		
		int result = 0;
		int noQcnt = 0;
		int least = 1000;
		int[] qs = {opInd, sepInd, litEnd, keyInd, idInd};
		for(int i = 0; i<qs.length; i++){
			if((qs[i] != -1) && (qs[i] < least)){
				least = qs[i];
				result = (i+1);
			}
			if(qs[i] == -1){
				noQcnt++;
			}
		}
		if(noQcnt == 5){
			result = -1;
		}
		
		return result;
	}
	
	private static int sepCheck(String term){
		/* Returns index of first separator found.
		 * Returns -1 if no separator found.
		 */
		int ind = -1;
		char[] seps = {'(', ')', '{', '}',
				'[', ']', ';', ',', '.'
				};
		for (int s=0;s<seps.length;s++){
			ind = term.indexOf(seps[s]);
			if (ind != -1){
				break;
			}
		}
		return(ind);
	}
	
	private static int[] opCheck(String term){
		/* This method checks whether a given inputed String contains a Java operator.
		 * The result is :
		 * a.) -1 in the first index if the String contains no operator, or 
		 * b.) the index where the first separator begins in term if the String does.
		 * The second index indicates the length of the matched String.
		 */
		int[] ind = {-1, -1};
		//need to check longer operators first so that first match is longest operator possible
		//Some of these are metacharacters that need to be escaped.
		String[] operators = {
				"==", "<=", ">=", "!=", "&&", "||", "++", "--",
				"<<=", ">>=",  ">>>",">>>=",
				"<<", ">>",
				"+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", 
				"=", ">", "<", "!", "~", "?", ":",
				"+", "-", "*", "/", "&", "|", "^", "%"
				};
		for (int i = 0; i<operators.length; i++){
			ind[0] = term.indexOf(operators[i]);
			ind[1] = operators[i].length();
			if (ind[0] != -1){
				break;
			}
		}
		return(ind);
	}

}
