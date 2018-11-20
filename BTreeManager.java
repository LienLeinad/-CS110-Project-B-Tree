import java.io.*;
public class BTreeManager{
	private long numRecords;
	private long rootNum;
	private RandomAccessFile bt;
	public BTreeManager(String name)throws IOException{
		// instantiate named file as a File object and check if it even exists
		File myFile = new File(name);
		if(myFile.exists()){
			//Create RAF for it
			bt = new RandomAccessFile(myFile,"rwd");
			//initialize numRecord as whatever is in byte 0-8
			bt.seek(0);
			numRecords = bt.readLong();
			//initialize rootNum as whatever is in byte 9-16
			bt.seek(9);
			rootNum = bt.readLong();

		}
		else{
			// create a new RAF for the file
			bt = new RandomAccessFile(myFile,"rwd");
			// initialize numrecords to be 0, and the first bit of the file to be 0
			numRecords = 1;
			bt.seek(0);
			bt.writeLong(numRecords);
			// initialize byte as 0, since the root would be the 0th record
			rootNum = 0;
			bt.seek(9);
			bt.writeLong(rootNum);
		}	
	}
	// insert method will insert the key integer as well as its corresponding offset for the object (From the data.val)
	// returns an int regarding where the current root node is
	public long insert(int key, int offset){
		//put the pointer towards where 
		return rootNum;
	}

	public long giveRootNum(){
		return rootNum;
	}
	public long numNodes(){
		return numRecords;
	}
	public void close(){
		try{
			bt.close();
		}catch(IOException e){
			System.out.println("IOException at close method");
		}
	}
}