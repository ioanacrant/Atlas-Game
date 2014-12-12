import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//Ioana Crant
/*OVERALL GAME: This game is named Atlas. It is a top-down shooter set in space. The game begins with a menu, where the user
 *can select "highscores" to view the high scores, or "instructions" to view the instructions. The play button begins the game itself.
 *The objective is to shoot all the enemies and the boss to defeat the level, and then buy enhancements in the store with the coins 
 *that were earned. After 5 levels, or 10 deaths, the game is over and the user can enter their name as a high score. */
 
public class MainGame extends JFrame implements ActionListener{
	MainMenu menu;
	GamePanel game;
	Store store;
	static int level=1;
	static int usershootcounter = 0, enemyshootcounter = 0, enemyloadcounter = 0;		//user/enemyshootcounter delays the user/enemy's shooting
	javax.swing.Timer myTimer;																//enemyloadcounter delays the  enemy arriving on screen					
	ArrayList<ArrayList> enemydata;										//all the enemies in that level
	private Image[] enemyExplodePics;									//enemy stuff is loaded in maingame
	Random r=new Random();
	BufferedReader inf; 
	JOptionPane input;
	Boss boss;
	private boolean storeflag=false;
	public boolean[] storepicked;
	private double bulletdoubleflag=1.0;
	JLabel greybox;
	public static int coins=0;
	public static boolean[] mainstoreused;
	
	//Constructor, is created when user presses play in MainMenu.
    public MainGame() {													
    	super("Atlas");
   		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLayout(new BorderLayout(450,834));
    	setSize(450,834);
    	
    	game=new GamePanel();											//initializing gamepanel
    	game.setSize(443,800);
    	game.setLocation(0,0);
    	add(game);
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	myTimer=new javax.swing.Timer(20,this);
    	myTimer.start();
    	
    	enemydata=new ArrayList<ArrayList>();
    	loadEnemyData(level);
    	
    	enemyExplodePics = new Image[7];								//loads the explosion pictures for enemies
    	for (int i=0; i<7; i++){
			enemyExplodePics[i]=new ImageIcon("pics/explosions/e"+(i+1)+".png").getImage();
		}
		
		mainstoreused=new boolean[]{false,false,false,false,false,false};
   
    	game.setVisible(true);
    	setVisible(true);
    }
    
    //Checks all actions performed and handles the logic for the two main parts of the game (GamePanel and Store). It exits
    //the store if play is selected, and goes to store or gameover screen if the level is over.
    public void actionPerformed(ActionEvent evt){						
    	
    	if(storeflag==true){										//if the user is in the store
    		if(store.getRESUME()==true){							//if the user pressed play to resume
    		
    			//*** these if statements change the game settings depending on which buttons were pressed***
    			//*** if anything from top row was selected (user stuff), changes the userShip/fireEngine/bullet names (for new images) and maxHP
    			//*** if anything from bottom row selected (bullet stuff), it changes the bullet damage, homing flag, and double speed
    			
    			if(store.getNewUserName()==null && store.getNewBulletName()==null){			//nothing selected
    				game.setNewUserShip(new UserShip(game.userShip.getName(),game.userShip.getFireName(),game.userShip.getBulletName(), 200, 600,game.getExplodePics(),game.userShip.getMaxHP()));
    			}
    			if(store.getNewUserName()==null && store.getNewBulletName()!=null){			//only bullet selected
    				game.setNewUserShip(new UserShip(game.userShip.getName(),game.userShip.getFireName(),store.getNewBulletName(), 200, 600,game.getExplodePics(),game.userShip.getMaxHP()));
    				game.setDefaultBulletDamage(store.getNewDamage());
    			}
    			if(store.getNewUserName()!=null && store.getNewBulletName()==null){			//only ship selected
    				game.setNewUserShip(new UserShip(store.getNewUserName(),store.getNewFireName(),game.userShip.getBulletName(), 200, 600,game.getExplodePics(),store.getNewHP()));
    			}
    			if(store.getNewUserName()!=null && store.getNewBulletName()!=null){			//both selected
    				game.setNewUserShip(new UserShip(store.getNewUserName(),store.getNewFireName(),store.getNewBulletName(), 200, 600,game.getExplodePics(),store.getNewHP()));
    				game.setDefaultBulletDamage(store.getNewDamage());
    			}
    			
    			if (store.getBulletHoming()==true){
    				game.setBulletHoming(true);
    			}
    			mainstoreused=store.getStoreUsed();					//main store used is for selected things to grey out next store
    			bulletdoubleflag=store.getBulletDouble();
    			usershootcounter=0;									//resets usercounter
    			store.setVisible(false);							//sets store invisible, game back to visible
 				game.setVisible(true);			
 				setVisible(true);
 				storeflag=false;	
    		}
    	}
    	
    	else{				//in the game
    		usershootcounter++;
	    	enemyshootcounter++;
	    	
	    	if (usershootcounter==20*bulletdoubleflag){				//if doublespeed was bought, itll be 20*0.5=10;
	    		game.shootUserBullet();
	    		usershootcounter=0;
	    	}
	    	
	    	if (enemyshootcounter==50){								//counter for both shooting enemy and boss bullets
	    		game.shootEnemyBullet();
	    		game.shootBossBullet();
	    		enemyshootcounter=0;
	    	}
	 
	    	enemyloadcounter++;
	    	if (enemyloadcounter==75){
	    		if (enemydata.size()>0){							//if there are enemies left to add, add them to the game
	    			game.arriveEnemy(enemydata.get(0));					//according to the counter
	    			enemydata.remove(0);
	    		}
	    		enemyloadcounter=0;	
	    	}
	    	
	    	if (game.gameOver()){									//starts increasing frame to show gameover screen
	    		game.frame++;										
	    	}
	    	if (game.go==true && game.frame==100){					//if gameoverscreen has been shown for long enough
	    		myTimer.stop();
				game.setEnabled(false);
				game.setVisible(false);
				setVisible(false);
				menu=new MainMenu();								//goes back to menu
				updateHighScore(game.score); 						//asks user for name to add high score
				level=1;
				game.level=1;
	    	}
	    	
	    	if (game.levelOver()==true && game.gameOver()==false){		//between levels		
	    		store=new Store();									//creates new store
	    		store.setStoreUsed(mainstoreused);
	    		game.setVisible(false);
	    		setVisible(false);
	    		game.levelUp();
	    		game.levelover = false;	
	    		storeflag=true;
	    		level++;
	    		if (level==2){
		   			loadEnemyData(2);
	    		}
	    		if (level==3){
		   			loadEnemyData(3);
	    		}
	    		if (level==4){
		   			loadEnemyData(4);
	    		}
	    		if (level==5){
		   			loadEnemyData(5);
	    		}
	    	}
	    	
	    	game.moveUserShip();
	    	game.moveEnemy();
	    	game.moveBullets();
	    	game.collide();
	    	game.removeDead();
	    	game.repaint();
    	}
    }
    
