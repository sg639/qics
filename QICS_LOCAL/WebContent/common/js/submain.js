// 탭 설정
var tabs;
var full = false;
var ssnDataRwType = "";  //20140102 적용

// 초기 매뉴 오픈을 위한 최소 기준 갯수를 가져옴
var openMenuCnt = ajaxCall("/CommonCode.do?cmd=getCommonNSCodeList&searchStdCd=LEFT_MENU_OPEN_CNT", "queryId=getSystemStdData", false) ;

// 현재 탭 메뉴 정보
var menuInfo = new Array();

// 탭 설정
function setTab() {

	tabs = $( "#tabs" ).tabs();
	// 탭 메뉴 활성화시에 이벤트
	tabs.on( "tabsactivate", function( event, ui ) {
		// 탭 활성화시에 로케이션값을 설정한다.
		$("#pageLocation").html( " &gt; " + ui.newTab.attr("location") );

		menuInfo["mainMenuCd"] = ui.newTab.attr("mainMenuCd") == "undefined" ? "" : ui.newTab.attr("mainMenuCd");
		menuInfo["priorMenuCd"] = ui.newTab.attr("priorMenuCd") == "undefined" ? "" : ui.newTab.attr("priorMenuCd");
		menuInfo["menuCd"] = ui.newTab.attr("menuCd") == "undefined" ? "" : ui.newTab.attr("menuCd");
		menuInfo["menuSeq"] = ui.newTab.attr("menuSeq") == "undefined" ? "" : ui.newTab.attr("menuSeq");

		// 도움말 버튼 숨김 여부
		$("#helpPopup").hide();
		//alert("["+menuInfo["mainMenuCd"]+"]");
		if(menuInfo["menuCd"] != ""){
			var data = ajaxCall("/HelpPopup.do?cmd=getHelpPopupMap"
					,"searchMainMenuCd="	+ menuInfo["mainMenuCd"]
					+ "&searchPriorMenuCd="	+ menuInfo["priorMenuCd"]
					+ "&searchMenuCd="		+ menuInfo["menuCd"]
					+ "&searchMenuSeq="		+ menuInfo["menuSeq"],false);
			if(data.map == null){
			} else {
				if ( ssnDataRwType == "A" ) {
					$("#helpPopup").show();
					//$("#helpPopup").hide();
					$("#helpPopup").removeClass("tPink");
					if ( data.map.mgrHelpYn == "Y" ) {
					} else {
						$("#helpPopup").hide();
						$("#helpPopup").addClass("tPink");
					}
				} else {
					if ( data.map.empHelpYn == "Y" ) {
						$("#helpPopup").show();
					}
				}

				if(data.map.myMenu == "Y") {
					$("#addMyMenu").html(" 나의메뉴 - ");
					//alert(0);
				} else {
					$("#addMyMenu").html(" 나의메뉴 + ");
					//alert(1);
				}


			}
		}
	});

	// 탭 닫기 버튼 클릭 이벤트
	tabs.delegate( "span.btn_close", "click", function(event) {
		event.stopPropagation();
		// 탭이 1개일경우 탭이 닫히지 않는다.
		if( tabs.find( "li" ).length < 2 ) {
			return;
		}

		/**
		추가된 소스 부분
		panelId를 가져오고 해당탭의 ibsheet div를 찾아 상태값이 I|U|D(추가,수정,삭제)인것을 찾는다.
		( sheet div에 ibsheet name 을 주기 위해 ibsheetinfo.js 값을 조금 변경했다.
		25번 라인에  name='IBSheet' 부분이 추가됨(ibsheetinfo.js)
		있으면 confirm 메시지를 띄우고 확인을 누르면 탭 닫기, 취소를 누르면 탭닫기 취소가 된다.
		*/
		var panelId = $( this ).closest( "li" ).attr( "aria-controls" );
		var str = "iframe_"+panelId;
		var obj = $('iframe[name='+ str + ']').contents().find("div[name=IBSheet]");
		for(var i=0, max=obj.length;max > i;i++){
			if(obj[i].id != null && obj[i].id.length > 4){
				var sheetNm = obj[i].id.substr(4);
				var objSheet = document.getElementsByName(str)[0].contentWindow.eval(sheetNm);
				var retData = objSheet.FindStatusRow("I|U|D");
				if(retData != ""){
					if(!(confirm("저장하지 않은 내용이 있습니다."))){
						return;
					}
				}
			}
		}

		//if(!tabcolse) return;
		// 탭을 제거한다.
		var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
		$( "#" + panelId ).remove();

		// 탭을 재정렬한다.F
		tabs.tabs( "refresh" );

		// 화면 높이값 재설정
		setIframeHeight();
	});
}

