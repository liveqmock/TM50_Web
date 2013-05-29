package org.openflashchart.elements;

/**
 * 
 * {
		"bg_colour":	"#EEEEEE",
		"elements":	[{
		"font-size":	10,
		"text":	"My name is Bar_Fade",
		"type":	"bar_fade",
		"values":	[7,9,6,11,8,4],
		"bar_3d":	true,
		"colour":	"#9933CC",
		"alpha":	0.9
	}],

	"x_axis":{
		"steps":	1,
		"colour":	"#00EE00",
		"labels":	[2,3,4,5]
	},

	"y_axis":{
		"max":	15
	}

}

 * @author zhuzhenhua
 *
 * @time Aug 4, 20081:01:04 PM
 */
public class Bar_Fade extends BaseBar {

	public Bar_Fade() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}
	
	public Bar_Fade(String text) {
		this();
		this.text = text;
	}

}
