package org.openflashchart.elements;

/**
 * The Bar sketch graphic
 {
		"bg_colour":	"#EEEEEE",
		"elements":	[{
		"font-size":	10,
		"text":	"My name is Bar_Sketch",
		"type":	"bar_sketch",
		"values":	[7,9,6,10,0,4],
		"colour":	"#3333CC"
	}],

	"x_axis":{
		"steps":	1,
		"colour":	"#00EE00",
		"labels":	[2,3,4,5]
	},

	"y_axis":{
		"min":	-1,
		"max":	15
	}

}

 * 
 * @author zhuzhenhua
 * 
 * @time Jul 30, 200810:47:24 AM
 */
public class Bar_Sketch extends BaseBar {

	public Bar_Sketch() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}

	public Bar_Sketch(String text) {
		this();
		this.text = text;
	}

}
