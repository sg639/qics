<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 8 - 작업내역 상세검색</title>
<style type="text/css">
    th {
    	text-align: center;
	}
	
a.cute {display: inline-block;padding: 0;line-height: normal;margin-right: .1em;cursor: pointer;vertical-align: middle;text-align: center;overflow: visible; /* removes extra width in IE */background: #faa3cc;font-weight: normal;padding: 4px 5px 3px 6px;padding:5px 5px 4px 6px\9;color:#fff;font-size:11px;word-break:keep-all}
a:hover.cute {color:#fff}
input.cute, button.cute {border:none;display: inline-block;padding: 0;line-height: normal;margin-right: .1em;cursor: pointer;vertical-align: middle;text-align: center;overflow: visible; /* removes extra width in IE */background: #faa3cc;font-weight: normal;padding: 4px 5px 3px 6px;padding:5px 5px 3px 6px\9;color:#fff;font-size:11px;word-break:keep-all}

</style>
<script type="text/javascript">
var groupMemberMap= new HashMap();
var userValueMap= new HashMap();
var memberValueMap= new HashMap();
var nodataMsgDiv1 =$('<div class="search_result_none">조회된 데이터가 존재 하지 않습니다.</div>');
var psImageName="";
var psImagePath="";
var CskinMap= new HashMap();
var CppMap= new HashMap();
var ClvMap= new HashMap();
var CalvMap= new HashMap();
var CasMap= new HashMap();
var CshMap= new HashMap();
var CstMap= new HashMap();
var CrhMap= new HashMap();
var CrtMap= new HashMap();
var CdefCdMap= new HashMap();
var CchkNoMap= new HashMap();
var orgInfoMap= new HashMap();
var ufsResourceMap= new HashMap();
var ngCodeMap= new HashMap();


	$(document).ready(function() {		
		$("#selectErp").hide();
		$('#FROM_DATE_VW').datepicker({
			beforeShow: function() {  
		        setTimeout(function(){  
		            $('.ui-datepicker').css('z-index', 99999999999999);  
		        }, 0);  
		    } , 
			 changeMonth: true,
			 dateFormat:'yy/mm/dd',
			 dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
			 dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'], 
			 monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
			 monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
		});
		$('#END_DATE_VW').datepicker({
			beforeShow: function() {  
		        setTimeout(function(){  
		            $('.ui-datepicker').css('z-index', 99999999999999);  
		        }, 0);  
		    } , 
			 changeMonth: true,
			 dateFormat:'yy/mm/dd',
			 dayNames: ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'],
			 dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'], 
			 monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
			 monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
		});
		
		$("#FROM_DATE_VW").datepicker("disable");
		$("#END_DATE_VW").datepicker("disable");
		
		$("#ON_POC_NO").css({"ime-mode":"disabled","text-transform":"uppercase"});
		
		var result =ajaxCall("/Code.do?cmd=codeList", $("#frameForm").serialize(), false);
		setCodeMap(result.DATA);

	        selectMonth();
			initJqGrid1();
		 /*	   $(window).bind('resize',function(){
		 	//	  $('#list1').setGridWidth(960, true); 
		 	   $('#list1').setGridHeight(220, true); //
		 		}).trigger('resize');
		 	   $(window).resize();	*/
		    	 initFormatter('list1');
	});
	function setCodeMap(codeDataInfo){
		for(var i = 0; i<codeDataInfo.length; i++){ 
		                                                                                       
			if(codeDataInfo[i].CODE_GUBUN =="UFS_GRADE"){
				CppMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
				ClvMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
				CalvMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
			}
			if(codeDataInfo[i].CODE_GUBUN =="INSPECTOR"){
				CchkNoMap.put(codeDataInfo[i].CODE,codeDataInfo[i].CODE);
			}
			if(codeDataInfo[i].CODE_GUBUN =="SURFACE"){
				CskinMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
			}
			if(codeDataInfo[i].CODE_GUBUN =="ORG_INFO"){
				 orgInfoMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
			}
			if(codeDataInfo[i].CODE_GUBUN =="UFS_RESOURCE"){
				 ufsResourceMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE2);
			}
			if(codeDataInfo[i].CODE_GUBUN =="NGCODE"){
				 ngCodeMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE2);
			}
			if(codeDataInfo[i].CODE_GUBUN =="STEEL"){
				CdefCdMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
			}
			
		}
	 
		var inFact = ufsResourceMap.get($("#IN_LINE").val()); //BGC라는 정보를 가져온다.
	 
		var orgInfo = orgInfoMap.keys();
	 
		var ngValue="";
		for(var i=0; i<orgInfo.length; i++){
			if(inFact == orgInfoMap.get(orgInfo[i])){
				ngValue = orgInfo[i];
			}
		}
		var ngCode =ngCodeMap.keys();
		
		for(var i=0; i<ngCode.length; i++){
			if(ngValue == ngCodeMap.get(ngCode[i])){
				CshMap.put(ngCode[i],ngCode[i]);
				CrhMap.put(ngCode[i],ngCode[i]);
				CdefCdMap.put(ngCode[i],ngCode[i]);
			}
		}
		
		var result = ajaxCall("/Process.do?cmd=processList", $("#mySheetForm").serialize(), false); //작업공정 I/F 조회
	    createSelect(result);
	}
	function selectMonth(){

		 var tempMonth = $("#selectMM").val();
		 var fromDate=""
		 var endDate=""
		 var nowDate = new Date();
		 var nowYear = nowDate.getFullYear();
		 var nowMonth = (nowDate.getMonth()+1) <10  ? "0"+(nowDate.getMonth()+1):(nowDate.getMonth()+1);
		 var nowDay = nowDate.getDate() <10 ? "0"+nowDate.getDate():nowDate.getDate();
		 var settingDate = new Date();
		 var settingYear = "";
		 var settingMonth = "";
		 var settingDay = "";
		 var currDay = 1*24 * 60 * 60 * 1000;// 
		 endDate =nowYear +"/"+nowMonth+"/"+nowDay;
		  
		 $("#FROM_DATE_VW").datepicker("disable");
	     $("#END_DATE_VW").datepicker("disable");
	     
		 if(tempMonth == "1"){
			 fromDate =endDate;
		 }else if(tempMonth == "2") {
			 settingDate.setTime(nowDate.getTime()-currDay);
			 settingYear = settingDate.getFullYear();
			 settingMonth = (settingDate.getMonth()+1) <10 ? "0"+ (settingDate.getMonth()+1):(settingDate.getMonth()+1);
			 settingDay = settingDate.getDate() <10 ? "0" + settingDate.getDate(): settingDate.getDate();
			 fromDate = settingYear+"/"+settingMonth +"/"+settingDay;
		 }else if(tempMonth == "3") {
			 settingDate.setTime(nowDate.getTime()-(currDay*7));
			 settingYear = settingDate.getFullYear();
			 settingMonth = (settingDate.getMonth()+1)<10 ? "0"+ (settingDate.getMonth()+1):(settingDate.getMonth()+1);
			 settingDay = settingDate.getDate() <10 ? "0" + settingDate.getDate(): settingDate.getDate();
			 fromDate = settingYear+"/"+settingMonth +"/"+settingDay;
		 }else if(tempMonth == "4") {
			 settingYear = settingDate.getFullYear();
			 settingMonth = (settingDate.getMonth()+1)<10 ? "0"+ (settingDate.getMonth()+1):(settingDate.getMonth()+1);
			 settingDay = settingDate.getDate()<10 ? "0" + settingDate.getDate(): settingDate.getDate();
			 fromDate = settingYear+"/"+settingMonth +"/"+"01";
		 }else{
			 fromDate="";
			 endDate ="";

			 $("#FROM_DATE_VW").datepicker("enable");
		     $("#END_DATE_VW").datepicker("enable");

		 }
		 $('#FROM_DATE_VW').val(fromDate);
		 $('#END_DATE_VW').val(endDate);
			
		$('#FROM_DATE').val(fromDate);
		$('#END_DATE').val(endDate);
		 
		// $('#FROM_DATE').val(fromDate);
		// $('#END_DATE').val(endDate);
		 
		 
		/*
		var nowDate = new Date();
		 var nowYear = nowDate.getFullYear();
		 var nowMonth = nowDate.getMonth()+1;
		 var nowDay = nowDate.getDate();
		 var lastDay = ( new Date( nowYear, nowMonth, 0) ).getDate();
		 var fromDate = nowYear +"/"+(nowMonth <10 ? "0"+nowMonth:nowMonth) +"/"+'01';
		 var endDate ="";
		 var tempDate= new Date(nowYear , nowMonth+parseInt(tempMonth), "01");
		 if(tempMonth == "1"){
			 lastDay = ( new Date( nowYear, nowMonth, 0) ).getDate();
			 endDate = tempDate.getFullYear()+"/"+((tempDate.getMonth()) <10 ? "0"+(tempDate.getMonth()-1):(tempDate.getMonth()-1) )+"/"+lastDay;
		 }else if(tempMonth == "3"){
			 fromDate = 
			 lastDay = ( new Date( nowYear, nowMonth+parseInt(tempMonth)-1, 0) ).getDate();
			 endDate = tempDate.getFullYear()+"/"+((tempDate.getMonth()-1) <10 ? "0"+(tempDate.getMonth()-1):(tempDate.getMonth()-1) )+"/"+lastDay;
		 }else if(tempMonth == "6"){
			 lastDay = ( new Date( nowYear, nowMonth+parseInt(tempMonth)-1, 0) ).getDate();
			 endDate = tempDate.getFullYear()+"/"+((tempDate.getMonth()-1) <10 ? "0"+(tempDate.getMonth()-1):(tempDate.getMonth()-1) )+"/"+lastDay;
		 }else{
			 fromDate="";
			 endDate ="";
			 $('#FROM_DATE').attr("readonly",false);
			 $('#END_DATE').attr("readonly",false);
		 }
		 */
		
	}
	//작업공정 select 생성
	function createSelect(result){
	    var str="<option value=''>전체</option>";
		if(result.DATA.length > 0 ){
			for(var i = 0; i<result.DATA.length; i++){
				str =str+"<option value='"+result.DATA[i].CODE+"'>"+result.DATA[i].CODE+" </option>";
			}
		}else{
			str = "<option value=''>없음</option>";
		}

		$("#ON_LINE").append(str);  
		var inLine = document.mySheetForm.IN_LINE.value;
		$("#ON_LINE").val(inLine);
		 
		
	}
	
	
	function initFormatter(grid_id){
		var orgColModel =  $("#"+grid_id).jqGrid('getGridParam','colModel');
 		
		for(var i =0; i<orgColModel.length;i++){
		//	 alert("orgColModel[i].userfomatter = "+orgColModel[i].userfomatter);
			if(orgColModel[i].userfomatter =='text'){
			
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {editoptions:{dataEvents:[{ type:'change' , fn:function(e){evenFn(this.id, this.value,grid_id);}},{ type:'blur' , fn:function(e){evenFn(this.id, this.value,grid_id);}}]}});
			
			}else if(orgColModel[i].userfomatter =='select'){
				
				if(orgColModel[i].name =="sStatus") {
					tempStr = 'I:입력;U:수정;D:삭제;E:오류';
				}else{
					tempStr = '';
				}
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter:'select',edittype:"select",editoptions:{value:tempStr,dataEvents:[{ type:'change' , fn:function(e){evenFn(this.id, this.value,grid_id);}},{ type:'blur' , fn:function(e){evenFn(this.id, this.value,grid_id);}}]}});
			}else if(orgColModel[i].userfomatter =='check'){
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter:cboxFormatter,formatoptions:{disabled:false}});
			}else if(orgColModel[i].userfomatter =='image'){
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter : ImgFormatter, unformat:ImgUnFormatter} );
			}else if(orgColModel[i].userfomatter =='image2'){
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter : statusFormatter, unformat:ImgUnFormatter} );
			}else if(orgColModel[i].userfomatter =='int'){
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter:'integer'});
			}else{
			}
			
		}
		doAction("Search");
	}
	function initJqGrid1() {
		jQuery("#list1").jqGrid({
			datatype:"local",			
			colNames : ['No','삭제','sStatus','<div onclick="javascript:checkAllGrid();">선택</div>','공정','검사일자','POC No','현공정','강종','작업자NO','작업자','등급','양식지명','FORM_CODE','NG_CODE','C_STD01','C_STD02','C_LEN','C_PP','STATUS_NM','상태','보기','두께','폭','중량' ,'공칭G','Aiming','G범위','원재사','지시사항','순번','작지번호','공장', '공정',  '상태', 'POC NO','POC NO1','POC NO2','A.SEQ_Q100','SEQ_T300','DATA_YN','ERP_UPLOAD_YN','ERP전송메시지','FORM_SEQ'  ],
			colModel : [
						{name:  'sNo', index:'sNo', width:40, hidden:true },        
						{name : 'sDelete', index:'sDelete',  width:40, align:'center', hidden:true},
						{name:  'sStatus', index:'sStatus', width:40, hidden:true,  	editable:false,userfomatter:'select' },
						{name:  'select', index:'select', width:40, align:'center', hidden:false,  	editable:false,userfomatter:'check' },
						{name : 'IN_LINE', 		index : 'IN_LINE', 		width : 60,	align:'center',		   editable:false,userfomatter:'text'  },
						{name : 'WORK_DATE', 		index : 'WORK_DATE', 		width : 100, 		align:'center', hidden:false, editable:false,userfomatter:'text'  },
						{name : 'POC_NO', 		index : 'POC_NO', 		width : 200, 		align:'center',  editable:false,userfomatter:'text'  },						
						{name : 'MRG_LINE_CODE', 		index : 'MRG_LINE_CODE', 		width : 50, 		align:'center',hidden:true, editable:false,userfomatter:'text'  },
						{name : 'MRG_STEEL_TYPE', 		index : 'MRG_STEEL_TYPE', 		width : 60, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'C_CHK_NO', 		index : 'C_CHK_NO', 		width : 50, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'C_CHKER', 		index : 'C_CHKER', 		width : 100, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'C_LV', 		index : 'C_LV', 		width : 80, 		align:'center',hidden:false,editable:false,userfomatter:'text'  },					
						{name : 'FORM_NAME', 		index : 'FORM_NAME', 		width : 200, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'FORM_CODE', 		index : 'FORM_CODE', 		width : 200, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'NG_CODE', 		index : 'NG_CODE', 		width : 80, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'C_STD01', 		index : 'C_STD01', 		width : 80, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'C_STD02', 		index : 'C_STD02', 		width : 80, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'C_LEN', 		index : 'C_LEN', 		width : 80, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'C_PP', 		index : 'C_PP', 		width : 80, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'STATUS_NM', 		index : 'STATUS_NM', 		width : 80, 		align:'center',editable:false,hidden:true,userfomatter:'text'  },
						{name : 'STATUS_ROW', 		index : 'STATUS_ROW', 		width : 80, 		align:'center',editable:false,userfomatter:'image2'  },
						{name : 'VIEW_ROW', 		index : 'VIEW_ROW', 		width : 80, 		align:'center',editable:false,userfomatter:'image'  },
						{name : 'MRG_R_SUPPLY_THICKNESS', 		index : 'MRG_R_SUPPLY_THICKNESS', 		width : 50, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'MRG_R_WIDTH', 		index : 'MRG_R_WIDTH', 		width : 70, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'PARTIAL_WEIGHT', 		index : 'PARTIAL_WEIGHT', 		width : 70, 		align:'center',editable:false,userfomatter:'int'  },
						{name : 'TARGET_THICKNESS', 		index : 'TARGET_THICKNESS', 		width : 70, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'AIM_THICKNESS', 		index : 'AIM_THICKNESS', 		width : 70, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'AIM_TOL', 		index : 'AIM_TOL', 		width : 120, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'MRG_R_SUPPLIER', 		index : 'MRG_R_SUPPLIER', 		width : 100, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'JOB_DESCRIPTION', 		index : 'JOB_DESCRIPTION', 		width : 350, 		align:'center',editable:false,userfomatter:'text'  },
						{name : 'MRG_SEQ', 	index : 'MRG_SEQ', 	width : 50, 	align:'center',hidden:true, userfomatter:'text' },           
						{name : 'MRG_DISCRETE_NUMBER', 		index : 'MRG_DISCRETE_NUMBER', 		width : 150, 		align:'center',hidden:true,editable:false,userfomatter:'text'  },
						{name : 'IN_FACT', 		index : 'IN_FACT', 		width : 80, 	align:'center', hidden:true,editable:false,userfomatter:'text'  },
						{name : 'IN_ORDER', 	index : 'IN_ORDER', 	width : 80, 		align:'center', hidden:true,editable:false,userfomatter:'text' },
						{name : 'IN_TYPE', 			index : 'IN_TYPE', 			width : 100,  		align:'center', hidden:true, editable:false ,userfomatter:'select'  },
						{name : 'MRG_WIP_ENTITY_NAME', 		index : 'MRG_WIP_ENTITY_NAME', 		width : 200, 		align:'center', hidden:true,editable:false,userfomatter:'text'  },
						{name : 'POC_NO01', 		index : 'POC_NO01', 		width : 100, 		align:'center', hidden:true, editable:false,userfomatter:'text'  },
						{name : 'POC_NO02', 		index : 'POC_NO02', 		width : 100, 		align:'center', hidden:true, editable:false,userfomatter:'text'  },
						{name : 'SEQ_Q100', 		index : 'SEQ_Q100', 		width : 100, 		align:'center', hidden:true, editable:false,userfomatter:'text'  },
						{name : 'SEQ_T300', 		index : 'SEQ_T300', 		width : 100, 		align:'center', hidden:true, editable:false,userfomatter:'text'  },
						{name : 'DATA_YN', 		index : 'DATA_YN', 		width : 100, 		align:'center', hidden:true, editable:false,userfomatter:'text'  },
						{name : 'ERP_UPLOAD_YN', 		index : 'ERP_UPLOAD_YN', 		width : 100, 		align:'center', hidden:true, editable:false,userfomatter:'text'  },
						{name : 'ERP_UPLOAD_MESSAGE', 		index : 'ERP_UPLOAD_MESSAGE', 		width : 250, 		align:'center', hidden:false, editable:false,userfomatter:'text'  },
						{name : 'FORM_SEQ', 		index : 'FORM_SEQ', 		width : 100, 		align:'center', hidden:true, editable:false,userfomatter:'text'  }

			],
			formatter : {
				integer : {thousandsSeparator: " ", defaultValue: ''}
			},
			rowNum: 99999, //-1 : 무제한
			rowTotal: -1,
            pager: '#paginate',
           // scroll:1,
            gridview: true,
            rownumbers: true,  
            sortable: false, cmTemplate: {sortable:false, resizable:false, fixed:true},
            viewrecords: true,
            cellEdit: true,
            cellsubmit: 'clientArray',
            emptyrecords : "총 : 0 건 ",
            pgbuttons : false,
            recordtext:  "총 : {2} 건 ",
            pgtext :"",        
            height: "310",
            width: "989",
			shrinkToFit:false,
            loadonce:true,
	        jsonReader: {	    			
	        	root:"DATA",
    			repeatitems : false,
    			page: function (obj) { return 1; },
    	        total: function (obj) { return 1; },
    	        records: function (obj) { return obj.length; }
	    	},
	    	loadComplete : function(data){  
	    		if(($("#list1").jqGrid('getGridParam','datatype'))=="local") {
						nodataMsgDiv1.hide();
	    		} else {
	    			var rowCnt = $('#list1').jqGrid('getGridParam','records');
					if(rowCnt == 0){
						nodataMsgDiv1.show();
					} else {
						nodataMsgDiv1.hide();
					}
	    		}
				
			},
			loadError : function(xhr,st,err) {
				nodataMsgDiv1.hide();
				nodataMsgDiv1.show();
			},
			onSelectRow:function(id,iRow,iCol){
			},            
 			ondblClickRow: function(rowid, iRow, iCol, e){ 
            },
			onCellSelect:function(rowid,iCol, cellcontent,e){
			},            
            afterEditCell: function (id,name,val,iRow,iCol){
        	},
        	afterSaveCell : function(rowid,name,val,iRow,iCol) {
        	}
        	
		});
		
		nodataMsgDiv1.insertAfter($('#list1').parent());
		
		jQuery("#list1").jqGrid('navGrid','#paginate',{edit:false, add:false, del:false, search:false, refresh:false},{},{},{},{multipleSearch:true});
		//doAction("Search");		 
	}
	function checkAllGrid(){
		var ids = jQuery("#list1").getDataIDs();
		var selectIdx ="0";
		var statusCode="";
		var isStatus = true;
		// var isStatus = $("input:checkbox[id='checkAll']").is(":checked");
//		alert("1111111111111");
		for(var i=0;i<ids.length;i++){
			var ch = $("#list1").find("#"+ids[i]+' input[type=checkbox]').prop('checked');
			if(ch){
				isStatus = false;
				break;
			}
		}
		 if(isStatus){
			selectIdx = "1";
			statusCode="I";
			
		}else{
			selectIdx = "0";
			statusCode="";
		}
		for(var i=0;i<ids.length;i++){
			var rowdata =$("#list1").getRowData(ids[i]);
			rowdata.select=selectIdx;
			rowdata.sStatus=statusCode;
			rowdata.sNo=ids[i];
			$("#list1").jqGrid('setRowData',ids[i],rowdata); 
		}
		
	}
	function doAction(sAction) {
		switch (sAction) {
			case "Search": //조회
				if( $("#STATUS_CODE").val() =="E"){
					 $("#selectErp").show();
			}else{
				$("#selectErp").hide();
			}
				if($('#ON_POC_NO').val()!="") {
					$('#ON_POC_NO').val($('#ON_POC_NO').val().toUpperCase());
					if($('#ON_POC_NO').val().length<9) {
						alert('POC No는 최소 9자리를 입력해주세요.');
					} else if($('#ON_POC_NO').val().length>=9) {
						 $('#FROM_DATE').val($('#FROM_DATE_VW').val());
						 $('#END_DATE').val($('#END_DATE_VW').val());
							
						callSendReload( "/Work.do?cmd=workList", 'popUpForm', 'list1' );
					} 
				} else {
				    $('#FROM_DATE').val($('#FROM_DATE_VW').val());
					$('#END_DATE').val($('#END_DATE_VW').val());
					
					if($('#FROM_DATE').val()!="" && $('#END_DATE').val()!="" ) {
						try {
							var arr1 = $('#FROM_DATE').val().split('/');
						    var arr2 = $('#END_DATE').val().split('/');
						    var dat1 = new Date(arr1[0], arr1[1], arr1[2]);
						    var dat2 = new Date(arr2[0], arr2[1], arr2[2]);
						    
						    var diff = dat2 - dat1;
						    var oneDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
						    
						   if( parseInt(diff/oneDay) > 90 ) {
								alert('최대 조회기간은 90일 입니다. 조회기간을 다시 확인해주세요.');   
								return;
						   }
						    
						} catch (e) {
							// TODO: handle exception
						}
						
						callSendReload( "/Work.do?cmd=workList", 'popUpForm', 'list1' );
					} else {
						alert("검사일자 조회조건은 반드시 입력해야 합니다.");	
					}
				}
				
				
				
	            break;
			case "Save"			:	saveRow();	    break;
		}
    }
