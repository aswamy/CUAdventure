package zuulproject.view;

import java.awt.event.ActionListener;

import javax.swing.*;

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

	public void addDialogInputListener(ActionListener listener) {
		saveFile.addActionListener(listener);
	}

	public void resetUserInput() {
		saveFile.setText("");
	}
	
	public void showPopup(boolean b) {
		this.setVisible(b);
	}
	
	public String getUserInput() {
		return saveFile.getText();
	}
}