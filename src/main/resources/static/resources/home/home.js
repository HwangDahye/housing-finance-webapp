angular.module('home', ['auth']).controller('homeCtrl', function($scope, $http, $location, auth){
  const LOAD_URI = "/api/finance/load";
  const GET_BANKS_URI = "/api/finance/banks";
  const GET_TOTAL_AMOUNT_URI = "/api/finance/total/amount";
  const GET_TOP_BANK_URI = "/api/finance/top";
  const GET_AVG_MIN_MAX_URI = "/api/finance/minmax/avg/amount";
  const PREDICT_URI = "/api/finance/predict";
  const BEARER_TYPE = "Bearer";

  var accessToken = localStorage.getItem('accessToken');
  var refreshToken = localStorage.getItem('refreshToken');
  var headers = {"Authorization" : BEARER_TYPE+" "+accessToken};
  var viewer = null;
  var timer;

  $scope.userId = localStorage.getItem('userId');
  $scope.csvLoad = false;
  $scope.doPredictClicked = false;
  $scope.getMinMaxAvgClicked = false;
  $scope.predictParamWarning = false;

  $scope.load = function(){
    $scope.doPredictClicked=false;
    $scope.getMinMaxAvgClicked = false;
    $http({url : LOAD_URI, headers : headers, method : 'POST'})
    .then(function success(res){
      if(res.data.success){
        $scope.csvLoad = true;
        printResult(res.data.data);
      }else{
        $scope.csvLoad = false;
        requestFail(res.data);
      }
    })
    .catch(function (err){
      console.log(err);
      if(err.status == 401) auth.clear();
    });
  };
  $scope.getBanks = function(){
    $scope.doPredictClicked=false;
    $scope.getMinMaxAvgClicked = false;
    $http({url : GET_BANKS_URI, headers : headers, method : 'GET'})
    .then(function success(res){
      if(res.data.success){
        printResult(res.data.data);
      }else{
        requestFail(res.data);
      }
    },function error(res){  requestError(); })
    .catch(function (err){
      console.log(err);
      if(err.status == 401) auth.clear();
    });
  };
  $scope.getTotalSum = function(){
    $scope.doPredictClicked=false;
    $scope.getMinMaxAvgClicked = false;
    $http({url : GET_TOTAL_AMOUNT_URI, headers : headers, method : 'GET'})
    .then(function success(res){
      if(res.data.success){
        printResult(res.data.data);
      }else{
        requestFail(res.data);
      }
    },function error(res){  requestError(); })
    .catch(function (err){
      console.log(err);
      if(err.status == 401) auth.clear();
    });
  };
  $scope.getTopOfBank = function(){
    $scope.doPredictClicked=false;
    $scope.getMinMaxAvgClicked = false;
    $http({url : GET_TOP_BANK_URI, headers : headers, method : 'GET'})
    .then(function success(res){
      if(res.data.success){
        printResult(res.data.data);
      }else{
        requestFail(res.data);
      }
    },function error(res){  requestError(); })
    .catch(function (err){
      console.log(err);
      if(err.status == 401) auth.clear();
    });
  };
  $scope.getMinMaxAvg = function(){
    let bank = $('input[name="minMaxBank"]').val();
    $scope.doPredictClicked=false;
    $http({url : GET_AVG_MIN_MAX_URI, params : bank != null && bank !== "" ? {bank:bank} : {}, headers : headers, method : 'GET'})
    .then(function success(res){
      if(res.data.success){
        printResult(res.data.data);
      }else{
        requestFail(res.data);
      }
    },function error(res){  requestError(); })
    .catch(function (err){
      console.log(err);
      if(err.status == 401) auth.clear();
    });
  }
  $scope.doPredict = function(){
    $scope.getMinMaxAvgClicked = false;
    let bank = $('input[name="bank"]').val();
    let month = $('input[name="month"]').val();

    if(bank === "" || month === ""){
      $scope.predictParamWarning = true;
      return;
    }
    $scope.predictParamWarning = false;
    $http({url : PREDICT_URI, headers : headers, params :{bank : bank, month: month}, method : 'GET'})
    .then(function success(res){
      if(res.data.success){
        // printResult(res.data.data);
        let result = res.data.data;
        let resultString = result.year+"년 "+result.month+"월 "+bank+"("+result.bank+")의 예상 지원금액은 "+result.amount+"(억원)으로 예측되었습니다.";
        printResult(resultString);
      }else{
        requestFail(res.data);
      }
    },function error(res){  requestError(); })
    .catch(function (err){
      console.log(err);
      if(err.status == 401) auth.clear();
    });
  };

  requestFail = function(res){
    printResult(res);
  };

  requestError = function(){

  };

  printResult = function(data){
    if(viewer == null){
      viewer =new JsonEditor('#json-renderer', data);
      return;
    }
    viewer.load(data);
  }

  $scope.viewClear = function(){
    viewer.load(null);
  }

  $scope.logout = function(){
    clearInterval(timer);
    auth.clear();
  }

  parseJwt = function() {
    var base64Url = accessToken.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  };



  var timerSetting = function(){
    var decode = parseJwt();
    var compareDate = new Date(decode.exp *1000);


    timer = setInterval(function() {
      timeBetweenDates(compareDate);
    }, 1000);
  };

  var timeBetweenDates = function(toDate) {
    var dateEntered = toDate;
    var now = new Date();
    var difference = dateEntered.getTime() - now.getTime();

    if (difference <= 0) {
      clearInterval(timer);
      // Timer done
      if(confirm("인증이 만료되었습니다. 연장하시겠습니까?")){
        $scope.tokenReissue();
        return;
      }else{
        auth.clear();
      }
    } else {

      var seconds = Math.floor(difference / 1000);
      var minutes = Math.floor(seconds / 60);
      var hours = Math.floor(minutes / 60);
      var days = Math.floor(hours / 24);

      hours %= 24;
      minutes %= 60;
      seconds %= 60;

      // $("#days").text(days);
      // $("#hours").text(hours);
      $("#minutes").text(minutes);
      $("#seconds").text(seconds);
    }
  };

  $(document).ready(function() {
    timerSetting();
  });
  $scope.tokenReissue = function(){
    clearInterval(timer);
    auth.reissue($scope.userId, refreshToken, function(authenticated) {
      if (authenticated) {
        accessToken = localStorage.getItem('accessToken');
        refreshToken = localStorage.getItem('refreshToken');
        timerSetting();
      }
      else { console.log("Login failed"); $scope.error = true; }
    });
  };

  var currentLocation = $location.url();
  $scope.$on("$locationChangeSuccess", function handleLocationChangeStartEvent(event) {
    currentLocation = $location.url();
  });

  $scope.$on("$locationChangeStart", function(event){
    event.preventDefault();
    var targetPath = $location.path();
    if (currentLocation != targetPath)
    {
      clearInterval(timer);
      auth.clear();
    }
  });
});