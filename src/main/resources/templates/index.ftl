<!DOCTYPE html>
<html lang="ko" ng-app="myApp" ng-controller="controller">
<head>
  <base href="/" />
  <script src="/resources/scripts/libs/angular-1.3.9/angular.js"></script>
  <script src="/resources/scripts/libs/angular-1.3.9/angular-route.js"></script>
  <script src="/resources/scripts/libs/bootstrap/ui-bootstrap-tpls-1.2.4.min.js"></script>
  <script src="/resources/scripts/libs/angular-1.3.9/directives/paging.js"></script>
  <script src="/resources/scripts/auth/auth.js"></script>
  <script src="/resources/home/home.js"></script>
  <script src="/resources/login/login.js"></script>
  <script src="/resources/app.js"></script>
  <#-- jquery -->
  <script src="/resources/scripts/libs/jquery-2.1.4.js"></script>

  <#-- bootstrap -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

  <#-- table, modal-->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
  <script src='https://cdn.datatables.net/1.10.9/js/jquery.dataTables.min.js'></script>
  <link rel='stylesheet prefetch' href='http://cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.9.0/bootstrap-table.min.css'>
  <link rel='stylesheet prefetch' href='https://cdn.datatables.net/1.10.9/css/jquery.dataTables.min.css'>

  <#--json viewer-->
  <script src="/resources/scripts/libs/json-viewer/jquery.json-editor.js"></script>
  <script src="/resources/scripts/libs/json-viewer/jquery.json-viewer.js"></script>
  <link href="/resources/scripts/libs/json-viewer/jquery.json-viewer.css" type="text/css" rel="stylesheet">

  <#-- custom -->
  <link href="/resources/css/filter.css" rel="stylesheet">
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