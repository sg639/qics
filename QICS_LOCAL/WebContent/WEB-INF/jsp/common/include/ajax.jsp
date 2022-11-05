<script type="text/javascript">
$(function()  {
  $(document).keypress(function(e) {if (e.keyCode == 13) return false; });
  /*
  $.extend($.validator.messages, {
      required: function(element,input) {
        var vtxt =  $(input).attr("vtxt");
        if(vtxt != "undefined" && vtxt != ""){
          return vtxt+"<fmt:message key='jquery.valdate.required.existid'/>" ;
        }else{
          return "<fmt:message key='jquery.valdate.required.noneid'/>";
        }
      },
      remote:     "<fmt:message key='jquery.valdate.remote'/>",
      email:       "<fmt:message key='jquery.valdate.email'/>",
      url:       "<fmt:message key='jquery.valdate.url'/>",
      date:       "<fmt:message key='jquery.valdate.date'/>",
      dateISO:     "<fmt:message key='jquery.valdate.dateISO'/>",
      number:     "<fmt:message key='jquery.valdate.number'/>",
      digits:     "<fmt:message key='jquery.valdate.digits'/>",
      creditcard:   "<fmt:message key='jquery.valdate.creditcard'/>",
      equalTo:     "<fmt:message key='jquery.valdate.equalTo'/>",
      accept:     "<fmt:message key='jquery.valdate.accept'/>",
      maxlength: $.validator.format("<fmt:message key='jquery.valdate.maxlength'/>"),
      minlength: $.validator.format("<fmt:message key='jquery.valdate.minlength'/>"),
      rangelength: $.validator.format("<fmt:message key='jquery.valdate.rangelength'/>"),
      range: $.validator.format("<fmt:message key='jquery.valdate.range'/>"),
      max: $.validator.format("<fmt:message key='jquery.valdate.max'/>"),
      min: $.validator.format("<fmt:message key='jquery.valdate.min'/>")
  });
  */
});
function ajaxJsonErrorAlert(jqXHR, textStatus, thrownError){
	alert(jqXHR.status);
  if(jqXHR.status == "905"){
    if(opener != null){
      self.close();
      opener.window.top.location.replace("/Login.do");
    }else{
      window.top.location.replace("/Login.do");
    }
  }else if(jqXHR.status  ==0){         alert("<fmt:message key='ajax.exception.status.0'/>");
  }else if(jqXHR.status  ==404){       alert("<fmt:message key='ajax.exception.status.404'/>");
  }else if(jqXHR.status  ==500){       alert("<fmt:message key='ajax.exception.status.500'/>");
  }else if(thrownError  =='parsererror'){   alert("<fmt:message key='ajax.exception.thrownError.parsererror'/>");
  }else if(thrownError  =='timeout'){     alert("<fmt:message key='ajax.exception.thrownError.timeout'/>");
  }else {                   alert("<fmt:message key='ajax.exception.else'/>"); }

  //network Error 12029 error UnKnown
}
</script>

<script>
var comBtnAuthPg = ("${authPg}"=="") ? "R" : "${authPg}";
$(function() {
  (comBtnAuthPg =="A") ? $(".authA,.authR").removeClass("authA").removeClass("authR"):$(".authR").removeClass("authR");
});
</script>