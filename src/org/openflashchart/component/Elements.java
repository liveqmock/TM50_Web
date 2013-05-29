package org.openflashchart.component;

import java.util.ArrayList;
import java.util.List;

import org.openflashchart.chart.BaseGraphProperty;
import org.openflashchart.elements.Element;
import org.openflashchart.json.JsonHelper;

/**
 * 
 * The component of graphics
 * @author zhuzhenhua
 * 
 */
public class Elements extends BaseGraphProperty {

	/** all basic component collections */
	private List<Element> elements = new ArrayList<Element>();

	/**
	 * @return the elements
	 */
	public List<Element> getElements() {
		return elements;
	}

	/**
	 * @param e
	 *            the e to set
	 */
	public void addElement(Element e) {
		this.elements.add(e);
	}

	public String toString() {
		return JsonHelper.getJsonString(this);
	}

	/**
	 * Decide elements is contains paramter element
	 * @param element the component of graphic
	 * @return boolean the elements is contains paramter element
	 */
	public boolean containsElement(Element element) {
		return this.elements.contains(element);
	}

}
