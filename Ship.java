import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//A Ship in the game is either an Enemy or UserShip (no ship instances are created). a Ship has a location, image, engine fire image,
//exploding images, and HP. this class has methods to move and draw the ship.
public class Ship {
	protected int x,y;				//x,y is center of ship
	protected int len, height;
	protected int enginefirecounter,explodingpicscounter;
	
	protected Image img, engineFireImg1, engineFireImg2, bulImg;	
	protected Image[] explosionPics;				
	protected int speed;
	
	protected boolean exploding,dead;			//is it exploding or dead
	protected int HP;
	
    public Ship( String name, String firename, String bulname, int x, int y, Image[] ep, int hp) {
    	this.x = x;
    	this.y = y;
    
    	speed = 1;
    	enginefirecounter=0;
    	explodingpicscounter=0;
    	//System.out.println(getClass().getResource("pics/ships002user.png"));
    	img=new ImageIcon(name).getImage();
    	engineFireImg1=new ImageIcon(firename.substring(0,firename.indexOf("."))+"1.png").getImage();
    	engineFireImg2=new ImageIcon(firename.substring(0,firename.indexOf("."))+"2.png").getImage();
    	bulImg=new ImageIcon(bulname).getImage();
    	len = img.getWidth(null);
    	height = img.getHeight(null);
    	
    	explosionPics=ep;
    	
    	exploding=false;
    	dead=false;
    	HP=hp;
    	
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public int getLen(){return len;}
    public int getHeight(){return height;}
    public int getHP(){return HP;}
    public Image getBulletImage(){return bulImg;}
    public Image[] getExplosionPics(){return explosionPics;}
    public boolean isDead(){return dead;}
    public boolean isExploding(){return exploding;}
    
    public void setHP(int h){HP=h;}
    public void changeHP(int h){HP+=h;}
    public void setSpeed(int s){speed=s;}
    public void setExploding(boolean e){exploding=e;}
    
    public Point getCornerCoord(){
    	return new Point((int)(x-len/2),(int)(y-height/2));
    }
    
    public void move(double dx, double dy){
    	
    	x += speed*dx;
    	y += speed*dy;
    }
    
    public void drawImg(Graphics g, GamePanel panel){
    	if (exploding==false){
    		g.drawImage(img,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel);
			enginefirecounter++;
			if (enginefirecounter%5>3){
				g.drawImage(engineFireImg1,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel); 
			}
			else{
				g.drawImage(engineFireImg2,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel); 	
			}
    	}
    	else{								//its exploding!!!
    		Image im=explosionPics[explodingpicscounter/4];
    		g.drawImage(im,x-im.getWidth(null)/2,y-im.getWidth(null)/2,panel);
    		explodingpicscounter++;
    		if (explodingpicscounter==4*explosionPics.length){
    			dead=true;
    			exploding=false;
    		}
    		
    	
    		
    		
    
    	}
		
		
	}
    
}