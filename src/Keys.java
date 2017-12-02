import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keys implements KeyListener {
  public static boolean leftDown;
  public static boolean rightDown;
  public static boolean upDown;
  public static boolean spaceDown;

  @Override
  public void keyTyped(KeyEvent keyEvent) {

  }

  public void keyPressed(KeyEvent e) {
//      System.out.println("Hello");
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
