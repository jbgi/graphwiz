package graphWiz;

import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.LabelUI;
import java.awt.*;

public class Logo extends JPanel{
	public Logo(){
		super();
		ImageIcon icon = createImageIcon("/GraphWiz/src/graphWiz/resources/logo.jpg","a pretty but meaningless splat");	
		JLabel im = new JLabel(icon);
		
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = LabelUI.class.getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}



}
