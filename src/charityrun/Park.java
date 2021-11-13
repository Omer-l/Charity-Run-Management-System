package charityrun;

public class Park extends Venue
{

	int numChangingFacilities;

	// constructor 
	public Park(String name, int numChangingFacilities)
	{
		super(name);
		this.numChangingFacilities = numChangingFacilities;
	}

	public String getName()
	{
		return name;
	}

	public int getNumChangingFacilities()
	{
		return numChangingFacilities;
	}
}
