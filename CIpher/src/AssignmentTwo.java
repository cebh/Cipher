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
	public static HashMap<String, Double> quadgrams;

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
/*		testing
 		HashMap<Character,Character> test1 = new HashMap<Character,Character>();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i = 0; i < 26; i++)
		{
			test1.put(alphabet.charAt(i), alphabet.charAt(i));
		}
		test1.put('a', 'b');
		test1.put('b', 'a');
		
		String tester = "BMAERBNDARBSS";
		System.out.println((decrypt(tester,test1)));
		System.out.println(checkPhrase(tester,test1));
		*/
	}

	//check to see if a phrase is a valid sentence of english words
	public static boolean isValid(String phrase) throws FileNotFoundException
	{
		phrase.replace(" ", ""); //remove whitespace
		if(phrase.length() == 0) //base case
		{
			return true;
		}
		//iterate through phrase
		for(int i = 1; i <= phrase.length(); i++) //changed from phrase.length()
		{
			//if this substring is a word
			if(searchWord(phrase.substring(0,i)))//i < 10 && 
			{
				//recursively call for the rest of the phrase
				if(isValid(phrase.substring(i)))
				{
					//entire phrase is correct
					return true;
				}
			}
		}
		return false;
	}

	//search for a word in the dictionary
	public static boolean searchWord(String word) throws FileNotFoundException
	{
		//check for i and a
		if(word.length() == 1) 
		{
			if(word.equals("i") || word.equals("a"))
			{
				//is a word
				return true;
			}
			return false;
		}
		//check hashmap for word
		if(words.contains(word.toLowerCase()))
		{
			return true;
		}
		return false;
	}

	//convert text file of words to hashset
	public static void hashWords(String fileName) throws FileNotFoundException
	{
		int longest = 0;
		Scanner file = new Scanner(new File(fileName)); //input file
		while(file.hasNextLine()) //iterate through file
		{
			String nextLine = file.nextLine();
			if(nextLine.length() > 2) //don't add words less than two letters
			{
				words.add(nextLine.toLowerCase());
				if(nextLine.length() > longest)
				{
					//store longest length
					longest = nextLine.length();
				}
			}
		}
		//use separate list for words of two letters or less
		Scanner twoFile = new Scanner(new File("src//two_letter_words.txt"));
		while(twoFile.hasNextLine())
		{
			//store two letter words
			String nextLine = twoFile.nextLine();
			words.add(nextLine.toLowerCase());
		}
		longestLength = longest;
	}

	//perform ceaser cipher
	public static boolean ceaserCipher(String cipher) throws FileNotFoundException
	{
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		HashMap<Character,Character> key = new HashMap<Character,Character>();
		for(int i = 0; i < 26; i++) //try every single combination
		{
			//compute offset and store in map
			for(int j = 0; j < 26; j++)
			{
				int offset = i + j;
				if(offset >= 26)
				{
					offset-= 26;
				}
				key.put(alphabet.charAt(j), alphabet.charAt(offset));
			}
			
			//attempt to decrypt phrase with current offset
			StringBuilder sb = new StringBuilder();
			for(int k = 0; k < cipher.length(); k++)
			{
				//offset with ascii values
				int numValue = (int)Character.toLowerCase(cipher.charAt(k));
				if(numValue >= 97 && numValue <= 122)
				{
					sb.append(key.get(Character.toUpperCase(cipher.charAt(k))));
				}
			}
			//check to see if phrase is composed of words
			if(isValid(sb.toString()))
			{
				//output correct phrase and offset
				System.out.println("Offset:" + (26 - i) + "\n" + sb);
				return true;
			}
		}
		return false;
	}

	//perform substitution cipher
	public static void substitutionCipher(String cipher) throws FileNotFoundException
	{
		hashFitness(); //hash quadgram fitnesses into map
		System.out.println("START");
		cipher = cipher.replace(" ", "").toUpperCase(); //remove whitespace
		HashMap<Character,Character> bestKey = generateParentKey(); //create random paret key
		double bestFitness = measureFitness(decrypt(cipher, bestKey)); //measure fitness of current key
		int lastHit = 1; //store how long until a new best key has been found
		for(int i = 0; i < 500000; i++) //run for finite amount of time
		{
			lastHit++; //incease last hit
			HashMap<Character,Character> key = scrambleKey(bestKey); //scramble key

			//scramble less times when there hasn't been a new best key in a while
			for(int j = 0; j < 10 / (lastHit/100.0); j++)
			{
				key = scrambleKey(key);
			}

			String decrypted = decrypt(cipher, key); //decrypt using current key
			double fitness = measureFitness(decrypted); //measure fitness
			if(fitness > bestFitness) //if new fitness is better
			{
				lastHit = 0; //reset
				bestFitness = fitness; //new best fitness
				bestKey = key; //new best key
				System.out.println(i + ":\t" + bestFitness + ":\t" + decrypted); //print out result
			}
		}
		
		//check to see if the phrase is less than one swap off
		String answer = checkPhrase(cipher, bestKey);
		if(answer != null) //if it is not null
		{
			System.out.print("CORRECT ANSWER");
			System.out.println(answer); //print correct answer
		} else {
			System.out.print("WRONG ANSWER"); //print wrong answer
			substitutionCipher(cipher); //try again
		}
	}

	//create random parent key
	public static HashMap<Character,Character> generateParentKey()
	{
		HashMap<Character,Character> parentKey = new HashMap<Character,Character>();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//populate key
		for(int i = 0; i < alphabet.length(); i++)
		{
			parentKey.put(alphabet.charAt(i), alphabet.charAt(i));
		}
		//scramble key many times
		for(int i = 0; i < 100; i++)
		{
			parentKey = scrambleKey(parentKey);
		}

		return parentKey;
	}

	//swap two letters in the key
	public static HashMap<Character,Character> scrambleKey(HashMap<Character,Character> key)
	{
		HashMap<Character,Character> tempMap = new HashMap<Character,Character>();
		//populate temporary key
		for(Character c : key.keySet())
		{
			tempMap.put(c,key.get(c));
		}
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		//swap random letters
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

	//measure fitness of a phrase
	public static double measureFitness(String phrase)
	{
		double score = 0;
		//check every 4 letter phrase against quadgrams
		for(int i = 0; i < phrase.length() - 3; i++)
		{
			//get subphrase
			String subPhrase = phrase.substring(i, i+4);
			//get fitness from quadgrams map
			if(quadgrams.containsKey(subPhrase))
			{
				//if it exists, add score
				score += quadgrams.get(subPhrase);
			} else {
				//if it doesn't exist, add base score
				score += quadFloor;
			}
		}

		return score;
	}

	//store fitness levels from text file
	public static void hashFitness() throws FileNotFoundException
	{
		quadgrams = new HashMap<String, Double>();
		
		//calculate quadgram fitness levels
		double total = getTotal("src//quadgrams.txt");
		Scanner file = new Scanner(new File("src//quadgrams.txt"));
		while(file.hasNextLine())
		{
			String[] line = file.nextLine().split(" ");
			//use frequency to calculate score using log and total quadgrams
			quadgrams.put(line[0], Math.log(Integer.parseInt(line[1]) / total));
		}
		//calculate base value for values not in the quadgram list
		quadFloor = Math.log(0.01/total);
		System.out.println("TOTAL:" + total);

	}

	//check if phrase is less than one swap away from a porper english phrase
	public static String checkPhrase(String phrase, HashMap<Character,Character> key) throws FileNotFoundException
	{
		//check if current phrase is valid
		if(isValid(decrypt(phrase, key)))
		{
			return decrypt(phrase, key);
		}
		//check all combinations of one swap to see if that phrase is valid
		for(int i = 0; i < 26; i++)
		{
			for(int j = i + 1; j < 26; j++)
			{
				//swap one letter in the map
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
				//check potentially decrypted string
				String potential = decrypt(phrase, tempMap);
				//check if new phraseisvalid
				if(isValid(potential))
				{
					return potential;
				}
			}
		}
		return null;
	}
	
	//caluclate total number of quadgrams used in text file
	public static double getTotal(String fileName) throws FileNotFoundException
	{
		Scanner file = new Scanner(new File(fileName));
		double total = 0;
		//read through file
		while(file.hasNextLine())
		{
			//add number of quadgrams to total
			total += Integer.parseInt(file.nextLine().split(" ")[1]);
		}
		return total;
	}
	
	//decrypt phrase using a current key
	public static String decrypt(String phrase, HashMap<Character,Character> key)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < phrase.length(); i++)
		{
			//convert individual character using key, add to phrase
			sb.append(key.get(phrase.charAt(i)));
		}
		return sb.toString();
	}
	
	//print out a hashmap (used for testing)
	public static void printMap(HashMap map)
	{
		for(Object o : map.keySet())
		{
			System.out.println(o + ":" + map.get(o));
		}
	}

	//print out an array (used for testing)
	public static void printArray(int[] p1)
	{
		for(int i = 0; i < p1.length; i++)
		{
			System.out.print(p1[i] + ".");
		}
		System.out.println();
	}
}
