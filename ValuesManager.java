import java.io.*;

public class ValuesManager {

	private long numRecords;
	private RandomAccessFile val;

	public ValuesManager(String name) throws IOException {
		File myFile = new File(name);

		// Check if the file exists 
		if (myFile.exists()) {
			// System.out.println("Exists");

			// Create the RandomAccessFile
			val = new RandomAccessFile(myFile,"rwd");

			// Seek to byte index which refers to number of records
			val.seek(0);

			// Set numRecords to the long within this 
			numRecords = val.readLong();
		}

		else {
			
			// Creates the new file and sets numRecords to 0
			val = new RandomAccessFile(myFile,"rwd");
			numRecords = 0;

			// Initializes the value of the numRecords of the file
			val.seek(0);
			val.writeLong(numRecords);	
		}

		// Record checker
		// System.out.println("Number of Records: " + numRecords);
	}

	public long insert(String value) throws IOException {
		// Update the val file to have correct numRecords
		val.seek(0);
		val.writeLong(numRecords + 1);

		// Sets the file pointer to the appropriate location
		val.seek(8 + (numRecords * 256));

		// Writes the length of the String
		val.writeInt(value.length());

		// Write the String value
		val.writeBytes(value);

		// Returns the record number then increments
		return numRecords++;
	}
	// for same key error
	public void deleteLast(){
		numRecords--;
	}

	// returns object string which is the string which the given offset is assigned to
	// NOTE: THERE'S AN INT BEFORE THE OBJECT WHICH DICTATES HOW LONG THE STRING IS, REMEMBER TO READ THAT FIRST AND THEN READ THAT MANY CHARS TO BE ABLE TO KNOW WHAT OBJECT YOU"RE SUPPOSED TO RETURN
	public void close() {
		try {
			val.close();
		} catch (IOException e) {
			;
		}
	}
}