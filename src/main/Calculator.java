package main;

public class Calculator
{
	public static final int numSides = 6;
	public static final int numDice = 2;
	
	public static void main(String[] args)
	{
		//First, find all the ways of rolling one dice		
		Integer[][] originalRolls = new Integer[numSides][1];
		for (int i=0; i<numSides; i++)
		{
			originalRolls[i][0] = i+1;
		}
		
		//For each additional dice, roll each number for each result
		Integer[][] rolls = new Integer[1][1];
		for (int i=1; i<numDice; i++)
		{
			int numPossibilities = originalRolls.length * numSides;
			
			rolls = new Integer[numPossibilities][i+1];
			
			for (int j=0; j<originalRolls.length; j++)
			{
				for (int k=0; k<numSides; k++)
				{
					int index = j * originalRolls.length + k;
					
					for (int l=0; l<i; l++)
					{
						rolls[index][l] = originalRolls[j][l];
					}
					rolls[index][i] = k;					
				}
			}
			
			originalRolls = rolls;
		}
		
		for (int i=0; i<rolls.length; i++)
		{
			
		}
		
	}
}
