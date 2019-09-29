# Housing Finance Service

{% api-method method="post" host="http://localhost:8080" path="/api/auth/signup" %}
{% api-method-summary %}
Sign Up
{% endapi-method-summary %}

{% api-method-description %}
This API This API provides an opportunity to join the Housing Finance Service.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="Content-Type" type="string" required=true %}
application/json
{% endapi-method-parameter %}
{% endapi-method-headers %}

{% api-method-body-parameters %}
{% api-method-parameter name="id" type="string" required=true %}
Your Id
{% endapi-method-parameter %}

{% api-method-parameter name="password" type="string" required=true %}
Your Password
{% endapi-method-parameter %}

{% api-method-parameter name="id" type="string" required=false %}
Your Name
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
SignUp successfully retrieved.
{% endapi-method-response-example-description %}

```javascript
{
    "success": true,
    "code": 0,
    "msg": "성공"
}
```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=400 %}
{% api-method-response-example-description %}
Not Blank ! Required params\(id, password\)
{% endapi-method-response-example-description %}

```javascript
{
    "timestamp": "2019-09-27T12:48:04.954+0000",
    "status": 400,
    "error": "Bad Request",
    "errors":[
    {"codes":["NotBlank.signUpReq.password", "NotBlank.password", "NotBlank.java.lang.String", "NotBlank" ], "arguments":[{"codes":["signUpReq.password",…}
    ],
    "message": "Validation failed for object='signUpReq'. Error count: 1",
    "path": "/signup"
}
```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=405 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
{
    "timestamp": "2019-09-27T12:31:25.937+0000",
    "status": 405,
    "error": "Method Not Allowed",
    "message": "Request method 'GET' not supported",
    "path": "/signup"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

{% api-method method="get" host="http://localhost:8080" path="/api/auth/signin" %}
{% api-method-summary %}
Sign In
{% endapi-method-summary %}

{% api-method-description %}
Let's log in and save token in response
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="id" type="string" required=true %}
Your Id
{% endapi-method-parameter %}

{% api-method-parameter name="password" type="string" required=true %}
Your Password
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}

{% api-method-headers %}
{% api-method-parameter name="Content-Type" type="string" required=false %}
application/json
{% endapi-method-parameter %}
{% endapi-method-headers %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Log in Success
{% endapi-method-response-example-description %}

```javascript
{
    "success": true,
    "code": 0,
    "msg": "성공",
    "data":{
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYWh5MjEyIiwiaWF0IjoxNTY5NzI3Nzc1LCJleHAiOjE1Njk3MzEzNzV9.9rnguGpVMofLuvjXiE1-Wo551_2-gUlLNHfSDEi0ZzU",
        "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYWh5MjEyIiwiaWF0IjoxNTY5NzI3Nzc2LCJleHAiOjE1NzA5MzczNzZ9.AWaMwkK6A-s-DHdm1bvwCbOcdLhWEx8zPnLvAsZznmY"
    }
}
```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=401 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
// Invalid Password
{
    "success":false,
    "code":-1001,
    "msg":"비밀번호가 일치하지 않습니다. 비밀번호를 확인해주세요"
}
// Not found user
{
    "success":false,
    "code":-1002,
    "msg":"회원 정보를 찾을 수 없습니다. 회원가입을 진행해주세요"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

