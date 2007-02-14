package graphWiz.model;

import org.jgrapht.VertexFactory;

@SuppressWarnings("serial")
public class GWizVertex implements VertexFactory<GWizVertex> {

	private boolean fixed = false;
	
	private boolean fixing = false;
	
	private boolean hasPred = false;
	
	private String name;
	
	private GWizVertex pred;
	
	private GWizVertex previousPred = null;

	private double previousValuation = Double.POSITIVE_INFINITY;

	private boolean updated = false;

	private double valuation;

	public GWizVertex(String name) {

		this.name = name;
		this.valuation = Float.POSITIVE_INFINITY;
	}

	public GWizVertex createVertex() {
		return new GWizVertex("#");
	}
	
	public void fixeMe() {
		fixed = true;
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

	public GWizVertex getPreviousPred() {
		return previousPred;
	}

	public double getPreviousValuation() {
		return previousValuation;
	}

	public double getValuation() {
		return valuation;
	}

	public boolean hasPred(){
		return hasPred;
	}

	public boolean isFixed() {
		return fixed;
	}

	public boolean isFixing() {
		return fixing;
	}

	public boolean isUpdated() {
		return updated;
	}

	public boolean isValuated() {
		return (valuation != Float.POSITIVE_INFINITY);
	}
	
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public void setFixing(boolean fixing) {
		this.fixing = fixing;
	}
	
	public void setHasPred(boolean hasPred) {
		this.hasPred = hasPred;
	}

	public void setName(final String nomDuNoeud) {
		this.name = nomDuNoeud;
	}

	public void setPred(GWizVertex pred) {
		setPreviousPred(pred);
		this.pred = pred;
		hasPred = true;
	}

	public void setPreviousPred(GWizVertex previousPred) {
		this.previousPred = previousPred;
	}

	public void setPreviousValuation(double previousValuation) {
		this.previousValuation = previousValuation;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public void setValuation(double valuation) {
		setPreviousValuation(this.valuation);
		this.valuation= valuation;
	}

	public String toString() {
		return this.name;
	}

}
