package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;

/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerShip extends BaseEntity{

	private int health = 3,attackCooldown = 30,speed =6,destroyedCoolDown = 60*7;
	private boolean attacking = false, destroyed = false;
	private Animation deathAnimation;
	private boolean occupied = false;


	public PlayerShip(int x, int y, int width, int height, BufferedImage sprite, Handler handler) {
		super(x, y, width, height, sprite, handler);

		deathAnimation = new Animation(256,Images.galagaPlayerDeath);

	}

	@Override
	public void tick() {
		super.tick();
		if (destroyed){
			if (destroyedCoolDown<=0){
				destroyedCoolDown=60*7;
				destroyed=false;
				deathAnimation.reset();
				bounds.x=x;
			}else{
				deathAnimation.tick();
				destroyedCoolDown--;
			}
		}else {
			if (attacking) {
				if (attackCooldown <= 0) {
					attacking = false;
				} else {
					attackCooldown--;
				}
			}
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !attacking) {
				handler.getMusicHandler().playEffect("laser.wav");
				attackCooldown = 30;
				attacking = true;
				handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));

			}
			//N button is added. It destroys the ship.

			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
				destroyed=true;
				health--;
			}  
			//U button is added. It adds a new life.

			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_U) && health <3){
				health++;
			}


//			int[][] cr = new int[2][8];{
//
//				for(int i=0; i<2; i++) {
//					for(int j=0; j<9; j++) {
//
//						if(cr[i][j] == 0){
//							occupied=true;
//							break; 
//
//						}else{
//							continue; 				
//
//						}	
//					}
//				}
//			}

			int c1= (int) random.nextInt(8);
			int ra = 3;
			int rb = 4;
			int rc = random.nextBoolean() ? ra : rb;

			//P button is added. It spawns the bees when pressed.
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){
				handler.getGalagaState().entityManager.entities.add(new EnemyBee(0, 0, 32, 32, handler,rc, c1 ));
			}

			//Hacer un array doble dimension (i,j) que devuelva true si el array 
			//no encuentra a alguien en esa columna / fila. False si ya hay alguien.


		}


		if (handler.getKeyManager().left) {
			x -= (speed);
		}
		if (handler.getKeyManager().right) {
			x += (speed);
		}

		bounds.x = x;
	}




	@Override
	public void render(Graphics g) {
		if (destroyed){
			if (deathAnimation.end){
				g.drawString("READY",handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);
			}else {
				g.drawImage(deathAnimation.getCurrentFrame(), x, y, width, height, null);
			}
		}else {
			super.render(g);
		}
	}

	@Override
	public void damage(BaseEntity damageSource) {
		if (damageSource instanceof PlayerLaser){
			return;
		}
		health--;
		destroyed = true;
		handler.getMusicHandler().playEffect("explosion.wav");

		bounds.x = -10;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

}
