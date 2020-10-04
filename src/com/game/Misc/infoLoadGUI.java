package com.game.Misc;

/**
 * Created by Master Don Pro (Prodige) on 07/02/2016.
 *
 * This Class will read from the instruction file in the dir, save it within
 * an ArrayList and display the contents within a denoted text field.
 */

import java.awt.*;
import java.awt.Event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.game.Main.GamePanel;

public class infoLoadGUI extends JFrame {

    private ArrayList<String> lines;

   // private JButton readInfo = new JButton("Read Instructions");
   // private JButton close = new JButton("Close Window");
    private TextArea ta = new TextArea();

    private Dimension gp = new Dimension(GamePanel.WIDTH * GamePanel.SCALE,700);
   // private Dimension btnD = new Dimension(30,50);
    public infoLoadGUI(){
        super();

        lines = new ArrayList<String>();


        //Create new window frame
        setPreferredSize(gp);
        setFocusable(true);
        requestFocus();
        setLocation(400,200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Attempt to read from file
        try{
            Scanner fileScanner = new Scanner(new FileReader("Instructions.txt"));
            String line;
            while (fileScanner.hasNextLine()){
                line = fileScanner.nextLine();
                lines.add(line);
            }//END While
            fileScanner.close();
        }//END Try
        catch (IOException e){
            e.printStackTrace();
        }//END Catch

        buildGUI();
    }//END Constructor

    //Getters
    public ArrayList<String> getLines(){
        return lines;
    }

    public void buildGUI(){

        setTitle("Instructions & Bibliography");
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        add(ta);

        String data = "";

        for (String s: lines){
            data = data + "\n" + s;
        }//END for

        ta.setText(data);
        ta.setEditable(false);
    }//END buildGUI

}//END Class
