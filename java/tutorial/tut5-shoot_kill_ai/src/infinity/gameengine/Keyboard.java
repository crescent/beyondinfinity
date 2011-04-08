package infinity.gameengine;

import infinity.GraphicsCapabilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

public class Keyboard  implements KeyListener {
    private Game myGame;
    private static Hashtable<Integer,Boolean> keyboardState;

    public static boolean isKeyDown(int keyEventCode)
    {
        return keyboardState.containsKey(keyEventCode)? keyboardState.get(keyEventCode) : false;
    }
    public Keyboard(Game myGame) {
        this.myGame = myGame;
        keyboardState = new Hashtable<Integer, Boolean>();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        keyboardState.put(e.getKeyCode(), true);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            myGame.exit();
        }
    }

    public void keyReleased(KeyEvent e) {
        keyboardState.put(e.getKeyCode(), false);
        
        if (e.getKeyCode() == KeyEvent.VK_F1) {
        	GraphicsCapabilities.display();
        }
    }
}
