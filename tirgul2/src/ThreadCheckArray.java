import java.util.ArrayList;

public class ThreadCheckArray implements Runnable 
{
	private boolean flag;
	private boolean [] winArray;
	SharedData sd;
	ArrayList<Integer> array;
	int b;
	
	public ThreadCheckArray(SharedData sd) 
	{
		this.sd = sd;	
		synchronized (sd) 
		{
			array = new ArrayList<>(sd.getArray());
			b = sd.getB();
		}		
		winArray = new boolean[array.size()];
	}
	
	void rec(int n, int b)
	{
		synchronized (sd) 
		{
			if (sd.getFlag())
				return;
		}	
		if (n == 1)
		{
			if(b == 0 || b == array.get(n-1))
			{
				flag = true;
				synchronized (sd) 
				{
					sd.setFlag(true);
				}			
			}
			if (b == array.get(n-1))
				winArray[n-1] = true;
			return;
		}
		
		rec(n-1, b - array.get(n-1));
		if (flag)
			winArray[n-1] = true;
		synchronized (sd) 
		{
			if (sd.getFlag())
				return;
		}	
		rec(n-1, b);
	}
	public void run() 
    {
        int arraySize = array.size();
        
        if (arraySize == 1) 
        {
            if (b == array.get(0) && !flag)
            {
                winArray[0] = true;
                flag = true;
                synchronized (sd) 
                {
                    sd.setFlag(true);
                }
            }
        } 
        else 
        {
            if (Thread.currentThread().getName().equals("thread1"))
                rec(arraySize - 1, b - array.get(arraySize - 1));
            else 
                rec(arraySize - 1, b);
        }
        
        if (flag)
        {
            if (Thread.currentThread().getName().equals("thread1"))
                winArray[arraySize - 1] = true;
            
            synchronized (sd) 
            {
                sd.setWinArray(winArray);
            }    
        }
    }


}