// 상단 메뉴 이벤트 설정
function setTopButton() {
	// 쿠키에 화면 전체사이즈 값이 없을 경우 false 설정

	if( getCookie("setFullSize") == null ){setCookie("setFullSize","true",1000);}

	// 쿠키에 탭 자동닫기 값이 없을 경우 true 설정
	if( getCookie("setTabAutoClose") == null ){setCookie("setTabAutoClose","true",1000);}

	// 화면 전체사이즈 값을 쿠키에서 가져온다.
	setFullSize = getCookie("setFullSize");

	// 탭 자동닫기 값을 쿠키에서 가져온다.
	setTabAutoClose = getCookie("setTabAutoClose");

	// 탭 자동닫기 아이콘을 설정한다.
	if( setTabAutoClose == "true" ){$(".topTab").addClass("active");} else {$(".topTab").removeClass("active");}

	// 화면 전체사이즈 아이콘을 설정한다.
	if( setFullSize == "true" ) {
		$("#wrap").addClass("full");
		$(".topFull").addClass("active");
	}

	$("#tabWidgetMain").click(function() {
		return false;
	});

	// 탭 상단설정 아이콘을 클릭시에 위젯을 보여준다.
	$(".topTab").click(function() {
		$(document).click();
		$("#setTabAutoCloseCheck").attr("checked", getCookie("setTabAutoClose")=="true"?true:false);
		$("#tabWidgetMain").show();
		$(document).click(function() { $("#tabCancel").click();return false; });
		return false;
	});

	$("#setTabAutoCloseCheck").click(function(e) {
		e.stopImmediatePropagation();
		e.stopPropagation();
		return true;
	});

	$("#tabWidgetMain .content li").click(function() {
		var check = true;
		if( $("#setTabAutoCloseCheck").attr("checked") == "checked" ){check = false;}
		$("#setTabAutoCloseCheck").attr("checked",check );
		return false;
	});

	// 탭 상단설정 위젯에서 확인 클릭시 이벤트
	$("#tabOk").click(function() {
		// 쿠키에 선택된 값을 저장한다.
		setCookie("setTabAutoClose",$("#setTabAutoCloseCheck").is(':checked')?'true':'false',1000);
		setTabAutoClose = getCookie("setTabAutoClose");

		// 선택된 값에 따라서 아이콘을 설정한다.
		if( setTabAutoClose == "true" ){$(".topTab").addClass("active");} else {$(".topTab").removeClass("active");}

		// 탭 상단설정 위젯을 숨긴다.
		$("#tabCancel").click();
	});

	// 탭 상단설정 위젯에서 취소 클릭시 이벤트
	$("#tabCancel").click(function() {
		$("#tabWidgetMain").hide();
		$(document).unbind("click");
	});

	// 전체사이즈 클릭 이벤트
	$(".topFull").click(function() {
		// 전체사이즈 온일때
		if( $("#wrap").hasClass("full") ) {
			$("#wrap").removeClass("full");
			$(this).removeClass("active");
			setFullSize = "false";

		}
		// 전체사이즈 오프일때
		else {
			$("#wrap").addClass("full");
			$(this).addClass("active");
			setFullSize = "true";
		}
		// 선택된 값을 쿠키에 저장한다.
		setCookie("setFullSize",setFullSize,1000);
	});

	// 유틸메뉴의 좌측메뉴 토클 아이콘 클릭 이벤트
	$(".topBtn1").click(function() {
		// 좌측 메뉴를 보여짐/숨김처리를 한다.
		$(".layout_content").hasClass("menu_close")?$("#left_open").click():$("#left_close").click();
	});

	// 유틸메뉴의 탑메뉴 토클 아이콘 클릭 이벤트
	$(".topBtn2").click(function() {

		if($(".layout_header").css('display') == "none") {
			$(".layout_header").show();
			// 상단 메뉴의 높이값을 조절한다.
			$(".layout_tab").css("top", 114);
			// 상단 아이콘을 선택 안된 상태로 만든다.
			$(".topBtn2").removeClass("active");
		} else {
			$(".layout_header").hide();
			// 상단 메뉴의 높이값을 조절한다.
			$(".layout_tab").css("top", 61);
			// 상단 아이콘을 선택 상태로 만든다.
			$(".topBtn2").addClass("active");
		}

		// 로고를 숨김/보여짐 처리한다.
		checkLogo();
	});

	// 유틸메뉴의 좌탑아이콘 클릭 이벤트
	$(".topBtn3").click(function() {
		// 아이콘이 선택된 상태일때
		if( $(this).hasClass("active") ) {
			// 좌측 메뉴를 나타낸다
			$("#left_open").click();
			// 상단 메뉴를 나타낸다.
			$(".layout_header").show(300);
			// 상단 메뉴를 높이값을 조절한다.
			$(".layout_tab").css("top", 114);
			// 유틸 메뉴 아이콘 선택안된 상태로 만든다.
			$(".topBtn2").removeClass("active");
			// 유틸메뉴의 좌탑아이콘 선택안된 상태로 만든다.
			$(this).removeClass("active");
		}
		// 아이콘이 선택 안된 상태일때
		else {
			$("#left_close").click();
			// 상단 메뉴를 숨긴다.
			$(".layout_header").hide();
			// 상단 메뉴를 높이값을 조절한다.
			$(".layout_tab").css("top", 61);
			// 유틸 메뉴 아이콘 선택 상태로 만든다.
			$(".topBtn2").addClass("active");
			// 유틸메뉴의 좌탑아이콘 선택 상태로 만든다.
			$(this).addClass("active");
		}
		// 로고를 숨김/보여짐 처리한다.
		checkLogo();
	});

	// 나의 메뉴 추가 버튼 클릭 이벤트
	$("#addMyMenu").click(function() {
		// 선택된 탭의 url을 가져온다
		//alert( getIframeUrl() );

		if($("#prgCd").val() == ""){
			alert("마이메뉴에 등록할 수 없는 페이지 입니다.");
			return;
		}

		if($("#srchSeq").val() != "null"){
			alert("일부 메뉴는 마이메뉴가 지원되지 않습니다.");
			return;
		}

		//등록
		if($("#addMyMenu").html().indexOf("-") > 0) {
			var myMenuResult = ajaxCall("/getMymenu.do?status=D",$("#subForm").serialize(),false).result;
			alert(myMenuResult. Message);
			$("#addMyMenu").html(" 나의메뉴 + ");
		} else {
			var myMenuResult = ajaxCall("/getMymenu.do?status=I",$("#subForm").serialize(),false).result;
			alert(myMenuResult. Message);
			$("#addMyMenu").html(" 나의메뉴 - ");
		}
	});

	// 도움말 버튼 클릭 이벤트
	$("#helpPopup").click(function() {
		if(!isPopup()) {return;}

		var args = new Array();
		args["mainMenuCd"]	= menuInfo["mainMenuCd"];
		args["priorMenuCd"]	= menuInfo["priorMenuCd"];
		args["menuCd"]	= menuInfo["menuCd"];
		args["menuSeq"]	= menuInfo["menuSeq"];

		openPopup("/HelpPopup.do?cmd=viewHelpPopup", args, "940","550");
	});
}

// 탭의 기본 속성
var tabTitle = $( "#tab_title" ),
tabContent = $( "#tab_content" ),
tabTemplate = "<li location='#[location]' onedepth='#[onedepth]' mainMenuCd='#[mainMenuCd]' priorMenuCd='#[priorMenuCd]' menuCd='#[menuCd]' menuSeq='#[menuSeq]'><a href='#[href]'>#[label] <span class='btn_close'>&nbsp;</span></a></li>",
tabCounter = 0;

// 탭 생성
function openContent(title,url,location,dataRwType,cnt,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn) {
	tabs.find("li").each(function() {
		if( $(this).attr("onedepth") == "true" ) {
			var panelId = $( this ).remove().attr( "aria-controls" );
			$( "#" + panelId ).remove();
		}
	});

	var duplication = -1;

	// 동일한 iframe 여부 확인
	tabs.find("li").each(function() {
		if( $(this).attr("location") == location ) {
			duplication = $(this).index();
		}
	});

	if( duplication > -1) {
		if( confirm("이미 해당탭이 존재합니다.\n새로고침하시겠습니까?") ) {
			tabRefresh(duplication,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn);
		}
		else {
			tabRefresh(duplication);
		}
		return;
	}

	if( tabs.find("div").length > 5 ) {
		// if( url.indexOf(".html") > -1 ) return;
		if( setTabAutoClose == "true" ) {
			var panelId = tabs.find( "li:first-child" ).remove().attr( "aria-controls" );
			tabRemove(panelId);

			// popupUseYn 값이 있을 경우 패스워드 확인창을 띄움
			if( popupUseYn=="Y" ) {
				if(!isPopup()) {return;}
				openPopup('/Popup.do?cmd=pwConPopup','',461,250, function(rtnValue) {
					if(rtnValue["result"] == "Y") {
						tabCreate(title,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn);
					}
				});
			} else {
				tabCreate(title,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn);
			}
		}
		else {
			if( confirm("동시에 열수 있는 창(TAB)은 6개 입니다.\n\n[  "+tabs.find( "li:first-child" ).text()+"]탭을 종료합니다.\n계속하시겠습니까?") ) {
				var panelId = tabs.find( "li:first-child" ).remove().attr( "aria-controls" );
				tabRemove(panelId);

				// popupUseYn 값이 있을 경우 패스워드 확인창을 띄움
				if( popupUseYn=="Y" ) {
					if(!isPopup()) {return;}
					openPopup('/Popup.do?cmd=pwConPopup','',461,250, function(rtnValue) {
						if(rtnValue["result"] == "Y") {
							tabCreate(title,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn);
						}
					});
				} else {
					tabCreate(title,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn);
				}
			}
		}
		return;
	}

	// popupUseYn 값이 있을 경우 패스워드 확인창을 띄움
	if( popupUseYn=="Y" ) {
		if(!isPopup()) {return;}
		openPopup('/Popup.do?cmd=pwConPopup','',461,250, function(rtnValue) {
			if(rtnValue["result"] == "Y") {
				tabCreate(title,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn);
			}
		});
	} else {
		tabCreate(title,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn);
	}
}