function cboxFormatter(cellvalue,options,rowObject){
  	var isCheckBox ='<input type="checkbox"' + (cellvalue == 1 ? ' checked="checked"':'') + ' onclick="onCheckBoxClick(\''+options.gid+'\',\''+options.rowId+'\')"/>';
  	return  isCheckBox ;
  }

function onCheckBoxClick(jqgrid_id,rowId){
	var rowdata =$("#"+jqgrid_id).getRowData(rowId);
	var ch = $("#"+jqgrid_id).find("#"+rowId+' input[type=checkbox]').prop('checked');
	if(ch){	
		rowdata.select='1';
		rowdata.sStatus='I';
		rowdata.sNo=rowId;
	}else{
		rowdata.select='0';
		rowdata.sStatus='';
		rowdata.sNo=rowId;
	}
	$("#"+jqgrid_id).jqGrid('setRowData',rowId,rowdata); 
}
function statusFormatter(cellvalue,options,rowObject){
	var rtn = "";
		var erpUpLoadYn = rowObject.ERP_UPLOAD_YN;
		if("출력대기"==rowObject.STATUS_NM) {
			rtn ='<img src="/common/img/btn_p_1.png">';
				
		} else if ("양식생성중"==rowObject.STATUS_NM) {
			rtn ='<img src="/common/img/btn_p_2.png">';
			
		} else if ("검사대기"==rowObject.STATUS_NM) {
			rtn ='<img src="/common/img/btn_p_3.png">';
			
		} else if ("검사중"==rowObject.STATUS_NM) {
			rtn ='<img src="/common/img/btn_p_4.png">';
			
		} else if ("ERP 전송완료"==rowObject.STATUS_NM) {
			rtn ='<img src="/common/img/btn_p_5.png">';
			
		} else if ("ERP 전송실패"==rowObject.STATUS_NM) {
			rtn ='<img src="/common/img/btn_p_6.png">';
			
		} else {
			rtn =rowObject.STATUS_NM;
			
		}
		
	return rtn;
}

