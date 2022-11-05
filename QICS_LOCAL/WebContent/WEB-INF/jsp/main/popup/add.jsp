<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 1</title>
<style type="text/css">
    th {
    	text-align: center;
	}
</style>
<script type="text/javascript">
	//var p = eval("top");
var iLineMap=new HashMap();
var iFactMap=new HashMap();
var iOperMap=new HashMap();
var nodataMsgDiv1 =$('<div class="search_result_none">조회된 데이터가 존재 하지 않습니다.</div>');

	$(document).ready(function() {		
		document.popUpForm.IN_FACT.value = document.mySheetForm.IN_FACT.value;
		document.popUpForm.IN_ORDER.value = document.mySheetForm.IN_ORDER.value;
		document.popUpForm.IN_LINE.value = document.mySheetForm.IN_LINE.value;
		document.popUpForm.WORK_DATE.value = document.mySheetForm.WORK_DATE.value;
		var result = ajaxCall("/Process.do?cmd=processList", $("#popUpForm").serialize(), false); //작업공정 I/F 조회
		var str="<option value=''>선택</option>";

		if(result.DATA.length > 0 ){
			for(var i = 0; i<result.DATA.length; i++){
				iLineMap.put(result.DATA[i].CODE,result.DATA[i].CODE);
				iFactMap.put(result.DATA[i].CODE,result.DATA[i].ATTRIBUTE2);
				iOperMap.put(result.DATA[i].CODE,result.DATA[i].ATTRIBUTE1);
				str =str+"<option value='"+result.DATA[i].CODE+"'>"+result.DATA[i].CODE+" </option>";
			}
		}else{
			str = "<option value=''>없음</option>";
		}
		$("#POP_LINE").append(str);  
		$("#POP_LINE").val($("#IN_LINE").val());  
		result = ajaxCall("/Process.do?cmd=steelGroupList", $("#popUpForm").serialize(), false); //작업공정 I/F 조회
		str="<option value=''>전체 </option>";
 
		if(result.DATA.length > 0 ){
			for(var i = 0; i<result.DATA.length; i++){
				str =str+"<option value='"+result.DATA[i].CODE+"'>"+result.DATA[i].CODE+" </option>";
			}
		}
		$("#IN_STEEL_GROUP").append(str);  
		
		$("#IN_POCNO").css({"ime-mode":"disabled","text-transform":"uppercase"});
		
		
		initJqGrid1();
 	   /* $(window).bind('resize',function(){
 		//  $('#list1').setGridWidth(960, true); 
 	 	  $('#list1').setGridHeight(220, true); //
 		}).trigger('resize');
 	   $(window).resize();*/
    	 initFormatter('list1');
		 
	});


	function initFormatter(grid_id){
		var orgColModel =  $("#"+grid_id).jqGrid('getGridParam','colModel');
 		
		for(var i =0; i<orgColModel.length;i++){
		//	 alert("orgColModel[i].userfomatter = "+orgColModel[i].userfomatter);
			if(orgColModel[i].userfomatter =='text'){
			
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {editoptions:{dataEvents:[{ type:'change' , fn:function(e){evenFn(this.id, this.value,grid_id);}},{ type:'blur' , fn:function(e){evenFn(this.id, this.value,grid_id);}}]}});
			
			}else if(orgColModel[i].userfomatter =='select'){
				
				if(orgColModel[i].name =="sStatus") {
					tempStr = 'I:입력;U:수정;D:삭제;E:오류';
				}else if(orgColModel[i].name =="IN_TYPE"){
					tempStr = 'START:시작;PREPARE:준비;ALL:전체';
				}else {
					tempStr = '';
				}
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter:'select',edittype:"select",editoptions:{value:tempStr,dataEvents:[{ type:'change' , fn:function(e){evenFn(this.id, this.value,grid_id);}},{ type:'blur' , fn:function(e){evenFn(this.id, this.value,grid_id);}}]}});
			}else if(orgColModel[i].userfomatter =='check'){
				$("#" + grid_id).jqGrid('setColProp', orgColModel[i].name, {formatter:cboxFormatter,formatoptions:{disabled:false}});
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
			colNames : ['No','삭제','상태','<div onclick="javascript:checkAllGrid();">선택</div>','현공정','강종','원재사','POC No','두께','폭','중량' ,'공칭G','Aiming','G범위','지시사항','순번','작지번호','공장', '공정', '라인', '상태', 'POC NO','POC NO1','POC NO2' ,'POC NO3' ,'SEQ_TEMP', 'FORM_CODE' ],
			
			colModel : [
				{name:  'sNo', index:'sNo', width:40, hidden:true },        
				{name : 'sDelete', index:'sDelete',  width:40, align:'center', hidden:true},
				{name:  'sStatus', index:'sStatus', width:40, hidden:true,  	editable:false,userfomatter:'select' },
				{name:  'select', index:'select', width:80, align:'center', hidden:false,  	editable:false, frozen:true,  userfomatter:'check' },
				{name : 'MRG_LINE_CODE', 		index : 'MRG_LINE_CODE', 		width : 50, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'MRG_STEEL_TYPE', 		index : 'MRG_STEEL_TYPE', 		width : 60, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'MRG_R_SUPPLIER', 		index : 'MRG_R_SUPPLIER', 		width : 100, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'MRG_WIP_ENTITY_NAME', 		index : 'MRG_WIP_ENTITY_NAME', 		width : 200, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'MRG_R_SUPPLY_THICKNESS', 		index : 'MRG_R_SUPPLY_THICKNESS', 		width : 50, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'MRG_R_WIDTH', 		index : 'MRG_R_WIDTH', 		width : 70, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'PARTIAL_WEIGHT', 		index : 'PARTIAL_WEIGHT', 		width : 70, 		align:'center',editable:false,frozen:true, userfomatter:'int'  },
				{name : 'TARGET_THICKNESS', 		index : 'TARGET_THICKNESS', 		width : 70, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'AIM_THICKNESS', 		index : 'AIM_THICKNESS', 		width : 70, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'AIM_TOL', 		index : 'AIM_TOL', 		width : 70, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'JOB_DESCRIPTION', 		index : 'JOB_DESCRIPTION', 		width : 350, 		align:'left',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'MRG_SEQ', 	index : 'MRG_SEQ', 	width : 50, 	align:'center',hidden:false, frozen:true, userfomatter:'text' },           
				{name : 'MRG_DISCRETE_NUMBER', 		index : 'MRG_DISCRETE_NUMBER', 		width : 150, 		align:'center',editable:false,frozen:true, userfomatter:'text'  },
				{name : 'IN_FACT', 		index : 'IN_FACT', 		width : 80, 	align:'center', hidden:true,editable:false,frozen:true, userfomatter:'text'  },
				{name : 'IN_ORDER', 	index : 'IN_ORDER', 	width : 80, 		align:'center', hidden:true,editable:false,frozen:true, userfomatter:'text' },
				{name : 'IN_LINE', 		index : 'IN_LINE', 		width : 80,	align:'center',		 hidden:true,   editable:false,frozen:true, userfomatter:'text'  },
				{name : 'INTYPE', 			index : 'INTYPE', 			width : 100,  		align:'center', hidden:true, editable:false ,frozen:true, userfomatter:'select'  },
				{name : 'POC_NO', 		index : 'POC_NO', 		width : 100, 		align:'center', hidden:true, editable:false,frozen:true, userfomatter:'text'  },
				{name : 'POC_NO01', 		index : 'POC_NO01', 		width : 100, 		align:'center', hidden:true, editable:false,frozen:true, userfomatter:'text'  },
				{name : 'POC_NO02', 		index : 'POC_NO02', 		width : 100, 		align:'center', hidden:true, editable:false,frozen:true, userfomatter:'text'  },
				{name : 'POC_NO03', 		index : 'POC_NO03', 		width : 100, 		align:'center', hidden:true, editable:false,frozen:true, userfomatter:'text'  },
				{name:  'SEQ_TEMP', index:'SEQ_TEMP', width:40, hidden:true },       
				{name:  'FORM_CODE', index:'FORM_CODE', width:40, hidden:true }       
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
		
	}
	/*Sheet Action*/
	function doAction(sAction) {
		switch (sAction) {
			case "Search": //조회
				var key = $("#POP_LINE").val();
				if(iFactMap.containsKey(key)){
					//$("#IN_FACT").val(iFactMap.get(key));
 
					document.popUpForm.IN_FACT.value=iFactMap.get(key);
				}
				if(iOperMap.containsKey(key)){
					//$("#IN_ORDER").val(iOperMap.get(key));
					
					document.popUpForm.IN_ORDER.value=iOperMap.get(key);
				}
				//$("#IN_LINE").val(key);
			//	document.popUpForm.IN_FACT.value = document.mySheetForm.IN_FACT.value;
			//	document.popUpForm.IN_ORDER.value = document.mySheetForm.IN_ORDER.value;
				document.popUpForm.IN_LINE.value = key;
			//	alert($("#IN_FACT").val() +" : "+$("#IN_ORDER").val()+" : " +$("#IN_LINE").val());
			var me = $("input:checkbox[id='gubunValue']").is(":checked");// == true : false  /* by ID */    
			if(me){
				$("#IN_GUBUN").val("Y");
			}else{
				$("#IN_GUBUN").val("N");
			}
	
			    $("#IN_POCNO").val($("#IN_POCNO").val().toUpperCase());
			
				callSendReload( "/Add.do?cmd=addList", 'popUpForm', 'list1' );
	            break;
			case "Save"			:	preCheck();	    break;
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
		    	}
		    	$("#"+jqgrid_id).jqGrid('setRowData',rowId,rowdata); 
		    }
		    
function checkAllGrid(){
	var ids = jQuery("#list1").getDataIDs();
	var selectIdx ="0";
	var statusCode="";
	var isStatus = true;
	// var isStatus = $("input:checkbox[id='checkAll']").is(":checked");
//	alert("1111111111111");
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
function preCheck(){
	var resultMn = dateCheck();
	if(resultMn != ""){
		if(confirm(resultMn)){
			saveRow();
		}
	}else{
		saveRow();
	}
}
function saveRow(){
	
	var temp = new Array();
	var ids = jQuery("#list1").getDataIDs();
	var idx =0;
	var paramUrl="";
	var tempData = '';
	var isError = false;
	var isErrMsg = "";
	
	var paramMap		= new Map();
	
	for(var i=0;i<ids.length;i++){
		var cols = $("#list1").getRowData(ids[i]);
		tempData="";
		
		var ch = $("#list1").find("#"+ids[i]+' input[type=checkbox]').prop('checked');
		
		if(ch){
			var inLine = document.popUpForm.IN_LINE.value;
			var inFact =""
			var inOrder="";
			if(iFactMap.containsKey(inLine)){
				inFact=iFactMap.get(inLine);
			}
			if(iOperMap.containsKey(inLine)){
				inOrder=iOperMap.get(inLine);
			}
			
			$('#lineTitle').text(inLine);
			document.mySheetForm.IN_LINE.value = inLine;
			
			for(var key in cols){
					if(idx == 0){
						if(key == "IN_FACT"){
							paramUrl = paramUrl+key+"="+ encodeURIComponent(inFact);     
						}else if(key == "IN_ORDER"){
							paramUrl = paramUrl+key+"="+ encodeURIComponent(inOrder);     
						}else if(key == "IN_LINE"){
							paramUrl = paramUrl+key+"="+ encodeURIComponent(inLine);
						}else if(key == "FORM_CODE"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);   
							paramMap.set('QC_DESIGN_ID', encodeURIComponent(cols[key]));
						}else if(key == "MRG_WIP_ENTITY_NAME"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);   
							paramMap.set('QC_NO', encodeURIComponent(cols[key]));
						}else if(key == "SEQ_TEMP"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);   
							paramMap.set('SEQ_TEMP', encodeURIComponent(cols[key]));
						}else{
							paramUrl = paramUrl+key+"="+ encodeURIComponent(cols[key]);     
						}
      					
      				}else{
      					if(key == "IN_FACT"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(inFact);     
						}else if(key == "IN_ORDER"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(inOrder);     
						}else if(key == "IN_LINE"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(inLine);     
						}else if(key == "FORM_CODE"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);   
							paramMap.set('QC_DESIGN_ID', encodeURIComponent(cols[key]));
						}else if(key == "MRG_WIP_ENTITY_NAME"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);   
							paramMap.set('QC_NO', encodeURIComponent(cols[key]));
						}else if(key == "SEQ_TEMP"){
							paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);   
							paramMap.set('SEQ_TEMP', encodeURIComponent(cols[key]));
						}else{
      						paramUrl = paramUrl+"&"+key+"="+ encodeURIComponent(cols[key]);    
						}
      				}
				
					idx++;
					//tempData =" "+ tempData + key+" = "+cols[key]+" ";
			}
			// 임시코드 : 테스트 후 삭제 end
			$("#tempData").append(tempData+"<br>");
       		
		}
	}	
	if(paramUrl == ""){
		alert("선택된 작업이 없습니다.");
		return false;
	}

	let baseCopyNo = prompt('검사지 번호를 입력해주세요.', '검사지 번호');
	paramMap.set('BASE_COPY_NO', baseCopyNo);
	qcMapping(paramMap);
	
	/* 이 부분은 임시코드 삭제 후 원복시켜야할 부분 */
	if(confirm("선택된 POP작업지시건을 QICS작업목록에 추가하시겠습니까?")){
		
		jqGrid_SaveName(document.popUpForm,'list1');
	
		
		var result = ajaxCall("/Add.do?cmd=addInsert", paramUrl+"&"+$("#popUpForm").serialize(),false);
		var message = ""; 
 
		if(parseInt(result.Result.errorCount)> 0){
			message = "QICS작업목록 추가 결과는 아래와 같습니다.\n해당 POC번호를 다시 확인바랍니다. \n\n - 작업추가요청건수 : "+result.Result.totalCount+"건\n\n - 정상추가 : "+(parseInt(result.Result.totalCount)-parseInt(result.Result.errorCount))+"건, \n\n - 실패 : "+result.Result.errorCount+"건 입니다.\n\n※ 이미 등록된 POC번호 입니다.\n POC_NO : "+ result.Result.existPocNo;
			alert(message); 
		}else{
			message = "총 "+result.Result.totalCount +"건의 작업지시건이 QICS작업목록에 추가되였습니다."
			alert(message); 
		}
		parent.callPenWorkList();
		closePopUp('pop_up_banner');
	}

}

