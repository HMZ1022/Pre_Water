<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<style>

.tabTitle{
	font-family:微软雅黑;
	font-weight:bolder;
}
.pageDiv{
	width:100%;
	height:40px;
	line-height: 40px;
	margin:5;
	font-family:微软雅黑;
	text-align: center;
}
.selectButton{
	margin-bottom: 5px;
}
/*context-box样式  */
div.content_box {
	width: 100%;
	height: 100%;
	background-color: #fff;
	border-radius:5px;
	/* border: 1px solid #dcdcdc; */
}
.box-body{
margin-top:10px;
}
.model-title{
	width:90%;
	height:50px;
	margin-left: auto;
	margin-right: auto; 
	margin-top: 10px;
}
.model-title-context{
 	margin:1%;
	/* text-align: left;  */
	font-size:20px;
	font-family: 微软雅黑;
	
}
div.content_box {
	width: 100%;
	height: 100%;
	background-color: #fff;
	border-radius:5px;
	border: 1px solid #dcdcdc;
}
.content_box .box-header {
	height: 50px;
	line-height: 50px;
	padding: 0 20px;
	border-bottom: 1px solid #dcdcdc;
}
.content_box .box-header span {
	float: left;
	line-height: 50px;
	color: rgba(0,0,0,0.85);
	/*margin: 0;*/
}
/* .content_box .box-body {
	height: calc(100% - 50px); 
} */
</style>
<script src="extends/echarts.js"></script>
<!-- datatables -->
<link href="extends/datatables/media/css/jquery.dataTables.min.css" rel="stylesheet">
<link href="extends/datatables/extensions/Buttons/css/buttons.dataTables.min.css" rel="stylesheet">
<script src="extends/datatables/media/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/dataTables.buttons.min.js"></script>
<script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/buttons.flash.min.js"></script>
<script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/jszip.min.js"></script>
<script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/pdfmake.min.js"></script>
<script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/vfs_fonts.js"></script>

 <script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/buttons.html5.min.js"></script>
