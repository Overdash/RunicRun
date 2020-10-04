package com.game.Entity;

/**
 * Created by Master Don Pro (Prodige) on 29/01/2016.
 * @author Prodige Tukala
 *
 * Base Class for all Entities.
 */

import java.awt.Rectangle;

import com.game.Main.GamePanel;
import com.game.TileMap.Tile;
import com.game.TileMap.TileMap;


public abstract class MapObject {
	
	//Tile
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//Position & vectors
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//Dimensions
	protected int width;
	protected int height;
	
	//Collision Container
	protected int cwidth;
	protected int cheight;
	
	//Collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;

    //Due to four corners method
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	//Movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	//Movement Attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;

	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
		animation = new Animation();
		facingRight = true;
	}//END Constructor
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}//END intersects
	
	public boolean intersects(Rectangle r) {
		return getRectangle().intersects(r);
	}//END intersects(Overloaded)
	
	public boolean contains(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}//END contains
	
	public boolean contains(Rectangle r) {
		return getRectangle().contains(r);
	}//END contains(Overloaded)
	
	public Rectangle getRectangle() {
		return new Rectangle((int)x - cwidth / 2, (int)y - cheight / 2, cwidth, cheight);
	}//END getRectangle
	
	public void calculateCorners(double x, double y) {
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;

		if(topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}

		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}//END calculateCorners
	
	public void checkTileMapCollision() { //Long ass Collision checking Method *NECESSARY*
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest); //Prevents hitting walls (Horizontally)
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}//END Inner If-Else
		}//END Outer-If
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}//END Inner If-Else
		}//END Outer-if
		
		calculateCorners(xdest, y); //Prevents hitting walls (Vertically)
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}//END Inner If-Else
		}//END Outer-If
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}//END Inner If-Else
		}//END Outer If
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}//END Inner-If
		}//END Outer-IF
		
	}//END checkTileMapCollision

	//Getters and Setters
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	public boolean isFacingRight() { return facingRight; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}//END notOnScreen

    //For Debugging only!
    public void getPos() { System.out.println("(" + this.x + "," + this.y + ")"); }

	public void draw(java.awt.Graphics2D g) {
		setMapPosition();
		if(facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else {
			g.drawImage(animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}
		/*Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r); */
	}//END draw
	
}//END Class