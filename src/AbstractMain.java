



import java.io.File;
import java.util.Scanner;

/**
 * Rozsir tuto triedu.
 * 
 * @author Kris
 *
 */
public abstract class AbstractMain {

	protected FixParams promptParams() {
		FixParams result = null;
		Scanner sc = null;

		try {
			result = new FixParams();
			sc = new Scanner(System.in);

			System.out.println("Enter file path to .srt file:");
			String input = sc.nextLine();
			result.setFile(new File(input));

			System.out.println("Enter time in seconds:");
			input = sc.nextLine();
			result.setBy(Float.parseFloat(input));
		} catch (NumberFormatException e) {
			System.err.println("Invalid number, exiting...");
			System.exit(-1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("Exiting...");
			System.exit(-1);
		} finally {
			if (sc != null)
				sc.close();
		}
		return result;
	}

}
