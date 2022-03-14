function login(){
    var mid = $("#mid").val();
    var mpw = $("#mpw").val();
    var memberdto = { "mid" : mid , "mpw" : mpw };

    $.ajax({
        url: "/member/logincontroller",
        data : { "mid" : mid , "mpw" : mpw },
        method: "post",
        success: function(result) {
            if( result == 1 ) {
                location.href="/";
            }else{
                $("#loginfailmsg").html("아이디 혹은 비밀번호가 다릅니다.");
            }
        }
    });
}