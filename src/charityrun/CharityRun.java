package charityrun;

import java.util.ArrayList;

abstract public class CharityRun 
{
	String date;
	String startTime;
	ArrayList<Competitor> competitors;
	RunEntry entryNumber;
	boolean isEventOn;

	public CharityRun(String date, String startTime, boolean isEventOn)
	{
		this.date = date;
		this.startTime = startTime;
		this.competitors = new ArrayList<>();
		this.entryNumber = new RunEntry();
		this.isEventOn = isEventOn;
	}
	
	public String getDate()
	{
		return this.date;
	}
	
	public String getStartTime()
	{
		return this.startTime;
	}

	public RunEntry getEntryNumber()
	{
		return entryNumber;
	}

	public ArrayList<Competitor> getCompetitors()
	{
		return competitors;
	}

	public boolean isIsEventOn()
	{
		return isEventOn;
	}
	
}
