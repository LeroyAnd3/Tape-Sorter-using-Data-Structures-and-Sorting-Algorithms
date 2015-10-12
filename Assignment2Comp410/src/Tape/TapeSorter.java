package Tape;
/**Leroy Anderson, III
 * PID: 720430081*/
/**
 * Represents a machine with limited memory that can sort tape drives.
 */
public class TapeSorter {

    private int memorySize;
    private  int tapeSize;
    public int[] memory;
    int amountLeft;
    int timesPassedNormally;
    boolean leftOvers;
    int remains;
   
  

    public TapeSorter(int memorySize, int tapeSize) {
        this.memorySize = memorySize;
        this.tapeSize = tapeSize;
        this.memory = new int[memorySize];
        amountLeft=memorySize;
        timesPassedNormally =0;
        remains = 0;
        leftOvers=true;
    }

    /**
     * Sorts the first `size` items in memory via quicksort
     */
    public void quicksort(int size) {/**got help on how to do quicksort from the following site http://tekmarathon.com/2013/09/17/understanding-quicksort-algorithm/**/
        // TODO: Implement me for 10 points
    	quicksorthelper(memory, 0, size-1);
    	for (int i = 0; i < memorySize; i++){
    		//System.out.println("Final: "+memory[i]);
    	}
    }
    
    public void quicksorthelper(int[] arr, int leftpos, int rightpos){
    	
    	int index = partition(arr, leftpos, rightpos);
    	
    	if(leftpos < index-1)
    		quicksorthelper(arr,leftpos,index-1);
    	
    	if(rightpos>index)
    		quicksorthelper(arr, index, rightpos);
    }
    
   public int partition(int[] arr, int left, int right){
	    int pivot = arr[left];
	    while(left <= right) {
	        while(arr[left]<pivot)
	            left++;
	        
	        while(arr[right]>pivot)
	            right--;
	        
	        if(left <= right) {
	            int tmp = arr[left];
	            arr[left] = arr[right];
	            arr[right] = tmp;
	            left++;
	            right--;
	        }
	    }   
	    return left;
   }
  
    /**
     * Reads in numbers from drive `in` into memory (a chunk), sorts it, then writes it out to a different drive.
     * It writes chunks alternatively to drives `out1` and `out2`.
     *
     * If there are not enough numbers left on drive `in` to fill memory, then it should read numbers until the end of
     * the drive is reached.
     *
     * Example 1: Tape size = 8, memory size = 2
     * ------------------------------------------
     *   BEFORE:
     * in: 4 7 8 6 1 3 5 7
     *
     *   AFTER:
     * out1: 4 7 1 3 _ _ _ _
     * out2: 6 8 5 7 _ _ _ _
     *
     *
     * Example 2: Tape size = 10, memory size = 3
     * ------------------------------------------
     *   BEFORE:
     * in: 6 3 8 9 3 1 0 7 3 5
     *
     *   AFTER:
     * out1: 3 6 8 0 3 7 _ _ _ _
     * out2: 1 3 9 5 _ _ _ _ _ _
     *
     *
     * Example 3: Tape size = 13, memory size = 4
     * ------------------------------------------
     *   BEFORE:
     * in: 6 3 8 9 3 1 0 7 3 5 9 2 4
     *
     *   AFTER:
     * out1: 3 6 8 9 2 3 5 9 _ _ _ _ _
     * out2: 0 1 3 7 4 _ _ _ _ _ _ _ _
     */
    public void initialPass(TapeDrive in, TapeDrive out1, TapeDrive out2) {
        // TODO: Implement me for 15 points!
    	
    	int fullQuickSorts = tapeSize/memorySize;
    	int remainingSize = tapeSize%memorySize;
    	timesPassedNormally=fullQuickSorts;
    	remains = remainingSize;
    	
    	if(fullQuickSorts%2==0){//modulo is 0
    		for(int i = 0; i< fullQuickSorts/2; i++){
        		this.storeInMemory(in);
        		this.quicksort(memorySize);         		
        		for(int j=0;j<memorySize;j++)
        			out1.write(memory[j]);   	
        		this.storeInMemory(in);
        		this.quicksort(memorySize);        	
        		for(int k=0; k< memorySize;k++)
        			out2.write(memory[k]);
        	}
    		for(int i=0;i<1;i++){
    			this.storeInMemory(in);
        		this.quicksort(remainingSize);         		
        		for(int j=0;j<remainingSize;j++)
        			out1.write(memory[j]);
    		}
    		leftOvers = false;
    	} else{//what to do if modulo isn't 0
    		for(int i = 0; i< fullQuickSorts/2; i++){
        		this.storeInMemory(in);
        		this.quicksort(memorySize);         		
        		for(int j=0;j<memorySize;j++)
        			out1.write(memory[j]);   	
        		this.storeInMemory(in);
        		this.quicksort(memorySize);        	
        		for(int k=0; k< memorySize;k++)
        			out2.write(memory[k]);
        	}
    		for(int i = 0; i< 1; i++){
        		this.storeInMemory(in);
        		this.quicksort(memorySize);         		
        		for(int j=0;j<memorySize;j++)
        			out1.write(memory[j]);   	
        	}
    		for(int i=0;i<1;i++){
    			this.storeInMemory(in);
        		this.quicksort(remainingSize);         		
        		for(int j=0;j<remainingSize;j++)
        			out2.write(memory[j]);
    		}
    		leftOvers = true;
    	}
    	
        in.reset();
  
    

    


    }

