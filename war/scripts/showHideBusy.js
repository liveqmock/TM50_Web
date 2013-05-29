function __startWaiting(element, className, timeUntilShow) {

	if (typeof element == 'string')
		element = document.getElementById(element);
	if (className == undefined)
		className = 'waiting';
	if (timeUntilShow == undefined)
		timeUntilShow = 20000;

	if(!element._waiting) element._waiting = 0;
	
	//element._waiting = true;
	element._waiting ++;

	if (element._loading) {
		if($defined(element.timeOut)) {
			
			$clear(element.timeOut);
			element._stopWaitFunction = function() { __stopWaiting(element) };
			element.timeOut = element._stopWaitFunction.delay( timeUntilShow );

			//(function() { __stopWaiting(element) }).delay( timeUntilShow );
			//window.clearTimeout(element.timeOut);
			//element.timeOut = window.setTimeout( function() { __stopWaiting(element); }, timeUntilShow );
		}
		return;
		//element._loading.dispose();
	}
		/* 2008-11-27
		if(Browser.Engine.trident4) {
			var frame = new Element('iframe',
				{
				'frameborder':'none',
				'scrolling': 'no',
				'marginWidth': 0,
				'marginHeight': 0,
				'src': ''
				}
			);

			(element.offsetParent || document.body).appendChild(element._loading_frame = frame);
			var left = element.offsetLeft, 
			top = element.offsetTop,
			width = element.scrollWidth,
			height = element.scrollHeight;

			frame.setStyle('position','absolute');
			frame.setStyle('z-index','999999');
			frame.setStyle('left',left+'px');
			frame.setStyle('top',top+'px');
			frame.setStyle('width',width+'px');
			frame.setStyle('height',height+'px');
			frame.setStyle('display','block');
			frame.setStyle('opacity', 0.01);

		} 
		*/
		
		var e = new Element('div');
		
		(element.offsetParent || document.body).appendChild(element._loading = e);
		e.style.position = 'absolute';
		e.style.zIndex = 1000000;
		e.setStyle('background-color','#fff');
		e.setStyle('opacity', 0.7 );

		/*
		try {e.style.opacity = 0.7;} catch(e) {}
		try {e.style.MozOpacity = 0.7;} catch(e) {}
		try {e.style.filter = 'alpha(opacity='+Math.round(0.7 * 100)+')';} catch(e) {}
		try {e.style.KhtmlOpacity = 0.7;} catch(e) {}
		*/
			
	//}

	

	//if (element._waiting ) {
		
	var left = element.offsetLeft, 
	top = element.offsetTop,
	width = element.scrollWidth,
	height = element.scrollHeight,
	l = element._loading;
				
	l.setStyle('left',left+'px');
	l.setStyle('top',top+'px');
	l.setStyle('width',width+'px');
	l.setStyle('height',height+'px');
	l.setStyle('display','block');

	//l.setStyle('text-align','center');
	/*
	var cnter = new Element('div', {
		'styles': {
		//'display': 'relative',
		'position': 'absolute',
		'left': (left+ (width/2) -16)+'px',
		'top': (top + (height/2) - 16)+'px'
		}
	}).inject(l);

	new Element('img',{
		'src' : 'images/bigWaiting.gif'}).inject(cnter);
		*/
	

	element._loading.className = className;


		//l.style.left = left+'px';
		//l.style.top = top+'px';
		//l.style.width = width+'px';
		//l.style.height = height+'px';
		//l.style.display = 'inline';
		
	//}
	
	//element.timeOut = window.setTimeout( function() { __stopWaiting(element); MochaUI.notification('Ʈũ ð ʰǾϴ. ٽ õϼ'); }, timeUntilShow );
	
	
	//element.timeOut = window.setTimeout( function() { __stopWaiting(element);}, timeUntilShow );
	element._stopWaitFunction = function() { __stopWaiting(element) };
	element.timeOut = element._stopWaitFunction.delay( timeUntilShow );

}
	
// Stop waiting status - hide loading element
function __stopWaiting(element) {

	
	element._waiting --;
	if (element._waiting <= 0) {

		//window.addEvent('domready', function() {
		//window.clearTimeout(element.timeOut);

		if(element.timeOut)
			$clear(element.timeOut);

		element._waiting = false;
		if(element._loading)
			$(element._loading).dispose(); //parentNode.removeChild(element._loading);
		element._loading = null;
		//});

		/*
		if(Browser.Engine.trident4) {
			element._loading_frame.dispose(); //element._loading_frame.parentNode.removeChild(element._loading_frame);
			element._loading_frame = null;
			
		}
		*/

	}
	
}/*,
	
	_zIndex: 1000000*/

function showBusy( obj ) {
	if(obj) {
		//alert('show:'+obj.id);
		__startWaiting(obj,'bigWaiting');
	} else {
		__startWaiting(document.body,'bigWaiting');
	}
}

function hideBusy( obj ) {
	if(obj) {
		//alert('hide:'+obj.id);
		__stopWaiting(obj);
	} else {
		__stopWaiting(document.body);
	}
}

