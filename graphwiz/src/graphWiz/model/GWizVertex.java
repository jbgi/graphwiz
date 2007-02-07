package graphWiz.model;

import org.jgrapht.VertexFactory;

@SuppressWarnings("serial")
public class GWizVertex implements VertexFactory<GWizVertex> {

	private boolean fixed = false;
	
	private boolean fixing = false;
	
	private boolean updated = false;
	
	private boolean hasPred = false;
	
	private GWizVertex previousPred = null;
	
	private double previousValuation = Double.POSITIVE_INFINITY;

	private String info = "";

	private String name;

	private GWizVertex pred;

	/**
	 * used in Dijstra and Bellman algorithm
	 * 
	 * @uml.property name="valuation"
	 */
	private double valuation;

	public GWizVertex(String name) {

		this.name = name;
		this.info = "";
		this.valuation = Float.POSITIVE_INFINITY;
	}

	public GWizVertex createVertex() {
		return new GWizVertex("#");
	}

	public String getInfo() {
		return info;
	}
	
	public boolean hasPred(){
		return hasPred;
	}

	public String getName() {
		return this.name;
	}

	public GWizVertex getPred() {
		if (hasPred())
			return pred;
		else
			return this;
	}

	public double getValuation() {
		return valuation;
	}

	public boolean isFixed() {
		return fixed;
	}

	public boolean isValuated() {
		return (valuation != Float.POSITIVE_INFINITY);
	}

	public void fixeMe() {
		fixed = true;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setName(final String nomDuNoeud) {
		this.name = nomDuNoeud;
	}

	public void setPred(GWizVertex pred) {
		setPreviousPred(pred);
		this.pred = pred;
		hasPred = true;
	}

	public void setValuation(double valuation) {
		setPreviousValuation(this.valuation);
		this.valuation= valuation;
	}

	public String toString() {
		return this.name;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public boolean isFixing() {
		return fixing;
	}

	public void setFixing(boolean fixing) {
		this.fixing = fixing;
	}

	public GWizVertex getPreviousPred() {
		return previousPred;
	}

	private void setPreviousPred(GWizVertex previousPred) {
		this.previousPred = previousPred;
	}

	public double getPreviousValuation() {
		return previousValuation;
	}

	private void setPreviousValuation(double previousValuation) {
		this.previousValuation = previousValuation;
	}

}