    /**
     * Merges the first chunk on drives `in1` and `in2` and writes the sorted, merged data to drive `out`.
     * The size of the chunk on drive `in1` is `size1`.
     * The size of the chunk on drive `in2` is `size2`.
     *
     *          Example
     *       =============
     *
     *  (BEFORE)
     * in1:  [ ... 1 3 6 8 9 ... ]
     *             ^
     * in2:  [ ... 2 4 5 7 8 ... ]
     *             ^
     * out:  [ ... _ _ _ _ _ ... ]
     *             ^
     * size1: 4, size2: 4
     *
     *   (AFTER)
     * in1:  [ ... 1 3 6 8 9 ... ]
     *                     ^
     * in2:  [ ... 2 4 5 7 8 ... ]
     *                     ^
     * out:  [ ... 1 2 3 4 5 6 7 8 _ _ _ ... ]
     *                             ^
     */
    public void mergeChunks(TapeDrive in1, TapeDrive in2, TapeDrive out, int size1, int size2) {
        // TODO: Implement me for 10 points


    	int in1Value=in1.read();
    	int in2Value=in2.read();
    	int in1Mem = size1;
    	int in2Mem = size2;


    	while(in1Mem>0 && in2Mem>0){    				
    		if(in1Value < in2Value){
    			out.write(in1Value);
    			in1Mem--;
    			if(in1Mem!=0)
    			in1Value=in1.read();
    			
    		} else if(in2Value < in1Value){
    			out.write(in2Value);
    			in2Mem--;
    			if(in2Mem!=0)
    	    	in2Value=in2.read();
    			
    		} else if(in2Value == in1Value){
    			out.write(in1Value);
    			out.write(in2Value);
    			in1Mem--;
    	    	in2Mem--;
    	    	if(in1Mem!=0)
    	    	in1Value=in1.read();
    	 
    			if(in2Mem!=0)
        	    	in2Value=in2.read();
    			
    		}
    	}	
    	
    	if(in1Mem>0){
    		for(int i=0; i<in1Mem;i++){
    			out.write(in1Value);
    			in1Mem--;
    			if(in1Mem!=0)
    			in1Value = in1.read();
    			
    		}
    	} else if (in2Mem>0){
    		for(int i=0; i<in2Mem;i++){
    			out.write(in2Value);
    			in2Mem--;
    			if(in2Mem!=0)
    			in2Value = in2.read();
    			
    		}
    	}
    	
    	while(in1Mem>0){
			out.write(in1Value);
			in1Mem--;
			if(in1Mem!=0)
			in1Value = in1.read();
			
    	}
    	while(in2Mem>0){
			out.write(in2Value);
			in2Mem--;
			if(in2Mem!=0)
			in2Value = in2.read();
			
    	}
 
    }

