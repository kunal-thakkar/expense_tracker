var app = angular.module('app', ['ngRoute']);
app.factory('AuthInterceptor', [function () {
    return {
        // Send the Authorization header with each request
        'request': function (config) {
            config.headers = config.headers || {};
            var encodedString = btoa("bill:abc123");
            config.headers.Authorization = 'Basic ' + encodedString;
            return config;
        }
    };
}]);
app.config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
    $httpProvider.interceptors.push('AuthInterceptor');
    $routeProvider
        .when("/", {
            templateUrl: "login.html"
        })
        .when("/dashboard", {
            templateUrl: "dashboard.html"
        })
        .when("/transactions", {
            templateUrl: "transactions.html"
        });
}]);