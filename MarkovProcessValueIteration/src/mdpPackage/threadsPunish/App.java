package mdpPackage.threadsPunish;

public class App  {  
 
    public static void main(String[] args) {
		
    	MarkovProcessValueIteration mvi = new MarkovProcessValueIteration();
    	mvi.run();
    	System.out.println("Agent was punished: " + mvi.getNum() + " times");
    	System.out.println("That is equal to number of threads agents created while stepping on a wrong row + col");
    	
	}
}
