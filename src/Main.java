import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {
    public static final int MAX_SPAWN = 30;
    public static final int X = 2000;
    public static final int Y = 1000;
    public static final double GRAVITY = 1000;
    public static final double DRAG = 0.2;
    public static final double BOUNCE = 0.9;
    public static final String TITLE = "Dragonbois";

    public static final double INIT_ARROW_DX = 1000;
    public static final double INIT_ARR_DY = 1500;

    private static JFrame frame;
    private static Canvas canvas;
    public static BufferStrategy bufferStrategy;
    private static GraphicsEnvironment graphicsEnvironment;
    private static GraphicsDevice graphicsDevice;
    private static GraphicsConfiguration graphicsConfiguration;
    private static BufferedImage bufferedImage;
    private static Graphics graphics;
    private static Graphics2D g2d;
    private static AffineTransform affineTransform;
    public static ArrayList<Spawn> objects = new ArrayList<Spawn>();
    public static boolean isRunning = true;
    public static final double UPA = 1500;

    public static void main(String[] args) {
        // Initialize some things.
        initializeJFrame();
        // Create and start threads.
        Thread moveEngine = new MoveEngine();
        moveEngine.start();
        // Run the animation loop.
        runAnimation();

        System.out.println(allDeadCounter);
        System.out.println(giveBirthCounter);
    }

    public static void runAnimation() {

        String path = "assets/img/";

        //TODO loop load
        String imgName = "dragon.jpg";
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path + imgName));
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
//         Set up some variables.
//        int fps = 0;
//        int frames = 0;
//        long totalTime = 0;
//        long curTime = System.currentTimeMillis();
//        long lastTime = curTime;
//         Start the loop.
        while (isRunning) {
            try {
                // Calculations for FPS.
//                lastTime = curTime;
//                curTime = System.currentTimeMillis();
//                totalTime += curTime - lastTime;
//                if (totalTime > 1000) {
//                    totalTime -= 1000;
//                    fps = frames;
//                    frames = 0;
//                }
//                ++frames;
                // clear back bufferedImage...
                g2d = bufferedImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, X, Y);
//                canvas.setBackground(Color.WHITE);
                // Draw entities
                for (Spawn s : objects) {
//                    g2d.setColor(Color.BLACK);
                    affineTransform = new AffineTransform();
//                    affineTransform = AffineTransform.getScaleInstance(0.1, 0.1);
//                    affineTransform.translate(objects.get(i).getX(), objects.get(i).getY());

                    affineTransform.translate(s.getX(), s.getY());
                    affineTransform.rotate(s.rotation.angle());
//                    g2d.rotate(s.rotation.angle(), s.getX(), s.getY());
                    g2d.drawRenderedImage(img, affineTransform);

//                    g2d.fill(new Rectangle2D.Double(s.getX(), s.getY(), s.getRadius() * 0.5, s.getRadius() * 0.5));

//                    g2d.rotate(-s.rotation.angle(), s.getX(), s.getY());


                }
                // display frames per second...
//                g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
//                g2d.setColor(Color.GREEN);
//                g2d.drawString(String.format("FPS: %s", fps), 20, 20);
                // Blit image and flip...
                graphics = bufferStrategy.getDrawGraphics();
                graphics.drawImage(bufferedImage, 0, 0, null);
                if (!bufferStrategy.contentsLost()) {
                    bufferStrategy.show();
                }
                // Let the OS have a little time...
                Thread.sleep(15);
            }
            //<editor-fold desc="catch-finally">
            catch (InterruptedException e) {
            }
            finally {
                // release resources
                if (graphics != null) graphics.dispose();
                if (g2d != null) g2d.dispose();
            }
            //</editor-fold>
        }
    }

    static int allDeadCounter = 0;
    static int giveBirthCounter = 0;

    public static boolean allDead() {
        allDeadCounter++;
        if (objects.size() < 1) return true;
        return false;
    }

    public static synchronized int giveBirth(int x, int y, double vx,
                                             double vy, int m) {
        giveBirthCounter++;
        System.out.println("");
        if (objects.size() >= MAX_SPAWN) return 1;
        objects.add(new Spawn(x, y, vx, vy, m, 0, -1, TYPE.DRAGON));
        return 0;
    }

    private static void initializeJFrame() {
        // Create the frame...
        frame = new JFrame(TITLE);
        frame.addKeyListener(new Keys());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);

        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create canvas for painting...
        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(X, Y);
        // Add the canvas, and display.
        frame.add(canvas);
        frame.pack();
        // The following line centers the window on the screen.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // Set up the BufferStrategy for double buffering.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        // Get graphics configuration...
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        // Create off-screen drawing surface
        bufferedImage = graphicsConfiguration.createCompatibleImage(X, Y);
        // Objects needed for rendering...
        graphics = null;
        g2d = null;
    }
}