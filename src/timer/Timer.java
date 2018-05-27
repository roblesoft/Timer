/*q
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timer;
import java.applet.AudioClip;
import java.io.File;
import java.sql.*;
import javax.swing.JFrame;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author uriel
 */
public class Timer {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        frame timer = new frame();
        timer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timer.setSize(800, 450);
        timer.setVisible(true);
        

    } 
}
