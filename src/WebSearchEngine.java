import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSearchEngine {
	static ArrayList<String> key = new ArrayList<String>();
	static Hashtable<String, Integer> numbers = new Hashtable<String, Integer>();
	static Scanner sc = new Scanner(System.in);

	public WebSearchEngine() throws Exception {
		try {
			HTMLTextConverter.convertHtmlToText();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
	}

	// Positions and Occurrences of the words
	public int searchWord(File filePath, String s1) throws IOException {
		int my_counter = 0;
		String my_data = "";
		
		try {
			BufferedReader my_Object = new BufferedReader(new FileReader(filePath));
			String line = null;

			while ((line = my_Object.readLine()) != null) {

				my_data = my_data + line;

			}
			my_Object.close();

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		String txt = my_data;
		BoyerMoore offset1a = new BoyerMoore(s1);

		int offset = 0;
		
		for (int loc = 0; loc <= txt.length(); loc += offset + s1.length()) {
			offset = offset1a.search(s1, txt.substring(loc));
			// System.out.println("\n");
			if ((offset + loc) < txt.length()) {
				my_counter++;
				System.out.println(my_counter + ") " + s1 + " is at position " + (offset + loc)); // printing position of word																									
			}
		}
		
		if (my_counter != 0) {
			System.out.println("\nFile name that contains above list: " + filePath.getName());
			System.out.println("****************************************************\n");
		}
		return my_counter;
	}

	// Merge-sort for ranking of the pages
	
	public static void rankFiles(Hashtable<?, Integer> fname, int occur) {

		// Transfer as List and sort it
		ArrayList<Map.Entry<?, Integer>> my_list = new ArrayList(fname.entrySet());

		Collections.sort(my_list, new Comparator<Map.Entry<?, Integer>>() {

			public int compare(Map.Entry<?, Integer> obj1, Map.Entry<?, Integer> obj2) {
				return obj1.getValue().compareTo(obj2.getValue());
			}
		});

		Collections.reverse(my_list);

		if (occur != 0) {
			System.out.println("\n------Top 5 search results -----\n");

			int my_number = 5;
			int j = 1;
			while (my_list.size() > j && my_number > 0) {
				System.out.println("(" + j + ") " + my_list.get(j) + " times ");
				j++;
				my_number--;
			}
		} else {

		}

	}

	//Using Regular Expressions for pattern matching
	public void suggestions(String pattern) {
		try {

			// String to be scanned to find the pattern.
			String line = " ";
			String reg_ex = "[\\w]+[@$%^&*()!?=.{}\b\n\t]*";

			// Create a Pattern object
			Pattern pat = Pattern.compile(reg_ex);
			// Now create matcher object.
			Matcher my_match = pat.matcher(line);
			int file_Number = 0;
			try {
				File my_directory = new File("C:\\Users\\sheshan\\workspace\\Serach_Engine\\ConvertedTextFiles\\");
				File[] fileArray = my_directory.listFiles();
				for (int i = 0; i < fileArray.length; i++)

				{
					findWord(fileArray[i], file_Number, my_match, pattern);
					file_Number++;
				}

				Set keys = new HashSet();
				Integer value = 1;
				Integer val = 0;
				int counter = 0;

				System.out.println("\nDid you mean?:");
				System.out.println("---------------------");
				for (Map.Entry entry : numbers.entrySet()) {
					if (val == entry.getValue()) {

						break;

					} else {

						if (value == entry.getValue()) {

							if (counter == 0) {
								System.out.print(entry.getKey());
								counter++;
							}

							else {
								System.out.print(" or " + entry.getKey());
								counter++;
							}

						}

					}
				}

			} catch (Exception e) {
				System.out.println("Exception:" + e);
			} finally {

			}

		} catch (Exception e) {

		}
	}

	// finds strings with similar pattern and calls edit distance() on those strings
	public static void findWord(File sourceFile, int fileNumber, Matcher match, String str)
			throws FileNotFoundException, ArrayIndexOutOfBoundsException {
		try {
			int i = 0;
			BufferedReader my_rederObject = new BufferedReader(new FileReader(sourceFile));
			String line = null;

			while ((line = my_rederObject.readLine()) != null) {
				match.reset(line);
				while (match.find()) {
					key.add(match.group());
				}
			}
			
			my_rederObject.close();
			for (int p = 0; p < key.size(); p++) {
				numbers.put(key.get(p), editDistance(str.toLowerCase(), key.get(p).toLowerCase()));
			}
		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}

	}

	public static int editDistance(String str1, String str2) {
		int len1 = str1.length();
		int len2 = str2.length();

		int[][] my_array = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			my_array[i][0] = i;
		}

		for (int j = 0; j <= len2; j++) {
			my_array[0][j] = j;
		}

		// iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = str1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = str2.charAt(j);

				if (c1 == c2) {
					my_array[i + 1][j + 1] = my_array[i][j];
				} else {
					int replace = my_array[i][j] + 1;
					int insert = my_array[i][j + 1] + 1;
					int delete = my_array[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					my_array[i + 1][j + 1] = min;
				}
			}
		}

		return my_array[len1][len2];
	}

	public static void main(String[] args) throws Exception {

		System.out.println("############ WEB SEARCH ENGINE ############");
		WebSearchEngine web_search1 = new WebSearchEngine();

		Hashtable<String, Integer> hash_table1 = new Hashtable<String, Integer>();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter query to search : ");
		String input = sc.nextLine();
		long my_fileNumber = 0;
		int frequency = 0;
		int rep = 0; 
		try {
			
			File my_dir = new File("C:\\Users\\sheshan\\workspace\\Serach_Engine\\ConvertedTextFiles\\");

			File[] fileArray = my_dir.listFiles();
			
		
			for (int i = 0; i < fileArray.length; i++) {

				frequency = web_search1.searchWord(fileArray[i], input);

				hash_table1.put(fileArray[i].getName(), frequency);
				
				if (frequency != 0) {
					rep++;
				}

				my_fileNumber++;
			}

			System.out.println("\nTotal Number of Files for " + input + " is : " + rep);
			
			if (rep == 0) {

				System.out.println("\nSearching...");

				web_search1.suggestions(input);

			}
			
			rankFiles(hash_table1, rep);

		} catch (Exception e) {
			System.out.println("Exception:" + e);
		} finally {

		}

	}

}
