



import java.io.File;
import java.nio.file.NoSuchFileException;

public class FixParams {
	private File file;
	private float by;

	public File getFile() {
		return file;
	}

	public void setFile(File file) throws NoSuchFileException {
		if (!file.isFile() || !file.canRead()) {
			throw new NoSuchFileException("Not a file or the file is unreadable.");
		}
		this.file = file;
	}

	public float getBy() {
		return by;
	}

	public void setBy(float by) {
		this.by = by;
	}

	@Override
	public String toString() {
		return String.format("File %s\nFix by %.3f", file, by);
	}
	
	
}