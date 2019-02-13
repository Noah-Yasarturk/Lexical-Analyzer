import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexAnalyzer {
	public static void main(String[] args) throws FileNotFoundException {

		        File file = new File("test.java");
		        Scanner scanner = new Scanner(file);
			String x="";
		              System.out.println("Lexemes\t\t\tTokens");
		      while(scanner.hasNextLine())
			{
				
			       
				x+=scanner.nextLine();
				x+="\n";
				
			}
			char[] charr = x.toCharArray();
			lex(charr);
	}
	public static boolean isKeyWord(String a)
	{
		String keywords[]={"byte","class","do","extends","for","import","long","private","short","switch","throws","volatile","assert",
		"case","const","double","final","goto","instanceof","native","protected","static","synchronized","transient","while","boolean","catch","continue","else","finally","if","int","new","public","strictfp","this","try","break","char","default","enum","float","implements","interface","package","return","super","throw","void"};
		for(int i=0;i<keywords.length;i++)
			if(a.equals(keywords[i]))
				return true;
		return false;

	}
	public static boolean isInteger(String a)
	{
		try{

			int i=Integer.parseInt(a);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public static boolean isFloat(String a)
	{
		try{

			float i=Float.parseFloat(a);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public static void lex(char [] arr) {
		
		int j = 0;
		String temp = "";
		String [] s = new String [100];

	    for(int i = 0; i < arr.length; i++) {
		if(arr[i]=='"')
		{
			i++;
			while(arr[i]!='"')
			{
				temp+=arr[i];
				i++;
			}
			i++;
			System.out.println(temp+" \t\tstring_literal");
		}
		temp="";
		 while(Character.isLetter(arr[i]) || Character.isDigit(arr[i])||arr[i]=='_') {
	            temp += arr[i];
			i++;				
	        }
		int l=temp.length();
		if(l!=0)
			if(isKeyWord(temp))
				System.out.println(temp+" \t\t\tkeyword");
			else if(isInteger(temp))
				System.out.println(temp+" \t\t\tint_literal");
			else if(isFloat(temp))
				System.out.println(temp+" \t\t\tfloat_literal");

			else
				
				System.out.println(temp+" \t\t\tidentifier");
		temp="";
	    	if(arr[i] == '=') {
	                System.out.println("= \t\t\tequal_sign");
	            }
	    	else if(arr[i] == '*') {
                System.out.println("* \t\t\tmult_op");
            	}
	    	else if(arr[i] == '+'){
	                System.out.println("+\t\t\tplus_op");
	            }
	       else if(arr[i] == '-') {
	                System.out.println("- \t\t\tsub_op");
	            }
	       else if(arr[i] == '.') {
               System.out.println(". \t\t\tperiod");
           }
	       else if(arr[i] == '/') {
			if(arr[i+1]=='*')	//multiline comment
			{
				i+=2;
				while(!(arr[i]=='*'&&arr[i+1]=='/'))
					i++;
				i++;
			}

			else if(arr[i+1]=='/')	//single line comment
			{
				i=i+2;
				while(arr[i]!='\n')
					i++;
			}
			else	
		                System.out.println("/ \t\t\tdiv_op");
	            }
	       else if(arr[i] == '('){
	                System.out.println("( \t\t\tleft_par");
	            }
	       else if(arr[i] == ')'){
	                System.out.println(") \t\t\tright_par");
	            }
	       else if(arr[i] == ';') {
               System.out.println("; \t\t\tsemicolon");
           	}
		else if(arr[i] == ',') {
	                System.out.println(", \t\t\tcomma");
	            }	   
	       
	    	}	        
	} 
}
