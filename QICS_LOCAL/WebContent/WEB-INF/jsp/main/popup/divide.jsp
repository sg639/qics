<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 5 -분할양식추가</title>

<script type="text/javascript">
var nodataMsgDiv1 =$('<div class="search_result_none">분할정보 입력/선택 후 추가해주세요</div>');
var divideData = new HashMap();
var textStampMap = new HashMap();
var defaultData="";
var indexIdx=0;
	$(document).ready(function() {				
		//var result = ajaxCall("/Divide.do?cmd=divideInfo", $("#popUpForm").serialize(), false);
		//alert(JSON.stringify(result));
	    document.popUpForm.SEQ_Q100.value = document.mySheetForm.SEQ_Q100.value;
		document.popUpForm.SEQ_T300.value = document.mySheetForm.SEQ_T300.value;
		var result = ajaxCall("/Divide.do?cmd=divideInfo", $("#popUpForm").serialize(), false);  //작업내역 상세검색
		//alert(JSON.stringify(result));
		spanData(result.DATA);
		
		if(defaultData) {
			var base_pocno02 = defaultData.POC_NO02;
			var first_div_pocno02 = "";
			for ( var i=0;i<base_pocno02.length;i++) {
				if(i==0 && base_pocno02.charCodeAt(i)==48) {
					//first_div_pocno02 = first_div_pocno02 + String.fromCharCode(65);
					defultDivCharIndex = i;
					break;
				} else if(base_pocno02.charCodeAt(i)>=65) {
					//first_div_pocno02 = first_div_pocno02 + String.fromCharCode(base_pocno02.charCodeAt(i)+1);
					defultDivCharIndex = i;
				} else {
					
				}
			}
			
			first_div_pocno02 = "";
			for ( var j=0;j<base_pocno02.length;j++ ) {
				if(j==defultDivCharIndex) {
					first_div_pocno02+= String.fromCharCode( ( base_pocno02.charCodeAt(j)==48?64:base_pocno02.charCodeAt(j) ) +1 );
				} else {
					first_div_pocno02+= String.fromCharCode(base_pocno02.charCodeAt(j)) ;
				}
				
			}
			//alert(first_div_pocno02);
			
			$("#pocCheck").val(first_div_pocno02);
		}
		
	});
	function spanData(tempData){
		defaultData = tempData;
		$("#SAPN_POC_NO").html('');
		$("#SPAN_ORDER").html('');
		$("#SPAN_LINE").html('');
		$("#SPAN_R_SUPPLY_THICKNESS").html('');
		$("#SPAN_R_WIDTH").html('');
		$("#SPAN_STEEL_TYPE").html('');
		$("#SPAN_R_SUPPLIER").html('');
		$("#STATUS_NM").html('');
		
		$("#SPAN_POC_NO").html(tempData.POC_NO);
		$("#SPAN_ORDER").html(tempData.IN_ORDER);
		
		document.popUpForm.IN_ORDER.value = tempData.IN_ORDER;
		document.popUpForm.IN_FACT.value = tempData.IN_FACT;
		document.popUpForm.FORM_TYPE.value = tempData.FORM_TYPE;
		document.popUpForm.IN_LINE.value = tempData.IN_LINE;
		document.popUpForm.POC_NO01.value = tempData.POC_NO01;
		document.popUpForm.POC_NO.value = tempData.POC_NO;
		document.popUpForm.WORK_DATE.value = tempData.WORK_DATE;
		document.popUpForm.WIP_ENTITY_NAME.value=tempData.MRG_WIP_ENTITY_NAME;
		$("#SPAN_LINE").html(tempData.IN_LINE);
		$("#SPAN_R_SUPPLY_THICKNESS").html(tempData.MRG_R_SUPPLY_THICKNESS);
		$("#SPAN_R_WIDTH").html(tempData.MRG_R_WIDTH);
		$("#SPAN_STEEL_TYPE").html(tempData.MRG_STEEL_TYPE);
		$("#SPAN_R_SUPPLIER").html(tempData.MRG_R_SUPPLIER);
		
		$("#STATUS_NM").html(tempData.STATUS_NM);
		
		  initJqGrid1();
		  
	   $(window).bind('resize',function(){
			   // $('#list1').setGridWidth($('#sheeId').width(), true); 
	 	  	  $('#list1').setGridHeight(150, true); //
	 		}).trigger('resize'); 	   
	 	   
	 	   $(window).resize();	
	       initFormatter('list1');
	       divideList();

	}
	function divideList(){
		var result = ajaxCall("/PenWork.do?cmd=penWorkFinalDivideInfo", $("#popUpForm").serialize(), false);  //작업내역 상세검색
		//alert(JSON.stringify(result.DATA));
		createSelectBox(result);
	}
	function createSelectBox(result){
	    var str="<option value=''>주문번호/행선지 선택 </option>";
	
		if(result.DATA.length > 0 ){
			
			for(var i = 0; i<result.DATA.length; i++){
				 
				divideData.put(i,result.DATA[i]);
 
				str =str+"<option value='"+i+"'>"+result.DATA[i].ORDER_NUMBER+"-"+result.DATA[i].LINE_NUMBER+" |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+result.DATA[i].CUSTOMER+"</option>";
			}
		}else{
			//str = "<option value=''>없음</option>";
		}

		str =str+"<option value='SD9'>SD9</option>";
		str =str+"<option value='DZ9'>DZ9</option>";
		str =str+"<option value='EZ9'>EZ9</option>";
		str =str+"<option value='HOOP'>HOOP</option>";

		$("#CUSTOMER_CODE").html(str);
		goOtherLine();
		createStamp();
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
				}else if(orgColModel[i].name =="sStatus"){
					tempStr = 'I:입력;U:수정;D:삭제;E:오류';
				}else if(orgColModel[i].name =="sStatus"){
					tempStr = 'I:입력;U:수정;D:삭제;E:오류';
				}else if(orgColModel[i].name =="sStatus"){
					tempStr = 'I:입력;U:수정;D:삭제;E:오류';
				}else{
					tempStr ="";
				}
				
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter:'select',edittype:"select",editoptions:{value:tempStr,dataEvents:[{ type:'change' , fn:function(e){evenFn(this.id, this.value,grid_id);}},{ type:'blur' , fn:function(e){evenFn(this.id, this.value,grid_id);}}]}});
			}else if(orgColModel[i].userfomatter =='check'){
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter:cboxFormatter,formatoptions:{disabled:false}});
			}else if(orgColModel[i].userfomatter =='image'){
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter : ImgFormatter, unformat:ImgUnFormatter} );
			}else{
			}
			
		}
		doAction("Search");
	}
	
	
	
	function initJqGrid1() {
		jQuery("#list1").jqGrid({
			datatype:"local",			
			colNames : ['No','삭제','상태','삭제', '분할 No','POC_NO02','POC_NO03','ME', '행선지', 'SALES NO', 'LINE','주문특기사항','외주','차공정','자동차','CHECK_DATA','FORM_CODE','MRG_SEQ','MRG_DISCRETE_NUMBER','MRG_LINE_CODE','MRG_STEEL_TYPE','MRG_R_SUPPLIER','MRG_WIP_ENTITY_NAME','MRG_R_SUPPLY_THICKNESS','MRG_R_WIDTH','PARTIAL_WEIGHT','TARGET_THICKNESS','AIM_THICKNESS','AIM_TOL','JOB_DESCRIPTION' ],
			colModel : [
				{name:  'sNo', index:'sNo', width:40, hidden:true },        
				{name : 'sDelete', index:'sDelete',  width:40, align:'center', hidden:true},
				{name:  'sStatus', index:'sStatus', width:40, hidden:true,  	editable:false,userfomatter:'select' },
				{name : 'DEL_ROW', 		index : 'DEL_ROW', 		width : 80, 		align:'center', hidden:false,  	editable:false,userfomatter:'image' },
				{name : 'POC_NUMBER', 		index : 'POC_NUMBER', 		width : 100, 	align:'center', editable:false,userfomatter:'text'  },
				{name : 'POC_NO02', 		index : 'POC_NO02', 		width : 100, 	align:'center', hidden:true,editable:false,userfomatter:'text'  },
				{name : 'POC_NO03', 		index : 'POC_NO03', 		width : 100, 	align:'center', hidden:true, editable:false,userfomatter:'text'  },
				{name : 'ME', 		index : 'ME', 		width : 40, 		align:'center', hidden:false,  	editable:false,userfomatter:'text'  },
				{name : 'CUSTOMER', 	index : 'CUSTOMER', 	width : 200, 		align:'center', editable:false,userfomatter:'text' },
				{name : 'ORDER_NUMBER', 		index : 'ORDER_NUMBER', 		width : 120,	align:'center',		    editable:false,userfomatter:'text'  },
				{name : 'LINE_NUMBER', 			index : 'LINE_NUMBER', 			width : 40,  		align:'center', editable:false ,userfomatter:'text'  },
				{name : 'PROD_REMARK', 		index : 'PROD_REMARK', 		width : 350, 		align:'center', editable:false,userfomatter:'text'  },
				{name : 'STAMP01_NM', 		index : 'STAMP01_NM', 		width : 60, 		align:'center', hidden:false,  	editable:false,userfomatter:'text'  },
				{name : 'STAMP02_NM', 		index : 'STAMP02_NM', 		width : 60, 		align:'center', hidden:false,  	editable:false,userfomatter:'text'  },
				{name : 'STAMP03_NM', 		index : 'STAMP03_NM', 		width : 60, 		align:'center', hidden:false,  	editable:false,userfomatter:'text'  },
				{name : 'CHECK_DATA', 		index : 'CHECK_DATA', 		width : 100, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'FORM_CODE', 		index : 'FORM_CODE', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_SEQ', 		index : 'MRG_SEQ', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_DISCRETE_NUMBER', 		index : 'MRG_DISCRETE_NUMBER', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_LINE_CODE', 		index : 'MRG_LINE_CODE', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_STEEL_TYPE', 		index : 'MRG_STEEL_TYPE', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_R_SUPPLIER', 		index : 'MRG_R_SUPPLIER', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_WIP_ENTITY_NAME', 		index : 'MRG_WIP_ENTITY_NAME', 		width : 150, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_R_SUPPLY_THICKNESS', 		index : 'MRG_R_SUPPLY_THICKNESS', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'MRG_R_WIDTH', 		index : 'MRG_R_WIDTH', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'PARTIAL_WEIGHT', 		index : 'PARTIAL_WEIGHT', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'TARGET_THICKNESS', 		index : 'TARGET_THICKNESS', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'AIM_THICKNESS', 		index : 'AIM_THICKNESS', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'AIM_TOL', 		index : 'AIM_TOL', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  },
				{name : 'JOB_DESCRIPTION', 		index : 'JOB_DESCRIPTION', 		width : 40, 		align:'center', hidden:true,  	editable:false,userfomatter:'text'  }

			],

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
            height: 150,
            width: 942,
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
    			var rowCnt = $('#list1').jqGrid('getGridParam','records');
				if(rowCnt == 0){
					nodataMsgDiv1.show();
				} else {
					nodataMsgDiv1.hide();
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
	
	/*Sheet Action*/
	function doAction(sAction) {
		switch (sAction) {
			case "Search": //조회
				//callSendReload( "/Add.do?cmd=addList", 'popUpForm', 'list1' );
	            break;
			case "Save"			:	saveRow();	    break;
		}
    }
/*function cboxFormatter(cellvalue,options,rowObject){
   	var isCheckBox ='<input type="checkbox"' + (cellvalue == 1 ? ' checked="checked"':'') + ' onclick="onCheckBoxClick(\''+options.gid+'\',\''+options.rowId+'\')"/>';
   	return  isCheckBox ;
 }

function onCheckBoxClick(jqgrid_id,rowId){
   	var rowdata =$("#"+jqgrid_id).getRowData(rowId);
   	var ch = $("#"+jqgrid_id).find("#"+rowId+' input[type=checkbox]').prop('checked');
   	if(ch){	
		rowdata.selectME='1';
		rowdata.FORM_CODE="FORM_QICS_D";
		rowdata.ME='1';
		rowdata.sStatus='I';
		rowdata.sNo=rowId;
	}else{
		rowdata.ME='0';
		rowdata.sStatus='I';
		rowdata.FORM_CODE="FORM_QICS_C";
		rowdata.sNo=rowId;
	}
   	$("#"+jqgrid_id).jqGrid('setRowData',rowId,rowdata); 
}	*/
function ImgFormatter(cellvalue,options,rowObject){
	var rtn = "";
		rtn = '<button type="button" onclick="javascript:deleteRow(\''+options.rowId+'\');">삭제</button>';
	return rtn;
}
function ImgUnFormatter(cellvalue,options,cell){
	return cellvalue;
}
function clearData(){
	
	/* 초기화 안함.
	$('#CUSTOMER_CODE').val(''); 
	$("#stamp01").val('0');
	$("#stamp02").val('0');
	$("#stamp03").val('0');
	$("#pocNo2").val('');
	*/
	setNextDivPOCNO2();
}

function setNextDivPOCNO2() {
	try {
		var currentPOCNO02 = $("#pocCheck").val().split('');
		currentPOCNO02[defultDivCharIndex] = String.fromCharCode( ( $("#pocCheck").val().charCodeAt(defultDivCharIndex)==48?64:$("#pocCheck").val().charCodeAt(defultDivCharIndex) ) +1  );
		var nextDivPOCNO02 = currentPOCNO02.join('');
		$("#pocCheck").val(nextDivPOCNO02);
		//alert(nextDivPOCNO02);
	} catch (e) {
		// TODO: handle exception
	}
}

function insertRow(){
	indexIdx++;
	$('#pocCheck').val($('#pocCheck').val().toUpperCase())
	
	var seqQ100 = $('#SEQ_Q100').val(); //업로드 파일 사이즈	
	var customerCode = $('#CUSTOMER_CODE').val(); 
	var ids = jQuery("#list1").getDataIDs();// jqGrid 전체 ROW의 ID  가져오기
	var podCheck =  $('#pocCheck').val();
	if(podCheck == ""){
		alert("분할 POC No는 필수값입니다. ");
		return;
	}
	
	var spanPOC = $('#SPAN_POC_NO').html().split("-");
	if(spanPOC.length==2) {
		if(podCheck.length<4) {
			alert("분할 POC No는 0000 형태이어야 합니다");
			return;
		}
	} else if(spanPOC.length==3) {
		if(podCheck.length<3) {
			alert("분할 POC No는 000 형태이어야 합니다");
			return;
		}
	}
		var checkPoc =document.popUpForm.POC_NO01.value +"-"+podCheck;
	
		var isExistData = parent.checkPocNo(checkPoc);
		if(isExistData){
			alert("QICS 작업목록에 해당 POC가 존재 합니다.");
			return;
		}
	
	
 
		var customer ="";
		var orderNumber ="";
		var lineNumber ="";
		var prodRemark ="";
		var isCompared = true;
		var ids = jQuery("#list1").getDataIDs();
		for(var i=0;i<ids.length;i++){		
    		var isData =$("#list1").getRowData(ids[i]);
    		if(podCheck == isData.POC_NO02){ 
    			alert("중복된 분할NO가 있습니다. 다시 입력해주세요.");
    			isCompared=false;
    			break;
    		}
		}
		if(isCompared){
			if(divideData.containsKey(customerCode)){
				var dataMap = divideData.get(customerCode);
				customer =dataMap.CUSTOMER;
				orderNumber =dataMap.ORDER_NUMBER;
				lineNumber =dataMap.LINE_NUMBER;
				prodRemark = dataMap.PROD_REMARK;
			} else {
				
				customer ="";
				orderNumber =$('#CUSTOMER_CODE').val();
				lineNumber ="";
				prodRemark = "";
			}
				var pocNo02 ="";
				var pocNo03="";
				pocNo02 = podCheck;
				pocNo03 = "";
				var stamp01 = $("#stamp01").val();
				var stamp02 = $("#stamp02").val();
				var stamp03 = $("#stamp03").val();
			    var checkData = stamp01+","+stamp02+","+stamp03+",";
				var stemtemp = checkData.split("0,").join("");
				var formCode="FORM_QICS_C";
				var meValue="";
				var me = $("input:checkbox[id='meValue']").is(":checked");// == true : false  /* by ID */
				 
				if(me){
					meValue="O";
					formCode="FORM_QICS_D";
				}
				//  $("input:checkbox[name='NAME']").is(":checked") == true : false /* by NAME */

				var selDatas = {sNo:'',
						sDelete:'',
						status:'입력',
						sStatus:'I',
						POC_NUMBER:$("#pocCheck").val(),
						POC_NO02:pocNo02,
						POC_NO03:pocNo03,
						CUSTOMER:customer,
						ORDER_NUMBER:orderNumber,
						LINE_NUMBER:lineNumber,
						PROD_REMARK:prodRemark,
						STAMP01_NM:$("#stamp01").val() =="0"?"":$("#stamp01").val(),
						STAMP02_NM:$("#stamp02").val()=="0"?"":$("#stamp02").val(),
						STAMP03_NM:$("#stamp03").val()=="0"?"":$("#stamp03").val(),
						CHECK_DATA:stemtemp,ME:meValue,
						FORM_CODE:formCode,
						MRG_DISCRETE_NUMBER:defaultData.MRG_DISCRETE_NUMBER,
						MRG_LINE_CODE:defaultData.MRG_LINE_CODE,
						MRG_STEEL_TYPE:defaultData.MRG_STEEL_TYPE,
						MRG_R_SUPPLIER:defaultData.MRG_R_SUPPLIER,
						MRG_WIP_ENTITY_NAME:defaultData.MRG_WIP_ENTITY_NAME,
						MRG_R_SUPPLY_THICKNESS:defaultData.MRG_R_SUPPLY_THICKNESS,
						MRG_R_WIDTH:defaultData.MRG_R_WIDTH,
						PARTIAL_WEIGHT:defaultData.PARTIAL_WEIGHT,
						TARGET_THICKNESS:defaultData.TARGET_THICKNESS,
						AIM_THICKNESS:defaultData.AIM_THICKNESS,
						AIM_TOL:defaultData.AIM_TOL,
						JOB_DESCRIPTION:defaultData.JOB_DESCRIPTION
				};
				var selRow = jQuery("#list1").getGridParam("selrow");//jqGrid의 선택된 로우 ID를 가져온다
				jQuery("#list1").addRowData(indexIdx,selDatas,'last',selRow); // 선택된 로우의 다음 
			}
		clearData();
		
		var rowCnt = $('#list1').jqGrid('getGridParam','records');
		if(rowCnt == 0){
			nodataMsgDiv1.show();
		} else {
			nodataMsgDiv1.hide();
		}
}
function deleteRow(rowId){
	jQuery("#list1").delRowData(rowId);
	var rowCnt = $('#list1').jqGrid('getGridParam','records');
	if(rowCnt == 0){
		nodataMsgDiv1.show();
	} else {
		nodataMsgDiv1.hide();
	}
}
function penWorkDirect(){
	var paramUrl="";
	var ids = jQuery("#list1").getDataIDs();

	var idx =0;
	var ch = $("#list1").find("#"+ids[i]+' input[type=checkbox]').prop('checked');
	
	for(var i=0;i<ids.length;i++){
		var cols = $("#list1").getRowData(ids[i]);
		for(var key in cols){
 
			if(idx == 0){
				paramUrl = paramUrl+key+"="+ encodeURIComponent(cols[key]);     
			}else{
					paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);     	
			}
			idx++;
		}
 
	}
	if(paramUrl == ""){
		alert("분할정보가 올바르지 않습니다. 다시 확인해주세요.");
		return;
	}
	
	$("#print").hide();
 	$("#direct").hide();
	
	try {
		$("#list1").jqGrid("hideCol", ["DEL_ROW"]); 
	} catch (e) {
		// TODO: handle exception
	}
	
	jqGrid_SaveName(document.popUpForm,'list1');
	var result = ajaxCall("/PenWork.do?cmd=penWorkFinalDirect", paramUrl+"&"+$("#popUpForm").serialize(),false);
	var okCount=0;
	var errorCount=0;
	for(var i = 0; i<result.DATA.length; i++){
	    var dataTemp = result.DATA[i];
	    if(dataTemp.RESULT =="Y"){
	    	okCount++;
	    }else{
	    	errorCount++;
	    }
	}
	if(result.DATA.length ==okCount){
		alert("총 "+okCount +"건의 검사결과표 양식이 생성되었습니다." );
	}else{
		alert("총 "+result.DATA.length +"건의 검사결과표생성 중      \n\n - 성공 : "+okCount+"건 \n - 실패 : "+errorCount+"건 입니다.     \n\n※ 검사결과표 생성에 실패한 "+errorCount+"건은 긴급작업 또는 분할양식추가 기능을 통해 다시 생성해주세요." );
	}
	callPenWorkList();
	closePopUp('pop_up_banner');
	 
}
function penWorkPrint(){
 

	var paramUrl="";
	var ids = jQuery("#list1").getDataIDs();

	var idx =0;
	var ch = $("#list1").find("#"+ids[i]+' input[type=checkbox]').prop('checked');
	
	for(var i=0;i<ids.length;i++){
		var cols = $("#list1").getRowData(ids[i]);
		for(var key in cols){
 
			if(idx == 0){
				paramUrl = paramUrl+key+"="+ encodeURIComponent(cols[key]);     
			}else{
					paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);     	
			}
			idx++;
		}
 
	}
	if(paramUrl == ""){
		alert("분할정보가 올바르지 않습니다. 다시 확인해주세요.");
		return;
	}

	jqGrid_SaveName(document.popUpForm,'list1');
	createImport(paramUrl);
	/*var result = ajaxCall("/PenWork.do?cmd=penWorkFinalPrint", paramUrl+"&"+$("#popUpForm").serialize(),false);
	var okCount=0;
	var errorCount=0;
	for(var i = 0; i<result.DATA.length; i++){
	    var dataTemp = result.DATA[i];
	    if(dataTemp.RESULT =="Y"){
	    	okCount++;
	    }else{
	    	errorCount++;
	    }
	}
	if(result.DATA.length ==okCount){
		alert("총 "+okCount +"건의 검사결과표 양식이 생성되었습니다." );
	}else{
		alert("총 "+result.DATA.length +"건의 검사결과표생성 중      \n\n - 성공 : "+okCount+"건 \n - 실패 : "+errorCount+"건 입니다.     \n\n※ 검사결과표 생성에 실패한 "+errorCount+"건은 긴급작업 또는 분할양식추가 기능을 통해 다시 생성해주세요." );
	}
	parent.callPenWorkList();*/
	
}
function createImport(paramUrl){
	$("#print").hide();
 	$("#direct").hide();
	
	try {
		$("#list1").jqGrid("hideCol", ["DEL_ROW"]); 
	} catch (e) {
		// TODO: handle exception
	}
	
	progressStart("양식생성에 필요한 정보를 수집중입니다.<br/>잠시만 기다려 주세요",function() {
		var result = ajaxCall("/PenWork.do?cmd=penWorkFinalPrint", paramUrl+"&"+$("#popUpForm").serialize(),false);
		progressStop();
		var tempSeq ="";
		for(var i = 0; i<result.DATA.length; i++){
			 var dataTemp = result.DATA[i];
			    if(dataTemp.RESULT =="Y"){
			    	tempSeq=tempSeq+dataTemp.SEQ_T300+",";
			    }else{
			    }
		}
		callHostUI("psprint", tempSeq);
		var delayTime =0;
		progressStart('검사결과표 ('+result.DATA.length+' 건)을 생성중입니다.<br/>잠시만 기다려 주세요', 
				function() {
					progressStop();
					createSampleFormFinish(result); 
				}, delayTime
			);
	});
}
function createSampleFormFinish(result){
	var okCount=0;
	var errorCount=0;
	for(var i = 0; i<result.DATA.length; i++){
		 var dataTemp = result.DATA[i];
		    if(dataTemp.RESULT =="Y"){
		    	okCount++;
		    }else{
		    	errorCount++;
		    }
	}
	if(result.DATA.length ==okCount){
		alert("총 "+okCount +"건의 검사결과표 양식이 생성되었습니다." );
	}else{
		alert("총 "+result.DATA.length +"건의 검사결과표생성 중      \n\n - 성공 : "+okCount+"건 \n - 실패 : "+errorCount+"건 입니다.     \n\n※ 검사결과표 생성에 실패한 "+errorCount+"건은 긴급작업 또는 분할양식추가 기능을 통해 다시 생성해주세요." );
	}
	callPenWorkList();
	closePopUp('pop_up_banner');
}	
function createStamp(){
	var result =  ajaxCall("/Process.do?cmd=qicsStampList", $("#frameForm").serialize(), false);  //작업내역 상세검색
	stampData(result);
}
function stampData(result){
	
    var str1='<option value="0">없음</option>';
    var str2='<option value="0">없음</option>';
    var str3='<option value="0">없음</option>';
	if(result.DATA.length > 0 ){
		
		for(var i = 0; i<result.DATA.length; i++){
			 
			if(result.DATA[i].ATTRIBUTE2 == "최종검사외주"){
				str1 = str1+"<option value='"+result.DATA[i].CODE+"'>"+result.DATA[i].CODE+" </option>";
				textStampMap.put(result.DATA[i].CODE,result.DATA[i].CODE);
			}
			if(result.DATA[i].ATTRIBUTE2 == "최종검사차공정"){
				str2 = str2+"<option value='"+result.DATA[i].CODE+"'>"+result.DATA[i].CODE+" </option>";
			}
			if(result.DATA[i].ATTRIBUTE2 == "최종검사자동차"){
				str3 = str3+"<option value='"+result.DATA[i].CODE+"'>"+result.DATA[i].CODE+" </option>";
			}
		}
	}
	$("#stamp01").append(str1);
	$("#stamp02").append(str2);
	$("#stamp03").append(str3);
}
function changeStamp(){
	var customerCode = $('#CUSTOMER_CODE').val(); 
	var ccDefineName ="";
	if(divideData.containsKey(customerCode)){
		
		var dataMap = divideData.get(customerCode);
		customer =dataMap.CUSTOMER;
		ccDefineName=dataMap.CC_DEFINE_NAME;
		if(textStampMap.containsKey(ccDefineName)){
			$("#stamp01").val(ccDefineName);
		}else{
			$("#stamp01").val("0");
		}
		if(customer.indexOf("자동차") > -1){
			$("#stamp03").val("자동차");
		}else{
			$("#stamp03").val("0");
		}
	}else{
		$("#stamp01").val("0");
		$("#stamp03").val("0");
	}
}
</script>

</head>
<body>
<div class="wrapper">

<!-- Popup Area -->
<div class="modal-dialog modal-xl">
  <div class="modal-content">  
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onclick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label>분할양식추가</label>
      </h3>
      <span></span>
    </div>
    <!-- ./modal-header -->
    
    <div class="modal-body">
    
    		<div class="row">
                <div class="col-lg-12">
                  <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>작업기본정보</h5>
                    </div>
                    <div class="ibox-content">
                        <table class="table table-bordered green">
                            <thead>
                            <tr>
                                <th colspan="4"><div class="th-text">POC NO</div>
                                	<span id ="SPAN_POC_NO"></span>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><div class="td-text">공정</div>
                                	<span id ="SPAN_ORDER"></span>
                                </td>
                                <td><div class="td-text">라인</div>
                                	<span id ="SPAN_LINE"></span>
                                </td>
                                <td><div class="td-text">강종</div>
                                	<span id ="SPAN_STEEL_TYPE"></span>
                                </td>
                                <td><div class="td-text">원재사</div>
                                    <span id ="SPAN_R_SUPPLIER"></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3"><div class="td-text">소재 (두께 x 폭)</div>
                                	<span id ="SPAN_R_SUPPLY_THICKNESS"></span> X <span id ="SPAN_R_WIDTH"></span> 
                                </td>
                                <td><div class="td-text">상태</div>
                                	<span id ="STATUS_NM"></span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                  </div>
                  <!-- ./ibox -->
            	</div>
                <!-- ./col -->
            </div>
            <!-- ./row -->
            
            <div class="row">
                <div class="col-lg-12">
                  <div class="ibox float-e-margins m-b-none">
                    <div class="ibox-title">
                       	<h5>분할정보</h5>
                    </div>
                    <div class="ibox-content">
                    	<!-- Search Area -->
                    	<div class="ibox-search m-b-sm">
                      	<form id="popUpForm" name="popUpForm"  class="form-horizontal">
                      	    <input type="hidden" id="SEQ_Q100" name="SEQ_Q100"   />
                      	    <input type="hidden" id="SEQ_T300" name="SEQ_T300"   />
			        		<input type="hidden" id="POC_NO" name="POC_NO"  />
			        		<input type="hidden" id="POC_NO01" name="POC_NO01"  />
							<input type="hidden" id="WORK_DATE" name="WORK_DATE"  />
							<input type="hidden" id="IN_LINE" name="IN_LINE"   />
							<input type="hidden" id="FORM_TYPE" name="FORM_TYPE"   />
							<input type="hidden" id="CHECK_DATA" name="CHECK_DATA"   />
							<input type="hidden" id="IN_FACT" name="IN_FACT"   />
							<input type="hidden" id="IN_ORDER" name="IN_ORDER"   />
							<input type="hidden" id="NEW_FINAL" name="NEW_FINAL"  value="N"  />
 							<input type="hidden" id="WIP_ENTITY_NAME" name="WIP_ENTITY_NAME" />
 
                    		<div class="form-group m-b-sm">
                        		<label class="col-sm-2 control-label">분할NO</label>
                            	<div class="col-sm-3">
                            		<input type="text" id="pocCheck"  name="pocCheck" class="form-control input-sm" style="ime-mode:disabled;text-transform:uppercase;">
                            	</div>
                            	<div class="col-sm-4">
                  					<select id="CUSTOMER_CODE"  class="form-control input-sm" onchange="javascript:changeStamp();">
                  					</select> 
                            	</div>
                            	<div class="col-sm-2">
            						<div class="pull-right">
                                		<div class="checkArea"><label><span>M/E</span> <input type="checkbox"  id="meValue"  name="meValue"></label></div>
                                    </div> 
                                </div>
                            	<div class="col-sm-1"></div>
            				</div>
                            <div class="form-group">
                            	<label class="col-sm-2 control-label">알림정보&nbsp;&nbsp;&nbsp;외주</label>
                            	<div class="col-sm-2">
                  					<select id="stamp01" class="form-control input-sm">
                  					</select> 
                            	</div>
                            	<label class="col-sm-1 control-label">차공정</label>
                            	<div class="col-sm-2">
                  					<select  id="stamp02" class="form-control input-sm">
                  					</select>
                            	</div>
                            	<label class="col-sm-1 control-label">자동차</label>
                            	<div class="col-sm-2">
                  					<select id="stamp03" class="form-control input-sm">
                  					</select> 
                            	</div>
                            	<div class="col-sm-2">
                            		<button type="button" class="btn btn-primary pull-left width80px" onclick="javascript:insertRow();">추가</button>
                            	</div>
                        	</div>
                      	</form>
                        </div>
                      	<!-- ./Search Area -->
                        
				        <div style="height:210px;"> 
							<div style="height:210px;">
								<table id="list1"></table>
								<div id="paginate"></div>
							</div>
						</div>  
                        
                    </div>
                    <!-- ./ibox-content -->
                    
                  </div>
                  <!-- ./ibox -->
            	</div>
                <!-- ./col -->
            </div>
            <!-- ./row -->
              
    </div>
    <!-- ./modal-body-->
    
    <div class="modal-footer">
    	<div class="row">
			<div class="col-xs-12">
				<button type="button"  id="print"  class="btn btn-primary pull-right" onclick="javascript:penWorkPrint();"><i class="fa fa-print"></i> 인쇄요청</button>
				<button type="button"  id="direct" class="btn pull-right m-r-xs" onclick="javascript:penWorkDirect();"><i class="fa fa-edit"></i> 직접입력</button>
			</div>
		</div>
    </div>
    
  </div>
  <!-- ./modal-content -->
</div>
<!-- ./modal-dialog -->

</div>
</body>
</html>