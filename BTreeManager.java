import java.io.*;
import java.util.*;
public class BTreeManager{
	private long numRecords;
	private long rootNum;
	private RandomAccessFile bt;
	// an ArrayList of nodes
	private ArrayList<Node> nodes;
	// global variable for the root node
	private Node root;
	public BTreeManager(String name)throws IOException{
		// instantiate named file as a File object and check if it even exists
		File myFile = new File(name);
		// create an ArrayList of nodes to represent the b-tree
		nodes = new ArrayList<Node>();
		// for using already existent b-tree files
		if(myFile.exists()){
			//Create RAF for it
			bt = new RandomAccessFile(myFile,"rwd");
			//initialize numRecord as whatever is in byte 0-8
			bt.seek(0);
			numRecords = bt.readLong();
			//initialize rootNum as whatever is in byte 9-16
			bt.seek(8);
			rootNum = bt.readLong();

			/*//create the node object for each record and add them into nodes
			for(int i = 0; i < numRecords; i ++){
				nodes.add(createNode(i));				
			}*/

			nodes.add(createNode(0));

			// point the root to be the one in rootNum
			root = nodes.get((int)rootNum);


		}
		// when creating a NEW B-tree
		else{
			// create a new RAF for the file
			bt = new RandomAccessFile(myFile,"rwd");
			// initialize numrecords to be 0, and the first bit of the file to be 0
			numRecords = 0;
			//create the first node
			createFirstNode();
			//set byte 0-9 as the numRecords
			bt.seek(0);
			bt.writeLong(numRecords);
			// initialize byte as 0, since the root would be the 0th record
			rootNum = 0;
			bt.seek(8);
			bt.writeLong(rootNum);
		}	
	}
	// insert method will insert the key integer as well as its corresponding offset for the object (From the data.val)
	// returns an int regarding where the current root node is
	//ONLY INSERTS IN ROOT FOR NOW!!!
	public long insert(long key, long offset){
		root.insert(key,offset);
		return rootNum;
	}

	public long giveRootNum(){
		return rootNum;
	}
	public long numNodes(){
		return numRecords;
	}
	// writes all nodes into the bt.file before closing
	public void close(){
		// System.out.println("close method from BTreeManager");
		try{
			// seek to the first long
			bt.seek(16);
			
			for (int i = 0; i < nodes.size(); i ++) {
				
				Node temp = nodes.get(i);
				long[] nums = temp.giveArray();
				for (long l : nums) {
					bt.writeLong(l);
				}
				System.out.println(Arrays.toString(nums));
			}

			bt.close();
		}catch(IOException e){
			System.out.println("IOException at close method");
		}
	}
	// creates the FIRST node of a NEW B-tree
	public void createFirstNode(){
		// create a node object which will accept the array, and then increment numRecords by 1, this node will be the root node
		root = new Node();
		nodes.add(root);
		numRecords++;
	}
	// given a long from a certain byte, creates a nodeObject for it
	public Node createNode(long recNum){
		long[] nums = new long[14];
		// TO BE IMPROVED ON SOON:
		// takes each long from the record in data.bt and adds it into nums
		try{
			// seek into first long of the record
			bt.seek(16 + 112*recNum);
			for(int i = 0; i < nums.length; i++){
				
				
				// add to the index
				nums[i] = bt.readLong();
			}
		}catch(IOException e){

			System.out.println("IOException at createNode");
		} 
		numRecords++;
		//return a newly created node with the array taken from the file
		return new Node(nums);
	}
}