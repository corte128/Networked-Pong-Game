/*
This is a simple wrapper class for integers.
*/

public class intHolder
{
	private int value = 0;
	public intHolder()
	{
	}
	public intHolder(int value)
	{
		this.value = value;
	}
	public void setValue(int value)
	{
		this.value = value;
	}
	public int getValue()
	{
		return value;
	}
	public String toString()
	{
		return "" + value;
	}
}