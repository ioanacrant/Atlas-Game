import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;
//Ioana Crant
//BossRocket is the ammo that is shot by the boss. It explodes at a random location after being shot and everything in vicinity
//of it is hit by a lor of damage.

public class BossRocket {
	private int x,y,dTillExplode;			//distance until rocket explodes
	private Image img1,img2;				//beeping rocket like grenade
	private int imagecounter,explodingpicscounter;
	private boolean exploding,done;			//done exploding or exploding
	private Image[] explosionPics;
	
    public BossRocket(int xx,int yy) {
    	x=xx;
    	y=yy;
    	img1 = new ImageIcon("pics/bossrocket1.png").getImage();
    	img2 = new ImageIcon("pics/bossrocket2.png").getImage();
    	dTillExplode=100+ (int)(Math.random()*((750-x)-100));					//explodes in random spot between 100p and end of window
   	
   		exploding=false;
   		done=false;
   		imagecounter=0;
   		explodingpicscounter=0;
   		explosionPics= new Image[9];
		for (int i=0; i<9; i++){
			explosionPics[i]=new ImageIcon("pics/explosions/bossrocketexplode/000"+(i+1)+".png").getImage();
		}
    }
    public int getX(){ return x;}
    public int getY(){ return y;}
    public boolean getDone(){return done;}
    
    public void move(){
    	if(dTillExplode<=0){
    		exploding=true;				//if it has moved total distance, then explode
    	}
    	else{
    		y+=4;
    		dTillExplode-=4;
    	}
    }
    
    public void drawImg(Graphics g, GamePanel panel){
    	imagecounter++;	
    	if(exploding==false){
    		if(imagecounter%10>=5){
    			g.drawImage(img1,x-10,y-30,panel);
    		}
    		else{
    			g.drawImage(img2, x-10,y-30,panel);
    		}
    	}
    	else{					//exploding
    		//System.out.println(x+" "+y);
    		Image im=explosionPics[explodingpicscounter/2];
    		g.drawImage(im,x-im.getWidth(null)/2-5,y-im.getHeight(null)/2+60,panel);
    		
    		explodingpicscounter++;
    		if (explodingpicscounter==17){
    			exploding=false;
    			done=true;
    		}
    	}
		
	}
	public boolean collide(UserShip u){
		if(exploding==true){
			if(distance(x,y-20,u.getX(),u.getY())<60){				//assumes 30 radius explosion,-20 for displacement of drawing explosion
				return true;
			}
		
		}
		return false;
	}
	public double distance(double x1,double	y1,double x2,double y2){
		return Math.sqrt(Math.pow(y2-y1,2)+Math.pow(x2-x1,2));
	}
	
}