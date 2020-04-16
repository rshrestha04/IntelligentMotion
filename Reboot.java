/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;
/**
 * Main game engine, creating characters, moving them each turn, and checking for collisions.
 * @author jrsullins
 */
public class Reboot extends JFrame {
    
    // Board diminesions
    public static int WIDTH = 750;
    public static int HEIGHT = 500;
    public static int MARGIN = 35; // Room at top for status bar (bit of a hack).
    private boolean first = true;
    
    // References to game characters
    private Dot dot;        // Player character
    private Bob bob;        // NPC helper for player
    private Hack hack;      // NPC opponent
    private Slash slash;    // NPC opponent
    private Wire[] wires;   // Wire objects that must be avoided
    private Wall[] walls;   // Walls (including middle square)
    private Exit exit;      // Goal for player
    
    /**
     * Constructor to initialize game.
     */
    public Reboot() {
        
        // Create visual surface
        JPanel surface = new JPanel();
        getContentPane().add(surface);
        setSize(WIDTH, HEIGHT);
        setVisible(true);    
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(listener);
        
        // Calls to initialization methods
        createPlayers();
        createWalls();
        createWires();
    }
    
    /**
     * Create player and NPCs at specific locations.
     */
    public void createPlayers() {
        dot = new Dot(40, (HEIGHT+MARGIN)/2.0);
        bob = new Bob(60, (HEIGHT+MARGIN)/2.0);
        hack = new Hack(WIDTH - 60, HEIGHT/2.0-20);
        slash = new Slash(WIDTH - 60, HEIGHT/2.0+20);
    }
    
    /**
     * Create wires at initial locations, defining their maximum/minimum locations
     * and initial directions.
     */
    public void createWires() {
        wires = new Wire[2];
        wires[0] = new Wire(WIDTH/2.0 - 50, 180, 60, 275, 2, false);
        wires[1] = new Wire(WIDTH/2.0 + 50, 100, 60, 275, 2, true);
    }
    
    /**
     * Create walls and exit.
     */
    public void createWalls() {
        walls = new Wall[5];
        
        // Exterior walls
        walls[0] = new Wall(0, 0, 20, HEIGHT+MARGIN+10);
        walls[1] = new Wall(WIDTH-20, 0, 20, HEIGHT+MARGIN+10);
        walls[2] = new Wall(0, MARGIN-10, WIDTH, 20);
        walls[3] = new Wall(0, HEIGHT+MARGIN-60, WIDTH, 20);
        
        // Center square
        walls[4] = new Wall(300, 300, 200, 50);
        
        // Exit
        exit = new Exit(WIDTH - 30, (HEIGHT+MARGIN)/2 - 80, 10, 150);
    }
    
    /**
     * Main drawing method used by Java 2D graphics, called every frame.
     * @param g 
     */
    public void paint(Graphics g) {
        
        // Get the 2D graphics component to draw on.
        Graphics2D g2 = (Graphics2D) g;
        
        // Only draw walls and background once.
        if (first) {
            g2.setPaint(Color.LIGHT_GRAY);
            g2.fillRect(0, 0, WIDTH+10, HEIGHT+MARGIN+10);
            for (int i = 0; i < walls.length; i++) {
                walls[i].drawSelf(g2);
            }
            exit.drawSelf(g2);
            first = false;
        }
        
        // Redraw moving entities each frame
        for (int i = 0; i < wires.length; i++) {            
            wires[i].drawSelf(g2);
        }
        slash.drawSelf(g2);
        hack.drawSelf(g2);
        bob.drawSelf(g2);
        dot.drawSelf(g2);
    }
    
    
    KeyListener listener = new KeyListener() {
        /**
         * Event handler for keys, checking which key is pressed and moving Dot accordingly, and
         * moving other characters as well.
         * @param event Keyboard event object containing key pressed.
         */
    public void keyTyped(KeyEvent event) {
        
        // Use WASD keys. If Dot is alive call move for the appropriate Manhattan direction.
        if (dot.isAlive()) {
            if (event.getKeyChar() == 'w') {
                dot.move(270, walls);
            }
            if (event.getKeyChar() == 's') {
                dot.move(90, walls);
            }
            if (event.getKeyChar() == 'd') {
                dot.move(0, walls);
            }
            if (event.getKeyChar() == 'a') {
                dot.move(180, walls);
            }
        }
        
        // Call methods to move other characters and check for collisions.
        moveAll();
        checkHits();
        
        // Call paint to redraw the game board.
        repaint();
    }
    public void keyPressed(KeyEvent event) {}
    public void keyReleased(KeyEvent event) {}
    };
    
    /**
     * Move the wires and the nonplayer characters by calling each one's move method.
     */
    public void moveAll() {
        for (int i = 0; i < wires.length; i++) {
            wires[i].move();
        }
        bob.move(walls, dot, hack, slash);
        hack.move(dot, bob, slash, wires, walls);
        slash.move(dot, bob, hack, wires, walls);
    }
    
    /**
     * Check to see what objects are in collision and take appropriate actions.
     */
    public void checkHits() {
        
        // Check whether any characters hav collided with a wire, and destroy them if the have
        // (printing a message).
        if (dot.isAlive() && checkWires(dot)) {
            JOptionPane.showMessageDialog(this, "Dot hit a wire! Game Over!");
            dot.destroy();
        }
        if (bob.isAlive() && checkWires(bob)) {
            JOptionPane.showMessageDialog(this, "Bob hit a wire!");
            bob.destroy();
        }
        if (hack.isAlive() && checkWires(hack)) {
            JOptionPane.showMessageDialog(this, "Hack hit a wire!");
            hack.destroy();
        }
        if (slash.isAlive() && checkWires(slash)) {
            JOptionPane.showMessageDialog(this, "Slash hit a wire!");
            slash.destroy();
        }
        
        // Check whether Hack or Slash has collided with Bob, destroying them if they have.
        if (hack.isAlive() && bob.isAlive() && bob.intersects(hack)) {
            JOptionPane.showMessageDialog(this, "Bob caught Hack! Hack destroyed!");
            hack.destroy();
        }
        if (slash.isAlive() && bob.isAlive() && bob.intersects(slash)) {
            JOptionPane.showMessageDialog(this, "Bob caught Slash! Slash destroyed!");
            slash.destroy();
        }
        
        // Check whether Dot has collided with Hack or Slash, ending the game if so.        
        if (hack.isAlive() && dot.isAlive() && dot.intersects(hack)) {
            JOptionPane.showMessageDialog(this, "Hack caught Dot! Game over!");
            dot.destroy();
        }       
        if (slash.isAlive() && dot.isAlive() && dot.intersects(slash)) {
            JOptionPane.showMessageDialog(this, "Slash caught Dot! Game over!");
            dot.destroy();
        }
            
        // Check whether Dot has reached the exit, ending the game if so.
        if (exit.checkExit(dot)) {
            JOptionPane.showMessageDialog(this, "Dot has escaped! Player wins!");
        }
    }
    
    /**
     * Utility method to check whether a given character has collided with a wire.
     * @param m character to check
     * @return 
     */
    public boolean checkWires(Moving m) {
        for (int w = 0; w < wires.length; w++) {
            if (wires[w].checkCollision(m)) {
                return true;
            }
        }
        return false;
    }

    
    public static void main(String[] args) {
        Reboot app = new Reboot();
    }
    
    
    
}
