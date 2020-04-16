/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reboot;

import java.awt.Color;
/**
 * Exit object that player moves towards.
 * @author jrsullins
 */
public class Exit extends Entity
{
    public Exit(int x, int y, int width, int height) {
        super(x, y, width, height, Color.CYAN);        
    }
    
    /**
     * Checks whether Dot has reached exit (ending game).
     * @param dot player character
     * @return 
     */
    public boolean checkExit(Dot dot) {
        if (intersects(dot)) {
            return true;
        }
        return false;
    }
    
}
