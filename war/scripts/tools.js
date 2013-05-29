function refreshWindow( windowId ) {

	
		var win = MochaUI.Windows.instances.get(windowId);
		var options = $extend({},win.updateConfig);
		
		var oldOnClose;
		var oldOnSuccess;
		var oldonContentLoaded;

		//if((!options.type || !options.type.test("modal")) && Browser.Engine.trident4) {
		
		if((!options.type || !options.type.test("modal")) ) {
			if(!options.noOtherClose) {
				MochaUI.closeAll();
				MochaUI.garbageCleanUp();
			}
		}
		

		//options.async = false;

		if(Browser.Engine.trident4 ) {
			//if (options.update)	options.update.empty();
			options.async = false;
		}

		/*
		if(options.busyEl) {
			if(typeof(options.busyEl) == 'string') {
				if(getWindowEl(options.busyEl)) 
					showBusy(getWindowEl(options.busyEl).contentBorderEl);
				else
					showBusy($(options.busyEl));
			} else {
				showBusy(options.busyEl);
			}
		}
		*/

		if(!options.draggable) options.draggable = true;

		if(!options.container) options.container = "pageWrapper";


		if(options.onClose) 
			oldOnClose = options.onClose;

		options.onClose = function() {

			if(oldOnClose) 
				oldOnClose();

			
			
			if(toolTip) 
				toolTip.hideTooltip();
			

			if($('minical'))
				$('minical').setStyle('display','none');
			
			//try {eval(options.id+"popup").free();} catch(e) {}

		}

		if(options.onContentLoaded) 
			oldonContentLoaded = options.onContentLoaded;

		options.onContentLoaded = function() {

			if(MochaUI.Windows.instances.get(options.id)) {
				contentEl = MochaUI.Windows.instances.get(options.id).contentEl;
				busyEl = MochaUI.Windows.instances.get(options.id).contentBorderEl;
			} else {
				contentEl = $(document.body);
				busyEl = $(document.body);
			}


			if(oldonContentLoaded) 
				oldonContentLoaded();

			//initWeb20Buttons(contentEl);

			//if(jsBtn)	
			//	jsBtn.init(contentEl);

		    //if(toolTip)	
			//	toolTip.init(contentEl);
		    
			//renderTableHeader( contentEl );
			

			// textarea bug fix
			//if(Browser.Engine.trident) {
			//	contentEl.getElements('textarea').each( function( area ) {
			//		area.value = area.value;
			//	});
			//}



			//if(options.tbodyId) randerFromEl( $(options.tbodyId) , 'tbl_tr' );
			/*
			if(options.busyEl) {
				if(typeof(options.busyEl) == 'string') {
					if(getWindowEl(options.busyEl)) 
						hideBusy(getWindowEl(options.busyEl).contentBorderEl);
					else
						hideBusy($(options.busyEl));
				} else {
					hideBusy(options.busyEl);
				}
			}
			*/


		}
		var xhr = false;
		var contentURL = '';
		
		if(options.loadMethod == 'xhr') {

			xhr = true;
			options.loadMethod = 'html';
			contentURL = options.contentURL;
			options.contentURL = '';
			options.content = '';
		}

		
		if(xhr) {
			window.addEvent('domready', function() {
				
				nemoRequest.init( {
					busyWindowId: win.contentBorderEl,
					updateWindowId: win.contentBorderEl,
					url: contentURL,
					update: win.contentEl,
					onSuccess: options.onContentLoaded
				});
				nemoRequest.post( options.form );

			});
		}

		return win;


}


function nemoWindow ( options ) {

		var config = $extend({},options);

		var oldOnClose;
		var oldOnSuccess;
		var oldonContentLoaded;

		//if((!options.type || !options.type.test("modal")) && Browser.Engine.trident4) {
		
		if((!options.type || !options.type.test("modal")) ) {
			if(!options.noOtherClose) {
				MochaUI.closeAll();
				MochaUI.garbageCleanUp();
			}
		}
		

		//options.async = false;

		if(Browser.Engine.trident4 ) {
			//if (options.update)	options.update.empty();
			options.async = false;
		}

		/*
		if(options.busyEl) {
			if(typeof(options.busyEl) == 'string') {
				if(getWindowEl(options.busyEl)) 
					showBusy(getWindowEl(options.busyEl).contentBorderEl);
				else
					showBusy($(options.busyEl));
			} else {
				showBusy(options.busyEl);
			}
		}
		*/

		if(!options.draggable) options.draggable = true;

		if(!options.container) options.container = "pageWrapper";


		if(options.onClose) 
			oldOnClose = options.onClose;

		options.onClose = function() {

			if(oldOnClose) 
				oldOnClose();

			
			
			if(toolTip) 
				toolTip.hideTooltip();
			

			if($('minical'))
				$('minical').setStyle('display','none');
			
			//try {eval(options.id+"popup").free();} catch(e) {}

		}

		if(options.onContentLoaded) 
			oldonContentLoaded = options.onContentLoaded;

		options.onContentLoaded = function() {

			if(MochaUI.Windows.instances.get(options.id)) {
				contentEl = MochaUI.Windows.instances.get(options.id).contentEl;
				busyEl = MochaUI.Windows.instances.get(options.id).contentBorderEl;
			} else {
				contentEl = $(document.body);
				busyEl = $(document.body);
			}


			if(oldonContentLoaded) 
				oldonContentLoaded();

			//initWeb20Buttons(contentEl);

			//if(jsBtn)	
			//	jsBtn.init(contentEl);

		    //if(toolTip)	
			//	toolTip.init(contentEl);
		    
			//renderTableHeader( contentEl );
			

			// textarea bug fix
			//if(Browser.Engine.trident) {
			//	contentEl.getElements('textarea').each( function( area ) {
			//		area.value = area.value;
			//	});
			//}



			//if(options.tbodyId) randerFromEl( $(options.tbodyId) , 'tbl_tr' );
			/*
			if(options.busyEl) {
				if(typeof(options.busyEl) == 'string') {
					if(getWindowEl(options.busyEl)) 
						hideBusy(getWindowEl(options.busyEl).contentBorderEl);
					else
						hideBusy($(options.busyEl));
				} else {
					hideBusy(options.busyEl);
				}
			}
			*/


		}
		
		var xhr = false;
		var contentURL = '';
		if(options.loadMethod == 'xhr') {

			xhr = true;
			options.loadMethod = 'html';
			contentURL = options.contentURL;
			options.contentURL = '';
			options.content = '';
		}

		var newWin = new MochaUI.Window( options );
		newWin.updateConfig = $extend({},config);
		
		if(xhr) {
			window.addEvent('domready', function() {

				nemoRequest.init( {
					busyWindowId: newWin.contentBorderEl,
					updateWindowId: newWin.contentBorderEl,
					url: contentURL,
					update: newWin.contentEl,
					onSuccess: options.onContentLoaded
				});
				nemoRequest.post( options.form );

			});
		}

		return newWin;


}
/* 윈도우의 크기에 따라 컨텐츠의 높이가 변한다 */

