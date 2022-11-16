package mdpPackage.threadsPunish;

public class Constants {

	private Constants() {
		
	}
	
	// reward in non-terminal states (used to initialize r[][])
	public static double REWARD = -15;    
	//gamma discount factor: how to deal with future rewards [0,1]
	public static double GAMMA = 0.95;   
    //make the system stochastic: with 80% probability we go where we want
    public static double ACTION_PROB = 0.8;       
    //with 10% probability we go in another direction (left or right) 10% left, 10% right
    public static double ACTION_MISS_PROB = 0.1; 
    //number of iterations
    public static int NUM_OF_ITERATIONS = 100;  
    //to measure the difference |V(t+1)-V(t)| ~ convergence criterion
    public static double EPSILON = 1e-7;  
    //board's height
    public static int ROWS = 5;
    //board's width
    public static int COLUMNS = 8;
}
