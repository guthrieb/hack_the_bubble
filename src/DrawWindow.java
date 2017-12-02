import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * @version 0.6
 */
public class DrawWindow {

    // Define constants
    public static final int PANE_WIDTH = 1920;
    public static final int PANE_HEIGHT = 1080;

    private static JFrame mainWindow = new JFrame();
    private static DrawPane pane = new DrawPane();
    private static boolean userPressedExit = false;
    public static ArrayList<Spawn> objects = new ArrayList<>();
    static HashMap imagePath;  // maps type to image todo

    //
    public static boolean isRunning = true;
    public static final double UPA = 1500;


    //    public static final int MAX_SPAWN = 30;
    public static final int X = 2000;
    public static final int Y = 100000;
    public static final double GRAVITY = 1000;
    //    public static final double DRAG = 0.2;
    public static final double BOUNCE = 0.9;
    //


    static BufferedImage img;

    /**
     * Define inner class DrawPane, which is a JPanel used for custom drawing.
     */
    private static class DrawPane extends JPanel {

        /**
         * Part of background.
         */
        private void drawClouds(Graphics g) {
        }

        private void renderObject(Graphics2D g, Spawn s) {
            AffineTransform at = AffineTransform.getScaleInstance(0.1, 0.1);
//            at.rotate(s.rotation.angle());
            at.translate(s.getX(), s.getY());
            g.drawRenderedImage(img, at);
        }

        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, X, Y);
            for (Spawn s : objects) renderObject(g2d, s);

            // Printing texts
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("Game images", 10, 20);

        }

    }

    private static void loadMainWindow() {
        pane.setPreferredSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));
        mainWindow.setContentPane(pane);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setTitle("Here Be Dragons!");
        mainWindow.setVisible(true);
    }

    private static void setKeyInteraction() {

        //TODO fix listener
        pane.setFocusable(true);
        pane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("key = " + e.toString());
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    userPressedExit = true;
                }
            }
        });
    }

    private static void loadAssets() {

        String path = "assets/img/";

        //TODO loop load
        String imgName = "dragon.jpg";
        try {
            img = ImageIO.read(new File(path + imgName));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setup() {
        loadMainWindow();
        setKeyInteraction();
        loadAssets();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            setup();
            MoveEngine me = new MoveEngine();
            me.start();

            while (isRunning){
                mainWindow.update(pane.getGraphics());
                try {
                    Thread.sleep(15);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
