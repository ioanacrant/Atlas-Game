import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//Ioana Crant
//Boss is brought into the game at the end of the level. It moves onto screen to baseline and then moves left and right.
//This class moves and draws the boss.

public class Boss{
	private int x,y,goaly;				//x,y is center of ship
	private int len, height;
	private int explodingpicscounter;
	private int dir; 							//0 is left, 1 is right
	
	private Image img;	
	private Image[] explosionPics;				
	
	private boolean exploding,atBase,dead;			//is it exploding or dead or at the line its supposed to be at
	private int HP;
	
    public Boss( String name,  int goaly,int x, int y,  Image[] ep, int hp) {
    	this.x = x;
    	this.y = y;
    	this.goaly = goaly;
    	explodingpicscounter=0;
    	
    	img=new ImageIcon(name).getImage();
   
    	
    	len = img.getWidth(null);
    	height = img.getHeight(null);
    	
    	explosionPics=ep;
    	dead=false;
    	exploding=false;
    	HP=hp;
    	dir=1;
    
    	
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public int getGoalY(){return goaly;}
    public int getLen(){return len;}
    public int getHeight(){return height;}
    public int getHP(){return HP;}
    public Image[] getExplosionPics(){return explosionPics;}
    public boolean isDead(){return dead;}
    public boolean isAtBase(){return atBase;}
    
    public void setHP(int h){HP=h;}
    public void changeHP(int h){HP+=h;}
 
    public void setExploding(boolean e){exploding=e;}
    
    public Point getCornerCoord(){
    	return new Point((int)(x-len/2),(int)(y-height/2));
    }
    
    public void moveToBase(){
    	y+=1;
    	if(y==goaly){
    		atBase=true;
    	}
    }
    public void move(){
    	if(exploding==false){
    		if(x-len/2<-10 || x+len/2>440){
    			dir=1-dir;		//0 to 1, 1 to 0
    		}
    		if(dir==1){			//right
    			x+=2;	
    		}
    		else if(dir==0){		//left
    			x-=2;
    		}
    	}
    }
    
    public void drawImg(Graphics g, GamePanel panel){
    	if (exploding==false){
    		g.drawImage(img,(int)this.getCornerCoord().getX(),(int)this.getCornerCoord().getY(),panel);
			
    	}
    	else{								//its exploding!!!
    		if(explodingpicscounter<56){
    			g.drawImage(explosionPics[explodingpicscounter/4],x-len/2-50,y-len/2-50,panel);
    		}
    		
    		explodingpicscounter++;
    		
    		if (explodingpicscounter>=4*explosionPics.length-1){
    			dead=true;
    			exploding=false;
    		}
    
    	}
		
		
	}
    
}
	 
