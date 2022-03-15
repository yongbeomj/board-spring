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
        url : "/board1/replywrite",
        data : {"bno" : bno, "rcontents" : rcontents},
        success : function(data){
            if( data == 1 ){
                $('#replytable').load( location.href+' #replytable' );
                $("#rcontents").val("");
            }else if( data == 2 ) {
                alert("로그인 하세요");
                return;
            }
        }
    });
}

function rrewrite(rno, rparent, rdepth, rorder){
    alert(rno+","+rparent+","+ rdepth+","+ rorder)
    document.getElementById("rrewrite"+rno).innerHTML =
    "<div class='row'>"+
        "<div class='col-md-10'>"+
            "<input class='form-control' type='text' id='rcontents' name='rcontents'>"+
        "</div>"+
        "<div class='col-md-2'>"+
            "<button class='form-control' th:onclick='rwrite([[${boardDto.bno}]])'>댓글작성</button>"+
        "</div>"+
    "</div>"

    var rcontents = $("#rcontents").val();
}

function rdelete(rno) {
    $.ajax({
        url : "/board1/replydelete",
        data : {"rno" : rno},
        success : function(result){
            if (result == 1) {
                $('#replytable').load( location.href+' #replytable' );
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
                url: "/board1/replyupdate",
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