import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//This class creates a Bullet instance which can be shot by a usership or an enemy. it has an image, a direction, a location, and a dis-
//placement.
public class Bullet {
	int x,y;
	private double dx,dy; 			//direction its moving in
	private String type;
	private Image img;
	private int speed;
    public Bullet(Image i,String t,int xx,int yy, double ddx, double ddy, int s) {
    	type=t;
    	x=xx;
    	y=yy;
    	dx=ddx;
    	dy=ddy;

    	img = i;
    	speed=s;
    }
    public int getX(){ return x;}
    public int getY(){ return y;}
    public double getDX(){ return dx;}
    public double getDY(){ return dy;}

    public void setX(int xx){x=xx ;}
    public void setY(int yy){ y=yy;}
    public void incX(int xx) {x+=xx ;}
    public void incY(int yy){y+=yy;}
    
    public void setSpeed(int s){speed=s;}
    
    public void move(){
    	x+=dx*speed;
    	y+=dy*speed;
    }
    public void drawImg(Graphics g, GamePanel panel){	
		g.drawImage(img,x,y,panel);
	}
    
    public boolean collide(Enemy e){			//if bullet is in enemy box
		int enemyLen=e.getLen(), enemyHeight=e.getHeight(); 
		if (x<=(e.getX()+enemyLen/2) && x>=(e.getX()-enemyLen/2) && y>=(e.getY()-enemyHeight/2) && y<=(e.getY()+enemyHeight/2)){
			return true;
		}
		return false;
	}
	public boolean collide(Boss b){
		int bossLen=b.getLen(), bossHeight=b.getHeight(); 
		if (x<=(b.getX()+bossLen/2) && x>=(b.getX()-bossLen/2) && y>=(b.getY()-bossHeight/2) && y<=(b.getY()+bossHeight/2)){
			return true;
		}
		return false;
	}
	
	public boolean collide(UserShip u){
		int userShipLen=u.getLen(), userShipHeight=u.getHeight(); 			//
		if (x<=(u.getX()+userShipLen/2) && x>=(u.getX()-userShipLen/2) && y>=(u.getY()-userShipHeight/2) && y<=(u.getY()+userShipHeight/2)){
			return true;
		}
		return false;
	}
}