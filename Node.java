import java.util.*;
import java.io.*;
public class Node
{
	// made everything static so that I could test it in the main method
	// otherwise, i think is the only thing that should be static since it's global for all Nodes
	private long[] keyArray;
	private int keyCount;
	private long recNumb;
	private long[] excess;// if this is filled, then it needs to split
	private boolean isLeaf; // has no children, if this is false, then it has children
	private boolean isRoot; // has no parents, basically is batman
	private int numChild;
	private long excessChildOffset;
	public Node(long recNum)
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
		//set the record number for the node
		recNumb = recNum;
		//since it's an empty node, it is the first node in the b-tree and thus it is the root
		isRoot = true;
		// since it's the first node and it's empty, it has no children, it is a leaf
		isLeaf = true;
		//set excess to have empty variables
		excess = new long[2];
		excessChildOffset = -1;
	
	}
	
	public Node(long[] nums, long recNum){
		// take nums as the array of the node object
		keyArray = nums;
		// count how many are not -1 in the keys to set keyCount
		for (int i = 2; i < keyArray.length; i += 3 ) {
			if(keyArray[i] != (long)-1){
				keyCount++;
			}
		}
		//set the recordnumber for the node
		recNumb = recNum;
		// check if it has children, if it does, it is an internal node
		for (int i = 1;i < keyArray.length ; i += 3 ) {
			if(keyArray[i] != -1){
				isLeaf = false;
				break;
			}
			else{
				isLeaf = true;
			}
		}
		//check if it has a parent, if it does, it's not batman and is an internal node
		if(keyArray[0] != -1){
			isRoot = false;
		}else{
			isRoot = true;
		}
		excess = new long[2];
		excessChildOffset = -1;
	}

	public long getExcessChild(){
		return excessChildOffset;
	}

	public void setExcessChild(int offSet){
		excessChildOffset = offSet;
	}

	// returns keyCount
	public int getKeyCount(){
		return keyCount;
	}

	//returns true if it is a leaf, false if it is an internal node
	public boolean nodeIsLeaf(){
		return isLeaf;
	}

	public void emptyExcess(){
		excess[0] = -1;
		excess[1] = -1;
	}

	//returns true if it is a root, false if it has a parent and isn't batman
	public boolean nodeIsRoot(){
		return isRoot;
	}
	// returns recordNumber of leftChild, only to be used when number of nodes in btreemanager is greater than 1
	public long getLeftChild(long key){
		long offSet = -1;
		// look for key
		for (int i = 2; i < keyArray.length; i+=3 ) {
			if(keyArray[i] == key){
				// once its found return the value of the index before it
				offSet = keyArray[i-1];
			}
		}
		return offSet;
	}
	//returns recordNumber of rightChild
	public long getRightChild(long key){
		long offSet = -1;
		// look for key
		for (int i = 2; i < keyArray.length; i+=3 ) {
			if(keyArray[i] == key){
				// once its found return the value of the index before it
				offSet = keyArray[i+2];
			}
		}
		return offSet;
	}

	//sets left child of a given key
	public void setLeftChild(long key, long childOffset){
		// it's not a leaf anymore
		isLeaf = false;
		//look for the index number of the key
		int keyPointer = 2;
		while(key != keyArray[keyPointer]){
			keyPointer += 3;
		}
		//increment numChildren
		numChild++;
		// insert at the index before it the offset of the left child
		keyArray[keyPointer-1] = childOffset;

	}
	//sets right child of a given key
	public void setRightChild(long key, long childOffset){
		// tis not a leaf anymore
		isLeaf = false;
		//look for the index number of the key
		int keyPointer = 2;
		while(key != keyArray[keyPointer]){
			keyPointer += 3;
		}
		//increment numChildren
		numChild++;
		// insert at the index After it the offset of the right child
		keyArray[keyPointer+2] = childOffset;

	}

	public int getNumChild(){
		return numChild;
	}


	public long[] insert(long keyVal, long keyOffset)throws SameKeyException{
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
		else if (keyCount == 4)// splitting needed
		{
			boolean hasSameKey = false;
			for (int i = 2; i < 13; i += 3 ) {
					if (keyArray[i] == keyVal){
						hasSameKey = true;
						break;
					}
				}
				if(hasSameKey){
					throw new SameKeyException();
				}
			// check if the keyVal and offset should be in excess (meaning it's bigger than the biggest key in the node)
			if(keyVal > keyArray[11]){
				// if it is bigger, it belongs in excess
				excess[0] = keyVal;
				excess[1] = keyOffset;
			}
			else{// the last belongs in excess
				excess[0] = keyArray[11];
				excess[1] = keyArray[12];
				//set last to -1 again for inserting purposes
				keyArray[11] = -1;
				keyArray[12] = -1;
				keyCount--;
				//insert the keyVal in the array
				insert(keyVal,keyOffset);
			}
			

			// System.out.println("Key inserted, node full! No split handling yet!");
			return excess;
		}
		//return nothing if it will not split
		long[] val = new long[2];
		val[0] = -1;
		val[1] = -1;
		return val;

	}

	public long[] getExcess(){
		return excess;
	}
	public long[] giveArray(){
		return keyArray;
	}

	//given a key, returns offset of the key
	public long select(long key) {
		long findKey = 0;
		long count = 1;
		long i = 0;

		while (i < 5) {
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

		long temp = (3 * count);
		long temp2 = keyArray[(int)temp];

		// returns the offset value
		return temp2;
	}
	// sets parent given a recordNumber
	public void setParent(long recNum){
		keyArray[0] = recNum;
	}
	public long giveParent(){
		return keyArray[0];
	}
	// gives the nth key of the node, given the index n
	public long giveKey(int index){
		int keyPointer = (3*index) - 1;
		return keyArray[keyPointer];
	}
	//gives the nth offset of the node, given index n
	public long giveOffSet(int index){
		int keyPointer = 3*index;
		return keyArray[keyPointer];
	}

	public long getRecNum(){
		return recNumb;
	}

	// for debugging purposes
	public void printArray(){
		System.out.println(Arrays.toString(keyArray));
	}
	

}