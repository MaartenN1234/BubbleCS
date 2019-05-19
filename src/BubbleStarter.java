import java.util.Arrays;

public class BubbleStarter {
	static byte [][]testCaseX = new byte[][]{  {1,2,3,4,5,1,2,3,4,5},
											 {2,3,4,5,1,2,3,4,5,1},
											 {3,4,0,1,0,3,4,0,1,2},
											 {2,3,4,5,1,2,3,4,5,1},
											 {3,4,0,1,0,3,4,0,1,2},
											 {4,0,3,3,3,3,0,1,2,5},
											 {1,0,3,4,0,1,2,5,4,0},
											 {3,4,3,3,3,3,4,0,1,2},
											 {4,0,1,0,3,4,0,1,2,5},
											 {1,0,3,4,0,1,2,5,4,0}};
	static byte [][]testCaseY = new byte[][]{{1,2,3,4,5,1,2,3,4,5},
		 {2,3,4,5,0,2,3,4,5,1},
		 {3,4,0,1,0,3,4,0,1,2},
		 {2,3,4,5,0,2,3,4,5,1},
		 {0,0,0,0,0,0,0,0,0,0},
		 {4,0,1,0,0,4,0,1,2,5},
		 {1,0,3,4,0,1,2,5,4,0},
		 {3,4,0,1,0,3,4,0,1,2},
		 {4,0,1,0,0,4,0,1,2,5},
		 {1,0,3,4,0,1,2,5,4,0}};	
			static byte [][]testCaseY2 = new byte[][]{{1,2,0,0,0,1,0,0,4,5},
				 {2,3,4,5,0,2,0,4,5,1},
				 {3,0,0,1,0,0,4,0,0,2},
				 {2,3,4,5,0,2,3,0,5,1},
				 {0,0,0,0,0,0,0,0,0,0},
				 {4,0,1,0,0,4,0,1,2,5},
				 {1,0,3,0,0,1,2,5,4,0},
				 {3,0,0,1,0,0,4,0,1,2},
				 {4,0,1,0,0,0,0,1,2,5},
				 {1,0,3,4,0,1,2,5,4,0}};	
					static byte [][]testCaseY3 = new byte[][]{{1,2,0,0,0,1,0,0,4,5},
						 {0,0,0,0,0,0,0,0,0,0},
						 {0,0,0,0,0,0,0,0,0,0},
						 {1,0,3,0,0,1,2,5,4,0},
						 {0,0,0,0,0,0,0,0,0,0},
						 {4,0,1,0,0,4,0,1,2,5},
						 {1,0,3,0,0,1,2,5,4,0},
						 {3,0,0,1,0,0,4,0,1,2},
						 {0,0,0,0,0,0,0,0,0,0},
						 {1,0,3,4,0,1,2,5,4,0}};	
				 static byte[][]testCaseZ = new byte[][]{ {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0}};
	
	static byte[][]puzzle0001 = new byte[][]{{2,5,2,5,4,3,1,2,5,4},
										 {4,1,5,4,1,5,4,1,4,4},
										 {3,1,2,5,5,5,3,2,1,5},
										 {1,2,4,2,5,5,3,2,2,5},
										 {2,5,4,2,2,1,1,3,5,2},
										 {1,1,4,2,3,2,4,4,3,1},
										 {1,2,3,2,3,1,1,1,5,5},
										 {2,5,3,3,3,2,4,1,4,3},
										 {1,3,1,2,3,2,4,5,4,5},
										 {5,4,3,4,3,5,3,3,4,1}};
			static byte[][]puzzle0002 = new byte[][]{{5,5,4,4,1,2,5,3,5,1},
				 {2,5,1,3,3,1,5,3,2,1},
				 {3,3,3,5,1,2,4,1,1,4},
				 {2,3,5,3,4,5,1,3,1,5},
				 {3,2,1,2,1,2,1,2,2,3},
				 {4,1,1,5,4,4,4,4,1,1},
				 {3,3,4,1,2,2,2,1,5,4},
				 {2,4,3,2,5,5,5,5,2,1},
				 {2,5,2,5,4,1,4,3,3,2},
				 {4,2,1,3,3,1,2,1,2,4}};

