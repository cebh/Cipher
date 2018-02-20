import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class SubCipher {
	public static void main(String[]args)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter encrypted code:");
		String cipher = in.nextLine();
		int[] frequencies = new int[26];
		int count = 0;
		for(Character c : cipher.toCharArray())
		{
			int numValue = (int)Character.toLowerCase(c);
			//System.out.println(numValue);
			if(numValue >= 97 && numValue <= 122)
			{
				frequencies[numValue-97] = frequencies[numValue-97] + 1;
			}
		}
		for(int i = 0; i < frequencies.length; i++)
		{
			System.out.println((char)(i + 97) + ":" + frequencies[i]);
		}
		HashSet<Integer> freqNums = new HashSet<Integer>();
		for(int i = 0; i < frequencies.length; i++)
		{
			freqNums.add(frequencies[i]);
		}
		Integer[] freqNumsArray = freqNums.toArray(new Integer[freqNums.size()]);
		
		Arrays.sort(freqNumsArray);
		System.out.println("------------------------------");
		for(int i = freqNumsArray.length - 1; i > 0; i--)
		{
			System.out.print(freqNumsArray[i] + ":");
			for(int j = 0; j < frequencies.length; j++)
			{
				if(frequencies[j] == freqNumsArray[i])
				{
					count++;
					System.out.print((char)(j + 97));
				}
			}
			System.out.println();
		}
		System.out.println("Letters used:" + count);
//		HashMap<Character,Integer> frequency = new HashMap<Character,Integer>();
//		for(Character c : cipher.toCharArray())
//		{
//			if(frequency.containsKey(c))
//			{
//				frequency.put(c, frequency.get(c) + 1);
//			} else {
//				frequency.put(c,1);
//			}
//		}
//		for(Character c : frequency.keySet()) 
//		{
//			System.out.println(c + ":" + frequency.get(c));
//		}
//		HashSet<Integer> nums = new HashSet<Integer>();
//		for(Character c : frequency.keySet()) 
//		{
//			nums.add(frequency.get(c));
//		}
//		for(Integer num : nums)
//		{
//			System.out.print(num + ": ");
//			for(Character c : frequency.keySet())
//			{
//				if(frequency.get(c) == num)
//				{
//					System.out.print(c + " ");
//				}
//			}
//			System.out.println();
//		}
		
		
	}
}
