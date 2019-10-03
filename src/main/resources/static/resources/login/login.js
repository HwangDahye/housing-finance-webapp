angular.module('login', ['auth']).controller('loginCtrl', function($scope, auth) {
  const JOIN = "join";  const LOGIN = "login";
  $scope.credentials = {id:null,password:null,name:null};

  $scope.authenticated = function() { return auth.authenticated; }
  $scope.join = function() {
    setCredentials(JOIN);
    auth.signup($scope.credentials, function(authenticated) {
      if (authenticated) { console.log("Join succeeded"); $scope.error = false; $('.sign').animate({height: "toggle", opacity: "toggle"}, "slow");}
      else { console.log("Join failed"); $scope.error = true; }
    })
  };
  $scope.login = function() {
    setCredentials(LOGIN);
    auth.authenticate($scope.credentials, function(authenticated) {
      if (authenticated) { console.log("Login succeeded"); $scope.error = false;}
      else { console.log("Login failed"); $scope.error = true; }
    })
  };
  $scope.logout = function() { auth.clear(); }

  var setCredentials = function(type){
    if(type == JOIN){
      $scope.credentials.id = $('.form input[name="id"]').val();
      $scope.credentials.password = $('.form input[name="password"]').val();
      $scope.credentials.name = $('input[name="name"]').val();
    }else if(type == LOGIN){
      $scope.credentials.id = $('.login-form input[name="id"]').val();
      $scope.credentials.password = $('.login-form input[name="password"]').val();
    }
  }

  $(document).ready(function() {
    $('.message a').click(function () {
      $('.sign').animate({height: "toggle", opacity: "toggle"}, "slow");
    });
  });
});