					static byte[][]puzzle0031 = new byte[][]{{4,2,4,2,5,5,5,4,5,2},
						 {2,2,2,5,5,4,4,2,2,5},
						 {4,4,2,2,2,2,2,5,2,4},
						 {4,4,2,2,2,2,2,4,4,4},
						 {4,2,2,2,5,4,2,4,2,2},
						 {2,5,5,5,2,5,4,4,5,2},
						 {2,2,4,2,4,2,4,4,4,5},
						 {4,5,5,5,5,4,2,4,5,5},
						 {2,4,5,4,5,2,4,5,4,5},
						 {5,4,2,2,4,2,5,2,5,4}};				 
						 static byte[][]puzzle0032 = new byte[][]{{4,4,5,2,5,2,5,4,2,5},
							 {2,2,5,4,4,5,4,4,4,4},
							 {5,2,5,4,5,2,4,5,5,2},
							 {2,2,5,4,4,4,5,5,5,4},
							 {4,4,4,2,5,2,4,4,2,2},
							 {4,2,4,2,5,2,2,4,2,5},
							 {4,2,2,4,2,5,4,2,5,5},
							 {2,5,5,2,5,4,2,5,2,5},
							 {5,2,2,2,4,2,2,2,4,4},
							 {4,5,5,5,4,4,2,4,2,5} };
							 static byte[][]puzzle0003 = new byte[][]{{1,5,2,2,1,3,1,1,4,1},
								 {5,1,2,4,2,3,5,2,1,4},
								 {5,4,3,5,1,5,4,2,5,2},
								 {1,2,1,5,3,4,4,5,5,4},
								 {4,1,2,5,3,3,4,2,1,2},
								 {3,2,3,5,5,3,4,1,3,1},
								 {1,5,3,1,5,1,2,3,1,2},
								 {5,3,1,2,4,4,1,5,1,2},
								 {1,4,3,3,4,1,2,2,1,3},
								 {3,2,1,1,1,3,5,4,4,2}
							 };

							 
			/* GREEN = 1;
			RED   = 2;
			YELLOW= 3;
			BLUE  = 4;
			CYAN  = 5;
			PURLE = 6;
			SILVER= 7;
			GOLD  = 8;
			*/

