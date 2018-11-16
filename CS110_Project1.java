import java.io.*;

public class CS110_Project1 {

	public static void main(String[] args) {
		
		try {
			// creates the B-Tree file
			RandomAccessFile bt = new RandomAccessFile(args[0], "rwd");	
			// creates the file that will contain the values
			RandomAccessFile val = new RandomAccessFile(args[1], "rwd");	
		}

		catch(IOException e) {
			;
		}
		
	}
}