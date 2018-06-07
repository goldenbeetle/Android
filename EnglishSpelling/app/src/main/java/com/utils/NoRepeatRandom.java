package com.utils;

public class NoRepeatRandom 
{
	private int[] number = null;
	  private int N = -1;
	  private int size = 0;
	  public NoRepeatRandom(int minVal, int maxVal)
	  {
	    N = (maxVal - minVal) + 1;
	    number = new int[N];
	    int n = minVal;
	    for(int i = 0; i < N; i++)
	      number[i] = n++;
	    size = N;
	  }

	  public void Reset() { size = N; }

	  // Returns -1 if none left
	  public int GetRandom()
	  {
	    if(size <= 0) return -1;
	    int index = (int) (size * Math.random());
	    int randNum = number[index];

	    // Swap current value with current last, so we don't actually
	    // have to remove anything, and our list still contains everything
	    // if we want to reset
	    number[index] = number[size-1];
	    number[--size] = randNum;

	    return randNum;
	  }

}
