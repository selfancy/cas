function checkUserName() {
    var username = $("#username").val()
    if (username === "") {
        layer.alert("用户名不能为空", {icon: 2});
        return;
    }
    $.ajax({
        type: 'POST',
        url: /*[[@{/biz/user/checkUserName}]]*/'biz/user/checkUserName',
        data: {"username": username},
        success: function (result) {
            if (result.code === 0) {
                openCaptchaDialog()
            } else {
                layer.alert(result.msg, {icon: 2});
            }
        },
        error: function () {
            layer.alert("请求失败:", {icon: 2});
        }
    });


}


function openCaptchaDialog() {
    $("#captcha").val("")
    getCaptcha()
    var index = layer.open({
        type: 1,
        title: "请输入图片中的字母",
        content: $("#captchaDialog"),
        btn: ['确定'],
        yes: function () {
            sendSmsCode(index)
        }
    });


}

function sendSmsCode(index) {
    var username = $("#username").val()
    var captcha = $("#captcha").val()
    if (username === "") {
        layer.alert("用户名不能为空", {icon: 2});
        layer.close(index);
        return;
    }

    $.ajax({
        type: 'POST',
        url: /*[[@{/biz/sms/sendSmsCode}]]*/'biz/sms/sendSmsCode',
        data: {"username": username, "captcha": captcha},
        success: function (result) {
            if (result.code === 0) {
                layer.alert("短信验证码发送成功,手机尾号:" + result.mobile);
                $("#sencSmsCode_btn").attr("disabled", "disabled")
                countDown();
                layer.close(index);
            } else {
                layer.alert(result.msg, {icon: 2});
            }
        },
        error: function () {
            layer.alert("请求失败:", {icon: 2});
        }
    });

}


window.smsCodeCountDown = 60;

function countDown() {
    window.smsCodeCountDown = window.smsCodeCountDown - 1;
    $("#sencSmsCode_btn").html(window.smsCodeCountDown + "秒后重新发送");
    if (window.smsCodeCountDown == 0) {
        $("#sencSmsCode_btn").html("发送短信验证码");
        $("#sencSmsCode_btn").removeAttr("disabled")
        window.smsCodeCountDown = 60;
        return;
    }
    setTimeout('countDown()', 1000);
}

function getCaptcha() {
    var username = $("#username").val()
    var url = /*[[@{/tools/getCaptcha}]]*/"tools/getCaptcha";
    url = url + "?username=" + username;
    $("#captcha_img").attr("src", url);
}