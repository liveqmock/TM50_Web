package org.openflashchart.elements;


/**
 * The Bar graphic
 * {
		"bg_colour":	"#EEEEEE",
		"elements":	[{
		"font-size":	10,
		"text":	"My name is bar",
		"type":	"bar",
		"tip":	"Bar 1<br> val = #val#",
		"values":	[7,9,6,11,"",4],
		"bar_3d":	true,
		"colour":	"#9933CC",
		"alpha":	0.9
	}, {
		"font-size":	10,
		"text":	"My name is bar",
		"type":	"bar",
		"tip":	"Bar 2 val = #val#",
		"values":	[7,9,6,11,"",4],
		"bar_3d":	true,
		"colour":	"#00AADD",
		"alpha":	0.9
	}],

	"tooltip":{
		"title":	"{font-size: 14px; color: #CC2A43;}",
		"stroke":	5,
		"background":	"#BDB396",
		"body":	"{font-size: 10px; font-weight: bold; color: #000000;}",
		"shadow":	false
	},

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
public class Bar extends BaseBar {

	public Bar() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}
	
	public Bar(String text) {
		this();
		this.text = text;
	}

}
