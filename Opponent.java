/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Superclass for all NPC characters that act as adversaries in the game.
 * @author jrsullins
 */
public abstract class Opponent extends Moving {
    
    /**
     * Constructs NPC.
     * @param x Initial x location
     * @param y Initial y location
     * @param speed Maximum speed character moves
     * @param color Color drawn on board
     */
    public Opponent(double x, double y, double speed, Color color) {
        super(x, y, color);
    }
    
    /**
     * Estimate velocity character is moving in each dimension based on last locations.
     * @param character character in question
     * @param time number of previous moves to consider
     * @return array with velX = element 0, velY = element 1
     */
    public static double[] estimateVelocities(Moving character, int time) {
        double[] results = new double[]{0, 0}; // DEfault if no movement
        
        // Get current location
        ArrayList history = character.getHistory();
        Coordinates now = (Coordinates)history.get(0);
        
        // If initial move, no velocities yet
        if (history.size() == 0) {
            return results;
        }
        
        // If not enough data (time locations), reset to number of results so far
        if (history.size() < time) {
            time = history.size() - 1;
        }
        
        // Get location time units ago
        Coordinates then = (Coordinates)history.get(time);
        
        // Compute differences and normalize to time
        results[0] = (now.getX() - then.getX())/time;
        results[1] = (now.getY() - then.getY())/time;
        
        return results;        
    }
    
    public double raycastWalls(Wall[] walls, double direction) {
        // Really terrible algorithm that just creates a fake entity and moves it in
        // the given direction until it intersects a wall
        double x = getX();
        double y = getY();
        Entity fake = new Entity(x, y, 1, 1, Color.WHITE);
        double distance = 0;
        boolean hit = false;
        while (!hit) {
            for (int i = 0; i < walls.length; i++) {
                if (fake.intersects(walls[i])) {
                    hit = true;
                }
            }        
            distance++;
            x += Math.cos(direction/360*2*Math.PI);
            fake.setX(x);
            y += Math.sin(direction/360*2*Math.PI); 
            fake.setY(y);
            if (distance > 1000) {
                System.out.println("No hit found");
                return distance;
            }
        }   
        return distance;
    }
    
    
    
    /**
     * Gets distance from (x, y) to nearest wall in given direction
     * @param walls array of walls
     * @param x coordinate of ray origin
     * @param y coordinate of ray origin
     * @param direction to cast ray
     * @return distance to nearest wall
     */
    public static double rayCastWalls(Wall[] walls, double x, double y, double direction) {
        
        // Really terrible algorithm that just creates a fake entity and moves it in
        // the given direction until it intersects a wall
        Entity fake = new Entity(x, y, 1, 1, Color.WHITE);
        double distance = 0;
        boolean hit = false;
        while (!hit) {
            for (int i = 0; i < walls.length; i++) {
                if (fake.intersects(walls[i])) {
                    hit = true;
                }
            }        
            distance++;
            x += Math.cos(direction/360*2*Math.PI);
            fake.setX(x);
            y += Math.sin(direction/360*2*Math.PI); 
            fake.setY(y);
            if (distance > 1000) {
                System.out.println("No hit found");
                return distance;
            }
        }   
        return distance;
    }
    
    public double getDistance(Moving character) {
        return Math.sqrt((getX()-character.getX())*(getX()-character.getX()) + 
                         (getY()-character.getY())*(getY()-character.getY()));
    }
    
}
