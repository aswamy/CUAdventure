package zuulproject.view;

import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * A prompt box that asks the user to enter the name of the file he/she wishes to open/save
 * @author Alok
 *
 */
public class SaveDialogBox extends JFrame {
	
	private static final long serialVersionUID = 521364007971526377L;
	private static final String message = "Enter the name of the game file (e.g. game.xml):";
	
	private JTextField saveFile = new JTextField(20);

	private JLabel label;
	
	public SaveDialogBox(String title) {
		label = new JLabel(message);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		this.add(label);
		this.add(saveFile);
		
		this.setTitle(title);
		this.setVisible(false);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	/*
	 * add the listener to the dialog box (done by controller)
	 */
	public void addDialogInputListener(ActionListener listener) {
		saveFile.addActionListener(listener);
	}

	// reset input
	public void resetUserInput() {
		saveFile.setText("");
	}
	
	// show/hide popup box
	public void showPopup(boolean b) {
		this.setVisible(b);
	}
	
	// gets the name of the file entered by the user
	public String getUserInput() {
		return saveFile.getText();
	}
}