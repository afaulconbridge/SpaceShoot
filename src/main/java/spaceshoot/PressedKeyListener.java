package spaceshoot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PressedKeyListener implements KeyListener {
	
	protected Set<Integer> downKeys = new HashSet<>();
	
	public PressedKeyListener() {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		downKeys.add(e.getExtendedKeyCode());
 
	}

	@Override
	public void keyReleased(KeyEvent e) {
		downKeys.remove(e.getExtendedKeyCode());

	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing
	}

	public Set<Integer> getPressedKeys() {
		return Collections.unmodifiableSet(downKeys);
	}
	
	/** 
	 * Tests if a key from KeyEvent.VK_* is currently pressed
	 * 
	 * @param extendedKeyCode
	 * @return
	 */
	public boolean isPresed(int extendedKeyCode) {
		return downKeys.contains(extendedKeyCode);
	}
}