 static byte[][]puzzle0033 = new byte[][]{
		{3,3,3,4,2,2,5,3,3,2},
		{3,2,2,5,5,5,3,5,4,4},
		{2,2,5,3,4,2,3,3,3,2},
		{5,4,5,5,5,3,2,5,5,5},
		{5,5,4,5,2,4,4,3,3,2},
		{5,2,3,2,5,5,3,4,2,3},
		{2,4,3,4,2,4,5,5,3,5},
		{3,5,2,5,3,3,2,5,4,5},
		{2,2,5,3,5,2,3,5,4,5},
		{3,4,4,3,4,5,3,5,5,3}
		};					 
static byte[][]puzzle0034 = new byte[][]{
	{4,2,3,2,5,3,3,4,4,4},
	{5,2,3,3,4,2,3,2,5,4},
	{2,5,3,5,2,2,4,5,5,3},
	{5,4,5,4,5,5,5,4,4,5},
	{4,2,4,3,3,3,3,3,2,3},
	{3,5,3,2,2,4,2,5,5,2},
	{5,4,5,2,5,5,4,2,3,3},
	{2,3,3,5,3,4,2,4,5,3},
	{4,4,4,5,4,5,2,2,5,3},
	{5,2,5,5,5,4,3,5,4,3}
	};					 
	static byte[][]puzzle0035 = new byte[][]{
		{1,2,2,1,7,3,3,3,2,5},
		{1,2,5,1,4,7,5,1,4,4},
		{4,5,5,5,3,1,2,2,1,1},
		{1,7,4,3,1,2,1,2,5,4},
		{5,7,4,3,1,5,3,2,3,4},
		{7,7,3,4,4,3,7,4,4,7},
		{1,3,3,3,1,2,2,1,5,7},
		{5,5,3,1,2,2,3,7,4,7},
		{1,4,7,4,4,2,2,1,4,1},
		{2,7,3,1,5,3,2,1,3,5}
		};					 
		static byte[][]puzzle0036 = new byte[][]{
			{4,1,7,4,2,2,7,4,7,7},
			{3,4,3,4,1,4,2,5,2,3},
			{5,7,1,5,4,3,5,1,7,2},
			{7,7,7,4,1,2,2,1,5,2},
			{5,5,1,5,4,1,7,2,3,2},
			{4,4,4,2,3,5,5,2,5,2},
			{1,7,2,1,5,7,7,2,1,4},
			{1,1,2,4,5,1,7,7,3,4},
			{5,3,3,2,5,7,7,1,3,3},
			{3,4,2,2,5,5,1,1,7,1}
			};					 
			static byte[][]puzzle0037 = new byte[][]{
				{3,2,2,4,7,1,2,3,2,7},
				{1,7,3,5,7,3,7,1,6,4},
				{5,3,2,4,1,6,1,1,5,4},
				{4,6,4,4,3,1,3,1,6,5},
				{3,7,1,6,6,3,3,3,2,7},
				{3,2,1,4,2,6,6,6,6,1},
				{1,6,2,5,1,1,2,5,1,4},
				{2,2,6,3,3,3,2,2,5,1},
				{1,2,3,3,2,6,3,3,4,5},
				{7,7,5,6,3,5,2,5,4,6}
				};					 
				static byte[][]puzzle0038 = new byte[][]{
					{5,7,1,4,7,3,3,5,7,7},
					{7,4,1,1,5,5,7,1,1,5},
					{5,4,4,2,6,7,7,5,1,4},
					{6,4,1,6,2,3,4,6,7,7},
					{5,6,1,4,6,6,6,6,7,2},
					{4,1,1,3,3,3,4,6,4,3},
					{4,1,6,2,6,1,4,3,3,7},
					{3,5,2,4,1,1,7,3,4,5},
					{2,5,1,1,3,6,6,7,2,6},
					{2,1,5,5,6,1,3,2,1,4}
					};					 
					static byte[][]puzzle0039 = new byte[][]{
						{7,6,7,8,7,4,2,1,1,1},
						{2,5,2,2,5,3,4,1,4,8},
						{5,3,8,5,3,1,6,3,4,5},
						{6,1,1,8,5,5,1,4,8,2},
						{6,2,2,3,5,5,3,2,3,1},
						{1,5,3,2,2,6,8,2,4,1},
						{3,4,6,6,5,2,4,6,7,1},
						{8,7,1,7,4,3,4,6,3,1},
						{4,2,7,5,8,8,8,7,7,7},
						{1,1,8,3,8,5,7,3,2,1}
						};					 
static byte[][]puzzle0040 = new byte[][]{
	{5,4,3,8,6,8,7,5,1,4},
	{8,3,8,1,1,3,5,8,7,4},
	{6,1,5,8,5,7,4,4,8,4},
	{3,3,1,3,3,4,6,1,8,8},
	{1,8,8,3,3,2,7,5,1,3},
	{3,7,2,6,7,3,2,2,3,6},
	{6,2,5,1,7,2,6,7,7,5},
	{6,1,7,5,8,3,5,2,2,1},
	{1,5,1,8,7,5,6,2,5,5},
	{4,7,5,1,8,3,2,5,6,7}
	};					 
	static byte[][]puzzle0041 = new byte[][]{
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{}
		};					 
		static byte[][]puzzle0042 = new byte[][]{
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{}
			};					 
			static byte[][]puzzle0043 = new byte[][]{
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{}
				};					 
				static byte[][]puzzle0044 = new byte[][]{
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{}
					};					 
					static byte[][]puzzle0045 = new byte[][]{
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{}
						};					 
						static byte[][]puzzle0046 = new byte[][]{
							{},
							{},
							{},
							{},
							{},
							{},
							{},
							{},
							{},
							{}
							};					 
							static byte[][]puzzle0047 = new byte[][]{
								{},
								{},
								{},
								{},
								{},
								{},
								{},
								{},
								{},
								{}
								};					 
								static byte[][]puzzle0048 = new byte[][]{
									{},
									{},
									{},
									{},
									{},
									{},
									{},
									{},
									{},
									{}
									};					 
									static byte[][]puzzle0049 = new byte[][]{
										{},
										{},
										{},
										{},
										{},
										{},
										{},
										{},
										{},
										{}
										};					 
										static byte[][]puzzle0050 = new byte[][]{
											{},
											{},
											{},
											{},
											{},
											{},
											{},
											{},
											{},
											{}
											};					 
											static byte[][]puzzle0051 = new byte[][]{
												{},
												{},
												{},
												{},
												{},
												{},
												{},
												{},
												{},
												{}
												};					 
												static byte[][]puzzle0052 = new byte[][]{
													{},
													{},
													{},
													{},
													{},
													{},
													{},
													{},
													{},
													{}
													};					 
static byte[][]puzzle0053 = new byte[][]{
	{},
	{},
	{},
	{},
	{},
	{},
	{},
	{},
	{},
	{}
	};					 
	static byte[][]puzzle0054 = new byte[][]{
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{}
		};					 
		static byte[][]puzzle0055 = new byte[][]{
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{}
			};					 
			static byte[][]puzzle0056 = new byte[][]{
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{}
				};					 
				static byte[][]puzzle0057 = new byte[][]{
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{}
					};					 
					static byte[][]puzzle0058 = new byte[][]{
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{}
						};					 
static byte[][]puzzle0059 = new byte[][]{
	{},
	{},
	{},
	{},
	{},
	{},
	{},
	{},
	{},
	{}
	};					 
	static byte[][]puzzle0060 = new byte[][]{
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{},
		{}
		};					 
		static byte[][]puzzle0061 = new byte[][]{
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{},
			{}
			};					 
			static byte[][]puzzle0062 = new byte[][]{
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{}
				};					 
				static byte[][]puzzle0063 = new byte[][]{
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{},
					{}
					};					 
					static byte[][]puzzle0064 = new byte[][]{
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{},
						{}
						};					 


