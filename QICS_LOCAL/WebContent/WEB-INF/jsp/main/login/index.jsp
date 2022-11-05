<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapSub">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: QICS작업 진행상태</title>

 <!-- Bootstrap 3.3.6 -->
 <link href="/common/css/bootstrap.min.css" rel="stylesheet">
 <!-- HYUNDAI BNG STEEL css -->
 <link href="/common/css/default.css" rel=" stylesheet">

</head>
<body>
	<div class="wrapSub">
    
    		<div class="row">
                <div class="col-lg-12">
                  <div class="ibox">
                    <div class="ibox-title">
                        <h5>QICS작업 진행상태</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="img-process"></div>
                    </div>
                  </div>
                  <!-- ./ibox -->
            	</div>
                <!-- ./col -->
            </div>
            <!-- ./row -->
            
            <div class="row">
                <div class="col-lg-12">
                  <div class="ibox float-e-margins">
                    <div class="ibox-title">
                       	<h5>상태별 설명</h5>
                    </div>
                    <div class="ibox-content">
                    	
                        <!-- 상태별 설명 -->
                        <div class="row white-bg dashboard-header">

                    		<div class="col-sm-12">
                        		<ul class="list-group clear-list">
                            		<li class="list-group-item fist-item">
                                		<span class="label label-default">1</span> <span class="label label-default">출력대기</span>: POP 작업지시목록에서 QICS 작업목록에 추가된 상태입니다.
                            		</li>
                            		<li class="list-group-item">
                                        <span class="label label-default">2</span> <span class="label label-primary">양식생성중</span>: 검사를 진행하기위해 검사양식을 생성하고 출력하는 단계입니다.
                            		</li>
                            		<li class="list-group-item">
                                        <span class="label label-default">3</span> <span class="label label-warning">검사대기</span>: 검사양식 출력이 완료되어 검사를 시작하시는 단계입니다.
                            		</li>
                            		<li class="list-group-item">
                                        <span class="label label-default">4</span> <span class="label label-info">검사중</span>: 전자펜으로 검사양식 작성후 전송하신뒤 결과값을 보정 또는 저장하는 단계입니다.
                            		</li>
                            		<li class="list-group-item">
                                		<span class="label label-default">5</span> <span class="label label-success">ERP전송완료</span>: QICS 검사데이터를 ERP로 전송하여 검사실적이 생성되며 검사업무가 종료된 상태입니다.
                            		</li>
                            		<li class="list-group-item">
                                		<span class="label label-default">6</span> <span class="label label-danger">ERP전송실패</span>: ERP전송이 실패된 상태입니다.
                            		</li>
                        		</ul>
                    		</div>
						</div>
                        <!-- ./상태별 설명 -->                        
                       
                    </div>
                    <!-- ./ibox-content -->
                    
                  </div>
                  <!-- ./ibox -->
            	</div>
                <!-- ./col -->
            </div>
            <!-- ./row -->
            
             
	</div>
</body>
</html>
