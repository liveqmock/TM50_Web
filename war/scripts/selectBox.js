var makeSelectBox = {
	IsMochaUI: false,
	render: function( wrapperEl ) {

		wrapperEl = (wrapperEl || document.body);

		if($defined(MochaUI)) {
			this.IsMochaUI = true;
		}

		wrapperEl.getElements('ul').each( function(el) {

			if(el.get('class') == 'selectBox')
				makeSelectBox.makeControl( el );
			
			var tmp = el;
			while ( true  )
			{
				tmp = tmp.getParent();
				if(tmp == null) break;

				if (tmp.getStyle('z-index') > 0)
				{	
					el.setStyle('z-index', tmp.getStyle('z-index').toInt()+1 );
					break;
				}
				
			}
			if(!el.style.zIndex) {
				el.setStyle('z-index','100');
			}

		});

		document.addEvent( 'click', function() { makeSelectBox.hide(); } );
		//document.addEvent( 'contextmenu', function() { makeSelectBox.hide(); } );

	},
	hide: function() {
		if(makeSelectBox.focused != null) {
			makeSelectBox.focused.setStyle('display','none');
			makeSelectBox.focused.showing = false;
			makeSelectBox.focused = null;
		}
	},
	focused: null,
	makeControl: function( ul ) {

		ul.setStyle('display', 'none');
		ul.removeProperty('class');

		// 최상위 element
		var wrapper = new Element('div', {'class':'selectBox', 'styles': { 'margin':0, 'padding-right':6} }).inject( ul, 'before' );

		//wrp = new Element('div',{'styles': {'margin':0, 'padding':0} }).inject(wrapper,'top');
		// 값이 표시될 element
		//displayControl = new Element('input',{'type':'text','class':'box'}).inject( wrp, 'top' );

		displayControl = new Element('input',{'type':'text','class':'box'}).inject( wrapper, 'top' );

		var wrp = new Element('div',{'styles': {'display':'block','margin':0, 'padding':0} }).inject(wrapper);
		ul.inject( wrp );


		displayControl.readOnly = true;

		// 값이 저장될 element
		var hidden = new Element('input', {
				'name' : ul.id,
				'id' : ul.id,
				'type' : 'text',
				'styles' : { 'display' : 'none' }
		}).inject( wrapper , 'top' );

		if(ul.getAttribute('title')) {
			displayControl.set('title',ul.get('title'));
			ul.erase('title');
		}

		if(ul.getAttribute('mustInput')) {
			hidden.setAttribute('mustInput',ul.getAttribute('mustInput'));
			ul.erase('mustInput');
		}

		if(ul.getAttribute('msg')) {
			hidden.setAttribute('msg',ul.getAttribute('msg'));
			ul.erase('msg');
		}


		//selectText.textBox = textBox;


		ul.removeProperty('id');
		//ul.addEvent('change', function(ul) { ul.get('onchange')  } );
		ul.wrapper = wrapper;

		wrapper.listBox = ul;

		ul.displayControl = displayControl;
		displayControl.listBox = ul;

		hidden.listBox = ul;
		ul.hiddenInput = hidden;
		ul.set('value','');
		ul.value = '';


		ul.showing = false;
		ul.selected = null;

		//ul.getElements = function() { ul.getElements('li') };

		changefnc = function() { eval(ul.get('onchange'))  };
		ul.addEvent( 'change', changefnc.bindWithEvent(ul) );

		hidden.removeItem = function( index ) {
			
			var lis = this.listBox.getElements('li');
			for(i=0; i < lis.length; i ++) {
				if(i == index) {
					this.deleteItem(i);
					break;
				}
			}

		}

		hidden.deleteItem = function( index ) {
			var lis = this.listBox.getElements('li');
			if( lis[index].getAttribute('select') && lis[index].getAttribute('select').test('y','i')) {
				makeSelectBox.initValue(lis[index].getParent());
			}
			lis[index].dispose();

		}

		hidden.addItem = function(text, value) {
			li = new Element('li',{
					'text': text
			}).inject( this.listBox );
			li.setAttribute('data',value);

			makeSelectBox.setListProperty( li );

			//makeSelectBox.refresh( this.listBox );
		}

		hidden.getLength = function() {
			return this.listBox.getElements('li').length;
		}
		hidden.getList = function() {
			return this.listBox.getElements('li');
		}

		hidden.reSize = function() {
			var a = new Element('a',{'text': listBox.maxStr }).inject( document.body );
			this.listBox.maxWidth = a.getSize().x;
			a.dispose();
			makeSelectBox.setMaxWidth( this.listBox );
		}

		hidden.setSelect = function( index ) {
			var lis = this.listBox.getElements('li');
			for(i=0; i < lis.length; i ++) {
				lis[i].setAttribute('select','no');
				lis[i].erase('class');
				if(i == index) {
					makeSelectBox.select(lis[i]);
				}
			}
			this.refresh();
		}

		hidden.refresh = function() {
			makeSelectBox.refresh( this.listBox );
		}


		displayControl.addEvent('click', function(e) {
		
			if(this.listBox.showing) {
				makeSelectBox.hide();
			} else {
				//alert(this.offsetLeft);
				makeSelectBox.hide();
				//this.listBox.setStyle('left', '0px');
				//alert(this.getParent().getParent().offsetTop);
				//this.listBox.setStyle('top', (this.offsetTop+20)+'px');
				if(this.listBox.selected) {
					this.listBox.selected.set('class','selected');
				}
				makeSelectBox.reCoordinate( this.listBox );
				//this.listBox.setStyle('left', this.listBox.displayControl.offsetLeft+'px' );
				this.listBox.setStyle('display','block');
				new Event.stop(e);
				makeSelectBox.focused = this.listBox;
				this.listBox.showing = true;
			}

		});

		makeSelectBox.refresh( ul );

		wrapper.setStyle('display','block');

		
	},
	reCoordinate: function( listBox ) {

		listBox.setStyle('height','auto');

		var tmp = listBox.getParent();
		var h = $(document.body).scrollHeight;
		var t = 0;

		if(makeSelectBox.IsMochaUI) {
			var mocha = $$('.mocha.isFocused');
			if(mocha.length > 0) {
				h = mocha[0].getStyle('height').toInt() - 62;
				t = mocha[0].getPosition().y;
			}
		}
		if(listBox.getElements('li').length > 5 && h < (listBox.displayControl.getCoordinates().bottom-t) + listBox.getElements('li').length*16 ) {
			
			listBox.setStyle('height',
				 (h - (displayControl.getCoordinates().bottom-t)) + 'px');
		}


	},
	refresh: function( listBox ) {

		// li ************************
		makeSelectBox.initValue( listBox );

		listBox.maxWidth = 0;
		listBox.maxStr = '';
		var cnt = 0;
		listBox.getElements('li').each( function(li) {
			//if(li.getAttribute('test'))	{
			//	alert(li.getAttribute('selected')+' '+li.get('text'));
			//}
			makeSelectBox.setListProperty( li );
			cnt ++;
		});

		if(!listBox.selected && listBox.getElements('li').length > 0) {
			makeSelectBox.select( listBox.getFirst() );	
		}

		var a = new Element('a',{'text': listBox.maxStr }).inject( document.body );
		listBox.maxWidth = a.getSize().x;
		a.dispose();

		//makeSelectBox.reSize( listBox );
		makeSelectBox.setMaxWidth( listBox );

	},
	setListProperty: function( li ) {
		// 글씨 크기 구하기
		listBox = li.getParent();
		/*
		a = new Element('a',{'text': li.get('text') }).inject( document.body );
		w = a.getSize().x;
		a.dispose();
		*/
		var txt = li.get('text');

		if(txt.length > listBox.maxStr.length) {
			listBox.maxStr = txt;
		}


		if(Browser.Engine.trident) {
			li.onmouseenter = function(e) { 
				if(this.getParent().selected) {
					this.getParent().selected.erase('class');
				}
				this.set('class','selected');
			};
			li.onmouseleave = function(e) { this.erase('class')};
		} else {
			li.onmouseover = function(e) { 
				if(this.getParent().selected) {
					this.getParent().selected.erase('class');
				}
				this.set('class','selected');
			};
			li.onmouseout = function(e) { this.erase('class')};
		}


		li.onclick = function(e) {
			this.getParent().getElements('li').each( function(el) {
				el.setAttribute('select','no');
			});
			makeSelectBox.select( this );
			
			this.getParent().fireEvent('change');

			this.getParent().hiddenInput.fireEvent('change');
			
		}

		if(!li.getAttribute('data')) {
			li.setAttribute('data','');
		}
		
		var sel = li.getAttribute('select');
		if(sel) {
			if(sel.test('y','i')) {
				makeSelectBox.select( li );
			}
		}
	},
	setMaxWidth: function( listBox ) {


		listBox.displayControl.setStyle('width',(listBox.maxWidth+22)+'px');
		listBox.setStyle('width',(listBox.maxWidth+22)+'px');
		listBox.wrapper.setStyle('width',(listBox.maxWidth+22)+'px');

	},
	initValue: function( listBox ) {
		listBox.displayControl.set('value','');
		listBox.hiddenInput.set('value','');
		listBox.selected = null;
		listBox.hiddenInput.selected = null;
		listBox.set('value','');
		listBox.value = '';
		listBox.hiddenInput.text = '';
		listBox.hiddenInput.selectedIndex = -1;
	},
	select: function( li ) {
		listBox = li.getParent();
		listBox.displayControl.set('value',li.get('text'));
		listBox.hiddenInput.set('value',li.getAttribute('data'));
		listBox.set('value',(li.getAttribute('data') || ''));
		listBox.value = (li.getAttribute('data') || '');
		listBox.selected = li;
		listBox.hiddenInput.selected = li;
		//li.set('class','selected');
		li.setAttribute('select','yes');
		listBox.hiddenInput.text = displayControl.value;

		var index = 0;
		listBox.getElements('li').each( function(el) {
			if(el == li) listBox.hiddenInput.selectedIndex = index;
			index ++;
		});
	}
}
