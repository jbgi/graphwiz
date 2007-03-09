package graphWiz.widgets;

import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.LabelUI;

public class Logo extends JFrame{
	public Logo(){
		super();
		ImageIcon icon = createImageIcon("/GraphWiz/src/graphWiz/resources/logo.JPG","a pretty but meaningless splat");	
		Image im = icon.getImage(); 
		
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
