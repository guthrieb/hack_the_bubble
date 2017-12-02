
public class Keys implements KeyListener{
public boolean leftDown;
public boolean rightDown;
public boolean upDown;
public boolean spaceDown;
  public static void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    switch(code){
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
