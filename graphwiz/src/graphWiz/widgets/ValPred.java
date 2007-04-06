package graphWiz.widgets;
import graphWiz.model.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ValPred extends JPanel{

	Box panneau;
	String[] columnNames;
	Object[][] val;
	Object[][] pred;
	JTable tableVal;
	JTable tablePred;
    DefaultTableModel valData;
    DefaultTableModel predData;
	
	public ValPred(){
		
		panneau = Box.createHorizontalBox();
		//création des vecteurs
		//String[] columnNames = {"1","2", "3", "4","5"}; 
        //Object[][] val = {{"<html><font size=5> 0 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>","<html><font size=5> &#8734 </font></html>"}};
        //Object[][] pred = {{"<html><font size=5> 0 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>","<html><font size=5> &#150 </font></html>"}};
        columnNames= new String[5];
        val = new Object[1][5];
        pred=new Object[1][5];
        val[0][0] = 0; pred[0][0]=0;columnNames[0]="1";
        val[0][1] = 0; pred[0][1]=0;columnNames[1]="2";
        val[0][2] = 0; pred[0][2]=0;columnNames[2]="3";
        val[0][3] = 0; pred[0][3]=0;columnNames[3]="4";
        val[0][4] = 0; pred[0][4]=0;columnNames[4]="5";
        valData = new DefaultTableModel();
        predData = new DefaultTableModel();
		tableVal = new JTable(valData);
        tablePred = new JTable(predData);
        
        //mise en valeur des vecteurs
        tableVal.setBackground(Color.orange);
        tablePred.setBackground(Color.yellow);
        tableVal.setFocusable(false);
        tablePred.setFocusable(false);
        
        JScrollPane valPan = new JScrollPane(tableVal);
        valPan.setMaximumSize(new Dimension(400,40));
        valPan.setMinimumSize(new Dimension(100,40));
        valPan.setPreferredSize(new Dimension(250,40));
        JScrollPane predPan = new JScrollPane(tablePred);
        predPan.setAutoscrolls(true);
        predPan.setMaximumSize(new Dimension(400,40));
        predPan.setMinimumSize(new Dimension(100,40));
        predPan.setPreferredSize(new Dimension(250,40));
        
        panneau.add(new JLabel("<html><blockquote><font size=5>  VAL : </font></blockquote></html>"));
        panneau.add(valPan);
        panneau.add(new JLabel("<html><blockquote><font size=5> PRED : </font></blockquote></html>"));
        panneau.add(predPan);
        
        
        add(panneau);
        
	}
	
	public void update(GWizGraph graph){
		int NbSommet=graph.vertexSet().size();
        columnNames= new String[NbSommet];
        val = new Object[1][NbSommet];
        pred=new Object[1][NbSommet];
        valData.setColumnCount(NbSommet);
        valData.setRowCount(1);
        int b=0;
		Iterator<GWizVertex> i = graph.vertexSet().iterator();
		while (i.hasNext()){
			GWizVertex v = i.next();
			val[0][b] =((int)(v.getValuation()));
			tableVal.getModel().setValueAt(v.getValuation(), 0, b);
			if (v.hasPred())
				pred[0][b] = v.getPred().getName();
			columnNames[b]= v.getName();
			b++;
		}
		valData.setColumnIdentifiers(columnNames);
	}
	
}
