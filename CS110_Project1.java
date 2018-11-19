import java.io.*;
import java.util.*;

public class CS110_Project1 {

	private static ValuesManager val;

	public static void main(String[] args) {
		// create the files
		try {

			// Creates the file that will contain the values
			val = new ValuesManager(args[1]);

			// Creates the B-Tree file
			RandomAccessFile bt = new RandomAccessFile(args[0], "rwd");
		}

		catch(IOException e) {
			;
		}

		// Creates Scanner
		Scanner in = new Scanner(System.in);

		while (in.hasNextLine()) {
			// read the line of commands + arguments
			String inputLine = in.nextLine();
			//split into an array of size 3
			//[0] = cmd, [1] = key, [2] = object(if applicable)
			String[] inputs = inputLine.split(" ", 3);

			// gets the command inputs
			String cmd = inputs[0];

			if (cmd.equals("exit")) {
				val.close();
				break;
			}

			else if (cmd.equals("insert")) {
				long key = 0;
				try{
					key = Long.parseLong(inputs[1]);	
					if (inputs.length == 2){
						insert(key, "");
					}
					else{
						insert(key, inputs[2]);
					}
				}catch(NumberFormatException nfe){
					System.out.println("ERROR: invalid key input, Please enter an integer");
				}
				
				// if the inputs length is just 2, then it means the user inserted an empty string

			}
			else if (cmd.equals("search")) {
				long key = 0;
				try{
					key = Long.parseLong(inputs[1]);	
					search(key);
				}catch(NumberFormatException nfe){
					System.out.println("ERROR: invalid key input, Please enter an integer");
				}
				
			}
			else if (cmd.equals("update")) {
				long key = 0;
				try{
					key = Long.parseLong(inputs[1]);	
					update(key, inputs[2]);
				}catch(NumberFormatException nfe){
					System.out.println("ERROR: invalid key input, Please enter an integer");
				}catch(IndexOutOfBoundsException ie){
					update(key,"");
				}
			}

			else
				System.out.println("ERROR: invalid command");
		}
	}

	public static void insert(long key, String value) {
		long recordNumber = -1;

		try{
			recordNumber = val.insert(value);	
		}catch(IOException ie){
			;
		}
		
		System.out.printf("--> in method insert( long key, String value ), value %s inserted at index %d\n", value, recordNumber);
	}
	public static void search(long key) {
		System.out.println( "SEARCH"+ " "+ key);
	}
	public static void update(long key, String value) {
		System.out.println( "UPDATED:"+ " "+ key + " " + value);
	}


}