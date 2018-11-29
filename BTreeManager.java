import java.io.*;
import java.util.*;
public class BTreeManager{
	private long numRecords;
	private long rootNum;
	private RandomAccessFile bt;
	private ArrayList<Node> nodes; // an ArrayList of nodes
	private Node root; // global variable for the root node

	public BTreeManager(String name) throws IOException {
		// instantiate named file as a File object and check if it even exists
		File myFile = new File(name);
		// create an ArrayList of nodes to represent the b-tree
		nodes = new ArrayList<Node>();
		// for using already existent b-tree files
		if (myFile.exists()) {
			//Create RAF for it
			bt = new RandomAccessFile(myFile,"rwd");
			//initialize numRecord as whatever is in byte 0-8
			bt.seek(0);
			numRecords = bt.readLong();
			//initialize rootNum as whatever is in byte 9-16
			bt.seek(8);
			rootNum = bt.readLong();
			System.out.println(numRecords);
			//create the node object for each record and add them into nodes
			for (int i = 0; i < numRecords; i ++) {
				nodes.add(createNode(i));				
			}

			// point the root to be the one in rootNum
			root = nodes.get((int)rootNum);
		}

		// when creating a NEW B-tree
		else {
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
	public long insert(long key, long offset) throws SameKeyException{
		if(rootNum == 0 || numRecords == 1){ // if the root is also the first node
			//check if it's not full
			if(root.getKeyCount() != 4){
				//check first if it has a key that's the same as what youre inserting
				for (int i = 1; i <= root.getKeyCount();  i++) {
					long rootKey = root.giveKey(i);
					if(key == rootKey){
						throw new SameKeyException();
						// return -1;
					}
				}
				//add
				// System.out.println("inserted at root");
				root.insert(key,offset);
			}
			else if(root.getKeyCount() == 4){//root needs to be split now
				for (int i = 1; i <= root.getKeyCount();  i++) {
					long rootKey = root.giveKey(i);
					if(key == rootKey){
						throw new SameKeyException();
						// return -1;
					}
				}
				long[] excess = root.insert(key,offset);
				// long excessChild = root.getExcessChild();
				split(excess, root, (long)-1);
			}
		}else{// root is not a leaf, and has children
			//locate which leaf to add the key to

			// look through root node's keys to figure out where key and offset should be inserted
			Node currentNode = root;
			long currentNodeRecNum = currentNode.getRecNum();
			// look through all the keys of the current node
			// do this while currentNode is not a leaf
			while(!currentNode.nodeIsLeaf()){

				boolean hasChanged = false;
				for (int i = 1; i <= currentNode.getKeyCount();i++) {
					//get key at index i
					long nodeKey = currentNode.giveKey(i);
					// if the nodeKey is greater than key, thereore it belongs to the left child
					if(nodeKey == key){
						throw new SameKeyException();
					}
					if(key < nodeKey){
						//look through this node nowwwwwwww
						currentNode = nodes.get((int) currentNode.getLeftChild(nodeKey));
						currentNodeRecNum = nodes.indexOf(currentNode);
						hasChanged = true;
						break;
					}
				}	
				// if at this point the currentNode hasn't changed, then the right child of the last key is the new currentNode
				if(!hasChanged){
					currentNode = nodes.get((int) currentNode.getRightChild(currentNode.giveKey(currentNode.getKeyCount())));
					currentNodeRecNum = nodes.indexOf(currentNode);
				}
			}
			//by this point you're in a leaf node

			//insert like normal if the ndoe isn't full
			if(currentNode.getKeyCount() != 4){
				for (int i = 1; i <= currentNode.getKeyCount();  i++) {
					long rootKey = currentNode.giveKey(i);
					if(key == rootKey){
						throw new SameKeyException();
						// return -1;
					}
				}
				//insert
				// System.out.println("insert no split");
				currentNode.insert(key,offset);
			}else if (currentNode.getKeyCount() == 4) {
				for (int i = 1; i <= currentNode.getKeyCount();  i++) {
					long rootKey = currentNode.giveKey(i);
					if(key == rootKey){
						throw new SameKeyException();
						// return -1;
					}
				}
				//get excess while inserting
				// System.out.println("insert with split");
				long[] excess = currentNode.insert(key,offset);
				//split the node
				split(excess,currentNode, (long) -1);
			}			
		}
		return rootNum;
	}

	public void split(long[] excess, Node nodeToSplit, long excessChild){
		int numberOfKeys = 5;
		int medianKeyIndex = 3;
		long medianKey = nodeToSplit.giveKey(medianKeyIndex);
		long medianOffSet = nodeToSplit.giveOffSet(medianKeyIndex);

		//create a left Node
		// this node replaces the record of the nodeToSplit
		// nodes.remove(nodes.indexOf(nodeToSplit));
		// numRecords--;
		Node left = new Node(nodes.indexOf(nodeToSplit));
		nodes.add(nodes.indexOf(nodeToSplit), left);
		nodes.remove(nodeToSplit);
		//create the right node
		// this node is brand new
		Node right = new Node(numRecords++);
		nodes.add(right);

		// insert keys belonging to the left Node to the left Node (2 keys before medianKeyIndex)
		for (int i = 1; i < medianKeyIndex; i++) {
			try{
				left.insert(nodeToSplit.giveKey(i), nodeToSplit.giveOffSet(i));	
			}catch(SameKeyException ie){
				System.out.println("SameKeyexception at i = " + i + "at split method");
			}
		}
		// do the same for right keys
		try{
			right.insert(nodeToSplit.giveKey(4),nodeToSplit.giveOffSet(4));
			right.insert(excess[0],excess[1]);
			nodeToSplit.emptyExcess();
		}catch(SameKeyException ie){
			System.out.println("SameKeyexception at right Child insert, at split method");
		}

 

		// if it has children, set it one by one
		if(nodeToSplit.nodeIsLeaf() == false){
			for(int i = 1; i < 3; i++){
				long leftChildOffset = nodeToSplit.getLeftChild(nodeToSplit.giveKey(i));
				//if there is one
				if(leftChildOffset != -1){
					//add it into left
					left.setLeftChild(nodeToSplit.giveKey(i),leftChildOffset );
				} 
			}
			//if the second key of nodeToSplit has a right child
			if(nodeToSplit.getRightChild(nodeToSplit.giveKey(2)) != -1){
				//put it in left
				
				left.setRightChild(nodeToSplit.giveKey(2), nodeToSplit.getRightChild(nodeToSplit.giveKey(2)));
			}

			//left node is done
			//set right node

			long leftChildOffset = nodeToSplit.getLeftChild(nodeToSplit.giveKey(4));
			//if there's a left child add it to right
			if(leftChildOffset != -1){
				right.setLeftChild(nodeToSplit.giveKey(4), leftChildOffset);
			}

			long rightChildOffset = nodeToSplit.getRightChild(nodeToSplit.giveKey(4));
			//if there's a right child add it to right
			if(rightChildOffset != -1){
				right.setRightChild(nodeToSplit.giveKey(4), rightChildOffset);
			}


			//handle the excess if there is any
			if(excessChild != -1){
				right.setRightChild(excess[0], excessChild);
			}


		}
		//check for parent for the node 
		if(nodeToSplit.giveParent() == -1){
			//if it's a root  (no parents)(is batman)
			//make new root
			Node newRoot = new Node(numRecords++);
			nodes.add(newRoot);
			root = newRoot;
			rootNum = nodes.indexOf(root);
			//set it as parents for both left and right
			left.setParent(nodes.indexOf(root));
			right.setParent(nodes.indexOf(root));
			// insert the medianKey and medianOffset
			try{
				root.insert(medianKey,medianOffSet);		
			}catch(SameKeyException ske){
				System.out.println("SameKeyException at newRoot insertion");
			}
			
			//set leftChild of the newRoot
			root.setLeftChild(medianKey,nodes.indexOf(left));
			//set rightChild of the newRoot
			root.setRightChild(medianKey,nodes.indexOf(right));
		}else{
			Node parent = nodes.get((int)nodeToSplit.giveParent());
			//this is where it gets tricky
			//check first if the parent has less than or equal to 5 children
			if(parent.getNumChild() < 5){
				//if so, then no problem add as normal
				// //insert to parent node the key and offset
				try{
					parent.insert(medianKey,medianOffSet);		
				}catch(SameKeyException ske){
					System.out.println("SameKeyException at newRoot insertion");
				}
				// set leftChild
				left.setParent(nodes.indexOf(parent));
				parent.setLeftChild(medianKey,nodes.indexOf(left));
				//set rightChild
				right.setParent(nodes.indexOf(parent));
				parent.setRightChild(medianKey, nodes.indexOf(right));
			}else if (parent.getNumChild() == 5) {//parent node must be split
				//this gets tricky
				//insert first the key, this will give you an excess
				long[] excessKeyOffSet = new long[2];
				try{
					excessKeyOffSet = parent.insert(medianKey,medianOffSet);
				}catch(SameKeyException ske){
					System.out.println("Error at insert");
				}
				
				//set excessChild of parent as right
				int excessChildOffSet = nodes.indexOf(right);
				parent.setExcessChild(excessChildOffSet);
				// set left child
				left.setParent(nodes.indexOf(parent));
				parent.setRightChild(parent.giveKey(4), nodes.indexOf(left));
				//setting right's parent will be done after splitting the parent
				//split parent
				split(excessKeyOffSet, parent,parent.getExcessChild());

			}
		}

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
			//write the root and numRecords
			bt.seek(0);
			bt.writeLong(numRecords);
			bt.writeLong(rootNum);

			// seek to the first long
			bt.seek(16);
			
			for (int i = 0; i < nodes.size(); i++) {				
				Node temp = nodes.get(i);
				long[] nums = temp.giveArray();

				for (long l : nums) {
					bt.writeLong(l);
				}

				System.out.println(Arrays.toString(nums) + " " + i);

			}

			bt.close();

		} catch(IOException e) {
			System.out.println("IOException at close method");
		}
	}

	// creates the FIRST node of a NEW B-tree
	public void createFirstNode(){
		// create a node object which will accept the array, and then increment numRecords by 1, this node will be the root node
		root = new Node(numRecords);
		nodes.add(root);
		numRecords++;
	}

	//returns offset given a key
	//TO BE IMPROVED ON ONCE SPLITTING IS IMPLEMENTED
	public long select(long key) throws ArrayIndexOutOfBoundsException{
		//cuurent node at nodes(0)
		int j = 0;
		while(j < nodes.size()){
			Node currentNode = nodes.get(j);
			//keyFound boolean is false
			boolean keyFound = false;
				for (int i = 1; i <= currentNode.getKeyCount() ; i++ ) {
					if(currentNode.giveKey(i) == key){
						//if the key is in the current node we are done
						return currentNode.select(key);
					}
				}
			//if the key isn't in the node, check another node
			if(!keyFound){
				j++;
			}
		}
		throw new ArrayIndexOutOfBoundsException();
			
	}

	// given a long from a certain byte, creates a nodeObject for it, to be used for when creating nodes from existing .bt files
	public Node createNode(long recNum) {
		long[] nums = new long[14];
		// takes each long from the record in data.bt and adds it into nums
		try {
			// seek into first long of the record
			bt.seek(16 + 112*recNum);
			for(int i = 0; i < nums.length; i++) {
				// add to the index
				nums[i] = bt.readLong();
			}
		} catch(IOException e) {
			System.out.println("IOException at createNode");
		}

		//return a newly created node with the array taken from the file
		return new Node(nums,recNum);
	}
	//in case of a split, use this code creation method
}