function ImgFormatter(cellvalue,options,rowObject){
	var rtn = "";
		//alert(JSON.stringify(rowObject));
		var erpUpLoadYn = rowObject.ERP_UPLOAD_YN;
		if(erpUpLoadYn == 'Y'){
			rtn = '<button type="button" onclick="javascript:viewRow(\''+options.rowId+'\');" class="btn btn-success btn-xs">보기</button> ';
		}else if(erpUpLoadYn == 'E'){
			rtn = '<button type="button" onclick="javascript:viewRow(\''+options.rowId+'\');" class="btn btn-danger btn-xs">편집</button> ';
		}else{
			rtn = '<button type="button" onclick="javascript:viewRow(\''+options.rowId+'\');" class="btn btn-danger btn-xs">편집</button> ';
		}
		
	return rtn;
}
function ImgUnFormatter(cellvalue,options,cell){
	return cellvalue;
}


function openExtraViewSelf(line,pocno,wd) { 
	if(""!=pocno){
		var result = ajaxCall("/Other.do?cmd=extraViewerKey", "EXTRAVIEW_POC="+pocno+"&EXTRAVIEW_LINE="+line+"&EXTRAVIEW_WD="+wd, false);
		if(result&&result.RESULT&&result.RESULT.LINKURL) {
			var extraViewerURL = "/Other.do?cmd=extraViewer&p="+result.RESULT.LINKURL;
			
		    var W = 1100;        //screen.availWidth; 
		    var H = screen.availHeight;

		    var features = "menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,width=" + W + ",height=" + H + ",left=0,top=0"; 
		    var ExtraViewer = window.open(extraViewerURL,(""+line+""+pocno),features); 
		} else {
			alert("타공정조회화면 권한 오류");	
		}	
	} else {
		
	}
}

