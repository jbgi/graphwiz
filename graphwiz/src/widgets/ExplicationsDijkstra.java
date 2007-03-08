package widgets;

import java.awt.GridLayout;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ExplicationsDijkstra extends JPanel {

	public String Algorithme = "../GraphWiz/Dijkstra.txt";
	
	public String[] algo = new String[14];
	
	JList explicationsDijkstra;
	
	public ExplicationsDijkstra(){
		super(new GridLayout(1,0));
		explicationsDijkstra = new JList(algo);
		add(new JScrollPane(explicationsDijkstra));
		//explicationsDijkstra.setFocusable(true);
		
		LireFichierAlgo();
		
	}
	
	public void LireFichierAlgo() {
		try {
			RandomAccessFile raf = new RandomAccessFile("../GraphWiz/Dijkstra.txt", "r");
			String ligne;
			int i=0;
			while ((ligne = raf.readLine()) != null ){
				algo[i]=ligne;
				i++;
			}
			algo[13] = "   tttttt";
			System.out.println(algo[10]);
			explicationsDijkstra.setListData(algo);
			System.out.println("algo chargé");
		}  
		catch (IOException e) {    
			System.out.println("erreur dans: " + e);
		}	
	}
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Explications Dijkstra");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        ExplicationsDijkstra newContentPane = new ExplicationsDijkstra();
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
