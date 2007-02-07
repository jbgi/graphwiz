package graphWiz.model;

import java.util.Stack;

public abstract class GraphManipulator {

	private GWizGraph currentGraph;

	private Stack<GWizGraph> graphHistory;

	public GraphManipulator() {
	}

	public GWizGraph getCurrentGraph() {
		return currentGraph;
	}

	void restorePreviousGraph() {
		if (!graphHistory.isEmpty())
			currentGraph = graphHistory.pop();
	}

	void saveGraph() {
		graphHistory.add((GWizGraph) currentGraph.clone());
	}

	public void setGraph(GWizGraph gWizGraph) {
		currentGraph = gWizGraph;
		graphHistory.clear();
		graphHistory.add(currentGraph);
	}

}
