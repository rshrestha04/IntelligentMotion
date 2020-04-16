/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;

/**
 * Represents walls in world.
 * @author jrsullins
 */
public class Wall extends Entity {
    private int width;
    private int height;
    private int x; // Upper left corner of wall
    private int y;

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height, Color.BLACK);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    
}
