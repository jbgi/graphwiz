package graphWiz.model;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

@SuppressWarnings("serial")
public class GWizGraph extends ListenableDirectedWeightedGraph<GWizVertex, GWizEdge> {
	
	private String info;

	public GWizGraph() {
		super(GWizEdge.class);
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
