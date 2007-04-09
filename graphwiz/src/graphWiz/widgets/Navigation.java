package graphWiz.widgets;

import graphWiz.GWizModelAdapter;
import graphWiz.model.Algorithm;
import graphWiz.model.Bellman;
import graphWiz.model.Dijkstra;
import graphWiz.model.Floyd;
import graphWiz.visual.GWizVertexValuationView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import org.jgraph.JGraph;


public class Navigation extends JPanel{
	
	protected ImageIcon begin, back, pause, play, forward,end;
	private String[] algos = {"Selectionner l'algorithme", "DIJKSTRA","BELLMAN","FLOYD"};
	private JComboBox choixAlgo;
	protected JToolBar bHor; 
	private JGraph jgraph;
	public Algorithm algo;
	public JList algoText = new JList();
	public JTextArea commentaires = new JTextArea(" Bienvenue sur GraphWiz ... Le simulateur d'algorithmes de graphes ...  ");
	private Graphics valuations;
	private Algorithm dijkstra;
	private Algorithm bellman;
	private Algorithm floyd;
	private boolean pauseTimer = true;
	
	private Timer timer = new Timer();
	private TimerTask updateAlgo;
	
	private ValPred valPred;
	
	//private Logo logo = new Logo();
	public Navigation(){
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void start(JGraph editorGraph, ValPred valpred){
		
		valPred=valpred;
		bHor = new JToolBar();
		jgraph=editorGraph;
		dijkstra = new Dijkstra(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		bellman = new Bellman(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		floyd = new Floyd(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		algo = dijkstra;
		algoText.setListData(algo.getAlgo());
		algoText.setEnabled(false);
		algoText.setSelectionBackground(Color.CYAN);
		begin = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_begin.gif"));
        back = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_bf.gif"));
        pause = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_pause.gif"));
        play = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_play.gif"));
        forward = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_ff.gif"));
        end = new ImageIcon(Navigation.class.getClassLoader().getResource(
		"graphWiz/resources/btn_end.gif"));
        
        choixAlgo = new JComboBox(algos);
        choixAlgo.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			
        	if(choixAlgo.getSelectedIndex()== 1){
				algo.retoreInitialState();
				algo = dijkstra;
				algoText.setListData(algo.getAlgo());
				algo.initialize();
				valPred.update(algo.getGraph(),algo);
				commentaires.setText(algo.getCommentaires());
				algoText.setSelectedIndex(algo.getCurrentStep());
			}
			if (choixAlgo.getSelectedIndex()==2){
				algo.retoreInitialState();
				algo = bellman;
				
				algoText.setListData(algo.getAlgo());
				algo.initialize();
				valPred.update(algo.getGraph(),algo);
				commentaires.setText(algo.getCommentaires());
				algoText.setSelectedIndex(algo.getCurrentStep());
			}
			if (choixAlgo.getSelectedIndex()==3){
				algo.retoreInitialState();
				algo = floyd;
				algoText.setListData(algo.getAlgo());
				algo.initialize();
				valPred.update(algo.getGraph(),algo);
				commentaires.setText(algo.getCommentaires());
				algoText.setSelectedIndex(algo.getCurrentStep());
			}
			}
        });
        
        commentaires.setPreferredSize(new Dimension(430,450));
        commentaires.setMaximumSize(new Dimension(430,500));
        
        commentaires.setBackground(Color.WHITE);
        add(commentaires);

        bHor.add(new AbstractAction("",begin){
        	public void actionPerformed(ActionEvent arg0) {
				algo.retoreInitialState();
				valPred.update(algo.getGraph(),algo);
				algoText.setSelectedIndex(algo.getCurrentStep());
				commentaires.setText("Retour au graphe initial");
				jgraph.repaint();
			}});
        bHor.addSeparator();
       
        bHor.add(new AbstractAction("", back) {
			public void actionPerformed(ActionEvent e) {
				algo.previousStep();
				valPred.update(algo.getGraph(),algo);
				algoText.setSelectedIndex(algo.getCurrentStep());
				commentaires.setText("étape précédente");
				jgraph.repaint();
			}
        });	
        bHor.addSeparator();
       
        bHor.add(new AbstractAction("",pause){

			public void actionPerformed(ActionEvent arg0) {
				if (updateAlgo!=null)
					updateAlgo.cancel();
				commentaires = new JTextArea("Cliquez sur play pour continuer");
			}
		});
        
        bHor.add(new AbstractAction("",play){

			public void actionPerformed(ActionEvent arg0) {
				if (updateAlgo!=null)
					updateAlgo.cancel();
		        updateAlgo = new TimerTask() {
				    public void run(){
				    	if (algo.isRunnable()){
							algo.nextStep();
							valPred.update(algo.getGraph(),algo);
							algoText.setSelectedIndex(algo.getCurrentStep());
							commentaires.setText(algo.getCommentaires());
							jgraph.repaint();
				    	}
				    	
				    }
		        };
				timer.scheduleAtFixedRate(updateAlgo, 1500, 1500);
			}}); 
        
        bHor.addSeparator();
        
        bHor.add(new AbstractAction("", forward) {
			public void actionPerformed(ActionEvent e) {
				algo.nextStep();
				algoText.setSelectedIndex(algo.getCurrentStep());
				commentaires.setText(algo.getCommentaires());
				valPred.update(algo.getGraph(),algo);
				jgraph.repaint();
			}
		});
                
        bHor.addSeparator();

        bHor.add(new AbstractAction("",end){
        	public void actionPerformed(ActionEvent arg0) {
        		if (algo.isRunnable()){
				while (!algo.isEnd())
					algo.nextStep();
				jgraph.repaint();
				commentaires.setText(algo.getCommentaires());
				valPred.update(algo.getGraph(),algo);
        		}
        		algoText.setSelectedIndex(algo.getCurrentStep());
			}});
        bHor.addSeparator();
        
        bHor.add(choixAlgo);
        
        bHor.setFloatable(false);
        bHor.setAlignmentY(200);
        Dimension bsize = new Dimension(450,30);
        bHor.setMaximumSize(bsize);
        bHor.setPreferredSize(bsize);
        add(bHor);
        JPanel jpAlgo = new JPanel();
        algoText.setPreferredSize(new Dimension(430,1000));
        algoText.setMaximumSize(new Dimension(430,2048));
        jpAlgo.setPreferredSize(new Dimension(450,1000));
        jpAlgo.setMaximumSize(new Dimension(450,2048));
        jpAlgo.setBackground(Color.WHITE);
        jpAlgo.add(algoText);
        add(jpAlgo);
		URL logoUrl = getClass().getClassLoader().getResource(
		"graphWiz/resources/logo.png");
		ImageIcon logo = new ImageIcon(logoUrl);
		JButton jLogo = new JButton(logo);
		jLogo.setToolTipText("Crédits");
		jLogo.setAction(new AbstractAction("", logo) {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Crédits");
				JPanel panel = new JPanel();
				Object[][] tableau = {{"<html><b>Version du logiciel</b></html>","<html><b>1.0.1</b></html>"},
									{"<html><b>Tuteurs</b></html>","JUSSIEN Christelle"},
									  {" ","JUSSIEN Narendra"},
									  {"<html><b>Chef de projet</b></html>","RAJESSON Fanja"},
									  {"<html><b>Responsable technique</b></html>","GIRAUDEAU Jean-Baptiste"},
									  {"<html><b>Autres Membres de </b></html>","OLIVIER Luc"},
									  {"<html><b> l'équipe projet</b></html>","CANTU Paulina"},
									  {" ","REYES Felix"}};
				String[] Colonnes={"1","2"};
				JTable table = new JTable(tableau,Colonnes);
				table.getColumnModel().getColumn(0).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.setRowHeight(15);
				panel.add(table);
		        frame.add(panel);
				frame.setSize(450,170);
				frame.setVisible(true);
			}
		});
		JPanel jpLogo = new JPanel();
		Dimension lsize = new Dimension(450,120);
		jpLogo.setPreferredSize(lsize);
		jpLogo.setMaximumSize(lsize);
		//jLogo.setMinimumSize(lsize);
		jpLogo.add(jLogo);
		add(jpLogo);
		setRequestFocusEnabled(false);
		stopExplorer();
	}
	
