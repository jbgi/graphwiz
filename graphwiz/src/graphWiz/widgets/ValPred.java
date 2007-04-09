package graphWiz.widgets;
import graphWiz.model.Algorithm;
import graphWiz.model.Floyd;
import graphWiz.model.GWizGraph;
import graphWiz.model.GWizVertex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ValPred extends JPanel{

	JScrollPane panneau;
	String[] columnNames;
	
	JTable tableVal;
	JTable tablePred;
    DefaultTableModel valData;
    DefaultTableModel predData;
    JScrollPane valPan;
    JScrollPane predPan;
    DecimalFormat decFormatter;
    DefaultTableCellRenderer rN;
    DefaultTableCellRenderer rT;
	public ValPred(){

		this.setLayout(new BorderLayout());
		
		NumberFormat numberFormatter = NumberFormat.getInstance();
		decFormatter = (DecimalFormat)numberFormatter;
		
		rN = new DefaultTableCellRenderer() {
			  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			    Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			    // Create and put formatted double in cell
			    setText(decFormatter.format((Double)value));
			    // Reset right justify again as it gets lost
			    setHorizontalAlignment(JLabel.CENTER);
			    return renderer;
			  }
			};
		
		rT = new DefaultTableCellRenderer() {
			  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				    Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				    // Create and put formatted double in cell
				    if (value!=null)
				    	setText(value.toString());
				    // Reset right justify again as it gets lost
				    setHorizontalAlignment(JLabel.CENTER);
				    return renderer;
				  }
				};
			
		
		JPanel panList = new JPanel();
		
		panList.setLayout(new GridLayout(1,2));
		
		JPanel panLabel = new JPanel();
		
		panLabel.setLayout(new GridLayout(1,2));
		
        valData = new DefaultTableModel();
        predData = new DefaultTableModel();
		tableVal = new JTable(valData);
        tablePred = new JTable(predData);
        
        //mise en valeur des vecteurs
        tableVal.setBackground(Color.orange);
        tablePred.setBackground(Color.yellow);
        
        tableVal.setFocusable(false);
        tablePred.setFocusable(false);
        
        valPan = new JScrollPane(tableVal);
        valPan.setAutoscrolls(true);
        
        predPan = new JScrollPane(tablePred);
        predPan.setAutoscrolls(true);
        
        panLabel.add(new JLabel("<html><font size=5>Valuations :</font></html>"));
        panLabel.add(new JLabel("<html><font size=5> Pr&eacute;decesseurs : </font></html>"));
        panList.add(valPan);
        panList.add(predPan);
        add(panLabel, BorderLayout.NORTH);
        add(panList, BorderLayout.CENTER);
        
	}
	
	public void update(GWizGraph graph, Algorithm algo){
		int NbSommet=graph.vertexSet().size();
        int TailleColumn = 5;
        if(NbSommet>6)
        	TailleColumn=4;
        if(NbSommet>=15)
        	TailleColumn = 2;
		columnNames= new String[NbSommet];
    	predData.setColumnCount(NbSommet);
    	valData.setColumnCount(NbSommet);
    	
        if (!(algo instanceof Floyd)){        	
        	valData.setRowCount(1);
        	predData.setRowCount(1);
        	int b=0;
        	Iterator<GWizVertex> i = graph.vertexSet().iterator();
        	
        	while (i.hasNext()){
        		GWizVertex v = i.next();
        		tableVal.getModel().setValueAt(v.getValuation(), 0, b);
        		if (v.hasPred() && v.getPred()!=null)
        			tablePred.getModel().setValueAt(v.getPred().getName(), 0, b);
        		columnNames[b]= "<html><font size="+Math.max(4, 7-NbSommet/3)+">"+v.getName()+"</font></html>";
        		b++;
        	}
        	valData.setColumnIdentifiers(columnNames);
        	predData.setColumnIdentifiers(columnNames);
            for (int j = 0; j<NbSommet;j++){
            	tableVal.setFont(tableVal.getFont().deriveFont((float) Math.max(10, 25-NbSommet)));
            	tablePred.setFont(tablePred.getFont().deriveFont((float) Math.max(10, 25-NbSommet)));
            	tableVal.getColumn(valData.getColumnName(j)).setCellRenderer(rN);
                tablePred.getColumn(predData.getColumnName(j)).setCellRenderer(rT);
            }
        }
        else update2(graph,(Floyd) algo);
        
        tablePred.setRowHeight(Math.max(10, 25-NbSommet)+1);
        tableVal.setRowHeight(Math.max(10, 25-NbSommet)+1);

	}
	
	public void update2(GWizGraph graph, Floyd algo){
		System.out.println("updateFloyd");
		int NbSommet=graph.vertexSet().size();
		columnNames= new String[NbSommet+1];
		String[] columnNamesPred = new String[NbSommet+1];
        System.out.println("il y a "+NbSommet+" sommets");
        valData.setColumnCount(NbSommet+1);
        valData.setRowCount(NbSommet);
        predData.setColumnCount(NbSommet+1);
        predData.setRowCount(NbSommet);
        int i=0;
        columnNames[0]="<html><font size="+Math.max(3, 6-NbSommet/3)+">V<sup>"+Integer.toString(algo.getIteration())+"</sup></font></html>";
        columnNamesPred[0]="";
        for(int a=0;a<NbSommet;a++){
        	tableVal.getModel().setValueAt("<html><font size="+Math.max(4, 7-NbSommet/3)+">"+a+"</font></html>", a, 0);
        	tablePred.getModel().setValueAt("<html><font size="+Math.max(4, 7-NbSommet/3)+">"+a+"</font></html>", a, 0);
        	for(int b=1;b<NbSommet+1;b++){
        		tableVal.getModel().setValueAt(algo.getVal()[a][b-1], a, b);
        		tablePred.getModel().setValueAt(algo.getPred()[a][b-1], a, b);
        	}
        	if(a>0){
        		columnNames[a]="<html><font size="+Math.max(4, 7-NbSommet/3)+">"+i+"</font></html>";
        		i++;
        	}
				
        }
        if(algo.getArrivee()<NbSommet && algo.getDepart()<NbSommet)
        	tableVal.getModel().setValueAt(algo.getVal()[algo.getDepart()][algo.getArrivee()], algo.getDepart(), algo.getArrivee()+1);
        columnNames[NbSommet]= "<html><font size="+Math.max(4, 7-NbSommet/3)+">"+(NbSommet-1)+"</font></html>";
        valData.setColumnIdentifiers(columnNames);
        columnNames[0]="";
        predData.setColumnIdentifiers(columnNames);
        for (int j = 1; j<=NbSommet;j++){
        	tableVal.setFont(tableVal.getFont().deriveFont((float) Math.max(10, 25-NbSommet)));
        	tablePred.setFont(tablePred.getFont().deriveFont((float) Math.max(10, 25-NbSommet)));
        	tableVal.getColumn(valData.getColumnName(j)).setCellRenderer(rN);
            tablePred.getColumn(predData.getColumnName(j)).setCellRenderer(rT);
        }
        
	}
	
}

