import java.util.*;
import java.io.*;
public class Node
{
	private long[] keyArray;
	
	public Node()
	{
		// node record consists of 14 "pointers" of type long int
		keyArray = new long[14];
		// initialize -1 values within the long[] array
		for (int i = 0; i < 14; i++)
		{
			keyArray[i] = -1;
		}
	}
}