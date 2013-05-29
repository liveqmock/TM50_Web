/*
  popupmenu.js - simple JavaScript popup menu library.

  Copyright (C) 2008 Jiro Nishiguchi <jiro@cpan.org> All rights reserved.
  This is free software with ABSOLUTELY NO WARRANTY.

  You can redistribute it and/or modify it under the modified BSD license.

  Usage:
    var popup = new PopupMenu();
    popup.add(menuText, function(target){ ... });
    popup.addSeparator();
    popup.bind('targetElement');
    popup.bind(); // target is document;
*/
var PopupMenu = function( windowId ) {
    this.init( windowId );
}
PopupMenu.SEPARATOR = 'PopupMenu.SEPARATOR';
PopupMenu.current = null;
PopupMenu.addEventListener = function(element, name, observer, capture) {
    if (typeof element == 'string') {
        element = document.getElementById(element);
    }
    if (element.addEventListener) {
        element.addEventListener(name, observer, capture);
    } else if (element.attachEvent) {
        element.attachEvent('on' + name, observer);
    }
};
PopupMenu.prototype = {
    init: function( windowId ) {
        this.items  = [];
        this.width  = 0;
        this.height = 0;
		this.zIndex = 0;
        var self = this;

		if(windowId && MochaUI )
			MochaUI.Windows.instances.get(windowId).addEvent('onClose',function() { self.free() } );


    },
    setSize: function(width, height) {
        this.width  = width;
        this.height = height;
        if (this.element) {
            var self = this;
            with (this.element.style) {
                if (self.width)  width  = self.width  + 'px';
                if (self.height) height = self.height + 'px';
            }
        }

    },
    bind: function(element) {

        var self = this;
        if (!element) {
            element = document;
        } else if (typeof element == 'string') {
            element = document.getElementById(element);
        }
        this.target = $(element);
		/*
        this.target.oncontextmenu = function(e) {
            self.show.call(self, e);
            return false;
        };
		*/

		/*
        this.target.addEvent('click',function(e) {
            self.show.call(self, e);
			new Event(e).stop();
			//e.stop();
            return false;
        });
		*/

        this.target.oncontextmenu = function(e) {
            self.show.call(self, e);
            return false;
        };
		
		/*
		var tmp = this.target;
		while ( true  )
		{
			tmp = tmp.getParent();
			if(tmp == null) break;

			if (tmp.getStyle('z-index') > 0)
			{	
				this.zIndex = tmp.getStyle('z-index');
				break;
			}
			
		}
		if(!this.zIndex) {
			this.zIndex = 2009;
		}
		this.zIndex = 2009; */

        var listener = function() { self.hide.call(self) };
        PopupMenu.addEventListener(document, 'click', listener, true);
    },
	bindCell: function( cells, zIndex ) {
		//var testClass = new RegExp("/|INPUT|SELECT|A|/");
		//var testClass = "/|INPUT|SELECT|A|/";
        var self = this;
		this.zIndex = zIndex;

		for(i=0; i < cells.length; i ++ ) {
			cell = cells[i];
			found = false;

			if(cell.getElements("a").length > 0 || 
			   cell.getElements("input").length > 0) {
				found = true;
				cell.setStyle('cursor','default');
			}

			/*
			for(j=0; j < cell.childNodes.length; j ++ ) {
				if(testClass.test(cell.childNodes[j].tagName)) {
					found = true;
					break;
				}
			}
			*/
			
			if(!found) {
				//if( (cellIndex != null && cellIndex == i) || cellIndex == null  ) {
					cell.setStyle('cursor','pointer');
					PopupMenu.addEventListener(cell,'click', function(e){
								self.show.call(self, e);
								//new Event(e).stop();
								return false;
							});			
				//}
			}
		}
        var listener = function(e) { self.hide.call(self) };
        PopupMenu.addEventListener(document, 'click', listener, true);


	},
	bindCellOneCell: function( trs, zIndex, cellIndex ) {

		var self = this;
		this.zIndex = zIndex;

		for(i=0; i < trs.length; i ++ ) {
			cell = trs[i].getElements('td')[cellIndex];
			found = false;

			if(cell.getElements("a").length > 0 || 
			   cell.getElements("input").length > 0) {
				found = true;
				cell.setStyle('cursor','default');
			}

		
			if(!found) {
					cell.setStyle('cursor','pointer');
					PopupMenu.addEventListener(cell,'click', function(e){
								self.show.call(self, e);
								//new Event(e).stop();
								return false;
							});			
			}
		}
        var listener = function(e) { self.hide.call(self) };
        PopupMenu.addEventListener(document, 'click', listener, true);


	},
    add: function(text, callback, disabled) {
        this.items.push({ 'text': text, 'callback': callback, 'disabled': disabled });
    },
    addSeparator: function() {
        this.items.push(PopupMenu.SEPARATOR);
    },
    setPos: function(e) {
		
        if (!this.element) return;
        if (!e) e = window.event;
        var x, y;
        if (window.opera) {
            x = e.clientX;
            y = e.clientY;
        } else if (document.all) {
            x = document.body.scrollLeft + event.clientX;
            y = document.body.scrollTop + event.clientY;
        } else if (document.layers || document.getElementById) {
            x = e.pageX;
            y = e.pageY;
        }
		//alert(x+" "+y);
        this.element.style.top  = y + 'px';
        this.element.style.left = x + 'px';
    },
    show: function(e) {
        if (PopupMenu.current && PopupMenu.current != this) return;
        PopupMenu.current = this;
		//if (this.element) this.element.destroy();
		if (this.element) this.element.style.display = 'none';

		if( this.onShow ) this.onShow();
        /*if (this.element) {
            this.setPos(e);
            this.element.style.display = '';
        } else { */
            this.element = this.createMenu(this.items);

			//this.element.style.zIndex = this.zIndex + 2;
			this.element.style.zIndex = 2000;

            this.setPos(e);

            this.element.inject(document.body);
			this.element.getFirst().dropShadow({fade:true});

			//newEl.dropShadow();
			//alert(newEl);

			//applyDropShadows( newEl, "shadow1" );
        //}
    },
    hide: function() {
        PopupMenu.current = null;
        //if (this.element) this.element.destroy();
        if (this.element) this.element.style.display = 'none';
    },
    createMenu: function(items) {
        var self = this;
        var menu = new Element('div',{
							'styles': {
								'border'     : '1px solid gray',
								'background' : '#FFFFFF',
								'color'      : '#000000',
								'position'   : 'absolute',
								'display'    : 'block',
								'padding'    : '2px',
								'cursor'     : 'default',
								'width' :  self.width  + 'px',
								'height' : 'auto',
								'left' : '0px',
								'top' : '0px'}
					});
		
        with (menu.style) {
            //if (self.width)  width  = self.width  + 'px';
            //if (self.height) height = self.height + 'px';
			/*
            border     = "1px solid gray";
            background = '#FFFFFF';
            color      = '#000000';
            position   = 'absolute';
            display    = 'block';
            padding    = '2px';
            cursor     = 'default';
			left = '0px';
			top = '0px';
			*/
			//zIndex	   = 500;
        }
		
        for (var i = 0; i < items.length; i++) {
            var item;
            if (items[i] == PopupMenu.SEPARATOR) {
                item = this.createSeparator();
            } else {
                item = this.createItem(items[i]);
            }
            menu.appendChild(item);
		}
		//var c = menu.getCoordinates();
		//alert(menu.getStyle('width'));
		//alert(menu.getStyle('height'));

			var wrap = new Element('div',
			{
				'styles':{
					'position' : 'absolute',
					'width' : self.width + 5,
					'height' : 'auto'
				}
			});

			//var wrap2 = document.createElement('div');
			//var wrap3 = document.createElement('div');

			//wrap1.className = "shadow2";

			//wrap3.appendChild(menu);
			//wrap2.appendChild(wrap3);
			menu.inject(wrap);

			//menu.dropShadow();
			

		return wrap;
		//menu.dropShadow();

        //return menu;
    },
    createItem: function(item) {
        var self = this;
        var elem = new Element('div');
        elem.style.padding = '4px';
        var callback = item.callback;
		
		if( !item.disabled ) {

			elem.addEvent('click',function(e) {
					self.hide();
					callback(self.target, e);
			});
			

			/*PopupMenu.addEventListener(elem, 'click', function(_callback) {
				return function() {
					self.hide();
					_callback(self.target);
				};
			}(callback), true);*/

			PopupMenu.addEventListener(elem, 'mouseover', function(e) {
				elem.style.background = '#B6BDD2';
			}, true);
			PopupMenu.addEventListener(elem, 'mouseout', function(e) {
				elem.style.background = '#FFFFFF';
			}, true);

		} else {

			elem.setStyle('color','#C6C6C6');

		}

        elem.appendChild(document.createTextNode(item.text));
        return elem;
    },
	disable: function() {
        for (var i = 0; i < this.items.length; i++) {
			this.items[i].disabled = true;
		}
		
	},
	enable: function() {
        for (var i = 0; i < this.items.length; i++) {
			this.items[i].disabled = false;
		}
		
	},
    createSeparator: function() {
        var sep = document.createElement('div');
        with (sep.style) {
            borderTop = '1px dotted #CCCCCC';
            fontSize  = '0px';
            height    = '0px';
        }
        return sep;
    },
	free: function() {
        PopupMenu.current = null;
        if (this.element) {
			if(this.element.destroy) { 
				this.element.destroy();
			} else {
				p = (this.element.parentNode || this.element.parentElement);
				p.removeChild( this.element );
			}
		}
	}
};