    //Takes data file depending on level and creates an arraylist of parameters for new enemies to be introduced into the game
    //data file : imgname, fireimgname, bulimgname, goalx, goaly, x, y, type, HP
    public void loadEnemyData(int level){
    	try{
    		inf=new BufferedReader(new FileReader("data/level"+level+".txt"));
    		int numEnemies=Integer.parseInt(inf.readLine());
    		inf.readLine();										//filler line
    		for (int i=0; i<numEnemies; i++){
    			String [] line=inf.readLine().split(" ");
    			ArrayList<Object> e = new ArrayList<Object>();
    			e.add(line[0]);
    			e.add(line[1]);
    			e.add(line[2]);
    			e.add(Integer.parseInt(line[3]));
    			e.add(Integer.parseInt(line[4]));
    			e.add(Integer.parseInt(line[5]));
    			e.add(Integer.parseInt(line[6]));
    			e.add(line[7]);
    			e.add(Integer.parseInt(line[8]) );
    			
    			enemydata.add(e);
    		}
    			
    	}
    	catch(IOException e){}
    }
    
    //Allows user to enter name, then goes through old scores and places user score in correct place and rewrites file
    public void updateHighScore(int score){
    	String name=input.showInputDialog("Enter your name");
    	boolean flag=false;
    	BufferedReader oldscores=null;
    	try{
    		oldscores=new BufferedReader(new FileReader("data/highscores.txt"));
    	}
    	catch(IOException e){}
    	
		String[] newscores=new String[10];
		int counter=0;
		while (true){
			try{												//reads file and places score into list
				String line= oldscores.readLine();
				int s=Integer.parseInt(line.split(" ")[1]);
				if (s>score || flag==true){						//flags says if score was already inserted into highscores list
					newscores[counter]=line;
				}
				if(s<=score && flag==false){
					newscores[counter]=name+" "+score;
					flag=true;
				}
			}
			catch(IOException e){}
			counter++;
			if(counter==10){
				break;
			}	
		}

		BufferedWriter outf;
		try{
			outf=new BufferedWriter(new FileWriter("data/highscores.txt"));			//writes new scores to file
			for (int i=0; i<10; i++){
				menu.setScoreList(i,newscores[i]);
				outf.write(newscores[i]);
				outf.newLine();
			}
			outf.close();	
		}
		catch(IOException e){}
	}
	
    public static void main(String[]args) throws IOException{
    	MainGame MAINGAME=new MainGame();
    }
    
}
