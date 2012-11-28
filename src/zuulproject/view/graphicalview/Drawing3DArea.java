package zuulproject.view.graphicalview;

import zuulproject.model.*;
import zuulproject.model.innercontroller.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;

import javax.swing.*;

/**
 * A 3D representation of the model. The exits in this view are clickable listened to by the GameController
 * 
 * @author Alok
 */

public class Drawing3DArea extends JPanel {

	private static final long serialVersionUID = -6491486951861072281L;
	private static final int ORIGIN_X = 0;
	private static final int ORIGIN_Y = 0;
	private static final Color DEFAULT_COLOR = Color.BLACK;
	private final static float dash1[] = {10.0f};
    private final static BasicStroke DASHED_STROKE =
        new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);			// this allows me to create shapes using dashed lines
	private final static BasicStroke DEFAULT_STROKE = new BasicStroke();
	private final static BasicStroke BOLD_STROKE = new BasicStroke(3);		// this allows me to create shapes using bolded (thick) lines
	
	private final static String north = "N";
	private final static String south = "S";
	private final static String west = "W";
	private final static String east = "E";
	
	private GameSystem game;
	
	private HashMap<Exit, Shape> exits;		//this hashmap makes each exit to a certain shape (to keep track of their location)
	
	//Unlike the static 2D view, the 3D view can stretch, allowing any 2d dimension as an input
	
	public Drawing3DArea(GameSystem g, Dimension dimension) {
		game = g;
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setBackground(Color.white);
		setBorder(BorderFactory.createEtchedBorder());
		exits = new HashMap<Exit, Shape>();
		for (Exit exit : Exit.values()) {
			exits.put(exit, null);
		}
	}

	
	// The entire 3D view is painted using coordinates from ratios, so the shapes in the panel will shrink/expand along with the panel
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		
		g2D.setColor(DEFAULT_COLOR);
		
		int componentHeight = getHeight();
		int componentWidth = getWidth();
		
		if (game.gameRunning()) {
			
		// Drawing the non-interactable drawings in the 3D View
		
			//the following lines represent the room
			g2D.drawRect(componentWidth/3, componentHeight/3, componentWidth/3, componentHeight/3);
			
			g2D.drawLine(ORIGIN_X, ORIGIN_Y, componentWidth/3, componentHeight/3);
			g2D.drawLine(componentWidth, ORIGIN_Y, componentWidth*2/3, componentHeight/3);
			g2D.drawLine(ORIGIN_X, componentHeight, componentWidth/3, componentHeight*2/3);
			g2D.drawLine(componentWidth, componentHeight, componentWidth*2/3, componentHeight*2/3);		
		
			// this represents the compass to tell what each exit represents
			g2D.setStroke(BOLD_STROKE);
			g2D.setColor(Color.RED);
			g2D.drawLine(componentWidth/2, componentHeight/9, componentWidth/2, componentHeight*2/9);
			g2D.drawLine(componentWidth*7/18, componentHeight*3/18, componentWidth*11/18, componentHeight*3/18);

			g2D.drawString(north, componentWidth/2, componentHeight*1/18);
			g2D.drawString(south, componentWidth/2, componentHeight*5/18);
			g2D.drawString(west, componentWidth/3, componentHeight*3/18);
			g2D.drawString(east, componentWidth*2/3, componentHeight*3/18);
			
			g2D.setStroke(DEFAULT_STROKE);
			g2D.setColor(DEFAULT_COLOR);

		// The following is all the iteractable drawings (i.e the exits)
			
        	if(game.getGame().getPlayer().getRoom().getExitRoom(Exit.west) != null) {
        		Polygon polygon = new Polygon();
        		
            	polygon.addPoint(componentWidth/9, componentHeight*8/9);
            	polygon.addPoint(componentWidth*2/9, componentHeight*7/9);
            	polygon.addPoint(componentWidth*2/9, componentHeight/2);
            	polygon.addPoint(componentWidth/9, componentHeight/2);

            	g2D.drawPolygon(polygon);
            	
        		exits.put(Exit.west, polygon);
        	} else exits.put(Exit.west, null);
        	
        	if(game.getGame().getPlayer().getRoom().getExitRoom(Exit.north) != null)
        	{
        		Polygon polygon = new Polygon();
        		
        		polygon.addPoint(componentWidth*4/9, componentHeight*2/3);
        		polygon.addPoint(componentWidth*5/9, componentHeight*2/3);
        		polygon.addPoint(componentWidth*5/9, componentHeight/2);
        		polygon.addPoint(componentWidth*4/9, componentHeight/2);

        		g2D.drawPolygon(polygon);
        		
        		exits.put(Exit.north, polygon);
        	} else exits.put(Exit.north, null);
        	
        	if(game.getGame().getPlayer().getRoom().getExitRoom(Exit.east) != null)
        	{
        		Polygon polygon = new Polygon();
        		
        		polygon.addPoint(componentWidth*8/9, componentHeight*8/9);
        		polygon.addPoint(componentWidth*7/9, componentHeight*7/9);
        		polygon.addPoint(componentWidth*7/9, componentHeight/2);
        		polygon.addPoint(componentWidth*8/9, componentHeight/2);
        		
        		g2D.drawPolygon(polygon);
        		
        		exits.put(Exit.east, polygon);
        	} else exits.put(Exit.east, null);
        	
        	if(game.getGame().getPlayer().getRoom().getExitRoom(Exit.south) != null)
        	{
        		Polygon polygon = new Polygon();
        		g2D.setStroke(DASHED_STROKE);
        		
        		polygon.addPoint(componentWidth*7/18, componentHeight);
        		polygon.addPoint(componentWidth*11/18, componentHeight);
        		polygon.addPoint(componentWidth*11/18, componentHeight*13/18);
        		polygon.addPoint(componentWidth*7/18, componentHeight*13/18);

        		g2D.drawPolygon(polygon);
        		
        		g2D.setStroke(DEFAULT_STROKE);        		
        		exits.put(Exit.south, polygon);
        	} else exits.put(Exit.south, null);
        	
        	if(game.getGame().getPlayer().getRoom().getExitRoom(Exit.teleporter) != null)
        	{
        		Shape shape = new Ellipse2D.Double(componentWidth*2/9, componentHeight*7/9, componentWidth/9, componentHeight*2/9);
        		
        		g2D.setColor(Color.BLUE);
        		g2D.draw(shape);
        		
        		g2D.setColor(DEFAULT_COLOR);
        		exits.put(Exit.teleporter, shape);
        	} else exits.put(Exit.teleporter, null);
        	
        	// These are things that COULD exist, but not necessarily (like items, and monsters)
        	if(game.getGame().getPlayer().getRoom().numOfItems() > 0)
        	{
        		Shape shape = new Ellipse2D.Double(componentWidth*2/3, componentHeight*7/9, componentWidth/9, componentHeight/9);
    		
        		g2D.setColor(Color.GREEN);
        		g2D.setStroke(BOLD_STROKE);
        		g2D.draw(shape);
        	}
        	
        	if(game.getGame().getPlayer().getRoom().hasMonster())
        	{
        		Polygon monsterBody = new Polygon();
        		Polygon monsterLegs = new Polygon();
        		Shape monsterHead = new Ellipse2D.Double(componentWidth*4/9, componentHeight*4/9, componentHeight*1/9, componentHeight*1/9);
        		
        		monsterBody.addPoint(componentWidth*4/9, componentHeight*7/9);
        		monsterBody.addPoint(componentWidth*4/9, componentHeight*4/9);
        		monsterBody.addPoint(componentWidth*5/9, componentHeight*4/9);
        		monsterBody.addPoint(componentWidth*5/9, componentHeight*7/9);
        		
        		monsterLegs.addPoint(componentWidth*17/36, componentHeight*7/9);
        		monsterLegs.addPoint(componentWidth*19/36, componentHeight*7/9);
        		monsterLegs.addPoint(componentWidth*18/36, componentHeight*8/9);
        		
        		g2D.setColor(Color.RED);
        		g2D.setStroke(BOLD_STROKE);
        		g2D.drawPolygon(monsterBody);
        		g2D.drawPolygon(monsterLegs);
        		g2D.draw(monsterHead);
        	}
    		g2D.setColor(DEFAULT_COLOR);
		}
	}
	
	/* This function determines if the point given is within any of the shapes
	 * This function is used to check if the user clicked any exit
	 */
	public String pointInExit(Point p) {
		for (Exit e : exits.keySet()) {
			Shape shape = exits.get(e);
			if (shape != null) {
				if (shape.contains(p)) return e.toString();
			}
		}
		return null;
	}
	
	/*
	 * This was initially used to make the exits using 4 points, but this actually created more code when calling the function
	 * since you have to turn the x and y into new Point(x,y), which is then decomposed back into x and y. Also, exits like
	 * the teleporter is not rectangular, so this function became obsolete
	 */
	/*
	private Polygon makeExit(Point p1, Point p2, Point p3, Point p4) {
		Polygon polygon = new Polygon();
		polygon.addPoint(p1.x, p1.y);
		polygon.addPoint(p2.x, p2.y);
		polygon.addPoint(p3.x, p3.y);
		polygon.addPoint(p4.x, p4.y);
		
		return polygon;
	}
	*/

}