    /**
     * Merges chunks from drives `in1` and `in2` and writes the resulting merged chunks alternatively to drives `out1`
     * and `out2`.
     *
     * The `runNumber` argument denotes which run this is, where 0 is the first run.
     *
     * -- Math Help --
     * The chunk size on each drive prior to merging will be: memorySize * (2 ^ runNumber)
     * The number of full chunks on each drive is: floor(tapeSize / (chunk size * 2))
     *   Note: If the number of full chunks is 0, that means that there is a full chunk on drive `in1` and a partial
     *   chunk on drive `in2`.
     * The number of leftovers is: tapeSize - 2 * chunk size * number of full chunks
     *
     * To help you better understand what should be happening, here are some examples of corner cases (chunks are
     * denoted within curly braces {}):
     *
     * -- Even number of chunks --  check
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 } { 3 5 6 9 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 3 5 5 6 7 8 9 9 }
     *
     * -- Odd number of chunks --  check
     * in1 ->   { 1 3 5 } { 6 7 9 } { 3 4 8 }
     * in2 ->   { 2 4 6 } { 2 7 8 } { 0 3 9 }
     * out1 ->  { 1 2 3 4 5 6 } { 0 3 3 4 8 9 }
     * out2 ->  { 2 6 7 7 8 9 }
     *
     * -- Number of leftovers <= the chunk size -- check  
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 5 7 8 9 }
     *
     * -- Number of leftovers > the chunk size -- check
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 } { 3 5 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 3 5 5 7 8 9 }
     *
     * -- Number of chunks is 0 -- check
     * in1 ->   { 2 4 5 8 9 }
     * in2 ->   { 1 5 7 }
     * out1 ->  { 1 2 4 5 5 7 8 9 }
     * out2 ->
     */
    public void doRun(TapeDrive in1, TapeDrive in2, TapeDrive out1, TapeDrive out2, int runNumber) {
        // TODO: Implement me for 15 points
    	//make sure runNumber is correct
    	int chunkSize = memorySize * (int) (Math.pow(2, runNumber));
    	int fullChunks = (int) Math.floor(tapeSize / (chunkSize * 2));
    	int initialFC = fullChunks;
    	int leftOvers =  (tapeSize - (2 * chunkSize * fullChunks));
    	

    		
    		while(fullChunks>0){//covers even and odd chunk numbers
    		if(fullChunks>0){
    		mergeChunks(in1,in2,out1,chunkSize,chunkSize);
    		fullChunks--;
    		}
    		
    		if(fullChunks>0){
    		mergeChunks(in1,in2,out2,chunkSize,chunkSize);
    		fullChunks--;
    		}
    		}
    		
    		if(initialFC == 0){
    			mergeChunks(in1,in2,out2,chunkSize,(leftOvers%chunkSize));
    		} else if(initialFC != 0 && leftOvers <= chunkSize && leftOvers != 0 && initialFC%2==0){
    			mergeChunks(in1,in2,out1,leftOvers,0);
    		}else if(initialFC != 0 && leftOvers <= chunkSize && leftOvers != 0 && initialFC%2 !=0){
    			mergeChunks(in1,in2,out2,leftOvers,0);
    		} else if(initialFC !=0 && leftOvers > chunkSize && (int) Math.floor(tapeSize / (chunkSize * 2)) >= 1 && leftOvers != 0 && initialFC%2==0 ){
    			mergeChunks(in1,in2,out1,chunkSize,(leftOvers%chunkSize));
    		}else if(initialFC !=0 && leftOvers > chunkSize && (int) Math.floor(tapeSize / (chunkSize * 2)) >= 1 && leftOvers != 0&& initialFC%2!=0){
    			mergeChunks(in1,in2,out2,chunkSize,(leftOvers%chunkSize));
    		}

 
        
    	if(initialFC != 0){
    		runNumber++;
    		in1.reset();
    		in2.reset();
    		out1.reset();
    		out2.reset();
    		doRun(out1,out2,in1,in2,runNumber);
    	}
    		
    }

