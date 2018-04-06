package main;

public class Calculator
{
	public static final int numSides = 6;
	public static final int numDice = 4;
	
	public static void main(String[] args)
	{
		//First, find all the ways of rolling one dice		
		Integer[][] originalRolls = new Integer[1][numSides];
		for (int i=0; i<numSides; i++)
		{
			originalRolls[0][i] = i+1;
		}
		
		//For each additional dice, roll each number for each result
		Integer[][] rolls = new
		
	}
}