function viewRow(rowId, erpYn){
	var rowdata = $("#list1").getRowData(rowId);
	 //alert(JSON.stringify(rowdata));
	 if(rowdata.ERP_UPLOAD_YN=='Y'){
		 
		 /* 작업편의성을 위해 팝업창으로 보여주기.
		 $("#workInfo").hide();
		 $("#viewErp").show();
		   document.popUpForm.SEQ_Q100.value = rowdata.SEQ_Q100;
			document.popUpForm.SEQ_T300.value  =rowdata.SEQ_T300;
			var str ='';
			str =str+"<option value='"+rowdata.SEQ_Q100+'_'+rowdata.SEQ_T300+'_'+rowdata.FORM_SEQ+"'>"+rowdata.POC_NO+"&frasl;"+rowdata.IN_LINE+"&frasl;"+rowdata.WORK_DATE+" </option>";
			$("#otherSelect").append(str);
			getBGImage();
		   */ 
		   
		   openExtraViewSelf(rowdata.IN_LINE,rowdata.POC_NO,rowdata.WORK_DATE);
	 }else{
		// closePopUp('pop_up_banner');
		 changeMain(rowdata.IN_LINE,rowdata.WORK_DATE,rowdata.SEQ_Q100)
	 }
}
function viewBack(){
	 $("#workInfo").show();
	 $("#viewErp").hide();
}
/////////////////////////////////////////////////////////////////////////////
function getBGImage(){
	$("#bgImage").attr('src','');
	$("#viewer").html('');
	$("#orgBg").removeClass('btn pull-left').addClass('btn btn-success  pull-left');
	$("#viewUser").removeClass('btn btn-success  pull-left').addClass('btn pull-left');
	var result = ajaxCall("/Other.do?cmd=otherBGInfo", $("#popUpForm").serialize(), false);
	setBG(result.DATA);
}
function setBG(viewDataInfo){
	
	for(var i = 0; i<viewDataInfo.length; i++){ 
		if($("WEB_DATA_YN").val()=="Y"){
			var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_NAME="+viewDataInfo[i].BG_NAME;
			$("#bgImage").attr('src',url);
		}else{
			var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_NAME="+viewDataInfo[i].P_BG;
			$("#bgImage").attr('src',url);
		}
	}
	
}
function setLayOut(){
	$("#orgBg").removeClass('btn btn-success  pull-left').addClass('btn pull-left');
	$("#viewUser").removeClass('btn pull-left').addClass('btn btn-success  pull-left');
	var result = ajaxCall("/View.do?cmd=viewValueInfo", $("#popUpForm").serialize(), false);  //작업내역 상세검색
	setUserValue(result.DATA);
}
function setUserValue(viewDataInfo){
	for(var i = 0; i<viewDataInfo.length; i++){
		userValueMap.put(viewDataInfo[i].FIELD_NAME,viewDataInfo[i].USER_INPUT_VALUE);
	}
	var result = ajaxCall("/View.do?cmd=viewPadInfo", $("#popUpForm").serialize(), false);  //작업내역 상세검색
	getGroupMember(result.DATA);
}
function getGroupMember(viewDataInfo){
	for(var i = 0; i<viewDataInfo.length; i++){ 
		if(viewDataInfo[i].FIELD_FIELDVALUE !=null && viewDataInfo[i].FIELD_FIELDVALUE !=""){
			memberValueMap.put(viewDataInfo[i].FIELD_NAME,viewDataInfo[i].FIELD_FIELDVALUE);
		}
		if(viewDataInfo[i].FIELD_GROUPTYPE =="radio" ){
			var str ='';
			if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
				str ='<input type="hidden" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+userValueMap.get(viewDataInfo[i].FIELD_NAME)+'" />';
			}else{
				str ='<input type="hidden" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" />';
			}
			$("#popUpForm").append(str)
			 var groupMember = viewDataInfo[i].FIELD_GROUPMEMBER.split(";");
			 for(var j =0; j < groupMember.length; j++){
				 groupMemberMap.put(groupMember[j],viewDataInfo[i].FIELD_GROUPMEMBER);
			 }
		 }
	}
	createInfoDiv(viewDataInfo);
}
function createInfoDiv(viewDataInfo){
	var dataDiv ="";//작성된 필드 정보 입력
	var tempOrder=0;
	var fullDiv="";
	var subDiv=""
	for(var i = 0; i<viewDataInfo.length; i++){ 
		 if(i == 0){
			 tempOrder=viewDataInfo[i].PAGE_ORDER;
			 $("#FORM_SEQ").val(viewDataInfo[i].FORM_SEQ);
			 $("#PAGE_ORDER").val(viewDataInfo[i].PAGE_ORDER);
	        fullDiv='<div id="result_'+(tempOrder)+'" style="display: block;">'
		 }
		 if(tempOrder ==viewDataInfo[i].PAGE_ORDER ){
			 var userValue ="";
			 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
				 userValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
			 }			 
			 if(viewDataInfo[i].FIELD_DATATYPE == "image"){
				 subDiv="";
				 if( viewDataInfo[i].FIELD_NAME == "C_SPEC"){
					 
					if(psImageName != "" && psImagePath  != ""){
						subDiv = "<div id='pg_"+viewDataInfo[i].FIELD_NAME+"' style='border:2px solid #c5c5c5;display:block; position:absolute  ;width:"+viewDataInfo[i].FIELD_WIDTH+"px;height:"+(parseInt(viewDataInfo[i].FIELD_HEIGHT))+"px; left:"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px; top:"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;  background-image:url(/ImagePS.do?BG_PATH="+psImagePath+"&P_BG="+psImageName+") ; background-repeat: no-repeat;background-position:-"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px -"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;' >"	;
						$("#subImage").append(subDiv);
					}
					
				 }
				 if( viewDataInfo[i].FIELD_NAME == "C_IMG_RM"){
					 
						if(psImageName != "" && psImagePath  != ""){
							subDiv = "<div id='pg_"+viewDataInfo[i].FIELD_NAME+"' style='border:2px solid #c5c5c5;display:block; position:absolute  ;width:"+viewDataInfo[i].FIELD_WIDTH+"px;height:"+(parseInt(viewDataInfo[i].FIELD_HEIGHT))+"px; left:"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px; top:"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;  background-image:url(/ImagePS.do?BG_PATH="+psImagePath+"&P_BG="+psImageName+") ; background-repeat: no-repeat;background-position:-"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px -"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;' >"	;
							$("#subImage").append(subDiv);
						}
						
					 }
			 }
			 if(viewDataInfo[i].FIELD_DATATYPE == "text" || (viewDataInfo[i].FIELD_DATATYPE == "group" && viewDataInfo[i].FIELD_GROUPTYPE == "text")){
				 if(viewDataInfo[i].FIELD_DYNAMIC=="true"){
					 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT))+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
				    
					 //C_POC_NO1 의 값이 존재 한다면 C_POC_NO1로 없으면 C_POC_NO1_POD로
					 if(viewDataInfo[i].FIELD_NAME == "C_POC_NO01_POD" || viewDataInfo[i].FIELD_NAME == "C_POC_NO02_POD" || viewDataInfo[i].FIELD_NAME == "C_POC_NO03_POD"
				    	 || viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" || viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD" || viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD"
				    		 || viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
						 
						 if(viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" || viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD"){
							 var maxValue1="";
							 var maxValue2="";
							 if(userValueMap.containsKey("C_THMAX")){
								 userValue = userValueMap.get("C_THMAX");
								 var tempValue = userValue.split(".");
								 if(tempValue.length >0){
									 maxValue1 = tempValue[0];
									 maxValue2 = tempValue[1];
								 }
								 
							 }		
							 if(viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" ){
								 if(viewDataInfo[i].FIELD_POD !="" && viewDataInfo[i].FIELD_POD !=null){
									 maxValue1 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+maxValue1+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" />';
							 }else if(viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD"){
								 if(viewDataInfo[i].FIELD_POD !="" && viewDataInfo[i].FIELD_POD !=null){
									 maxValue2 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+maxValue2+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }
							 
						 }else if(viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD"	 || viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
							 var minValue1="";
							 var minValue2="";
							 if(userValueMap.containsKey("C_THMIN")){
								 userValue = userValueMap.get("C_THMIN");
								 var tempValue = userValue.split(".");
								 if(tempValue.length >0){
									 minValue1 = tempValue[0];
									 minValue2 = tempValue[1];
								 }
							 }							 
							 if(viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD" ){
								 if(viewDataInfo[i].FIELD_POD !="" && viewDataInfo[i].FIELD_POD !=null){
									 minValue1 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+minValue1+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }else if(viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
								 if(viewDataInfo[i].FIELD_POD !="" && viewDataInfo[i].FIELD_POD !=null){
									 minValue2 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+minValue2+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }
						 }else{
							 var tempValue = viewDataInfo[i].FIELD_POD;
							 
							 if(viewDataInfo[i].FIELD_NAME == "C_POC_NO01_POD"){
								 
								 if(tempValue == "" || tempValue == null){
									 if(userValueMap.containsKey("C_POC_NO01")){
										 tempValue = userValueMap.get("C_POC_NO01")
									 }
								 }
							 }
 							if(viewDataInfo[i].FIELD_NAME == "C_POC_NO02_POD"){
								 
								 if(tempValue == "" || tempValue == null){
									 if(userValueMap.containsKey("C_POC_NO02")){
										 tempValue = userValueMap.get("C_POC_NO02")
									 }
								 }
							 }
 							if(viewDataInfo[i].FIELD_NAME == "C_POC_NO03_POD"){
								 
								 if(tempValue == "" || tempValue == null){
									 if(userValueMap.containsKey("C_POC_NO03")){
										 tempValue = userValueMap.get("C_POC_NO03")
									 }
								 }
							 }
							
							 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"   value="'+tempValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
						 }
				    	 
				     }else{
				    	 if(viewDataInfo[i].FIELD_NAME == "C_FRM_DPT" || viewDataInfo[i].FIELD_NAME == "C_FRM_PRN" || viewDataInfo[i].FIELD_NAME == "C_FRM_ID" || viewDataInfo[i].FIELD_NAME == "C_FRM_NO" || viewDataInfo[i].FIELD_NAME == " C_SEQ"){
				    		 
				    	 }else if(viewDataInfo[i].FIELD_NAME == "C_STAMP01_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP02_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP03_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP04_POD"){
				    		if(viewDataInfo[i].FIELD_POD == "" || viewDataInfo[i].FIELD_POD == null){
				    			
				    		}else{
				    			if($("#WEB_DATA_YN").val() == 'Y'){
				    				dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn-inform" style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].FIELD_POD.split("\n").join("<br>")+'</span>';
				    			}else{
				    				
				    			}
				    			
				    		}
				    		 
				    	 }else{
				    		 dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].FIELD_POD+'</span>';
				    	 }
				     }
					 //dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" disabled="disabled" value="'+viewDataInfo[i].FIELD_POD+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
				     
					 dataDiv +='</div>';
				 }else{
					 var tempName = viewDataInfo[i].FIELD_NAME;
					 var tempValue ="";
					 var tempStyle="";
					 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT))+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
					 if( viewDataInfo[i].FIELD_NAME.indexOf("C_MST") > -1){
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_PP"){
						 
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CppMap.containsKey(tempValue)){
								 	 
							}else{
								  tempStyle="text-danger";
							}
						 }
						 
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_LV"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(ClvMap.containsKey(tempValue)){
								 	 
							}else{
								  tempStyle="text-danger";
							}
						 }
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; "/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_ALV"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CalvMap.containsKey(tempValue)){
								 	 
							}else{
								  tempStyle="text-danger";
							}
						 }
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; "/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_SKIN"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							  
							 if(CskinMap.containsKey(tempValue)){
							}else{
								  tempStyle="text-danger";
							}
						 }
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" />';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_SH") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CshMap.containsKey(tempValue)){
								 	 
							}else{
								  tempStyle="text-danger";
							}
						 }
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " />';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_RH") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CrhMap.containsKey(tempValue)){
								 	 
							}else{
								  tempStyle="text-danger";
							}
						 }
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; "/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_DEF_CD") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CdefCdMap.containsKey(tempValue)){
								 	 
							}else{
								  tempStyle="text-danger";
							}
						 }
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_ST") > -1){
						 if(viewDataInfo[i].FIELD_NAME.indexOf("C_STP") > -1){
							 
						 }else{
							 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
								 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
								 if(checkValue(tempValue)){
									 	 
								}else{
									  tempStyle="text-danger";
								}
							 }
						 }
						
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; "/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_RT") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(checkValue(tempValue)){
								 	 
							}else{
								  tempStyle="text-danger";
							}
						 }
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; "/>';
					 }else{
						 dataDiv +='<input type="text" readonly="readonly"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
					 }
					 
					 dataDiv +='</div>';
				 }

			 }else{
				 if(groupMemberMap.containsKey(viewDataInfo[i].FIELD_NAME)){
					 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT) )+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
					 var tempStr = viewDataInfo[i].FIELD_NAME.substring(0,viewDataInfo[i].FIELD_NAME.lastIndexOf("_"));
					 
					 var tempVal = '';
					 var memVal = '';
					 if(userValueMap.containsKey(tempStr)){
						 tempVal = userValueMap.get(tempStr);
					 }
					 if(memberValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
						 memVal = memberValueMap.get(viewDataInfo[i].FIELD_NAME);
					 }
					if(tempVal == memVal && memVal !=''){
						dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs active"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
					}else{
						dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
					}
					 dataDiv +='</div>'
				 }
			 }
			
		 }
			
		 if(i == viewDataInfo.length-1){
	     	fullDiv=fullDiv+dataDiv+"</div>"
	     }
	}
	$("#viewer").append(fullDiv);
	//backGroudImage();
}
function backGroudImage(){
	if($("#WEB_DATA_YN").val() == 'Y'){
		
	}else{
		var str ="<div id='subBg' style='border:2px solid #c5c5c5;display:block; position:absolute  ;width:177px;height:173px; left:455px; top:495px;  background-image:url(/common/img/20160128_135437_947[1]_THUMBNAIL.png) ; background-repeat: no-repeat;background-position:-455px -495px;' >"	;
		$("#subImage").html(str);
	}
	
}
//image원본(결과)

