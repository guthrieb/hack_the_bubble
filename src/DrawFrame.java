import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class DrawFrame extends JFrame {

    // Define constants
    public static final int PANE_WIDTH = 640;
    public static final int PANE_HEIGHT = 480;

    private static JFrame mainWindow = new JFrame();
    private static DrawPane pane = new DrawPane();
    private static boolean userPressedExit = false;
    LinkedList<Object> objs = new LinkedList<>();

    //TODO hash map enum type to image path

    /**
     * Define inner class DrawPane, which is a JPanel used for custom drawing.
     */
    private static class DrawPane extends JPanel {

        int rect_width;
        int rect_height;

        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.CYAN);

            g.setColor(Color.YELLOW);

            //<editor-fold desc="img">
            g.drawRect(10, 10, rect_width, rect_height);
            try {
                BufferedImage img = ImageIO.read(new File("assets/img/dragon.jpg"));
                g.drawImage(img, 30, 30, null);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            //</editor-fold>

            // Printing texts
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("Testing custom drawing ...", 10, 20);

        }

    }

    private static void setup() {
        pane.setPreferredSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));
        mainWindow.setContentPane(pane);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setTitle("Here Be Dragons!");
        mainWindow.setVisible(true);

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

    private static void redraw(int i) {
        pane.rect_width = i * 40;
        pane.rect_height = i * 20;
        mainWindow.update(pane.getGraphics());
    }

    private static void loopUpdate() {
        int i = 0;
        while (!userPressedExit  && i < 10000) {
            redraw(new Random().nextInt(20));
            userPressedExit = true;
            i++;
        }
    }

    //TODO keylistener
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            setup();
            loopUpdate();
        });

    }

}
