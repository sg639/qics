//HashMap 추가
/*
 *  사용 예제
 *  var tempMap= new HashMap();
 *  tempMap.containsKey(key) // key 존재여부 확인
 *
 * */
function HashMap() {
	 this.map = new Object();
	 this.put = function(key, value){
		 this.map[key] = value;
	 };

	 this.get = function(key){
		 return this.map[key];
	 };

	 this.containsKey = function(key){
		 return key in this.map;
	 };

	 this.containsValue = function(value){
		for(var prop in this.map){
			 if(this.map[prop] == value) {
				return true;
			}
		}
		return false;
	 };

	 this.isEmpty = function(key){
		 return (this.size() == 0);
	 };

	 this.clear = function(){
		for(var prop in this.map){
			 delete this.map[prop];
		}
	 };

	 this.remove = function(key){
		 delete this.map[key];
	 };

	 this.keys = function(){
		 var keys = new Array();
		 for(var prop in this.map){
			 keys.push(prop);
		 }
		 return keys;
	 };

	 this.values = function(){
		 var values = new Array();
		 for(var prop in this.map){
			 values.push(this.map[prop]);
		 }
		 return values;
	 };

	 this.size = function(){
		 var count = 0;
		 for (var prop in this.map) {
			 count++;
		 }
		 return count;
	 };

	 this.printMap = function(){
		 var str = "";
		 var keys = this.keys();

		 for(var i = 0; i < keys.length; i++){
			 if(i != 0) {
				 str += "\n";
			 }
			 str += '"';
			 str += keys[i];
		     str += '"';
		     str += ":";
		     if(this.get(keys[i])){
		    	 	str += '"';
		    	 	str += this.get(keys[i]);
		    	 	str += '"';
		     }else{
		    	 str += "null";
		     }
		 }
		 return str;
	  };
	}
/* ----------------------------------------------------------------------------
 * jqGrid 화면 사이즈 변경
 *
 * 입력 파라미터 -----
 * grid_id : jqGrid 명
 * div_id  : 해당 DIV 명
 * width : width

 * 사용예 ---
 * resizeJqGridWidth('list1','wrapper_div',300);


function resizeJqGridWidth(grid_id,div_id,width){

	$(window).bind('resize',function(){
		$('#'+grid_id).setGridWidth(width, true); //back to original width
		$('#'+grid_id).setGridWidth($('#'+div_id).width()-50 , true); //resize to new width as per window
		$('#'+grid_id).setGridHeight(width, true); //back to original width
		$('#'+grid_id).setGridHeight($('#'+div_id).height()-150 , true); //resize to new width as per window
	}).trigger('resize');

}
 --------------------------------------------------------------------------- */
function resizeJqGridWidth(grid_id,div_id,root_id){
	$(window).bind('resize',function(){
		$('#'+div_id).width($('.'+root_id).width()-55);
		$('#'+grid_id).setGridWidth($('#'+div_id).width(), true); //resize to new width as per window
		$('#'+grid_id).setGridHeight(($('#'+div_id).height()-150), true); //resize to new width as per window
	}).trigger('resize');

/*	$(window).bind('resize',function(){
		//$('#'+grid_id).setGridWidth(width, true); //back to original width
		//var tempWidth = $('#'+div_id).width()-30;
		//$('#'+div_id).width(tempWidth);

		$('#'+grid_id).setGridWidth(width, true); //back to original width
		$('#'+grid_id).setGridWidth($('#'+div_id).width()-50 , true); //resize to new width as per window
		//$('#'+grid_id).setGridHeight(width, true); //back to original width
		$('#'+grid_id).setGridHeight(width, true);
		$('#'+grid_id).setGridHeight($('#'+div_id).height()-150 , true); //resize to new width as per window
	}).trigger('resize');
*/
}

