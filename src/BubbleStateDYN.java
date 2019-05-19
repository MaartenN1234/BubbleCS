import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BubbleStateDYN implements Comparable<BubbleStateDYN>{
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
	public final static int WIDTH  = 10;
	public final static int HEIGHT = 10;
	public final static int HEIGHTD= HEIGHT/2;
	public final static int BOUNDS_LIMIT = WIDTH*HEIGHT;
	public final static int ARR_SIZE     = BOUNDS_LIMIT/2;
	
	public final byte[] state; //59 +1 per move
	public int     score;      //4


	public BubbleStateDYN(byte[][] inState){
		state    = transformInput(inState);
		fallStones();
		score         = 0;
	}
	
	BubbleStateDYN(byte[] innerState){
		state         = new byte[ARR_SIZE+1];
		System.arraycopy(innerState, 0, state, 0, ARR_SIZE+1);
		score         = 0;
	}
	


	static byte[] transformInput(byte[][] in) {
		byte[] result = new byte[ARR_SIZE+1];
		for (int i=0; i<WIDTH; i++)
			for (int j=0; j<HEIGHT; j++){
				if ((j & 1) == 1)
					result[(i*HEIGHT+j)>>1] |= in[j][i] << 4;
				else
					result[(i*HEIGHT+j)>>1] |= in[j][i];
			}
		result[ARR_SIZE] = 0;
		return result;
	}

	public static BubbleStateDYN applyMove(BubbleStateDYN reference, byte move) {
		connectedNodesPrep.clear();
		int col = reference.getColForLoc(move);
		reference.getConnectedNodes(connectedNodesPrep, move, col);
		return new BubbleStateDYN(reference, connectedNodesPrep, false);
		
	}
	
	private BubbleStateDYN(BubbleStateDYN reference, BitMap nodes, boolean switchUseMem) {
		state = new byte[reference.state.length+1];
		System.arraycopy(reference.state, 0, state, 0, reference.state.length);

		int nodeCount = 0;
		byte move =-1;

		for (int node = 0; node<BOUNDS_LIMIT; node++)
		if (nodes.isSet(node)){
			nodeCount++;
			move = (byte) node;
			if ((node & 1) == 1)
				state[node>>1] &= 0x0F;
			else
				state[node>>1] &= 0xF0;
		}
		fallStones();
		
		score       = reference.score+scores[nodeCount];
		state[reference.state.length] = move;

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
			state[ARR_SIZE] = 1;

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
			
			score         += scores[nodeCount];
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
			state[ARR_SIZE] = 1;
			
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
			
			score         += scores[nodeCount];
			return true;
		}
	}


	public void fallStones() {
		// fall down
		for (int i = 0; i<WIDTH; i++){
			int k = HEIGHT - 1;
			int l = HEIGHT - 2;
			for (int j=1; j<HEIGHT; j++){
				if (getColForLoc(i*HEIGHT+k) == EMPTY){
					if (getColForLoc(i*HEIGHT+l) == EMPTY){
						l--;
					} else {
						setColForLoc(i*HEIGHT+k, getColForLoc(i*HEIGHT+l));
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
						state[k*HEIGHTD +i] = state[l*HEIGHTD +i];
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
	}


	private static BitMap connectedNodes     = new BitMap();
	private static BitMap connectedNodes2    = new BitMap();	
	private static BitMap connectedNodesPrep = new BitMap();	
	private static BitMap alreadyDone        = new BitMap();
	private static BitMap alreadyDone2       = new BitMap();
	private static BitMap alreadyDoneProp    = new BitMap();
	private static List<BubbleStateDYN>  results      = new ArrayList<BubbleStateDYN>();
	public static void cloneResultList(){
		results  = new ArrayList<BubbleStateDYN>();
	}
	
	public List<BubbleStateDYN> getSubsequentStates(){
		getScore();
		results.clear();
		alreadyDone.clear();	
		for (int move = 0; move<BOUNDS_LIMIT; move++){
			int col  = getColForLoc(move);
			if ( col != EMPTY && !alreadyDone.isSet(move)){
				if ((move+HEIGHT<BOUNDS_LIMIT && getColForLoc(move+HEIGHT) == col) || ((move+1)% HEIGHT != 0 && getColForLoc(move+1) == col)){
					connectedNodes.clear();
					getConnectedNodes(connectedNodes, move, col);
					alreadyDone.or(connectedNodes);
					results.add(new BubbleStateDYN(this, connectedNodes, true));
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



	int getCount() {
		int result = 0;
		for(int i = 0; i<ARR_SIZE; i++){
			if ((state[i] & 0xF) != EMPTY)
				result++;
			if ((state[i] >> 4) != EMPTY)
				result++;
		}
		return result;
	}
	
	public int getScore(){
		return score;
	}
	
	
	public boolean equals(Object o){
		if (o instanceof BubbleStateDYN){
			byte[] otherState = ((BubbleStateDYN) o).state;
			for(int i = 0; i<ARR_SIZE; i++){
				if (state[i] != otherState[i])
					return false;
			}
			return true;
		}
		return false;
	}
	public int hashCode(){
		int result = 0;
		for(int i = 0; i<ARR_SIZE; i++){
			result = ((result + state[i]) << 7) % 1734511;
		}
		return result;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		toStringAppender(sb);	
		sb.append("Total score: "+getScore());
		return sb.toString();
	}
	private void toStringAppender(StringBuffer sb){
		for (byte move : getMoves()){
			int k = (move / HEIGHT);
			int j = (move % HEIGHT);
			sb.append("Move ("+ (1+k) + "."+(1+j) + ") ");;
		}
		sb.append("Score " + score+"\r\n");
		
		for (int i=0; i<WIDTH; i++){
			sb.append(colorsS[getColForLoc(i)]);
			for (int j=1; j<HEIGHT; j++){
				sb.append(", ");
				sb.append(colorsS[getColForLoc(j*HEIGHT+i)]);
			}
			sb.append("\r\n");
		}
	}

	public boolean isContinuableState(){
		return state[ARR_SIZE] == 0;
	}

	
	public int compareTo(BubbleStateDYN o) {
		return score > o.score ? 1 : score == o.score ? 0 : -1;
	}

	public byte[] getMoves() {
		byte [] result = new byte[state.length-1-ARR_SIZE];
		System.arraycopy(state, ARR_SIZE+1, result, 0, result.length);
		return result;
	}
}
