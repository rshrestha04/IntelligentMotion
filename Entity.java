/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


/**
 * Base class for all visual entities in game. Each is a type of rectangle, so we extend it
 * from the Rectangle2D class to make things like drawing, finding intersections, etc. simpler.
 * @author jrsullins
 */
public class Entity extends Rectangle2D.Double {
    private Color color; // Each has a set color
    private boolean alive; // Whether or not should be rendered
    
    /**
     * Set parameters, as well as color.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color 
     */
    public Entity(double x, double y, double width, double height, Color color) {
        super(x, y, width, height);
        this.color = color;       
        alive = true;        
    }

    // Getters and setters for dimensions
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    
    
    
    public Color getColor() {
        return color;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    /**
     * Entity draws self on screen as rectangle of its color.
     * @param g2 Graphics object to draw self on
     */
    public void drawSelf(Graphics2D g2) { 
        if (alive) {
            g2.setPaint(color);
            g2.fill(this);
        }
    }
    
    /**
     * Entity erases self on screen by drawing self background color.
     * @param g2 
     */
    public void eraseSelf(Graphics2D g2) {   
        if (alive) {    
            g2.setPaint(Color.LIGHT_GRAY);
            g2.fill(this);
        }
    }
    
    /**
     * Shift the object by (dx, dy) for moving objects.
     * @param dx
     * @param dy 
     */
    public void move(double dx, double dy) {
        this.setFrame(getX()+dx, getY()+dy, getWidth(), getHeight());
    }
 }
