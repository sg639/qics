function progressFindTopWindow() {
	/*
	var win = window;
	for(var i=0;i<10;i++) {
		if(win.top) {
			win = win.top;
			if(win.parent) {
				win = win.parent;
			} else {
				return win;
			}
		} else {
			return win;
		}
	}
	return win;
	*/
	return window.top;
}

function progressStart(title, callbackFunc, delayTime) {
	if(!delayTime) {
		delayTime = 100;
	}
	var win = progressFindTopWindow();
	
	var $body = $(win.document.body);
	var $progressDiv = $body.find("#progressDiv");
	if($progressDiv.size() == 0) {
		var html = new Array();
		html.push('<div id="progressDiv" style="position:absolute;top:0px;left:0px;width:100%;height:100%;z-index:99999;display:none">');
		html.push('<div id="progressLoadingBgDiv" class="ui-popup-overlay"></div>');
		html.push('<div id="progressContentDiv" class="order_create_area"><div id="progressTitleDiv" class="txt">처리중입니다.<br/>잠시만 기다려 주세요</div></div>');
		html.push('</div>');
		
		$body.append(html.join(""));
		$progressDiv = $body.find("#progressDiv");
	}
	
	if(title) {
		$progressDiv.find("#progressTitleDiv").html(title);
	}
	var $progressContentDiv = $progressDiv.find("#progressContentDiv");
	var width = $progressContentDiv.width();
	var height = $progressContentDiv.height(); 
	var screenWidth = $(win).width();
	var screenHeight = $(win).height();
	var top = (screenHeight/2) - (height/2);
	var left = (screenWidth/2) - (width/2);

	$progressContentDiv.css("top",top);
	$progressContentDiv.css("left",left);
	
	$progressDiv.show();
	
	if(callbackFunc) {
		setTimeout(callbackFunc, delayTime);
	}
}

function progressStop(callbackFunc) {
	var $body = $(top.parent.top.document.body);
	var $progressDiv = $body.find("#progressDiv");
	if($progressDiv.size() > 0) {
		$progressDiv.hide();
	}
	if(callbackFunc) {
		callbackFunc();
	}
}
