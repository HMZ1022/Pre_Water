function initEcharts(elem){
	var myChart = echarts.init(document.getElementById(elem));
	option = {
		    title : {
		        text: '未来一周气温变化',
		        subtext: '纯属虚构'
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['最高气温','最低气温']
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            boundaryGap : false,
		            data : ['周一','周二','周三','周四','周五','周六','周日']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            axisLabel : {
		                formatter: '{value} °C'
		            }
		        }
		    ],
		    series : [
		        {
		            name:'最高气温',
		            type:'line',
		            data:[11, 11, 15, 13, 12, 13, 10],
		            markPoint : {
		                data : [
		                    {type : 'max', name: '最大值'},
		                    {type : 'min', name: '最小值'}
		                ]
		            },
		            markLine : {
		                data : [
		                    {type : 'average', name: '平均值'}
		                ]
		            }
		        },
		        {
		            name:'最低气温',
		            type:'line',
		            data:[1, -2, 2, 5, 3, 2, 0],
		            markPoint : {
		                data : [
		                    {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
		                ]
		            },
		            markLine : {
		                data : [
		                    {type : 'average', name : '平均值'}
		                ]
		            }
		        }
		    ]
		};
	myChart.setOption(option);                    
}

function initTable(elem){
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) { $($.fn.dataTable.tables(true)).DataTable().columns.adjust(); });
	tableDatas=[];
	tableDatas=[];
	for(var i=0;i<10;i++){
		tableDatas[i]={
			'id':i,
			'product':'product'+i,
			'time':'2016-09-08',
			'num':100,
			'preNUm1':55,
			'dValue1':10,
			'preNUm2':58,
			'dValue2':12,
			'preNUm3':59,
			'dValue3':13,
		}
	}
	var lengthMenu;
	if(elem=='t3'){
		lengthMenu=[4]
		buttons=[]
	}else{
		lengthMenu=[10];
        buttons= [
                  'copy', 'csv', 'excel', 'pdf'
              ];
	}
	
	
	table=$("#"+elem).DataTable({
		/*dom: 'Bfrt<"pageclass"p><"infoclass"i>',*/
		'dom': '<"float_left"f>r<"float_right"l>tip',
		buttons:buttons,
        sScrollX: "100%",   //表格的宽度
		scrollX:true,//设置滚动
		scollY:true,//设置滚动
		destroy: true,
		retrieve:true,
		ordering: true,
		searching: true,//开启搜索
        info: true,//是否显示左下角信息
        bLengthChange: true, //改变每页显示数据数量
        autoWidth: true,//自动宽度
		pagingType: "full",
		orderFixed:true,//高亮排序的列
		"lengthMenu": lengthMenu,
		stripeClasses: [ 'strip1', 'strip2' ],
		columns: [
			{ title:"编号","data": "id" , "className": "dt-center"},
			{ title:"测量名称","data": "product" , "className": "dt-center" },
			{ title:"时间","data": "time" , "className": "dt-center" },
			{ title:"用量","data": "num" , "className": "dt-center" },
			{ title:"预测用量","data": "preNUm1" , "className": "dt-center" },
			{ title:"插化","data": "dValue1" , "className": "dt-center" },
			{ title:"预测用量","data": "preNUm2" , "className": "dt-center" },
			{ title:"插化","data": "dValue2" , "className": "dt-center" },
			{ title:"预测用量","data": "preNUm3" , "className": "dt-center" },
			{ title:"插化","data": "dValue3" , "className": "dt-center" },
		],
		data: tableDatas,
		language: {
			info: "显示第 _PAGE_ 页，一共 _PAGES_ 页",
			emptyTable: "无数据",
			zeroRecords:    "无匹配结果",
			paginate: {
				first:      "首页",
				previous:   "上一页",
				next:       "下一页",
				last:       "尾页"
			}
		}		
    });
}
function initTable2(elem){
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) { $($.fn.dataTable.tables(true)).DataTable().columns.adjust(); });
	tableDatas=[];
	tableDatas=[];
	for(var i=0;i<10;i++){
		tableDatas[i]={
			'id':i,
			'product':'product'+i,
			'time':'2016-09-08',
			'num':100,
		}
	}
	var lengthMenu;
	if(elem=='t3'){
		lengthMenu=[4]
		buttons=[]
	}else{
		lengthMenu=[10];
        buttons= [
                  'copy', 'csv', 'excel', 'pdf'
              ];
	}
	
	
	table=$("#"+elem).DataTable({
		dom: 'Bfrt<"pageclass"p><"infoclass"i>',
		buttons:buttons,
        sScrollX: "100%",   //表格的宽度
		scrollX:true,//设置滚动
		scollY:true,//设置滚动
		destroy: true,
		retrieve:true,
		ordering: true,
		searching: true,//开启搜索
        info: false,//是否显示左下角信息
        bLengthChange: true, //改变每页显示数据数量
        autoWidth: true,//自动宽度
		pagingType: "full",
		orderFixed:true,//高亮排序的列
		"lengthMenu": lengthMenu,
		stripeClasses: [ 'strip1', 'strip2' ],
		columns: [
			{ title:"编号","data": "id" , "className": "dt-center"},
			{ title:"测量名称","data": "product" , "className": "dt-center" },
			{ title:"时间","data": "time" , "className": "dt-center" },
			{ title:"用量","data": "num" , "className": "dt-center" }
		],
		data: tableDatas,
		language: {
			info: "显示第 _PAGE_ 页，一共 _PAGES_ 页",
			emptyTable: "无数据",
			zeroRecords:    "无匹配结果",
			paginate: {
				first:      "首页",
				previous:   "上一页",
				next:       "下一页",
				last:       "尾页"
			}
		}		
    });
}
