package org.openflashchart.elements;

/**
 * 
 * The Bar stack graphic
{
		"bg_colour":	"#EEEEEE",
		"elements":	[{
		"font-size":	10,
		"text":	"My name is Bar_Stack",
		"type":	"bar_stack",
		"values":	[[2.5,5],[7.5],[5,{"val":5,"colour":"#ff0000"}],[2,2,2,2,{"val":2,"colour":"#ff00ff"}]],
		"colour":	"#3333CC"
	}],

	"x_axis":{
		"colour":	"#00EE00",
		"labels":	[2,3,4,5]
	},

	"y_axis":{
		"min":	0,
		"max":	15
	}

}
 * 
 * @author zhuzhenhua
 * 
 * @time Aug 1, 20082:11:02 PM
 */
public class Bar_Stack extends BaseBar {

	public Bar_Stack() {
		this.type = this.getClass().getSimpleName().toLowerCase();
	}
	
	public Bar_Stack(String text) {
		this();
		this.text = text;
	}
}
