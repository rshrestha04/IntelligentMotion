/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;
import static reboot.Slash.SPEED;

/**
 * Nonplayer character Hack, who chases player.
 * @author jrsullins
 */
public class Hack extends Opponent {
    
    public static double SPEED = 3; // Hack moves up to 3 units/turn
    private int timeout=5;
    private double lastDirection;
    
    // Feel free to add your own member variables here.
    
    
    /**
     * Initialization for Hack. Feel free to add to this to initialize your member variables.
     * @param x Initial location
     * @param y Initial location
     */
    public Hack(double x, double y) {
        super(x, y, SPEED, Color.YELLOW);
        
    }
    
    /**
     * Code to move Hack. This is the main method you will define.
     * @param dot Reference to Dot object
     * @param bob Reference to Bob object
     * @param slash Reference to Slash object (so you can create cooperative motion)
     * @param wires List of Wire objects (so you can avoid)
     * @param walls List of Wall objects (need to be passed to super move, and can 
     *              do raycasting to find where they are).
     */
    public void move(Dot dot, Bob bob, Slash slash, Moving[] wires, Wall[] walls) {
       
       //storing all the necessary positions 
       double xb = bob.getX();
       double yb = bob.getY();
       double xd = dot.getX();
       double yd = dot.getY();
       double xh = slash.getX();
       double yh = slash.getY(); 
       double wx0 = wires[0].getX();
       double wy0 = wires[0].getY();
       double wx1 = wires[1].getX();
       double wy1 = wires[1].getY();
       
       
      
       double speed = SPEED; // Speed to move in this direction. It should not exceed SPEED.
       double direction = Math.atan2(yd-getY(), xd-getX())/Math.PI*180;
       
      //getting all the distance of moving objects to avoid them
       double distanceWire0 = this.getDistance(wires[0]);
       double distanceWire1 = this.getDistance(wires[1]);
       double distancebob= this.getDistance(bob);
     
     
     
       
       //raycasting to find the nearest wall for avoidance 
       double wall_distance =this.rayCastWalls(walls,this.getX(), this.getY(),direction);
        
       
    
     
       // checking for 1st wire distanceto avoid it
       
       if(distanceWire0 < 25){
            direction = Math.atan2(getY()-wy0, getX()-wx0)/Math.PI*180;//formula for flee algorimth
            timeout= 0; // prioritizing avoiding wires than avoiding walls by getting out of timeouts
        }
     
      //checking for second wire distance to avoid it
      
        else if(distanceWire1 < 25){
            direction = Math.atan2(getY()-wy1, getX()-wx1)/Math.PI*180;
            timeout= 0; // prioritizing avoiding wires than avoiding walls by getting out of timeouts
        }
       //cheking for bob to flee from him
        
        else if(distancebob < 25){
            direction = Math.atan2(getY()-yb, getX()-xb)/Math.PI*180;
        }
       
      // checking for wall near by to avoid it
       else if (wall_distance < 50){
          direction +=90;
        timeout=5;
       }
      
       else direction = Math.atan2(yd-getY(), xd-getX())/Math.PI*180;// Set this to the direction to move.
       
      
        //Remebering last direction
         lastDirection = direction;
         
         //timeout so the character continues to move on the same direction to avoid walls for 5 frames
         if (!(timeout ==0)){
             direction = lastDirection;
             timeout--;
             
         }
        
        
        // Call to superclass method to actually move Hack in this direction. 
        // DO NOT MODIFY!
        if (speed > SPEED) speed = SPEED;
       
        move(direction, speed, walls);        
    }
}