function resizeJqGridWidth2(grid_id,div_id,root_id){

	$(window).bind('resize',function(){
		$('#'+div_id).width($('.'+root_id).width()-55);
		$('#'+grid_id).setGridWidth(($('#'+div_id).width()-60)/2, true); //resize to new width as per window
		$('#'+grid_id).setGridHeight(($('#'+div_id).height()-150), true); //resize to new width as per window
	}).trigger('resize');

/*
	$(window).bind('resize',function(){
		$('#'+grid_id).setGridWidth(width, true); //back to original width
		$('#'+grid_id).setGridWidth(($('#'+div_id).width()-100)/2, true); //resize to new width as per window
		$('#'+grid_id).setGridHeight(width, true); //back to original width
		$('#'+grid_id).setGridHeight($('#'+div_id).height()-200, true); //resize to new width as per window
	}).trigger('resize');*/
}
function resizeJqGridWidth3(grid_id,div_id,root_id){
	$(window).bind('resize',function(){
		$('#'+div_id).width($('.'+root_id).width()-55);
		$('#'+grid_id).setGridWidth($('#'+div_id).width(), true); //resize to new width as per window
		$('#'+grid_id).setGridHeight(($('#'+div_id).height()-200)/2, true); //resize to new width as per window
	}).trigger('resize');
}
//jqGrid ROW 추가
function addRow(table_id,insertData){
	var ids = jQuery("#"+table_id).getDataIDs(); // jqGrid 전체 ROW의 ID  가져오기
	var selRow = 	jQuery("#"+table_id).getGridParam("selrow"); //jqGrid의 선택된 로우 ID를 가져온다
	if(selRow == null){ //선택된 ROW가 없을 경우
		jQuery("#"+table_id).addRowData(ids.length+1,insertData,'first');
	}else{
		jQuery("#"+table_id).addRowData(ids.length+1,insertData,'after',selRow);
	}
}

/* ----------------------------------------------------------------------------
 * URL호출 시 결과  페이지 리로드
 *
 * 입력 파라미터 -----
 * send_url : 호출할 url
 * form_name : form 명
 * grid_name : grid 명
 * 사용예 ---
 * callSendReload("/PrgMgr.do?cmd=getPrgMgrList", "mySheetForm", "list1");

 --------------------------------------------------------------------------- */
function callSendReload(send_url, form_name, jqgrid_id){
	//$("#"+jqgrid_id).clearGridData();

	$("#"+jqgrid_id).setGridParam({
		url:send_url,
		mtype:"POST",
		datatype:"json",
		postData:addFormData(form_name)
	}).clearGridData(true).trigger("reloadGrid");

}
/* ----------------------------------------------------------------------------
 * URL호출 시 결과  페이지 리로드
 *
 * 입력 파라미터 -----
 * send_url : 호출할 url
 * form_name : form 명
 * grid_name : grid 명
 * 사용예 ---
 * callSendReload("/PrgMgr.do?cmd=getPrgMgrList", "mySheetForm", "list1");
 */
function eventFn(id,value,jqgrid_id){
	var temp = id.split("_");
	var rowid = temp[0];
	var name = temp[1];
	var iCol =0;
	var orgColModel =  $("#"+jqgrid_id).jqGrid('getGridParam','colModel');
	for(var i=0; i<orgColModel.length; i++){
		if(orgColModel[i].name == name) {
			iCol = i;
		}
	}

	jQuery('#'+jqgrid_id).editCell(rowid,iCol,false);
}


function insertRow(selDatas,jqgrid_id){
	var ids = $("#"+jqgrid_id).getDataIDs();

	//var selDatas = {SEQNO:'',sDelete:'',sStatus:'I',ZGUBUN:'I',prgCd:'',prgNm:'',prgEngNm:'',prgPath:'',use:'',version:'',memo:'',dateTrackYn:'',logSaveYn:''};
	var selRow = 	$("#"+jqgrid_id).getGridParam("selrow");

	if(selRow == null){
		$("#"+jqgrid_id).addRowData(ids.length+1,selDatas,'first');
	}else{
		$("#"+jqgrid_id).addRowData(ids.length+1,selDatas,'after',selRow);
	}
}