//임시코드 : 테스트 후 삭제 start #권민 #울트라캡숑
// 임시 프로세스 QC_MAPPING 테이블 레코드 생성
function qcMapping(param){
	$('<form id="qcMappingForm" method="post"></form>').appendTo('body');
	$('<input type="hidden" name="QC_DESIGN_ID" value="' + param.get('QC_DESIGN_ID') + '" />').appendTo($('#qcMappingForm'));
	$('<input type="hidden" name="QC_NO" value="' + param.get('QC_NO') + '" />').appendTo($('#qcMappingForm'));
	$('<input type="hidden" name="BASE_COPY_NO" value="' + param.get('BASE_COPY_NO') + '" />').appendTo($('#qcMappingForm'));
	$('<input type="hidden" name="SEQ_TEMP" value="' + param.get('SEQ_TEMP') + '" />').appendTo($('#qcMappingForm'));
	
	var result = ajaxCall("/Add.do?cmd=addQcMapping", $("#qcMappingForm").serialize(),false);
	var message = ""; 
/*	
	if(parseInt(result.Result.resultCnt) < 0){
		message = "QC_MAPPING 실패";
		alert(message); 
	}else{
		message = result.Result.Message;
		alert(message); 
	}
	console.log(result);
*/
}
//임시코드 : 테스트 후 삭제 end #권민 #울트라캡숑

