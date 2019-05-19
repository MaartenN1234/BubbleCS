import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

public class BubbleSolver {
	final static int messageInterval             = 100000;//100000;

	final static int reachedStatesCapacity       = 6 * 1000 * 1000;
	final static int meetInMiddleCapacity        = 1 * 1000 * 1000;
	final static int propagatedStatesCapacity    = 1 * 1000 * 1000;

	final static int reachedStatesCapacityMIM    = 1000;
	final static int meetInMiddleCapacityMIM     = 0;

	final static int meetInMiddleThreshold       = 40; 
	final static private int LIMIT = 289;
	final static private int DISPLAY_LIMIT = 289;
	final static public double  HEUR_FACTOR        = 4.;
	final static public boolean HEUR_SIMPLE_MODE   = true;
	final static public boolean HEUR_SUBSCORE_MODE = false;
	
	public static BubbleState getOptimalHeuristic(BubbleState initialState){
		return getOptimalHeuristic(initialState, true);
	}

	public static BubbleState getOptimalHeuristic(BubbleState initialState, boolean startState){
		BubbleState bestState = initialState;
		HashMap<BubbleState, BubbleState> reachedStates       = new HashMap<BubbleState, BubbleState>(startState ? reachedStatesCapacity : reachedStatesCapacityMIM);
		HashMap<BubbleState, BubbleState> meetInMiddleForward = new HashMap<BubbleState, BubbleState>(startState ? meetInMiddleCapacity : meetInMiddleCapacityMIM);
		Queue<BubbleState> toPropagateStates                  = startState ?
										new PriorityQueue<BubbleState>(propagatedStatesCapacity) :
										new LinkedList<BubbleState>();
		reachedStates.put(initialState, initialState);
		toPropagateStates.add(initialState);
		long l = System.currentTimeMillis();
		long counter = 0;
		while (!toPropagateStates.isEmpty()){
			BubbleState propagatingState = toPropagateStates.poll();
			if (startState && (counter++ %messageInterval ==0))
				System.out.println("ReachedStates: "+reachedStates.size()+"\tMIMStates: "+meetInMiddleForward.size()+"\tPropagatableStates: "+toPropagateStates.size() + "\t" + (System.currentTimeMillis()-l)+"ms");

			for(BubbleState propagatedState : propagatingState.getSubsequentStates()){
				propagatedState.score = propagatingState.score + propagatedState.lastMoveScore;
				if(reachedStates.containsKey(propagatedState)){
					BubbleState alreadyExistingState = reachedStates.get(propagatedState);
					if (propagatedState.score > alreadyExistingState.getScore()){
						alreadyExistingState.lastMovebest  = propagatedState.lastMovebest;
						alreadyExistingState.lastMoveScore = propagatedState.lastMoveScore;
						alreadyExistingState.prevStatebest = propagatedState.prevStatebest;
						alreadyExistingState.score         = propagatedState.score;
						bestState.getScore();
						if( meetInMiddleForward.get(propagatedState) != null){
							propagatedState = meetInMiddleForward.get(propagatedState);
							propagatedState.getScore();
						}
					}						
				} else {
					int check = (startState && LIMIT >  bestState.score) ? LIMIT :  bestState.score;
					if (propagatedState.calcOpportunityScore() + propagatedState.score > check){
						reachedStates.put(propagatedState, propagatedState);
						if (startState && propagatedState.getCount() <= meetInMiddleThreshold){
							BubbleState.cloneResultList();
							BubbleState finalState = getOptimalHeuristic(propagatedState, false);
							meetInMiddleForward.put(propagatedState, finalState);
							propagatedState = finalState;
						} else {
							toPropagateStates.offer(propagatedState);
						}
					}
				}
				if (propagatedState.score > bestState.score){
					bestState = propagatedState;
					if (startState && bestState.score>DISPLAY_LIMIT)
						System.out.println(bestState);
				}
			}
		}
		
		return bestState;
	}
	
	public static BubbleState getOptimalBruteForce(BubbleState initialState){
		BubbleState bestState = initialState;
		for(BubbleState propagatedState : new ArrayList<BubbleState>(initialState.getSubsequentStates())){
			BubbleState propagatedFinalState = getOptimalBruteForce(propagatedState);
			if (propagatedFinalState.score > bestState.score)
					bestState = propagatedFinalState;
		}		
		return bestState;
	}
}
