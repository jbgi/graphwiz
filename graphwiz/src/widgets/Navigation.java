package widgets;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Navigation extends JPanel {
	
	protected JButton begin, back, pause, play, forward;
	protected Box bHor; 
	
	public Navigation(){
		super(new GridLayout(1,1));
			bHor= Box.createHorizontalBox();
	        begin = new JButton(new ImageIcon("src/graphWiz/resources/btn_begin.gif"));
	        back = new JButton(new ImageIcon("src/graphWiz/resources/btn_bf.gif"));
	        pause = new JButton(new ImageIcon("src/graphWiz/resources/btn_pause.gif"));
	        play = new JButton(new ImageIcon("src/graphWiz/resources/btn_play.gif"));
	        forward = new JButton(new ImageIcon("src/graphWiz/resources/btn_ff.gif"));
	        bHor.add(begin);
	        bHor.add(back);
	        bHor.add(pause);
	        bHor.add(play);
	        bHor.add(forward);
	      add(bHor); 
	    }
	
	 /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Navigation newContentPane = new Navigation();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    }

