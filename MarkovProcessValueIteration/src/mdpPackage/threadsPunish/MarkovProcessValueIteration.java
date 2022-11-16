package mdpPackage.threadsPunish;

public class MarkovProcessValueIteration {

	
	// V(s) value function
    private double v[][]; 
    // V'(s) used in updates
    private double vNext[][];
    // List that store rewards
    private double r[][]; 
    // Store the policy itself
    private char pi[][];
    // Track error delta=|V(t+1)-V(t)|
    private double delta = 0;
    private int n;
    // Thread needs it
    // Name - included in Thread
    private String name;
    // num - included in Thread
    private int num = 0;
    int lst;
    
    public MarkovProcessValueIteration() {
    	//initialize policy as new Char List: in the end we want to have the characters - where to go U(up),D(down),L(left),R(right)
    	pi = new char[Constants.ROWS][Constants.COLUMNS]; 
    	//initialize the V value function values as new double List
    	v = new double[Constants.ROWS][Constants.COLUMNS];
    	//initialize V' as new double List
        vNext = new double[Constants.ROWS][Constants.COLUMNS];
        //initialize reward function R(s',s) as new double List
        r = new double[Constants.ROWS][Constants.COLUMNS];

        initVariables();
	}
     
    private void initVariables() {
		//we just have to initialize the R(s',s) rewards 
    	for (int rowIndex=0; rowIndex<Constants.ROWS; rowIndex++) {
            for (int columnIndex=0; columnIndex<Constants.COLUMNS; columnIndex++) {
                r[rowIndex][columnIndex] = Constants.REWARD;
            }
        }
    	
    	//initialize the +1 and -1 states + we have an unreachable state
    	r[0][3] =+100; 
        r[1][3] =-100; 
        r[1][1] =0;
	}

	public void run() {
    	
        do {
            copyArray(vNext, v);
            n++;
            delta = 0;
            
            for (int rowIndex=0; rowIndex<Constants.ROWS; rowIndex++) {
                for (int columnIndex=0; columnIndex<Constants.COLUMNS; columnIndex++) {
                	//manipulates vNext
                    update(rowIndex, columnIndex);
                    double error = Math.abs(vNext[rowIndex][columnIndex] - v[rowIndex][columnIndex]);
                   
                    //we make sure every state s it will converge |V(s)-V'(s)|<delta
                    //we just track the maximum error (as many error terms as the num of states)
                    //if the max error is smaller than epsilon -> the algorithm has converged !!!
                    if (error > delta)
                        delta = error;
                }
            }
        } while (delta > Constants.EPSILON && n < Constants.NUM_OF_ITERATIONS);
         
        //after iterating approximated V*(s) and pi*(s) functions are found, then print the result
        printResults();
    }
     // Function that is used to print results after iteration, function is called in run function
    private void printResults() {
    	
    	//display the V(s) value-function values
        System.out.println("The V(s) values after " + n + " iterations:\n");
        for (int rowIndex=0; rowIndex<Constants.ROWS; rowIndex++) {
            for (int columnIndex=0; columnIndex<Constants.COLUMNS; columnIndex++) {
                System.out.printf("% 6.5f\t", v[rowIndex][columnIndex]);
            }
            System.out.print("\n");
        }
        // Destination pi      
        pi[0][3]='+';
        // Punish agent pi - go to minus probability
        pi[1][3]='-';
        // obstacle in table
        pi[1][2]='@';
        
        //display the pi(s) policy-function: prints out what action to do in every state
        System.out.println("\nBest policy:\n");
        for (int rowIndex=0; rowIndex<Constants.ROWS; rowIndex++) {
            for (int columnIndex=0; columnIndex<Constants.COLUMNS; columnIndex++) {
                System.out.print(pi[rowIndex][columnIndex] + "   ");
            }
            System.out.print("\n");
        }    
	}

