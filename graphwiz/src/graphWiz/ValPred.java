package graphWiz;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class ValPred extends JPanel{

	
	public ValPred(){
		
		super(new GridLayout(1,1));

		String[] columnNames = {"1","2", "3", "4","5"}; 
        Object[][] val = {{0,"inf","inf","inf","inf"}};
        Object[][] pred = {{0,"-","-","-","-"}};
        final JTable tableVal = new JTable( val, columnNames);
        final JTable tablePred = new JTable( pred, columnNames);
        tableVal.setPreferredScrollableViewportSize(new Dimension(500, 70));
        tablePred.setPreferredScrollableViewportSize(new Dimension(500, 70));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        //JComponent panel1 = makeTextPanel("Panel #1");
        //panel1.add(tableVal);
        tabbedPane.addTab("VAL", null, tableVal,null);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
       // JComponent panel2 = makeTextPanel("Panel #2");
        //panel2.add(tablePred);
        tabbedPane.addTab("PRED", null, tablePred,null);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        add(tabbedPane);       
	}
	
	 protected JComponent makeTextPanel(String text) {
	        JPanel panel = new JPanel(false);
	        JLabel filler = new JLabel(text);
	        filler.setHorizontalAlignment(JLabel.CENTER);
	        panel.setLayout(new GridLayout(1, 1));
	        panel.add(filler);
	        return panel;
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
        ValPred newContentPane = new ValPred();
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
