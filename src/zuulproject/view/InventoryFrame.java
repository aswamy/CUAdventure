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

/**
 * A frame that contains a JList full of all the items the user carries
 * This class is similar to the other two frames (CommandListFrame and RoomItemFrame - explained more in the Readme.txt)
 * It implements GameListener because when the game changes (picks up items, consumes items), inventory changes
 */


public class InventoryFrame extends JFrame implements GameChangeListener {

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
		refreshList();
		list = new JList<String>(listModel);
		list.addMouseListener(new ListClickListener());
		
		scrollPane = new JScrollPane(list);
		list.setVisible(true);
		this.setTitle("Inventory");
		this.setContentPane(scrollPane);
	  	this.setSize(200, 300);
	}

	public void refreshList() {
		listModel.clear();
		List<String> listContent = game.getGame().getPlayer().getItemListString();
		for (String s : listContent) listModel.addElement(s);
	}
	
	@Override
	public void gameBegins(GameEvent e) {
		refreshList();
	}

	/*
	 * Whenever a command is processed, the model will call this function, and the JList will be updated
	 */
	@Override
	public void gameCmdProcessed(GameChangeEvent e) {
		refreshList();
	}

	@Override
	public void gameBattleCmdProcessed(GameBattleChangeEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void gameEnded(GameOverEvent e) {
		listModel.clear();
	}

	@Override
	public void gameBattleEnded(GameEvent e) {
		// TODO Auto-generated method stub
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
