package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Created by AlexVR on 1/24/2020.
 */

public class KeyManager implements KeyListener {

	private boolean[] keys,justPressed,cantPress;
	public boolean up=false, down=false, left=false, right=false;
	public boolean pausebutt=false;
	public boolean nbutt=false;
	public boolean ubutt=false;
	public boolean pbutt=false;
	public boolean obutt=false;

	public KeyManager(){

		keys = new boolean[256];
		justPressed = new boolean[keys.length];
		cantPress = new boolean[keys.length];

	}

	public void tick(){
		for(int i =0; i < keys.length;i++){
			if(cantPress[i] && !keys[i]){
				cantPress[i]=false;

			}else if(justPressed[i]){
				cantPress[i]=true;
				justPressed[i] =false;
			}
			if(!cantPress[i] && keys[i]){
				justPressed[i]=true;
			}
		}

		up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
	
		//N button was added to destroy the ship.
		//U button added to ADD a live if there is less than 3
		//P button added to spawn the bees.
		nbutt=keys[KeyEvent.VK_N];
		ubutt=keys[KeyEvent.VK_U];
		pbutt=keys[KeyEvent.VK_P];
		obutt=keys[KeyEvent.VK_O];

		pausebutt = keys[KeyEvent.VK_ESCAPE];

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
			return;
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
			return;
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public boolean keyJustPressed(int keyCode){
		if(keyCode < 0 || keyCode >= keys.length)
			return false;
		return justPressed[keyCode];
	}

}
