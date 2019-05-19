
public class BitMap {
	private static final long[] LOOKUP = new long[128];
	{
		for (int i=0; i<64; i++){
			LOOKUP[i] = 1l << i;
			LOOKUP[i+64] = 1l << i;
		}
	}
	
	
	private long l1;
	private long l2;
	public BitMap(){
		clear();
	}
	public void clear() {
		l1 = 0;
		l2 = 0;
	}
	public void or(BitMap other){
		l1 = l1 | other.l1;
		l2 = l2 | other.l2;
	}
	public boolean isSet(int ix){
		if (ix < 64)
			return ((l1 & LOOKUP[ix]) != 0);
		return ((l2 & LOOKUP[ix]) != 0);
	}
	public void set(int ix){
		if (ix < 64)
			l1 = l1 | LOOKUP[ix];
		else if (ix < 128)
			l2 = l2 | LOOKUP[ix];
	}
	public int trueCount() {
		int result = 0;
		for (int i =0; i<100; i++)
			if (isSet(i))
				result++;
		return result;
	}
}
