
package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author alyacarina
 */
// NOTE: Resetting of a textarea must be implemented in doThis
public abstract class EnterKeyListener implements KeyListener {
    
    private boolean isEnterKey(KeyEvent e){
        return (e.getKeyChar()=='\n');
    }
    
    public abstract void doThis(KeyEvent e);
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(isEnterKey(e)) {
            doThis(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
