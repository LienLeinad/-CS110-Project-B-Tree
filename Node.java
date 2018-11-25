import java.util.*;
import java.io.*;
public class Node
{
	// made everything static so that I could test it in the main method
	// otherwise, i think keyOffset is the only thing that should be static since it's global for all Nodes
	private static long[] keyArray;
	private static int keyCount;
	public static int keyOffset;
	
	public Node()
	{
		// no parameters - empty node
		// haven't taken care of splitting case yet
		// node record consists of 14 "pointers" of type long int
		keyArray = new long[14];
		// initialize -1 values within the long[] array
		for (int i = 0; i < 14; i++)
		{
			keyArray[i] = -1;
		}
		// set starting values for keyCount and keyOffset
		keyCount = 0;
		keyOffset = 0;
	}
	
	public static void insertKey(long keyVal)
	{
		if(keyCount == 0) // case for empty Node
		{
			// set values for key and offset in the "first" location
			keyArray[2] = keyVal;
			keyArray[3] = keyOffset;
			// increment offset and key counter
			keyOffset+=1;
			keyCount+=1;
			System.out.println("Key inserted");
		}
		else if (keyCount < 4) // case for less than 4 keys
		{
			// j refers to the index of the rightmost keyArray
			// 3x-1 is used as the locations of the first 4 keys within the array are 2, 5, 8, and 11 respectively
			int j = ((3*keyCount)-1);
			while (j >= 2 && keyArray[j] > keyVal) // while loop to move larger values to the right while locating the position for the current key to be inserted
			{
				// this moving and locating algorithm is based off of geeksforgeeks' BTree insertNonFull method
				// website link: https://www.geeksforgeeks.org/b-tree-set-1-insert-2/
				keyArray[j+3] = keyArray[j];
				keyArray[j+4] = keyArray[j+1];
				j = j - 3;
				System.out.println("Rearranged successfully");
			}
			// place value to be inserted and offset to be inserted in their proper place
			keyArray[j+3] = keyVal;
			keyArray[j+4] = keyOffset;
			// increment offset and key counter
			keyCount+=1;
			keyOffset+=1;
			System.out.println("Key inserted");
		}
		else if (keyCount == 4)
		{
			keyArray[13] = keyVal;
			System.out.println("Key inserted, node full! No split handling yet!");
		}
	}
	
	public static void main(String[] args)
	{
		Node testNode = new Node();
		// these values are the ones found in the PowerPoint prior to the splitting
		// you can crosscheck the final printed array with the one in the PowerPoint
		Node.insertKey(8);
		Node.insertKey(11);
		Node.insertKey(6);
		Node.insertKey(14);
		System.out.println(Arrays.toString(keyArray));
		System.out.println("Number of keys: " + keyCount);
		System.out.println("Current offset: " + keyOffset);
	}
}