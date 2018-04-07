package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class Calculator
{
	public static final int numSides = 6;
	
	public static final int numDiceRoll = 4;
	public static final int numDiceTotal = 3;
	
	public static final int numAbilities = 6;
	
	public static final int numRanksToPrint = 100;
	
	public static void main(String[] args)
	{
		Integer[][] rolls = generatePossibilities(1, numSides, numDiceRoll);
		ArrayList<Integer> scores = generateScoresFromRolls(rolls);
		
		HashMap<Integer, Long> scoreCounts = generateCountMap(scores);
		
		int maxScore = numDiceTotal * numSides;
		Integer[][] scoreSets = generatePossibilities(numDiceTotal, maxScore, numAbilities);
		ArrayList<AbilityScoreSet> abilitySets = generateAbilityScoreSets(scoreSets);
		
		HashMap<AbilityScoreSet, Long> abilityScoreSetCounts = generateCountMap(abilitySets);
		
		HashMap<AbilityScoreSet, Long> abilityScoreSetPossibilityCounts = calculateAbilityScoreSetCombinations(abilityScoreSetCounts, scoreCounts);
		HashMap<Long, Vector<AbilityScoreSet>> countToAbilityScoreSets = inverseHashMap(abilityScoreSetPossibilityCounts);

		Set<Long> possibilities = countToAbilityScoreSets.keySet();
		ArrayList<Long> counts = new ArrayList<Long>(possibilities);
		Collections.sort(counts, Collections.reverseOrder());
		
		//Now we have everything we need we can display the ranks
		long totalPossibilities = (long)Math.pow(numSides, numDiceRoll * numAbilities);
		long totalPossibilitiesAccounted = 0;
		
		long rank = 1;
		for (int i=0; i<counts.size(); i++)
		{
			long numPossibilities = counts.get(i);
			Vector<AbilityScoreSet> abilityScoreSets = countToAbilityScoreSets.get(numPossibilities);
			
			double percentage = 100.0d * (double)numPossibilities / (double)totalPossibilities;
			
			for (int j=0; j<abilityScoreSets.size(); j++)
			{
				AbilityScoreSet abilityScoreSet = abilityScoreSets.get(j);
				
				if (rank <= numRanksToPrint)
				{
					System.out.println(rank + "	" + abilityScoreSet + "	" + numPossibilities + "	" + percentage + "%");
				}
				
				totalPossibilitiesAccounted += numPossibilities;
				
				rank++;
			}
		}
		
		System.out.println("There are a total of " + totalPossibilities + " possibilities");
		System.out.println("Accounted for " + totalPossibilitiesAccounted + " possibilities");
		
		Integer[] searchSet = new Integer[numAbilities];
		for (int i=0; i<numAbilities; i++)
		{
			searchSet[i] = maxScore;
		}
		
		AbilityScoreSet searchAbilityScoreSet = new AbilityScoreSet(searchSet);
		Long numberSearched = abilityScoreSetPossibilityCounts.get(searchAbilityScoreSet);
		double percentage = 100.0d * (double)numberSearched / (double)totalPossibilities;
		System.out.println("There are " + numberSearched + " (" + percentage + "%) ways of achieving " + searchAbilityScoreSet);
	}

	/**
	 * Enumerate through the possible results of rolling an ability scores
	 * 
	 * @return A HashMap containing the possible results mapped to the number of ways of achieving that result
	 */
	private static Integer[][] generatePossibilities(int minValue, int maxValue, int numValues)
	{
		maxValue++;
		int numNumbers = maxValue - minValue;
		int numPossibilities = numNumbers;
		
		//First, find all the ways of rolling one dice		
		Integer[][] originalNumbers = new Integer[numPossibilities][1];
		for (int i=0; i<numPossibilities; i++)
		{
			originalNumbers[i][0] = minValue + i;
		}
		
		//For each additional dice, roll each number for each result
		Integer[][] rolls = originalNumbers;
		for (int i=1; i<numValues; i++)
		{
			int originalPossibilities = originalNumbers.length;
			numPossibilities = originalPossibilities * numNumbers;
			
			rolls = new Integer[numPossibilities][i+1];			
			
			for (int j=0; j<originalPossibilities; j++)
			{
				for (int k=0; k<numNumbers; k++)
				{
					int index = j * numNumbers + k;
					
					for (int l=0; l<i; l++)
					{
						rolls[index][l] = originalNumbers[j][l];
					}
					rolls[index][i] = minValue + k;
				}
			}
			
			originalNumbers = rolls;
		}
		
		System.out.println("Generated " + rolls.length + " rolls");
		
		return rolls;
	}
	
	private static ArrayList<Integer> generateScoresFromRolls(Integer[][] rollPossibilities)
	{
		int numPossibilities = rollPossibilities.length;
		
		ArrayList<Integer> scorePossibilities = new ArrayList<Integer>(numPossibilities);

		for (int i=0; i<numPossibilities; i++)
		{
			Integer[] abilityRolls = rollPossibilities[i];
			Arrays.sort(abilityRolls, Collections.reverseOrder());
			
			int total = 0;
			for (int j=0; j<numDiceTotal; j++)
			{
				total += abilityRolls[j];
			}
			
			scorePossibilities.add(total);
		}
		
		return scorePossibilities;	
	}
	
	private static ArrayList<AbilityScoreSet> generateAbilityScoreSets(Integer[][] abilitySetPossibilities)
	{
		int numPossibilities = abilitySetPossibilities.length;
		
		ArrayList<AbilityScoreSet> abilityScoreSets = new ArrayList<AbilityScoreSet>(numPossibilities);
		for (int i=0; i<numPossibilities; i++)
		{
			Integer[] abilitySet = abilitySetPossibilities[i];
			AbilityScoreSet abilityScoreSet = new AbilityScoreSet(abilitySet);
			
			abilityScoreSets.add(abilityScoreSet);
		}
		
		return abilityScoreSets;
	}
	
	/**
	 * 
	 * @param scorePossibilities
	 * @return
	 */
	private static <E> HashMap<E, Long> generateCountMap(ArrayList<E> arrayList) 
	{
		System.out.println("Starting with " + arrayList.size() + " total possibilities");
		
		HashMap<E, Long> countMap = new HashMap<E, Long>();

		for (int i=0; i<arrayList.size(); i++)
		{
			E e = arrayList.get(i);
			
			long count = countMap.getOrDefault(e, (long) 0);
			count++;
			countMap.put(e, count);
		}
		
		System.out.println("There are " + countMap.size() + " distinct possibilities");
		
		return countMap;
	}
	
	/**
	 * Iterate through all of the ability score sets and group similar ones, then calculate how many ways each of these sets can occur from the ways that each score can be obtained
	 */
	private static HashMap<AbilityScoreSet, Long> calculateAbilityScoreSetCombinations(HashMap<AbilityScoreSet, Long> abilityScoreSetMap, HashMap<Integer, Long> scorePossibilitiesMap)
	{
		HashMap<AbilityScoreSet, Long> abilityScoreSetToPossibilitiesMap = new HashMap<AbilityScoreSet, Long>();
		
		for (Iterator<HashMap.Entry<AbilityScoreSet, Long>> i = abilityScoreSetMap.entrySet().iterator(); i.hasNext(); )
		{
			Entry<AbilityScoreSet, Long> entry = i.next();
			
			AbilityScoreSet abilityScoreSet = entry.getKey();
			Integer[] abilityScores = abilityScoreSet.abilityScores;
			
			//This is the number of ways this result can be achieved (from order of scores)
			long totalWays = entry.getValue();
			
			//For each score, multiply by the number of ways that score can be achieved with the given dice
			for (int j=0; j<abilityScores.length; j++)
			{
				Integer ability = abilityScores[j];
				long numWays = scorePossibilitiesMap.get(ability);

				totalWays *= numWays;
			}
			
			abilityScoreSetToPossibilitiesMap.put(abilityScoreSet, totalWays);
		}
		
		return abilityScoreSetToPossibilitiesMap;
	}
	
	private static <V, K> HashMap<V, Vector<K>> inverseHashMap(HashMap<K, V> map)
	{
		HashMap<V, Vector<K>> inverseMap = new HashMap<V, Vector<K>>();
		
		for (Iterator<Entry<K, V>> i = map.entrySet().iterator(); i.hasNext(); )
		{
			Entry<K, V> entry = i.next();
			K key = entry.getKey();
			V value = entry.getValue();
			
			Vector<K> inverseValue = inverseMap.get(value);
			if (inverseValue == null)
			{
				inverseValue = new Vector<K>();
				inverseMap.put(value, inverseValue);
			}
			inverseValue.add(key);
		}
		
		return inverseMap;
	}
}