			static byte[][]puzzle0000 = new byte[][]{
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{},
				{}
				};					 
	public static void main(String[] args) {
		//testStateFallStones();
		//testOneIter();
		//testSolver();
		solver();
	}
	public static void testStateFallStones(){
		System.out.println(new BubbleState(testCaseX));
		System.out.println(new BubbleState(testCaseY));
		System.out.println(new BubbleState(testCaseZ));

	}
	public static void testOneIter(){
		BubbleState s = new BubbleState(puzzle0031);
		System.out.println("START");
		System.out.println(s);
		System.out.println("----------------------------------------------------------------------------------------------------");
		int i=1;
		for (BubbleState s2 : s.getSubsequentStates()){
			System.out.println("Alternative "+ i++);
			System.out.println(s2);
			System.out.println("----------------------------------------------------------------------------------------------------");
		}
	}
	
	public static void testSolver(){
		BubbleState s = new BubbleState(testCaseY3);
		System.out.println("START");
		System.out.println(s);
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("Solution");
		long l = System.currentTimeMillis();
		//System.out.println(Solver.getOptimalBruteForce(s));
		System.out.println(BubbleSolver.getOptimalHeuristic(s));
		System.out.println(( System.currentTimeMillis()-l)+"ms");
				
	}
	public static void solver(){
		BubbleState s = new BubbleState(puzzle0003);		
		System.out.println("START");
		System.out.println(s);
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("Solution");
		System.out.println(BubbleSolver.getOptimalHeuristic(s, true));
				
	}
}