//결과 layout

//image배경();
function dataChange(){
	var tempStr = $("#otherSelect").val().split("_");
	$("#bgImage").attr('src','');
	$("#viewer").html('');
	groupMemberMap= new HashMap();
	userValueMap= new HashMap();
	memberValueMap= new HashMap();
	document.popUpForm.SEQ_Q100.value = tempStr[0];
	document.popUpForm.SEQ_T300.value  =tempStr[1];
	document.popUpForm.FORM_SEQ.value  = tempStr[2];
	getBGImage();
	
}
	
function 	sendErpData(){
	var ids = jQuery("#list1").getDataIDs();
	var paramUrl="";
	var idx =0;
	for(var i=0;i<ids.length;i++){
		var cols = $("#list1").getRowData(ids[i]);
		var ch = $("#list1").find("#"+ids[i]+' input[type=checkbox]').prop('checked');
		if(ch){
			for(var key in cols){
				if(idx == 0){
					paramUrl = paramUrl+key+"="+ encodeURIComponent(cols[key]);     
				}else{
					paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);     
				}
				idx++;
			}
		}
	}
	
	if(paramUrl == ""){
		alert("선택된 작업이 없습니다.");
		return false;
	}
	if(confirm("선택된 ERP 전송 실패건을 재전송하시겠습니까?")){
		jqGrid_SaveName(document.popUpForm,'list1');
		var result = ajaxCall("/Work.do?cmd=erpReSend", paramUrl+"&"+$("#popUpForm").serialize(),false);
		var message = ""; 
		var totalCount =result.DATA.length;
		var errorCount =0;
		 
		for(var i=0; i<result.DATA.length; i++){
			if(result.DATA[i].ERP_UPLOAD_YN == "E"){
				errorCount++;
			}
		}
		if(parseInt(errorCount)> 0){
			message = "ERP 일괄전송 결과는 아래와 같습니다.\n해당 POC번호를 다시 확인바랍니다. \n\n - ERP 일괄전송 건수 : "+totalCount+"건\n\n - 정상  : "+(parseInt(totalCount)-parseInt(errorCount))+"건, \n\n - 실패 : "+errorCount+"건 입니다.";
			alert(message); 
		}else{
			message = "총 "+totalCount +"건의 작업지시건이 ERP에 전송되였습니다."
			
			alert(message); 
			
			
		}
		doAction("Search");
	}
	
	
}
 
