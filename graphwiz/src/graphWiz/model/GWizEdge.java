package graphWiz.model;

import org.jgrapht.graph.DefaultWeightedEdge;

@SuppressWarnings("serial")
public class GWizEdge extends DefaultWeightedEdge {
	
	public enum Description {
		EXPLORER, PATH, REGULAR
	}
	
	private Description description;
	
	private String info;

	public Description getDescription() {
		return this.description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(final String info) {
		this.info = info;
	}

}

