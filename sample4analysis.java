
public class sample4analysis {
	/* This program prints the Fibonacci sequence 
	 * up to 1,000. It is the simple sample input 
	 * to lexAnalyz.java.
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Fibonacci sequence starts with 0 and 1.
		//Each iteration adds the two previous ones.
		int f0 = 0;
		int f = 1;
		int sum = 0;
		System.out.println(f0);
		while (sum <= 1000){
			System.out.println(f);
			sum = f + f0;
			f0 = f;
			f = sum;
		}
		
	}
}
