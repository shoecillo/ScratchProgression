


var scratchApp = angular.module('scratchApp', []);


scratchApp.controller('scratchCtrl',["$scope","$http","$q",function($scope,$http,$q){
	
	var  vm = $scope;
	
	vm.fallos = 0;
	
	vm.started = false;
	
	vm.lsStats = [];
	
	vm.contaFails = 0;
	
	vm.addFail = function(){
		
		$http.get('/addFail').then(function(result)
		{
			console.log(result);
			vm.lsStats = result.data;
			vm.contaFails++;
		}, 
		function(error)
		{
			console.log(error);
					
		});
	};
	
	vm.startSession = function(){
		
		vm.started = true;
		
		$http.get('/startSession').then(function(result)
		{
			console.log(result);
			vm.lsStats = result.data;
			
			
		}, 
		function(error)
		{
			console.log(error);
					
		});
	};
	
	vm.endSession = function(){
		
		vm.started = false;
		vm.contaFails = 0;
		
		$http.get('/stopSession').then(function(result)
		{
			console.log(result);
			vm.lsStats = result.data;
			vm.setChart();
			vm.myChart.update();
		}, 
		function(error)
		{
			console.log(error);
					
		});
	};
	
	vm.getStats = function(){
		
		
		
		$http.get('/getStats').then(function(result)
		{
			console.log(result);
			vm.lsStats = result.data;
			vm.setChart();
		}, 
		function(error)
		{
			console.log(error);
					
		});
	};
	
	vm.backupStats = function(){
		
		
		
		$http.get('/backupStats').then(function(result)
		{
			console.log(result);
			vm.lsStats = result.data;
			vm.setChart();
		}, 
		function(error)
		{
			console.log(error);
					
		});
	};
	
	vm.toDate = function(d){
		return moment(d).utcOffset('+0200').format('YYYY-MM-DD HH:mm:ss');
	};
	
	vm.setChart = function(){
		
		var lsChart = vm.lsStats.map((it) =>{
			
			return {
				"startTime" : it.startTime,
				"endTime" : it.endTime,
				"failsCount" : it.fails.length
			};
		});
		
		for(var i=0;i<lsChart.length;i++){
			lsChart[i].legend = "Session #"+i;
		}
		
		var lsLegend = lsChart.map((item) => {
			return item.legend;
		});
		
		var lsData = lsChart.map((item) => {
			return item.failsCount;
		});
		
		//var ctx = document.getElementById('myChart');
		vm.myChart = new Chart(document.getElementById("myChart"), {
		    "type": "line",
		    "data": {
		        "labels": lsLegend,
		        "datasets": [{
		                "label": "Scratch progression",
		                "data": lsData,
		                "fill": false,
		                "borderColor": "rgb(75, 192, 192)",
		                "lineTension": 0.4
		            }
		        ]
		    },
		    "options": {
		    	responsive: false
		    }
		});
		
	};
	
	
	
	

	vm.getStats();
	
	
}]);