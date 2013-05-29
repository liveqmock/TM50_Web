package org.openflashchart.component;

import org.openflashchart.chart.BaseGraphProperty;

/**
 *  The legend of Axis
 * @author zhuzhenhua
 * 
 * @time Aug 5, 20081:03:30 PM
 */
public abstract class BaseLegend extends BaseGraphProperty {

	/** the Legend's text */
	protected String text;

	/** the Legend's css style */
	private String style;

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
