/**
 * 抽奖配置
 */
angular.module('inspinia',['uiSwitch']).controller('prizeConfigureCtrl',function($scope,$http,i18nService,SweetAlert,$document,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.funcCode=$stateParams.funcCode;

    //获取抽奖配置
    $scope.getAddInfo=function () {
        $http.post("prizeConfigure/getPrizeConfigure","funcCode="+$stateParams.funcCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.info;
                    $scope.addInfo.startTime=$scope.addInfo.startTime==null?null:moment($scope.addInfo.startTime).format('YYYY-MM-DD HH:mm:ss');
                    $scope.addInfo.endTime=$scope.addInfo.endTime==null?null:moment($scope.addInfo.endTime).format('YYYY-MM-DD HH:mm:ss');
                }
            });
    };
    $scope.getAddInfo();

    //获取奖项配置表
    $scope.getPrizeList=function () {
        $http.post("prizeConfigure/getPrizeList","funcCode="+$stateParams.funcCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.prizeList=data.list;
                }
            });
    };
    $scope.getPrizeList();

    $scope.userGrid={                           //配置表格
        data: 'prizeList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'ID',width:100 },
            { field: 'awardDesc',displayName:'奖项说明',width:150 },
            { field: 'awardName',displayName:'奖品名称',width:150 },
            { field: 'status',displayName: '是否有效',width: 150,
                cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'prizeConfigure.closeOpenPrize\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.closeOpenPrize(row.entity)" /></span>'
                +'<span ng-show="!grid.appScope.hasPermit(\'prizeConfigure.closeOpenPrize\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'

            },
            { field: 'sort',displayName:'序列',width:120 },
            { field: 'awardCount',displayName:'奖品数量',width:120 },
            { field: 'prob',displayName:'中奖机率(%)',width:120 },
            { field: 'dayCount',displayName:'每天最多派发',width:120 },
            { field: 'dayCount2',displayName:'今日剩余',width:120 },
            { field: 'merDayCount',displayName:'每天最多中奖次数',width:120 },
            { field: 'id',displayName:'操作',width:210,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="func.prizeDetail({funcCode:row.entity.funcCode,id:row.entity.id})">详情</a> ' +
                '<a target="_blank" ui-sref="func.blacklist({funcCode:row.entity.funcCode,id:row.entity.id})"> | 黑名单</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'prizeConfigure.updatePrize\')&&row.entity.status==0" target="_blank" ui-sref="func.prizeEdit({funcCode:row.entity.funcCode,id:row.entity.id})"> | 修改</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'prizeConfigure.deletePrize\')&&row.entity.status==0" ng-click="grid.appScope.deletePrize(row.entity.id)""> | 删除</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.submitting = false;
    //新增banner
    $scope.submitInfo = function(){
        if($scope.submitting){
            return;
        }
        if($scope.addInfo.startTime==null||$scope.addInfo.startTime==""
            ||$scope.addInfo.endTime==null||$scope.addInfo.endTime==""){
            $scope.notice("活动时间不能为空!");
            return;
        }

        $scope.subAddinfo=angular.copy($scope.addInfo);
        $scope.subAddinfo.funcDesc=null;

        var data={
            info:angular.toJson($scope.subAddinfo),
            funcDesc:$scope.addInfo.funcDesc
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("prizeConfigure/updatePrizeConfigure",data,postCfg)
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('func.couponActivity',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

    $scope.deletePrize=function(id){
        SweetAlert.swal({
                title: "确认删除?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("prizeConfigure/deletePrize","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.getPrizeList();
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });

                }
            });
    };

    $scope.closeOpenPrize=function(entity){
        var title="";
        var sta=0;
        if(entity.status==false){
            title="确认关闭?"
        }else{
            title="确认开启?"
            sta=1;
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("prizeConfigure/closeOpenPrize","id="+entity.id+"&status="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.getPrizeList();
                            }else{
                                $scope.notice(data.msg);
                                $scope.callBackSwitch(entity);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            $scope.callBackSwitch(entity);
                        });
                }else{
                    $scope.callBackSwitch(entity);
                }
            });
    };
    $scope.callBackSwitch=function (entity) {
        if(entity.status==false){
            entity.status=true;
        }else{
            entity.status=false;
        }
    };

    /**
     *富文本框按钮控制
     */
    $scope.summeroptions = {
        toolbar: [
            ['style', ['bold', 'italic', 'underline','clear']],
            ['fontface', ['fontname']],
            ['textsize', ['fontsize']],
            ['fontclr', ['color']],
            ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
            ['height', ['height']],
            ['insert', ['hr']],
            // ['insert', ['link','picture','video','hr']],
            ['view', ['codeview']]
        ]
    };
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});