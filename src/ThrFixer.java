

public class ThrFixer implements Runnable {
	FixParams fps;

	public ThrFixer(FixParams fps) {
		this.fps = fps;
	}

	@Override
	public void run() {
		System.out.println(fps);
		try {
			new FileSubFixer().fix(fps);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
