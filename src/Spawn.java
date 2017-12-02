import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Spawn {
    private double x, y, vx, vy, radius;
    private ArrayList<Accel> accelerations = new ArrayList<Accel>();
    public Vector2D rotation;
    public TYPE type;

    public int hp;

    public Spawn(double x, double y, double vx, double vy, double m, double anglex, double angley, TYPE type) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = m;
        this.rotation = new Vector2D(anglex, angley);
        this.type = type;

        if (type == TYPE.ARROW) {
            hp = 2;
        }
        else if (type == TYPE.DRAGON) {
            hp = 10;
        }
        else if (type == TYPE.GROUND) {
            hp = Integer.MAX_VALUE;
        }
    }

    public TYPE getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Spawn{" +
                "x=" + x +
                ", y=" + y +
                ", vx=" + vx +
                ", vy=" + vy +
                ", radius=" + radius +
//            ", accelerations=" + accelerations +
//            ", rotation=" + rotation +
                ", type=" + type +
                '}';
    }

    public Spawn(int x, int y) {
        this(x, y, 0, 0, 10, 0.1, 0.1, TYPE.DRAGON);
    }

    public Vector2D velVector() {
        return new Vector2D(this.vx(), this.vy());
    }

    public static Spawn generateArrow(double x, double y, double vx, double vy) {
        Spawn newarrow = new Spawn(x, y, vx, vy, 10, vx, vy, TYPE.ARROW);

        return newarrow;
    }

    public static Spawn generateCannonball(double vx, double vy) {
        Spawn player = Main.objects.get(0);
        double y = 0.25 * Main.screenHeight;
        double ran = Math.random();
        double x;
        if (ran < 0.5) {
            x = 0;
        }
        else {
            x = Main.screenWidth;
            vx *= -1;
        }
        Spawn cball = new Spawn(x, y, vx, vy, 20, vx, vy, TYPE.CANNONBALL);
        return cball;
    }

    public static Spawn generateDirectedArrow(double x) {
        Spawn dragon = Main.objects.get(0);

        double yod = dragon.y;
        double voyd = dragon.vy;

        double xod = dragon.x;
        double voxd = dragon.vx;

        double t = 10; //pick t for collision
        double yd = yod + voyd * t + 0.5 * Main.GRAVITY * t * t;

        double va = 300;
        double yoa = Main.screenHeight;
        double xoa = x;

        double inside = (yd - yoa) / (va * t + 0.5 * Main.GRAVITY * t * t) % (2 * Math.PI);

        double theta = Math.asin(inside);

        double vy = va * Math.sin(theta);
        double vx = va * Math.cos(theta);

        if (xoa < xod) {
            vx *= -1;
        }

        Spawn newArrow = new Spawn(xoa, yoa, vx, vy, 10, vx, vy, TYPE.ARROW);
        return newArrow;
    }

    public static Spawn generateFlames() {
        Spawn player = Main.objects.get(0);

        Spawn flame = new Spawn(player.getX(), player.getY(), player.vx * 2, player.vy * 2, 10, player.vx * 2, player.vy * 2, TYPE.FIREBALL);

        return flame;
    }

    public void applyDrag(double drag) {
        this.vx = (drag * this.vx);
        this.vy = (drag * this.vy);
    }

    public Accel sumAccel() {
        double xAccel = 0, yAccel = 0;
        for (int i = 0; i < this.accelerations.size(); i++) {
            xAccel += this.accelerations.get(i).ax();
            yAccel += this.accelerations.get(i).ay();
        }
        this.accelerations.clear();
        return new Accel(xAccel, yAccel);
    }

    public void addAccel(Accel a) {
        this.accelerations.add(a);
    }

    public void updateVelocity(double vx, double vy) {

        this.vx = vx;
        this.vy = vy;

        if (this.type == TYPE.ARROW) {
            updateRot(vx, vy);
        }

    }

    public void updateRot(double newX, double newY) {
        this.rotation.x = newX;
        this.rotation.y = newY;
    }

    public void updatePos(double newX, double newY) {
        this.x = newX;
        if (y < Main.screenHeight-50)
            this.y = newY;
    }

    public double vx() {
        return this.vx;
    }

    public double vy() {
        return this.vy;
    }

    public int dimX() {
        return (int) (this.radius * 2);
    }

    public int dimY() {
        return (int) (this.radius * 2);
    }

    public Point2D getCenter() {
        return new Point2D.Double(this.x + (this.dimX() / 2), this.y
                + (this.dimY() / 2));
    }

    public double getRadius() {
        return this.radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getX2() {
        return (this.x + this.dimX());
    }

    public double getY2() {
        return (this.y + this.dimY());
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    /**
     * @return x coordinate relative to dragon
     */
    public int getRelativeX() {
        return (int) (this.x - Main.objects.get(0).x + Main.screenWidth / 2);
    }

    /**
     * @return y coordinate relative to dragon
     */
    public int getRelativeY() {
        return (int) (this.y - Main.objects.get(0).y + Main.screenHeight / 2);
    }

}
