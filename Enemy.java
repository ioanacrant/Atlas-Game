import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//only difference between Enemy and Ship is that Enemy's have goal locations. They start of the screen at (x,y) and move to
//(goalx, goaly) on screen.
public class Enemy extends Ship{
	private int goalx, goaly;
	private String type;
    public Enemy(String name, String name2, String name3, int gx, int gy, int x, int y, String t,Image[] ep, int h) {
    	super(name, name2, name3, x,y,ep,h);
    	goalx=gx;
    	goaly=gy;
    	type=t;
    	Random r=new Random();
    }
    public int getGoalX(){return goalx;}
    public int getGoalY(){return goaly;}
    
    public String getType(){return type;}
    
    
}