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