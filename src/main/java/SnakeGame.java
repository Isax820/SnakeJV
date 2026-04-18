import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    private LinkedList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private javax.swing.Timer timer;

    private int score = 0;
    private int speed = 100;

    private String state = "MENU";

    // UI moderne
    private JButton easyBtn = new JButton("Facile");
    private JButton mediumBtn = new JButton("Moyen");
    private JButton hardBtn = new JButton("Difficile");

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);

        styleButton(easyBtn, 200);
        styleButton(mediumBtn, 260);
        styleButton(hardBtn, 320);

        easyBtn.addActionListener(e -> startGame(150));
        mediumBtn.addActionListener(e -> startGame(100));
        hardBtn.addActionListener(e -> startGame(60));

        add(easyBtn);
        add(mediumBtn);
        add(hardBtn);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (state.equals("GAME")) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP: if (direction != 'D') direction = 'U'; break;
                        case KeyEvent.VK_DOWN: if (direction != 'U') direction = 'D'; break;
                        case KeyEvent.VK_LEFT: if (direction != 'R') direction = 'L'; break;
                        case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
                    }
                }
            }
        });
    }

    private void styleButton(JButton btn, int y) {
        btn.setBounds(200, y, 200, 40);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    }

    private void startGame(int speed) {
        this.speed = speed;
        snake = new LinkedList<>();
        snake.add(new Point(5, 5));
        direction = 'R';
        score = 0;
        running = true;
        state = "GAME";
        spawnFood();

        easyBtn.setVisible(false);
        mediumBtn.setVisible(false);
        hardBtn.setVisible(false);

        timer = new javax.swing.Timer(speed, this);
        timer.start();
        requestFocusInWindow();
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(WIDTH / TILE_SIZE), rand.nextInt(HEIGHT / TILE_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && state.equals("GAME")) {
            move();
            checkCollision();
        }
        repaint();
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 'U': newHead.y--; break;
            case 'D': newHead.y++; break;
            case 'L': newHead.x--; break;
            case 'R': newHead.x++; break;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            score += 10;
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        if (head.x < 0 || head.y < 0 || head.x >= WIDTH / TILE_SIZE || head.y >= HEIGHT / TILE_SIZE) {
            gameOver();
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        running = false;
        timer.stop();
        state = "MENU";

        easyBtn.setVisible(true);
        mediumBtn.setVisible(true);
        hardBtn.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (state.equals("MENU")) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("SNAKE GAME", 190, 120);

            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Choisis une difficulte", 210, 160);

            g.drawString("Score precedent: " + score, 210, 400);
        } else {
            g.setColor(Color.RED);
            g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + score, 10, 20);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}