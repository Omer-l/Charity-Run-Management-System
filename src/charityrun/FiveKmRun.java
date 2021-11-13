package charityrun;

import java.util.ArrayList;
import java.util.Arrays;

public class FiveKmRun extends CharityRun
{

	Park venue;

	//args: venue, num changing facilities, date, start time
	public FiveKmRun(String venue, int numChangingFacilities, String date, String startTime, boolean isEventOn)
	{
		super(date, startTime, isEventOn);
		this.venue = new Park(venue, numChangingFacilities);
	}

	public Park getVenue()
	{
		return venue;
	}

	@Override
	public String toString()
	{
		StringBuilder tmpString = new StringBuilder();

		tmpString.append("Event Venue:			" + this.getVenue().getName() + "\n");
		tmpString.append("Event Date:			" + getDate() + "\n");
		tmpString.append("Event Time:			" + getStartTime() + "\n");
		tmpString.append("Event Type:			" + RunType.FIVE_KM_FUN_RUN);
		tmpString.append("\nNo. of Changing facilities:	" + this.getVenue().getNumChangingFacilities() + "\n");
		tmpString.append("Total Entries:			" + this.getCompetitors().size() + "\n");
		return tmpString.toString();
	}

}
