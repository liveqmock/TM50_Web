package org.openflashchart.elements;

/**
 * {
 "elements":	[{
 "dot-size":	3,
 "text":	"My name is Area_Hollow",
 "width":	2,
 "type":	"area_hollow",
 "values":	[0.0,0.37,0.73,1.07,1.36,1.59,1.77,1.87,1.89,1.85,1.72,1.53,1.28,0.97,0.63,0.26,-0.11,-0.48,-0.84,-1.16,-1.43,-1.65,-1.8,-1.88,-1.89,-1.82,-1.67,-1.46,-1.19,-0.88],
 "colour":	"#FF",
 "fill":	"#DD3366",
 "fill-alpha":	0.6
 }],

 "x_axis":{
 "colour":	"#FF0000",
 "max":	30,
 "labels":	["0.00","0.38","0.74","1.07","1.36","1.60","1.77","1.87","1.90","1.85","1.73","1.54","1.28","0.98","0.64","0.27","-0.11","-0.49","-0.84","-1.16","-1.44","-1.66","-1.81","-1.89","-1.89","-1.82","-1.68","-1.47","-1.20","-0.88"]
 },

 "y_axis":{
 "min":	-2,
 "colour":	"#00FF00",
 "max":	2
 }

 }

 * 
 * @author zhuzhenhua
 * 
 * @time Jul 30, 200810:47:24 AM
 */
public class Area_Hollow extends Element {

	/** the  fill the colour */
	private String fill;

	/** the  alpha of filled the colour */
	private Double fill__alpha;

	/** the font's size*/
	private Integer font__size;

	/** the dot's size*/
	private Integer dot__size;

	/** the line's width*/
	private Integer width;

	/** the halo's size*/
	private Integer halo__size;

	/**
	 * @return the halo__size
	 */
	public Integer getHalo__size() {
		return halo__size;
	}

	/**
	 * @param halo__size the halo__size to set
	 */
	public void setHalo__size(Integer halo__size) {
		this.halo__size = halo__size;
	}

	public Area_Hollow() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}

	public Area_Hollow(String text) {
		this();
		this.text = text;
	}

	/**
	 * @return the dot__size
	 */
	public Integer getDot__size() {
		return dot__size;
	}

	/**
	 * @param dot__size
	 *            the dot__size to set
	 */
	public void setDot__size(Integer dot__size) {
		this.dot__size = dot__size;
	}

	/**
	 * @return the fill
	 */
	public String getFill() {
		return fill;
	}

	/**
	 * @param fill
	 *            the fill to set
	 */
	public void setFill(String fill) {
		this.fill = fill;
	}

	/**
	 * @return the fill__alpha
	 */
	public Double getFill__alpha() {
		return fill__alpha;
	}

	/**
	 * @param fill__alpha
	 *            the fill__alpha to set
	 */
	public void setFill__alpha(Double fill__alpha) {
		this.fill__alpha = fill__alpha;
	}

	/**
	 * @return the font__size
	 */
	public Integer getFont__size() {
		return font__size;
	}

	/**
	 * @param font__size
	 *            the font__size to set
	 */
	public void setFont__size(Integer font__size) {
		this.font__size = font__size;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

}
