/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;

/**
 * Player character Dot, controlled by keyboard.
 * @author jrsullins
 */
public class Dot extends Moving {
    private static double SPEED = 4;
    public Dot(double x, double y) {
        super(x, y, Color.GREEN);
    }
    
    /**
     * Called by game engine to move Dot based on keys.
     * @param direction
     * @param walls 
     */
    public void move(double direction, Wall[] walls) {
        move(direction, SPEED, walls);
    }
}
