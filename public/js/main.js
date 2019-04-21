var app = angular.module('app', ['ngRoute', 'ngCookies']);
app.factory('AuthInterceptor', ['$rootScope', '$q', '$window', function ($rootScope, $q, $window) {
    return {
        // Send the Authorization header with each request
        'request': function (config) {
            config.headers = config.headers || {};
            if ($window.sessionStorage.getItem("token")) {
                config.headers.Authorization = $window.sessionStorage.getItem("token");
            }
            return config;
        },
        responseError: function (rejection) {
            if (rejection.status == 404) {
                $window.location = '/';
            }
            return $q.reject(rejection);
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