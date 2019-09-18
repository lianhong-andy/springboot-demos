<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<body>
<div id="app" style="margin: 20px 20%">
    <div>
        <form action="/demo/user/login" method="post">
            <span>username:</span><input type="text" name="name" placeholder="username"><br/>
            <span>password:</span><input type="password" name="password" placeholder="password"><br/>
            <input type="submit" value="登录">
        </form>
    </div>
</div>
</body>
</html>