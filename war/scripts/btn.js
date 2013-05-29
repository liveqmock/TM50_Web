var jsBtn = {
    init : function( el ) {
        if (!document.getElementById || !document.createElement || !document.appendChild) return false;
        as = jsBtn.getElementsByClassName('btn(.*)',"A", el);
        for (i=0; i<as.length; i++) {
			
			if(as[i].isRender) continue;
			as[i].isRender = true;

			csName = as[i].className;


			as[i] = $(as[i]); 
			x = as[i].getSize().x;
			t = as[i].get('text');
			/*
			x = as[i].getSize().x;
			t = as[i].get('text');
			as[i].set('text','');
			d = new Element('span', { 'text' : t , 'styles' : {'width' : x+'px'} } ).inject(as[i]);

			*/

            if ( as[i].tagName == "INPUT" && ( as[i].type.toLowerCase() == "submit" || as[i].type.toLowerCase() == "button" ) ) {
                var a1 = new Element("a");
				//a1.setStyle('width',$(as[i]).getSize().x+'px');
                a1.appendChild(document.createTextNode(as[i].value));
                a1.className = as[i].className;
                a1.id = as[i].id;
                as[i] = as[i].parentNode.replaceChild(a1, as[i]);
                as[i] = a1;
                as[i].style.cursor = "pointer";
            }
            else if (as[i].tagName == "A") {
                var tt = as[i].childNodes;
            }
            else { return false };
            var i1 = new Element('i');
            var i2 = new Element('i');
            var s1 = new Element('span');
            var s2 = new Element('span');

		
			//s2.setStyle('width',($(as[i]).getSize().x+9)+'px');

            s1.appendChild(i1);
            s1.appendChild(s2);


            while (as[i].firstChild) {
              s1.appendChild(as[i].firstChild);
            }

			//si.style.filter = 'alpha(opacity='+Math.round(0.8 * 100)+')';
            as[i].appendChild(s1);
            as[i] = as[i].insertBefore(i2, s1);
			as[i].assignEffect = true;
			

	
			if(csName.test('btn_sm')) {
				//s2.setStyle('width',(x+2)+'px');
				//s1.setStyle('width',(x-4)+'px');
				if(t.length <= 3) {
					s2.setStyle('width',(s2.getSize().x-5)+'px');
					s1.setStyle('width',(s2.getSize().x-10)+'px');
				} else {
					s2.setStyle('width',(s2.getSize().x+6)+'px');
					s1.setStyle('width',(s2.getSize().x-9)+'px');
				}
			} else {
				s1.setStyle('width',(x-10)+'px');
			}
			
		

			//alert(s1.getSize().x);
			//$(as[i]).setStyle('width',($(as[i]).getSize().x)+'px');

			
			
			//s2.setStyle('width',(s1.getSize().x+8)+'px');

        }
    },
	assign: function(el) {

		if(el.isRender) return;
		el.isRender = true;

		csName = el.className;

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

			if(csName.test('btn_sm')) {
				s2.setStyle('width',(s2.getSize().x+6)+'px');
				s1.setStyle('width',(s2.getSize().x-9)+'px');
			} else {
				s1.setStyle('width',(s1.getSize().x-10)+'px');
			}


	},
    findForm : function(f) {
        while(f.tagName != "FORM") {
            f = f.parentNode;
        }
        return f;
    },
    getElementsByClassName : function(className, tag, elm) {
        var testClass = new RegExp("(^|\s)" + className + "(\s|$)");
		//alert(testClass);
        var tag = tag || "*";
        var elm = elm || document;
        var elements = (tag == "*" && elm.all)? elm.all : elm.getElementsByTagName(tag);
        var returnElements = [];
        var current;
        var length = elements.length;
        for(var i=0; i<length; i++){
            current = elements[i];
            if(testClass.test(current.className)){
                returnElements.push(current);
            }
        }
        return returnElements;
    }
}

//jsBtn.addEvent(window,'load', function() { jsBtn.init();} );
window.addEvent('load', function() { jsBtn.init();} );
//window.addEvent('domready', function() {  window.setTimeout( function() {jsBtn.init();}, 100 ); } );