function dynamicResizeHeight( elements, windowId, offsets ) {

	var instance = MochaUI.Windows.instances.get(windowId);

	var resizeFunc = function() {
		
		var winEl = MochaUI.Windows.instances.get(windowId).contentWrapperEl;

		for(var i=0; i < elements.length; i ++ ) {
			y = winEl.getStyle('height').toInt() - offsets[i];
			elements[i].setStyle('height',y + 'px');
		}
		
	}

	instance.addEvent('onResize',resizeFunc);
	resizeFunc();

}
/*

var nemoRequest = {
	count: -1,
	req: [],
	init: function( options ) {

		var busyEl = null;
		var updateEl = null;

		if(Browser.Engine.trident ) {
			//if (options.update)	options.update.empty();
		}
			//options.async = false;


		if(options.busyWindowId) {
			if(typeof(options.busyWindowId ) == 'string') 
				busyEl = MochaUI.Windows.instances.get(options.busyWindowId).contentBorderEl;
			else
				busyEl = options.busyWindowId;
		}

		if(options.updateWindowId) {
			if(typeof(options.updateWindowId ) == 'string') 
				updateEl = MochaUI.Windows.instances.get(options.updateWindowId).contentEl;
			else
				updateEl = options.updateWindowId;
		}

		var self = this;

		nemoRequest.count ++;

		nemoRequest.req[ nemoRequest.count ] = new Request.HTML(
			{
				url:options.url,

				//evalScripts: true,
				//evalResponse: true,

				update: options.update,

				onRequest: function() {
					if(options.onRequest)
						options.onRequest();

					if(busyEl) {
						showBusy(busyEl);
					}

				},
				onComplete: function() {

					if(options.onComplete) 
						options.onComplete();

					if(busyEl) {
						hideBusy(busyEl);
					}
					delete nemoRequest.req[nemoRequest.count];
					nemoRequest.req[nemoRequest.count] = null;

				},
				onSuccess: function(html,els,resHTML) {

					if(options.onSuccess) 
						options.onSuccess(html,els,resHTML);

					if(updateEl) {
						initWeb20Buttons(updateEl);
						toolTip.init( updateEl );
						renderTableHeader( updateEl );

						//jsBtn.init( updateEl );

						//if(options.tbodyId) randerFromEl( $(options.tbodyId) , 'tbl_tr' );

						// textarea bug fix
						if(Browser.Engine.trident) {
							updateEl.getElements('textarea').each( function( area ) {
								area.value = area.value;
							});
						}

					}

					
				},
				onFailure: function() {
					MochaUI.alertWindow("페이지 로딩에 실패 하였습니다. 관리자에게 문의해 주시기 바랍니다");
				}
			});

		return this;

	},
	cancel : function() {
		nemoRequest.req[ nemoRequest.count ].cancel();
	},
	put: function() { alert('ok'); },
	get: function( getOptions ) {
		nemoRequest.req[nemoRequest.count].get( getOptions );
		//delete this.req;
	},
	post: function( formElement ) {
		nemoRequest.req[nemoRequest.count].post( formElement );
		//delete this.req;
	}
};

*/
var nemoRequest = {
	options: {},
	retryCount: 0,
	getOptions: null,
	formElement: null,
	method: null,
	init: function( options ) {

		nemoRequest.options = options;
		nemoRequest.options.onFailure = nemoRequest.onFailure;

		/*
		nemoRequest.retryCount = 0;
		nemoRequest.method = null;
		nemoRequest.getOptions = null;
		nemoRequest.formElement = null;
		*/

		this.Ajax = new nemoAjax( options );
	},
	put: function() {  },
	get: function( getOptions ) {
		nemoRequest.method = 'get';
		nemoRequest.getOptions = getOptions;
		this.Ajax.get( getOptions );
	},
	post: function( formElement ) {
		nemoRequest.method = 'post';
		nemoRequest.formElement = formElement;
		this.Ajax.post( formElement );
	},
	onFailure: function() {

		if(nemoRequest.retryCount < 3) {
			nemoRequest.retryCount ++;
			MochaUI.notification("페이지 호출을 재 시도 합니다 ("+nemoRequest.retryCount+")");

			this.Ajax = new nemoAjax( nemoRequest.options );

			if(nemoRequest.method == 'get') {
				this.Ajax.get( nemoRequest.getOptions );
			} else {
				this.Ajax.post( nemoRequest.formElement );
			}

		} else {
			if(nemoRequest.options.update) {
				MochaUI.alertWindow("페이지 로딩에 실패 하였습니다. 관리자에게 문의해 주시기 바랍니다");
			}
		}
	}
}

