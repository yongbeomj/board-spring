function bdelete(bno) {
    $.ajax({
        url : "/board1/boarddelete",
        data : {"bno" : bno},
        success : function(result){
            if (result == 1){
                alert("삭제 완료");
                location.href = "/board1/boardlist";
            } else {
                alert("오류 발생");
            }

        }
    });
}

function bupdate(bno) {

    document.getElementById("tdtitle").innerHTML = "<input type='text' name='newtitle'>";
    document.getElementById("tdcontents").innerHTML = "<input type='text' name='newcontents'>";
    document.getElementById("btnbupdate").style = "display:none";
    document.getElementById("btnbchange").style = "display:block";

    // 확인 버튼을 누르면
    $("#btnbchange").click(function() {
        $.ajax({
            url: "/board1/boardupdate",
            data: {"newtitle" : document.getElementById("newtitle").value,
            "newcontents" : document.getElementById("newcontents").value},
            success : function(result){
                if (result == 1){
                    alert("수정 완료");
                } else {
                    alert("오류 발생")
                }
            }

        });

    })
}