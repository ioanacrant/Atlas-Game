import javax.swing.*;
import java.awt.event.*;
import java.awt.*;	
import java.util.*;
import java.io.*;

//Ioana Crant
/*GamePanel controls the actual game-playing aspect of Atlas. The specific actions that are handled are drawing, putting enemies/bosses
 *into the game, removing dead objects (ships and bullets), colliding ships and bullets, changing HP/score/coins, moving the user/enemy/
 *boss, shooting user/enemy/boss bullets, and checking if the level/game is over */
 
class GamePanel extends JPanel implements KeyListener,ActionListener{
	UserShip userShip;
	private Object[] defaultUserShipSettings;			//names of images and HP that are currently being used by userShip
	private int defaultBulletDamage;					//default bullet damage of usership
	javax.swing.Timer gameTimer;
	private Boss boss;
	private boolean[] keys;
	private ArrayList<Bullet> userbullets,enemybullets;
	private ArrayList<BossRocket> bossrockets;
	private ArrayList<Enemy> enemies;
	private Image userShipImage, userShipBulletImage;
	private Image[] explodePics,bossExplodePics;		//explodePics is for enemies and usership
	private Image background,gameoverscreen;
	private int lives;
	int level;
	private MainGame mg;
	boolean levelover;
	private boolean bullethoming=false;					//if homing was bought
	int score,frame=0;
	boolean go=false;									//gameover flag
	
	public GamePanel(){
		super();
		
		keys=new boolean[2000];
		setFocusable(true);
		grabFocus();
		addKeyListener(this);
	
		userbullets=new ArrayList<Bullet>();
		enemybullets= new ArrayList<Bullet>();
		bossrockets= new ArrayList<BossRocket>();
		enemies=new ArrayList<Enemy>();
		
		lives=10;
		
		explodePics= new Image[7];	
		for (int i=0; i<7; i++){
			explodePics[i]=new ImageIcon("pics/explosions/e"+(i+1)+".png").getImage();
		}
		bossExplodePics= new Image[14];
		for (int i=0; i<14; i++){
			bossExplodePics[i]=new ImageIcon("pics/explosions/bossexplode/explosion300"+(i+1)+".png").getImage();
		}
		
		userShip = new UserShip("pics/user01.png","pics/user01enginefire.png", "pics/bullet01.png", 200, 600,explodePics,200);
		userShip.setSpeed(5);
		
		defaultUserShipSettings= new Object[] {"pics/user01.png","pics/user01enginefire.png", "pics/bullet01.png",200};
		defaultBulletDamage=-50;				
		boss=null;
		
		background=new ImageIcon("pics/spacebackground.png").getImage();
		gameoverscreen=new ImageIcon("pics/menu/gameover.png").getImage();
		
		level=1;
		levelover = false;
	
		score=0; 
			
		gameTimer=new javax.swing.Timer(20,this);
    	gameTimer.start();
			
		repaint();
	}
	
	//Draws the bullets (calls draw in Bullet class), enemies, and userShip and boss if there
	public void paintComponent(Graphics g){
		g.drawImage(background,0,0,this);

		for (Bullet b:userbullets){
			b.drawImg(g,this);
		}	
		for (Bullet b:enemybullets){
			b.drawImg(g,this);
		}	
		for (BossRocket b:bossrockets){
			b.drawImg(g,this);
		}
		for (Enemy enem:enemies){
			enem.drawImg(g,this);		
		}	
		if (boss!=null){
			boss.drawImg(g,this);
		}
		userShip.drawImg(g,this);
	
		if(go==true || frame>1){
			g.drawImage(gameoverscreen,0,0,this);
		}
 		
 		g.setColor(new Color(122,116,213));							//bottom right corner stuff
 		g.setFont(new Font("Courier", Font.PLAIN, 16));
 		g.drawString(""+lives,388,776);
 		g.setFont(new Font("Courier", Font.PLAIN, 13));
 		g.drawString(""+score,383,755);
 		g.drawString(""+MainGame.coins,383,735);
 	
 		g.setColor(new Color(255,0,0));								//draws HP bar in bottom right
 		if(userShip.getMaxHP()>0){
 			int hpheight=(int)((double)userShip.getHP()/(double)userShip.getMaxHP()*50.0);
 			g.fillRect(428,780-hpheight,10,hpheight);
 		}	
	}
	