	public void restart(JGraph editorGraph){
		jgraph=editorGraph;
		dijkstra = new Dijkstra(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		bellman = new Bellman(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		floyd = new Floyd(((GWizModelAdapter) jgraph.getModel()).getGWizGraph());
		algo = dijkstra;
		algoText.setListData(algo.getAlgo());
	}

	public void stopExplorer() {
		for (int i = 0; i< (bHor.getComponentCount()-1);i++)
			bHor.getComponent(i).setEnabled(false);
		jgraph.getGraphLayoutCache().reload();
	}

	public void startExplorer() {
		for (int i = 0; i< (bHor.getComponentCount()-1);i++)
			bHor.getComponent(i).setEnabled(true);
		Iterator i = algo.getGraph().vertexSet().iterator();
		while (i.hasNext()){
			GWizVertexValuationView view = new GWizVertexValuationView(((GWizModelAdapter)jgraph.getModel()).getVertexCell(i.next()),(GWizModelAdapter)jgraph.getModel());
			jgraph.getGraphLayoutCache().insertViews(new GWizVertexValuationView[] {view});
			view.translate(0, -9);
		}
		algo.initialize();
		commentaires.setText(algo.getCommentaires());
		valPred.update(algo.getGraph(),algo);
		algoText.setSelectedIndex(algo.getCurrentStep());
	}

	public void inhiberChoixAlgo(){
		this.choixAlgo.setEnabled(false);
		this.choixAlgo.setOpaque(true);
	}
	
	public void activerChoixAlgo(){
		this.choixAlgo.setEnabled(true);
		this.choixAlgo.setOpaque(false);
	}
}