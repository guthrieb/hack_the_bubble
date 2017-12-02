import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Main {
    public static int screenWidth = 1920;
    public static int screenHeight = 1000;
    public static final double GRAVITY = 700;
    public static final int BAR = 20;
    public static final double DRAG = 0.2;
    public static final double BOUNCE = 0.4;
    public static final String TITLE = "Here be dragons!";

    public static final double INIT_ARROW_DX = 1000;
    public static final double INIT_ARR_DY = 1500;

    public static BufferStrategy bufferStrategy;
    private static BufferedImage bufferedImage;
    private static Graphics graphics;
    private static Graphics2D g2d;
    public static ArrayList<Spawn> objects = new ArrayList<>();
    public static boolean isRunning = true;
    public static final double UPA = 1500;

    private static final HashMap<TYPE, BufferedImage> images = new HashMap<>();

    public static void main(String[] args) {
        // Initialize some things.
        initializeJFrame();
        loadAssets();
        // Create and start threads.
        Thread moveEngine = new MoveEngine();
        moveEngine.start();
        // Run the animation loop.
        runAnimation();
    }

    private static void loadAssets() {
        String path = "assets/img/";
        for (TYPE t : TYPE.values()) {
            try {
                images.put(t, ImageIO.read(new File(path + t.toString() + ".png")));
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(t.toString());
                System.exit(1);
            }
        }
    }

    private static void drawDragon() {
        if (objects.isEmpty()) return;

        BufferedImage img = images.get(TYPE.DRAGON);
        double iw2 = img.getWidth() / 2, ih2 = img.getHeight() / 2;
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(screenWidth / 2 - iw2, screenHeight / 2 - ih2);
        affineTransform.rotate(objects.get(0).rotation.angle(), iw2, ih2);
        g2d.drawRenderedImage(img, affineTransform);
    }

    private static boolean inView(int x, int y) {
        return (0 <= x && x <= screenWidth) && (0 <= y && y <= screenHeight);
    }

    private static void drawObjects() {
        for (int i = 1; i < objects.size(); i++) {
            Spawn s = objects.get(i);
            int relX = s.getRelativeX();
            int relY = s.getRelativeY();
            // Don't draw spawns out of view.
            if (!inView(relX, relY)) continue;

            // Places the respective image, adjusting for its sizes.
            BufferedImage img = images.get(s.type);
            double iw2 = img.getWidth() / 2, ih2 = img.getHeight() / 2;
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(relX - iw2, relY - ih2);
            affineTransform.rotate(s.rotation.angle(), iw2, ih2);
            g2d.drawRenderedImage(img, affineTransform);
        }
        drawDragon();
    }

    public static void runAnimation() {

        while (isRunning) {
            try {
                // clear back bufferedImage...
                g2d = bufferedImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, screenWidth, screenHeight);
                drawObjects();
                // Blit image and flip...
                graphics = bufferStrategy.getDrawGraphics();
                graphics.drawImage(bufferedImage, 0, 0, null);
                if (!bufferStrategy.contentsLost()) bufferStrategy.show();
                // Let the OS have a little time...
                Thread.sleep(15);
            }
            //<editor-fold desc="catch-finally">
            catch (InterruptedException ignored) {
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

        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(ss);
        screenWidth = 1800;//ss.width - BAR;
        screenHeight = ss.height - BAR;

        // Create the frame...
        JFrame frame = new JFrame(TITLE);
        frame.addKeyListener(new Keys());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);

        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create canvas for painting...
        Canvas canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(screenWidth, screenHeight);
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
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        // Create off-screen drawing surface
        bufferedImage = graphicsConfiguration.createCompatibleImage(screenWidth, screenHeight);
        // Objects needed for rendering...
        graphics = null;
        g2d = null;
    }
}