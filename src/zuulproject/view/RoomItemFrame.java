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
 * A frame that contains a JList full of all the items the room holds
 * This class is similar to the other two frames (CommandListFrame and InventoryFrame - explained more in the Readme.txt)
 * It implements GameListener because when the game changes (player picks up items), room items change
 */


public class RoomItemFrame extends JFrame implements GameChangeListener {
	
	private static final long serialVersionUID = -8817821070075378882L;
	
	private JList<String> list;				// holds a list of all the items in the room
	private DefaultListModel<String> listModel;
	
	private JScrollPane scrollPane;
	private GameSystem game;
	private GameView view;
	
	public RoomItemFrame(GameView v, GameSystem g) {
		game = g;
		view = v;
		listModel = new DefaultListModel<String>();
		List<String> listContent = game.getGame().getPlayer().getRoom().getItemListString();
		for (String s : listContent) listModel.addElement(s);
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new ListClickListener());
		
		scrollPane = new JScrollPane(list);
		list.setVisible(true);
		this.setTitle("Room Items");
		this.setContentPane(scrollPane);
	  	this.setSize(200, 300);
	}

	public void refreshList() {
		listModel.clear();
		List<String> listContent = game.getGame().getPlayer().getRoom().getItemListString();
		for (String s : listContent) listModel.addElement(s);		
	}

	@Override
	public void gameBegins(GameEvent e) {
		refreshList();
	}

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
	
	// when a item in JList is clicked, it puts the string representation of the item in the commandbox
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
