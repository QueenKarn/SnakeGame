package com.mycompany.snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
  
  private static final long serialVersionUID = 1L;
  private static final int SCREEN_WIDTH = 500;
  private static final int SCREEN_HEIGHT = 500;
  private static final int BLOCK_SIZE = 10;
  private static final int DELAY = 100;
  
  private Timer timer;
  private Point head;
  private ArrayList<Point> snake;
  private Point apple;
  private int direction;
  private boolean gameOver;
  
  public SnakeGame() {
    setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);
    addKeyListener(this);
    initGame();
  }
  
  private void initGame() {
    head = new Point(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
    snake = new ArrayList<Point>();
    snake.add(new Point(head.x, head.y));
    apple = generateApple();
    direction = KeyEvent.VK_RIGHT;
    gameOver = false;
    timer = new Timer(DELAY, this);
    timer.start();
  }
  
  private Point generateApple() {
    int x = (int) (Math.random() * SCREEN_WIDTH / BLOCK_SIZE) * BLOCK_SIZE;
    int y = (int) (Math.random() * SCREEN_HEIGHT / BLOCK_SIZE) * BLOCK_SIZE;
    return new Point(x, y);
  }
  
  private void move() {
    Point tail = snake.remove(snake.size() - 1);
    tail.setLocation(head);
    
    switch (direction) {
      case KeyEvent.VK_UP:
        head.y -= BLOCK_SIZE;
        break;
      case KeyEvent.VK_DOWN:
        head.y += BLOCK_SIZE;
        break;
      case KeyEvent.VK_LEFT:
        head.x -= BLOCK_SIZE;
        break;
      case KeyEvent.VK_RIGHT:
        head.x += BLOCK_SIZE;
        break;
    }
    
    snake.add(0, tail);
  }
  
  private boolean checkCollision() {
    if (head.x < 0 || head.x >= SCREEN_WIDTH || head.y < 0 || head.y >= SCREEN_HEIGHT) {
      return true;
    }
    
    for (int i = 1; i < snake.size(); i++) {
      if (head.equals(snake.get(i))) {
        return true;
      }
    }
    
    return false;
  }
  
  private boolean checkAppleCollision() {
    return head.equals(apple);
  }
  
  private void grow() {
    snake.add(new Point(snake.get(snake.size() - 1)));
    apple = generateApple();
  }
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    g.setColor(Color.RED);
    g.fillRect(apple.x, apple.y, BLOCK_SIZE, BLOCK_SIZE);
    
    g.setColor(Color.WHITE);
    for (Point p : snake) {
      g.fillRect(p.x, p.y, BLOCK_SIZE, BLOCK_SIZE);
    }
    
    if (gameOver) {
      g.setColor(Color.WHITE);
      g.drawString("Game Over!", SCREEN_WIDTH / 2 - 40, SCREEN_HEIGHT / 2);
    }
  }
  
 @Override
  public void actionPerformed(ActionEvent e) {
    if (gameOver) {
      return;
    }
    
    move();
    
    if (checkCollision()) {
      gameOver = true;
    }
    
    if (checkAppleCollision()) {
      grow();
    }
    
    repaint();
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
      direction = key;
    } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
      direction = key;
    } else if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
      direction = key;
    } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
      direction = key;
    }
  }
  
  @Override
  public void keyReleased(KeyEvent e) {}
  
  @Override
  public void keyTyped(KeyEvent e) {}
  
  public static void main(String[] args) {
    JFrame frame = new JFrame("Snake Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.add(new SnakeGame());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}