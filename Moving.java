/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * This class of visual objects that move across the screen, meaning they must erase
 * their old position before being drawn at a new one.
 * @author jrsullins
 */
public class Moving extends Entity {
    private double x; // Internal center of entity (what code will manipulate)
    private double y;
    
    private ArrayList<Coordinates> history = new ArrayList(); // List of past coordinates used to estimate velocities
      
    private Moving previous; // Copy of old self to erase old position
    private boolean destroying = false; // Hack to insure only erase once
    
    private static int RADIUS = 8; // Default entity size when drawing
    private static double SPEED = 1; // Default maximum speed (should be overriden)
    
    public Moving(double x, double y, Color color) {
        super(x-RADIUS, y-RADIUS, 2*RADIUS+1, 2*RADIUS+1, color);
        this.x = x;
        this.y = y;
        previous = (Moving)this.clone();
        history.add(new Coordinates(x, y));
    }
    
    /**
     * Move by a given amount. Also check for intersection with walls.
     * @param direction Direction to move (in degrees)
     * @param speed Units to move in that direction
     * @param walls List of wall objects (cannot move past)
     */
    public void move(double direction, double speed, Entity[] walls) {
        
        // Move in given direction at given speed in x and y components
        double radians = direction/360*2*Math.PI; // Convert degrees to radians
        double dx = Math.cos(radians) * speed;
        double dy = Math.sin(radians) * speed;
        
        // Call superclass move method to actually move
        move(dx, dy);
        
        // Make sure this does not intersect a wall. If so, undo the move.
        for (int wall = 0; wall < walls.length; wall++) {
            if (intersects(walls[wall])) {
                x -= dx;
                y -= dy;
                super.move((int)-dx, (int)-dy);
            }
        }
        
        // Store the coordinates in the history for velocity estimation.
        // Inserting at front for ease of access.
        history.add(0, new Coordinates(x, y));
    } 
    
    /**
     * Getter for past history of locations
     * @return list of past locations
     */
    public ArrayList getHistory() {
        return history;
    }
    
    /**
     * Move by a given amount. This may be changed later to involve forces.
     * @param dx
     * @param dy 
     */
    public void move(double dx, double dy) {
                
        // Store previous position for redraw
        previous = (Moving)this.clone();
        
        // Set new position
        x += dx;
        y += dy;
        //System.out.println("Main move deltas: "+dx+" "+dy+" to "+x+" "+y);
        // Shift the objects visible frame.
        super.move((int)dx, (int)dy);
    }
    
    // Override base clas sgetX, getY for real coordinates
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    /**
     * Estimate velocity in each dimension based on history of previous locations.
     * @param time how many previous locations to use
     * @return array of 2 values, element 0 = X velocity, element 1 being Y velocity.
     */
    public double[] getVelocity(int time) {  
        double[] results = new double[]{0, 0}; // Default if no movement
        
        // Get current location
        Coordinates now = (Coordinates)history.get(0);
        
        // If initial move, no velocities yet
        if (history.size() <= 1) {
            return results;
        }
        
        // If not enough data (time locations), reset to number of results so far
        if (history.size() <= time+1) {
            time = history.size() - 1;
        }
        
        // Get location time units ago
        Coordinates then = (Coordinates)history.get(time);
        
        // Compute differences and normalize to time
        results[0] = (now.getX() - then.getX())/time;
        results[1] = (now.getY() - then.getY())/time;
        
        return results;        
    }
    
    
    /**
     * Called when object destroyed to erase it once from the screen.
     */
    public void destroy() {
        destroying = true;
        setAlive(false);
    }
      
    
    /**
     * Entity draws self on screen as rectangle of its color, erasing old position.
     * @param g2 Graphics object to draw self on
     */
    public void drawSelf(Graphics2D g2) { 
        if (destroying) {            
            previous.eraseSelf(g2);
            destroying = false;
        }
        if (isAlive()) {
            previous.eraseSelf(g2);
            super.drawSelf(g2);
        }
    }
    
}
