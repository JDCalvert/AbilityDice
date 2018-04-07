package main;

import java.util.Arrays;
import java.util.Collections;

public class AbilityScoreSet
{
	public Integer[] abilityScores = null;
	
	public AbilityScoreSet(Integer[] abilityScores)
	{
		this.abilityScores = abilityScores;
		
		Arrays.sort(abilityScores, Collections.reverseOrder());
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof AbilityScoreSet))
		{
			return false;
		}
		
		AbilityScoreSet otherSet = (AbilityScoreSet)other;
		Integer[] otherAbilityScores = otherSet.abilityScores;
		
		int numAbilityScores = abilityScores.length;
		if (otherAbilityScores.length != numAbilityScores)
		{
			return false;
		}
		
		for (int i=0; i<numAbilityScores; i++)
		{
			if (abilityScores[i] != otherAbilityScores[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int hashcode = 1;
		for (int i=0; i<abilityScores.length; i++)
		{
			hashcode = 37 * hashcode + abilityScores[i];
		}
		return hashcode;
	}
	
	public int size()
	{
		return abilityScores.length;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<abilityScores.length; i++)
		{
			int score = abilityScores[i];
			
			if (i>0) sb.append(",");
			if (score < 10) sb.append(" ");
			sb.append(score);			
		}
		
		return sb.toString();
	}
}