    /**
     * Sorts the data on drive `t1` using the external sort algorithm. The sorted data should end up on drive `t1`.
     *
     * Initially, drive `t1` is filled to capacity with unsorted numbers.
     * Drives `t2`, `t3`, and `t4` are empty and are to be used in the sorting process.
     */
    public void sort(TapeDrive t1, TapeDrive t2, TapeDrive t3, TapeDrive t4) {
        // TODO: Implement me for 15 points
    	t1.reset(); t2.reset(); t3.reset(); t4.reset();
    	this.initialPass(t1, t3, t4);
    	t1.reset(); t2.reset(); t3.reset(); t4.reset();
    	this.doRun(t3,t4,t1,t2,0);
        t1.reset(); t2.reset(); t3.reset(); t4.reset();
        
        int last = Integer.MIN_VALUE;
        boolean sorted = true;
        for (int i = 0; i < tapeSize; i++) {
            int val = t1.read();
            sorted &= last <= val;
            last = val;
        }
        if (sorted);
        


        int last2 = Integer.MIN_VALUE;
        boolean sorted2 = true;
        for (int i = 0; i < tapeSize; i++) {
            int val = t2.read();
            sorted2 &= last2 <= val;
            last2 = val;
        }
        if (sorted2){
        	t1.reset(); t2.reset();
            //System.out.println("Sorted!");
            for(int i=0; i<tapeSize; i++)
            t1.write(t2.read());	
        }
            
        
        int last3 = Integer.MIN_VALUE;
        boolean sorted3 = true;
        for (int i = 0; i < tapeSize; i++) {
            int val = t3.read();
            sorted3 &= last3 <= val;
            last3 = val;
        }
        if (sorted3){
        	t1.reset(); t3.reset();
            //System.out.println("Sorted!");
            for(int i=0; i<tapeSize; i++)
            t1.write(t3.read());
        }
        
        int last4 = Integer.MIN_VALUE;
        boolean sorted4 = true;
        for (int i = 0; i < tapeSize; i++) {
            int val = t4.read();
            sorted4 &= last4 <= val;
            last4 = val;
        }
        if (sorted4){
        	t1.reset(); t4.reset();
            //System.out.println("Sorted!");
            for(int i=0; i<tapeSize; i++)
            t1.write(t4.read());
        } 
        t1.reset(); t2.reset(); t3.reset(); t4.reset();
        
    }
    public void storeInMemory(TapeDrive t1){
    	for (int i = 0; i < memorySize; i++){
    		memory[i]= t1.read();    		
    	}
    }

    public static void main(String[] args) {
 
    	for(int j = 1; j<=80;j++){
        TapeSorter tapeSorter = new TapeSorter(j, 120);
        TapeDrive t1 = TapeDrive.generateRandomTape(120);
        TapeDrive t2 = new TapeDrive(120);
        TapeDrive t3 = new TapeDrive(120);
        TapeDrive t4 = new TapeDrive(120);
		
        tapeSorter.sort(t1, t2, t3, t4);
        t1.reset(); t2.reset(); t3.reset(); t4.reset();
        
        int last = Integer.MIN_VALUE;
        boolean sorted = true;
        for (int i = 0; i < 80; i++) {
            int val = t1.read();
            sorted &= last <= val;
            last = val;
        }
        if (sorted)
            System.out.println("Sorted!");
        else
            System.out.println("Not sorted!");
    	}
       



    }

}