<script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/buttons.print.min.js"></script>
<script type="text/javascript" charset="utf8" src="extends/datatables/extensions/Buttons/js/buttons.colVis.min.js"></script>
<script src="js/home.js"></script>
<div id="main">
	<ul class="nav nav-tabs tabTitle" style="margin-bottom: 5px;width:80%;margin-left:10%;margin-right:10%;">
		<li id="tabtitle1" class="active">
			<a href="#tab1" data-toggle="tab" class="">用水预测</a>
		</li>
		<li id="tabtitle2">
			<a href="#tab2" data-toggle="tab">模型管理</a>
		</li>
		<li id="tabtitle3">
			<a href="#tab3" data-toggle="tab">模型评估</a>
		</li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active" id="tab1">
			<div class="panel panel-default" style="width:80%;margin-left:10%;margin-right:10%;">
				<div class="panel-body">
					<form id="forcast" class="form-inline" role="form">
						<div >
							<div class="col-xs-3"><label for="place" class="control-label">测试点</label>
									<select id="place" class="form-control" style="width:200px;">
									</select>
							</div>
							<div class="selectButton col-xs-3 form-group"><label for="timeInterval" class="control-label">时间间隔:</label>
								<select id="timeInterval" class="form-control" style="width:200px;">
									<option value="15" selected="selected">15分钟</option>
									<option value="60">1小时</option>
									<option value="1440">1天</option>
								</select>
							</div>
							<div class="col-xs-3"><label for="forcastDays" class="control-label">预测天数:</label>
								<select id="forcastDays" class="form-control" style="width:200px;">
									<option value="1" selected="selected">一天</option>
									<option value="7">一周</option>
									<option value="15">半月</option>
								</select>
							</div>
							<div class="col-xs-3"><label for="algorithm" class="control-label">预测算法</label>
								<select id="algorithm" class="form-control" style="width:200px;">
									<option value="term_total" selected="selected">BP</option>
									<option value="city">搜索树</option>
									<option value="education">因子</option>
								</select>
							</div>
							<div class="col-xs-3">
								<label class="control-label">开始日期：</label>
								<input id="startDate" class="form-control" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"/>
							</div>
							<div class="col-xs-3">
								<label class="control-label">结束日期：</label>
								<input id="endDate" class="form-control" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"/>
							</div>
							<div class="col-xs-3"><input id="dataFitting" class="btn btn-primary" type="button" value="查询" onclick="forcast()"/></div>
						</div>
					</form>	
			  	</div>
			  	<!--表格和echarts展示-->
			  	<div class=""  style="height:70%;">
				  	<div class="col-xs-6">
				  		<div class="content_box">
				  		<div class="box-header">
							<span style="background"><img></img></span><span>预测表</span>
						</div>
					  	<div class="box-body" >
							<table id="table" class="dataTable table-striped table-bordered table-condensed" style="width:100%; height:400px;"></table>
						</div>	
						</div>
				  	</div>
				  	<div class="col-xs-6">
				  	<div class="content_box">
				  		<div class="box-header">
							<span style="background"><img></img></span><span>预测图</span>
						</div>
					  	<div class="box-body">
							<div id="chart" style="width:100%; height: 500px;"></div>
						</div>	
						</div>
				  	</div>
			  	</div>						
			</div>		
			
		</div>
		
		
		<div class="tab-pane" id="tab2">
			<div class="panel panel-default" style="width:80%;margin-left:10%;margin-right:10%;">
				<div class="panel-body">
					<form id="generatingModel" class="form-inline" role="form">
						<div >
							<div class="selectButton col-xs-3 form-group"><label for="timeInterval" class="control-label">时间间隔:</label>
								<select id="m_timeInterval" class="form-control" style="width:200px;">
									<option value="0.25" selected="selected">15分钟</option>
									<option value="1">1小时</option>
									<option value="24">1天</option>
								</select>
							</div>
							<div class="col-xs-3"><label for="forcastDays" class="control-label">评估天数:</label>
								<select id="m_forcastDays" class="form-control" style="width:200px;">
									<option value="1" selected="selected">一天</option>
									<option value="7">一周</option>
									<option value="15">半月</option>
								</select>
							</div>
							<div class="col-xs-3"><label for="algorithm" class="control-label">预测算法</label>
								<select id="m_algorithm" class="form-control" style="width:200px;">
									<option value="term_total" selected="selected">BP</option>
									<option value="city">搜索树</option>
									<option value="education">因子</option>
								</select>
							</div>
							<div class="col-xs-3"><label for="place" class="control-label">测试点</label>
								<select id="m_place" class="form-control" style="width:200px;">
									<option value="1" selected="selected">测试点1</option>
									<option value="2">测试点2</option>
									<option value="3">测试点3</option>
								</select>
							</div>
							<div class="col-xs-3">
								<label class="control-label">开始日期：</label>
								<input id="m_startDate" class="form-control" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"/>
							</div>
							<div class="col-xs-3">
								<label class="control-label">结束日期：</label>
								<input id="m_endDate" class="form-control" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"/>
							</div>
							<div class="col-xs-3"><input id="gene_model" class="btn btn-primary" type="button" value="生成模型"/></div>
						</div>
					</form>	
			  	</div>
			  	<!-- 模型分析和预测 -->
			  	<div class="" style="height:70%;">
			  		<div class="col-xs-4">
			  			<div class="content_box" style="height:100%">
							<div class="box-header">
								<span style="background"><img></img></span><span>模型分析</span>
							</div>
							<div class="box-body">
								<!-- <table id="pollution_table" class="stripe" cellspacing="0" style="width: 96%; margin-top:10px;"></table> -->
								<table id="taskTable" class="table table-hover">
									<thead>
										<tr>
											<th>模型时间</th>
											<th>模型名称</th>
											<th>间隔</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody></tbody>
								</table>
							</div>				
						</div>
			  		</div>
			  		<div class="col-xs-8">
			  			<div class="col-xs-6">
			  				<div class="content_box" style="height:100% ;">
								<div class="box-header">
									<span style="background"><img></img></span><span>模型参数</span>
								</div>
								<div class="box-body">
									参数
								</div>				
							</div>			  				
			  			</div>
			  			<div class="col-xs-6">
			  				<div class="content_box" style="height:100%">
								<div class="box-header">
									<span style="background"><img></img></span><span>评估结果</span>
								</div>
								<div class="box-body">
									<div>一天：</div>
									<div>一周：</div>
									<div>半月：</div>
								</div>				
							</div>	
			  			</div>
			  		</div>
				</div>
			</div>		
			
		</div>
		
		
		<div class="tab-pane" id="tab3">
			<div class="panel panel-default" style="width:80%;margin-left:10%;margin-right:10%;">
				<div class="panel-body" style="height:10%">
					<form id="generatingModel" class="form-inline" role="form">
						<div >
							<div class="col-xs-3">
								<label class="control-label">开始日期：</label>
								<input id="e_startDate" class="form-control" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"/>
							</div>
							<div class="col-xs-3">
								<label class="control-label">结束日期：</label>
								<input id="e_endDate" class="form-control" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"/>
							</div>
							<div class="selectButton col-xs-3 form-group"><label for="timeInterval" class="control-label">时间间隔:</label>
								<select id="e_timeInterval" class="form-control" style="width:200px;">
									<option value="0.25" selected="selected">15分钟</option>
									<option value="1">1小时</option>
									<option value="24">1天</option>
								</select>
							</div>							
							<div class="col-xs-3"><label for="algorithm" class="control-label">预测算法</label>
								<select id="e_algorithm" class="form-control" style="width:200px;">
									<option value="term_total" selected="selected">BP</option>
									<option value="city">搜索树</option>
									<option value="education">因子</option>
								</select>
							</div>
							<div class="col-xs-3"><label for="place" class="control-label">测试点</label>
								<select id="e_place" class="form-control" style="width:200px;">
									<option value="1" selected="selected">测试点1</option>
									<option value="2">测试点2</option>
									<option value="3">测试点3</option>
								</select>
							</div>
							<div class="col-xs-3"><input id="eve_model" class="btn btn-primary" type="button" value="模型评估"/></div>
						</div>
					</form>	
			  	</div>
			  	<!--table  -->
			  	<div style="height:30%;">
			  		<table id="t3" class="dataTable table-striped table-bordered table-condensed" style="width:100%;">
			  		<thead >
			            <tr>
			                <th rowspan="2" >编号</th>
			                <th rowspan="2" >测量名称</th>
			                <th rowspan="2">时间</th>
			                <th rowspan="2">用量</th>
			                <th colspan="2">模型1</th>
			                <th colspan="2">模型2</th>
			                <th colspan="2">模型3</th>
			            </tr>
			            <tr>
			                <th>预测用量</th>
			                <th>差化</th>
			                <th>预测用量</th>
			                <th>差化</th>
			                <th>预测用量</th>
			                <th>差化</th>
			            </tr>
			        </thead>
			  		</table> 
			  	</div>
			  	<!-- echarts -->
			  	<div  style="height:40%;">
				  	<div class="content_box" style="">
				  		<div class="col-md-6" style="margin-top:10px;">			  			
								<div id="c1" style="width:600px; height:350px"></div>				
						</div>
						<div class="col-md-6" style="margin-top:10px;">			  			
								<div id="c2" style="width:600px; height:350px"></div>			
						</div>			  				
				  	</div>
			  	</div>

			</div>
		</div>
		
	</div>
	
	
</div>
<script>
	$(document).ready(function() {
		getAllCstId();
	});
	var tableDatas=[];
	var table;
	initEcharts('chart');
	initEcharts('c1');
	initEcharts('c2');
	initTable2('table');
	initTable('t3');
	
	function getAllCstId(){
		$.ajax({
			  type: 'POST',
			  url: 'rest/system/interval/getAllCstId/',
			  success: function(result){
				  list =result.data;
				  var optionstring = "";
				  for(var i=0;i<list.length;i++){
					  optionstring += "<option value=\"" + list[i].CST_ID + "\" >" + list[i].CST_NAME + "</option>"; 
				  }
				  $("#place").html("<option value='请选择'>请选择...</option> "+optionstring); 
			  },
			  dataType: "json",
			  contentType:"application/json"
		});
	}
	
	function forcast(){
		var cst_id=$("#place").val();
		var timeInterval= $("#timeInterval").val();
		$.ajax({
			  type: 'POST',
			  url: 'rest/system/interval/getAllById/',
			  data:JSON3.stringify({
				  cst_id:cst_id,
				  timeInterval:timeInterval
				}),
			  success: function(result){
				  list =result.data;
			  },
			  dataType: "json",
			  contentType:"application/json"
		});
	}
</script>