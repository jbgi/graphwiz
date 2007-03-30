package graphWiz.model;
import java.util.Iterator;

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
	
	public int nbVertex(){
		return this.vertexSet().size();
	}
	
	public void clear() {
		if (vertexSet().size()!=0){
			Object[] v = vertexSet().toArray();
			for (int i=0 ; i<v.length; i++)
				this.removeVertex((GWizVertex)v[i]);
		}
	}

}
