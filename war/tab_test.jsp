<div class="toolbarTabs">
	<ul id="featuresTabs" class="tab-menu">
		<li id="featuresLayoutLink" class="selected"><a>Layout</a></li>
		<li id="featuresWindowsLink"><a>Windows</a></li>
		<li id="featuresGeneralLink"><a>General</a></li>
	</ul>
	<div class="clear"></div>
</div>

<script type="text/javascript">

	MochaUI.initializeTabs('featuresTabs');

	$('featuresLayoutLink').addEvent('click', function(e){
		MochaUI.updateContent({
			'element':  $('features'),
			'url':       'about:blank'
		});
	});

	$('featuresWindowsLink').addEvent('click', function(e){
		MochaUI.updateContent({
			'element':  $('features'),
			'url':       'about:blank'
		});
	});

	$('featuresGeneralLink').addEvent('click', function(e){
		MochaUI.updateContent({
			'element':  $('features'),
			'url':       'about:blank'
		});
	});

</script>