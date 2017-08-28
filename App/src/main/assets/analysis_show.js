
//电梯序列号
var elevatorSn = "";
//分析参考值
/* 参考值格式：{label:{maxaccs:[],tag:"",maxveloc:xxx,minaccs:[]}}*/
var analysisReference = {};

var server = "http://192.168.0.81:8080/h5/jsonp?url=elevator.hbydt.cn/analysis/";

//获取电梯基本信息
function getElevatorBaseInfo(){
	var baseInfo = {};
	$.ajax({
           type: "GET",
			url: server + "baseinfo",			
			cache:false,
			async:false,
			dataType: "jsonp",
			data: {"sn":$(elevatorSn)}, //参数之间用“,” 逗号隔开。
			success: function (data) {
				for(var key in data){
					baseInfo.put(key,data[key]);
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				//alert(errorThrown);
			}
	});
	return baseInfo;
}

//获取分析参考数据
function getAnalysisReference(){
	$.ajax({
           type: "GET",
			url: server + "reference",		
			cache:false,
			async:false,
			dataType: "jsonp",
			data: {"sn":elevatorSn}, //参数之间用“,” 逗号隔开。
			success: function (data) {
				for(var key in data){
					analysisReference[key] = data[key];
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
	});
	return analysisReference;
}
//获取加速度分析值数据
function getAnalysisAcc(time){
	//加速度分析值
	/* 加速度格式：{label:"",starttime:xx,accs:[],isnorm:1}*/
	var analysisAcc = new Queue(15);
	$.ajax({
           type: "GET",
			url: server + "acc",		
			cache:false,
			async:true,
			dataType: "jsonp",
			data: {"sn":elevatorSn,"st":time,'n':15}, //参数之间用“,” 逗号隔开。
			success: function (data) {
				$.each(data,function(i,v){
					analysisAcc.push(v);
				});
				
				forShowAccs(analysisAcc);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
	});
	return analysisAcc;
}
//获取速度分析值数据
function getAnalysisVeloc(time){
	//速度分析值
	/* 速度格式：{label:"",starttime:xx,veloc:[],isnorm:1}*/
	var analysisVeloc = new Queue(15);
	$.ajax({
           type: "GET",
			url: server + "veloc",		
			cache:false,
			async:true,
			dataType: "jsonp",
			data: {'sn':elevatorSn,'st':time,'n':15}, //参数之间用“,” 逗号隔开。
			success: function (data) {
				$.each(data,function(i,v){
					analysisVeloc.push(v);
				});
				forShowSpeed(analysisVeloc);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				//alert(errorThrown);
			}
	});
	return analysisVeloc;
}
//获取运行次数情况分析值数据
function getAnalysisTraffic(time){	
	//运行次数情况分析值
	/* 运行次数格式：{startTime:1498726510877,endTime:1498726810877:xx,num:xx}*/
	var analysisTraffic = new Queue(200);
	$.ajax({
           type: "GET",
			url: server + "traffic",		
			cache:false,
			async:true,
			dataType: "jsonp",
			data: {'sn':elevatorSn,'st':time,'n':200}, //参数之间用“,” 逗号隔开。
			success: function (data) {
				$.each(data,function(i,v){
					analysisTraffic.push(v);
				});
				forShowTraffic(analysisTraffic);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				//alert(errorThrown);
			}
	});
	return analysisTraffic;
}
//获取轨迹统计分析值数据
function getAnalysisTrajectory(time){
	//轨迹统计分析值
	/* 运行轨迹数格式：{time:xx,traj:[]}*/
	var analysisTrajectory = new Queue(50);
	$.ajax({
		type: "GET",
			url: server + "trajectory",		
			cache:false,
			async:true,
			dataType: "jsonp",
			data: {'sn':elevatorSn,'st':time,'n':50}, //参数之间用“,” 逗号隔开。
			success: function (data) {
				$.each(data,function(i,v){			
					analysisTrajectory.push(v);
				});
				forShowTraj(analysisTrajectory);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				//alert(errorThrown);
			}
	});
	return analysisTrajectory;
}

/*************************1498523943000*****/
var timeline__acc = new Queue(15);
var options__acc = new Queue(15);
function forShowAccs(analysisAcc){
	for(var i=0;i<analysisAcc.size();i++){
		var acc = analysisAcc.quere()[i];
		var starttime = new Date(acc.startTime-0);
		var reference = analysisReference[acc.label];
		if(reference==null){
			reference = {"minaccs":[],"maxaccs":[],"maxveloc":{},"tag":""};
		}
		timeline__acc.push(starttime.pattern("HH:mm:ss"));
		var series = [{data:[]},{data:[]},{data:[]}];
		series[0].data = reference["minaccs"];
		series[1].data = acc["accs"];
		series[2].data = reference["maxaccs"];
		var option = {};
		option.series = series;
		/**/
		var isNorm = '正常';
		if(acc.isnorm==0){
			isNorm = '疑似异常';
		}
		var len = acc["accs"].length+2;
		var res = [];
		while (len--) {
			res.unshift(len);
		}
		var xAxis= [{
						data : res
					},{
						name:reference.tag+'\n\n'+isNorm+'\n\n'+starttime.pattern("HH:mm:ss")
					}];		
		option.xAxis = xAxis;
		
		options__acc.push(option);
	}
	option_acc.options = options__acc.quere();
	option_acc.baseOption.timeline.data = timeline__acc.quere();	
	
	//alert(obj2string(option_acc));
	myChart_acc.setOption(option_acc);
}
/******************************/
var timeline__speed = new Queue(15);
var option__speed = new Queue(15);
function forShowSpeed(analysisVeloc){
	for(var i=0;i<analysisVeloc.size();i++){
		var veloc = analysisVeloc.quere()[i];
		var starttime = new Date(veloc.startTime-0);
		var reference = analysisReference[veloc.label];		
		if(reference==null){
			reference = {"minaccs":[],"maxaccs":[],"maxveloc":{},"tag":""};
		}
		timeline__speed.push(starttime.pattern("HH:mm:ss"));
		
		var series= [{data:[],markLine:{data: [{yAxis: 0,label: {show: true}}]}}] ;
		series[0].data = veloc["veloc"];
		series[0].markLine.data[0].yAxis = reference["maxveloc"];
		var option = {};
		option.series = series;
		
		var isNorm = '正常';
		if(veloc.isnorm==0){
			isNorm = '疑似异常';
		}
		var len = veloc["veloc"].length+1;
		var res = [];
		while (len--) {
			res.unshift(len);
		}
		var xAxis= [{
						data : res
					},{
						name:reference.tag+'\n\n'+isNorm+'\n\n'+starttime.pattern("HH:mm:ss")
					}];	
		option.xAxis = xAxis;
		
		option__speed.push(option);		
	}
	
	option_speed.options = option__speed.quere();
	option_speed.baseOption.timeline.data = timeline__speed.quere();	
	
	//alert(obj2string(option_speed));
	myChart_speed.setOption(option_speed);
}
/******************.reverse()************/
var total = 0;
var trafficTimes = new Queue(200);
var trafficNums = new Queue(200);
var trafficTotals = new Queue(200);
function forShowTraffic(analysisTraffic){
	for(var i=analysisTraffic.size()-1;i>=0;i--){
			var traffic = analysisTraffic.quere()[i];		
			var starttime = new Date(traffic.startTime-0);
			trafficTimes.push(starttime.pattern("yyyy-MM-dd \n HH:mm:ss"));
			trafficNums.push(traffic.num+0);
			
			//alert(traffic.num);
			total = total+traffic.num;
			//alert(total);
			trafficTotals.push(total);
			if(traffic.endTime>startTimeStamp){
				lastTimeStamp = traffic.endTime;
			}
			
		}
		
		option_flowrate.xAxis[0].data = trafficTimes.quere();
		option_flowrate.series[0].data = trafficNums.quere();
		option_flowrate.series[1].data = trafficTotals.quere();
		
		myChart_flowrate.setOption(option_flowrate);
}
/******************************************/
var timeline__traj = new Queue(50);
var option__traj = new Queue(50);
function forShowTraj(analysisTrajectory){
	for(var i=0;i<analysisTrajectory.size();i++){
		var traj = analysisTrajectory.quere()[i];
		var starttime = new Date(traj.startTime-0);	
		var endtime = new Date(traj.endTime-0);	
		timeline__traj.push(starttime.pattern("HH:mm:ss"));
		
		var item = traj["traj"];
		
		var x = [];
		var y = [];
		
		for(var key in item){
			var k = key;
			x.push(analysisReference[k]==null?key:analysisReference[k].tag);
			y.push(item[key]);
		}
		
		var series= [{data:[]}] ;
		series[0].data = y;
		var option = {};
		option.series = series;
				
		var xAxis= [{
						data : x
					},{
						name:'从'+starttime.pattern("HH:mm:ss")+'\n\n到'+endtime.pattern("HH:mm:ss")
					}];	
		option.xAxis = xAxis;
		
		option__traj.push(option);		
	}
	
	option_totalflowrate.options = option__traj.quere();
	option_totalflowrate.baseOption.timeline.data = timeline__traj.quere();	
	
	myChart_totalflowrate.setOption(option_totalflowrate);
}