// 탭 리프레쉬
function tabRefresh(index,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn) {
	// 선택된 탭을 활성화 시킴
	tabs.tabs( "option", "active", index );

	// 선택된 탭을 리로드 시킴
	if(url) {
		// 메뉴별 권한 셋팅
		var authPg = "R";
		if(dataPrgType==''){authPg='R';}
		//if( dataPrgType == 'U' ) authPg="A";
		if( dataPrgType == 'U' ){authPg= ssnDataRwType ;} else {authPg = dataRwType;}

		$("#dataRwType").val(dataRwType);
		$("#dataPrgType").val(dataPrgType);
		$("#mainMenuCd").val(mainMenuCd);
		$("#priorMenuCd").val(priorMenuCd);
		$("#menuCd").val(menuCd);
		$("#menuSeq").val(menuSeq);
		$("#prgCd").val(prgCd);
		$("#srchSeq").val(srchSeq);
		$("#popupUseYn").val(popupUseYn);
		$("#helpUseYn").val(helpUseYn);
		$("#myMenu").val(myMenu);
		$("#authPg").val(authPg);
		$("#workflowYn").val(workflowYn||'N');

		submitCall($("#subForm"),tabs.find("iframe:eq("+index+")").attr("name"),"post",url);

		// 도움말 버튼 숨김 여부
		//$("#helpPopup").hide();
		if(menuInfo["menuCd"] != ""){
			var data = ajaxCall("/HelpPopup.do?cmd=getHelpPopupMap"
					,"searchMainMenuCd="	+ menuInfo["mainMenuCd"]
					+ "&searchPriorMenuCd="	+ menuInfo["priorMenuCd"]
					+ "&searchMenuCd="		+ menuInfo["menuCd"]
					+ "&searchMenuSeq="		+ menuInfo["menuSeq"] ,false);
			if(data.map == null){
			} else {
				if ( ssnDataRwType == "A" ) {
					$("#helpPopup").show();
					//$("#helpPopup").hide();
					$("#helpPopup").removeClass("tPink");
					if ( data.map.mgrHelpYn == "Y" ) {
					} else {
						$("#helpPopup").hide();
						$("#helpPopup").addClass("tPink");
					}
				} else {
					if ( data.map.empHelpYn == "Y" ) {
						$("#helpPopup").show();
					}
				}
			}

			if($("#myMenu").val()=="Y"){$("#addMyMenu").html(" 나의메뉴 - ");} else {$("#addMyMenu").html(" 나의메뉴 + ");}

		}
	}
}

// 아이디로 탭 제거
function tabRemove(id) {
	$( "#" + id ).remove();
}

