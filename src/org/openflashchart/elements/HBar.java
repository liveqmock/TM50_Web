package org.openflashchart.elements;

/**
 * 
 *  The horizontal bar graphic
 * {
		"bg_colour":	"#EEEEEE",
		"elements":	[{
		"font-size":	10,
		"text":	"My name is hbar",
		"type":	"hbar",
		"values":	[{"right":5},{"right":9},{"left":13,"right":16}],
		"colour":	"#9933CC"
	}],

	"x_axis":{
		"steps":	1,
		"colour":	"#00EE00",
		"labels":	[2,3,4,5]
	},

	"y_axis":{
		"min":	-1,
		"max":	5
	}

}

 * @author zhuzhenhua
 *
 * @time Jul 30, 200810:47:24 AM
 */
public class HBar extends BaseBar {

	public HBar() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}
	
	public HBar(String text) {
		this();
		this.text = text;
	}

}
