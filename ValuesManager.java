import java.io.*;

public class ValuesManager {

	private String fileName;
	private long numRecords;
	private RandomAccessFile myFile1;

	public ValuesManager(String name) throws IOException {
		fileName = name;

		File myFile = new File(name);

		// Check if the file exists 
		if (myFile.exists()) {
			// System.out.println("Exists");

			// Create the RandomAccessFile
			myFile1 = new RandomAccessFile(myFile,"rwd");

			// Seek to byte index which refers to number of records
			myFile1.seek(0);

			// Set numRecords to the long within this 
			numRecords = myFile1.readLong();
		}

		else {
			
			// Creates the new file and sets numRecords to 0
			myFile1 = new RandomAccessFile(myFile,"rwd");
			numRecords = 0;

			// Initializes the value of the numRecords of the file
			myFile1.seek(0);
			myFile1.writeLong(numRecords);	
		}

		// Record checker
		// System.out.println("Number of Records: " + numRecords);
	}

	public long insert(String value) throws IOException {
		// Update the val file to have correct numRecords
		myFile1.seek(0);
		myFile1.writeLong(numRecords + 1);

		// Sets the file pointer to the appropriate location
		myFile1.seek(8 + (numRecords * 256));

		// Writes the length of the String
		myFile1.writeInt(value.length());

		// Write the String value
		myFile1.writeBytes(value);

		// Returns the record number then increments
		return numRecords++;
	}

	public void close() {
		try {
			myFile1.close();
		} catch (IOException e) {
			;
		}
	}
}