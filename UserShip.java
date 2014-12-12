import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//only difference between UserShip and Ship is that UserShip can become invincible after dying.
public class UserShip extends Ship{
	private boolean invincible;			//invincible when comes from dead
	private Image invincibleimg;
	private int invinciblecounter;		//counts how long invincible for
	private int maxHP;
	private String name,bulletname,firename;
	public UserShip( String name, String firename, String bulname, int x, int y, Image[] ep, int hp){
	 	
		super(name,firename,bulname, x,y,ep,hp);
		invincible=false;
		invincibleimg=new ImageIcon(name.substring(0,name.indexOf("."))+"invincible.png").getImage();
		invinciblecounter=0;
		maxHP=hp;
		this.name=name;
		this.firename=firename;
		bulletname=bulname;
	}
	public void setInvincible(){
    	invincible=true;
    }
    public boolean isInvincible(){
    	return invincible;
    }
    public int getMaxHP(){
    	return maxHP;
    }
    public String getName(){
    	return name;
    }
    public String getBulletName(){
    	return bulletname;
    }
    public String getFireName(){
    	return firename;
    }
     public void drawImg(Graphics g, GamePanel panel){
    	if (exploding==false){
    		if(invincible==false){
    			g.drawImage(img,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel);
    		}
    		else{			//its invincible!!
    			invinciblecounter++;
    			if(invinciblecounter==200){				//after 200 frames, no longer invincible
    				invincible=false;
    			}
    			g.drawImage(invincibleimg,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel);
    		}
    		
    		enginefirecounter++;
			if (enginefirecounter%5>3){
				g.drawImage(engineFireImg1,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel); 
			}
			else{
				g.drawImage(engineFireImg2,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel); 	
			}
    	}
    	else{								//its exploding!!!
    		if(explodingpicscounter<27){
    			Image im=explosionPics[explodingpicscounter/4];
    			g.drawImage(im,x-im.getWidth(null)/2,y-im.getWidth(null)/2,panel);
    		}
  
    		explodingpicscounter++;
    		if (explodingpicscounter>=4*explosionPics.length-1){
    			dead=true;
    			exploding=false;
    		}
    	}		
	} 
}