function copyRow(jqgrid_id){
	var ids = $("#"+jqgrid_id).getDataIDs();
	var selRow = $("#"+jqgrid_id).getGridParam("selrow");
	if(selRow == null){

	}else{
		var  rowdata =$("#"+jqgrid_id).getRowData(selRow);// 선택된 로우의 정보를 가져온다
		rowdata.sStatus = 'I';
		$("#"+jqgrid_id).addRowData(ids.length+1,rowdata,'after',selRow);
	}
}

function callDataReload (jqgrid_id, jqgrid_data, jqgrid_page){
	$("#"+jqgrid_id).clearGridData(); //데이터 초기화
    $("#"+jqgrid_id).jqGrid('setGridParam', {
		data:jqgrid_data,
		page:jqgrid_page
	}).trigger("reloadGrid");
}
function callJsonReload (send_url, form_name, jqgrid_id){
	$("#"+jqgrid_id).clearGridData();

	$("#"+jqgrid_id).setGridParam({
		url:send_url,
		mtype:"POST",
		datatype:"json",
		treedatatype : 'json',
		postData:addFormData(form_name)
	}).clearGridData(false).trigger("reloadGrid");




}
function addFormData(form_name) {
    var arr = $('#'+form_name).serializeArray();

    var params = {};
    $.each(arr, function(){
        var jname;
        jQuery.each(this, function(i, val){
            if (i=="name") {
                    jname = val;
            } else if (i=="value") {
                    params[jname] = val;
            }
        });
    });
    return params;
}

/*
 * defaultDateFormatter
 * 20081001 을 2008-10-01
 * 년-월-일 형식으로 리턴
 *
 */
function defaultDateFormatter ( cellvalue, options, rowObject )
{
	var yymmdd = "";
	if(cellvalue && cellvalue!="") {
		if (cellvalue.indexOf(".") > -1)
        {  // yyyy.mm.dd
            ar = cellvalue.split(".");
            yy = ar[0];
            mm = ar[1];
            dd = ar[2];

            if (parseInt(mm) < 10){mm = "0" + parseInt(mm) ;}
            if (parseInt(dd) < 10){dd = "0" + parseInt(dd);}
            yymmdd = yy+"-"+mm+"-"+dd;
        }
        else if (cellvalue.indexOf("-") > -1)
        {// yyyy-mm-dd
            ar = cellvalue.split("-");
            yy = ar[0];
            mm = ar[1];
            dd = ar[2];

            if (parseInt(mm) < 10){mm = "0" + parseInt(mm) ;}
            if (parseInt(dd) < 10){dd = "0" + parseInt(dd);}
            yymmdd = yy+"-"+mm+"-"+dd;
        }
        else if (cellvalue.length == 8)
        {
            yy = cellvalue.substr(0,4);
            mm = cellvalue.substr(4,2);
            dd = cellvalue.substr(6,2);
            yymmdd = yy+"-"+mm+"-"+dd;
        }else {
        	yymmdd = cellvalue;
        }


	}
	return yymmdd;
}
function jqGrid_SaveName() {

    var param = arguments;
    if (param.length < 2 ) {
        alert("최하 두개의 인자가 필요합니다.");
        return;
    }

	$("#s_SAVENAME").remove();
	$("<input></input>",{id:"s_SAVENAME",name:"s_SAVENAME",type:"hidden"}).appendTo(param[0]);
	var header ="";

	var orgColModel =  $("#"+param[1]).jqGrid('getGridParam','colModel');

	for(var i=0;i<orgColModel.length;i++){
		if(orgColModel[i].name !='rn'){
			if(i == orgColModel.length-1){
				header = header+orgColModel[i].name;
			}else{
				header = header+orgColModel[i].name+",";
			}
		}

	}
	param[0].s_SAVENAME.value =header;

}