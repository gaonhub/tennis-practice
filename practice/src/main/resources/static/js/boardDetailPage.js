$("#comment-submit").click(function () {
  let comment = {
    content: $("#comment").val(),
    boardId: $("#board-id").val()
  }
  Swal.fire({
    title: "댓글을 등록하시겠습니까?",
    icon: "success",
    inputType: "submit",
    showDenyButton: true,
  }).then((confirm) => {
    if (confirm.isConfirmed) {
      $.ajax({
        type: "post",
        async: false,
        url: "/api/comments/commentAdd/" + comment.boardId,
        data: comment,
        success: function (data) {
          if (data.result === "success") {
            window.location.reload();
          } else if (data.result === "validate") {
            Swal.fire("내용은 100자 이내여야 합니다.", '', 'warning');
          }
        },
        error: function () {
          Swal.fire({
            icon: 'error',
            title: '오류가 발생했습니다'
          })
        },
      });
    } else if (confirm.isDenied) {
      Swal.fire('등록을 취소하였습니다.', '', 'info')
    }
  });
});

const deleteComment = (commentId) => {
  Swal.fire({
    title: "삭제하시겠습니까?",
    icon: "warning",
    inputType: "submit",
    showDenyButton: true,
  }).then((confirm) => {
    if (confirm.isConfirmed) {
      $.ajax({
        type: "post",
        url: "/api/comments/commentDelete/" + commentId,
        success: function (data) {
          if (data.result === "success") {
            window.location.reload();
          }
        },
        error: function () {
          Swal.fire({
            icon: 'error',
            title: '오류가 발생했습니다'
          })
        },});
    } else if (confirm.isDenied) {
      Swal.fire('삭제를 취소하였습니다.', '', 'info')
    }
  });
};

const updateComment = (commentId) => {
  $("#comment-content-" + commentId).prop('disabled', false);
  $("#comment-update-" + commentId).attr("hidden", true);
  $("#comment-delete-" + commentId).attr("hidden", true);
  $("#comment-update-confirm-" + commentId).attr("hidden", false);
  $("#comment-update-cancel-" + commentId).attr("hidden", false);
};

const updateCommentConfirm = (commentId) => {

  let comment = {
    content: $("#comment-content-" + commentId).val()
  }
  Swal.fire({
    title: "수정하시겠습니까?",
    icon: "success",
    inputType: "update",
    showDenyButton: true,
  }).then((confirm) => {
    if (confirm.isConfirmed) {
      $.ajax({
        type: "post",
        url: "/api/comments/commentUpdate/" + commentId,
        data: comment,
        success: function (data) {
          if (data.result === "success") {
            window.location.reload();
            $("#comment-content-" + commentId).prop('disabled', true);
            $("#comment-update-" + commentId).attr("hidden", false);
            $("#comment-delete-" + commentId).attr("hidden", false);
            $("#comment-update-confirm-" + commentId).attr("hidden", true);
            $("#comment-update-cancel-" + commentId).attr("hidden", true);
          } else if (data.result === "validate") {
            Swal.fire("내용은 100자 이내여야 합니다.", '', 'warning');
          }
        },
        error: function () {
          Swal.fire({
            icon: 'error',
            title: '오류가 발생했습니다'
          })
        },
      });
    } else if (confirm.isDenied) {
      Swal.fire('수정을 취소하였습니다.', '', 'info')
    }
  });
}

const updateCommentCancel = (commentId) => {
  let comment = {
    content: $("#comment-content-" + commentId).val()
  }
  $.ajax({
    type: "post",
    url: "/api/comments/commentUpdateCancel/" + commentId,
    data: comment,
    success: function (data) {
      if (data.result === "success") {
        window.location.reload();
        $("#comment-content-" + commentId).prop('disabled', true);
        $("#comment-update-" + commentId).attr("hidden", false);
        $("#comment-delete-" + commentId).attr("hidden", false);
        $("#comment-update-confirm-" + commentId).attr("hidden", true);
        $("#comment-update-cancel-" + commentId).attr("hidden", true);
      }
    },
    error: function () {
      Swal.fire({
        icon: 'error',
        title: '오류가 발생했습니다'
      })
    },
  });
};

const deleteBoard = (boardId) => {
  Swal.fire({
    title: "삭제하시겠습니까?",
    icon: "warning",
    inputType: "submit",
    showDenyButton: true,
  }).then((confirm) => {
    if (confirm.isConfirmed) {
    $.ajax({
      type: "post",
      url: "/api/boards/boardDelete/" + boardId,
      success: function (data) {
        if (data.result === "success") {
          window.location.assign("http://localhost:8080/boards/home");
        }
      },
      error: function () {
        Swal.fire({
          icon: 'error',
          title: '오류가 발생했습니다'
        })
      },
    });
  } else if (confirm.isDenied) {
      Swal.fire('삭제를 취소하였습니다.', '', 'info')
    }
  });
};

