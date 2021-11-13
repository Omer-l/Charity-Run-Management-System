
import charityrun.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationRunner
{

	//All marathons, including inactive events.
	private static ArrayList<HalfMarathon> halfMarathons = new ArrayList<>();
	private static ArrayList<FiveKmRun> fiveKmRuns = new ArrayList<>();
	// Set of ALL competitors for non duplicate names.
	private static Set<String> competitors = new HashSet<String>();
	//ArrayList keep track of venues to display
	public static ArrayList<VenueToDisplay> venueToDisplay = new ArrayList<>();
	public static ArrayList<HalfMarathon> halfMarathonsToDisplay = new ArrayList<>();
	public static ArrayList<FiveKmRun> fiveKmRunsToDisplay = new ArrayList<>();

	//error messages
	private static final int SLEEP_TIME = 1000;
	private static final String ERR_MSG_THREAD_SLEEP = "\nINTERRUPTED EXCEPTION";
	private static final String ERR_MSG_INVALID_INPUT = "\nINVALID INPUT";
	private static final String ERR_MSG_FILE_NOT_FOUND = "\nFILE NOT FOUND";
	private static final String ERR_MSG_STRING_OUT_OF_BOUNDS = "\nPLEASE ENTER A SENSIBLE SEARCH!";

	public static void main(String[] args)
	{
		//initialise the ArrayLists of objects of charity runs.
		loadData();
		venueListToBeDisplayed();
		initialiseEventsToDisplayed();

		prompt();

	}

	private static void prompt()
	{
		Scanner input = new Scanner(System.in);
		System.out.print("\nSelect one of the following options:"
			+ "\n--------------------------------------"
			+ "\nSelect From All Events...............1"
			+ "\nList a Venue's Events................2"
			+ "\nSearch Competitor’s Event Entries....3"
			+ "\nExit.................................0"
			+ "\nEnter option number here: ");
		String userInput = input.next();
		outputAfterPrompt(userInput);
	}

	//display venues
	public static void outputAfterPrompt(String pick)
	{

		Scanner searchInput = new Scanner(System.in);
		try
		{
			int pickToInt = Integer.parseInt(pick);

			//minimum age for marathon, Create runner and add to arraylist of competitors.
			if (pickToInt == 1)
			{
				eventListToBeDisplayed();

				//prompt to pick event from list.
				System.out.print("Select one of the options to display the event details. > ");
				String userIn = searchInput.nextLine();
				System.out.print("\n");

				try
				{
					pickToInt = Integer.parseInt(userIn);
					if (pickToInt > 0 && pickToInt <= halfMarathonsToDisplay.size() + fiveKmRunsToDisplay.size())
					{
						if (pickToInt <= halfMarathonsToDisplay.size())
						{
							System.out.print(halfMarathonsToDisplay.get(pickToInt - 1).toString());
						} else if (pickToInt >= halfMarathonsToDisplay.size() && pickToInt <= halfMarathonsToDisplay.size() + fiveKmRunsToDisplay.size())
						{
							pickToInt -= halfMarathonsToDisplay.size() + 1;
							System.out.print(fiveKmRunsToDisplay.get(pickToInt).toString());
						}
					} else
					{
						System.out.print("\n*********\nPick a valid event!\n*********\n");
						pause1second();
					}

				} catch (NumberFormatException nfe)
				{
					System.out.println(ERR_MSG_INVALID_INPUT);
					pause1second();
					prompt();
				}
				prompt();
			} else if (pickToInt == 2)
			{

				displayVenueDetails();

				System.out.print("Select one of the options to display the events at the venue. > ");
				String userIn = searchInput.nextLine();
				System.out.print("\n");

				try
				{
					pickToInt = Integer.parseInt(userIn);
					if (pickToInt > 0 && pickToInt <= venueToDisplay.size())
					{
						String chosenVenue = venueToDisplay.get(pickToInt - 1).getVenue();
						chosenVenueEvents(chosenVenue);
					} else
					{
						System.out.print("\n*********\nPick a valid venue!\n*********\n");
						pause1second();
					}

				} catch (NumberFormatException nfe)
				{
					System.out.println(ERR_MSG_INVALID_INPUT);
					pause1second();
					prompt();
				}

				prompt();
			} else if (pickToInt == 3)
			{

				System.out.print("Enter name or part of the name > ");
				String userIn = searchInput.nextLine();
				System.out.print("\n");

				//if string is not sensible...
				try
				{
					searchCompetitor(userIn);
				} catch (StringIndexOutOfBoundsException stobe)
				{
					System.out.print(ERR_MSG_STRING_OUT_OF_BOUNDS);
					pause1second();
				}
				prompt();
			} else if (pickToInt == 0)
			{
				System.out.print("\nBye..\n");
				System.exit(0);
			} else
			{
				System.out.println(ERR_MSG_INVALID_INPUT);
				pause1second();
				prompt();
			}
		} catch (NumberFormatException nfe)
		{
			System.out.println(ERR_MSG_INVALID_INPUT);
			pause1second();
			prompt();
		}
	}

	private static void initialiseEventsToDisplayed()
	{

		for (HalfMarathon toDisplay : halfMarathons)
		{
			if (toDisplay.isIsEventOn())
			{
				halfMarathonsToDisplay.add(toDisplay);
			}
		}

		for (FiveKmRun toDisplay : fiveKmRuns)
		{
			if (toDisplay.isIsEventOn())
			{
				fiveKmRunsToDisplay.add(toDisplay);
			}
		}
	}

	public static void eventListToBeDisplayed()
	{

		int eventCounter = 0;
		//venueName to compare with chosenVenue
		String venueName;

		//Display Table header
		System.out.print("\n");
		for (int i = 0; i < 1 + 9 + 1 + 20 + 1 + 20 + 1 + 25 + 1; i++)
		{
			System.out.print("-");
		}
		System.out.print("\n");

		System.out.printf("%-1s%-9s%-1s%-20s%-1s%-20s%-1s%-25s%-1s\n".toUpperCase(),
			"|",
			" No. ",
			"|",
			" Charity Run Type ",
			"|",
			"        Date",
			"|",
			"          Venue",
			"|");

		for (int i = 0; i < 1 + 9 + 1 + 20 + 1 + 20 + 1 + 25 + 1; i++)
		{
			System.out.print("-");
		}
		System.out.print("\n");

		//display contents of Marathons in rows first.
		for (int i = 0; i < halfMarathons.size(); i++)
		{

			venueName = getMarathonEventVenueName(i);

			if (halfMarathons.get(i).isIsEventOn())
			{
				eventCounter++;
				System.out.printf("%-1s%-9s%-1s%-20s%-1s%-20s%-1s%-25s%-1s\n",
					"|",
					eventCounter,
					"|",
					" " + RunType.HALF_MARATHON,
					"|",
					halfMarathons.get(i).getDate(),
					"|",
					" " + venueName,
					"|");
				for (int j = 0; j < 1 + 9 + 1 + 20 + 1 + 20 + 1 + 25 + 1; j++)
				{
					System.out.print("-");
				}
				System.out.print("\n");
			}
		}
		//contents of five km runs.
		for (int i = 0; i < fiveKmRuns.size(); i++)
		{
			venueName = fiveKmRuns.get(i).getVenue().getName();

			if (fiveKmRuns.get(i).isIsEventOn())
			{
				eventCounter++;

				System.out.printf("%-1s%-9s%-1s%-20s%-1s%-20s%-1s%-25s%-1s\n",
					"|",
					eventCounter,
					"|",
					" " + RunType.FIVE_KM_FUN_RUN,
					"|",
					fiveKmRuns.get(i).getDate(),
					"|",
					" " + venueName,
					"|");
				for (int j = 0; j < 1 + 9 + 1 + 20 + 1 + 20 + 1 + 25 + 1; j++)
				{
					System.out.print("-");
				}
				System.out.print("\n");
			}
		}
	}

	public static void venueListToBeDisplayed()
	{

		String venueName;

		//function to fill in the venueToDisplay
		for (int i = 0; i < halfMarathons.size(); i++)
		{
			//get marathon event's venue name
			venueName = getMarathonEventVenueName(i);

			//is the event on in that venue?
			if (halfMarathons.get(i).isIsEventOn() == false)
			{
				boolean eventIsAlreadyListed = false;
				for (int j = 0; j < venueToDisplay.size(); j++)
				{

					if (venueToDisplay.get(j).getVenue().equals(venueName))
					{
						eventIsAlreadyListed = true;
					}
				}
				if (eventIsAlreadyListed == true)
				{
					continue;
				} else
				{
					venueToDisplay.add(new VenueToDisplay(venueName, 0));
					continue;
				}
			}

			if (i == 0 && venueToDisplay.size() == 0)
			{
				venueToDisplay.add(new VenueToDisplay(venueName, 0));
			}
			//duplicate same venue in halfmarathons? increase numbers of events.
			for (int j = 0; j < venueToDisplay.size(); j++)
			{
				if (venueToDisplay.get(j).getVenue().equals(venueName))
				{
					venueToDisplay.get(j).setNumberOfEvents();
					break;
				} else if (j == venueToDisplay.size() - 1)
				{
					venueToDisplay.add(new VenueToDisplay(venueName, 1));
					break;
				}
			}
		}
		//With towns, check if its also in the marathon, if so, then numberOfEvents++
		for (int i = 0; i < fiveKmRuns.size(); i++)
		{
			venueName = fiveKmRuns.get(i).getVenue().getName();
			//is the event on in that venue?
			if (fiveKmRuns.get(i).isIsEventOn() == false)
			{
				boolean eventIsAlreadyListed = false;
				for (int j = 0; j < venueToDisplay.size(); j++)
				{

					if (venueToDisplay.get(j).getVenue().equals(venueName))
					{
						eventIsAlreadyListed = true;
					}
				}
				if (eventIsAlreadyListed == true)
				{
					continue;
				} else
				{
					venueToDisplay.add(new VenueToDisplay(venueName, 0));
					continue;
				}
			}
			for (int j = 0; j < venueToDisplay.size(); j++)
			{
				if (venueToDisplay.get(j).getVenue().equals(venueName))
				{
					venueToDisplay.get(j).setNumberOfEvents();
					break;
				} else if (j == venueToDisplay.size() - 1)
				{
					venueToDisplay.add(new VenueToDisplay(venueName, 1));
					break;
				}
			}
		}
	}

	public static void displayVenueDetails()
	{

		//table headers
		System.out.print("\n");
		for (int i = 0; i < 1 + 9 + 1 + 25 + 1 + 15 + 1; i++)
		{
			System.out.print("-");
		}
		System.out.print("\n");

		System.out.printf("%-1s%-9s%-1s%-25s%-1s%-15s%-1s\n".toUpperCase(), "|", " No. ", "|", " Venue", "|", " No. of Events", "|");
		for (int i = 0; i < 1 + 9 + 1 + 25 + 1 + 15 + 1; i++)
		{
			System.out.print("-");
		}
		System.out.print("\n");
		//two for loops to display towns and parks
		//display contents of table
		for (int i = 0; i < venueToDisplay.size(); i++)
		{
			System.out.printf("%-1s%-9s%-1s%-25s%-1s%-15s%-1s\n", "|", i + 1, "|", venueToDisplay.get(i).getVenue(), "|", venueToDisplay.get(i).getNumberOfEvents(), "|");

			for (int j = 0; j < 1 + 9 + 1 + 25 + 1 + 15 + 1; j++)
			{
				System.out.print("-");
			}
			System.out.print("\n");
		}
	}

	private static void pause1second()
	{
		try
		{
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException ie)
		{
			System.out.println(ERR_MSG_THREAD_SLEEP);
		}
	}

	//album file path, can stay private.
	private final static String charityRunFilePath = System.getProperty("user.dir") + File.separator + "charity_runs.txt";

	private static void loadData()
	{
		//object of the loaded file
		File dataObject = new File(charityRunFilePath);

		//Try to access file
		try
		{
			Scanner fileRead = new Scanner(dataObject);

			int lineCount = 1;
			//Loop to read.
			while (fileRead.hasNextLine())
			{
				try
				{
					//store line into currentLine variable
					String currentLine = fileRead.nextLine();

					//split ','
					String[] splitString = currentLine.split("[,]");

					//get temporary values from the split elements
					String charityRun = splitString[0];
					String tmpCharityRunName = splitString[1];
					int tmpNumOfChangingFacilities = Integer.parseInt(splitString[2]);
					int tmpNumOfWaterStations = Integer.parseInt(splitString[3]);
					String tmpDate = splitString[4];
					String tmpStartTime = splitString[5];

					//6th splitted part is the list of competitors.
					String competitorsData = splitString[6];
					String[] splitCompetitors = competitorsData.split("[|]");
					Set<String> competitorSetThisEvent = new HashSet();

					boolean isEventOn;
					if (eventIsOn(tmpDate + " " + tmpStartTime))
					{
						isEventOn = true;
					} else
					{
						isEventOn = false;
					}
					
					//validates date and start time.
					if(!isLineValid(tmpDate, tmpStartTime))
					{
						System.out.print("\nCharity_runs.txt line number: " + lineCount + "'s date or time is incorrect as it was not added to the event list\n");
						continue;
					}
					
					//create object charity run object and fill with all the temporary attributes
					//if new line was a marathon then save as marathon, or vice versa if it was a five km run.
					if (charityRun.contains("Half Marathon"))
					{
						HalfMarathon newHalfMarathon = new HalfMarathon(tmpCharityRunName, tmpNumOfWaterStations, tmpNumOfChangingFacilities, tmpDate, tmpStartTime, isEventOn);

						//fill competitors into ArrayList tmpCompetitors
						for (int i = 0; i < splitCompetitors.length; i++)
						{
							String[] splitNameAndAge = splitCompetitors[i].split("[;]");
							String competitorName = splitNameAndAge[0];
							int age = Integer.parseInt(splitNameAndAge[1]);

							if (isCompetitorExist(competitorSetThisEvent, splitNameAndAge, splitCompetitors, i))
							{
								System.out.print("\n" + competitorName + " age: " + age + " is already in this event and so was not allowed to be entered again " + RunType.HALF_MARATHON + " " + tmpCharityRunName + "\n---");
								continue;
							}
                                                        //to not add competitors from events that have are in the past and not active. To not add competitors under 16
							if (age >= 16 && isEventOn)
							{
								addMarathonCompetitor(newHalfMarathon, competitorName, age);
								competitorSetThisEvent.add(competitorName + ";" + age);
								competitors.add(competitorName + ";" + age);
							} else
							{
								System.out.print("\n" + competitorName + "'s age is " + age + " and is below the minimum age 16 and was therefore not added to the list of competitors for " + RunType.HALF_MARATHON + " " + tmpCharityRunName + ".\n---");
								continue;
							}
						}
						//add new object to arraylist of marathons
						halfMarathons.add(newHalfMarathon);

					} else if (charityRun.contains("Five Km Run"))
					{
						FiveKmRun newFiveKmRun = new FiveKmRun(tmpCharityRunName, tmpNumOfWaterStations, tmpDate, tmpStartTime, isEventOn);

						//fill competitors into ArrayList tmpCompetitors
						for (int i = 0; i < splitCompetitors.length; i++)
						{
							String[] splitNameAndAge = splitCompetitors[i].split("[;]");
							String competitorName = splitNameAndAge[0];
							int age = Integer.parseInt(splitNameAndAge[1]);

							if (isCompetitorExist(competitorSetThisEvent, splitNameAndAge, splitCompetitors, i))
							{
								System.out.print("\n" + competitorName + " age: " + age + " is already in this event and so was not allowed to be entered again for " + RunType.FIVE_KM_FUN_RUN + " " + tmpCharityRunName + "\n---");
								continue;
							}

							addFiveKmRunCompetitor(newFiveKmRun, competitorName, age);
							competitorSetThisEvent.add(competitorName + ";" + age);
                                                        //to not add competitors from events that have are in the past and not active.
                                                        if(isEventOn)
							competitors.add(competitorName + ";" + age);
						}
						//add the new charityRun object to ArrayList of fiveKmRuns
						fiveKmRuns.add(newFiveKmRun);

					}
					lineCount++;
				} catch(NumberFormatException nfe)
				{
					System.out.print("\nLine number " + lineCount + " in file charity_runs.txt has an incorrect format. Please ensure the format is as follows: "
						+ "\n<Half Marathon OR Five Km Run>,<location>,<changing rooms (-1 IF TOWN)>,<no. of water facilities>,<date>,<start time>,<competitor name;age>|<second competitor name:age>\n");
					continue;
				}
			}

		} //If file was not found...
		catch (FileNotFoundException fnfe)
		{
			System.out.println(ERR_MSG_FILE_NOT_FOUND);
		}
	}

	private static Boolean isCompetitorExist(Set<String> competitorSetThisEvent, String[] splitNameAndAge, String[] splitCompetitors, int i)
	{
		splitNameAndAge = splitCompetitors[i].split("[;]");
		String competitorName = splitNameAndAge[0];
		int age = Integer.parseInt(splitNameAndAge[1]);

		boolean isCompetitorAlreadyExistInEvent = false;

		for (String tmpCompetitorName : competitorSetThisEvent)
		{
			String[] splitNameAndAgeTmpCompetitor = tmpCompetitorName.split("[;]");
			tmpCompetitorName = splitNameAndAgeTmpCompetitor[0];
			int tmpCompAge = Integer.parseInt(splitNameAndAgeTmpCompetitor[1]);
			if (tmpCompetitorName.equals(competitorName) && tmpCompAge == age)
			{
				isCompetitorAlreadyExistInEvent = true;
				break;
			}
		}

		return isCompetitorAlreadyExistInEvent;
	}

	private static String getMarathonEventVenueName(int i)
	{
		if (halfMarathons.get(i).getVenueTown() == null)

		{
			return halfMarathons.get(i).getVenuePark().getName();
		} else

		{
			return halfMarathons.get(i).getVenueTown().getName();
		}

	}

	//display chosen venue's events
	private static void chosenVenueEvents(String chosenVenue)
	{
		System.out.print("**** " + chosenVenue.toUpperCase() + " ****");
		//Display Table header
		System.out.print("\n");
		for (int i = 0; i < 1 + 9 + 1 + 20 + 1 + 25 + 1 + 15 + 1 + 9 + 1 + 27 + 1 + 27 + 1; i++)
		{
			System.out.print("-");
		}
		System.out.print("\n");

		System.out.printf("%-1s%-9s%-1s%-20s%-1s%-25s%-1s%-15s%-1s%-9s%-1s%-27s%-1s%-27s%-1s\n".toUpperCase(),
			"|",
			" No. ",
			"|",
			" Charity Run Type ",
			"|",
			" Date",
			"|",
			"  Start Time",
			"|",
			" Entries",
			"|",
			" No. of Water Stations",
			"|",
			" No. of Changing Facilities",
			"|");

		for (int i = 0; i < 1 + 9 + 1 + 20 + 1 + 25 + 1 + 15 + 1 + 9 + 1 + 27 + 1 + 27 + 1; i++)
		{
			System.out.print("-");
		}
		System.out.print("\n");

		//venueName to compare with chosenVenue
		String venueName;
		int eventCounter = 0;

		//display contents of Marathons in rows first.
		for (int i = 0; i < halfMarathons.size(); i++)
		{
			venueName = getMarathonEventVenueName(i);

			if (venueName.equals(chosenVenue) && halfMarathons.get(i).isIsEventOn())
			{
				eventCounter++;

				System.out.printf("%-1s%-9s%-1s%-20s%-1s%-25s%-1s%-15s%-1s%-9s%-1s%-27s%-1s%-27s%-1s\n",
					"|",
					eventCounter,
					"|",
					" " + RunType.HALF_MARATHON,
					"|",
					" " + halfMarathons.get(i).getDate(),
					"|",
					"     " + halfMarathons.get(i).getStartTime(),
					"|",
					" " + halfMarathons.get(i).getCompetitors().size(),
					"|",
					" " + ((halfMarathons.get(i).getVenueName().contains("Park")) ? " " + halfMarathons.get(i).getNumWaterStations() : halfMarathons.get(i).getNumWaterStations()),
					"|",
					((halfMarathons.get(i).getVenueName().contains("Park")) ? " " + halfMarathons.get(i).getVenuePark().getNumChangingFacilities() : "XXXXXXXXXXXXXXXXXXXXXXXXXXX"),
					"|");
				for (int j = 0; j < 1 + 9 + 1 + 20 + 1 + 25 + 1 + 15 + 1 + 9 + 1 + 27 + 1 + 27 + 1; j++)
				{
					System.out.print("-");
				}
				System.out.print("\n");
			}
		}
		//contents of five km runs.
		for (int i = 0; i < fiveKmRuns.size(); i++)
		{
			venueName = fiveKmRuns.get(i).getVenue().getName();

			if (venueName.equals(chosenVenue) && fiveKmRuns.get(i).isIsEventOn())
			{
				eventCounter++;
				System.out.printf("%-1s%-9s%-1s%-20s%-1s%-25s%-1s%-15s%-1s%-9s%-1s%-27s%-1s%-27s%-1s\n",
					"|",
					eventCounter,
					"|",
					" " + RunType.FIVE_KM_FUN_RUN,
					"|",
					" " + fiveKmRuns.get(i).getDate(),
					"|",
					"     " + fiveKmRuns.get(i).getStartTime(),
					"|",
					" " + fiveKmRuns.get(i).getCompetitors().size(),
					"|",
					"XXXXXXXXXXXXXXXXXXXXXXXXXXX",
					"|",
					" " + fiveKmRuns.get(i).getVenue().getNumChangingFacilities(),
					"|");
				for (int j = 0; j < 1 + 9 + 1 + 20 + 1 + 25 + 1 + 15 + 1 + 9 + 1 + 27 + 1 + 27 + 1; j++)
				{
					System.out.print("-");
				}
				System.out.print("\n");
			}
		}
		if (eventCounter == 0)
		{
			System.out.print("\n\t\tTHERE ARE CURRENTLY NO EVENTS AT THIS VENUE\t\tTHERE ARE CURRENTLY NO EVENTS AT THIS VENUE\n");

		}
	}

	private static void searchCompetitor(String input)
	{
		//is the Competitor from another venue?
		boolean competitorAlreadyDisplayed = false;

		input = input.toLowerCase();
		String userInputFirstCharToUpperCase = "";

		//if it's just one letter, just make it a capital letter, if its more than one letter, apply and add substring method after making first letter capital letter of each word
		//split spaces to make each word first letter upper case
		String[] userInputFirstCharToUpperCaseSplit = input.split(" ");
		if (input.length() > 1)
		{
			for (int i = 0; i < userInputFirstCharToUpperCaseSplit.length; i++)
			{
				userInputFirstCharToUpperCaseSplit[i] = userInputFirstCharToUpperCaseSplit[i].trim();

				userInputFirstCharToUpperCaseSplit[i] = Character.toUpperCase(userInputFirstCharToUpperCaseSplit[i].charAt(0)) + userInputFirstCharToUpperCaseSplit[i].substring(1);
				userInputFirstCharToUpperCase += userInputFirstCharToUpperCaseSplit[i] + " ";
			}
		} else
		{
			userInputFirstCharToUpperCase = Character.toUpperCase(input.charAt(0)) + "";
		}
		userInputFirstCharToUpperCase = userInputFirstCharToUpperCase.trim();
		//ArrayList for checking whether the next song is from an already found artist's song.
		ArrayList<String> alreadyMatched = new ArrayList<>();
		
		// Iterating though the Set
		for (String competitorName : competitors)
		{
			String[] splitString = competitorName.split("[;]");
			competitorName = splitString[0];
			String competitorAge = splitString[1];

			if (competitorName.contains(userInputFirstCharToUpperCase) || competitorName.contains(input))
			{
				System.out.print("\n\n/////////////////////////////////\n");
				System.out.print("COMPETITOR NAME:		" + competitorName + "\nCOMPETITOR AGE:			" + competitorAge + "\n/////////////////////////////////\nEvents participated in".toUpperCase());

				//go through searching + matching already matched names in marathons.
				for (int i = 0; i < halfMarathons.size(); i++)
				{
					if (halfMarathons.get(i).isIsEventOn())
					{
						String venueName = getMarathonEventVenueName(i);
						boolean isCompetitorInThisEvent = false;
						int competitorEntryNumberForThisEvent = 0;

						//check event's competitors
						for (int j = 0; j < halfMarathons.get(i).getCompetitors().size(); j++)
						{
							String currentCompetitorName = halfMarathons.get(i).getCompetitors().get(j).getName();
							if (currentCompetitorName.equals(competitorName))
							{
								isCompetitorInThisEvent = true;
								competitorEntryNumberForThisEvent = halfMarathons.get(i).getCompetitors().get(j).getEntryNumber();
							}
						}
						if (isCompetitorInThisEvent)
						{
							System.out.print("\n---------------------------------\n");
							System.out.print(halfMarathons.get(i).toString());
							System.out.print(competitorName + "'s Entry Number is: " + (competitorEntryNumberForThisEvent + 1));
						}
					}
				}
				//go through searching + matching names in fivekmruns.
				for (int i = 0; i < fiveKmRuns.size(); i++)
				{
					if (fiveKmRuns.get(i).isIsEventOn())
					{
						String venueName = fiveKmRuns.get(i).getVenue().getName();
						boolean isCompetitorInThisEvent = false;
						int competitorEntryNumberForThisEvent = 0;

						//check event's competitors
						for (int j = 0; j < fiveKmRuns.get(i).getCompetitors().size(); j++)
						{
							String currentCompetitorName = fiveKmRuns.get(i).getCompetitors().get(j).getName();
							if (currentCompetitorName.equals(competitorName))
							{
								isCompetitorInThisEvent = true;
								competitorEntryNumberForThisEvent = fiveKmRuns.get(i).getCompetitors().get(j).getEntryNumber();
							}
						}
						if (isCompetitorInThisEvent)
						{
							System.out.print("\n---------------------------------\n");
							System.out.print(fiveKmRuns.get(i).toString());
							System.out.print(competitorName + "'s Entry Number is: " + (competitorEntryNumberForThisEvent + 1));
						}
					}
				}

				System.out.print("\n====================================="
					+ "\n----------|>END OF EVENTS<|----------"
					+ "\n=====================================");
			} else if (competitorName.contains(input))
			{
//				System.out.print(competitorName + ", "); DELETE
			}
		}
		System.out.println();

	}

	public static boolean eventIsOn(String eventDate)
	{
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			Date date = new Date();
			String currentDate = formatter.format(date);
			Date eventDateAndTime = formatter.parse(eventDate);

			if (date.compareTo(eventDateAndTime) > 0)
			{
				return false;
			} else if (date.compareTo(eventDateAndTime) < 0)
			{
				return true;
		 	} else
			{
				return true;
			}
		} catch (ParseException pe)
		{
			System.out.println("Text parse error. Event was not added to active events: Check file charity_runs.txt. The date and time is incorrect format.");
			return false;
		}
	}

	public static void addMarathonCompetitor(HalfMarathon marathon, String competitorName, int age)
	{
		marathon.getCompetitors().add(new Competitor(competitorName, age, marathon.getEntryNumber().getEventNumber()));
	}

	public static void addFiveKmRunCompetitor(FiveKmRun fiveKmRun, String CompetitorName, int age)
	{
		fiveKmRun.getCompetitors().add(new Competitor(CompetitorName, age, fiveKmRun.getEntryNumber().getEventNumber()));
	}
	
	public static boolean isLineValid(String date, String time)
	{
		String[] dateSplit = date.split("[/]");
		String[] timeSplit = time.split("[:]");
		
		if(Integer.parseInt(dateSplit[0]) < 0 || Integer.parseInt(dateSplit[0]) > 31 || Integer.parseInt(dateSplit[1]) < 0 || Integer.parseInt(dateSplit[1]) > 12 || Integer.parseInt(dateSplit[2]) < 1000 || Integer.parseInt(timeSplit[0]) < 0 || Integer.parseInt(timeSplit[0]) > 24 || Integer.parseInt(timeSplit[1]) < 0 || Integer.parseInt(timeSplit[1]) > 59)
			return false;
		else
			return true;
	}
}
