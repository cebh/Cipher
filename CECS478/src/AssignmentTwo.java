import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class AssignmentTwo {
	public static int times = 0;
	public static HashSet<String> words = new HashSet<String>();
	public static int longestLength = 100;
	private static int counter = 0;
	public static void main(String[]args) throws FileNotFoundException
	{
		hashWords("src//scrabble_words.txt");
		Scanner in = new Scanner(System.in);
		System.out.println("Enter encrypted code:");
		String cipher = in.nextLine();
		ceaserCipher(cipher);
		//		HashMap<Character, Integer> test = new HashMap<Character, Integer>();
		//		//toPattern("classification", calculatePattern("classification", test));
		//		//System.out.println(isValidPattern("AAH", test));
		//		test = calculatePattern("cat");
		//		HashMap<Character, Integer> test2 = calculatePattern("dog");
		//		System.out.print(Arrays.equals(toPattern("cat"), toPattern("dog")));
		HashMap<Character,Character> test1 = new HashMap<Character,Character>();
		HashMap<Character,Character> test2 = new HashMap<Character,Character>();
		test1.put('c', 'd');
		test2.put('a', 'b');
		//System.out.println(checkMaps(test1,test2));
		//HashMap<Character,Character> test3 = combineMaps(test1,test2);
		//printMap(test3);
		//System.out.println(searchWordPattern("hello"));
		//System.out.println(isValidPattern(cipher, new HashMap<Character,Character>(), ""));
		//HashMap<Character,Character> test4 = createKeyMap("hip", "rob");
		//printMap(test4);
		//HashMap<Character,Character> test5 = new HashMap<Character,Character>();
		//		test5.put('y', 'a');
		//		test5.put('h', 'm');
		//		test5.put('l', 'g');
		//		test5.put('o', 'y');
		//	
		//		System.out.println(checkWordsWithMap("hello", "moggy", test5));
		//		int[]p1 = toPattern("hello");
		//		printArray(p1);
		//printMap(calculatePattern("Hello"));

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

	public static void ceaserCipher(String cipher) throws FileNotFoundException
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
			}
		}
	}
/*
	public static HashMap<Character,Integer> calculatePattern(String word)
	{
		HashMap<Character,Integer> match = new HashMap<Character,Integer>();
		int counter = 0;
		for(int i = 0; i < word.length(); i++)
		{
			char current = Character.toLowerCase(word.charAt(i));
			if(!match.containsKey(current))
			{
				match.put(current, counter);
				counter++;
			}
		}
		return match;
	}

	public static int[] toPattern(String word)
	{
		HashMap<Character,Integer> match = calculatePattern(word);
		int[] pattern = new int[word.length()];
		for(int i = 0; i < word.length(); i++)
		{
			pattern[i] = match.get(word.charAt(i));
		}
		//		print pattern
		return pattern;
	}

	public static String searchWordPattern(String word) throws FileNotFoundException
	{
		for(String s : words)
		{
			if(Arrays.equals(toPattern(s),toPattern(word)))
			{
				//System.out.println(word);
				return s;
			}
		}
		return null;
	}

	public static ArrayList<String> searchWordPatternList(String word) throws FileNotFoundException
	{
		ArrayList<String> list = new ArrayList<String>();
		if(word.length() == 1)
		{
			list.add("i");
			list.add("a");
		} else {
			for(String s : words)
			{
				if(Arrays.equals(toPattern(s),toPattern(word)))
				{
					//System.out.println(word);
					list.add(s);
				}
			}
		}
		return list;
	}

	public static boolean isValidPattern(String phrase, HashMap<Character,Character> keyMap, String currentString) throws FileNotFoundException
	{
		System.out.println("COUNTER:" + counter++);
		phrase = phrase.replace(" ", "");
		if(phrase.length() == 0)
		{
			return true;
		}
		for(int i = 1; i <= 12; i++) //changed from phrase.length()
		{
			ArrayList<String> list = searchWordPatternList(phrase.substring(0,i));
			for(int j = 0; j < list.size(); j++)
			{

				String matchString = list.get(j);
				HashMap<Character,Character> newMap = createKeyMap(phrase.substring(0,i), matchString);
				if(checkMaps(keyMap, newMap))
				{
					HashMap<Character,Character> tempMap = combineMaps(keyMap, newMap);
					System.out.println(phrase.substring(0,i) + " " + currentString);
					//printMap(tempMap);
					if(checkWordsWithMap(phrase.substring(0,i), matchString, tempMap))
					{
						System.out.println(i + "------------------");

						if(isValidPattern(phrase.substring(i), tempMap, currentString + matchString))
						{
							System.out.print("____________");
							System.out.println(currentString);
							printMap(tempMap);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static HashMap<Character,Character> createKeyMap(String encrypted, String regular)
	{
		HashMap<Character,Character> map = new HashMap<Character,Character>();
		for(int i = 0; i < encrypted.length(); i++)
		{
			map.put(encrypted.charAt(i), regular.charAt(i));
		}
		return map;
	}

	public static boolean checkMaps(HashMap<Character,Character> mapOne, HashMap<Character,Character> mapTwo)
	{
		for(Character c : mapOne.keySet())
		{
			if(mapTwo.keySet().contains(c))
			{
				if(mapOne.get(c) != mapTwo.get(c))
				{
					return false;
				}
			}
		}

		for(Character c : mapOne.keySet())
		{
			Character value1 = mapOne.get(c);
			for(Character cc : mapTwo.keySet())
			{
				Character value2 = mapTwo.get(cc);
				if(value1 == value2)
				{
					if(c != cc)
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	public static HashMap<Character,Character> combineMaps(HashMap<Character,Character> mapOne, HashMap<Character,Character> mapTwo)
	{
		for(Character c : mapOne.keySet())
		{
			mapTwo.put(c, mapOne.get(c));
		}
		return mapTwo;
	}

	public static boolean checkWordsWithMap(String encrypted, String regular, HashMap<Character,Character> map)
	{
		StringBuilder temp = new StringBuilder();
		for(int i = 0; i < encrypted.length(); i++)
		{
			temp.append(map.get(encrypted.charAt(i)));
		}
		if(temp.toString().equals(regular)) return true;
		else return false;
	}
*/
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
