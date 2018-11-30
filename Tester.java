import java.util.*;


public class Tester {

	private static String ALPHABET_CAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";	
	private static String ALPHABET_LOW = "abcdefghijklmnopqrstuvwxyz";
	private static Random r = new Random();

	public Tester() {

	}

	public static String randString() {
		String word = "";
		int len = r.nextInt(10) + 1;

		for (int i = 0; i < len; i++) {
			int index = r.nextInt(26);

			if (i % 2 == 0)
				word += String.valueOf(ALPHABET_CAP.charAt(index));
			else
				word += String.valueOf(ALPHABET_LOW.charAt(index));
		}

		return word;
	}

	public static int randNum() {
		int num = 0;
		
		return num = r.nextInt(20) + 1;
	}

	public static String generateCmd() {
		int randCMD = r.nextInt(100) + 3;

		if (randCMD % 3 == 0)
			return "insert";
		else if (randCMD % 3 == 1)
			return "select";
		else
			return "update";
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();

		for (int i = 0; i < cases; i++) {
			String cmd = generateCmd();
			if (cmd.equals("insert"))
				System.out.println("insert " + randNum() + " " + randString());
			else if (cmd.equals("select"))
				System.out.println("select " + randNum());
			else
				System.out.println("update " + randNum() + " " + randString());
		}
	}
}