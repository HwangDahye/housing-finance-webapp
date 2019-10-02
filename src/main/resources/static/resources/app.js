
var app = angular.module("myApp",[
  'ngRoute'
    ,'auth','home','login'
  ,'ui.bootstrap'
  ,'brantwills.paging'
]);
app.controller('controller', function($scope, $filter){
  $scope.commonData = {};
  $scope.SIGN_IN_URL = "http://localhost:8080/api/auth/signin";
  $scope.SIGN_UP_URL = "http://localhost:8080/api/auth/signup";
  $scope.MAIN_HOME_URL = "http://localhost:8080/#/home";

  // 삭제했어야 했는데 ㅠㅠ
  $scope.clickfuntion = function(){
    var time = $filter('date')(new Date(), 'yyyy-MM-dd HH:mm:ss Z');
    $scope.welcome = "Time = "+time;
  }
});


app.run(function(auth){
  auth.init('/', '/login', '/logout');
});

app.config(['$routeProvider', '$locationProvider'
  , function($routeProvider, $locationProvider) {
    $routeProvider
      .when('/login',{
        templateUrl: '/resources/login/login.html',
        controller: 'loginCtrl'
      }).when('/',{
        templateUrl: '/resources/home/home.html',
        controller: 'homeCtrl'
      });
    $routeProvider.otherwise({redirectTo: '/'});
    $locationProvider.html5Mode(true);
  }
]);