// 새로운 탭을 생성
function tabCreate(title,url,location,dataRwType,dataPrgType,mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd,srchSeq,popupUseYn,helpUseYn,myMenu,workflowYn) {

	// popupUseYn 값이 있을 경우 패스워드 확인창을 띄움
	//if( popupUseYn=="Y" ) {
	//	var popup = openPopup('/Popup.do?cmd=pwConPopup','',461,250);
	//	if( !popup ) return false;
	//}

	var onedepth = "";
	if( url.indexOf(".html") > -1 ){onedepth = "true";}

	var label = title,location = location,

	id = "tabs-" + tabCounter,
	li = $( tabTemplate.replace( /#\[href\]/g, "#" + id ).
			replace( /#\[label\]/g, label ).
			replace( /#\[location\]/g, location ).
			replace( /#\[onedepth\]/g, onedepth ).
			replace( /#\[mainMenuCd\]/g, mainMenuCd ).
			replace( /#\[priorMenuCd\]/g, priorMenuCd ).
			replace( /#\[menuCd\]/g, menuCd ).
			replace( /#\[menuSeq\]/g, menuSeq ) ),
	tabContentHtml = tabContent.val() || "Tab " + tabCounter + " content.";

	tabs.find( ".ui-tabs-nav" ).append( li );
	tabs.append("<div id='" + id + "' class='layout_tabs'><iframe name='iframe_" + id + "' frameborder='0' class='tab_iframes'></iframe></div>");
	tabs.tabs( "refresh" );
	tabs.tabs( "option", "active", tabs.find( "li" ).length-1 );
	tabCounter++;

	// 탭 생성시에 메뉴권한 및 페이지 값을 post로 iframe 내부로 보낸다.
	var authPg = "R";
	if(dataPrgType==''){authPg='R';}
	//if( dataPrgType == 'U' ) authPg="A";
	if( dataPrgType == 'U' ){authPg= ssnDataRwType ;} else {authPg = dataRwType;}

	$("#dataRwType").val(dataRwType);
	$("#dataPrgType").val(dataPrgType);
	$("#mainMenuCd").val(mainMenuCd);
	$("#priorMenuCd").val(priorMenuCd);
	$("#menuCd").val(menuCd);
	$("#menuSeq").val(menuSeq);
	$("#prgCd").val(prgCd);
	$("#srchSeq").val(srchSeq);
	$("#popupUseYn").val(popupUseYn);
	$("#helpUseYn").val(helpUseYn);
	$("#myMenu").val(myMenu);
	$("#authPg").val(authPg);
	$("#workflowYn").val(workflowYn||'N');

	submitCall($("#subForm"),"iframe_"+id,"post",url);
	setIframeHeight();

	if($("#myMenu").val()=="Y"){$("#addMyMenu").html(" 나의메뉴 - ");} else {$("#addMyMenu").html(" 나의메뉴 + ");}




}

//Low Menu생성
function makeLowMenu(lowMenu) {
	var viewMenuStr 	= "";
	var lvl 			= "";
	var mainMenuCd 		= "";
	var priorMenuCd 	= "";
	var menuCd 			= "";
	var menuSeq		 	= "";
	var grpCd 			= "";
	var type			= "";
	var menuNm 			= "";
	var prgCd 			= "";
	var srchSeq 		= "";
	var dataRwType 		= "";
	var dataPrgType 	= "";
	var seq 			= "";
	var cnt 			= "";
	var prgPath 		= "";
	var useYn 			= "";
	var inqSYmd 		= "";
	var inqEYmd 		= "";
	var dateTrackYn 	= "";
	var mainMenuNm 		= "";
	var popupUseYn 		= "";
	var lastSessionUseYn= "";
	var searchUseYn 	= "";
	var helpUseYn 		= "";
	var myMenu 		    = "";

	var ulId1 = "";
	var ulId2 = "";

	var tabInfo = "";

	removeLeftEvent();
	$("#lowMenu").html("");

	for ( var i = 0; i < lowMenu.length; i++) {

		lvl 		= lowMenu[i].lvl;
		mainMenuCd 	= lowMenu[i].mainMenuCd;
		priorMenuCd = lowMenu[i].priorMenuCd;
		menuCd 		= lowMenu[i].menuCd;
		menuSeq 	= lowMenu[i].menuSeq;
		grpCd 		= lowMenu[i].grpCd;
		type	 	= lowMenu[i].type;
		menuNm 		= lowMenu[i].menuNm;
		prgCd 		= lowMenu[i].prgCd;
		srchSeq	    = lowMenu[i].srchSeq;
		dateTrackYn = lowMenu[i].dateTrackYn;
		mainMenuNm 	= lowMenu[i].mainMenuNm;
		popupUseYn 	= lowMenu[i].popupUseYn;
		dataRwType 	= lowMenu[i].dataRwType;
		cnt 		= lowMenu[i].cnt;
		dataPrgType	= lowMenu[i].dataPrgType;
		helpUseYn	= lowMenu[i].helpUseYn;
		myMenu      = lowMenu[i].myMenu;

		// 메뉴생성시에 메뉴권한, 메뉴인덱스등 정보를 셋팅한다.
		var arry = prgCd.split("?pgmTitle=");
		tabInfo = "url='"+prgCd+"' ";
		tabInfo += "dataPrgType='"+dataPrgType+"' ";
		tabInfo += "dataRwType='"+dataRwType+"' ";
		tabInfo += "cnt='"+cnt+"' ";
		tabInfo += "mainMenuCd='"+mainMenuCd+"' ";
		tabInfo += "priorMenuCd='"+priorMenuCd+"' ";
		tabInfo += "menuCd='"+menuCd+"' ";
		tabInfo += "menuSeq='"+menuSeq+"' ";
		tabInfo += "prgCd='"+prgCd+"' ";
		tabInfo += "srchSeq='"+srchSeq+"' ";
		tabInfo += "popupUseYn='"+popupUseYn+"' ";
		tabInfo += "helpUseYn='"+helpUseYn+"' ";
		tabInfo += "myMenu='"+myMenu+"' ";

		// 2뎁스일때 메뉴 생성
		if (lvl == "1" && type == "M" ) {
			ulId1 = "leftMenu_" + menuCd;
			ulname1 = menuNm; //dataPrgType
			$("#lowMenu").append( "<li location='"+mainMenuNm+" &gt; &lt;span&gt;"+ulname1+"&lt;/span&gt;' "+tabInfo+"><span>"+menuNm+"</span><ul id='"+ulId1+"'></ul></li>");
		}
		else if (lvl == "1"  ) {
			ulId1 = "leftMenu_" + menuCd;
			ulname1 = menuNm; //dataPrgType
			$("#lowMenu").append( "<li class='link' location='"+mainMenuNm+" &gt; &lt;span&gt;"+ulname1+"&lt;/span&gt;' "+tabInfo+"><span>"+menuNm+"</span><ul id='"+ulId1+"'></ul></li>");
		}
		// 3뎁스이면서 서브가 있을 경우 메뉴 생성
		else if (lvl == "2" && type == "M") {
			ulId2 = "leftMenu_" + menuCd;
			ulname2 = menuNm;
			$("#" + ulId1).append( "<li location='"+mainMenuNm+" &gt; "+ulname1+" &gt; &lt;span&gt;"+ulname2+"&lt;/span&gt;' "+tabInfo+"><span>"+menuNm+"</span><ul id='"+ulId2+"'></ul></li>");
		}
		// 3뎁스이면서 서브가 없을 경우 메뉴 생성
		else if (lvl == "2") {
			ulname2 = menuNm;
			$("#" + ulId1).append( "<li class='link' location='"+mainMenuNm+" &gt; "+ulname1+" &gt; &lt;span&gt;"+ulname2+"&lt;/span&gt;' "+tabInfo+"><span>"+menuNm+"</span></li>");
		}
		// 4뎁스 메뉴 생성
		else if (lvl == "3") {
			ulname3 = menuNm;
			$("#" + ulId2).append( "<li location='"+mainMenuNm+" &gt; "+ulname1+" &gt; "+ulname2+" &gt; &lt;span&gt;"+ulname3+"&lt;/span&gt;' "+tabInfo+"><span>"+menuNm+"</span></li>");
		}
	}

	//var a="${authPg}";
	addLeftEvent();

	// if( lowMenu.length < 30 ) $("#btnStep3").click();

	if( $(".layout_content").hasClass("menu_close") ){
		$("#left_open").trigger("click");
	}

	/* 총매뉴 갯수에 따라 대,중,소 매뉴까지 모두 펼친다. */
	openMenuCnt != null && openMenuCnt.codeList[0] != null && openMenuCnt.codeList[0].codeNm != "" && parseInt( openMenuCnt.codeList[0].codeNm ) >= lowMenu.length ? setAllMenuMode() : "" ;
}


// 활성화된 tab의 url 가져오기
/*
function getIframeUrl() {
	var currentIndex = parent.tabs.tabs( "option", "active");
	return tabs.find("iframe:eq("+currentIndex+")").attr("page");

}
*/
// 탭 높이 변경
function setIframeHeight() {
	var iframeTop = $("#tabs ul").height() + 16;
	$(".layout_tabs").each(function() {
		$(this).css("top",iframeTop);
	});

	$(".layout_content").hasClass("menu_close")?$(".topBtn1").addClass("active"):$(".topBtn1").removeClass("active");
}

// 로고 숨김처리
function checkLogo() {
	if( $(".layout_header").css('display') == "none" && $(".layout_content").hasClass("menu_close")) {
		$("#main_logo").hide();
		$(".topBtn3").addClass("active");
	}
	else {
		$("#main_logo").show();
		$(".topBtn3").removeClass("active");
	}
	setIframeHeight();
}

// 좌측 유틸메뉴 이벤트 설정
function setLeftMenu() {

	// 좌측 마이메뉴 클릭시 이벤트
	$("#btnMyMenu").click(function() {
		//마이메뉴 리스트
		myMenuList();

		$("#majorMenu>li").each(function() {
			$(this).removeClass("on");
		});

		// 서브메뉴 리스트를 숨긴다.
		$("#subMenu").hide();

		// 마이메뉴 리스트를 보여준다.
		$("#myMenu").show();

		// 1뎁스 좌측 아이콘을 마이메뉴 아이콘으로 설정한다.
		$("#left_icon").attr("class","");
		$("#left_icon").addClass("pos99");

		// 좌측 메뉴 스크롤을 재설정한다.
		initLeftScroll();
	});

	$("#btnPlus").click(function() {
		if( $("#subMenu").css('display') == "none" ) {
			return;
		}

		$(this).toggleClass("minus");
		$(this).hasClass("minus")?$("#btnStep3").click():$("#btnStep1").click();
	});

	$("#mainMenu li").click(function() {
		$("#mainMenu li").each(function() {
			$(this).removeClass("on");
		});
		$(this).addClass("on");

		// 좌측 메뉴 스크롤을 재설정한다.
		initLeftScroll();
	});

	// 좌측 유틸메뉴의 - 클릭 이벤트
	$("#btnStep1").click(function() {
		if( $("#subMenu").css('display') == "none" ) {
			return;
		}

		$("#subMenu>ul>li").each(function() {
			$(this).removeClass("on");
		});
		$("#subMenu li:has(li)").each(function() {
			$(this).removeClass("on");
		});

		// 좌측 메뉴 스크롤을 재설정한다.
		initLeftScroll();
	});

	// 좌측 유틸메뉴의 = 클릭 이벤트
	$("#btnStep2").click(function() {
		if( $("#subMenu").css('display') == "none" ) {
			return;
		}

		$("#subMenu li:has(li)").each(function() {
			$(this).removeClass("on");
		});

		$("#subMenu>ul>li").each(function() {
			$(this).addClass("on");
		});

		// 좌측 메뉴 스크롤을 재설정한다.
		initLeftScroll();
	});

	// 좌측 유틸메뉴의 Ξ 클릭 이벤트
	$("#btnStep3").click(function() {
		if( $("#subMenu").css('display') == "none" ) {
			return;
		}

		$("#subMenu li:has(li)").each(function() {
			$(this).addClass("on");
		});

		// 좌측 메뉴 스크롤을 재설정한다.
		initLeftScroll();
	});

	// 좌측 메뉴 닫기 클릭 이벤트
	$("#left_close").click(function() {
		$("#btnMyMenu").hide("fast");
		$("#left_menu").hide("fast");
		$("#left_open").show();
		$(".layout_content").addClass("menu_close");
		$(".topBtn1").addClass("active");
		checkLogo();
	});

	// 좌측 메뉴 열기 클릭 이벤트
	$("#left_open").click(function() {
		$("#btnMyMenu").show("fast");
		$("#left_menu").show("fast");
		$("#left_open").hide();
		$(".layout_content").removeClass("menu_close");
		$(".topBtn1").removeClass("active");
		checkLogo();
	});
}

// 좌측 네비 이벤트 설정
function addLeftEvent() {
	// 2뎁스 메뉴
	$("#subMenu li").click(function(event) {
		event.stopPropagation();

		var li;
		$("#subMenu>ul>li li:not(:has(li))").each(function() {
			$(this).removeClass("on");
			$(this).removeClass("linkon");
		});

		if( $(this).find("li:not(:has(li))").length == 0 ) {
			if( $(this).attr("url") != undefined ) {
				openContent(
						$(this).text()
						,$(this).attr("url")
						,$(this).attr("location")
						,$(this).attr("dataRwType")
						,$(this).attr("cnt")
						,$(this).attr("dataPrgType")
						,$(this).attr("mainMenuCd")
						,$(this).attr("priorMenuCd")
						,$(this).attr("menuCd")
						,$(this).attr("menuSeq")
						,$(this).attr("prgCd")
						,$(this).attr("srchSeq")
						,$(this).attr("popupUseYn")
						,$(this).attr("helpUseYn")
						,$(this).attr("myMenu")
						);
			}
		}

		// 닫을때
		else if( $(this).hasClass("on") ) {}
		// 서브가 있을때

		/* 메뉴 클릭시 자동으로 열리게 하는 부분 시작 */
/*
		else if( $(this).find("li:first").attr("url") == "" ) {
			$(this).find("li:first").addClass("on");
			$(this).find("li:first").find("li:first").addClass("linkon");
			li = $(this).find("li:first").find("li:first");

			openContent(
					li.text()
					,li.attr("url")
					,li.attr("location")
					,li.attr("dataRwType")
					,li.attr("cnt")
					,li.attr("dataPrgType")
					,li.attr("mainMenuCd")
					,li.attr("priorMenuCd")
					,li.attr("menuCd")
					,li.attr("menuSeq")
					,li.attr("prgCd")
					,li.attr("srchSeq")
					,li.attr("popupUseYn")
					,li.attr("helpUseYn")
					,li.attr("myMenu")
					);
		}
		// 마지막일때
		else {
			$(this).find("li:first").addClass("linkon");
			li = $(this).find("li:first");
			openContent(
					li.text()
					,li.attr("url")
					,li.attr("location")
					,li.attr("dataRwType")
					,li.attr("cnt")
					,li.attr("dataPrgType")
					,li.attr("mainMenuCd")
					,li.attr("priorMenuCd")
					,li.attr("menuCd")
					,li.attr("menuSeq")
					,li.attr("prgCd")
					,li.attr("srchSeq")
					,li.attr("popupUseYn")
					,li.attr("helpUseYn")
					,li.attr("myMenu")
					);
		}
*/
		/* 메뉴 클릭시 자동으로 열리게 하는 부분 종료 */

		if( $(this).hasClass("link") ) {
			$(this).hasClass("linkon")?$(this).removeClass("linkon"):$(this).addClass("linkon");
		}
		else {
			$(this).hasClass("on")?$(this).removeClass("on"):$(this).addClass("on");
		}
		// 좌측 메뉴 스크롤을 재설정한다.
		initLeftScroll();
	});
	// 좌측 메뉴 스크롤을 재설정한다.
	initLeftScroll();
}

// 좌측 네비 이벤트 제거
function removeLeftEvent() {
	$("#subMenu li").unbind("click");

}

// 화면 리사이즈 설정
function subResize() {

	if( !full ) {
		if( $(window).width() >= 1240 ){$("#wrap").removeClass("min");} else {$("#wrap").addClass("min");}

		$(".scroll-pane").height(1);
		$(".scroll-pane").height($(".layout_content").height()-144-46);
	}
	setIframeHeight();

	// 좌측 메뉴 스크롤을 재설정한다.
	initLeftScroll();
}


// 좌측 스크롤 재생성
function initLeftScroll() {
	var api = $(".scroll-pane").data('jsp');
	api.reinitialise();
}



function goPOPUP(url,name) {
	// scrollbars = yes	:: 스크롤바 사용 (미사용 no)
	// resizeable = yes :: 좌우스크롤바 사용 (미사용 no)
	// menubar = yes    :: 메뉴바 사용 (미사용 no)
	// toolbar = yes	:: 툴바사용 (미사용 no)
	// width = 100     	:: 팝업창의 가로사이즈
	// height = 100   	:: 팝업창의 세로사이즈
	// left = 10       	:: 좌측에서 10픽셀을 띄운다.
	// top = 10       	:: 상단에서 10픽셀을 띄운다.
	window.open(url,name,"location=0,scrollbars=1,resizable=1,menubar=0,toolbar=0,width=1300,height=900,left=0,top=0");
}

//Major Menu click
//s: param.menuSeq
//p: param.prgCd
function majorMenuAction(s, p){
	$("#majorMenu>li:not(#sampleMenu,#IBSheetMenu)").click(function(){
		// 좌측 메뉴 리스트를 보여준다
		$("#subMenu").show();

		// 마이 메뉴 리스트를 숨겨준다.
		$("#myMenu").hide();

		//Low Menu 조회 및 생성
		$("#majorMenu>li").removeClass("on");
		$(this).addClass("on");

		var mainMenuCd = $(this).attr("mainMenuCd") ;

		// 1뎁스 좌측 아이콘 설정
		$("#left_icon").attr("class","");
		$("#left_icon").addClass("pos"+mainMenuCd);

		//console.log("createLowMenu>>"+ "서브메뉴 이벤트 등록 ");
		// 서브 메뉴 이벤트를 등록
		createLowMenu();

		//console.log("simpleLowMenu>>"+ "워크플로우 등록 ");
		//워크플로우, 커뮤니트를 등록
		//simpleLowMenu();

		// 좌측 메뉴 스크롤을 재설정한다.
		initLeftScroll();

		// mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd로 서브메뉴 열기

		// 메뉴별 설명 페이지를 호출한다.

		//obj arr = new Object();
/*
		var mStr = replaceAll(m,"=",":");
		mStr = replaceAll(mStr, ":", "\":\"");
		mStr = replaceAll(mStr, ", ", "\", \"");
		mStr = replaceAll(mStr, "{", "{\"");
		mStr = replaceAll(mStr, "}", "\"}");

		var mStrObj = eval("("+ mStr+")");
*/
		var  prgCd = "";
		if( p != ""){
			//param.menuSeq, param.prgCd 넘김
			prgCd = getDirectMenuMap(s,p);
		}
		//alert(m);
		if(prgCd != ""){
			eval(prgCd);
		}
		else{
			openContent(
					$(this).text(),
					"/html/contents/base"+mainMenuCd+".html",
					"<span>"+$(this).text()+"</span>"
					);
		}

	});
}



// mainMenuCd 로 열기
function openMainMenuCd(mainMenuCd) {
	$("#majorMenu li").each(function(){
		if( $(this).attr("mainMenuCd") == mainMenuCd ){
			$(this).click();
		}
	});
}

function getDirectMenuMap(menuSeq,prgCd){
	var str = "menuSeq="+menuSeq+"&prgCd="+prgCd ;
	var result = ajaxCall("/getDirectSubMenu.do",str,false).map;
	return(result.jsParam); //JS_PARAM
}

// mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd로 서브메뉴 열기
function openSubMenuCd(mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd) {
	//console.log("openSubMenuCd()"+mainMenuCd+" : "+priorMenuCd+" : "+menuCd+" : "+menuSeq+" : "+prgCd);
	$("#subMenu>ul>li li:not(:has(li))").each(function() {
	//$("#subMenu>ul>li").each(function() {
		$(this).removeClass("on");
		$(this).removeClass("linkon");
	});

//	$("#subMenu>ul>li li:not(:has(li))").each(function() {
//		console.log( $(this).text());
	// 2뎁스 이후부터 열리도록 수정
	$("#subMenu>ul>li, #subMenu>ul>li li").each(function() {
		if(
			$(this).attr("mainMenuCd") == mainMenuCd &&
			$(this).attr("priorMenuCd") == priorMenuCd &&
			$(this).attr("menuCd") == menuCd &&
			$(this).attr("menuSeq") == menuSeq &&
			$(this).attr("prgCd") == prgCd
			) {

			// 메뉴가 2뎁스 일때 하위메뉴 화면 열리지 않도록 수정
			if( $(this).parent().parent().attr("id") != "subMenu"){$(this).addClass("on");}

			$(this).addClass("linkon");
			$(this).parent().parent().addClass("on");
			$(this).parent().parent().parent().parent().addClass("on");
			$(this).click();

			// 좌측 메뉴 스크롤을 재설정한다.
			initLeftScroll();
			return;
		}
	});
}

// 마이메뉴 이벤트 제거
function removeMymenuEvent() {
	$("#myMenu li").each(function() {
		$(this).unbind("click");
	});
}

// 마이메뉴 이벤트 추가
function addMymenuEvent() {
	$("#myMenu li").each(function() {
		// 리스트 클릭 이벤트
		$(this).click(function() {
			$("#myMenu li").each(function() {
				$(this).removeClass("on");
			});
			openContent(
					$(this).text()
					,$(this).attr("url")
					,$(this).attr("location")
					,$(this).attr("dataRwType")
					,$(this).attr("cnt")
					,$(this).attr("dataPrgType")
					,$(this).attr("mainMenuCd")
					,$(this).attr("priorMenuCd")
					,$(this).attr("menuCd")
					,$(this).attr("menuSeq")
					,$(this).attr("prgCd")
					,$(this).attr("srchSeq")
					,$(this).attr("popupUseYn")
					,$(this).attr("helpUseYn")
					,$(this).attr("myMenu")
					);
		});

		// 닫기 클릭 이벤트
		$(this).find(".close").click(function() {
			$(this).unbind("click");
			$(this).parent().unbind("click");
			$(this).parent().remove();
			return false;
		});
	});
}

// form 생성
function createAuthForm() {
	$("<form></form>",{id:"subForm",name:"subForm",method:"post"}).appendTo('body');
	createInput("subForm","mainMenuCd");
	createInput("subForm","grpCd");
	createInput("subForm","sDataRwType");
	createInput("subForm","dataRwType");
	createInput("subForm","dataPrgType");
	createInput("subForm","priorMenuCd");
	createInput("subForm","menuCd");
	createInput("subForm","menuSeq");
	createInput("subForm","prgCd");
	createInput("subForm","srchSeq");
	createInput("subForm","popupUseYn");
	createInput("subForm","helpUseYn");
	createInput("subForm","myMenu");
	createInput("subForm","authPg");
	createInput("subForm","workflowYn");
}

//input 생성
function createInput(form,id) {
	$("<input></input>",{id:id,name:id,method:"post",type:"hidden"}).appendTo($("#"+form));
}


//권한 설정 변경
function setLevelWidget() {
	// 권한 레이어 클릭시 이벤트 막기
	$("#levelWidgetMain").click(function() {return false;});
	// 권한 설정 클릭 이벤트
	$("#authMgr").click(function(){$(document).click();$("#levelWidgetMain").show();$(document).click(function() { $("#levelCancel").click(); });return false;});
	// 권한 설정 닫기 클릭 이벤트
	$("#levelCancel").click(function(){$(document).unbind("click");$("#levelWidgetMain").hide();return false;});
}


// 권한 조회 및 변경
function createAuthList(gCd){
	// 권한 조회
//	var authList = ajaxCall("/getCollectAuthGroupList.do", "",false).result;
	 $.ajax({
			url 		: "getCollectAuthGroupList.do",
			type 		: "post", dataType 	: "json", async 		: true, data 		: "",
			success : function(rv) {
				var authList = rv.result;
				var grpCd		= "";
				var grpNm		= "";
				var dataRwType	= "";
				var searchType	= "";
				var viewAuthStr	= "";
				var className	= "";
				//권한 조회 및 초기화
				$("#authList").html("");
				for ( var i = 0; i < authList.length; i++) {
					grpCd		= authList[i].ssnGrpCd;
					grpNm		= authList[i].ssnGrpNm;
					dataRwType 	= authList[i].ssnDataRwType;
					searchType 	= authList[i].ssnSearchType;
					if( grpCd == gCd ){className = "on";} else {className = "";
					//${ssnGrpCd} grpCd
}

					viewAuthStr +="<li grpCd="+grpCd+" grpNm="+grpNm+" dataRwType="+dataRwType+" searchType="+searchType+" class="+className+" ><span>"+ grpNm +"</span></li>";
				}

				$("#authList").append(viewAuthStr);

				// 권한 변경리스트 클릭  이벤트
				$("#authList li").click(function() {
					$("#subGrpCd").val($(this).attr("grpCd"));
					$("#subGrpNm").val($(this).attr("grpNm"));
					$("#subDataRwType").val($(this).attr("dataRwType"));
					$("#subSearchType").val($(this).attr("searchType"));

					ajaxCall("/ChangeSession.do",$("#ssnChangeForm").serialize(),false);
					//location.href = "/";
					redirect("/", "_top");
				});

			},
			error : function(jqXHR, ajaxSettings, thrownError) {
				ajaxJsonErrorAlert(jqXHR, ajaxSettings, thrownError);
			}
		});
}


// layout Header / 홈으로/권한설정/로그아웃 버튼설정/ 샘플
function layoutHeader(){
	// 홈으로 버튼 클릭 이벤트
	$("#goHome").click(function(){
		submitCall($("#subForm"),"","post","/Main.do");
	});

	// 회사 로고 클릭 이벤트
	$("#main_logo img").click(function(){
		submitCall($("#subForm"),"","post","/Main.do");
	});




	// 로그아웃 클릭 이벤트
	$("#logout").click(function(){
		//submitCall($("#subForm"),"","post","/logoutUser.do");
		var url = "/logoutUser.do";
		//$(location).attr('href',url);
		redirect(url, "_top");


	});

	// Camel변환 클릭 이벤트
	$("#camelSample").click(function(){
		goPOPUP("/Sample.do?cmd=viewSampleConvCamel","CamelCase변환");
	});

	// Guide 샘플 클릭 이벤트
	$("#guideSample").click(function(){
		goPOPUP("/Sample.do?cmd=viewSampleTab&authPg=A","guideSample");
	});

	// IBSheet 샘플 클릭 이벤트
	$("#ibsheetSample").click(function(){
		goPOPUP("/ibSample/index.html","ibsheetSample");
	});
}

// 스킨 설정
function setSkinWidget(t,f) {

	var cTint= 0;
	var cFint= 0;

	if(t=="theme1"){cTint=0;}
	if(t=="theme2"){cTint=1;}
	if(t=="theme3"){cTint=2;}
	if(t=="theme4"){cTint=3;}

	if(f=="dotum"){cFint=0;}
	if(f=="nanum"){cFint=1;}


	var currentSkin = cTint;
	var currentFont = cFint;

	// 테마 설정 레이어 이벤트 막기
	$("#themeWidgetMain").click(function(){
		return false;
	});

	// 테마 설정 아이콘 클릭 이벤트
	$("#themeWidget").click(function() {
		$(document).click();
		// 활성화 된 테마를 설정한다.
		$("#themeWidgetMain div.theme li").each(function() {
			if( $(this).index() == currentSkin ){$(this).find("div").addClass("on");} else {$(this).find("div").removeClass("on");}
		});

		$("#themeWidgetMain div.font input").each(function() {
			if( $(this).parent().index() == currentFont ){$(this).attr("checked",true);} else {$(this).attr("checked",false);}
		});

		// 테마 설정 레이어 표시
		$("#themeWidgetMain").show();
		$(document).click(function() { $("#themeCancel").click(); });
		return false;
	});

	// 테마 리스트 클릭시 이벤트
	$("#themeWidgetMain div.theme li").click(function() {
		// 테마 리스트의 체크박스를 선택안된 상태로 만든다.
		$("#themeWidgetMain div.theme div").each(function() {
			$(this).removeClass("on");
		});
		// 선택된 테마의 체크박스만 체크 상태로 만든다.
		$(this).find("div").addClass("on");
	});

	// 폰트 리스트 클릭 이벤트
	$("#themeWidgetMain div.font li").click(function() {
		// 폰트 리스트의 체크박스를 선택안된 상태로 만든다.
		$("#themeWidgetMain div.font input").each(function() {
			$(this).attr("checked",false);
		});

		// 선택된 폰트의 체크박스만 체크 상태로 만든다.
		$(this).find("input").attr("checked",true);
		return false;
	});

	$("#themeWidgetMain div.font input").click(function(e) {
		e.stopPropagation();
		$("#themeWidgetMain div.font li:eq(" + $(this).parent().index() + ")").click();
	});

	// 테마 설정 레이어의 확인 클릭시 이벤트
	$("#themeOk").click(function() {
		var valueChanged = false;
		var selectTheme = "";
		var selectFont = "";
		// 현재 선택된 테마를 저장한다.
		$("#themeWidgetMain .theme li").each(function() {
			if( $(this).find("div").hasClass("on") ) {
				if( currentSkin != $(this).index() ){valueChanged = true;}
				currentSkin = $(this).index();
				selectTheme = $(this).attr("theme");
			}
		});

		// 현재 선택된 폰트를 저장한다.
		$("#themeWidgetMain .font li").each(function() {
			if( $(this).find("input").attr("checked") ) {
				if( currentFont != $(this).index() ){valueChanged = true;}
				currentFont = $(this).index();
				selectFont = $(this).find("input").val();
			}
		});

		// 스킨이나 폰트 값이 변경되었을 경우 변수에 저장후 home으로 이동
		if( valueChanged ) {
			//값 호출 작업 ..

			$("#subThemeType").val(selectTheme);
			$("#subFontType").val(selectFont);

			//저장
			ajaxCall("/UserMgr.do?cmd=userTheme",$("#ssnChangeForm").serialize(),false);
			//submitCall($("#subForm"),"","post","${ctx}/Main.do");
			//location.href = "/";
			redirect("/", "_top");

		}
		$("#themeCancel").click();
	});

	// 테마 설정 레이어의 취소 클릭시 이벤트
	$("#themeCancel").click(function() {
		// 테마 설정 레이어를 숨겨준다.
		$("#themeWidgetMain").hide();
		$(document).unbind("click");
	});
}

// Major Menu 조회 및 생성

function createMajorMenu(){
	// 대분류 메뉴 조회
	var majorMenu = ajaxCall("/getMainMajorMenuList.do", "",false).result;
	var mainMenuCd = "";
	var grpCd = "";
	var mainMenuNm = "";
	var viewMenuStr = "";
	var workflowMenuStr = "";
	//대분류 메뉴 초기화
	$("#majorMenu").html("");
	//워크플로  메뉴  초기화
	$(".others_menu_wrap>ul").html("");
	for ( var i = 0; i < majorMenu.length; i++) {
		mainMenuCd 	= majorMenu[i].mainMenuCd;
		grpCd 		= majorMenu[i].grpCd;
		mainMenuNm 	= majorMenu[i].mainMenuNm;
		//viewMenuStr +="<li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+" ><span>"+ mainMenuNm +"</span></li>";
		if(mainMenuCd =="20" ){
			viewMenuStr  +="<br><br><br><br><br><br><li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+" class='ac community'><span>"+ mainMenuNm +"</span></li>";
		}else if(mainMenuCd =="21" ){
			viewMenuStr  +="<li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+"  class='ac workflow'><span>"+ mainMenuNm +"</span></li>";
		}else{
			viewMenuStr +="<li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+"><span>"+ mainMenuNm +"</span></li>";
		}

	}

	$("#majorMenu").append(viewMenuStr);
	//워크플로  메뉴  초기화
	//$(".others_menu_wrap>ul").append(workflowMenuStr);
}

//Low Menu 조회 및 생성
function createLowMenu(){
	var mainMenuCd 	= $("#majorMenu>li.on").attr("mainMenuCd");
	var grpCd 		= $("#majorMenu>li.on").attr("grpCd");

	if(mainMenuCd == $("#mainMenuCd").val() && grpCd == $("#grpCd").val() && $("#workflowYn").val() == "N") {
		return;
	}

	$("#mainMenuCd").val( $("#majorMenu>li.on").attr("mainMenuCd") );
	$("#grpCd").val( $("#majorMenu>li.on").attr("grpCd") );

	var lowMenu = ajaxCall("/getSubLowMenuList.do", $("#subForm").serialize(),false).result;
	makeLowMenu(lowMenu);
}


//Low Menu 조회 및 생성
function simpleLowMenu(){
/*
	console.log("Low Menu 조회 및 생성>>"+ $(".others_menu_wrap>ul>li.on").length);

	var mainMenuCd 	= $(".others_menu_wrap>ul>li.on").attr("mainMenuCd");
	var grpCd 		= $(".others_menu_wrap>ul>li.on").attr("grpCd");

	if(mainMenuCd == $("#mainMenuCd").val() && grpCd == $("#grpCd").val()) return;
	$("#mainMenuCd").val( $(".others_menu_wrap>ul>li.on").attr("mainMenuCd") );
	$("#grpCd").val( $(".others_menu_wrap>ul>li.on").attr("grpCd") );

	var lowMenu = ajaxCall("/getSubLowMenuList.do", $("#subForm").serialize(),false).result;
	makeLowMenu(lowMenu);
*/
}


//마이메뉴 List
function myMenuList(){
	var list = ajaxCall("/getMyMenuList.do","",false).result;
	var tmp = "<li #LOCATION# #MYMENU# ><span>#MENUNM#</span><span class='close' menuSeq='#MENUSEQ#' prgCd='#PRGCD#'>&nbsp;</span></li>";
	var str = "";
	var tabInfo = "";
	$("#myMenu ul").empty(); //클리어

	for(var i=0; i<list.length; i++){

		mainMenuCd 	= list[i].mainMenuCd;
		priorMenuCd = list[i].priorMenuCd;
		menuCd 		= list[i].menuCd;
		menuSeq 	= list[i].menuSeq;
		grpCd 		= list[i].grpCd;
		type	 	= list[i].type;
		menuNm 		= list[i].menuNm;
		prgCd 		= list[i].prgCd;
		srchSeq	    = list[i].srchSeq;
		dateTrackYn = list[i].dateTrackYn;
		mainMenuNm 	= list[i].mainMenuNm;
		popupUseYn 	= list[i].popupUseYn;
		dataRwType 	= list[i].dataRwType;
		dataPrgType	= list[i].dataPrgType;
		helpUseYn	= list[i].helpUseYn;
		myMenu      = list[i].myMenu;

		// 메뉴생성시에 메뉴권한, 메뉴인덱스등 정보를 셋팅한다.
		tabInfo = "url='"+prgCd+"' ";
		tabInfo += "dataPrgType='"+dataPrgType+"' ";
		tabInfo += "dataRwType='"+dataRwType+"' ";
		tabInfo += "mainMenuCd='"+mainMenuCd+"' ";
		tabInfo += "priorMenuCd='"+priorMenuCd+"' ";
		tabInfo += "menuCd='"+menuCd+"' ";
		tabInfo += "menuSeq='"+menuSeq+"' ";
		tabInfo += "prgCd='"+prgCd+"' ";
		tabInfo += "srchSeq='"+srchSeq+"' ";
		tabInfo += "popupUseYn='"+popupUseYn+"' ";
		tabInfo += "helpUseYn='"+helpUseYn+"' ";
		tabInfo += "myMenu='"+myMenu+"' ";



		str += tmp.replace(/#MENUNM#/g,list[i].menuNm)
				.replace(/#MYMENU#/g,tabInfo)
				.replace(/#LOCATION#/g, "location='"+mainMenuNm+" &gt; &lt;span&gt;"+menuNm+"&lt;/span&gt;'")
				.replace(/#MENUSEQ#/g,list[i].menuSeq)
				.replace(/#PRGCD#/g,list[i].prgCd);
	}

	$("#myMenu ul").append(str);

	$("#myMenu ul li .close").click(function(event) {

		var str = "menuSeq="+$(this).attr("menuSeq")+"&prgCd="+$(this).attr("prgCd") ;
		var myMenuResult = ajaxCall("/getMymenu.do?status=D",str,false).result;

		$(this).parent().remove();
		return false;
	});
	$("#myMenu ul li").click(function(event) {
		/*
		if( $(this).attr("prgCd") != undefined ) {
			goSubPage(	 $(this).attr("mainMenuCd")
						,"","",""
						,$(this).attr("prgCd"));
		}
		*/
			openContent(
					$(this).text()
					,$(this).attr("url")
					,$(this).attr("location")
					,$(this).attr("dataRwType")
					,$(this).attr("cnt")
					,$(this).attr("dataPrgType")
					,$(this).attr("mainMenuCd")
					,$(this).attr("priorMenuCd")
					,$(this).attr("menuCd")
					,$(this).attr("menuSeq")
					,$(this).attr("prgCd")
					,$(this).attr("srchSeq")
					,$(this).attr("popupUseYn")
					,$(this).attr("helpUseYn")
					,$(this).attr("myMenu")
					);


	});


	//alert(myMenuList);


}




//해당 Left대매뉴 안의 중,소매뉴를 모두 펼침
function setAllMenuMode() {
	if( $("#subMenu").css('display') == "none" ) {
		return;
	}
	$("#btnPlus").addClass("minus");
	$("#btnPlus").hasClass("minus")?$("#btnStep3").click():$("#btnStep1").click();
}


//mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd로 서브메뉴 열기
function openTabMenuCd(mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd) {

	var str = "mainMenuCd="+ mainMenuCd + "&menuSeq="+menuSeq+"&prgCd="+prgCd ;
	var result = ajaxCall("/tabSubDirectMap.do",str,false).map;

	if(result==null){
		alert("해당메뉴에 대한 조회 권한이 없습니다.");
		return;
	}

	openContent(
			result.menuNm
			,result.prgCd
			,"워크플로 &gt; &lt;span&gt;"+result.menuNm+"&lt;/span&gt;"
			,result.dataRwType
			,result.cnt
			,result.dataPrgType
			,result.mainMenuCd
			,result.priorMenuCd
			,result.menuCd
			,result.menuSeq
			,result.prgCd
			,result.srchSeq
			,result.popupUseYn
			,result.helpUseYn
			,result.myMenu
			);

}