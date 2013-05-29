package org.openflashchart.elements;

/**
 * The bar glass graphic
 * {
		"bg_colour":	"#EEEEEE",
		"elements":	[{
		"font-size":	10,
		"text":	"My name is Bar_Glass",
		"type":	"bar_glass",
		"values":	[7,9,6,10,2,4],
		"colour":	"#3333CC"
	}],

	"x_axis":{
		"colour":	"#00EE00"
	},

	"y_axis":{
		"max":	15
	}

}
 * 
 * @author zhuzhenhua
 * 
 * @time Jul 30, 200810:47:24 AM
 */
public class Bar_Glass extends BaseBar {

	public Bar_Glass() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}

	public Bar_Glass(String text) {
		this();
		this.text = text;
	}
}