	public void update(int row, int col) {
         
        double actions[] = new double[4]; 
        // Counts how many times over n iteration agent step on wrong row+col
        if((row == 1 && col == 1) || (row == 1 && col == 2)) {
        	this.name = "Row: " + row + " Column: " + col;
        	 ThreadPunish tp = new ThreadPunish(this.name, this.num);
            tp.run();
            lst ++;
        }
     
        // If agent step on rows update vNext with current row parameters
        if ((row == 0 && col == 3) || (row == 1 && col == 3) || (row == 1 && col == 2)) {
            vNext[row][col] = r[row][col];
            
        } else {
        	//Calculating the P(s'|s,a)*V(s') values
        	actions[0] = Constants.ACTION_PROB*goUp(row,col) + Constants.ACTION_MISS_PROB*goLeft(row,col) + Constants.ACTION_MISS_PROB*goRight(row,col);
            actions[1] = Constants.ACTION_PROB*goDown(row,col) + Constants.ACTION_MISS_PROB*goLeft(row,col) + Constants.ACTION_MISS_PROB*goRight(row,col);
            actions[2] = Constants.ACTION_PROB*goLeft(row,col) + Constants.ACTION_MISS_PROB*goDown(row,col) + Constants.ACTION_MISS_PROB*goUp(row,col);
            actions[3] = Constants.ACTION_PROB*goRight(row,col) + Constants.ACTION_MISS_PROB*goDown(row,col) + Constants.ACTION_MISS_PROB*goUp(row,col);
            
            //Find the optimal by finding the max index (max index is best reward)
            int best = findMaxIndex(actions);
             
            //Calculate V with Bellman-equation - V(s)=R(s)+max[gamma*sum(P(s'|s,a)*V(s'))]
            vNext[row][col] = r[row][col] + Constants.GAMMA * actions[best];
             
            //UPDATE Policy (implement argmax) U-Up, D-Down, L-Left, R-Right 
            switch(best) {
            	case 0: 
            		pi[row][col]='U';
            		break;
            	case 1:
            		pi[row][col]='D';
            		break;
            	case 2:
            		pi[row][col]='L';
            		break;
            	case 3:
            		pi[row][col]='R';
            		break;
            }
        }
    }
     
	//Return optimal action index - function called in update function to find best Policy 
    public int findMaxIndex(double actions[]) {
        
    	int maxIndex=0;
        
        for (int i=1; i<actions.length; i++)
        	if( actions[i] > actions[maxIndex])
        		maxIndex = i;
        
        return maxIndex;
    }
    
    /*check if it's possible to go up
	if not agent stay at the same state */
    public double goUp(int row, int col) {
        if ((row==0) || (row==2 && col==2))
            return v[row][col];
        
        //go up
        return v[row-1][col];
    }
    
	/*check if it's possible to go down
    	if not agent stay at the same state*/
    public double goDown(int row, int col) {
        if ((row==Constants.ROWS-1) || (row==0 && col==2))
            return v[row][col];
        
        //go down
        return v[row+1][col];
    }
    
    /*check if it's possible to go left
	if not agent stay at the same state*/
    public double goLeft(int row, int col) {
        if ((col==0) || (row==1 && col==3))
            return v[row][col];
        
        //go left 
        return v[row][col-1];
    }
    
    /*check if it's possible to go right
	if not agent stay at the same state*/
    public double goRight(int row, int col) {
        if ((col==Constants.COLUMNS-1) || (row==1 && col==1))
            return v[row][col];
        
        //go right
        return v[row][col+1];
    }
     
    public void copyArray(double[][] sourceArray, double[][] destionationArray) {
        for (int i=0; i<sourceArray.length; i++) {
            for (int j=0; j<sourceArray[i].length; j++) {
                destionationArray[i][j] = sourceArray[i][j];
            }
        }
    }

	public String getName() {
		return name;
	}
	
	
	public int getNum() {
		return lst - 1;
	}
    
    
}
