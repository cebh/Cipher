import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class AssignmentTwo {
	public static int times = 0;
	public static HashSet<String> words = new HashSet<String>();
	public static int longestLength = 100;
	private static int counter = 0;
	public static HashMap<String, Double> monograms;
	public static HashMap<String, Double> bigrams;
	public static HashMap<String, Double> trigrams;
	public static HashMap<String, Double> quadgrams;
	public static HashMap<String, Double> quintgrams;

	public static double monoFloor = 0;
	public static double biFloor = 0;
	public static double triFloor = 0;
	public static double quadFloor = 0;


	public static void main(String[]args) throws FileNotFoundException
	{
		//get user input
		Scanner in = new Scanner(System.in);
		System.out.println("Enter encrypted code:");
		String cipher = in.nextLine();
		//attemt ceaser cipher
		if(!ceaserCipher(cipher))
		{
			//if ceaser cipher doesn't work, try sub cipher
			substitutionCipher(cipher);
		}
//		HashMap<Character,Character> test1 = new HashMap<Character,Character>();
//		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		for(int i = 0; i < 26; i++)
//		{
//			test1.put(alphabet.charAt(i), alphabet.charAt(i));
//		}
////		test1.put('a', 'b');
////		test1.put('b', 'a');
//		
//		String tester = "BMAERBNDARBSS";
//		System.out.println((decrypt(tester,test1)));
//		System.out.println(checkPhrase(tester,test1));
	}

	public static boolean isValid(String phrase) throws FileNotFoundException
	{
		phrase.replace(" ", "");
		//		if(times >= 10)
		//		{
		//			times = 0;
		//			return true;
		//		}
		//System.out.println(phrase);
		if(phrase.length() == 0)
		{
			return true;
		}
		for(int i = 1; i <= phrase.length(); i++) //changed from phrase.length()
		{
			if(searchWord(phrase.substring(0,i)))//i < 10 && 
			{
				//times++;
				if(isValid(phrase.substring(i)))
				{
					//System.out.println(phrase.substring(i));
					return true;
				}
			}
		}
		return false;
	}

	public static boolean searchWord(String word) throws FileNotFoundException
	{
		if(word.length() == 1)
		{
			if(word.equals("i") || word.equals("a"))
			{
				return true;
			}
			return false;
		}
		if(words.contains(word.toLowerCase()))
		{
			//System.out.println(word);
			return true;
		}
		return false;
	}

	public static void hashWords(String fileName) throws FileNotFoundException
	{
		int longest = 0;
		Scanner file = new Scanner(new File(fileName));
		while(file.hasNextLine())
		{
			String nextLine = file.nextLine();
			if(nextLine.length() > 2)
			{
				words.add(nextLine.toLowerCase());
				if(nextLine.length() > longest)
				{
					longest = nextLine.length();
				}
			}
		}
		Scanner twoFile = new Scanner(new File("src//two_letter_words.txt"));
		while(twoFile.hasNextLine())
		{
			String nextLine = twoFile.nextLine();
			words.add(nextLine.toLowerCase());
		}
		longestLength = longest;
	}

	public static boolean ceaserCipher(String cipher) throws FileNotFoundException
	{
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		HashMap<Character,Character> key = new HashMap<Character,Character>();
		for(int i = 0; i < 26; i++)
		{
			//System.out.println("COMPUTING OFFSET " + (26 - i));
			for(int j = 0; j < 26; j++)
			{
				int offset = i + j;
				if(offset >= 26)
				{
					offset-= 26;
				}
				key.put(alphabet.charAt(j), alphabet.charAt(offset));
			}
			StringBuilder sb = new StringBuilder();
			for(int k = 0; k < cipher.length(); k++)
			{
				int numValue = (int)Character.toLowerCase(cipher.charAt(k));
				//System.out.println(numValue);
				if(numValue >= 97 && numValue <= 122)
				{
					sb.append(key.get(Character.toUpperCase(cipher.charAt(k))));
				}
			}
			//check to see if they are all words
			if(isValid(sb.toString()))
			{
				System.out.println("Offset:" + (26 - i) + "\n" + sb);
				return true;
			}
		}
		return false;
	}

	public static void substitutionCipher(String cipher) throws FileNotFoundException
	{
		hashFitness();
		System.out.println("START");
		cipher = cipher.replace(" ", "").toUpperCase();
		HashMap<Character,Character> bestKey = generateParentKey();
		double bestFitness = measureFitness(decrypt(cipher, bestKey));
		int lastHit = 1;
		for(int i = 0; i < 500000; i++)
		{
			lastHit++;
			HashMap<Character,Character> key = scrambleKey(bestKey);

//			for(int j = 0; j < 10 / (lastHit/100.0); j++)
//			{
//				key = scrambleKey(key);
//			}

			String decrypted = decrypt(cipher, key);
			double fitness = measureFitness(decrypted);
			if(fitness > bestFitness)
			{
				lastHit = 0;
				bestFitness = fitness;
				bestKey = key;
				System.out.println(i + ":\t" + bestFitness + ":\t" + decrypted);
			}
		}
		
		String answer = checkPhrase(cipher, bestKey);
		if(answer != null)
		{
			System.out.print("CORRECT ANSWER");
			System.out.println(answer);
		} else {
			System.out.print("WRONG ANSWER");
			substitutionCipher(cipher);
		}
	}

	public static HashMap<Character,Character> generateParentKey()
	{
		HashMap<Character,Character> parentKey = new HashMap<Character,Character>();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i = 0; i < alphabet.length(); i++)
		{
			parentKey.put(alphabet.charAt(i), alphabet.charAt(i));
		}
		for(int i = 0; i < 100; i++)
		{
			parentKey = scrambleKey(parentKey);
		}

		return parentKey;
	}

	public static HashMap<Character,Character> scrambleKey(HashMap<Character,Character> key)
	{
		HashMap<Character,Character> tempMap = new HashMap<Character,Character>();
		for(Character c : key.keySet())
		{
			tempMap.put(c,key.get(c));
		}
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rand = new Random();
		int num1 = rand.nextInt(26);
		Character char1 = alphabet.charAt(num1);
		int num2 = rand.nextInt(26);
		Character char2 = alphabet.charAt(num2);
		Character temp = tempMap.get(char1);
		tempMap.put(char1, tempMap.get(char2));
		tempMap.put(char2, temp);
		return tempMap;
	}

	public static double measureFitness(String phrase)
	{
		double score = 0;
		//get quadgrams
		for(int i = 0; i < phrase.length() - 3; i++)
		{
			String subPhrase = phrase.substring(i, i+4);
			if(quadgrams.containsKey(subPhrase))
			{
				score += quadgrams.get(subPhrase);
			} else {
				score += quadFloor;
			}
		}

		return score;
	}

	public static void hashFitness() throws FileNotFoundException
	{
		monograms = new HashMap<String, Double>();
		bigrams = new HashMap<String, Double>();
		trigrams = new HashMap<String, Double>();
		quadgrams = new HashMap<String, Double>();
		quintgrams = new HashMap<String, Double>();
		//monograms


		//quadgrams
		double total = getTotal("src//quadgrams.txt");
		Scanner file = new Scanner(new File("src//quadgrams.txt"));
		while(file.hasNextLine())
		{
			String[] line = file.nextLine().split(" ");
			quadgrams.put(line[0], Math.log(Integer.parseInt(line[1]) / total));
		}
		quadFloor = Math.log(0.01/total);
		System.out.println("TOTAL:" + total);

		/*
		file = new Scanner(new File("src//monograms.txt"));
		total = getTotal("src//monograms.txt");
		while(file.hasNextLine())
		{
			String[] line = file.nextLine().split(" ");
			monograms.put(line[0], Math.log(Integer.parseInt(line[1]) / total));
		}
		monoFloor = Math.log(0.01/total);
		System.out.println("TOTAL:" + total);

		//bigrams
		file = new Scanner(new File("src//bigrams.txt"));
		total = getTotal("src//bigrams.txt");
		while(file.hasNextLine())
		{
			String[] line = file.nextLine().split(" ");
			bigrams.put(line[0], Math.log(Integer.parseInt(line[1]) / total));
		}
		biFloor = Math.log(0.01/total);
		System.out.println("TOTAL:" + total);

		//trigrams
		file = new Scanner(new File("src//trigrams.txt"));
		total = getTotal("src//trigrams.txt");
		while(file.hasNextLine())
		{
			String[] line = file.nextLine().split(" ");
			trigrams.put(line[0], Math.log(Integer.parseInt(line[1]) / total));
		}
		triFloor = Math.log(0.01/total);
		System.out.println("TOTAL:" + total);


		//quintgrams
		total = getTotal("src//quintgrams.txt");
		file = new Scanner(new File("src//quintgrams.txt"));
		while(file.hasNextLine())
		{
			String[] line = file.nextLine().split(" ");
			quintgrams.put(line[0], Math.log(Integer.parseInt(line[1]) / total));
		}
		System.out.println("TOTAL:" + total);*/

	}

	public static String checkPhrase(String phrase, HashMap<Character,Character> key) throws FileNotFoundException
	{
		if(isValid(decrypt(phrase, key)))
		{
			return decrypt(phrase, key);
		}
		for(int i = 0; i < 26; i++)
		{
			for(int j = i + 1; j < 26; j++)
			{
				HashMap<Character,Character> tempMap = new HashMap<Character,Character>();
				for(Character c : key.keySet())
				{
					tempMap.put(c,key.get(c));
				}
				String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				int num1 = i;
				Character char1 = alphabet.charAt(num1);
				int num2 = j;
				Character char2 = alphabet.charAt(num2);
				Character temp = tempMap.get(char1);
				tempMap.put(char1, tempMap.get(char2));
				tempMap.put(char2, temp);
				String potential = decrypt(phrase, tempMap);
				if(isValid(potential))
				{
					return potential;
				}
			}
		}
		return null;
	}
	
	public static double getTotal(String fileName) throws FileNotFoundException
	{
		Scanner file = new Scanner(new File(fileName));
		double total = 0;
		while(file.hasNextLine())
		{
			total += Integer.parseInt(file.nextLine().split(" ")[1]);
		}
		return total;
	}
	public static String decrypt(String phrase, HashMap<Character,Character> key)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < phrase.length(); i++)
		{
			sb.append(key.get(phrase.charAt(i)));
		}

		return sb.toString();
	}
	public static void printMap(HashMap map)
	{
		for(Object o : map.keySet())
		{
			System.out.println(o + ":" + map.get(o));
		}
	}

	public static void printArray(int[] p1)
	{
		for(int i = 0; i < p1.length; i++)
		{
			System.out.print(p1[i] + ".");
		}
		System.out.println();
	}
}
