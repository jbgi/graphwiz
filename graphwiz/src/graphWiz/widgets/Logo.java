package graphWiz.widgets;

import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.LabelUI;

public class Logo extends JPanel{
	public Logo(){
		super();
		URL logoUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/logo.png");
		ImageIcon icon = new ImageIcon(logoUrl);	
		Image im = icon.getImage();
		
	}

}
