package graphWiz.model;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

/**
 * @author  jbg
 */
@SuppressWarnings("serial")
public class GWizGraph extends ListenableDirectedWeightedGraph<GWizVertex, GWizEdge> {
	
	private String info;

	public GWizGraph() {
		super(GWizEdge.class);
	}

	/**
	 * @return  the info
	 * @uml.property  name="info"
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info  the info to set
	 * @uml.property  name="info"
	 */
	public void setInfo(String info) {
		this.info = info;
	}

}
