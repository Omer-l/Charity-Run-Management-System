package charityrun;

public class VenueToDisplay
{
		String venue;
		int numberOfEvents;
		
	public VenueToDisplay(String venue, int numberOfEvents)
	{
		this.venue = venue;
		this.numberOfEvents = numberOfEvents;
	}

	public String getVenue()
	{
		return venue;
	}

	public int getNumberOfEvents()
	{
		return numberOfEvents;
	}

	public void setNumberOfEvents()
	{
		this.numberOfEvents++;
	}
	
	
}