var nemoAjax = new Class( {

	initialize: function( options ) {

		this.options = options;
		this.busyEl = null;
		this.updateEl = options.updateEl;
		
		//if(Browser.Engine.trident ) {
			//if (options.update)	options.update.empty();
		//}

		this.init();
			//options.async = false;
		var self = this;

		this.req = new Request.HTML(
			{
				url: options.url,

				//evalScripts: true,
				//evalResponse: true,

				//update: options.update,
				evalScripts: false,
				
				onRequest: function() {
					if(options.onRequest)
						options.onRequest();

					if(self.busyEl) {
						showBusy(self.busyEl);
					}

				},
				onComplete: function() {

					if(options.onComplete) 
						options.onComplete();

					if(self.busyEl) {
						hideBusy(self.busyEl);
					}
					//delete this.req;
				},
				onSuccess: function(html,els,resHTML,scripts) {
					if(options.update) {
						if(resHTML.trim() != '') {
							$(options.update).empty().set('html', resHTML);
							//$exec(scripts);
						}
					}
					
					if(scripts.trim()) {
						$exec(scripts);
					}
					
					//if(resHTML.trim() != '') {	
					//	alert('script');
					//}
					
					if(options.onSuccess) 
						options.onSuccess(html,els,resHTML,scripts);

					if(self.updateEl) {
						
						initWeb20Buttons(self.updateEl);
						toolTip.init( self.updateEl );
						renderTableHeader( self.updateEl );

						//jsBtn.init( updateEl );

						//if(options.tbodyId) randerFromEl( $(options.tbodyId) , 'tbl_tr' );

						// textarea bug fix
						if(Browser.Engine.trident) {
							self.updateEl.getElements('textarea').each( function( area ) {
								area.value = area.value;
							});
						}

					}
				},
				onFailure: function() {
					if(options.onFailure) {

						options.onFailure();

					} else {
						if(options.update) {
							MochaUI.alertWindow("페이지 로딩에 실패 하였습니다. 관리자에게 문의해 주시기 바랍니다");
						}
					}
				}
			});

		return this;

	},
	init: function() {
		if(this.options.busyWindowId) {
			if(typeof(this.options.busyWindowId ) == 'string') {
				if(MochaUI.Windows.instances.get(this.options.busyWindowId)) {
					this.busyEl = MochaUI.Windows.instances.get(this.options.busyWindowId).contentBorderEl;
				} else {
					this.busyEl = $(document.body);
				}
			} else {
				this.busyEl = this.options.busyWindowId;
			}
		}

		if(this.options.updateWindowId) {
			if(typeof(this.options.updateWindowId ) == 'string') {
				if(MochaUI.Windows.instances.get(this.options.updateWindowId)) {
					this.updateEl = MochaUI.Windows.instances.get(this.options.updateWindowId).contentEl;
				} else {
					this.busyEl = $(document.body);
				}
				
			} else {
				this.updateEl = this.options.updateWindowId;
			}
		}

	},
	put: function() { alert('ok'); },
	get: function( getOptions ) {
		this.req.get( getOptions );
		//delete this.req;
	},
	post: function( formElement ) {
		this.req.post( formElement );
		//delete this.req;
	}
});	
	
/*
options
	element: 기본 엘리먼트 
	altcolor: 배경컬러
*/

		
renderTable = new Class( 
{
	initialize: function( options ) {
		this.selectedCell = null;
		this.selectedRow = null;

		this.options = options;

		this.options.element = (this.options.element || document);

		this.options.altcolor = (this.options.altcolor || "#E9F4FE");  //#EFF7FE #f4f9fe
		this.options.color = (this.options.color || "#fff");
		this.options.selectedColor = (this.options.selectedColor || "#BED6ED");
		
		//this.options.cursor = (this.options.cursor || "default");
		this.options.focusColor = (this.options.focusColor || '#CDE4FC'); // #E3F0FD

		var headerTrs = this.options.element.getParent().getElement('thead').getLast();

		if(!headerTrs) alert('테이블에 thead 이상');

		this.header = headerTrs.getElements('th');

		if(!this.header) alert('테이블에 th 이상');


		return this;
	},

	getElement: function() {
		return this.options.element;
	},

	getSelectedCell: function() {
		return this.selectedCell;
	},

	getSelectedRow: function() {
		return this.selectedRow;
	},

	render: function() {


		var trs = $(this.options.element).getElements("TR");

		zIndex = 0;

		if(trs.length > 0) {


			node = trs[0];
			while( node && node.style) {
				if(node.style.zIndex) {
					zIndex = node.style.zIndex+1;
					break;
				}
				node = (node.parentNode || node.parentElement);
			}

			// td에 div로 감싼다 길이 부여
			for( var i = 0; i < trs.length; i ++ ) {
				var tds = trs[i].getElements('td');
				for( var j = 0; j < tds.length; j ++ ) {
					if(this.header[j]) {
						//tds[j].setStyle('width',(this.header[j].getStyle('width').toInt())+'px');
						var dv = new Element('div',{
							'class':'tbl_td_div',
							'styles': {'width': (this.header[j].getStyle('width').toInt()-10)+'px' }
						});
						dv.adopt(tds[j].childNodes);
						tds[j].empty();
						dv.inject(tds[j]);

					}
				}
			}
			
			

			this.renderByTr( trs );
			
			if(this.options.select)
				this.setClick( trs  );

			if(this.options.popup) {
				
				if(this.options.popupCellIndex != null) {
					this.options.popup.bindCellOneCell( trs, zIndex, this.options.popupCellIndex );
				} else {
					tds = this.options.element.getElementsByTagName("TD");
					// 팝업메뉴 이벤트 생성
					this.options.popup.bindCell( tds, zIndex );
				}
			}

		
		}

	},

	setClick: function( trs ) {
		// cell 클릭이벤트 생성
		var self = this;
		
		trs = (trs || this.options.element.getElementsByTagName("TR"));
		
		for(var i = 0; i < trs.length; i ++ ) {
			/*
			addevent(trs[i],'click',function(e) { 
				alert("OK");
					self.selectedRow = this;
				alert("OK2");
					//new Event(e).stop();
					},true);
			*/
			
			var tr = $(trs[i]);
					
			
			tr.addEvent('click' , function(e) { 

					if( self.options.select ) {
						if(self.selectedRow != null) {
							self.selectedRow.style.backgroundColor = (self.selectedRow.altColor? self.options.altcolor: self.options.color);
						}
					}
					self.selectedRow = this;
					if( self.options.select ) {
						//self.selectedRow.
						self.selectedRow.style.backgroundColor = self.options.selectedColor;
					}
					// option에 onclick 이 있으면 실행
					if(self.options.onclick) {
						self.options.onclick( self.selectedRow );
					}
					//alert('ok');

					if(self.selectedCell.getElements("a").length > 0 || self.selectedCell.getElements("input").length > 0) {

					} else {
						new Event(e).stop();
					}
			});
			
			for(var j = 0; j < tr.cells.length; j ++ ) {
				

				$(tr.cells[j]).addEvent('click' , function(e) 
					{ 
						self.selectedCell = this; 

					}
				);

				
				/* onclick event 만들기 */ 
				if(Browser.Engine.trident4) {
					
					cell = tr.cells[j].getElements('a');
					for(var k = 0; k < cell.length; k ++ ) {
						if(!cell[k].makeClickEvent) {
							cell[k].makeClickEvent = true;
							if(cell[k].getAttribute('onclick')) {
								script = cell[k].getAttribute('onclick');
								cell[k].setStyle('cursor','pointer');
								cell[k].addEvent('click',function() { eval(this.get('onclick')); });
								//cell[k].setAttribute("onclick",function(){ eval(cell[k].getAttribute("onclick")) }) ;

							}
						}
					}
				}
				
			}
		}


		//tds = this.options.element.getElementsByTagName("TD");
		
	},

	// 클래스네임의 tr을 찾아서 테이블의 짝수 row에 백그라운드를 준다
	renderByClassName: function ( className ) {

		trs = getElementsByClassName(className,"*",this.options.element);

		this.renderByTr( $(trs) );

	},
	// tbody row 렌더링
	renderByTr: function( trs ) {

		var self = this;

		trs = (trs || this.options.element.getElementsByTagName("TR"));

		if(this.options.cursor)
			(trs[0].parentNode || trs[0].parentElement).style.cursor = this.options.cursor;


		for( i = 0; i < trs.length; i ++ ) {
			if( (i % 2) == 1) {
				trs[i].style.backgroundColor = this.options.altcolor;
				trs[i].altColor = true;
			} else {
				trs[i].style.backgroundColor = this.options.color;
				trs[i].altColor = false;
			}

			if(this.options.focus) {
				trs[i].addEvent("mouseover", function(e) { 
						if( this == self.selectedRow && self.options.select ) 
							this.style.backgroundColor = self.options.selectColor;
						else
							this.style.backgroundColor = self.options.focusColor ;
					} );
				trs[i].addEvent("mouseout", function(e) { 
						if( this == self.selectedRow && self.options.select ) 
							this.style.backgroundColor = self.options.selectColor;
						else
							this.style.backgroundColor = (this.altColor? self.options.altcolor: self.options.color);
					} );
			}


		}

	}
});

