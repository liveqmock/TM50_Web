package org.openflashchart.component;

import org.openflashchart.chart.BaseGraphProperty;

/**
 * @author zhuzhenhua
 *
 * @time Aug 5, 20081:08:57 PM
 */
public class Title extends BaseGraphProperty {

	/** the Title's text*/
	private String text;

	/** the Title's style*/
	private String style;

	public Title() {
	}

	public Title(String text, String style) {
		this.text = text;
		this.style = style;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}
}
