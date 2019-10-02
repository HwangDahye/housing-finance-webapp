angular.module('auth', []).factory('auth', function($rootScope, $http, $location) {
  var auth = {
    authenticated: false,
    loginUrl: '/api/auth/signin',
    joinUrl: '/api/auth/signup',
    reissueTokenUrl : '/api/auth/refresh/token',
    loginViewPath: '/login',
    logoutViewPath: '/logout',
    homeViewPath: '/',
    authenticate: function (credentials, callback) {
      $http({
        url : auth.loginUrl,
        params : {'id':credentials.id, 'password' : credentials.password},
        method : 'GET'
      }).then(function successCallback(res){
        if(res.data.success){
          auth.authenticated = true;
          localStorage.accessToken = res.data.data.token;
          localStorage.refreshToken = res.data.data.refreshToken;
          localStorage.userId = res.data.data.user.id;
          $location.path(auth.homeViewPath);
          callback && callback(auth.authenticated);
        }else{
          alert(res.data.msg+"(에러코드:"+res.data.code+")");
          auth.authenticated = false;
          clearLocalStorage();
          callback && callback(false);
        }
      },function errorCallBack(res) {
        alert(res.data.msg+"(에러코드:"+res.data.code+")");
        auth.authenticated = false;
        clearLocalStorage();
        callback && callback(false);
      })
    },
    signup: function(credentials, callback){
      $http.post(auth.joinUrl, credentials)
      .then(function successCallback(res) {
        if (res.data.success) {
          callback && callback(true);
        }
      },function errorCallBack(res) {
        alert(res.data.msg+"(에러코드:"+res.data.code+")");
        callback && callback(false);
      })
    },
    reissue : function(userId, token, callback){
      $http({
        url : auth.reissueTokenUrl,
        headers : {"Authorization" : "Bearer "+token},
        params : {'id':userId},
        method : 'GET'
      }).then(function successCallback(res){
        if(res.data.success){
          auth.authenticated = true;
          localStorage.accessToken = res.data.data.token;
          localStorage.refreshToken = res.data.data.refreshToken;
          localStorage.userId = res.data.data.user.id;
          // $location.path(auth.homeViewPath);
          callback && callback(auth.authenticated);
        }else{
          alert(res.data.msg+"(에러코드:"+res.data.code+")");
          auth.authenticated = false;
          clearLocalStorage();
          callback && callback(false);
        }
      },function errorCallBack(res) {
        alert(res.data.msg+"(에러코드:"+res.data.code+")");
        auth.authenticated = false;
        clearLocalStorage();
        callback && callback(false);
      })
    },
    clear:function () {
        auth.authenticated = false;
        clearLocalStorage();
        $location.path(auth.loginViewPath);
    },
    init : function (homeViewPath, loginViewPath, logoutViewPath) {
        auth.homeViewPath = homeViewPath;
        auth.loginViewPath = loginViewPath;
        auth.logoutViewPath = logoutViewPath;

      $rootScope.$on('$routeChangeStart', function() { enter(); });
    }
  };
  var enter = function() {
    if($location.path() != auth.loginPath && isEmptyAccessToken()){
      auth.path = $location.path();
      if(!auth.authenticated){
        $location.path(auth.loginViewPath);
      }
    }
  };

  var clearLocalStorage = function() {
    if(localStorage.getItem('accessToken') != undefined && localStorage.getItem('accessToken') != null){
      localStorage.removeItem('accessToken');
    }
    if(localStorage.getItem('refreshToken') != undefined && localStorage.getItem('refreshToken') != null){
      localStorage.removeItem('refreshToken');
    }
    if(localStorage.getItem('userId') != undefined && localStorage.getItem('userId') != null){
      localStorage.removeItem('userId');
    }
  };

  var isEmptyAccessToken = function() {
    if(localStorage.getItem('accessToken') != undefined && localStorage.getItem('accessToken') != null){
      return false;
    }else{
      return true;
    }
  }

  return auth;
});

