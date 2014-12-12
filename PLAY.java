import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//Ioana Crant
//This class creates a MainMenu which is the menu that the user sees when starting the game, and allows them to select play,
//instructions or high scores.
 
public class MainMenu extends JFrame implements ActionListener{
    CustButton startButton,instructButton,backButton,scoreButton;			//CustButton is at bottom of page
    JLabel bg,instructPage,scorePage;				//background for 3 different pages
    JLabel[] scoreList;								//list of high scores as jLabels to display when user selects high score screen
    BufferedReader scoreInf;
    boolean newgame=false;							//turns true if user presses play
   
    
    public  MainMenu(){
    	super("Atlas");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	try{
    		scoreInf=new BufferedReader(new FileReader("data/highscores.txt"));
    	}
    	catch(IOException e){}
    	
    	setLayout(null);
    	setSize(450,834);
    	setResizable(false);
		
    	startButton=new CustButton("pics/menu/startbutton");
    	startButton.setLocation(130,420);
    	startButton.setSize(170,75);
    	startButton.addActionListener(this);
    	add(startButton);
    	
    	instructButton=new CustButton("pics/menu/instructbutton");
    	instructButton.setLocation(130,420+90);
    	instructButton.setSize(75,75);
    	instructButton.addActionListener(this);
    	add(instructButton);
    	
    	scoreButton= new CustButton("pics/menu/scorebutton");
    	scoreButton.setLocation(130+80,420+90);
    	scoreButton.setSize(75,75);
    	scoreButton.addActionListener(this);
    	add(scoreButton);
    	
    	backButton= new CustButton("pics/menu/backbutton");
    	backButton.setLocation(44,740);				
    	backButton.setSize(80,50);
    	backButton.addActionListener(this);
   		add(backButton);
   		backButton.setVisible(false);									//only can go back on instruct and high score page
    	
    	scoreList=new JLabel[10];										//creates jLabels of high scores (text only)
    	try{
    		for (int i=0; i<10; i++){
	   			scoreList[i]=new JLabel();
	   			scoreList[i].setSize(400,30);
	   			scoreList[i].setFont(new Font("Courier", Font.PLAIN, 28));
	   			scoreList[i].setText(scoreInf.readLine());
	   			scoreList[i].setLocation(80,350+i*36);
	   			scoreList[i].setVisible(false);
	   			add(scoreList[i]);
	   			
	   		} 
    	}
    	catch(IOException e){}
    	
    	instructPage= new JLabel(new ImageIcon("pics/menu/instructpage.png"));
    	instructPage.setSize(450,834);
    	add(instructPage);
    	instructPage.setVisible(false);

    	scorePage= new JLabel(new ImageIcon("pics/menu/scorepage.png"));
    	scorePage.setSize(450,834);
    	add(scorePage);
    	scorePage.setVisible(false);
	   	
	   	bg= new JLabel(new ImageIcon("pics/menu/background.png"));
    	bg.setSize(450,834);
    	add(bg);
    	bg.setVisible(true);
    			
    	setVisible (true);
    }	
    	
    //Changes pages depending on buttons pressed, or creates new MainGame if play pressed
    public void actionPerformed(ActionEvent e){
    	
    	Object src = e.getSource();
    	
    	if(src==startButton){	
    		new MainGame();
    		setVisible(false);						//menu is invisible
    	}
    	
    	else if(src==instructButton){
    		instructPage.setVisible(true);
    		bg.setVisible(false);
    		scorePage.setVisible(false);
    		backButton.setVisible(true);
    		startButton.setVisible(false);
    		instructButton.setVisible(false);
    		scoreButton.setVisible(false);
    	}
    	
    	else if(src==scoreButton){
    		setScoreListVisible(true);
    		scorePage.setVisible(true);
    		bg.setVisible(false);
    		instructPage.setVisible(false);
    		backButton.setVisible(true);
    		startButton.setVisible(false);
    		instructButton.setVisible(false);
    		scoreButton.setVisible(false);
    	}
    	
    	else if(src==backButton){
    		bg.setVisible(true);
    		scorePage.setVisible(false);
    		instructPage.setVisible(false);
    		backButton.setVisible(false);
    		startButton.setVisible(true);
    		instructButton.setVisible(true);
    		scoreButton.setVisible(true);
    		setScoreListVisible(false);
    	}
    }
    
    //Helper function to set the high score JLabels visible. 
   	public void setScoreListVisible(boolean b){
		for (int i=0; i<10; i++){
			scoreList[i].setVisible(b);
		}
	}
	public void setScoreList(int ind,String b){
		scoreList[ind].setText(b);
	}
	public static void main(String[]args){
		new MainMenu();
	}
}

//Makes JButtons easier, adds different image if hovering
class CustButton extends JButton{									
	public CustButton(String name){
		ImageIcon a = new ImageIcon(name+"1.png");
		ImageIcon b = new ImageIcon(name+"2.png");
		this.setIcon(a);
		this.setPressedIcon(b);
		this.setRolloverIcon(b);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		
		
	}
}
