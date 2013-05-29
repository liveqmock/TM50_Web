package org.openflashchart.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * ��ͼThe pie graphic 
 * {
		"bg_colour":	"#66DD33",
		"elements":	[{
		"animate":	true,
		"colours":	["#225599","#995522","#3399cc","#cc9933","#551188"],
		"type":	"pie",
		"tip":	"hello",
		"values":	[21,29,15,10,13,12],
		"border":	6,
		"alpha":	0.6
	}],

	"title":{
		"text":	"My name is Title",
		"style":	"{colour:#DDDDDD;font-size: 15px;}"
	}

}

 * 
 * @author zhuzhenhua
 * 
 * @time Jul 30, 200810:47:24 AM
 */
public class Pie extends Element {

	private Double alpha;

	private Integer stroke;

	private Boolean animate;

	@SuppressWarnings("unchecked")	
	private List colours;

	private Double start__angle;

	private Double border;
	
	private Boolean no__labels;

	
	public Boolean getNo__labels() {
		return no__labels;
	}

	public void setNo__labels(Boolean no__labels) {
		this.no__labels = no__labels;
	}

	public Pie(){
		this.type = this.getClass().getSimpleName().toLowerCase();
	}
	
	public Pie(String text){
		this();
		this.text = text;
	}
	
	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @param animate
	 *            the animate to set
	 */
	public void setAnimate(Boolean animate) {
		this.animate = animate;
	}

	/**
	 * @param border
	 *            the border to set
	 */
	public void setBorder(Double border) {
		this.border = border;
	}

	/**
	 * @param colours
	 *            the colours to set
	 */
	@SuppressWarnings("unchecked")	
	public void setColours(List colours) {
		this.colours = colours;
	}
	
	@SuppressWarnings("unchecked")
	public void setColours(Object[] objArray){
		if(null == this.colours){
			this.colours = new ArrayList();
		}
		for(Object obj : objArray){
				this.colours.add(obj);
		}
	}

	/**
	 * @param start__angle
	 *            the start__angle to set
	 */
	public void setStart__angle(Double start__angle) {
		this.start__angle = start__angle;
	}

	/**
	 * @param stroke
	 *            the stroke to set
	 */
	public void setStroke(Integer stroke) {
		this.stroke = stroke;
	}

	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return alpha;
	}

	/**
	 * @return the animate
	 */
	public Boolean getAnimate() {
		return animate;
	}

	/**
	 * @return the border
	 */
	public Double getBorder() {
		return border;
	}

	/**
	 * @return the colours
	 */
	@SuppressWarnings("unchecked")	
	public List getColours() {
		return colours;
	}

	/**
	 * @return the start__angle
	 */
	public Double getStart__angle() {
		return start__angle;
	}

	/**
	 * @return the stroke
	 */
	public Integer getStroke() {
		return stroke;
	}

}
