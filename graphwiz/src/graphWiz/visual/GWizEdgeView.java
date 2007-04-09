/*
 * @(#)EdgeView.java	1.0 03-JUL-04
 * 
 * Copyright (c) 2001-2004 Gaudenz Alder
 *  
 */
package graphWiz.visual;

import graphWiz.GWizModelAdapter;
import graphWiz.model.GWizEdge;
import graphWiz.model.GWizEdge.Description;

import java.awt.Color;

import org.jgraph.JGraph;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;

/**
 * The default implementation of an edge view. The getGWizEdgeRenderer method assumes a renderer of type GWizEdgeRenderer. If you provide a custom renderer to a subclass, you must also override the methods that call this method, namely: getShape, getLabelBounds, getExtraLabelBounds, intersects and getBounds.
 * @version  1.0 1/1/02
 * @author  Gaudenz Alder
 */

public class GWizEdgeView extends EdgeView {
	
	EdgeView predStroke;
	
	JGraph graph;
	
	boolean updated =false ;

	private GWizModelAdapter jgAdapter;
	
	/** Renderer for the class. */
	public static transient GWizEdgeRenderer renderer;

	/**
	 * Constructs an empty edge view.
	 */
	public GWizEdgeView(Object cell, GWizModelAdapter jgAdapter) {
		super(cell);
		this.jgAdapter = jgAdapter;
		renderer = new GWizEdgeRenderer();
		
	}

	//
	// Data Source
	//


	/**
	 * Returns a renderer for the class.
	 * @uml.property  name="renderer"
	 */
	public CellViewRenderer getRenderer() {
		return renderer;
	}
	
	private GWizEdge getModel() {
		return jgAdapter.getCellEdge((DefaultEdge) getCell());
	}
	
	Color getColor(){
		if (getModel()==null) return Color.BLACK;
		if (getModel().getDescription()==Description.EXPLORED )
			return Color.LIGHT_GRAY;
		if (getModel().getDescription()==Description.PATH)
			return Color.GREEN;
		if (getModel().getDescription()==Description.SELECT)
			return Color.RED;
		if (getModel().getDescription()==Description.REGULAR )
			return Color.CYAN.darker();
		if (getModel().getDescription()==Description.EXPLORER )
			return Color.RED;
		else return Color.BLACK;
	}
	
	void paintPred(){
		if (getModel()!=null && !updated){
		//	if (jgAdapter.getGWizGraph().getEdgeSource(getModel()) == jgAdapter.getGWizGraph().getEdgeTarget(getModel()).getPred()){
				GraphConstants.setLineBegin(getAllAttributes(), GraphConstants.ARROW_CIRCLE);
		//	}
		jgAdapter.cellsChanged(new Object[] { getCell()});
		updated = true;
		}
		else
			updated = false;
	}
	
	String getWeight() {
		double weight = jgAdapter.getGWizGraph().getEdgeWeight(getModel());
		if (weight == (int) weight)
			return Integer.toString((int) weight);
		else return Double.toString(weight);
	}
	
	
}