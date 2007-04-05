package graphWiz.model;

import java.io.Serializable;

import org.jgrapht.VertexFactory;

/**
 * @author  jbg
 */
public class GWizVertex implements VertexFactory<GWizVertex>,Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int count = -1;

	private boolean fixed = false;
	
	private boolean fixing = false;
	
	private boolean hasPred = false;
	
	private boolean updatedDone = false;
	
	private boolean valuated = false;
	
	private boolean start;
	
	private boolean end;
	
	private String name;
	
	private GWizVertex pred = null;
	
	private GWizVertex previousPred = null;

	private double previousValuation = Float.POSITIVE_INFINITY;

	private boolean updated = false;

	private double valuation;

	public GWizVertex(String name) {
		this.name = name;
		this.valuation = Float.POSITIVE_INFINITY;
	}

	public GWizVertex createVertex() {
		count++;
		return new GWizVertex(Integer.toString(count));
	}

	/**
	 * @return  the name
	 * @uml.property  name="name"
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return  the pred
	 * @uml.property  name="pred"
	 */
	public GWizVertex getPred() {
		return pred;
	}

	/**
	 * @return  the previousPred
	 * @uml.property  name="previousPred"
	 */
	public GWizVertex getPreviousPred() {
		return previousPred;
	}

	/**
	 * @return  the previousValuation
	 * @uml.property  name="previousValuation"
	 */
	public double getPreviousValuation() {
		return previousValuation;
	}

	/**
	 * @return  the valuation
	 * @uml.property  name="valuation"
	 */
	public double getValuation() {
		return valuation;
	}

	public boolean hasPred(){
		return hasPred;
	}

	/**
	 * @return  the fixed
	 * @uml.property  name="fixed"
	 */
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * @return  the fixing
	 * @uml.property  name="fixing"
	 */
	public boolean isFixing() {
		return fixing;
	}

	/**
	 * @return  the updated
	 * @uml.property  name="updated"
	 */
	public boolean isUpdated() {
		return updated;
	}

	public boolean isValuated() {
		return valuated;
	}
	
	/**
	 * @param fixed  the fixed to set
	 * @uml.property  name="fixed"
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * @param fixing  the fixing to set
	 * @uml.property  name="fixing"
	 */
	public void setFixing(boolean fixing) {
		this.fixing = fixing;
	}
	
	/**
	 * @param hasPred  the hasPred to set
	 * @uml.property  name="hasPred"
	 */
	public void setHasPred(boolean hasPred) {
		this.hasPred = hasPred;
	}

	/**
	 * @param name  the name to set
	 * @uml.property  name="name"
	 */
	public void setName(final String nomDuNoeud) {
		this.name = nomDuNoeud;
	}

	/**
	 * @param pred  the pred to set
	 * @uml.property  name="pred"
	 */
	protected void setPred(GWizVertex pred) {
		if (pred!=null){
			setPreviousPred(this.pred);
			this.pred = pred;
			hasPred = true;
		}
	}

	/**
	 * @param previousPred  the previousPred to set
	 * @uml.property  name="previousPred"
	 */
	protected void setPreviousPred(GWizVertex previousPred) {
		this.previousPred = previousPred;
	}

	/**
	 * @param previousValuation  the previousValuation to set
	 * @uml.property  name="previousValuation"
	 */
	protected void setPreviousValuation(double previousValuation) {
		this.previousValuation = previousValuation;
	}

	/**
	 * @param updated  the updated to set
	 * @uml.property  name="updated"
	 */
	protected void setUpdated(boolean updated) {
		this.updated = updated;
		if (!updated)
			setUpdatedDone(false);
	}

	/**
	 * @param valuation  the valuation to set
	 * @uml.property  name="valuation"
	 */
	protected void setValuation(double valuation) {
		setPreviousValuation(this.valuation);
		this.valuation= valuation;
	}

	public String toString() {
		return this.name;
	}

	public boolean isUpdatedDone() {
		return updatedDone;
	}

	protected void setUpdatedDone(boolean updatedDone) {
		this.updatedDone = updatedDone;
	}
	
	protected void reset(){
		setUpdatedDone(false);
		setFixed(false);
		setFixing(false);
		setHasPred(false);
		setUpdated(false);
		setStart(false);
		setEnd(false);
		setValuated(false);
		pred = null;
		previousPred = null;
		previousValuation = Float.POSITIVE_INFINITY;
		valuation = Float.POSITIVE_INFINITY;
	}

	public boolean isStart() {
		return start;
	}

	protected void setStart(boolean start) {
		this.start = start;
	}

	public boolean isEnd() {
		return end;
	}

	protected void setEnd(boolean end) {
		this.end = end;
	}

	protected void setValuated(boolean valuated) {
		this.valuated = valuated;
	}

}
