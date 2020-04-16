/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;
import javax.swing.JOptionPane;

/**
 * Bob is a friend of Dot, moving to protect her from Hack and Slash.
 * @author jrsullins
 */
public class Bob extends Moving {
    
    private static double SPEED = 2; // Bob moves slower
    
    public Bob(double x, double y) {
        super(x, y, Color.BLUE);
    }
    
    /**
     * Bob moves towards either Hack or Slash depending on which is closer to Dot.
     * @param walls list of walls
     * @param hack Hack object
     * @param slash Slash object
     */
    public void move(Wall[] walls, Dot dot, Hack hack, Slash slash) { 
        // Determine which is closer to Dot
        double xd = dot.getX();
        double yd = dot.getY();
        double xh = hack.getX();
        double yh = hack.getY(); 
        double dh = 1000;
        double ds = 1000;
        double xs = slash.getX();
        double ys = slash.getY();          
        if (hack.isAlive()) {       
            dh = Math.sqrt((xh-xd)*(xh-xd)+(yh-yd)*(yh-yd));
        }
        if (slash.isAlive()) {      
            ds = Math.sqrt((xs-xd)*(xs-xd)+(ys-yd)*(ys-yd));
        }
        double tx, ty;
        if (dh < ds) {            
            tx = xh;
            ty = yh;
        }
        else {           
            tx = xs;
            ty = ys;
        }
        double dx = tx - getX();
        double dy = ty - getY();
        double direction = Math.atan(dy/dx)/Math.PI*180;
        
        // Move towards that entity
        move(direction, SPEED, walls);  
        
        
    }
    
}