</script>

</head>
<body class="hold-transition skin-blue layout-top-nav">

<div class="wrapper">
<!-- Popup Area -->
<div class="modal-dialog modal-lg" style="width:1031px">
  <div class="modal-content">  
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onclick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label>작업추가 - 작업지시목록</label>
      </h3>
    </div>
    <!-- ./modal-header -->
    <div class="modal-body">
      
    	<!-- Search Area -->
    	  <form  id="popUpForm" name="popUpForm" class="form-horizontal">
        <div class="pbox-search m-b">
			<input type="hidden" id="IN_FACT" name="IN_FACT" />
			<input type="hidden" id="IN_ORDER"  name="IN_ORDER" />
			<input type="hidden" id="IN_LINE"  name="IN_LINE" />
			<input type="hidden" id="IN_GUBUN"  name="IN_GUBUN" />
			<input type="hidden" id="WORK_DATE" name="WORK_DATE" />
            <div class="form-group m-b-sm">
            	<label class="col-sm-1 control-label">POC No</label>
            	<div class="col-sm-3">
            		<input type="text" id="IN_POCNO"  name="IN_POCNO" class="form-control input-sm" >
            	</div>
            	<label class="col-sm-1 control-label">작지번호</label>
            	<div class="col-sm-4">
            		<input type="text" id="IN_DISCRETE_NUMBER"  name="IN_DISCRETE_NUMBER"  class="form-control input-sm" value="">
            	</div>
            	<label class="col-sm-1 control-label">대강종</label>
            	<div class="col-sm-2">
            		<select id="IN_STEEL_GROUP" name="IN_STEEL_GROUP" class="form-control input-sm">
            		</select> 
                </div>
            </div>
            <div  class="form-group m-b-sm">
                <!--<label class="col-sm-1 control-label">공정</label>
                <div class="col-sm-1">
                <select id="IN_ORDER" name="IN_ORDER" class="form-control input-sm">
                	<option value="BA1">BA1</option>
                	<option value="">옵션1</option>
                	<option value="">옵션2</option>
                </select> 
                </div> -->
                <label class="col-sm-1 control-label">공정</label>
                <div class="col-sm-1">
                <select  id="POP_LINE"  name="POP_LINE" class="form-control input-sm">
                </select> 
                </div>
                <label class="col-sm-1 control-label">상태</label>
                <div class="col-sm-2">
                <select id="IN_TYPE" name="IN_TYPE"  class="form-control input-sm">
                	<option value="">전체</option>
                	<option selected="selected" value="START">시작</option>
                	<option value="PREPARE">준비</option>
                </select> 
                </div>
               <!--  <label class="col-sm-1 control-label">차공정</label>
                <div class="col-sm-1">
                <select id="IN_UT_LINE" name="IN_UT_LINE" class="form-control input-sm">
                	<option value="">전체</option>
                	<option value="">옵션1</option>
                	<option value="">옵션2</option>
                </select> 
                </div>--> 
                <label class="col-sm-1 control-label">표면</label>
                <div class="col-sm-1">
                	<input type="text"  id="IN_SURFACE" name="IN_SURFACE" class="form-control input-sm" >
                </div>
                <label class="col-sm-3 control-label">작업대기건 우선조회여부</label>
                <div class="col-sm-1">
                	<div class="checkArea"><input type="checkbox"  id="gubunValue"  name="gubunValue" checked="checked" ></div>
                </div>
                <div class="col-sm-1">
                	<button type="button" class="btn btn-primary btn-sm pull-right" onClick="javascript:doAction('Search');"><i class="fa fa-search"></i> 검색</button>
                </div>
			</div>
			<!-- 
            <div class="form-group">
            	<div class="col-sm-12">
                	<button type="button" class="btn btn-primary btn-sm pull-right width80px" onclick="javascript:doAction('Search');">검색</button>
                </div>
            </div>
             -->
            </form>
		</div>
        <!-- ./Search Area -->
		
        <div> 
			<div>
				<table id="list1"></table>
				<div id="paginate"></div>
			</div>
		</div>      
		
        <div class="pbox-footer"> 
        	<div class="row">
            	<div class="col-xs-12">
                	<button type="button" class="btn btn-info pull-right" onclick="javascript:closePopUp('pop_up_banner');">닫기</button>
                  	<button type="button" class="btn btn-success pull-right m-r-xs"  onclick="javascript:doAction('Save');">선택항목 작업추가</button>
              	</div>
            </div>
		</div> 
              
    </div>
    <!-- ./modal-body-->
  </div>
  <!-- ./modal-content -->
</div>
<!-- ./modal-dialog -->

</div>

</body>
</html>