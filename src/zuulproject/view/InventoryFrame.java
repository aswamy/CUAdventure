package zuulproject.view;

import zuulproject.event.*;
import zuulproject.model.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * A frame that contains a JList full of all the items the user carries
 * This class is similar to the other two frames (CommandListFrame and RoomItemFrame - explained more in the Readme.txt)
 * It implements GameListener because when the game changes (picks up items, consumes items), inventory changes
 */


public class InventoryFrame extends JFrame implements GameListener {

	private static final long serialVersionUID = -9185125079205946734L;
	
	private JList<String> list;				// list of all the items the player carries
	private DefaultListModel<String> listModel;
	
	private JScrollPane scrollPane;
	private GameSystem game;
	private GameView view;
	
	public InventoryFrame(GameView v, GameSystem g) {
		game = g;
		view = v;
		listModel = new DefaultListModel<String>();
		List<String> listContent = game.getGame().getPlayer().getItemListString();
		for (String s : listContent) listModel.addElement(s);
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new ListClickListener());
		
		scrollPane = new JScrollPane(list);
		list.setVisible(true);
		this.setTitle("Inventory");
		this.setContentPane(scrollPane);
	  	this.setSize(200, 300);
	}

	/*
	 * Whenever a command is processed, the model will call this function, and the JList will be updated
	 */
	@Override
	public void commandProcessed(GameModifiedEvent e) {
		listModel.clear();
		List<String> listContent = game.getGame().getPlayer().getItemListString();
		for (String s : listContent) listModel.addElement(s);
	}

	@Override
	public void endGame() {
	}
	
	/*
	 * when an item in the Jlist is clicked, the item is entered into the command box in the main view
	 */
	private class ListClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			view.appendCommandInput((String)list.getSelectedValue());
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
		
	}
}
