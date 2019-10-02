<!DOCTYPE html>
<html lang="ko" ng-app="myApp" ng-controller="controller">
<head>
  <base href="/" />
  <script src="/resources/scripts/libs/angular-1.3.9/angular.js"></script>
  <script src="/resources/scripts/libs/angular-1.3.9/angular-route.js"></script>
  <script src="/resources/scripts/auth/auth.js"></script>
  <script src="/resources/home/home.js"></script>
  <script src="/resources/login/login.js"></script>
  <script src="/resources/app.js"></script>
  <#-- jquery -->
  <script src="/resources/scripts/libs/jquery-2.1.4.js"></script>

  <#-- bootstrap -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

  <#--json viewer-->
  <script src="/resources/scripts/libs/json-viewer/jquery.json-editor.js"></script>
  <script src="/resources/scripts/libs/json-viewer/jquery.json-viewer.js"></script>
  <link href="/resources/scripts/libs/json-viewer/jquery.json-viewer.css" type="text/css" rel="stylesheet">

  <#-- custom -->
  <link href="/resources/login/login.css" rel="stylesheet">
  <link href="/resources/home/home.css" rel="stylesheet">
  <meta charset="UTF-8">
  <title>${title}</title>
</head>
<body>
<#--Contents-->
<div  ng-controller="controller">
  <div ng-view />
</body>
</html>