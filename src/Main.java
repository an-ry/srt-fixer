
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.NoSuchFileException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Main extends AbstractMain {

	public static void main(String[] args) throws UnsupportedEncodingException {

		new Main().showGui();
		/*
		 * FileSubFixer fixer = new FileSubFixer(); fixer.fix(new
		 * Main().promptParams());
		 */
	}

	private JFrame mainFrame;
	private JPanel jpanel;
	private JPanel jpanel2;
	private JTextField txtFixBy;
	private JButton btnFix;
	private JButton btnChoose;
	private JLabel lblStatus;
	private JLabel chosenFileLabel;
	private JPanel progressPanel;
	public static JProgressBar progressBar;
	private String chosenFileString = "";
	private File chosenFile;

	private void showGui() throws UnsupportedEncodingException {
		mainFrame = new JFrame("SrtFix");
		jpanel = new JPanel();
		jpanel2 = new JPanel();
		chosenFileLabel = new JLabel("");
		lblStatus = new JLabel("");
		progressPanel = new JPanel();
		progressBar = new JProgressBar(0, 100);
		jpanel.setBackground(new Color(216, 226, 240));
		jpanel2.setBackground(new Color(216, 226, 240));
		progressPanel.setBackground(new Color(216, 226, 240));

		mainFrame.setResizable(false);
		mainFrame.setSize(300, 200);
		mainFrame.setLocation(200, 200);

		txtFixBy = new JTextField(7);
		txtFixBy.setText("000.000 s");
		btnFix = new JButton("Fix");
		btnChoose = new JButton("Choose file");
		progressBar.setStringPainted(true);

		jpanel.add(txtFixBy);
		jpanel.add(btnFix);
		jpanel.add(chosenFileLabel);
		jpanel.add(lblStatus);
		jpanel2.add(btnChoose);
		progressPanel.add(progressBar);

		mainFrame.add(jpanel, BorderLayout.CENTER);
		mainFrame.add(jpanel2, BorderLayout.NORTH);
		mainFrame.add(progressPanel, BorderLayout.SOUTH);
		progressPanel.setVisible(false);

		// String jarLocation =
		// Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		// String decodedJarLocation = URLDecoder.decode(jarLocation, "UTF-8");

		btnFix.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FixParams fps = new FixParams();
				try {
					fps.setBy(Float.parseFloat(txtFixBy.getText()));
					fps.setFile(new File(chosenFileString));
					try {
						progressPanel.setVisible(true);
						Thread thrFixer = new Thread(new ThrFixer(fps));
						thrFixer.start();

					} catch (Exception er) {
						lblStatus.setText(er.getMessage());
					}
				} catch (NumberFormatException ex) {
					lblStatus.setText("Number must be in format 123.456");
				} catch (NoSuchFileException ex) {
					lblStatus.setText(ex.getMessage());
				}
			}
		});
		btnChoose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chosenFileString = showFileChooser();
				chosenFile = new File(chosenFileString);
				lblStatus.setText("");
				progressPanel.setVisible(false);
				chosenFileLabel.setText(chosenFile.getName());
			}
		});

		txtFixBy.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				JTextField source = (JTextField) e.getComponent();
				source.setText("");
				source.removeFocusListener(this);
			}
		});

		mainFrame.setVisible(true);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public String showFileChooser() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select subtitle file");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("SRT files", "srt");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFile().getPath();
		}
		return null;
	}

}