</script>

</head>
<body>
<div id="workInfo" class="wrapper">

<!-- Popup Area -->
<div class="modal-dialog modal-xl" >
  <div class="modal-content">  
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onClick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label>작업내역 상세검색</label>
      </h3>
    </div>
    <!-- ./modal-header -->
    <div class="modal-body">
      
    	<!-- Search Area -->
        <div class="pbox-search m-b">
        <form id="popUpForm" name="popUpForm" role="form" class="form-horizontal">
			   	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100"  />
			   	<input type="hidden" id="SEQ_T300" name="SEQ_T300"   />
			   	<input type="hidden" id="FORM_SEQ" name="FORM_SEQ" />
			    <input type="hidden" id="PAGE_ORDER" name="PAGE_ORDER"   />
			    <input type="hidden" id="WEB_DATA_YN" name="WEB_DATA_YN"  />  
			    <input type="hidden" id="FROM_DATE"   name="FROM_DATE" >
			    <input type="hidden" id="END_DATE"   name="END_DATE" >    
            <div class="form-group m-b-sm">
            	<label class="col-sm-1 control-label">공정</label>
                <div class="col-sm-1">
                <select id="ON_LINE" name="ON_LINE"  class="form-control input-sm">
                </select> 
                </div>
            	<label class="col-sm-1 control-label">검사일자</label>
            	<div class="col-sm-1">
            		<select id="selectMM" name="selectMM" class="form-control input-sm"  onchange="javascript:selectMonth();">
            	  	  <option value="1">당일</option>
            	  	  <option value="2">전일</option>
            	  	  <option value="3" selected="selected">일주일</option>
            	  	  <option value="4">당월</option>
            	  	  <option value="">직접</option>
            		</select> 
                </div>
                <div class="col-sm-2">
                	<div class="input-group"> 
                    	<input type="text" id="FROM_DATE_VW"   name="FROM_DATE_VW"  readonly="readonly"  class="form-control width110px">
                  	</div>
                    <span style="position:absolute; z-index:2000; left:157px; top:10px">~</span>
                </div>
                <div class="col-sm-2">
                	<div class="input-group"> 
                    	<input type="text"  id="END_DATE_VW"   name="END_DATE_VW"  readonly="readonly"  class="form-control width110px" style="margin-left:2px">
                  	</div>
                </div>
               <label class="col-sm-1 control-label">검사자</label>
               <div class="col-sm-1">
            		<input type="text"  id="C_CHK_NO" name="C_CHK_NO"  class="form-control width70px"  >
                </div>
               <div class="col-sm-2">
            		<input type="text"  id="C_CHKER" name="C_CHKER"  class="form-control"  >
                </div>
            </div>
            <div class="form-group m-b-sm">
                <label class="col-sm-1 control-label">검사상태</label>
                <div class="col-sm-2">
                <select id="STATUS_CODE" name="STATUS_CODE" class="form-control input-sm" >
                	<option value="">전체</option>
                	<option value="W">검사중(보정중)</option>
                	<option value="C">ERP 전송완료</option>
                	<option value="E">ERP 전송실패</option>
                </select> 
                </div>   
                <label class="col-sm-1 control-label">POC No</label>
                <div class="col-sm-4">
                	<input type="text"  id="ON_POC_NO"   name="ON_POC_NO"  class="form-control input-sm"  >
                </div>
               
                <label class="col-sm-1 control-label">등급</label>
                <div class="col-sm-1">
                	<input type="text"  id="C_LV"   name="C_LV"  class="form-control input-sm"  >
                </div>
                 
                <div class="col-sm-2">
                	<button type="button" class="btn btn-primary btn-sm pull-right" onClick="javascript:doAction('Search');"><i class="fa fa-search"></i> 검색</button>
                </div>
			</div>    
                        
        </form>
		</div>
        <!-- ./Search Area -->
        
        <div class="row">
        	<div class="col-sm-6">
				<h4>품질검사 정보목록</h4>
            </div> 
        	<div class="col-sm-6">   
        		<div class="text-right"><span id="totalCount"></span></div>
        	</div>
        </div>    
        <div> 
			<div>
				<table id="list1""></table>
				<div id="paginate"></div>
			</div>
		</div>  
              
    </div>
    <!-- ./modal-body-->
    
    <div class="modal-footer">
    	<div class="row">
        	 <div class="col-xs-6">
              	<!--<button type="button" class="btn btn-success pull-left">선택항목 ERP 일괄전송</button>-->
           	</div>
           	<div class="col-xs-6">
               	<button type="button" class="btn pull-right" onClick="javascript:closePopUp('pop_up_banner');">닫기</button>
                <button type="button"  id="selectErp"  name="selectErp" class="btn btn-primary pull-right m-r-xs" onclick="javascript:sendErpData();">선택항목 ERP 일괄전송</button>
           	</div>
        </div>
    </div>
    
  </div>
  <!-- ./modal-content -->
