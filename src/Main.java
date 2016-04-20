import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * My attempt at creating a program that simulates a compression of a text file using Huffman Encoding.
 * Note that it does not actually compress files as when I write "binary" to a file each 1 and 0 is written as a byte and not a bit.
 * While doing this assignment I used/modified/referred to code from these places: https://rosettacode.org/wiki/Huffman_coding#Java ,
 * http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/ , http://algs4.cs.princeton.edu/55compression/Huffman.java.html
 * 
 * @author Chris Bodels
 *
 */
public class Main {
	
	private String text = "";
	private Map<Character, Integer> freqMap;
	private Map<Character, String> codeMap;
	private Map<String, Character> reverseCodeMap;
	private String encodedText;
	
	/**
	 * Constructor for objects of type Main.
	 */
	public Main()
	{
		codeMap = new HashMap<Character, String>();
		reverseCodeMap = new HashMap<String, Character>();
	}

	/**
	 * The main method that is run automatically when the program starts.
	 * @param argvs
	 */
	public static void main(String argvs[])
	{
		Main app = new Main();
		app.readFile();
		app.setUp();
		app.writeEncodedFile();
		app.decodeFile();
	}
	
	/**
	 * This method reads in the file to be "compressed" and saves the text contained in the file to a string.
	 */
	public void readFile()
	{
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader("sampleFile.txt")))
		{
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			
			while(line != null)
			{
				stringBuilder.append(line);
				//stringBuilder.append(System.lineSeparator());
				line = bufferedReader.readLine();
			}
			text = stringBuilder.toString();
			bufferedReader.close();
			System.out.println("The text from the input file: ");
			System.out.println(text);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method sets up many elements of the program such as arrays that are used to form maps, a map of the frequency of each character in the file etc.
	 */
	public void setUp()
	{
		char[] charArray = text.toCharArray();
		int[] freq = new int[128];
		for(int i = 0; i < charArray.length; i++)
		{
			freq[charArray[i]]++;
		}
		
		freqMap = new HashMap<Character, Integer>();
		for(int i = 0; i < text.length(); i++)
		{
			char currentChar = text.charAt(i);
			if(!freqMap.containsKey(currentChar))
			{
				freqMap.put(currentChar, 1);
			}
			else
			{
				int value = freqMap.get(currentChar);
				freqMap.put(currentChar, ++value);
			}
		}
		System.out.println("");
		System.out.println("Map of frequency of each character: " + freqMap.toString());
		System.out.println("");
		
		Node root = buildTree(freq);
		String[] stringArray = new String[128];
		buildCode(stringArray, root, "");
		
		System.out.println("");
		System.out.println("Map of characters as keys and their Huffman coded binary equivalent as values: " + codeMap.toString());
		System.out.println("");
		System.out.println("");
	}
	
	/**
	 * This method writes the encoded file. First it creates a text version of the hash map used to encode the file which can be read back in later to 
	 * rebuild the hash map which will be used to decode the encoded file. It then writes the encoded file using Huffman codes to represent characters.
	 */
	public void writeEncodedFile()
	{
		try
		{
			File encodedFile = new File("Encoded.txt");
			encodedFile.createNewFile();
			FileWriter fileWriter = new FileWriter(encodedFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			for(Map.Entry<Character, String> map : codeMap.entrySet())
			{
				String entry = map.getKey() + "¿" + map.getValue() + "Æ";
				bufferedWriter.write(entry);
			}
			bufferedWriter.write("¶");
			bufferedWriter.write(encodeText());
			bufferedWriter.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method decodes the encoded file by first reading in the text version of the hash map and then uses the rebuilt hash map to decode the 
	 * Huffman coded content of the file back into plain text.
	 */
	public void decodeFile()
	{
		String content = "";
		Map<String, String> decoderMap = new HashMap<String, String>();
		Map<String, String> reverseDecoderMap = new HashMap<String, String>();
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader("Encoded.txt")))
		{
			//StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while(line != null)
			{
				String[] headerAndContent = line.split("¶");
				String header = headerAndContent[0];
				content = headerAndContent[1];
				String charsAndCodes[] = header.split("Æ");
				for(String charAndCode : charsAndCodes)
				{
					String[] pairing = charAndCode.split("¿");
					String ch = pairing[0];
					String code = pairing[1];
					decoderMap.put(ch, code);
					reverseDecoderMap.put(code, ch);
				}
				line = bufferedReader.readLine();
			}
			System.out.println("Map taken from encoded file: ");
			System.out.println(decoderMap.toString());
			bufferedReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String chars = "";
		String decodedMessage = "";
		for(int i = 0; i < content.length(); i++)
		{
			chars += content.charAt(i);
			if(reverseDecoderMap.containsKey(chars))
			{
				decodedMessage += reverseDecoderMap.get(chars);
				chars = "";
			}
		}
		System.out.println("");
		System.out.println("The decoded message: ");
		System.out.println(decodedMessage);
		writeDecodedFile(decodedMessage);
	}
	
	/**
	 * This method writes the final decoded file to "Decoded.txt". All going well this should be the same as the original "sampleFile.txt"
	 * with the exception of line breaks.
	 * @param decoded The already decoded string to be written to the file.
	 */
	public void writeDecodedFile(String decoded)
	{
		try
		{
			File decodedFile = new File("Decoded.txt");
			decodedFile.createNewFile();
			FileWriter fileWriter = new FileWriter(decodedFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);			
			
			bufferedWriter.write(decoded);
			bufferedWriter.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method encodes the text of the original file by going through the file character by character and looking up the hash map 
	 * for the character and creating a string of the Huffman code for each character.
	 * @return The newly Huffman encoded text.
	 */
	public String encodeText()
	{
		encodedText = "";
		for(int i = 0; i < text.length(); i++)
		{
			encodedText += codeMap.get(text.charAt(i));
		}
		return encodedText;
	}
	
	/**
	 * This method builds the tree used to encode characters using a priority queue.
	 * @param freq
	 * @return
	 */
	public Node buildTree(int[] freq)
	{
		PriorityQueue<Node> priorityQ = new PriorityQueue<Node>();
		for(char i = 0; i < 128; i++)
		{
			if(freq[i] > 0)
			{
				priorityQ.add(new Node(i, freq[i], null, null));
			}
		}
		
		while(priorityQ.size() > 1)
		{
			Node leftChild = priorityQ.remove();
			Node rightChild = priorityQ.remove();
			Node parent = new Node('\0', leftChild.getFreq() + rightChild.getFreq(), leftChild, rightChild);
			priorityQ.add(parent);
		}
		return priorityQ.remove();
	}
	
	/**
	 * Method that builds the code for each character by traversing through the tree and adding a 1 to the code for right and 0 for left.
	 * Once it reaches a leaf it adds the code that it has made into a hash map and associates it with the character it was encoding.
	 * @param stringArray
	 * @param node
	 * @param string
	 */
	public void buildCode(String[] stringArray, Node node, String string)
	{
		if(!node.isLeaf())
		{
			buildCode(stringArray, node.getLeft(), string + '0');
			buildCode(stringArray, node.getRight(), string + '1');
		}
		else
		{
			stringArray[node.getChar()] = string + " ";
			codeMap.put(node.getChar(), string);
			reverseCodeMap.put(string, node.getChar());
		}
	}
}
