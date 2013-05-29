package org.openflashchart.elements;

/**
 * The Filled bar graphic
 * {
 "bg_colour":	"#FFFFFF",
 "elements":	[{
 "font-size":	10,
 "text":	"My name is Filled_Bar",
 "type":	"filled_bar",
 "values":	[9,8,7,6,5,4,3,2,1],
 "colour":	"#E2D66A",
 "outline-colour":	"#577261",
 "alpha":	0.8
 }]

 }
 * 
 * @author zhuzhenhua
 * 
 * @time Aug 1, 20083:58:44 PM
 */
public class Filled_Bar extends BaseBar {

	/** teh out line's colour*/
	private String outline__colour;

	public Filled_Bar() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}

	public Filled_Bar(String text) {
		this();
		this.text = text;
	}

	/**
	 * @return the outLine_colour
	 */
	public String getOutline__colour() {
		return outline__colour;
	}

	/**
	 * @param outLine_colour
	 *            the outLine_colour to set
	 */
	public void setOutline__colour(String outline__colour) {
		this.outline__colour = outline__colour;
	}

}