</div>
<!-- ./modal-dialog -->

</div>

<div id="viewErp" class="wrapper" style="display: none;">
<div class="modal-dialog modal-xl">
  <div class="modal-content"> 
   
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onClick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label>작업내역 보기</label>
        <div class="form-group pull-right m-b-none">
            <div class="col-sm-12">
            <select  id="otherSelect"  class="form-control input-sm m-r" onChange="javascript:dataChange();" style="display:none;">
            </select> 
        	</div>
        </div>
      </h3>
      <span></span>
    </div>
    <!-- ./modal-header -->
    
    <div class="modal-body">
		<div class="modal-paper">
           	<div class="img-shadow paper">
           		<img id="bgImage" src="" alt="image">
           	</div>
           	<div id='subImage' ></div>
           	<div id="viewer" class="data-area"></div>
		</div>
    </div>
    <!-- ./modal-body -->  
      
    <div class="modal-footer">
    	<div class="row">
            <div class="col-xs-7">
           		<button id="orgBg" name="orgBg"  type="button" class="btn btn-success  pull-left" onClick="javascript:getBGImage();">원본양식</button>
                <button id="viewUser" name="viewUser" type="button" class="btn pull-left" onClick="javascript:setLayOut();">보정데이터 보기</button>
           	</div>
           	<div class="col-xs-5">
           		<button type="button" class="btn pull-right" onClick="javascript:viewBack();">BACK</button>
           		<button type="button" class="btn btn-primary pull-right m-r-xs"><i class="fa fa-print"></i> 인쇄하기</button>
           	</div>
		</div>    
    </div>
    <!-- ./modal-footer -->
    
  </div>
  <!-- ./modal-content -->
</div>
<!-- ./modal-dialog -->

</div>



