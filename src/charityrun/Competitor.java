package charityrun;

public class Competitor
{

	String name;
	int age;
	int entryNumber;

	public Competitor(String name, int age, int entryNumber)
	{
		this.name = name;
		this.age = age;
		this.entryNumber = entryNumber;
	}

	public String getName()
	{
		return name;
	}

	public int getAge()
	{
		return age;
	}

	public int getEntryNumber()
	{
		return entryNumber;
	}
	
	@Override
	public String toString()
	{
		StringBuilder tmpString = new StringBuilder();
		
		tmpString.append("Competitor Name:		" + this.getName() +"\n");
		tmpString.append("Age:				" + this.getAge() + "\n");
		
		return tmpString.toString();
	}
}
