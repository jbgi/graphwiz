package widgets;

import javax.swing.*;

public class Fenetre {

	private JFrame frame;
	
	public JFrame getFrame(){
		return this.frame;
	}
	
	public void showOnFrame(JComponent component, String frameName) {
		this.frame = new JFrame(frameName);

//		1. Optional: Specify who draws the window decorations. 
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setSize(1100, 750);

//		4. Create components and put them in the frame.
//		...create emptyLabel...
		frame.getContentPane().add(component);

//		5. Size the frame.
		frame.pack();

//		6. Show it.
		frame.setVisible(true);

	}
}
