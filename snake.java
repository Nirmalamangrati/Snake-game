import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.*;

public class snake extends JPanel {
    int headx = 100, heady = 100;
    int cellSize = 20;
    Timer tmr;
    int directionX = cellSize;
    int directionY = 0;
    Point food;
    int score = 0;
    LinkedList<Point> snakebody = new LinkedList<>();

    public snake() {
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        snakebody.add(new Point(headx, heady));

        spawnFood();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                // Changing direction
                if (keycode == KeyEvent.VK_UP && directionY == 0) {
                    directionX = 0;
                    directionY = -cellSize;
                }
                if (keycode == KeyEvent.VK_DOWN && directionY == 0) {
                    directionX = 0;
                    directionY = cellSize;
                }
                if (keycode == KeyEvent.VK_LEFT && directionX == 0) {
                    directionX = -cellSize;
                    directionY = 0;
                }
                if (keycode == KeyEvent.VK_RIGHT && directionX == 0) {
                    directionX = cellSize;
                    directionY = 0;
                }
            }
        });

        tmr = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
            }
        });
        tmr.start();
    }

    public void spawnFood() {
        int foodx = (int) (Math.random() * (400 / cellSize)) * cellSize;
        int foody = (int) (Math.random() * (400 / cellSize)) * cellSize;
        food = new Point(foodx, foody);
    }

    public void updateGame() {
        headx += directionX;
        heady += directionY;

        Point newHead = new Point(headx, heady);
        snakebody.addFirst(newHead);

        if (headx == food.x && heady == food.y) {
            score++;
            spawnFood();
        } else {
            snakebody.removeLast();
        }

        // Collision with self
        for (int i = 1; i < snakebody.size(); i++) {
            if (snakebody.get(i).equals(newHead)) {
                tmr.stop();
                JOptionPane.showMessageDialog(this, "Game Over: You collided with yourself!");
                return;
            }
        }

        // Collision with walls
        if (headx < 0 || headx >= 400 || heady < 0 || heady >= 400) {
            tmr.stop();
            JOptionPane.showMessageDialog(this, "Game Over: You hit the wall!");
        }

        repaint(); // Redraw the panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw food
        g.setColor(Color.GREEN);
        g.fillOval(food.x, food.y, cellSize, cellSize);

        // Draw snake
        g.setColor(Color.RED);
        for (Point p : snakebody) {
            g.fillRect(p.x, p.y, cellSize, cellSize);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score, 10, 20);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        snake snk = new snake();
        frame.add(snk);
        frame.pack(); // Resize to fit content
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
