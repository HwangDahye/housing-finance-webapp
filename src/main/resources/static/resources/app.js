
var app = angular.module("myApp",[
  'ngRoute','auth','home','login'
]);
app.controller('controller', function($scope, $filter){
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