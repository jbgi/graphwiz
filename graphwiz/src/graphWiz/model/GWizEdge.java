package graphWiz.model;

import java.io.Serializable;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.EdgeFactory;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * @author  jbg
 */
@SuppressWarnings("serial")
public class GWizEdge extends DefaultWeightedEdge implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private GWizGraph gWizGraph;
	
	/**
	 * @author  jbg
	 */
	public enum Description {
		SELECT, EXPLORER, EXPLORED, PATH, REGULAR
	}
	
	private Description description = Description.REGULAR;
	
	private String info = "1";

	/**
	 * @return  the description
	 * @uml.property  name="description"
	 */
	public Description getDescription() {
		return this.description;
	}

	/**
	 * @return  the info
	 * @uml.property  name="info"
	 */
	public String getInfo() {
		if (gWizGraph!=null)
			return Double.toString(gWizGraph.getEdgeWeight(this));
		else
			return this.info;
	}

	/**
	 * @param description  the description to set
	 * @uml.property  name="description"
	 */
	public void setDescription(Description description) {
		this.description = description;
	}

	/**
	 * @param info  the info to set
	 * @uml.property  name="info"
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String toString(){
		return getInfo();
	}

	protected void reset() {
		description = Description.REGULAR;
	}

}

