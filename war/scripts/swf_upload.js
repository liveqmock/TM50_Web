/*

2009-06-04 nemo create

				order_num = 'order_index',	// 순서를 기록하는 input 태그의 id 값입니다.
				modify_files = 'a.gif b.jpg', // 수정할 파일 이름 한칸씩 띄워서 순서대로
				modify_filesize = '245236 23144', // 수정할 파일 사이즈 한칸씩 띄워서 순서대로
				modify_fileidx ='2 4', // 수정할 파일 고유번호 한칸씩 띄워서 순서대로
				movie_id='smu05', //파일폼 고유ID
				flash_width='400', //파일폼 너비 (기본값 400, 권장최소 300)
				list_rows='5', // 파일목록 행 (기본값:5)
				limit_size='30', // 업로드 제한용량 (기본값 10메가)
				limit_total = '5',	// 업로드 제한갯수(기본값 무한대, 0 이면 무한대)
				file_type_name='모든파일', // 파일선택창 파일형식명 (예: 그림파일, 엑셀파일, 모든파일 등)
				allow_filetype='*.*', // 파일선택창 파일형식 (예: *.jpg *.jpeg *.gif *.png)
				deny_filetype='*.cgi *.pl', // 업로드 불가형식 
				upload_exe='upload.php' // 업로드 담당프로그램


*/


SwFileUpload = new Class({

	Implements: [Events, Options],

	options: {
		id: 'swfUpload',
		multi: false,
		width: 400,
		limitSize: 100,
		fileTypeName: '모든 파일',
		allowFileType: '*.*',
		denyFileType: '*.asp *.aspx *.com *.bat *.exe *.php *.cgi *.pl *.jsp *.asd *.chm *.dll *.ocx *.hlp *.hta *.js *.pif *.scr *.shb *.shs *.vb *.vbe *.vbs *.wsf *.wsh',
		listRows: 5,
		limitTotal: 5,
		modifyFiles: '',
		modifyFilesize: '',
		modifyFileidx: '',
		location: 'swf/'
	},

	initialize: function(id, options){
		this.setOptions(options);
		this.options.id = id;
		this.uploadCommpleted = false;
		this.uploadKey = '';

		this.param = new Hash();

		//this.render();

		if(options.onComplete) {
			this.addEvent('uploadComplete',options.onComplete.bind(this));
		}

		if(options.onNotExistsUploadFile) {
			this.addEvent('fileNotFound',options.onNotExistsUploadFile.bind(this));
		}

		return this;
	},
	render: function() {

		var method = 'single_upload';
		var movie = this.options.location+'single_upload.swf';
		var height = 70;

		if(this.options.multi) {
			method = 'multi_upload';
			movie = this.options.location+'multi_upload3.swf';
			height = this.options.listRows*20+25+45;
		}
		
		this.options.uploadPage = '../'+this.options.uploadPage;

		var flashvars = "flash_width="+this.options.width+"&amp;";
			flashvars += "limit_size="+this.options.limitSize+"&amp;";
			flashvars += "file_type_name="+this.options.fileTypeName+"&amp;";
			flashvars += "allow_filetype="+this.options.allowFileType+"&amp;";
			flashvars += "deny_filetype="+this.options.denyFileType+"&amp;";
			flashvars += "upload_exe="+this.options.uploadPage+"&amp;";
			flashvars += "upload_id="+this.options.id+"&amp;";
			flashvars += "browser_id="+Cookie.read("PHPSESSID");

		if(this.options.multi) {
			flashvars += "list_rows="+this.options.listRows+"&amp;";
			flashvars += "limit_total="+this.options.limitTotal+"&amp;";
			flashvars += 'order_num='+this.options.id+'_order&amp;';
			flashvars += "modify_files="+this.options.modifyFiles+"&amp;";
			flashvars += "modify_filesize="+this.options.modifyFilesize+"&amp;";
			flashvars += "modify_fileidx="+this.options.modifyFileidx+"&amp;";
		}

		var flashStr = "<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000'";
			flashStr += "codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0'";
			flashStr += "width='"+this.options.width+"' height='"+height+"' align='middle' id='"+this.options.id+"' method='"+method+"'>";
			flashStr += "<param name='allowScriptAccess' value='sameDomain' />";
			flashStr += "<param name='movie' value='"+movie+"' />";
			flashStr += "<param name='quality' value='high' />";
			flashStr += "<param name='wmode' value='transparent' />";
			flashStr += "<param name='bgcolor' value='#ffffff' />";
			flashStr += "<param name='flashvars' value='"+flashvars+"' />";
			flashStr += "<embed src='"+movie+"' width='"+this.options.width+"' height='"+height+"' quality='high'";
			flashStr += "bgcolor='#ffffff' id='"+this.options.id+"_embed' align='middle' allowScriptAccess='sameDomain' type='application/x-shockwave-flash'";
			flashStr += "pluginspage='http://www.macromedia.com/go/getflashplayer' flashvars='"+flashvars+"' wmode='transparent'/>";
			flashStr += "</object>";


		$(this.options.container).innerHTML = flashStr;

		
		new Element('input',{
			'type': 'hidden',
			'id': this.options.id+'_order'
		}).inject($(this.options.container));


		/*
		if(Browser.Engine.trident) {
			this.obj = $(this.options.id);
		} else {
			this.obj = $(this.options.id+'_embed');
		}
		*/
		
		return this;


	},
	upload: function( uploadKey ) {

		SwFileUpload.IO.currentUploadObj = this;

		if(uploadKey) {
			this.uploadKey = uploadKey;
		} else {
			if(!this.uploadKey)
				this.uploadKey = new Date().getTime(); // 업로드 키값을 부여한다.
		}

		if(Browser.Engine.trident) { // 키값을 플레쉬로 넘긴다.
			$(this.options.id).SetVariable( "setUploadKey", this.uploadKey );
		} else {
			$(this.options.id+'_embed').SetVariable( "setUploadKey", this.uploadKey );
		}

		if(Browser.Engine.trident) {
			$(this.options.id).SetVariable( "startUpload", "" );
		} else {
			$(this.options.id+'_embed').SetVariable( "startUpload", "" );
		}

	},
	setParameter: function( param ) {


		var hash = new Hash(param);
		var options = this.options;

		this.param = hash;

		if(Browser.Engine.trident) {
			$(this.options.id).SetVariable( "setParameter", hash.toQueryString() );
		} else {
			$(this.options.id+'_embed').SetVariable( "setParameter", hash.toQueryString() );
		}

	}

});

