/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;
import java.awt.Frame;
import java.util.Random;
import static reboot.Hack.SPEED;

/**
 * Nonplayer character Slash, who chases player.
 * @author jrsullins
 */
public class Slash extends Opponent {
    
    public static double SPEED = 3; // Hack moves up to 3 units/turn
    
     protected Random random = new Random();
     private int timeout=0;
     private double lastDirection= 0;
     int a =random.nextInt(3);
    // Feel free to add your own member variables here.
    
    
    /**
     * Initialization for Hack. Feel free to add to this to initialize your member variables.
     * @param x Initial location
     * @param y Initial location
     */
    
    public Slash(double x, double y) {
        super(x, y, SPEED, Color.ORANGE);
    }
    
    /**
     * Code to move Slash. This is the main method you will define.
     * @param dot Reference to Dot object
     * @param bob Reference to Bob object
     * @param hack Reference to Hack object (so you can create cooperative motion)
     * @param wires List of Wire objects (so you can avoid)
     * @param walls List of Wall objects (need to be passed to super move, and can 
     *              do raycasting to find where they are).
     */
    
    public void move(Dot dot, Bob bob, Hack hack, Moving[] wires, Wall[] walls) {
        
       //getting all the requred position of all moving characters
        
        double xb = bob.getX();
        double yb = bob.getY();
        double xd = dot.getX();
        double yd = dot.getY();
        double xh = hack.getX();
        double yh = hack.getY(); 
        double wx0 = wires[0].getX();
        double wy0 = wires[0].getY();
        double wx1 = wires[1].getX();
        double wy1 = wires[1].getY();
        
        //getting all the distance of moving objects to avoid them
        double distanceWire0 = this.getDistance(wires[0]);
        double distanceWire1 = this.getDistance(wires[1]);
        double distancebob= this.getDistance(bob);
        double distancedot = this.getDistance(dot);
        
        //array for waypoints. The Slash Character will mover around these points guarding the door. 
        
        double[] x= new double[] {600,680,680};
        double[] y= new double[] {250,340,150};
       
        //to find the distance of waypoints from slash  
        double difx= y[a]-getY();
        double dify= x[a]-getX();
        
        double direction;
        
        
        //if bob is close fleeing is prioritized than moving randomly along waypoints
        if ( distancebob < 25){
           direction = Math.atan2(getY()-yb, getX()-xb)/Math.PI*180;
           
        }
        //if dot is too close chsing is proritized than moving randomly but is less prioritized than fleeding from bob 
        else if (distancedot < 50){
           direction = Math.atan2(yd-getY(), xd-getX())/Math.PI*180;
           
        }
        
       
        
       /**If hack character dies due to Bob, Slash leaves its position and goes after Dot. This shows coordination
        * between the two NPC characters that makes it appear intelligent. Also this makes the game more competative.
        */    
        
       else if (!hack.isAlive() ){
       
      //Similar code as HACK
            
       double speed = SPEED; // Speed to move in this direction. It should not exceed SPEED.
       direction = Math.atan2(yd-getY(), xd-getX())/Math.PI*180;
     
       
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
        }
        
        else {
            /**Codes that check the proximity of slash character with the current way points. 
             When the slash character reaches the current way points it calls random to find 
            the next random way point position for slash to move to. Randomness demonstrates the 
            * intelligence and makes game less predictable.
            */
            if(Math.abs(difx)<2 && Math.abs(dify)<2){
            a=random.nextInt(3);
            }
       
            direction = Math.atan2(y[a]-getY(),x[a]-getX())/Math.PI*180;
        }
        
        
        double speed = SPEED; // Speed to move in this direction. It should not exceed SPEED.
              
        // Call to superclass method to actually move Slash in this direction. 
        // DO NOT MODIFY!
        if (speed > SPEED) speed = SPEED;
        move(direction, speed, walls);        
    }
}
