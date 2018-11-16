import java.io.*;
import java.util.*;

public class CS110_Project1 {

	public static void main(String[] args) {
		// create the files
		try {
			// creates the B-Tree file
			RandomAccessFile bt = new RandomAccessFile(args[0], "rwd");	
			// creates the file that will contain the values
			RandomAccessFile val = new RandomAccessFile(args[1], "rwd");	
		}

		catch(IOException e) {
			;
		}

		// creates Scanner
		Scanner in = new Scanner(System.in);

		while (in.hasNextLine()) {

			// initializes the arrayList to take the individual arguments
			ArrayList<String> cmdArray = new ArrayList<String>();

			// takes the individual commands
			for (int i = 0; i < 3; i++) {
				// takes the command and the key
				if(i != 2) {
					cmdArray.add(in.next());
				}

				// takes the object name
				else if(i == 2) {
					cmdArray.add(in.nextLine()); // doesn't work
				}
			}

			// array tester
			for (String s : cmdArray) {
				System.out.println(s);
			}

			// gets the command inputs
			String cmd = cmdArray.get(0);

			if (cmd.equals("exit"))
				break;
			else if (cmd.equals("insert")) {
				
			}

			else
				System.out.println("ERROR: invalid command");
		}
	}

	public static void insert(long key, String value) {
		System.out.println();
	}

}