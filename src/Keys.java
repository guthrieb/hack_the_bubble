import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keys implements KeyListener {
  public boolean leftDown;
  public boolean rightDown;
  public boolean upDown;
  public boolean spaceDown;

  @Override
  public void keyTyped(KeyEvent keyEvent) {

  }

  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    switch(code) {
      case KeyEvent.VK_UP:
        upDown = true;
        break;
      case KeyEvent.VK_LEFT:
        leftDown = true;
        break;
      case KeyEvent.VK_RIGHT:
        rightDown = true;
        break;
      case KeyEvent.VK_SPACE:
        spaceDown = true;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int code = e.getKeyCode();
    switch(code) {
      case KeyEvent.VK_UP:
        upDown = false;
        break;
      case KeyEvent.VK_LEFT:
        leftDown = false;
        break;
      case KeyEvent.VK_RIGHT:
        rightDown = false;
        break;
      case KeyEvent.VK_SPACE:
        spaceDown = false;
        break;
    }
  }
}
