var toolTip = {

	isControl: false,

	timeUnique: 0
	,
    init : function(contentEl) {
		
		var links,i,h;

		if(!document.getElementById || !document.getElementsByTagName) 
			return;

		//if(!document.getElementById("btc")) {
			toolTip.bringToFront();
			//toolTip.createBackGround();

		//}


		links = toolTip.getElementsByTitle(contentEl);

		for(i=0;i<links.length;i++){

			toolTip.Prepare($(links[i]));
		}
    },

	bringToFront: function() {

		a = document.getElementById("btc");
		if(a) {
			document.getElementsByTagName("body")[0].removeChild(a);
		}

		toolTip.createBackGround();

	},
	
	createBackGround: function() {

			h=document.createElement("span");
			h.id="btc";
			h.setAttribute("id","btc");
			h.style.position="absolute";
			h.style.zIndex  = 1200000;

/*
		o = document.getElementById("modalOverlay");
		if(o) {
			o.appendChild( h );
			alert("append");
		}
		*/

			document.getElementsByTagName("body")[0].appendChild(h);

	},
	showTipByEvent: function(e, html, duration) {

		//if(!Browser.Engine.trident4) {
		//	MochaUI.notification(html);
		//}

		toolTip.hideTooltip();

		toolTip.isControl = true;

		tooltip = toolTip.CreateEl("span","tooltip");

		s=toolTip.CreateEl("span","top_red");

		s.innerHTML = html;

		tooltip.appendChild(s);

		b=toolTip.CreateEl("b","bottom_red");

		tooltip.appendChild(b);

		toolTip.setOpacity(tooltip);

		tooltip.onclick = toolTip.hideTooltip;
		//$(tooltip).addEvent('click', function(e) { toolTip.hideTooltip; } );

		tooltip.onmouseover = toolTip.hideTooltip;
		//$(tooltip).addEvent('mouseover', function(e) { toolTip.hideTooltip; } );

		document.getElementById("btc").appendChild(tooltip);

		toolTip.Locate(e);

		toolTip.timeUnique = window.setTimeout( function() { toolTip.hideTooltip();
			}, ((duration)? duration: 5000) ); 

		/*
		window.setTimeout( function() { 
			if(toolTip.isControl) toolTip.hideTooltip();
			}, ((duration)? duration: 5000) ); 
		*/

	}
	,
	showTipAtControl: function(el,html, duration) {
		/*
		if(Browser.Engine.trident4) {
			alert();
			return;
		}
		*/

		/*
		if(!Browser.Engine.trident4) {
			MochaUI.notification(html);
		}
		*/

		toolTip.hideTooltip();

		toolTip.isControl = true;

		tooltip = toolTip.CreateEl("span","tooltip");

		s=toolTip.CreateEl("span","top_red");

		s.innerHTML = html;

		tooltip.appendChild(s);

		b=toolTip.CreateEl("b","bottom_red");

		tooltip.appendChild(b);

		toolTip.setOpacity(tooltip);

		tooltip.onclick = toolTip.hideTooltip;
		tooltip.onmouseover = toolTip.hideTooltip;

		//$(tooltip).addEvent('click', function(e) { toolTip.hideTooltip; } );
		//$(tooltip).addEvent('mouseover', function(e) { toolTip.hideTooltip; } );

		document.getElementById("btc").appendChild(tooltip);

		toolTip.LocateForFocus(el);

		toolTip.timeUnique = window.setTimeout( function() { toolTip.hideTooltip();
			}, ((duration)? duration: 5000) ); 

		/*
		window.setTimeout( function() { 
			if(toolTip.isControl) toolTip.hideTooltip();
			}, ((duration)? duration: 5000) ); 
		*/

	}
	,
	showTipAtControlOffset: function(el,html, offset, duration) {
		/*
		if(Browser.Engine.trident4) {
			alert();
			return;
		}
		*/

		/*
		if(!Browser.Engine.trident4) {
			MochaUI.notification(html);
		}
		*/

		toolTip.hideTooltip();

		toolTip.isControl = true;

		tooltip = toolTip.CreateEl("span","tooltip");

		s=toolTip.CreateEl("span","top_red");

		s.innerHTML = html;

		tooltip.appendChild(s);

		b=toolTip.CreateEl("b","bottom_red");

		tooltip.appendChild(b);

		toolTip.setOpacity(tooltip);

		tooltip.onclick = toolTip.hideTooltip;
		tooltip.onmouseover = toolTip.hideTooltip;

		//$(tooltip).addEvent('click', function(e) { toolTip.hideTooltip; } );
		//$(tooltip).addEvent('mouseover', function(e) { toolTip.hideTooltip; } );

		document.getElementById("btc").appendChild(tooltip);

		toolTip.LocateForFocusOffset(el, offset);

		toolTip.timeUnique = window.setTimeout( function() { toolTip.hideTooltip();
			}, ((duration)? duration: 5000) ); 

		/*
		window.setTimeout( function() { 
			if(toolTip.isControl) toolTip.hideTooltip();
			}, ((duration)? duration: 5000) ); 
		*/

	}
	,
	Prepare: function(el) {

		var tooltip,t,b,s,l;

		t = el.getAttribute("title");

		el.removeAttribute("title");

		if(t.length > 40) {
			tooltip=toolTip.CreateEl("span","big_tooltip");
		} else if(t.length > 10) {
			tooltip=toolTip.CreateEl("span","tooltip");
		} else {
			tooltip=toolTip.CreateEl("span","small_tooltip");
		}


		s=toolTip.CreateEl("span","top");

		s.innerHTML = t;

		//s.appendChild(document.createTextNode(t));
		tooltip.appendChild(s);

		b=toolTip.CreateEl("b","bottom");

		//l=el.getAttribute("href");

		//if(l.length>30) l=l.substr(0,27)+"...";
		

		//b.appendChild(document.createTextNode(l));

		tooltip.appendChild(b);

		toolTip.setOpacity(tooltip);
		tooltip.onclick = toolTip.hideTooltip;
		tooltip.onmouseover = toolTip.hideTooltip;
		//$(tooltip).addEvent('click', function(e) { toolTip.hideTooltip; } );
		//$(tooltip).addEvent('mouseover', function(e) { toolTip.hideTooltip; } );

		el.tooltip=tooltip;

		//el.onmouseover = toolTip.showTooltip;
		//$(el).addEvent('mouseover', function(e) { $('mainPanel').appendText('mouseover '); } );
		
		var showFunc = function(e) { 
			toolTip.hideTooltip(e); 
			$(this.tooltip).inject( $('btc'));
			//toolTip.Locate(e);
		}
	
		el.addEvent('mouseenter', showFunc.bind(el) );
		//el.onmouseout = toolTip.hideTooltip;
		el.addEvent('mouseleave', function(e) {  toolTip.hideTooltip(e); } );
		//el.onmousemove = toolTip.Locate;
		el.addEvent('mousemove', function(e) {  toolTip.Locate(e); } );

		//el.onfocus = toolTip.showTooltipForFocus;
		var focusFunc = function(e) { 
			toolTip.hideTooltip(e);
			$(this.tooltip).inject($('btc'));
			toolTip.LocateForFocus($(this));

			//toolTip.Locate(e);
		}
		el.addEvent('focus', focusFunc.bind(el) );
		//el.onfocus = toolTip.showTooltipForFocus;
		el.addEvent('blur', function(e) { toolTip.hideTooltip(e); } );
		//el.onblur = toolTip.hideTooltip;

		//el.addEvent('mouseover', function() { toolTip.showTooltip();} );
		//el.addEvent('mouseout', function() { toolTip.hideTooltip();} );
		//el.addEvent('mousemove', function() { toolTip.Locate();} );


	},
	showTooltip: function(e){
		toolTip.hideTooltip();
		$(this.tooltip).inject( $('btc'));
		toolTip.Locate(e);
	},

	showTooltipForFocus: function(e){
		toolTip.hideTooltip();
		document.getElementById("btc").appendChild(this.tooltip);
		toolTip.LocateForFocus(this);
	},

	hideTooltip: function(e){

		toolTip.isControl = false;

		if(toolTip.timeUnique) window.clearTimeout(toolTip.timeUnique);

		var d=document.getElementById("btc");
		if(d && d.childNodes.length>0) d.removeChild(d.firstChild);
	},

	setOpacity: function(el){
		el.style.filter="alpha(opacity:95)";
		el.style.KHTMLOpacity="0.95";
		el.style.MozOpacity="0.95";
		el.style.opacity="0.95";
	},

	CreateEl: function(t,c){
		var x=document.createElement(t);
		x.className=c;
		x.style.display="block";
		return(x);
	},

	Locate: function (e){
		var posx=0,posy=0;
		/*
		if(e==null) {
			e=window.event;
		}
		

		
		if(e.pageX || e.pageY){
			posx=e.pageX; posy=e.pageY;
			}
		else if(e.clientX || e.clientY){
			if(document.documentElement.scrollTop){
				posx=e.clientX+document.documentElement.scrollLeft;
				posy=e.clientY+document.documentElement.scrollTop;
				}
			else{
				posx=e.clientX+document.body.scrollLeft;
				posy=e.clientY+document.body.scrollTop;
				}
			}
		*/
		posx = e.page.x;
		posy = e.page.y;

		//alert(document.getElementById("btc").childNodes[0].clientWidth);
		$('btc').setStyle('top',(posy+10)+"px");
		$('btc').setStyle('left',(posx-20)+"px");

		/*
		document.getElementById("btc").style.top=(posy+10)+"px";
		document.getElementById("btc").style.left=(posx-20)+"px";
		*/
	},


	LocateForFocus: function (el){
		var posx=0,posy=0;
		/*
		if(document.documentElement.scrollTop) {
			posx=toolTip.getOffset(el, "left")+document.documentElement.scrollLeft;
			posy=toolTip.getOffset(el,"top")+document.documentElement.scrollTop;
		} else {
			posx=toolTip.getOffset(el,"left")+document.body.scrollLeft;
			posy=toolTip.getOffset(el,"top")+document.body.scrollTop;
		}
		*/

		posy = $(el).getPosition().y;
		posx = $(el).getPosition().x;

		document.getElementById("btc").style.top=(posy+20)+"px";
		document.getElementById("btc").style.left=(posx)+"px";

	},

	LocateForFocusOffset: function (el, offset){
		var posx=0,posy=0;
		/*
		if(document.documentElement.scrollTop) {
			posx=toolTip.getOffset(el, "left")+document.documentElement.scrollLeft;
			posy=toolTip.getOffset(el,"top")+document.documentElement.scrollTop;
		} else {
			posx=toolTip.getOffset(el,"left")+document.body.scrollLeft;
			posy=toolTip.getOffset(el,"top")+document.body.scrollTop;
		}
		*/

		posy = $(el).getPosition().y;
		posx = $(el).getPosition().x;

		document.getElementById("btc").style.top=(posy+20+offset.x)+"px";
		document.getElementById("btc").style.left=(posx+offset.y)+"px";

	},
	
	getOffset: function (what, offsettype) {

		var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
		var parentEl=what.offsetParent;
		while (parentEl!=null){
			totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
			parentEl=parentEl.offsetParent;
		}
		return totaloffset;

	},
	assign: function(el) {

		if(el.getElementsByTagName("i").length > 0) return;

		var i1 = document.createElement('i');
		var i2 = document.createElement('i');
		var s1 = document.createElement('span');
		var s2 = document.createElement('span');
		s1.appendChild(i1);
		s1.appendChild(s2);
		while (el.firstChild) {
		  s1.appendChild(el.firstChild);
		}
		//si.style.filter = 'alpha(opacity='+Math.round(0.8 * 100)+')';
		el.appendChild(s1);
		el = el.insertBefore(i2, s1);
		el.assignEffect = true;

	},
    findForm : function(f) {
        while(f.tagName != "FORM") {
            f = f.parentNode;
        }
        return f;
    },
    getElementsByTitle : function(contentEl) {


        var elm = (contentEl)? contentEl: document;

		var elements = (elm.all)? elm.all : elm.getElementsByTagName("*");

        var returnElements = [];
        var current;
        var length = elements.length;

		for(var i=0; i<length; i++){
            current = elements[i];
			try
			{
				//if(elements[i].tooltip) continue;

				if(!elements[i].getAttribute) continue;

				t = elements[i].getAttribute("title");
				if(!t || t==null || t.length==0) 
					continue;

				returnElements.push(current);
			}
			catch(exception){
			}
			finally 
			{
			}
        }
        return returnElements;
    }
}

//jsBtn.addEvent(window,'load', function() { jsBtn.init();} );
//window.addEvent('load', function() { jsBtn.init();} );
//window.addEvent('domready', function() {  window.setTimeout( function() {jsBtn.init();}, 100 ); } );

