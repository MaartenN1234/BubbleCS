import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BubbleState implements Comparable<BubbleState>{
	public final static int EMPTY = 0;
	public final static int GREEN = 1;
	public final static int RED   = 2;
	public final static int YELLOW= 3;
	public final static int BLUE  = 4;
	public final static int CYAN  = 5;
	public final static int PURPLE  = 6;
	public final static int SILVER  = 7;
	public final static int GOLD  = 8;	
	public final static String[] colors = {"EMPTY","GREEN","RED","YELLOW","BLUE","CYAN","PURPLE","SILVER","GOLD"};
	public final static String[] colorsS = {" ","G","R","Y","B","C","P","S","G"};
	public final static int [] scores = {0,0,2,6,12,20,30,42,56,72,90,110,132,156,182,210,240,272,306,342,380,420,462,506,552,600,650,702,756,812,870,930,992,1056,1122,1190,1260,1332,1406,1482,1560,1640,1722,1806,1892,1980,2070,2162,2256,2352,2450,2550,2652,2756,2862,2970,3080,3192,3306,3422,3540,3660,3782,3906,4032,4160,4290,4422,4556,4692,4830,4970,5112,5256,5402,5550,5700,5852,6006,6162,6320,6480,6642,6806,6972,7140,7310,7482,7656,7832,8010,8190,8372,8556,8742,8930,9120,9312,9506,9702,9900};
	public final static int [] scoresS = {0,0,0,2,6,12,20,30,42,56,72,90,110,132,156,182,210,240,272,306,342,380,420,462,506,552,600,650,702,756,812,870,930,992,1056,1122,1190,1260,1332,1406,1482,1560,1640,1722,1806,1892,1980,2070,2162,2256,2352,2450,2550,2652,2756,2862,2970,3080,3192,3306,3422,3540,3660,3782,3906,4032,4160,4290,4422,4556,4692,4830,4970,5112,5256,5402,5550,5700,5852,6006,6162,6320,6480,6642,6806,6972,7140,7310,7482,7656,7832,8010,8190,8372,8556,8742,8930,9120,9312,9506,9702,9900};
	public final static int WIDTH  = 10;
	public final static int HEIGHT = 10;
	public final static int HEIGHTD= HEIGHT/2;
	public final static int BOUNDS_LIMIT = WIDTH*HEIGHT;
	public final static int ARR_SIZE     = BOUNDS_LIMIT/2;
	
	public final byte[] state; //108
	
	public int lastMoveScore;      //4
	public int score;              //4
	public byte  lastMovebest;     //1
	public BubbleState prevStatebest;    //8
	private int  compareScore= -1; //4
	
	private byte greenCount  = 0; 
	private byte redCount    = 0;
	private byte yellowCount = 0;
	private byte blueCount   = 0;
	private byte cyanCount   = 0;	
	private boolean isShiftingState = true;
	//108+5+21=134
	
	public BubbleState(byte[][] inState){
		state    = transformInput(inState);
		fallStones();
		lastMoveScore = 0;
		score         = 0;
		lastMovebest  = -1;
		prevStatebest = null;
		determineCounts();
	}
	
	BubbleState(byte[] innerState){
		state         = innerState;
		lastMoveScore = 0;
		score         = 0;
		lastMovebest  = -1;
		prevStatebest = null;
		determineCounts();		
	}
	


	static byte[] transformInput(byte[][] in) {
		byte[] result = new byte[ARR_SIZE];
		for (int i=0; i<WIDTH; i++)
			for (int j=0; j<HEIGHT; j++){
				if ((j & 1) == 1)
					result[(i*HEIGHT+j)>>1] |= in[j][i] << 4;
				else
					result[(i*HEIGHT+j)>>1] |= in[j][i];
			}
		return result;
	}

	public static BubbleState applyMove(BubbleState reference, byte move) {
		connectedNodesPrep.clear();
		int col = reference.getColForLoc(move);
		reference.getConnectedNodes(connectedNodesPrep, move, col);
		return new BubbleState(reference, connectedNodesPrep, col, false);
		
	}
	
	private BubbleState(BubbleState reference, BitMap nodes, int col, boolean switchUseMem) {
		isShiftingState = false;
		
		state = new byte[ARR_SIZE];
		System.arraycopy(reference.state, 0, state, 0, ARR_SIZE);

		int nodeCount = 0;

		for (int node = 0; node<BOUNDS_LIMIT; node++)
		if (nodes.isSet(node)){
			nodeCount++;
			lastMovebest = (byte) node;
			if ((node & 1) == 1)
				state[node>>1] &= 0x0F;
			else
				state[node>>1] &= 0xF0;
		}
		isShiftingState = fallStones();
		
		lastMoveScore = scores[nodeCount];
		score         = reference.score+lastMoveScore;
		prevStatebest = reference;
		
		greenCount  = reference.greenCount;
		redCount    = reference.redCount;
		yellowCount = reference.yellowCount;
		blueCount   = reference.blueCount;
		cyanCount   = reference.cyanCount;
		updateCounts(col, nodeCount);
	
		if (switchUseMem)
			while(attemptPropagate());
		else
			while(attemptPropagateAlt());
	}

	private int getColForLoc(int loc){
		if ((loc & 1) == 1)
			return state[loc>>1] >> 4;
		else
			return state[loc>>1] & 0xF;
	}

	private void setColForLoc(int loc, int col) {
		if ((loc & 1) == 1){
			state[loc>>1] = (byte) ((state[loc>>1] & 0x0F) | (col << 4));
		} else {
			state[loc>>1] = (byte) ((state[loc>>1] & 0xF0) | col);
		}
	}


	private void updateCounts(int col, int nodeCount) {
		switch(col){
		case GREEN:  greenCount -=nodeCount; break;
		case RED:    redCount   -=nodeCount; break;
		case YELLOW: yellowCount-=nodeCount; break;
		case BLUE:   blueCount  -=nodeCount; break;
		case CYAN:   cyanCount  -=nodeCount; break;
		}	
	}



	private boolean attemptPropagate() {
		int selCol = -1;
		
		alreadyDoneProp.clear();	
		for (int move = 0; move<BOUNDS_LIMIT; move++){
			int col  = getColForLoc(move);
			if (!alreadyDoneProp.isSet(move) && col != EMPTY){
				if ((move+HEIGHT<BOUNDS_LIMIT && getColForLoc(move+HEIGHT) == col) || ((move+1)% HEIGHT != 0 && getColForLoc(move+1) == col)){
					if (selCol != -1)
						return false; // more than one possible move--> do nothing
					connectedNodes.clear();
					getConnectedNodes(connectedNodes, move, col);
					alreadyDoneProp.or(connectedNodes);
					selCol  = col;
				}
			}
		}
		
		if(selCol==-1){
			//no further opportunity:
			greenCount  = 0;
			redCount    = 0;
			yellowCount = 0;
			blueCount   = 0;
			cyanCount   = 0;
			return false;
		} else {
			// exactly one possible move, execute:
			int nodeCount = 0;

			for (int node = 0; node<BOUNDS_LIMIT; node++)
			if (connectedNodes.isSet(node)){
				nodeCount++;
				if ((node & 1) == 1)
					state[node>>1] &= 0x0F;
				else
					state[node>>1] &= 0xF0;
			}
			fallStones();
			
			lastMoveScore += scores[nodeCount];
			score         += scores[nodeCount];
			updateCounts(selCol, nodeCount);
			return true;
		}
	}
	private boolean attemptPropagateAlt() {
		int selCol = -1;
		
		alreadyDone2.clear();	
		for (int move = 0; move<BOUNDS_LIMIT; move++){
			int col  = getColForLoc(move);
			if (!alreadyDone2.isSet(move) && col != EMPTY){
				if ((move+HEIGHT<BOUNDS_LIMIT && getColForLoc(move+HEIGHT) == col) || ((move+1)% HEIGHT != 0 && getColForLoc(move+1) == col)){
					if (selCol != -1)
						return false; // more than one possible move--> do nothing
					connectedNodes2.clear();
					getConnectedNodes(connectedNodes2, move, col);
					alreadyDone2.or(connectedNodes2);
					selCol  = col;
				}
			}
		}
		
		if(selCol==-1){
			//no further opportunity:
			greenCount  = 0;
			redCount    = 0;
			yellowCount = 0;
			blueCount   = 0;
			cyanCount   = 0;
			return false;
		} else {
			// exactly one possible move, execute:
			int nodeCount = 0;

			for (int node = 0; node<BOUNDS_LIMIT; node++)
			if (connectedNodes.isSet(node)){
				nodeCount++;
				if ((node & 1) == 1)
					state[node>>1] &= 0x0F;
				else
					state[node>>1] &= 0xF0;
			}
			fallStones();
			
			lastMoveScore += scores[nodeCount];
			score         += scores[nodeCount];
			updateCounts(selCol, nodeCount);
			return true;
		}
	}


	public boolean fallStones() {
		boolean hasMoved = false;
		// fall down
		for (int i = 0; i<WIDTH; i++){
			int k = HEIGHT - 1;
			int l = HEIGHT - 2;
			for (int j=1; j<HEIGHT; j++){
				if (getColForLoc(i*HEIGHT+k) == EMPTY){
					if (getColForLoc(i*HEIGHT+l) == EMPTY){
						l--;
					} else {
						int z = getColForLoc(i*HEIGHT+l);
						hasMoved |= (z != EMPTY);
						setColForLoc(i*HEIGHT+k, z);
						setColForLoc(i*HEIGHT+l, EMPTY);
						k--;
						l--;
					}
				} else {
					k--;
					l--;
				}
			}
		}
		
		//move aside
		int k = WIDTH - 1;
		int l = WIDTH - 2;
		for (int j=1; j<WIDTH; j++){
			if (state[k*HEIGHTD+HEIGHTD-1] == EMPTY){
				if (state[l*HEIGHTD+HEIGHTD-1] == EMPTY){
					l--;
				} else {
					for (int i=0; i<HEIGHTD; i++){
						byte z = state[l*HEIGHTD +i];
						hasMoved |= (z != EMPTY);
						state[k*HEIGHTD +i] = z;
						state[l*HEIGHTD +i] = EMPTY;
					}
					k--;
					l--;
				}
			} else {
				k--;
				l--;
			}
		}		
		return hasMoved;
	}


	private static BitMap connectedNodes     = new BitMap();
	private static BitMap connectedNodes2    = new BitMap();	
	private static BitMap connectedNodes3    = new BitMap();
	private static BitMap connectedNodesPrep = new BitMap();	
	private static BitMap alreadyDone        = new BitMap();
	private static BitMap alreadyDone2       = new BitMap();
	private static BitMap alreadyDone3       = new BitMap();
	private static BitMap alreadyDoneProp    = new BitMap();
	private static List<BubbleState>  results      = new ArrayList<BubbleState>();
	public static void cloneResultList(){
		results  = new ArrayList<BubbleState>();
	}
	
	public List<BubbleState> getSubsequentStates(){
		getScore();
		results.clear();
		alreadyDone.clear();	
		boolean hasHadShiftingState = false;
		for (int move = 0; move<BOUNDS_LIMIT; move++){
			int col  = getColForLoc(move);
			if ( col != EMPTY && !alreadyDone.isSet(move)){
				if ((move+HEIGHT<BOUNDS_LIMIT && getColForLoc(move+HEIGHT) == col) || ((move+1)% HEIGHT != 0 && getColForLoc(move+1) == col)){
					connectedNodes.clear();
					getConnectedNodes(connectedNodes, move, col);
					alreadyDone.or(connectedNodes);
					BubbleState newSubsState = new BubbleState(this, connectedNodes, col, true);
					/*
					if (newSubsState.isShiftingState){
						if (!hasHadShiftingState){
							hasHadShiftingState = true;
							results.clear();
						}
						results.add(newSubsState);
					} else {
						if (!hasHadShiftingState){
							results.add(newSubsState);
						} 
					}
					*/
					results.add(newSubsState);
				}
			}
		}
		
		return results;
	}


	private void getConnectedNodes(BitMap connectedNodes, int move, int col) {
		connectedNodes.set(move);
		int i = move / HEIGHT;
		int j = move % HEIGHT;
		if (i>0 && getColForLoc(move-HEIGHT) == col && !connectedNodes.isSet(move-HEIGHT))
			getConnectedNodes(connectedNodes, move - HEIGHT, col);
		if (j>0 && getColForLoc(move-1) == col && !connectedNodes.isSet(move-1))
			getConnectedNodes(connectedNodes, move - 1, col);
		if (i+1<WIDTH && getColForLoc(move+HEIGHT) == col && !connectedNodes.isSet(move+HEIGHT))
			getConnectedNodes(connectedNodes, move + HEIGHT, col);
		if (j+1<HEIGHT && getColForLoc(move+1) == col && !connectedNodes.isSet(move+1))
			getConnectedNodes(connectedNodes, move + 1, col);
	}



	private void determineCounts() {
		for(byte x : state){
			switch(x & 0xF){
			case GREEN:  greenCount++;  break;
			case RED:    redCount++;    break;
			case YELLOW: yellowCount++; break;
			case BLUE:   blueCount++;   break;
			case CYAN:   cyanCount++;   break;
			}		
			switch(x >> 4){
			case GREEN:  greenCount++;  break;
			case RED:    redCount++;    break;
			case YELLOW: yellowCount++; break;
			case BLUE:   blueCount++;   break;
			case CYAN:   cyanCount++;   break;
			}		
		}
	}
	int calcOpportunityScore() {
		return scores[greenCount]+scores[redCount]+scores[blueCount]+scores[yellowCount]+scores[cyanCount];
	}
	int calcOpportunitySubScore() {
		return scoresS[greenCount]+scoresS[redCount]+scoresS[blueCount]+scoresS[yellowCount]+scoresS[cyanCount];
	}
	int getCount() {
		return greenCount+redCount+blueCount+yellowCount+cyanCount;
	}
	public int getScore(){
		score = lastMoveScore + ((prevStatebest == null) ? 0 : prevStatebest.getScore());
		return score;
	}
	
	
	public boolean equals(Object o){
		if (o instanceof BubbleState){
			return Arrays.equals(state, ((BubbleState) o).state);
		}
		return false;
	}
	public int hashCode(){
		return Arrays.hashCode(state);
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		toStringAppender(sb);	

		for (int i=0; i<WIDTH; i++){
			sb.append(colorsS[getColForLoc(i)]);
			for (int j=1; j<HEIGHT; j++){
				sb.append(", ");
				sb.append(colorsS[getColForLoc(j*HEIGHT+i)]);
			}
			sb.append("\r\n");
		}
		sb.append("Total score: "+getScore());
		return sb.toString();
	}
	private void toStringAppender(StringBuffer sb){
		if (prevStatebest != null){
			prevStatebest.toStringAppender(sb);
			int i = (lastMovebest / HEIGHT);
			int j = (lastMovebest % HEIGHT);
			int col = prevStatebest.getColForLoc(lastMovebest);
			sb.append("Move ("+ (1+i) + "."+(1+j) + ") color " + colors[col] + " move score " + lastMoveScore+"\r\n");
		}
	}



	private void validateCompareScore(){
		if (compareScore == -1){
			if (BubbleSolver.HEUR_SIMPLE_MODE)
				compareScore = (int) (BubbleSolver.HEUR_FACTOR * (score) + (BubbleSolver.HEUR_SUBSCORE_MODE ? calcOpportunitySubScore() : calcOpportunityScore()));
			else {
				alreadyDone3.clear();	
				int maxNodeCount = 0;
				for (int move = 0; move<BOUNDS_LIMIT; move++){
					int col  = getColForLoc(move);
					if ( col != EMPTY && !alreadyDone3.isSet(move)){
						if ((move+HEIGHT<BOUNDS_LIMIT && getColForLoc(move+HEIGHT) == col) || ((move+1)% HEIGHT != 0 && getColForLoc(move+1) == col)){
							connectedNodes3.clear();
							getConnectedNodes(connectedNodes3, move, col);
							int nodeCount = connectedNodes3.trueCount();
							if (nodeCount > maxNodeCount){
								maxNodeCount = nodeCount;
							}
							alreadyDone3.or(connectedNodes3);
							

						}
					}
				}
				compareScore = (int) (BubbleSolver.HEUR_FACTOR * (score + scores[maxNodeCount])+ (BubbleSolver.HEUR_SUBSCORE_MODE ? calcOpportunitySubScore() : calcOpportunityScore()));
			}
		}
	}
	public int compareTo(BubbleState o) {
		validateCompareScore();
		o.validateCompareScore();
		return compareScore > o.compareScore ? 1 : compareScore == o.compareScore ? 0 : -1;
	}

	public byte[] getMoves() {
		List<Byte> internResult = new ArrayList<Byte>(); 
		getMovesIntern(internResult);
		byte[] result = new byte[internResult.size()];
		int i=0;
		for (byte b : internResult){
			result[i++] = b;
		}
		return result;
	}
	private void getMovesIntern(List<Byte> result) {
		if (prevStatebest != null){
			prevStatebest.getMovesIntern(result);
			result.add(lastMovebest);
		}		
	}
}
