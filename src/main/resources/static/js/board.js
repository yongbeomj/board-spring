function bdelete(bno, cano) {

    $.ajax({
        url : "/board/boarddelete",
        data : {"bno" : bno, "cano" : cano},
        success : function(result){
            if (result == 1){
                alert("삭제 완료");
                location.href = "/board"+cano+"/boardlist";
            } else {
                alert("오류 발생");
            }
        }
    });
}

function rwrite(bno) {
    var rcontents = $("#rcontents").val();
    if (rcontents == "") {
        alert("내용 입력하시오");
        return;
    }
    $.ajax({
        url : "/replywrite",
        data : {"bno" : bno, "rcontents" : rcontents},
        success : function(data){
            if( data == 1 ){
                location.reload();
            }else if( data == 2 ) {
                alert("로그인 하세요");
                return;
            }
        }
    });
}

function rrewrite(rno){
    document.getElementById("rreform"+rno).style = "display:block";
    $(function(){
        $("#btnrrechange" + rno).click(function(){ // 확인 버튼 클릭
            var rrecontents = $("#rrecontents" + rno).val();
            if (rrecontents == "") {
                alert("내용 입력하시오");
                return;
            }
            $.ajax({
                url : "/rereplywrite",
                data : {"rno" : rno, "rrecontents" : rrecontents},
                success : function(data){
                    if (data == 1){
                        location.reload();
                    } else {
                        alert("오류발생");
                        return;
                    }
                }
            });
        });
    });
}

function rdelete(rno) {
    $.ajax({
        url : "/replydelete",
        data : {"rno" : rno},
        success : function(result){
            if (result == 1) {
                location.reload();
                alert("댓글이 삭제되었습니다");
            } else {
                alert("오류발생");
            }
        }
    });
}

function rupdate(rno){

    document.getElementById("tdrcontents"+rno).innerHTML = "<input class='form-control' type='text' id='newcontents' name='newcontents'>";
    document.getElementById("btnrupdate"+rno).style = "display:none"; // 수정버튼 감추기
    document.getElementById("btnrchange"+rno).style = "display:block"; // 확인버튼 보이기

    $(function(){
        $("#btnrchange"+rno).click(function(){ // 확인 버튼 클릭
            $.ajax({
                url: "/replyupdate",
                data: {"rno" : rno, "newcontents" : document.getElementById("newcontents").value},
                success : function(result){
                    if(result == 1){
                        document.getElementById("tdrcontents"+rno).innerHTML = document.getElementById("newcontents").value;
                        document.getElementById("btnrchange"+rno).style = "display:none"; // 확인버튼 감추기
                        document.getElementById("btnrupdate"+rno).style = "display:block"; // 수정버튼 보이기
                    } else {
                        alert("오류발생");
                    }
                }
            });
        });
    });
}