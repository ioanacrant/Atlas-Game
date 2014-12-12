import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//Ioana Crant
/* The store is displayed after every level and allows users to buy enhancements using coins. The enhancements available are 3 different 
 *ships with different HP, a bullet with more damage, shooting at twice the pace, and homing which aims at the enemies*/
 
public class Store extends JFrame implements ActionListener{
	CusButton resumeButton,user02Button,user03Button,user04Button,bullet02Button,bulletdoubleButton,bullethomingButton;
	CusButton[] buttonarr;			//holds the buttons
	JLabel coinsText;				//shows how many coins you have
	JLabel bg;
	boolean RESUME=false;			//true if user presss play
	String newUserName, newBulletName;		//changes if user buys a ship or bullet		
	int newHP,newDamage;
	double bulletdoubleflag;
	boolean bullethomingflag=false;
	private boolean[] storeused;		//used to make boxes unavailable
	private int[] prices;
	
	public Store(){
		super("Atlas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
    	setSize(450,834);
    	setResizable(false);
    	
    	coinsText=new JLabel(""+MainGame.coins);
    	coinsText.setFont(new Font("Courier", Font.PLAIN, 60));
    	coinsText.setForeground(Color.yellow);
    	coinsText.setSize(120,60);
    	coinsText.setLocation(305,240);
    	add(coinsText);

    	resumeButton=new CusButton("pics/menu/startbutton1");		
    	resumeButton.setLocation(230,720);
    	resumeButton.setSize(170,75);
    	resumeButton.addActionListener(this);
   		add(resumeButton);
   		resumeButton.setVisible(true);
    	
    	user02Button=new CusButton("pics/store/storebuttonuser02");		
    	user02Button.setLocation(67,356);
    	user02Button.setSize(96,168);
    	user02Button.addActionListener(this);
    	add(user02Button);
    	
    	user03Button=new CusButton("pics/store/storebuttonuser03");		
    	user03Button.setLocation(179,356);
    	user03Button.setSize(96,168);
    	user03Button.addActionListener(this);
    	add(user03Button);
    	
    	user04Button=new CusButton("pics/store/storebuttonuser04");		
    	user04Button.setLocation(290,356);
    	user04Button.setSize(96,168);
    	user04Button.addActionListener(this);
    	add(user04Button);
    	
    	bullet02Button=new CusButton("pics/store/storebuttonbullet02");		
    	bullet02Button.setLocation(67,563);
    	bullet02Button.setSize(96,168);
    	bullet02Button.addActionListener(this);
    	add(bullet02Button);
    	
    	bulletdoubleButton=new CusButton("pics/store/storebuttonbulletdouble");		
    	bulletdoubleButton.setLocation(179,563);
    	bulletdoubleButton.setSize(96,168);
    	bulletdoubleButton.addActionListener(this);
    	add(bulletdoubleButton);
    	
    	bullethomingButton=new CusButton("pics/store/storebuttonbullethoming");		
    	bullethomingButton.setLocation(290,563);
    	bullethomingButton.setSize(96,168);
    	bullethomingButton.addActionListener(this);
    	add(bullethomingButton);
      	
    	bg= new JLabel(new ImageIcon("pics/store/storebackground.png"));
    	bg.setSize(450,834);
    	bg.setLocation(0,0);
    	add(bg);
    	
	    		
    	buttonarr=new CusButton[]{user02Button,user03Button,user04Button,bullet02Button,bulletdoubleButton,bullethomingButton};
    //	storeused=new boolean[]{false,false,false,false,false,false};
    //	newstoreused=new boolean[]{false,false,false,false,false,false};
    	prices=new int[]{20,35,50,15,30,30};
    	setStoreUsed(MainGame.mainstoreused);
    	changeStoreAvailable(MainGame.coins);						//changes storeused to false if you dont have enough money
    	greyOutButtons();											//makes buttons unavailable
    	bulletdoubleflag=1.0;
  		
    	setVisible (true);
	}
	
	public void actionPerformed(ActionEvent e){
    	Object src = e.getSource();
    
    	if(src==resumeButton){
    										
    		
    		RESUME=true;											
    		
    	}
    	if(src==user02Button){
    		if(storeused[0]==false && MainGame.coins>prices[0]){			//cant buy grey stuff or too rich stuff
    			newUserName="pics/user02.png";
    			newHP=350;
    			storeused[0]=true;						//can switch between 2 ships
    			storeused[1]=false;
    			storeused[2]=false;
    			MainGame.coins=MainGame.coins-prices[0];
    			changeStoreAvailable(MainGame.coins);		//makes things grey that are no longer avail
    		}
    		
    	}
    	if(src==user03Button ){
    		if(storeused[1]==false&& MainGame.coins>prices[1]){	
    			newUserName="pics/user03.png";
    			newHP=500;
    			storeused[0]=false;
    			storeused[1]=true;
    			storeused[2]=false;
    			MainGame.coins=MainGame.coins-prices[1];
    			changeStoreAvailable(MainGame.coins);
    		}
    	}
    	if(src==user04Button){
    		if(storeused[2]==false && MainGame.coins>prices[2]){	
    			newUserName="pics/user04.png";
    			newHP=700;
    			storeused[0]=false;
    			storeused[1]=false;
    			storeused[2]=true;
    			MainGame.coins=MainGame.coins-prices[2];
    			changeStoreAvailable(MainGame.coins);
    		}
    	}
    	if(src==bullet02Button){
    		if(storeused[3]==false&& MainGame.coins>prices[3]){	
    			newBulletName="pics/bullet02.png";
    			newDamage=-75;
    			storeused[3]=true;
    			MainGame.coins=MainGame.coins-prices[3];
    			changeStoreAvailable(MainGame.coins);
    
    		}
    	}
    	if(src==bulletdoubleButton){
    		if(storeused[4]==false&& MainGame.coins>prices[4]){	
    			bulletdoubleflag=0.5;
    			storeused[4]=true;
    			MainGame.coins=MainGame.coins-prices[4];
    			changeStoreAvailable(MainGame.coins);
    		}
    	}
    	if(src==bullethomingButton){
    		if(storeused[5]==false&& MainGame.coins>prices[5]){	
    			bullethomingflag=true;
    			storeused[5]=true;
    			MainGame.coins=MainGame.coins-prices[5];
    			changeStoreAvailable(MainGame.coins);
    		}
    	}
    	greyOutButtons();
    	coinsText.setText(""+MainGame.coins);
	}
	
	public boolean getRESUME(){
		return RESUME;
	}
	public String getNewFireName(){
		return newUserName.substring(0,newUserName.indexOf("."))+"enginefire.png";
	}
	public String getNewUserName(){
		return newUserName;
	}
	public String getNewBulletName(){
		return newBulletName;
	}
	public int getNewHP(){
		return newHP;
	}
	public int getNewDamage(){
		return newDamage;
	}
	public double getBulletDouble(){
		return bulletdoubleflag;
	}
	public boolean getBulletHoming(){
		return bullethomingflag;
	}

	public  void changeStoreAvailable(int newcoins){			//is called after a purchase is made to get rid of things
		for (int i=0;i<6;i++){										//that user can no longer buy
			if(newcoins<prices[i]){
				storeused[i]=true;
			}
		}
	}

	public void greyOutButtons(){			//doesnt allow user to use buttons set as true
		for(int i=0;i<6;i++){
			if(storeused[i]==true){
				buttonarr[i].setEnabled(false); 
			}
			else{
				buttonarr[i].setEnabled(true);
				
			}
				
		}
	}
	public boolean[] getStoreUsed(){return storeused;}
	public void setStoreUsed(boolean[] arr){
		storeused=new boolean[6];
		for(int i=0;i<6;i++){
			storeused[i]=arr[i];
		}
	}
	public static void main(String[]args){
		new Store();
	}
	
}
class CusButton extends JButton{				//not custbutton
	public CusButton(String name){
		super();
		ImageIcon a = new ImageIcon(name+".png");
		this.setIcon(a);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);				
	}
}

