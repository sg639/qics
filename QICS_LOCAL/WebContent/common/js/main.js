	// 화면 리사이즈
	function indexResize() {
		if( $(window).width() >= 1240 ) $("#index_wrap").addClass("on");
		else $("#index_wrap").removeClass("on");

		$("#left_menu").height($("#container").height());
	}

	// 좌측 메뉴 설정
	function setLeftMenu() {
		$(".navi li").mouseover(function() {

			$("#lefticon").attr("class","");
			$("#lefticon").css("top",$(this).position().top+2);
			$("#lefticon").addClass("bg"+$(this).attr("mainMenuCd"));
			$("#lefticon").show();
			stopTimeout();
		});
		$(".navi li").mouseout(function() {
			//$("#lefticon").hide();
			startTimeout();
		});

		$(".password a").click(function() {
			if(!isPopup()) {return;}

			openPopup('/Popup.do?cmd=pwChPopup','',461,250);
		});
	}

	// 메뉴 아이콘 깜빡거림 방지
	var timeout;
	function startTimeout() {
		stopTimeout();
		timeout = setTimeout(hideIcon, 10);
	}

	function stopTimeout() {
		clearTimeout(timeout);
	}

	function hideIcon() {
		$("#lefticon").hide();
	}

	// 달력 설정
	function setCalendarWidget() {
		$("#calendarWidget").click(function() {
			$("#mainCalendar").show();
			indexResize();
		});

		initCalendar();
		setCalendarDay();
	}

	var _today = new Date();
	var _current = new Date();
	// 달력 기본설정
	function initCalendar() {
		$("#mainCalendar .close").click(function() {
			$("#mainCalendar").hide();
			indexResize();
		});

		$("#mainCalendar .left").click(function() {
			_current = new Date(_current.getFullYear(),parseInt(_current.getMonth()-1),1);
			setCalendarDay();
		});

		$("#mainCalendar .right").click(function() {
			_current = new Date(_current.getFullYear(),parseInt(_current.getMonth()+1),1);
			setCalendarDay();
		});


		$("#mainCalendar .btn_today").click(function() {
			_current = _today;
			setCalendarDay();
		});

		$("#detailClose").click(function() {
			$("#calendarDetail").hide();
		});
	}

	// 달력 날짜 설정
	function setCalendarDay() {

		var month = _current.getMonth()+1;
		var day = _current.getDate();

		month = (month < 10)  ? "0" + month : month  ;

		day = (day < 10)  ?  "0" +day : day;

		$("#mainDate").val( _current.getFullYear() +""+ month +""+ day);

		//var getDay = ajaxCall("/getScheduleDay.do",$("#mainForm").serialize(),false).DATA;

		$.ajax({
			url 		: "/getScheduleDay.do",
			type 		: "post", dataType 	: "json", async 		: true, data 		: $("#mainForm").serialize(),
			success : function(rv) {
				var getDay = rv.DATA;

				var sortData =[];
				$.each(getDay, function(key, val){sortData.push( val.day );});

				var _dayAry = ["일","월","화","수","목","금","토"];
				var _day = _current.getDay();

				$("#calendar_year").text( _current.getFullYear() );
				$("#calendar_month").html((_current.getMonth()+1));
				$("#calendar_day").html("<b>" + _current.getDate() + "</b> 일 (" + _dayAry[_current.getDay()] + ")");

				$("#calendarDay").empty();

				var _date = new Date(_current.getFullYear(),_current.getMonth());
				var _month = _current.getMonth();
				var _class = "";
				for( var i=1;i<=31;i++) {
					_class = "";
					_date.setDate(i);

					if( _date.getFullYear()+""+_date.getMonth()+""+_date.getDate() == _today.getFullYear()+""+_today.getMonth()+""+_today.getDate() ) _class = "today";
					else if( _date.getDay() == 0 ) _class = "sun";
					else if( _date.getDay() == 6 ) _class = "sat";

					//if( _date.getDay() == 2 ) _class += " schedule";
					if(in_array((i < 10)  ?  "0" +i : i, sortData ))_class += " schedule";

					if( _month != _date.getMonth()) continue;
					$('<li></li>',{
						id: 'day'+i,
						"class": _class
					}).html("<span day='"+_date.getDay()+"'>"+i+"</span>").appendTo($("#calendarDay"));


				}

				$("#mainCalendar #calendarDay li").click(function() {
					$("#scheduleDetail").hide();
					$("#mainDate").val( _current.getFullYear() +""+ month +""+ (($(this).text() < 10)  ?  "0" +$(this).text() : $(this).text()) );

					var getOneDay = ajaxCall("/getMainCalendarMap.do",$("#mainForm").serialize(),false).list;

					var strDiv = "";
					var strDiv1 = "";
					$("#detailOption0").html("");
					$("#detailOption1").html("");
					$("#detailOption2").html("");
					for( var i = 0 ; i < getOneDay.length ; i++ ) {
						var strTitle = "";

						strTitle = getOneDay[i].title;

						if(getOneDay[i].type == 1 &&getOneDay[i].rk==1){
							$("#detailOption"+getOneDay[i].type).html("<p>"+strTitle+"" +
									"<span>"+getOneDay[i].period+"</span>" +
									"<span class='hide'>" + getOneDay[i].memo + "</span>"+
								  "</p>" +
								  "<div id='dp"+getOneDay[i].type+"Btn' style='padding:0 3px 0 3px; margin:11px 0 0 0; width:153px; background:#667780; border-style:none;'></div>");
							//strDiv1 += "<p>"+getOneDay[i].title+"<span>"+getOneDay[i].period+"</span></p>";
						} else if(getOneDay[i].type != 1 &&getOneDay[i].rk==1) {

							$("#detailOption"+getOneDay[i].type).html("<p><a href='#' id='sch_" + getOneDay[i].type + "_"+ getOneDay[i].rk + "' onclick='openDetail(this.id)'>"+strTitle+"</a>" +
									"<span>"+getOneDay[i].period+"</span>" +
									"<span class='hide'>" + getOneDay[i].memo + "</span>"+
								  "</p>" +
								  "<div id='dp"+getOneDay[i].type+"Btn' style='padding:0 3px 0 3px; margin:11px 0 0 0; width:153px; background:#667780; border-style:none;'></div>");
						}

						if(getOneDay[i].type == 0 && getOneDay[i].rk !=1){
							//$("#detailOption"+getOneDay[i].type + " div").html("<p>"+getOneDay[i].title+"<span>"+getOneDay[i].period+"</span></p><div></div>");
							strDiv1 += "<p style='color:#fff; border-width:1px; border-color:silver; border-top-style:dotted; border-right-style:none; border-left-style:none; border-bottom-style:none; font-weight:bold !important'>" +
									   "<a href='#' id='sch_" + getOneDay[i].type + "_"+ getOneDay[i].rk + "' onclick='openDetail(this.id)'>" +
									   		"<font style='color:orange'>["+ getOneDay[i].schGubunNm+"]</font> " +strTitle+"</a>" +
									   "<span>"+getOneDay[i].period+"</span>" +
									   "<span class='hide'>" + getOneDay[i].memo + "</span>"+
								   		"</p>";
						}

						if(getOneDay[i].type==1 && getOneDay[i].rk !=1){
							//strDiv = strDiv + "<p>"+getOneDay[i].period+"</p>";
							strDiv += "<p style='color:#fff; border-width:1px; border-color:silver; border-top-style:dotted; border-right-style:none; border-left-style:none; border-bottom-style:none;'><span style='font-weight:bold; '>"
								    + strTitle
								    +"</span><span>"+getOneDay[i].period+"</span>" +
								    		"</p>";
						}

					}

					$("#detailOption0 div").html(strDiv1);

					//$("#detailOption1").html().appendTo("<div></div>");
					$("#detailOption1 div").html(strDiv);

					$("#detailDate").html("<p><span id='selectDate'>"+$(this).text()+"</span> 일 (<span id='selectDay'>" + _dayAry[$(this).find("span").attr("day")] + "</span>)");
					$("#calendarDetail").show();

				});

				$("#detailOption0").mouseover(function () {
					// div 높이가 10이하일때 div 안나오게 수정
					if($(this).find("div").height()>10)
						$(this).find("div").show();

					//alert($(this).find("p").maxlength(9));
				});


				$("#detailOption0").mouseout(function() {
					$(this).find("div").hide();
				});


				$("#detailOption1").mouseover(function() {
					// div 높이가 10이하일때 div 안나오게 수정
					if($(this).find("div").height()>10)
						$(this).find("div").show();
				});

				$("#detailOption1").mouseout(function() {
					$(this).find("div").hide();
				});

			},
			error : function(jqXHR, ajaxSettings, thrownError) {
				ajaxJsonErrorAlert(jqXHR, ajaxSettings, thrownError);
			}
		});
	}

	function openDetail(obj){
		$("#scheduleDetail #schTitle").text($("#"+obj).text());
		$("#scheduleDetail #schPeriod").text($("#"+obj).parent().find("span")[0].outerText);
		$("#scheduleDetail #schMemo").text($("#"+obj).parent().find("span")[1].outerText);

		var tmp = obj.split("_");

		var position = $("#detailOption"+tmp[1]).offset();
		position.left = position.left + 158;

		$("#scheduleDetail").css(position);
		$("#scheduleDetail").show();
	}


	// 스킨 설정
	function setSkinWidget(t,f) {

		var cTint= 0;
		var cFint= 0;

		if(t=="theme1") cTint=0;
		if(t=="theme2") cTint=1;
		if(t=="theme3") cTint=2;
		if(t=="theme4") cTint=3;

		if(f=="dotum") cFint=0;
		if(f=="nanum") cFint=1;


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
				if( $(this).index() == currentSkin ) $(this).find("div").addClass("on");
				else $(this).find("div").removeClass("on");
			});

			$("#themeWidgetMain div.font input").each(function() {
				if( $(this).parent().index() == currentFont ) $(this).attr("checked",true);
				else $(this).attr("checked",false);
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
					if( currentSkin != $(this).index() ) valueChanged = true;
					currentSkin = $(this).index();
					selectTheme = $(this).attr("theme");
				}
			});

			// 현재 선택된 폰트를 저장한다.
			$("#themeWidgetMain .font li").each(function() {
				if( $(this).find("input").attr("checked") ) {
					if( currentFont != $(this).index() ) valueChanged = true;
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
				//submitCall($("#subForm"),"","post","/Main.do");
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
		$.ajax({
			url 		: "/getMainMajorMenuList.do",
			type 		: "post",dataType: "json",async: true,data:"",
			success : function(rv) {
				var majorMenu 	= rv.result;
				var mainMenuCd 	= "";
				var grpCd 		= "";
				var mainMenuNm 	= "";
				var viewMenuStr = "";

				var workflowMenuStr = "";

				//대분류 메뉴 초기화
				$("#majorMenu").html("");
				for ( var i = 0; i < majorMenu.length; i++) {
					mainMenuCd 	= majorMenu[i].mainMenuCd;
					grpCd 		= majorMenu[i].grpCd;
					mainMenuNm 	= majorMenu[i].mainMenuNm;
					//viewMenuStr +="<li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+"><span>"+ mainMenuNm +"</span></li>";

					if(mainMenuCd =="20" ){
						workflowMenuStr +="<li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+"><a href='#' class='community'><span>"+ mainMenuNm +"</span></a></li>";
					}
					else if(mainMenuCd =="21" ){
						workflowMenuStr +="<li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+"><a href='#' class='workflow'><span>"+ mainMenuNm +"</span></a></li>";
					}
					else{
						viewMenuStr +="<li mainMenuCd="+mainMenuCd+" grpCd="+grpCd+"><span>"+ mainMenuNm +"</span></li>";
					}

				}
				$("#workflowMenu").append(workflowMenuStr);
				$("#majorMenu").append(viewMenuStr);

				majorMenuAction();
				setLeftMenu();
			},
			error : function(jqXHR, ajaxSettings, thrownError) {
				ajaxJsonErrorAlert(jqXHR, ajaxSettings, thrownError);
			}
		});

	}


	//Major Menu click
	function majorMenuAction(){
		$("#majorMenu>li:not(#sampleMenu,#IBSheetMenu)").click(function(){
			//alert(123);
			$("#mainMenuCd").val($(this).attr("mainMenuCd"));
// 			$("#mainForm")
// 				.attr("action","/Hr.do")
// 				.attr("target", "")
// 				.attr("method","post")
// 				.submit();
			submitCall($("#mainForm"),"","post","/Hr.do");
// 			location.href = "/Hr.do";
		});


		//workflow Menu click
		$(".others_menu_wrap>ul>li").click(function(){
			$("#mainMenuCd").val($(this).attr("mainMenuCd"));
			submitCall($("#mainForm"),"","post","/Hr.do");
		});

	}


	// 패밀리 사이트 설정
	function setFamilySite() {
		$(".menu_footer>ul>li").click(function() {
			var _no = $(this).index();
			if( _no == 0 ) redirect("http://gw.isu.co.kr", "_blank");
			else if( _no == 1 ) redirect("http://www.credu.com", "_blank");
			else if( _no == 2 ) $("#family").show();	// 패밀리 사이트

		});

		$("#family>ul>li").click(function() {
			var _no = $(this).index();
			if( _no == 0 ) 		redirect("http://www.isu.co.kr", "_blank");
			else if( _no == 1 ) redirect("http://www.isuchemical.com", "_blank");
			else if( _no == 2 ) redirect("http://const.isu.co.kr/", "_blank");
			else if( _no == 3 ) redirect("http://www.petasys.com/", "_blank");
			else if( _no == 4 ) redirect("http://www.isu.co.kr/market/", "_blank");
			else if( _no == 5 ) redirect("http://www.isusystem.com/", "_blank");
			else if( _no == 6 ) redirect("http://www.isuvc.com/", "_blank");
			else if( _no == 7 ) redirect("http://www.abxis.com", "_blank");
			else if( _no == 8 ) redirect("http://www.exachem.co.kr", "_blank");
			else if( _no == 9 ) redirect("http://www.exaboard.com", "_blank");
			else if( _no == 10 ) redirect("http://www.isuexaflex.co.kr", "_blank");
			else if( _no == 11 ) redirect("http://www.todaisu.co.kr/main.asp", "_blank");
			$("#family").hide();
		});
		$("#family img").click(function() {
			$("#family").hide();
		});
	}


	function setDrag() {
		// 탭 드래그, 정렬
		$( "#sortable" ).sortable({
			placeholder: "ui-state-highlight",
			cursor: "move",
			handle: ".header,.tab",
			update: function( event, ui ) {

				var tempAry = [];
				var widgetIds ="";
				$("#sortable>div").each(function(){
					if( $(this).css("display") != "none" )
						tempAry.push( $(this).attr("id").substr(7) );

					widgetIds+=$(this).attr("id")+"|";
				});
				//$("#mainTabSeq").val(tempAry.join(","));
				setListLv();
				saveWidget(widgetIds);
			}
		});

		$( "#sortable" ).disableSelection();
	}

	// 위젯 팝업
	function setWidget() {
		$("#widgetButton .btn_widget").click(function() {
			if(!isPopup()) {return;}

			var listAry = [];
			var divClass;
			$(".notice_box").each(function() {
				divClass = $(this).css("display")=="none"?"off":"on";
				listAry.push({
					id:$(this).attr("id"),
					title:$(this).attr("title"),
					info:$(this).attr("info"),
					view:divClass
				});
			});

// 			var params = {};
// 			params.list = listAry;
// 			params.func = setItemList;
// 			var args = openPopup("/html/popup/widget_popup.html",params,540,587);
// 			setItemList(args);

			pGubun = "widjetPopup";
			openPopup("/widjetPopup.do",self,540,620);
		});

		$("#widgetButton .btn_top").click(function() {
			window.scrollTo(0,0);
		});

		// $("#widgetButton").addClass("on");
	}

	// 리스트 순서 셋팅
	function setListLv() {
		$(".notice_box").each(function() {
			$(this).attr("lv", $(this).index() );
		});
	}

	function setItemList(args) {
		if( args == undefined ) return;
		var tempAry = [];
		var len = args.length;
		var viewCount = 0;
		for( var i=0;i<len;i++ ) {
			$("#"+args[i].id).appendTo( $("#sortable") );
			if( args[i].view == "off" ) $("#"+args[i].id).addClass("hide");
			else {
				$("#"+args[i].id).removeClass("hide");
				viewCount++;
				tempAry.push( args[i].id.substr(7) );
			}
		}

		$("#mainTabSeq").val(tempAry.join(","));


		if( viewCount == 0 ) {
			$("#no_widget").show();
			$("#widgetButton").addClass("on");
		}
		else {
			$("#no_widget").hide();
			$("#widgetButton").removeClass("on");
		}

		indexResize();

		if( $("#cover").length > 0 ) {
			$("#cover").height($("#container").height());
		}
	}

	// 서브페이지로 이동
	function goSubPage(mainMenuCd,priorMenuCd,menuCd,menuSeq,prgCd) {
		//var str = "menuSeq="+menuSeq+"&prgCd="+prgCd ;
		var str = "mainMenuCd="+ mainMenuCd + "&menuSeq="+menuSeq+"&prgCd="+prgCd ;
		var result = ajaxCall("/geSubDirectMap.do",str,false).map;
		if(result==null){
			alert("해당메뉴에 대한 조회 권한이 없습니다.");
			return;
		}

		$("#mainMenuCd").val(result.mainMenuCd);
		$("#priorMenuCd").val(result.priorMenuCd);
		$("#menuCd").val(result.menuCd);
		$("#menuSeq").val(result.menuSeq);
		$("#prgCd").val(result.prgCd);

/*
		$("#mainMenuCd").val(mainMenuCd);
		$("#priorMenuCd").val(priorMenuCd);
		$("#menuCd").val(menuCd);
		$("#menuSeq").val(menuSeq);
		$("#prgCd").val(prgCd);
*/
		submitCall($("#mainForm"),"","post","/Hr.do");
	}

	// 위젯 셋팅
	function setWidgetList(str) {
		var tempAry = [];
		if( str.length > 0 )
			tempAry = str.split(",");

		var widgetAry = [];
		for( var i = 0 ; i < tempAry.length ; i++ ) {
			widgetAry.push({
				id:"listBox"+tempAry[i],
				view:"on"
			});
		};

		for( var i = 0 ; i < 12 ; i++ ) {
			if( str.indexOf(i) == -1 ) {
				widgetAry.push({
					id:"listBox"+i,
					view:"off"
				});
			}
		}
		setItemList(widgetAry);
	}

	function getWidgetValue() {

		setWidgetList( $("#mainTabSeq").val() );
	}

	function getWidgetList(){
		var widgetList  = ajaxCall("/getWidgetList.do","",false).DATA;
		var tabId 		= null;
		var tabUrl 		= null;
		var seq			= null;
		var tabName		= null;
		var tabDetail	= null;

		for(var i = 0; i<widgetList.length; i++){
			tabId 		= widgetList[i].tabId;
			tabUrl 		= widgetList[i].tabUrl;
			seq 		= widgetList[i].seq;
			tabName 	= widgetList[i].tabName;
			tabDetail 	= widgetList[i].tabDetail;
			if(seq != null && seq != ""){
				addWidget(tabUrl+"main_"+tabId);
				eval("main_"+tabId+"('"+tabName+"','"+tabDetail+"');");
			}
		}
		indexResize();
	}
	function previewWidget(viewList){
		$("#sortable").empty();
		var tabId 		= null;
		var tabName		= null;
		var tabDetail	= null;
		for(var i = 0; i<viewList.length; i++){
			tabId 		= viewList[i].tabId;
			tabName 	= viewList[i].tabName;
			tabDetail 	= viewList[i].tabDetail;
			addWidget("main/main/main_"+tabId);
			eval("main_"+tabId+"('"+tabName+"','"+tabDetail+"');");
		}
		indexResize();
	}
	function addWidget(wUrl){
		$.ajax({
			url : "/getWidgetToHtml.do",
			type : "post",
			dataType : "html",
			async : false,
			data : "url="+wUrl,
			success : function(data) {
				$("#sortable").append(data);
			},
			error : function(jqXHR, ajaxSettings, thrownError) {
				ajaxJsonErrorAlert(jqXHR, ajaxSettings, thrownError);
			}
		});
	}
	function saveWidget(widgetIds){
		var saveCnt = ajaxCall("/saveWidget.do","widgetIds="+widgetIds,false);
	}

	function setWidgetButton() {

		if( $("#sortable .notice_box").length == 0 ) {
			$("#no_widget").show();
			$("#widgetButton").addClass("on");
		}
		else {
			$("#no_widget").hide();
			$("#widgetButton").removeClass("on");
		}
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

		// 권한 레이어 클릭시 이벤트 막기
		$("#levelWidgetMain").click(function() {
			return false;
		});

		// 권한 설정 클릭 이벤트
		$("#authMgr").click(function(){
			$(document).click();
			$("#levelWidgetMain").show();
			$(document).click(function() { $("#levelCancel").click(); });
			return false;
		});

		// 권한 설정 닫기 클릭 이벤트
		$("#levelCancel").click(function(){
			$(document).unbind("click");
			$("#levelWidgetMain").hide();
			return false;
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
	
	
	// 권한 설정 변경 
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
//		var authList = ajaxCall("/getCollectAuthGroupList.do", "",false).result;
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
						if( grpCd == gCd ) className = "on";
						else className = "";
						//${ssnGrpCd} grpCd

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