SwFileUpload.IO = {
	currentUploadObj:  null, 
	
	swfUploadComplete: function (files, fileSize){	

		fileArray = files.split('|');
		fileSizeArray = fileSize.split('|');
		
		SwFileUpload.IO.currentUploadObj.uploadCommpleted = true;

		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[false,fileArray,fileSizeArray]);
	},

	fileNotFound: function (){ //업로드할 파일이 선택되지 않았을때
		SwFileUpload.IO.currentUploadObj.fireEvent('fileNotFound');
	},

	fileTypeError: function ( notAllowFileType ){ //허용하지 않은 형식의 파일일 경우 에러 메시지 출력
		alert("확장자가 " + notAllowFileType + "인 파일들은 업로드 할 수 없습니다.");
		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[true]);
	},

	overSize: function ( limitSize ){ //사이즈 초과시 에러메세지 출력
		alert("선택한 파일의 크기가 " + limitSize + "를 초과했습니다.");
		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[true,[]]);
	},

	versionError: function (){ //플래쉬 버전이 8 미만일 경우 에러 메시지 출력
		alert("플래쉬 버전이 맞지 않습니다. 최신버전으로 설치해 주세요.\n이미 설치하신 경우는 브라우저를 전부 닫고 다시 시작하세요.");
		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[true,[]]);
	},

	httpError: function (){ //http 에러 발생시 출력 메시지
		alert("네트워크 에러가 발생하였습니다. 관리자에게 문의하세요.");
		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[true,[]]);
	},

	ioError: function (){ //파일 입출력 에러 발생시 출력 메시지
		alert("입출력 에러가 발생하였습니다.\n 다른 프로그램에서 이 파일을 사용중인지 확인하세요.");
		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[true,[]]);
	},

	onSecurityError: function (){ //파일 입출력 에러 발생시 출력 메시지
		alert("보안 에러가 발생하였습니다. 관리자에게 문의하세요.");
		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[true,[]]);
	},

	overTotal: function ( limitTotal ){ //허용하지 않은 형식의 파일일 경우 에러 메시지 출력
		alert("선택한 파일이 " + limitTotal + "개를 초과했습니다.");
		SwFileUpload.IO.currentUploadObj.fireEvent('uploadComplete',[true,[]]);
	},

	make_order: function (targetId, arg)
	{
		$(targetId).value = arg;
	}
}