/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;

/**
 * Wires that move back and forth vertically.
 * @author jrsullins
 */
public class Wire extends Moving {
    
    private double yhigh; // Max and min values
    private double ylow;
    private double speed; // Speed of motion
    private boolean up;   // Current direction of motion
    
    public Wire(double x, double y, double yhigh, double ylow, double speed, boolean up) {
        super(x, y, Color.RED);
        this.yhigh = yhigh;
        this.ylow = ylow;
        this.speed = speed;
        this.up = up;
    }
    
    /**
     * Move the wire at speed, either increasing or decreasing y coordinate based on up.
     */
    public void move() {
        if (up) {
            super.move(0, -speed);
            if (getY() <= yhigh) {
                up = !up;
            }
        }
        else {
            super.move(0, speed);
            if (getY() >= ylow) {
                up = !up;
            }            
        }
    }
    
    /**
     * Has a given entity hit this wire? If so, it will be destroyed.
     * @param e entity to check
     * @return 
     */
    public boolean checkCollision(Entity e) {
        if (intersects(e)) {
            return true;
        }
        else {
            return false;
        }
    }
   
    
}