function renderTableHeader( el ) {

	//tbl = getElementsByClassName( "tbl", "table", el )
	var tbl = $(el).getElements('.tbl');

	max = 0;
	
	for( i = 0; i < tbl.length; i ++ ) {
		
		var tbl = $(tbl[i]);

		if(tbl.isRender) continue;

		tbl.isRender = true;
		
		tbl.getElements('th').each( function( th ) {
			
			//if(Browser.Engine.trident) {
			//	th.set('class', 'ie6');
			//} else {

				height = th.getStyle('height');
				//alert( height );
				//if(height > max) max = height;

				//txt = th.get('text');

				var inHTML = th.childNodes;

				var table = new Element('table',{
						'border' : '0'
						,'cellpadding' : '0'
						,'cellspacing' : '0'
				});

				var tbody = new Element('tbody').inject( table );

				var tr = new Element('tr').inject( tbody );
				new Element('td', {'colspan' : '3', 'class' : 'top'}).inject( tr );

				tr = new Element('tr').inject( tbody );
				var td = new Element('td', {'class' : 'middle_left', 'styles' :{'height':height}} ).inject( tr );

				new Element('div', {'styles' :{ 'width':'1px'} } ).inject( td );

				td = new Element('td', {'class' : 'middle_center'} ).inject( tr );
				td.adopt(inHTML);



				td = new Element('td', {'class' : 'middle_right'} ).inject( tr );
				new Element('div', {'styles' :{ 'width':'1px'} } ).inject( td );


				tr = new Element('tr').inject( tbody );
				new Element('td', {'colspan' : '3', 'class' : 'bottom'}).inject( tr );
						
				th.erase('text');


				table.inject( th );

			//}

		});

		/*
		tbl[i].getElements('th').each( function( th ) {
			if(!Browser.Engine.trident4) {
				tds = th.getElements('td');
				for( j = 0; j < tds.length; j ++ ) {
					if(tds[j].get('class') == 'middle_left') {
						//if(Browser.Engine.trident)
							tds[j].setStyle('height', height+'px');	
						//else
						//	tds[j].setStyle('height', (height+9)+'px');	
						break;
					}
				}
			}
		});
		*/
		

	}

}
	

/* 클래스네임의 tr을 찾아서 테이블의 짝수 row에 백그라운드를 준다*/

function renderFromEl( element, className, color ) {

	el = (element || document);

	trs = getElementsByClassName(className);

	for( i = 0; i < trs.length; i ++ ) {
		if( (i % 2) == 1) {

			trs[i].style.backgroundColor = (color || "#f4f9fe");
		}
	}
}

// tbody의짝수 row에 백그라운드를 준다*/
function renderFromTBoy ( el, color ) {

	trs = el.getElementsByTagName("TR");

	for( i = 0; i < trs.length; i ++ ) {
		if( (i % 2) == 1) {

			trs[i].style.backgroundColor = (color || "#f4f9fe");
		}

	}
}