	//Gets called from MainGame, after counter is done a new enemy arrives and is put into the enemy list
	public void arriveEnemy(ArrayList a){
		if (!a.get(7).equals("boss")){	
			enemies.add(new Enemy(a.get(0)+"",a.get(1)+"",a.get(2)+"",(int)a.get(3),(int)a.get(4),(int)a.get(5),(int)a.get(6),a.get(7)+"",explodePics,(int)a.get(8)));
		}
		else{	
			boss=new Boss(a.get(0)+"",(int)a.get(3),(int)a.get(5),(int)a.get(6),bossExplodePics,(int)a.get(8));
		}
		
	}
	
	//removes dead things
	public void removeDead(){
		for (int i=0;i<enemies.size();i++){		//enemies
			Enemy e=enemies.get(i);
			if (e.getHP()<=0){
				e.setExploding(true);	
			}
			if (e.isDead()){					//enemy only becomes dead after the explosion is over
				enemies.remove(i);				//removes enemy from list
				score+=50;
				MainGame.coins+=1;
			}
		}
		
		if (userShip.getHP()<=0){				//usership
			userShip.setExploding(true);		
		}
		if (userShip.isDead()){						//creates new usership using default settings
			userShip = new UserShip(defaultUserShipSettings[0]+"",defaultUserShipSettings[1]+"", defaultUserShipSettings[2]+"", 200, 600,explodePics,(int)defaultUserShipSettings[3]);
			userShip.setSpeed(5);
			userShip.setInvincible();				//usership is initially invincible 
			lives--;
		}
		if(boss!=null){
			if(boss.getHP()<=0){
				boss.setExploding(true);			//checking if boss is dead happens in levelOver	
			}
		}
		
	}
	
	//Checks if bullets collide with ships
	public void collide(){
		//userbullet collides with enemy
		for (int userBulletIndex=0 ; userBulletIndex<userbullets.size() ; userBulletIndex++){
			for (int enemyIndex=0 ; enemyIndex<enemies.size() ; enemyIndex++){
				if (userbullets.size()>0){
					Bullet b = userbullets.get( userBulletIndex );
					Enemy e = enemies.get( enemyIndex );
					if (b.collide(e)==true){
						userbullets.remove(userBulletIndex);
						e.changeHP(defaultBulletDamage);
						score+=20;
						userBulletIndex=Math.max(userBulletIndex-1,0);			//to prevent index from going to -1
						enemyIndex=Math.max(enemyIndex-1,0);
					}
				}
			
			}
		}
		
		//enemybullet collides with user
		for (int enemyIndex=0;enemyIndex<enemybullets.size();enemyIndex++){
			Bullet b = enemybullets.get(enemyIndex);
			if(userShip.isInvincible()==false && b.collide(userShip)==true){
				userShip.changeHP(-50);
				enemybullets.remove(enemyIndex);
			
			}
		}
		
		//both boss collide w user and user collide w boss
		if (boss!=null){
			for (int i=0;i<bossrockets.size();i++){
				BossRocket b = bossrockets.get(i);			
				if(userShip.isInvincible()==false && b.collide(userShip)==true){
					userShip.changeHP(-100);
					
			
				}
			}
			for (int i=0;i<userbullets.size();i++){
				Bullet b = userbullets.get(i);
				if(b.collide(boss)==true){
					boss.changeHP(defaultBulletDamage);
					score+=50;
					userbullets.remove(i);
				
				}
			}
		
		}
		
	}
	
	//Checks if arrow keys are being pressed, then calls move in Ship to change x,y coords
	public void moveUserShip(){
		if(keys[KeyEvent.VK_RIGHT] && (userShip.getX()+(userShip.getLen()/2))<getWidth()){
			userShip.move(1,0);
		}
		if(keys[KeyEvent.VK_LEFT] && userShip.getX()-userShip.getLen()/2>0){
			userShip.move(-1,0);
		}
		if(keys[KeyEvent.VK_UP] && userShip.getY()-(userShip.getHeight()/2)>0){
			userShip.move(0,-1);
		}
		if(keys[KeyEvent.VK_DOWN] && userShip.getY()+(userShip.getHeight()/2)<getHeight()){		
			userShip.move(0,1);
		}	
	}
	
