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
    public static final String TITLE = "Here be dragons!";

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
    }

    public static void runAnimation() {

        String path = "assets/img/";

        //TODO load in memory
        String imgName = "dragon2.png";
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path + imgName));
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
//         Start the loop.
        while (isRunning) {
            try {
                // clear back bufferedImage...
                g2d = bufferedImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, X, Y);
                // Draw entities
                for (Spawn s : objects) {
                    affineTransform = new AffineTransform();
                    double iw2 = img.getWidth()/2, ih2 = img.getHeight()/2;
                    affineTransform.translate(s.getX()-iw2, s.getY()-ih2);
                    affineTransform.rotate(s.rotation.angle(), iw2, ih2);
                    g2d.drawRenderedImage(img, affineTransform);
                }
                // Blit image and flip...
                graphics = bufferStrategy.getDrawGraphics();
                graphics.drawImage(bufferedImage, 0, 0, null);
                if (!bufferStrategy.contentsLost()) bufferStrategy.show();
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