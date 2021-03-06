/**
 * 设置任务
 */
angular.module('inspinia').controller("setAcqServiceWarningPeopleCtrl", function($scope, $http, $state, $stateParams, i18nService,$document){
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');
	
	$scope.wp= {};
	
	$http.post("warningPeople/getCsWarningPeople","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                	$scope.wp = data;
                	$scope.query();
    });
	
	$scope.query = function() {
		$http.post('groupService/serviceList.do',
       		 "info="+angular.toJson($scope.info),
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            $scope.moneyServiceData = data.result;
			$scope.moneyServiceGrid.totalItems = data.totalCount;//总记录数
        }).error(function(){
        }); 
	};
	
	$scope.moneyServiceGrid = {
	        data: 'moneyServiceData',
	     //   paginationPageSize:10,                  //分页数量
	     //   paginationPageSizes: [10,20,50,100],	//切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	   //     enableHorizontalScrollbar: 0,        //去掉滚动条
	   //     enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'id',displayName: '收单服务ID',width: 200,pinnable: false,sortable: false},
                {field: 'serviceName',displayName: '服务名称',width: 200,pinnable: false,sortable: false},
                {field: 'acqOrgName',displayName: '收单机构',width: 200,pinnable: false,sortable: false},
                {field: 'serviceType',displayName: '服务类型',width: 200,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.acqServiceTypes)},
	        ],
	        onRegisterApi: function(gridApi) {
	        	$scope.gridApi=gridApi
	           /* gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.query();
	            });*/
	        },
	        isRowSelectable: function(row){ 
	        	 if($scope.wp.sids!= undefined && $scope.wp.sids.indexOf(row.entity.id) >= 0){
                    row.grid.api.selection.selectRow(row.entity); 
                }
            }
	};
	
	$scope.selectId = function(){
		 var selectRows = $scope.gridApi.selection.getSelectedRows();
	        var list = new Array();
	        for(var i=0;i<selectRows.length;i++){
	            list[i]=selectRows[i].id;
	        }
	        $http.post("warningPeople/setSids","arr="+angular.toJson(list)+"&id="+$stateParams.id,
	                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	                .success(function(data){
	                    if(data.status){
	                    	//$scope.wps = data.wp;
	                    	$scope.notice(data.msg);
	                    }else{
	                        $scope.notice(data.msg);
	                    }
	        });
	        
	};

	//页面绑定回车事件
	/*
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	*/
	
});