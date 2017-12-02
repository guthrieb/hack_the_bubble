import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Spawn
{
  private double x, y, vx, vy, radius;
  private ArrayList<Accel> accelerations = new ArrayList<Accel>();
  public Vector2D rotation;
  public TYPE type;

  public Spawn(double x, double y, double vx, double vy, double m, double anglex, double angley,  TYPE type)
  {
    this.x = x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    this.radius = m;
    this.rotation = new Vector2D(anglex,angley );
    this.type = type;
  }

  public TYPE getType(){
      return this.type;
    }





  public Spawn(int x, int y)
  {
    this(x, y, 0, 0, 10, 0.1, 0.1, TYPE.DRAGON);
  }

  public Vector2D velVector()
  {
    return new Vector2D(this.vx(), this.vy());
  }

  public static Spawn generateArrow(double x, double y, double vx, double vy){
        Spawn newarrow = new Spawn(x, y, vx, vy, 10, vx, vy, TYPE.ARROW);

        return newarrow;
}

  public static Spawn generateFlames(){
    Spawn player = DrawWindow.objects.get(0);

    Spawn flame = generateArrow(player.getX(), player.getY(), player.vx*2, player.vy*2);

    return flame;
  }

  public void applyDrag(double drag)
  {
    this.vx = (drag * this.vx);
    this.vy = (drag * this.vy);
  }

  public Accel sumAccel()
  {
    double xAccel = 0, yAccel = 0;
    for (int i = 0; i < this.accelerations.size(); i++) {
      xAccel += this.accelerations.get(i).ax();
      yAccel += this.accelerations.get(i).ay();
    }
    this.accelerations.clear();
    return new Accel(xAccel, yAccel);
  }

  public void addAccel(Accel a)
  {
    this.accelerations.add(a);
  }

  public void updateVelocity(double vx, double vy)
  {

    this.vx = vx;
    this.vy = vy;

    if(this.type == TYPE.ARROW){
        updateRot(vx, vy);
    }

  }

  public void updateRot(double newX, double newY){
        this.rotation.x = newX;
        this.rotation.y = newY;
  }

  public void updatePos(double newX, double newY)
  {
    this.x = newX;
    this.y = newY;
  }

  public double vx()
  {
    return this.vx;
  }

  public double vy()
  {
    return this.vy;
  }

  public int dimX()
  {
    return (int) (this.radius * 2);
  }

  public int dimY()
  {
    return (int) (this.radius * 2);
  }

  public Point2D getCenter()
  {
    return new Point2D.Double(this.x + (this.dimX() / 2), this.y
                              + (this.dimY() / 2));
  }

  public double getRadius()
  {
    return this.radius;
  }

  public double getX()
  {
    return x;
  }

  public double getY()
  {
    return y;
  }

  public double getX2()
  {
    return (this.x + this.dimX());
  }

  public double getY2()
  {
    return (this.y + this.dimY());
  }

  public void setX(int newX)
  {
    this.x = newX;
  }

  public void setY(int newY)
  {
    this.y = newY;
  }
}