	//Moves enemies
	public void moveEnemy(){
		for (Enemy e:enemies){
			if (e.getY()<e.getGoalY()){ 		//if enemy hasn't reached goal position, it moves towards it
				int dx=(e.getGoalX()-e.getX())/Math.max(e.getGoalX()-e.getX(), e.getGoalY()-e.getY());
				int dy=(e.getGoalY()-e.getY())/Math.max(e.getGoalX()-e.getX(), e.getGoalY()-e.getY());
				e.move(dx,dy);
				
			}
		}
		if (boss!=null){
			moveBoss();
		}
	}
	
	//Moves boss
	public void moveBoss(){
	
		if(boss.isAtBase()==false){
			boss.moveToBase();
		}
		else{
			boss.move();
		}
	}
	
	//CHecks if space bar is pressed, then adds a bullet to the ones that user shot
	public void shootUserBullet(){
		if(keys[KeyEvent.VK_SPACE]){
			if(bullethoming==true && (enemies.size()>0 || boss!=null)){				//need enemies to home, homie
				
				double dx,dy,slope;
				int ux=userShip.getX(), uy=userShip.getY();
				int ex=0,ey=0;	
					
				if(enemies.size()>0 && boss==null){
					Enemy enem=enemies.get(0);
					for(int i=0;i<enemies.size();i++){					//chooses enemy closest to bottom
						if (enemies.get(i).getY()>enem.getY()){
							enem=enemies.get(i);
						}
					}
					ex=enem.getX();
					ey=enem.getY();
				}
				if(enemies.size()==0 && boss!=null){
					ex=boss.getX();
					ey=boss.getY();
				}
										//because errors if i dont
				if(boss!=null && enemies.size()>0){
					Enemy enem=enemies.get(0);
					for(int i=0;i<enemies.size();i++){					//chooses enemy closest to bottom
						if (enemies.get(i).getY()>enem.getY()){
							enem=enemies.get(i);
						}
					}
					if(enem.getY()>boss.getY()){
						ex=enem.getX();
						ey=enem.getY();
					}
					else{
						ex=boss.getX();
						ey=boss.getY();
					}
				}
				
				if (ex-ux==0 || ey-uy==0){					//direction that bullet should travel in to hit enemy
					dx=ex-ux;
					dy=ey-uy;
				}
				else{
					slope=(ey-uy)/(ex-ux);
					dx=(double)(ex-ux)/Math.max(Math.abs(ex-ux),Math.abs(ey-uy));
					dy=(double)(ey-uy)/Math.max(Math.abs(ex-ux),Math.abs(ey-uy));
				}
				userbullets.add(new Bullet(userShip.getBulletImage(),"user",userShip.getX()-23,userShip.getY()-22,dx,dy,12));
				userbullets.add(new Bullet(userShip.getBulletImage(),"user",userShip.getX()+17,userShip.getY()-22,dx,dy,12));
			}
			else{						//not homing, just striaght shoot
				userbullets.add(new Bullet(userShip.getBulletImage(),"user",userShip.getX()-23,userShip.getY()-22,0,-1,12));
				userbullets.add(new Bullet(userShip.getBulletImage(),"user",userShip.getX()+17,userShip.getY()-22,0,-1,12));
			}
		}
	}
	
	public void shootEnemyBullet(){
		double dx,dy,slope;
		for (Enemy enem:enemies){
			if (enem.getType().equals("straight") && enem.isExploding()==false){			//straight is actually directed towards user
				int ux=userShip.getX(), uy=userShip.getY();				
				int ex=enem.getX(), ey=enem.getY();
				
				if (ux-ex==0 || uy-ey==0){
					dx=ux-ex;
					dy=uy-ey;
				}
				else{
					slope=(uy-ey)/(ux-ex);
					dx=(double)(ux-ex)/Math.max(Math.abs(ux-ex),Math.abs(uy-ey));			//displacement of bullet
					dy=(double)(uy-ey)/Math.max(Math.abs(ux-ex),Math.abs(uy-ey));
				}
				enemybullets.add(new Bullet(enem.getBulletImage(),"straight",enem.getX()-22,enem.getY()+10,dx,dy,10));
				enemybullets.add(new Bullet(enem.getBulletImage(),"straight",enem.getX()+18,enem.getY()+10,dx,dy,10));	
			}
			
			else if (enem.getType().equals("circle") && enem.isExploding()==false){					//sprays 5 bullets
				enemybullets.add(new Bullet(enem.getBulletImage(),"circle",enem.getX(),enem.getY(),0,1,8)); 	
				enemybullets.add(new Bullet(enem.getBulletImage(),"circle",enem.getX(),enem.getY(),Math.sin(15.0*Math.PI/180.0),Math.cos(15.0*Math.PI/180.0),8)); 	//15 deg right
				enemybullets.add(new Bullet(enem.getBulletImage(),"circle",enem.getX(),enem.getY(),-Math.sin(10.0*Math.PI/180.0),Math.cos(15.0*Math.PI/180.0),8)); 	//15 deg left
				enemybullets.add(new Bullet(enem.getBulletImage(),"circle",enem.getX(),enem.getY(),Math.sin(30*Math.PI/180),Math.cos(30*Math.PI/180),8)); 		//30 deg right
				enemybullets.add(new Bullet(enem.getBulletImage(),"circle",enem.getX(),enem.getY(),-Math.sin(30*Math.PI/180),Math.cos(30*Math.PI/180),8)); 		//30 deg left
			}
			
		}
	}
	
