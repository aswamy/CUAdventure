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
 * A frame that contains a JList full of all the possible commands the user can press on
 * This class is similar to the other two frames (InventoryFrame and RoomItemFrame - explained more in the Readme.txt)
 * It implements GameListener because when the game changes (goes from battle mode to regular mode), the command words change
 */

public class CommandListFrame extends JFrame implements GameModifiedListener {
	
	private static final long serialVersionUID = 4682921453466334303L;

	private JList<String> list;				
	private DefaultListModel<String> listModel;		// this contains all the command words as string, when this changes, Jlist gets updated
	
	private JScrollPane scrollPane;
	private GameSystem game;
	private GameView view;
	
	public CommandListFrame(GameView v, GameSystem g) {
		game = g;
		view = v;
		listModel = new DefaultListModel<String>();
		
		//Get command words from the game
		List<String> listContent;
		if (game.getGame().getPlayer().inBattle()) {
			listContent = game.getGame().getParser().getCommandWords().getBattleCommandList();
		} else {
			listContent = game.getGame().getParser().getCommandWords().getCommandList();
		}
		// the reason we skipped "go" command is because it is redundent (it can be done by clicking on the doors)
		for (String s : listContent) {
			if(!(s.equals("go"))) listModel.addElement(s);
		}
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new ListClickListener());
		
		scrollPane = new JScrollPane(list);
		list.setVisible(true);
		this.setTitle("CommandList");
		this.setContentPane(scrollPane);
	  	this.setSize(200, 300);
	}

	/*
	 * Whenever a command is processed, the model will call this function, and the JList will be updated
	 */
	@Override
	public void commandProcessed(GameModifiedEvent e) {
		listModel.clear();
		List<String> listContent;
		if (game.getGame().getPlayer().inBattle()) {
			listContent = game.getGame().getParser().getCommandWords().getBattleCommandList();
		} else {
			listContent = game.getGame().getParser().getCommandWords().getCommandList();
		}

		for (String s : listContent) listModel.addElement(s);
	}
	
	/*
	 * This is a required function when you implement GameListener - currently does nothing
	 */
	@Override
	public void gameEnded() {
	}
	
	/*
	 * when an item in the Jlist is clicked, the item is entered into the command box in the main view
	 */
	private class ListClickListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			view.appendCommandInput((String)list.getSelectedValue() + " ");
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
