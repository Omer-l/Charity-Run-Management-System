package charityrun;

import java.util.ArrayList;
import java.util.Arrays;

public class HalfMarathon extends CharityRun
{

	int numWaterStations;

	Town venueTown;
	Park venuePark;
	String venueName;
	//delete
	Venue venue;

	//args: venue, num water stations, date, start time
	public HalfMarathon(String venue, int numWaterStations, int numChangingFacilities, String date, String startTime, boolean isEventOn)
	{
		super(date, startTime, isEventOn);
		this.venueName = venue;

		this.numWaterStations = numWaterStations;
		//if numWaterStations is -1, then it is a park location.
		if (venue.contains("Park"))
		{
			this.venuePark = new Park(venue, numChangingFacilities);
		} else
		{
			this.venueTown = new Town(venue);
		}
	}

	public int getNumWaterStations()
	{
		return numWaterStations;
	}

	public Town getVenueTown()
	{
		return venueTown;
	}

	public Park getVenuePark()
	{
		return venuePark;
	}

	public String getVenueName()
	{
		return venueName;
	}

	
	@Override
	public String toString()
	{
		StringBuilder tmpString = new StringBuilder();

		//if it is a park
		if (this.venueName.contains("Park"))
		{
			tmpString.append("Event Venue:			" + this.getVenuePark().getName() + "\n");
			tmpString.append("Event Date:			" + getDate() + "\n");
			tmpString.append("Event Time:			" + getStartTime() + "\n");
			tmpString.append("Event Type:			" + RunType.HALF_MARATHON);
			tmpString.append("\nNo. of Changing Facilities:	" + this.getVenuePark().getNumChangingFacilities() + "\n");
			tmpString.append("No. of Water stations:		" + this.getNumWaterStations() + "\n");
		} else
		{
			tmpString.append("Event Venue:			" + this.getVenueTown().getName() + "\n");
			tmpString.append("Event Date:			" + getDate() + "\n");
			tmpString.append("Event Time:			" + getStartTime() + "\n");
			tmpString.append("Event Type:			" + RunType.HALF_MARATHON);
			tmpString.append("\nNo. of Water stations:		" + this.getNumWaterStations() + "\n");
		}

		tmpString.append("Total Entries:			" + this.getCompetitors().size() + "\n");

		return tmpString.toString();
	}

}
