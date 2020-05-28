
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSubFixer implements SubFixer {
	private List<String> linesToDo = new ArrayList<String>();
	private final int NUM_OF_THRS = Runtime.getRuntime().availableProcessors();;
	private float fixBy;
	private Semaphore allFinished = new Semaphore(0);
	private int percentDone;
	private int onePercent = 1;
	private int percentMax;

	private class Fixer implements Runnable {
		int currentThr;

		public Fixer(int i) {
			currentThr = i;
		}

		@Override
		public void run() {
			String newLine = "";
			String line = "";
			for (int i = currentThr; i < linesToDo.size(); i = i + NUM_OF_THRS) {
				line = linesToDo.get(i);
				newLine = fixline(line, fixBy);
				if (!newLine.equals("")) {
					linesToDo.set(i, newLine);
				}
				synchronized (Main.class) {
					percentDone += onePercent;
					Main.progressBar.setValue(percentDone);
				}
			}
			// mame hotovo - vratime klic
			allFinished.release();
		}
	}

	@Override
	public String fixline(String line, float by) {
		String pattern = "(\\d\\d:\\d\\d:\\d\\d[.,]\\d\\d\\d)(.*)(\\d\\d:\\d\\d:\\d\\d[,.]\\d\\d\\d)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		String newLine = "";
		if (m.find()) {
			newLine += replaceDigits(m.group(1), by);
			newLine += m.group(2);
			newLine += replaceDigits(m.group(3), by);
		}
		return newLine;
	}

	private String replaceDigits(String group, float by) {

		String h = "" + group.charAt(0) + group.charAt(1);
		String min = "" + group.charAt(3) + group.charAt(4);
		String s = "" + group.charAt(6) + group.charAt(7) + '.' + group.charAt(9) + group.charAt(10) + group.charAt(11);
		int hour = Integer.parseInt(h);
		int minute = Integer.parseInt(min);
		float second = Float.parseFloat(s);
		float millisecond = ((hour * 3600000) + (minute * 60000) + (second * 1000)) + (by * 1000);
		if (millisecond <= 0) {
			hour = 0;
			minute = 0;
			second = 0;
		} else {
			float midSec = millisecond / 1000;
			second = midSec % 60;
			int m = (int) midSec / 60;
			minute = m % 60;
			int ho = m / 60;
			if (m >= 60) {
				hour = ho;
			}

		}

		String newHour = String.format("%2d", hour).replace(' ', '0');
		String newMinute = String.format("%2d", minute).replace(' ', '0');
		String newSecond = String.format("%6.3f", second).replace(' ', '0').replace('.', ',');

		return newHour + ':' + newMinute + ':' + newSecond;
	}

	@Override
	public void fix(FixParams fps) throws InterruptedException {
		fixBy = fps.getBy();
		Scanner scan = null;
		PrintWriter pw = null;
		File newFile = new File(fps.getFile().getAbsoluteFile().getParent() + "/fix_" + fps.getFile().getName());
		try {
			scan = new Scanner(fps.getFile());
			pw = new PrintWriter(newFile);

			// ukladani lines do que
			while (scan.hasNextLine()) {
				linesToDo.add(scan.nextLine());
			}

			// hodnoty procent:
			percentMax = linesToDo.size();
			Main.progressBar.setMaximum(percentMax);

			// vytvorime vlakna a dame jim praci
			for (int i = 0; i < NUM_OF_THRS; i++) {
				Thread th = new Thread(new Fixer(i));
				System.out.println("inicialice vlakna c." + i + " s id " + th.getId());
				th.start();
			}

			// lines jsou opravene a muzeme zapsat
			allFinished.acquire(NUM_OF_THRS);
			for (int i = 0; i < linesToDo.size(); i++) {
				pw.println(linesToDo.get(i));
			}

		} catch (FileNotFoundException e) {
			System.err.println("Soubor je FAKE jak ADO");
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (scan != null) {
				scan.close();
			}
		}
	}

}
