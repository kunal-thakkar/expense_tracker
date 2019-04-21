app.factory('DashboardService', ['$http', '$q', function ($http, $q) {
    return {
        getDashboardData: function(query){
            var deferred = $q.defer();
            $http.post("/getTransactions", query)
                .then(
                    function (response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        console.error('Error: ', errResponse);
                        deferred.reject(errResponse);
                    }
                );
            return deferred.promise;
        }
    };
}]);
app.controller('DashboardCtrl', ['$scope', 'DashboardService', 'AppUtil', function ($scope, DashboardService, AppUtil) {
    'use strict';
    var self = this;
    self.query = {
        from: AppUtil.addDays(new Date(), -60),
        to: new Date()
    };
    self.applyFilter = function(){
        DashboardService.getDashboardData({
            from:self.query.from.getTime(),
            to: self.query.to.getTime()
        }).then(
            function(d){
                var income = 0;
                var expense = 0;
                var chartData = {};
                var categoryPieSeries = {};
                d.forEach(o=>{
                    if(!chartData.hasOwnProperty(o.paymentMode)){
                        chartData[o.paymentMode] = {"label": o.paymentMode, data: 0, color: AppUtil.getColor()};
                    }
                    if(!categoryPieSeries.hasOwnProperty(o.category)){
                        categoryPieSeries[o.category] = {"label": o.category, data: 0, color: AppUtil.getColor()};
                    }
                    chartData[o.paymentMode].data += o.amount;
                    categoryPieSeries[o.category].data += o.amount;
                    if(o.transactionType == "dr"){
                        expense += o.amount;
                    }
                    else {
                        income += o.amount;
                    }
                });
                self.income = income;
                self.expense = expense;
                self.transactions = d;
                self.graphdata.series = Object.values(chartData);
                self.categoryPie.series = Object.values(categoryPieSeries);
                self.tabledata.data = d;
                console.log(self.dataTable);
            },
            function(e){
                console.log(e);
            }
        );
        if(self.dataTable) self.dataTable.ajax.reload();
    }
    self.graphdata = {
        series: [],
        options: {
            series: {
                pie: {
                    show: true,
                    label: {
                        show: false
                    },
                }
            },
            legend: {
                show: false
            },
            grid: { clickable: true }
        }
    };
    self.categoryPie = {
        series: [],
        options: {
            series: {
                pie: {
                    show: true,
                    label: {
                        show: false
                    },
                }
            },
            legend: {
                show: false
            },
            grid: { clickable: true }
        }
    };
    self.tabledata = {
        ajax: {
            url:"/getTransactions",
            dataSrc: "",
            type: "post",
            data: {
                from:self.query.from.getTime(),
                to: self.query.to.getTime()
            }
        },
        responsive: true,
        processing: true,
        columns: [
            { data: "dateTime", title:"Date", defaultContent: "", render: function(data, type, row){
                return AppUtil.formatDate(new Date(data));
            }},
            { data: "description", title: "Description", defaultContent: "" },
            { data: "category", title: "Category", defaultContent: "" },
            { data: "paymentMode", title: "Payment Mode", defaultContent: "" },
            { data: "amount", title: "Amount", defaultContent: "", render: function(data, type, row){
                return AppUtil.formatCurrency(data) + " " + row.transactionType;
            }, className: 'dt-body-right nowrap' }
        ],
        data: []
    };
    self.plotclick = function (arg) {
        if (arg)
            console.log(arg.series.label);
    };
    self.applyFilter();
}]);
