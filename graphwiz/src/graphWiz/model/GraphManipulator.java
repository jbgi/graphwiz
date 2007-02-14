package graphWiz.model;

import graphWiz.model.GWizEdge.Description;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public abstract class GraphManipulator {
	
	private GWizGraph graph;

	private Stack<Vector<Description>> edgesDescriptionHistory;
	
	private Stack<Vector<boolean[]>> verticesFlagHistory;
	
	private Stack<Vector<GWizVertex[]>> verticesPredHistory;
	
	private Stack<Vector<double[]>> verticesValuationHistory;

	public GraphManipulator() {
		edgesDescriptionHistory = new Stack<Vector<Description>>();
		verticesFlagHistory = new Stack<Vector<boolean[]>>();
		verticesPredHistory = new Stack<Vector<GWizVertex[]>>();
		verticesValuationHistory = new Stack<Vector<double[]>>();
	}

	public GWizGraph getGraph() {
		return graph;
	}

	void restorePreviousGraph() {
		
		if (!verticesFlagHistory.isEmpty()){
			Vector<boolean[]> verticesFlag = verticesFlagHistory.pop();
			Vector<GWizVertex[]> verticesPred = verticesPredHistory.pop();
			Vector<double[]> verticesValuation = verticesValuationHistory.pop();
			Vector<Description> edgesDescription = edgesDescriptionHistory.pop();
			
			Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
			while (i.hasNext()){
				int j = 0;
				GWizVertex v = i.next();
				v.setFixed(verticesFlag.get(j)[0]);
				v.setFixing(verticesFlag.get(j)[1]);
				v.setUpdated(verticesFlag.get(j)[2]);
				v.setHasPred(verticesFlag.get(j)[3]);
				
				v.setPred(verticesPred.get(j)[0]);
				v.setPreviousPred(verticesPred.get(j)[1]);
				
				v.setValuation(verticesValuation.get(j)[0]);
				v.setPreviousValuation(verticesValuation.get(j)[1]);
				
				j++;
			}
			
			Iterator<GWizEdge> j = getGraph().edgeSet().iterator();
			while (j.hasNext()){
				int k =0;
				j.next().setDescription(edgesDescription.get(k));
				k++;
			}
		}
	}

	void saveGraph() {
		
		Vector<boolean[]> verticesFlag = new Vector<boolean[]>();
		Vector<GWizVertex[]> verticesPred = new Vector<GWizVertex[]>();
		Vector<double[]> verticesValuation = new Vector<double[]>();
		Vector<Description> edgesDescription = new Vector<Description>();
		
		Iterator<GWizVertex> i = getGraph().vertexSet().iterator();
		while (i.hasNext()){
			GWizVertex v = i.next();
			
			boolean[] t1 = {v.isFixed(), v.isFixing(), v.isUpdated(), v.hasPred()};
			verticesFlag.add(t1);
			
			GWizVertex[] t2 = {v.getPred(), v.getPreviousPred()};
			verticesPred.add(t2);
			
			double[] t3 = {v.getValuation(), v.getPreviousValuation()};
			verticesValuation.add(t3);
		}
		
		Iterator<GWizEdge> j = getGraph().edgeSet().iterator();
		while (j.hasNext())
			edgesDescription.add(j.next().getDescription());
	}

	public void setGraph(GWizGraph gWizGraph) {
		graph = gWizGraph;
		edgesDescriptionHistory.clear();
		verticesFlagHistory.clear();
		verticesPredHistory.clear();
		verticesValuationHistory.clear();
	}

}