function getElementsByClassName(className, tag, elm) {

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


function chkNumber( obj ) {

	val = obj.value.substring(obj.value.length-1,obj.value.length);
	if(val < '0' || val > '9' ) {
		obj.value = obj.value.substring( 0, obj.value.length-1 );
	}
}


function addevent(element, name, observer, capture) {
    if (typeof element == 'string') {
        element = document.getElementById(element);
    }
    if (element.addEventListener) {
        element.addEventListener(name, observer, capture);
    } else if (element.attachEvent) {
        element.attachEvent('on' + name, observer);
    }
};

function getOffset(what, offsettype) {

		var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
		var parentEl=what.offsetParent;
		while (parentEl!=null){
			totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
			parentEl=parentEl.offsetParent;
		}
		return totaloffset;

}

// ajax 로인하여 onclick 이벤트가 없어질경우 addevent 로대체
function reMakeOnClickEvent( element ) {

	if(Browser.Engine.trident) {

		var fnc = function( el ) {
				if($defined(el.get('onclick'))) {
					if(!el.makeClickEvent) {
						el.makeClickEvent = true;
						el.addEvent('click',function() { eval(this.get('onclick')); });
					}
				} else if($defined(el.get('href')))	{
					if(!el.makeClickEvent) {
						el.makeClickEvent = true;
						el.erase('href');
						el.addEvent('click',function() { eval(this.get('href')); });
					}
				}
		};


		if(element.tagName == 'A' ) {
			fnc(element);
		} else {
			element.getElements('a').each( function(el) {
				fnc(el);
			});
		}
	}

}

function initSmallBtn(el) {

	c = getElementsByClassName( "smallbtn(.*)", "A", el );

	trident4 = Browser.Engine.trident4;


	for(i=0; i < c.length; i++){

		if(!$(c[i]).makeSmallBtn) {

			$(c[i]).makeSmallBtn = true;

			
			/* onclick event 만들기*/
			if(Browser.Engine.trident4) {
				if(!$(c[i]).makeClickEvent) {
					if($(c[i]).get('onclick')) {
						$(c[i]).makeClickEvent = true;
						$(c[i]).addEvent('click',function() { eval(this.get('onclick')); });
					}
				}
			}
			


			className = c[i].className;
			st = className.substring( className.indexOf(" ") +1 );

			var g=c[i].get('text'); //innerHTML;
			w = $(c[i]).getSize().x;
			$(c[i]).set('text','');
			c[i].className = "smallbtn";

			tbl = new Element( "table" , {"cellpadding":0, "cellspacing":0, "border": "0" } ).inject(c[i]);

			tbody = new Element( "tbody" ).inject(tbl);

			tbl.className = st;

			row = new Element( "tr" ).inject(tbody);

			//if(!trident4) {


				cell = new Element( "td" ).inject(row);
				cell.style.width = '1px';
				cell.style.height = '1px';
				if(trident4) cell.style.backgroundColor = "white";

				cell = new Element( "td"  ).inject(row);
				cell.className = "color"
				cell.style.height = '1px';

				cell = new Element( "td" ).inject(row);
				cell.style.width = '1px';
				cell.style.height = '1px';
				if(trident4) cell.style.backgroundColor = "white";

				row = new Element( "tr" ).inject(tbody);

				cell = new Element( "td" ).inject(row);
				cell.className = "color";
				cell.style.width = '1px';
				cell.style.height = '1px';
			//}

				cell = new Element( "td"  ).inject(row);
				cell.style.width = w+'px';
				cell.className = "padding_color"
			//	if(trident4) {
			//		cell.setStyle('padding','2px 3px 0px 3px');
			//	}
				cell.appendText( g );

			//if(!trident4) {


				cell = new Element( "td" ).inject(row);
				cell.className = "color";
				cell.style.width = '1px';
				cell.style.height = '1px';


				row = new Element( "tr" ).inject(tbody);

				cell = new Element( "td" ).inject(row);
				cell.style.width = '1px';
				cell.style.height = '1px';
				if(trident4) cell.style.backgroundColor = "white";

				cell = new Element( "td"  ).inject(row);
				cell.className = "color"
				cell.style.height = '1px';

				cell = new Element( "td" ).inject(row);
				cell.style.width = '1px';
				cell.style.height = '1px';
				if(trident4) cell.style.backgroundColor = "white";

			//}


			
		}

	}
}	



function initWeb20Buttons( el ){

	c = getElementsByClassName( "web20button(.*)", "A", el );

	for(i=0; i < c.length; i++){

		if(!$(c[i]).makeClickEvent) {

			className = c[i].className;
			$(c[i]).makeClickEvent = true;
			st = className.substring( className.indexOf(" ") +1 );

			
			if(!Browser.Engine.trident4) {  

			// fire fox / ie7이면 버튼을 바꾼다


				if( st == 'bigblue' ) {

					c[i].className = 'btn blue';

				} else if (st == 'bigpink') {

					c[i].className = 'btn pink';

				} else if (st.test('pink')) {
					c[i].className = 'btn_sm pink';
				} else {

					c[i].className = 'btn_sm blue';

				}

			} else {

				// ie 6
				
				var g=c[i].innerHTML;
				var owidth = $(c[i]).offsetWidth;
				var borderClass = '';

				if(className.test('pink')) 
					borderClass = 'border_pink';
				else
					borderClass = 'border_blue';


				$(c[i]).set('text','');

				tbl = new Element( "table" , {'class':'ieButton', 'align':'center', "cellpadding":0, "cellspacing":0, "border": "0" } ).inject(c[i]);

				tbody = new Element( "tbody" ).inject(tbl);

				tbl.className = 'ieButton';

				row = new Element( "tr" ).inject(tbody);
			
				cell = new Element( 'td' , { 'styles': {'width':'1px', 'height' : '1px' , 'background-color':'#fff'}} ).inject(row);
				cell = new Element( "td" , { 'class': borderClass ,'styles': {'height' : '1px' }} ).inject(row);
				cell = new Element( "td" , { 'styles': {'width':'1px', 'height' : '1px'  , 'background-color':'#fff'}} ).inject(row);


				row = new Element( "tr" ).inject(tbody);
				cell = new Element( 'td' , { 'class': borderClass ,'styles': {'width':'1px'}} ).inject(row);

				cell = new Element( 'td' , { 
					'text' : g, 
					'styles': {
						'word-break':'keep-all'
					}
				}).inject(row);
				//$(c[i]).inject( cell );
				//alert($(c[i]).getAttribute('onclick'));

				
				/* onclick event 만들기 */
				if( $(c[i]).get('onclick') ) {
					cell.addEvent('click',function() { eval(this.getParent('a').get('onclick')); });
				} else if($(c[i]).getProperty('href'))	{
					//$(c[i]).erase('href');
					cell.addEvent('click',function() { eval(this.getParent('a').getProperty('href')); });
				}
				


				newClass = 'ctd';
				if(className.test('pink')) {
					newClass += ' red';
					cell.enterEndColor = '#F08E59';
					cell.leaveEndColor = '#CEB7B7';
				} else {
					cell.enterEndColor = '#80D9FF';
					cell.leaveEndColor = '#A7BDDE';
				}
				if(!className.test('big'))
					newClass += ' small';
				cell.set('class',newClass);


				cell.addEvent('mouseenter', function() { this.filters.item(0).EndColorStr=this.enterEndColor; });
				cell.addEvent('mouseleave', function() { this.filters.item(0).EndColorStr=this.leaveEndColor; });

				cell = new Element( 'td' , { 'class': borderClass ,'styles': {'width':'1px'}} ).inject(row);

				row = new Element( "tr" ).inject(tbody);

				cell = new Element( 'td' , { 'styles': {'width':'1px', 'height' : '1px' , 'background-color':'#fff'}} ).inject(row);
				cell = new Element( "td" , { 'class': borderClass ,'styles': {'height' : '1px' }} ).inject(row);
				cell = new Element( "td" , { 'styles': {'width':'1px', 'height' : '1px' , 'background-color':'#fff'}} ).inject(row);


				//reMakeOnClickEvent( $(c[i]) );
				
				
				/*

				
				var g=c[i].innerHTML;


				dv = new Element('div',{
						'nowrap' : 'true',
						'align' : 'center',
						'valign' : 'middle',
						'unselectable' : 'on',
						'styles' : {
							'position' : 'relative',
							'width' : $(c[i]).offsetWidth}
					});


				dv2 = new Element('div',{
						'unselectable' : 'on'
					});
				
				$(c[i]).empty();

				newClass = 'ieButton';

				if(className.test('pink')) {
					newClass += ' red';
					dv.enterEndColor = '#F08E59';
					dv.leaveEndColor = '#DDB4A8';
				} else {
					dv.enterEndColor = '#85E0DA';
					dv.leaveEndColor = '#A7BDDE';
				}
				dv.addEvent('mouseenter', function() { this.filters.item(0).EndColorStr=this.enterEndColor; });
				dv.addEvent('mouseleave', function() { this.filters.item(0).EndColorStr=this.leaveEndColor; });

				if(!className.test('big'))
					newClass += ' small';

				$(c[i]).className = newClass;
				$(c[i]).setAttribute('unselectable','on');

				dv2.appendText(g);
				dv2.inject(dv);

				dv.inject(c[i]);


				delete dv;
				delete dv2;
				*/
				

			}
			
		}	
	}

	if(!Browser.Engine.trident4)
		jsBtn.init( el );
}

function closeWindow( windowEl ) {
	
	if(!windowEl) return;
	var winId;
	
	if( typeof(windowEl) == 'string' ) {
		winId = $(windowEl).id;
		MochaUI.closeWindow($(windowEl));
	} else {
		winId = windowEl.id;
		MochaUI.closeWindow(windowEl);
	}
	
	MochaUI.Windows.instances.each(function(instance){
		
		if (instance.options.type == 'modal' || instance.options.type == 'modal2') {
			if (instance.windowEl.id.indexOf(winId) == 0) {
				closeWindow( instance.windowEl );
			}
			return;
		}
	});

	
}


// 검색 폼값 체크
function checkFormValue( form ) {
	for(var i = 0; i < form.elements.length; i ++ ) {
		var txt = '', title = '';

		if(!form.elements[i].value && form.elements[i].getAttribute('mustInput') == 'Y') {
			msg = form.elements[i].getAttribute('msg');
			if(msg) {
				title = msg + '은 필수항목 입니다';
			} else { 
				title = '필수항목 입니다';
			}
			if($(form.elements[i]).listBox) {
				toolTip.showTipAtControl( $(form.elements[i]).listBox.displayControl, title );
			} else {
				toolTip.showTipAtControl( form.elements[i], title );
			}
			//form.elements[i].focus();
			return false;
		}
	}

	return true;
}

// 엔터키 검색 이벤트 만들기
function keyUpEvent( id, form, callBack ) {

	for(var i = 0; i < form.elements.length; i ++ ) {

		if(form.elements[i].getAttribute('type').test('text|password|checkbox')) {
			$(form.elements[i]).addEvent('keydown', function(e) {
				if(e.key == 'enter') {
					new Event(e).stop();
					if(callBack) {
						callBack();
					} else {
						$(id).list();
					}
				}
			});
		}
	}

}

function setFormValue( form, obj ) {
	
	for(i = 0; i < form.elements.length; i ++ ) {
		obj.setAttribute( form.elements[i].id, form.elements[i].value )
		if(form.elements[i].value && form.elements[i].getAttribute('mustInput') == 'Y') {
			msg = form.elements[i].getAttribute('msg');
			if(msg) {
				title = msg + '은 필수항목 입니다';
			} else { 
				title = '필수항목 입니다';
			}
			toolTip.showTipAtControl( form.elements[i], title );
			//form.elements[i].focus();
			return false;
		}
	}

	return true;
}

// 검색폼값을 새로운 히든폼값으로 복사한다
function cloneForm(form, newid, id, gridEl, wrapperEl ) {

	if($(newid)) $(newid).destroy();

	var newform = new Element('form', 
		{'id': newid,
		 'name': newid,
	 	 'display' : 'none',
		 'method' : 'post',
		 'styles' : { 'display' : 'none' }
		}
	);

	for(i = 0; i < form.elements.length; i ++ ) {
		new Element('input', {
			'id' : form.elements[i].id,
			'name' : form.elements[i].name,
			'type' : 'hidden',
			'value' : form.elements[i].value
		} ).inject( newform );
	}
	//alert(getOffset($(id),'top'));
	//alert(getOffset(gridEl,'top'));

	if(id) {
		//페이징을 위한값
		new Element('input', {
			'id' : 'curPage',
			'name' : 'curPage',
			'type' : 'hidden',
			'value' : '1'
		} ).inject( newform );

		var h;

		var winEl = $(document.body);
		if(wrapperEl) {
			winEl = wrapperEl;
		} else if(MochaUI.Windows.instances.get(id)) {
			winEl = MochaUI.Windows.instances.get(id).contentWrapperEl;
		}

		if(!$(id)._nextLoading) {
			if(Browser.Engine.gecko || MochaUI.Windows.instances.get(id)) {
				h = winEl.getSize().y - (gridEl.getPosition().y - $(id).getPosition().y);
			} else {
				h = winEl.getSize().y - (gridEl.getPosition().y - $(id).getPosition().y) - 47;
			}
			$(id)._nextLoading = true;
		} else {
			if(Browser.Engine.gecko) {
				h = winEl.getSize().y - (gridEl.getPosition().y - $(id).getPosition().y);
			} else {
				h = winEl.getSize().y - (gridEl.getPosition().y - $(id).getPosition().y) + 29;
			}
		}


		//alert(MochaUI.Windows.instances.get(id).contentWrapperEl.getSize().y );
		if( gridEl ) {

			//페이징을 위한 높이 값
			new Element('input', {
				'id' : 'listHeight',
				'name' : 'listHeight',
				'type' : 'hidden',
				'value' : h
			} ).inject( newform );
				//alert($(id).getPosition().y);
				//alert(gridEl.getPosition().y);
				//alert($(id).getStyle('height').toInt());


		}

		// 기본 객체에 할당
		newform.inject( $(id) );

		$(id).searchForm = newform;

	} else {
		// 소스폼위치에 할당
		newform.inject( form.getParent() , 'top');
	}

}


// 검색 폼값을 입력폼값 에 복사한다.
function copyForm( sform, eform ) {

	for( i = 0; i < sform.length; i ++ ) {
		// 존재 하면
		if(eform.elements[ sform.elements[i].id ]) {
			eform.elements[ sform.elements[i].id ].value = sform.elements[i].value;
		} else {
			new Element('input', {
				'id' : sform.elements[i].id,
				'name' : sform.elements[i].name,
				'type' : 'hidden',
				'value' : sform.elements[i].value
			} ).inject( eform );
		}
	}
}

// 파라미터로 전달할 hash json을 폼으로 변경한다.
function jsonToForm( id, JsonStr ) {

	if($(id).jsonForm) $(id).jsonForm.destroy();

	var jsonObj = JSON.decode( JsonStr );

	$(id).jsonForm = makeJsonForm( id, jsonObj );

	return $(id).jsonForm;
	
}

function makeJsonForm(id, jsonObj) {


	JsonHash = new Hash(jsonObj);

	var frm = new Element('form', {
			'name' : id+'_tmpform',
			'id' : id+'_tmpform'
		}).inject( $(id) );

	JsonHash.each( function(value,key) {

		new Element('input', {
				'name' : key,
				'id' : key,
				'type' : 'hidden',
				'value' : value 
		}).inject( frm );
	});

	return frm;
}

function nemoAlert( msg ) {

	MochaUI.alertWindow(msg);

}

function drawBox(ctx, width, height, shadowBlur, shadowOffset, shadows){

		var shadowBlur2x = shadowBlur * 2;
		var cornerRadius = 8;

		// This is the drop shadow. It is created onion style.
		if ( shadows != false ) {	
			for (var x = 0; x <= shadowBlur; x++){
				roundedRect(
					ctx,
					shadowOffset.x + x,
					shadowOffset.y + x,
					width - (x * 2) - shadowOffset.x,
					height - (x * 2) - shadowOffset.y,
					cornerRadius + (shadowBlur - x),
					[0, 0, 0],
					x == shadowBlur ? .29 : .065 + (x * .01)
				);
			}
		}
		// Window body.
		bodyRoundedRect(
			ctx,                          // context
			shadowBlur - shadowOffset.x,  // x
			shadowBlur - shadowOffset.y,  // y
			width - shadowBlur2x,         // width
			height - shadowBlur2x,        // height
			cornerRadius,                 // corner radius
			[229, 229, 229] //this.options.bodyBgColor      // Footer color
		);

		/*
		//if (this.options.type != 'notification'){

		headerStartColor =  [250, 250, 250];
		headerStopColor=   [229, 229, 229];

			topRoundedRect(
				ctx,                            // context
				shadowBlur - shadowOffset.x,    // x
				shadowBlur - shadowOffset.y,    // y
				width - shadowBlur2x,           // width
				50, //this.options.headerHeight,      // height
				cornerRadius ,                   // corner radius
				headerStartColor,  // Header gradient's top color
				headerStopColor    // Header gradient's bottom color
			);
		//}
		*/
		
	}

function bodyRoundedRect(ctx, x, y, width, height, radius, rgb){
		ctx.fillStyle = 'rgba(' + rgb.join(',') + ', 100)';
		ctx.beginPath();
		ctx.moveTo(x, y + radius);
		ctx.lineTo(x, y + height - radius);
		ctx.quadraticCurveTo(x, y + height, x + radius, y + height);
		ctx.lineTo(x + width - radius, y + height);
		ctx.quadraticCurveTo(x + width, y + height, x + width, y + height - radius);
		ctx.lineTo(x + width, y + radius);
		ctx.quadraticCurveTo(x + width, y, x + width - radius, y);
		ctx.lineTo(x + radius, y);
		ctx.quadraticCurveTo(x, y, x, y + radius);
		ctx.fill();

	}


function roundedRect(ctx, x, y, width, height, radius, rgb, a){
		ctx.fillStyle = 'rgba(' + rgb.join(',') + ',' + a + ')';
		ctx.beginPath();
		ctx.moveTo(x, y + radius);
		ctx.lineTo(x, y + height - radius);
		ctx.quadraticCurveTo(x, y + height, x + radius, y + height);
		ctx.lineTo(x + width - radius, y + height);
		ctx.quadraticCurveTo(x + width, y + height, x + width, y + height - radius);
		ctx.lineTo(x + width, y + radius);
		ctx.quadraticCurveTo(x + width, y, x + width - radius, y);
		ctx.lineTo(x + radius, y);
		ctx.quadraticCurveTo(x, y, x, y + radius);
		ctx.fill(); 
	}


function getWindowEl( id ) {
	return MochaUI.Windows.instances.get(id);
}

// 체크박스가 체크 되었는지 
function checkedCount( els ) {

	var len = 0;

	if(!els) return len;

	if(els.length) {
		for(var i =0; i < els.length; i ++ ) {
			if(els[i].checked) {
				len ++;
			}
		}
	} else {
		if(els.checked) len ++;
	}

	return len;

}

function ie6imageBugFix() {

	/*Use Object Detection to detect IE6*/
	var  m = document.uniqueID /*IE*/
	&& document.compatMode  /*>=IE6*/
	&& !window.XMLHttpRequest /*<=IE6*/
	&& document.execCommand ;

	try{
	  if(!!m){
	   m("BackgroundImageCache", false, true) /* = IE6 only */ 
	  }
	}catch(oh){};

}

// open flash chart 의 div z-index 를 위하여
function createSwfobject( swf, id, w, h, ver, install, flashvars ) {


	var params = {
	wmode: "opaque"
	};
	
	return swfobject.embedSWF(swf, id, w, h, ver, install, flashvars, params);


}

//open flash chart 의 div z-index 를 위하여
function createChartSwf( swf, id, w, h ) {

	return swfobject.embedSWF(swf, id, w, h, '9.0.0', 'swf/expressInstall.swf');


}

function chkswf(id) {
	alert($(id).innerHTML);
	/*
	var embedTag = $(id).getElementsByTagName("PARAM");
	for(var i =0 ; i < embedTag.length; i ++ ) {
		alert(embedTag[i].getAttribute('value'));
	}
	*/
}


// 이메일 형식 유효성 검사
function valid_email(email) { 

  if(email.match("^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+([\.][a-z0-9]+)+$") == null) {
      return false;
  }

  return true;
}

function CheckTel(str) 
{ 
  var flag = false; 
  var NumEx1 = /^[0-9-()~]+$/; 
  if(NumEx1.test(str)) { 
   	flag = true; 
  } else { 
   	flag = false; 
  } 
  return flag; 
} 


// drop shadow;
Element.implement({     

	smartDispose: function() {         // dispose of an element and its dropShadow (if there is one) 
		var rel = this.get("data-related");      
		if ($(rel)) {      
			$(rel).dispose();      
		}      
		this.dispose();     
	}, // end smartDispose     
	dropShadow: function(options) {    
		// creates a shadow effect to a rectangular element      
		// define defaults 
		/*
		var rel = this.get("data-related");      
		if ($(rel)) {      
			$(rel).dispose();      
		}
		*/

		if(!options) options = {};

		if(!options.x) options.x = 3;
		if(!options.y) options.y = 3;
		if(!options.opacity) options.opacity = .3;

		var options = $merge({             
			id: "dropShadow" + $random(100,1000),             
			border: "1px solid #000",             
			background: "#555",             
			zIndex: this.getStyle("z-index").toInt() - 1 // behind parent 
		}, options);         // only apply shadow on absolutely positioned elements 

		if (this.getStyle("position") != "absolute")             
			return this;         

		var c = this.getCoordinates();  
		
		var el = new Element("div", {             
				id: options.id,             
				styles: {                 
					position: "absolute",                 
					left: c.left + options.x,                 
					top: c.top + options.y,                 
					left: options.x,                 
					top: options.y,                 
					width: c.width,                 
					height: c.height,                 
					background: options.background,                 
					zIndex: options.zIndex             
				},             
				opacity: 0         
			}).injectBefore(this);
		
		if(options.fade)
			el.fade(0, options.opacity)
		else
			el.setStyle('opacity',options.opacity);
					
		// store the shadow id into the element 

		this.set("data-related", options.id);         
		return this;     
	} // end dropShadow 
}); 

// 체크박스 체크 여부
function isChecked( chkObj ) {
	var count = 0;

	if(chkObj) {
		if(chkObj.length) {
			for( var i = 0; i < chkObj.length; i ++ ) {
				if(chkObj[i].checked) {
					count ++;
				}
			}
		} else {
			if (chkObj.checked) count ++;
		}
	}

	return count;
}
// 체크박스 전체 선택
function selectAll(chkObj,checked) {
	
	if(chkObj) {
		
		if(chkObj.length) {
			for( var i = 0; i < chkObj.length; i ++ ) {
				chkObj[i].checked = checked;
			}
		} else {
			chkObj.checked = checked;
		}
	}
}


//검색폼값의 초기화
function initFormValue( form ) {

	for(var i=0; i < form.elements.length; i ++ ) {
		if($(form.elements[i]).get("type").toLowerCase() != "hidden") {
			if($(form.elements[i]).listBox) {
				if($(form.elements[i]).setSelect) {
					$(form.elements[i]).setSelect(0);
				}
			} else {
				form.elements[i].value = "";
			}
		}
	}

}

function getZIndex( node ) {
	var zIndex = 0;
	
	while( node && node.style) {
		if(node.style.zIndex) {
			zIndex = node.style.zIndex+1;
			break;
		}
		node = (node.parentNode || node.parentElement);
	}
	
	return zIndex;
	
}

function findGroupNode( tree, id ) {

	var node = tree.root.getFirst();
	
	while (true) {
		if( node == null) break;
		if(node.data.id == id) {
			return node;
		}
		node = node.getNext();
	} 
	
	return node;
	
}
/*
 * 그룹 노드를 찾아서 child 노드 array를 리턴
 */

function getChildNodes( tree, id ) {

	var node = tree.root.getFirst();
	
	while (true) {
		if( node == null) break;
		if(node.data.id == id) {
			return node.getChildren();
		}
		node = node.getNext();
	} 
	
	return null;

}

/*
 * chlid array에서 노드를 찾는다.
 */
function findNode( nodeArr, id ) {
	
	for( var i = 0; i < nodeArr.length; i ++) {
		if(nodeArr[i].data.id == id) {
			return nodeArr[i];
		}
	}
	return null;

}

//이메일 체크
function CheckEmail(strValue)
{ 

  emailEx1 = /^([A-Za-z0-9_]{1,15})(@{1})([A-Za-z0-9_]{1,15})(\\.{1})([A-Za-z0-9_]{2,10})(\\.{1}[A-Za-z]{2,10})?(\\.{1}[A-Za-z]{2,10})?$/; 
  emailEx1 = /^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+([\.][A-Za-z0-9]+)+$/

  return emailEx1.test(strValue);
  
} 

// 전화 체크
function CheckTel(str) 
{ 
	var patten = /^[0-9(]{2,4}[-)][0-9]{3,4}-[0-9]{4,4}$/;
	return patten.test(str);
} 


function getfileSize(size){ 
	if(size < 1024) { 
		return size + " bytes"; 
	} else if(size < 1048576) { 
		return (Math.round(((size*10) / 1024))/10) + " KB"; 
	} else { 
		return (Math.round(((size*10) / 1048576))/10) + " MB"; 
	} 
}