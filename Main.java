package com.breakingcode.unoptimised;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.breakingcode.unoptimised.data.FoodSafetyData;
import com.breakingcode.unoptimised.data.enums.Headers;

public class Main {

	private final List<FoodSafetyData> foodSafetyList = new LinkedList<FoodSafetyData>();
	private final List<String> authorities = new ArrayList<String>();
	private final Scanner scanner;

	public static void main(String[] args) {

		System.out.println("Greetings! Welcome to our Food Hygiene Data; ");
		System.out.println("");

		new Main();
	}

	public Main() {
		scanner = new Scanner(System.in);
		parseCSV();
		showMainMenu();
	}

	/**
	 * Method to scan all CSV files in the provided directory, and parse them as
	 * unique FoodSafetyData records in our internal data structures.
	 *
	 * Each unique file in the directory has it's filename added to a list of
	 * authorities so that we can keep track of the authorities we have records for.
	 *
	 * (This is assuming that all files placed in this directory are indeed FHRS
	 * files in the CSV format, whereby each files name is the authority where data
	 * has been collected from).
	 */
	private void parseCSV() {

		File dataset = new File("./datasets-foodsafety/");
		File[] csvFiles = dataset.listFiles();

		for (File file : csvFiles) {
			if (file.getName().contains(".csv")) {
				try (Reader in = new FileReader(file)) {

					Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(Headers.class).withFirstRecordAsHeader()
							.parse(in);

					for (CSVRecord record : records) {
						foodSafetyList.add(new FoodSafetyData(record));
					}

					authorities.add(file.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				System.out.println(
						"Detected file which doesn't belong in directory \'datatsets-foodsafety\': " + file.getName());
		}
	}

	/**
	 * Main menu method
	 */
	public void showMainMenu() {

		String[] mainMenu = { "List our local authorities", "List all recorded businessess", "List All Premise Ratings",
				"List output details with specified rating", "E", "Quit" };
		boolean running = true;
		do {
			char menuChoice = shortMenu(mainMenu);
			/**
			 * Menu handling. No need for default case, as this line shouldn't be reached
			 * until a valid input is detected.
			 */

			switch (menuChoice) {
			case '1':
				listAllLocalAutorities();
				break;
			case '2':
				listAllBusinesses();
				break;
			case '3':
				listAllPremiseRatings();
				break;
			case '4':
				listSpecifiedRatings();
				break;
			case '5':
				System.out.println("E");
				break;
			case '6':
				running = false;
				break;
			}
		} while (running); // Added this loop to stop unnecessary calls to showMenu (adding calls to stack)

		scanner.close();
	}

	/**
	 * A - Prints all Local Authorities using file names of the authorities we have
	 */
	private void listAllLocalAutorities() {
		System.out.println(" -- Local Authorities -- ");
		System.out.println("We have data for these authorities: ");

		for (String name : authorities) {
			name = name.split(".csv")[0];
			System.out.println(name);
		}
	}

	/*
	 * B - Prints unique business names
	 */
	private void listAllBusinesses() {
		System.out.println("We have data for the following businesses");
		List<String> uniqueNames = new ArrayList<>(this.foodSafetyList.size());

		List<FoodSafetyData> sortedList = this.foodSafetyList;
		
		final Comparator<FoodSafetyData> nameSorter = new Comparator<FoodSafetyData>() {
			@Override
			public int compare(FoodSafetyData o1, FoodSafetyData o2) {
				return o1.getBusinessName().compareTo(o2.getBusinessName());
			}
		};

		Collections.sort(sortedList, nameSorter);

		for (int i = 0; i < this.foodSafetyList.size();i++) {
			if(i-1 == -1) {uniqueNames.add(this.foodSafetyList.get(i).getBusinessName()); continue;}
			if (!sortedList.get(i).getBusinessName().equals(sortedList.get(i - 1).getBusinessName())) {
				uniqueNames.add(this.foodSafetyList.get(i).getBusinessName());
			}
		}

		System.out.printf("We have %s businesses\n", uniqueNames.size());
		String[] namesStr = new String[uniqueNames.size()];
		uniqueNames.toArray(namesStr);

		int currentIndex = 0;
		boolean navigate = true;

		do {
			char c = shortMenu(new String[] { "Previous", "Next", "Display Everything", "Quit" });

			switch (c) {
			case '1':
				int length = currentIndex - 10;
				if (length < 0) {
					currentIndex = namesStr.length - 1;
					length = currentIndex - 10; // length = 10
				}
				for (; currentIndex >= length - 1; currentIndex--) {
					System.out.printf("%s - %s \n", currentIndex, namesStr[currentIndex]);
				}
				break;
			case '2':
				length = currentIndex + 10;
				if (length >= namesStr.length) {
					currentIndex = 0;
					length = currentIndex + 10; // length = 10
				}

				for (; currentIndex < length; currentIndex++) {
					System.out.printf("%s - %s \n", currentIndex, namesStr[currentIndex]);
				}
				break;
			case '3':
				for (int i = 0; i < namesStr.length; i++) {
					System.out.printf("%s - %s \n", i, namesStr[i]);
				}
				break;
			case '4':
				navigate = false;
				break;
			}
		} while (navigate);

	}

	/**
	 * C - List all premise ratings
	 */
	public void listAllPremiseRatings() {
		System.out.print("\nEnter the business name: ");
		scanner.nextLine();
		String inputName = scanner.nextLine();
		final List<FoodSafetyData> tempList = new ArrayList<>(1);

		for (FoodSafetyData data : this.foodSafetyList) {
			if (data.getBusinessName().equalsIgnoreCase(inputName)) {
				tempList.add(data);
			}
		}

		if (tempList.size() < 0) {
			System.out.println("No business with that name found");
			return;
		}

		// Date sorter
		final Comparator<FoodSafetyData> dateSorter = new Comparator<FoodSafetyData>() {
			@Override
			public int compare(FoodSafetyData o1, FoodSafetyData o2) {
				if (o1.ratingDate == null || o2.ratingDate == null) {
					return -1;
				} else if (o1.ratingDate.before(o2.ratingDate)) {
					return 0;
				} else if (o1.ratingDate.after(o2.ratingDate)) {
					return -1;
				}

				return 0;

			}
		};

		// Sort out list chronologically
		Collections.sort(tempList, dateSorter);

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E dd/MM/yyyy");

		System.out.println("Display Business name for: " + inputName);

		String heading = "| Rating Date         | Hygiene Score | Structural Score | Confidence in Management Score | Food Hygiene Rating |";
        String top = printSeperator(heading, '┌', '┐');
        String seperator = printSeperator(heading, '|', '|');
        String bot = printSeperator(heading, '└', '┘');
		System.out.println(top);
		System.out.println(heading);
		System.out.println(seperator);

		for (FoodSafetyData data : tempList) {
			Date date = data.ratingDate;
			String dateFormat = "Date Not Avaliable";
			if (date != null) {
				dateFormat = DATE_FORMAT.format(date);
			}
			System.out.printf("| %-19s | %-13s | %-16s | %-30s | %-19s |\n", dateFormat, data.hygieneScore,
					data.structuralScore, data.confidenceInManagementScore, data.ratingValue);
		}
		System.out.println(bot);

	}
	
	public int getRatingValue(FoodSafetyData data) {
        if (data.ratingValue == null)
            return -999;
        else if (data.ratingValue.equals("AwaitingInspection")) {
            return -666;
        }
        else if (data.ratingValue.equals("Exempt")) {
            return -111;
        }
        else {
            try {
                return Integer.parseInt(data.ratingValue);
            } catch (NumberFormatException e) {
                return -999;
            }
        }
    }
	
	
	public void listSpecifiedRatings() {
		int minimum;
		int maximum;
		String specialValue;
		
		
		System.out.print("\nEnter the business name: ");
		scanner.nextLine();
		String inputName = scanner.nextLine();
		
		
		
		final List<FoodSafetyData> tempList = new ArrayList<>(1);
      int value;
			String[] mainMenu = { "Above a specified value", "Below a specified value", "Within a specified range", " A special value (pending)" };
			boolean running = true;
			do {
				char menuChoice = shortMenu(mainMenu);
				/**
				 * Menu handling. No need for default case, as this line shouldn't be reached
				 * until a valid input is detected.
				 */

				switch (menuChoice) {
				case '1':
				  System.out.println("Please enter a value");
				  value = scanner.nextInt();
				  running = false;
					for (FoodSafetyData data : this.foodSafetyList) {
						int hygieneValue = getRatingValue(data);
						if (data.getBusinessName().equalsIgnoreCase(inputName)&& hygieneValue > value && hygieneValue != -999) {
							tempList.add(data);
						}
					}
				  
					break;
				case '2':
					System.out.println("Please enter a value");
					value = scanner.nextInt();
					running = false;
					for (FoodSafetyData data : this.foodSafetyList) {
						int hygieneValue = getRatingValue(data);
						if (data.getBusinessName().equalsIgnoreCase(inputName)&& hygieneValue < value && hygieneValue != -999) {
							tempList.add(data);
						}
					}

					break;
					
				case '3':
					System.out.println("Please enter minimum rating: ");
					minimum = scanner.nextInt();
					System.out.println("Please enter the maximum rating");
					maximum = scanner.nextInt();
					running = false;
					for (FoodSafetyData data : this.foodSafetyList) {
						int hygieneValue = getRatingValue(data);
						if (data.getBusinessName().equalsIgnoreCase(inputName)&& hygieneValue < maximum && hygieneValue > minimum && hygieneValue != -999) {
							tempList.add(data);
						}
					}
					break;
				case '4':
					System.out.println("Please enter Exempt or Awaiting Inspection");
					scanner.nextLine();
					specialValue = scanner.nextLine();
					int temp = 0;
				    if(specialValue.equalsIgnoreCase("Exempt")) temp = -111;
				    else if(specialValue.equalsIgnoreCase("Awaitng Inspection")) temp = -666;
					running = false;
					for (FoodSafetyData data : this.foodSafetyList) {
						int hygieneValue = getRatingValue(data);
						if (data.getBusinessName().equalsIgnoreCase(inputName)&& hygieneValue != -999 && hygieneValue == temp) {
							tempList.add(data);
						}
					}
					break;
				}
				
			} while (running); // Added this loop to stop unnecessary calls to showMenu (adding calls to stack)

		
		
		
		
		
			if (tempList.size() < 0) {
			System.out.println("No business with that name found");
			return;
		}

		// Date sorter
		final Comparator<FoodSafetyData> dateSorter = new Comparator<FoodSafetyData>() {
			@Override
			public int compare(FoodSafetyData o1, FoodSafetyData o2) {
				if (o1.ratingDate == null || o2.ratingDate == null) {
					return -1;
				} else if (o1.ratingDate.before(o2.ratingDate)) {
					return 0;
				} else if (o1.ratingDate.after(o2.ratingDate)) {
					return -1;
				}

				return 0;

			}
		};

		// Sort out list chronologically
		Collections.sort(tempList, dateSorter);

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E dd/MM/yyyy");

		System.out.println("Display Business name for: " + inputName);

		String heading = "| Rating Date         | Hygiene Score | Structural Score | Confidence in Management Score | Food Hygiene Rating |";
		String top = printSeperator(heading, '┌', '┐');;
		String seperator = printSeperator(heading, '|', '|');
		String bot = printSeperator(heading, '└', '┘');;

		System.out.println(top);
		System.out.println(heading);
		System.out.println(seperator);

		for (FoodSafetyData data : tempList) {
			Date date = data.ratingDate;
			String dateFormat = "Date Not Avaliable";
			if (date != null) {
				dateFormat = DATE_FORMAT.format(date);
			}
			System.out.printf("| %-19s | %-13s | %-16s | %-30s | %-19s |\n", dateFormat, data.hygieneScore,
					data.structuralScore, data.confidenceInManagementScore, data.ratingValue);
		}
		System.out.println(bot);

	}
		
	
	
	
	private String printSeperator(String str, char start, char end) {
		int length = str.length();
		StringBuilder sb = new StringBuilder();
		sb.append(start);
		for (int i = 0; i < length - 2; i++) {
			sb.append("-");
		}
		sb.append(end);
		return sb.toString();
	}

	/**
	 * Create a menu and return the users input as a char. Input validation already
	 * occurs in here.
	 *
	 * @param options String array of options the user has
	 * @return char The users (validated) input - to be handled perhaps in a switch
	 *         statement.
	 */

	private char shortMenu(String[] options) {
		char menuChoice = ' ';
		int choiceInt = 0;

		System.out.println("\n");
		// print the menu options array
		for (int i = 0; i < options.length; i++)
			System.out.println(" " + (i + 1) + ". " + options[i]);
		System.out.print("\nChoose one: ");

		do {

			// read user input (and error check)
			try {
				menuChoice = (char) System.in.read();
			} catch (IOException e) {
				System.out.println("Error: failed to read menuChoice");
				e.printStackTrace();
			}

			// covert the users input from char to digit for comparison below
			if (Character.isDigit(menuChoice))
				choiceInt = Character.getNumericValue(menuChoice);

			// while condition checks the input is within the range of options provided
		} while (choiceInt < 1 || choiceInt > options.length);

		return menuChoice;
	}

}
