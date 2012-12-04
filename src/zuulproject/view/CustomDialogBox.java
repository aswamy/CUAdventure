package zuulproject.view;

import javax.swing.*;

public class CustomDialogBox {
	public static void main (String[] args) {
		JTextField firstName = new JTextField();

		final JComponent[] inputs = new JComponent[] {
				new JLabel("Save Path"),
				firstName
		};
		JOptionPane.showMessageDialog(null, inputs, "Save As", JOptionPane.PLAIN_MESSAGE);
		System.out.println("You entered " +
				firstName.getText());
	}
}
