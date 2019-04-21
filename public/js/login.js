app.factory('UserService', ['AppUtil', '$window', function (AppUtil, $window) {
    return {
        authUser: function(user){
            return AppUtil.deferHttpPost("/login", user).then(
                function(d){
                    $window.sessionStorage.setItem("token", user.token);
                    return d;
                }
            );
        },
        getToken: function(){
            var t = $window.sessionStorage.getItem("token") 
            return t ? t : false;
        },
        validateSession(){
            if($window.sessionStorage.getItem("token") == null){
                $window.location = '/';
                return;
            }
        }
    };
}]);
app.controller('LoginController', ['$location', 'UserService', function($location, LoginService){
    var self = this;
    self.user = {};
    self.authUser = function(){
        LoginService.authUser(self.user).then(
            function(d){
                if(d.token){
                    $location.path('/dashboard');
                }
                else if(d.Error){
                    alert(d.Error);
                }
            }
        )
    };

}]);