package graphWiz.widgets;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Logo extends JPanel{
	public Logo(){
		super();
		URL logoUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/logo.png");
		ImageIcon icon = new ImageIcon(logoUrl);	
		Image im = icon.getImage();
		
	}

}