	public void shootBossBullet(){
		if (boss!=null && boss.isDead()==false){
			bossrockets.add(new BossRocket(boss.getX()-35,boss.getY()+150));	
			bossrockets.add(new BossRocket(boss.getX()+30,boss.getY()+150));		
		}
		
	}
	
	//Goes through userbullets and enemybullets and moves them, removes if it goes above screen
    public void moveBullets(){
        for (int i=0;i<userbullets.size();i++){			//bullets that user shoots
            Bullet b = userbullets.get(i);
            if(((-10<b.getY() && b.getY()<getHeight()+10 && -10<b.getX() && b.getX()<getWidth()+10 ))){	
				b.move();	
			}
			else{				//remove if out of bounds
				userbullets.remove(i);
				i--;
			}
        }
        for (int i=0;i<enemybullets.size();i++){		//bullet that enemies shoot
            Bullet b = enemybullets.get(i);
            if((-10<b.getY() && b.getY()<getHeight()+10 && -10<b.getX() && b.getX()<getWidth()+10 )){		//10 for buffer
				b.move();	
		
			}
			else{
				enemybullets.remove(i);
				i--;
			}
        }
        for(int i=0;i<bossrockets.size();i++){			//bullet that boss shoots
        	BossRocket b = bossrockets.get(i);
            if((-10<b.getY() && b.getY()<getHeight()+10 && -10<b.getX() && b.getX()<getWidth()+10 )){		//10 for buffer
				b.move();	
		
			}
			else{
				bossrockets.remove(i);
				i--;
			}
			if (b.getDone()==true){						//if rocket exploded, remove it
				bossrockets.remove(i);
				i--;
			}
        }
    }
    
    public void setNewUserShip(UserShip u){
		defaultUserShipSettings=new Object[]{u.getName(),u.getFireName(),u.getBulletName(),u.getMaxHP()};		//updates default so that if it dies, still has
    	userShip=u;																					//newly bought things
    	userShip.setSpeed(5);
    }
    public void setDefaultBulletDamage(int d){
    	defaultBulletDamage=d;
    }
    
    public Image[] getExplodePics(){
    	return explodePics;
    }
    public void setBulletHoming(boolean b){bullethoming=b;}
    
    //called after level is over to reset the bullets
    public void levelUp(){							
    	userbullets=new ArrayList<Bullet>();
		enemybullets= new ArrayList<Bullet>();
		bossrockets=new ArrayList<BossRocket>();		
		boss=null;
    }
    
    public boolean levelOver(){					//level is over if enemies and boss are all killed
    	if (boss!=null){
    		if (enemies.size()==0 &&  boss.isDead() ){
    			levelover=true;
    			score+=1000;			//defeated boss
    			MainGame.coins+=5;
    			
    			levelUp();	
    			level++	;
    			return true;
    		}
    	}
    	return false;
    }
    
    //checks if game is over, returns true if no lives left or beat all levels
    public boolean gameOver(){
    	if( lives==0 || ( level==6)){
    		go = true;
    		return true;
    	}
    	return false;
    }
    
	public void actionPerformed(ActionEvent evt){
	}
	public void keyPressed(KeyEvent evt){
		int i=evt.getKeyCode();
		keys[i]=true;
	}
	public void keyReleased(KeyEvent evt){
		int i=evt.getKeyCode();
		keys[i]=false;
	}
	public void keyTyped(KeyEvent evt){}
}