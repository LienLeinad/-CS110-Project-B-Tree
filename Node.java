import java.util.*;
import java.io.*;
public class Node
{
	// made everything static so that I could test it in the main method
	// otherwise, i think is the only thing that should be static since it's global for all Nodes
	private long[] keyArray;
	private int keyCount;

	
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
		// set starting values for keyCount
		keyCount = 0;
	
	}
	
	public Node(long[] nums){
		// take nums as the array of the node object
		keyArray = nums;
		// count how many are not -1 in the keys to set keyCount
		for (int i = 2; i < keyArray.length; i += 2 ) {
			if(keyArray[i] != (long)-1){
				keyCount++;
			}
		}



	}

	public void insert(long keyVal, long keyOffset)throws SameKeyException{
		if(keyCount == 0) // case for empty Node
		{
			// set values for key and offset in the "first" location
			keyArray[2] = keyVal;
			keyArray[3] = keyOffset;
			keyCount++;
			// System.out.println("key: " + keyVal + " offSet num: " + keyOffset + " inserted");
		}
		else if (keyCount < 4) // case for less than 4 keys
		{
			try{
				boolean hasSameKey = false;
				//go through entire array of Keys and check for if the keyVale is equal to any of them
				for (int i = 2; i < 13; i += 3 ) {
					if (keyArray[i] == keyVal){
						hasSameKey = true;
						break;
					}
				}
				if(hasSameKey){
					throw new SameKeyException();
				}
				else{
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
						// System.out.println("Rearranged successfully");
					}
					// place value to be inserted and offset to be inserted in their proper place
					keyArray[j+3] = keyVal;
					keyArray[j+4] = keyOffset;
					// increment offset and key counter
					keyCount+=1;
					// System.out.println("key: " + keyVal + " offSet num: " + keyOffset + " inserted");
				}
					
			}catch(IndexOutOfBoundsException iobe){
				System.out.println("Too many key + objects inserted, please split");
			}
			
		}
		else if (keyCount == 4)
		{
			keyArray[13] = keyVal;
			System.out.println("Key inserted, node full! No split handling yet!");
		}

	}
	public long[] giveArray(){
		return keyArray;
	}
	public int search(long key) {
		long findKey = 0;
		long count = 1;
		long i = 0;

		while (i < 4) {
			// pointer to array
			findKey = ((3*count) - 1);
			// if findKey == key then break, you have the key
			if (keyArray[(int)findKey] == key)
				break;
			else {
				count++;
				i++;
			}
		}

		// returns the offset value
		return (int)(3 * count);
	}

	// for debugging purposes
	public void printArray(){
		System.out.println(Arrays.toString(keyArray));
	}
	

}