package Game.GameStates;

import Game.Galaga.Entities.EnemyBee;
import Game.Galaga.Entities.EnemyDragonfly;
import Game.Galaga.Entities.EntityManager;
import Game.Galaga.Entities.PlayerShip;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class GalagaState extends State {

	public EntityManager entityManager;
	public String Mode = "Menu";
	private Animation titleAnimation;
	public int selectPlayers = 1;
	public int startCooldown = 60*7;//seven seconds for the music to finish
	public int BeeCount = 0;
	public int DragonflyCount = 0;
	public boolean InitialSpawn= false ;
	public boolean BeePlace[][] = new boolean[10][10];
	public boolean DragonflyPlace[][] = new boolean[20][20];
	//public ArrayList<Integer> BeeP = new ArrayList<Integer>();
	Random random = new Random();

	public GalagaState(Handler handler){
		super(handler);
		refresh();
		entityManager = new EntityManager(new PlayerShip(handler.getWidth()/2-64,handler.getHeight()- handler.getHeight()/7,64,64,Images.galagaPlayer[0],handler));
		titleAnimation = new Animation(256,Images.galagaNewLogo);
	}


	public void OpeningSpawn() {
		for(int x=0; x<10; x++){
			for(int y=1; y<5; y++){
				if (BeePlace[y][x] == false){
					if( y >= 3){
						handler.getGalagaState().entityManager.entities.add(new EnemyBee(0, 0, 32, 32, handler, y, x));
						BeeCount=20;
						BeePlace[y][x] = true;
						
					}
					else {
					
								handler.getGalagaState().entityManager.entities.add(new EnemyDragonfly(0, 0, 32, 32, handler, y, x));
								DragonflyCount=20;
								DragonflyPlace[y][x] = true;
						
					
			}
		}
	}
		}}

	//Bee is randomized with the P button.
	@Override
	public void tick() {

		if(!InitialSpawn){
			OpeningSpawn();
			InitialSpawn= true;
		}
		if (Mode.equals("Stage")){
			if (startCooldown<=0) {
				entityManager.tick();
				if (BeeCount < 20 || handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)) {
					while(true){
						int c1= (int) random.nextInt(10);
						int ra = 3;
						int rb = 4;
						int rc = random.nextBoolean() ? ra : rb;
													if (!BeePlace[rc][c1]){
								BeePlace[rc][c1] = true;
								handler.getGalagaState().entityManager.entities.add(new EnemyBee(0, 0, 32, 32, handler, rc, c1));

								BeeCount++;
								break;
							}

					}

				}
				if ((DragonflyCount < 20 || handler.getKeyManager().keyJustPressed(KeyEvent.VK_O))) {
					while(true){
						int c2= (int) random.nextInt(10);
						int rd = (int) random.nextInt(3);
						if(rd<1) {
							rd+=1;}
						
							if (!DragonflyPlace[rd][c2]){
								DragonflyPlace[rd][c2] = true;
								handler.getGalagaState().entityManager.entities.add(new EnemyDragonfly(0, 0, 32, 32, handler, rd, c2));
								DragonflyCount++;
								break;
							}
						
						}
					
				}

			}else{
				startCooldown--;
			}

		}else{
			titleAnimation.tick();
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
				selectPlayers=1;
			}else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
				selectPlayers=2;
			}
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
				Mode = "Stage";
				handler.getMusicHandler().playEffect("Galaga.wav");

			}

		}
	}



	@Override
	public void render(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillRect(0,0,handler.getWidth(),handler.getHeight());
		g.setColor(Color.BLACK);
		g.fillRect(handler.getWidth()/4,0,handler.getWidth()/2,handler.getHeight());
		Random random = new Random(System.nanoTime());

		for (int j = 1;j < random.nextInt(15)+60;j++) {
			switch (random.nextInt(6)) {
			case 0:
				g.setColor(Color.RED);
				break;
			case 1:
				g.setColor(Color.BLUE);
				break;
			case 2:
				g.setColor(Color.YELLOW);
				break;
			case 3:
				g.setColor(Color.GREEN);
				break;
			case 4:
				g.setColor(Color.WHITE);
				break;
			case 5:
				g.setColor(Color.MAGENTA);
			}
			int randX = random.nextInt(handler.getWidth() - handler.getWidth() / 2) + handler.getWidth() / 4;
			int randY = random.nextInt(handler.getHeight());
			g.fillRect(randX, randY, 2, 2);

		}
		if (Mode.equals("Stage")) {
			g.setColor(Color.MAGENTA);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
			g.drawString("HIGH-SCORE:",handler.getWidth()/2-handler.getWidth()/18,26);
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
			g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()/2-32,50);
			g.setColor(Color.MAGENTA);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
			g.drawString("1UP:",handler.getWidth()/3-handler.getWidth()/18,26);
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
			g.drawString(String.valueOf(handler.getScoreManager().getGalagaCurrentScore()),handler.getWidth()/2-320,50);

			for (int i = 0; i< entityManager.playerShip.getHealth();i++) {
				g.drawImage(Images.galagaPlayer[0], (handler.getWidth()/ 4 + handler.getWidth() / 100) + ((entityManager.playerShip.width*1)*i), handler.getHeight()-handler.getHeight()/15, handler.getWidth() / 28, handler.getHeight() / 21, null);
			}
			if (startCooldown<=0) {
				entityManager.render(g);
			}else{
				g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
				g.setColor(Color.MAGENTA);
				g.drawString("Start",handler.getWidth()/2-handler.getWidth()/18,handler.getHeight()/2);
			}
		}else{

			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));

			g.setColor(Color.MAGENTA);
			g.drawString("HIGH-SCORE:",handler.getWidth()/2-handler.getWidth()/18,26);

			g.setColor(Color.WHITE);
			g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()/2-32,50);

			g.drawImage(titleAnimation.getCurrentFrame(),(handler.getWidth()/3 - 75),(handler.getHeight()/8-20),(handler.getWidth()/2 - 100),(handler.getHeight()/2 - 130) ,null);

			g.drawImage(Images.galagaCopyright,handler.getWidth()/2-(handler.getWidth()/8),handler.getHeight()/2 + handler.getHeight()/3,handler.getWidth()/4,handler.getHeight()/8,null);

			g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
			g.drawString("1   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2);
			g.drawString("2   PLAYER",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2+handler.getHeight()/12);
			if (selectPlayers == 1){
				g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2-handler.getHeight()/32,32,32,null);
			}else{
				g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2+handler.getHeight()/18,32,32,null);
			}


		}
	}

	@Override
	public void refresh() {



	}

}
