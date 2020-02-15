//publish提交问题时的验证
function check() {
    var flag = true;
    var title = document.getElementById("title");
    var description = document.getElementById("description");
    var tag = document.getElementById("tag");
    if (title.value.length == 0) {
        flag = false;
        title.style.background = "#FFF0F6";
        title.placeholder = "标题不能为空";
    }

    if (description.value.length == 0) {
        flag = false;
        description.style.background = "#FFF0F6";
        description.placeholder = "补充不能为空";
    }
    if (tag.value.length == 0) {
        flag = false;
        tag.style.background = "#FFF0F6";
        tag.placeholder = "标签不能为空";
    }
    return flag;
}


//展开二级评论
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment2) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment2.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment2.user.name
                    })).append($("<div/>", {
                        "html": comment2.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment2.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

//二级评论的实现
$(function () {
    var cs = window.localStorage.getItem("commentSuccess");
    var s9 = window.localStorage.getItem("sss996");
    var judgespan = parseToDOM(s9);
    if (cs == "true") {
        collapseComments(judgespan[0]);
        //成功后清空localstorage
        window.localStorage.removeItem("sss996");
        window.localStorage.removeItem("commentSuccess");
    }
});


function parseToDOM(str) {
    var div = document.createElement("div");
    if (typeof str == "string")
        div.innerHTML = str;
    return div.childNodes;
}

function comment(e) {
    //获取评论的问题
    var commentId = e.getAttribute("data-id");
    //获取评论的内容
    var content = $("#input-" + commentId).val();
    //把对应的span放入其中(commentId与id的值相同)
    var test = document.getElementById(commentId);
    window.localStorage.setItem("sss996", test.outerHTML);
    //添加一个全局标志位，说明添加成功了
    window.localStorage.setItem("commentSuccess", "true");
    comment2target(commentId, 2, content);
}

//question.html里的回复功能
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("回复的内容不能为空!");
        return false;
    }
    $.ajax({
        type: "post",
        url: "/comment",
        contentType: "application/json",
        //JSON.stringify作用是将js对象转换成json数据
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
                // $("#comment_section").hide()
            } else {
                //判断是否需要登录
                if (response.code == 2003) {
                    //请选择需要登录否
                    var accepted = confirm(response.message);
                    //确认登录
                    if (accepted) {
                        //跳转
                        window.open("https://github.com/login/oauth/authorize?client_id=a59d9015ba71476d171d&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closeable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

//显示标签
function showSelectTag() {
    $("#select-tag").show();
}

//添加标签
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    //判断该标签是否存在
    if (previous.split(',').indexOf(value) == -1) {
        //不存在
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}

//下拉不显示第一个
function pages_select() {
    if ($("#select_option")[0].options.length == 2) {
        //删除第一个option
        $("select option").each(function (i) {
            if (i == 0) {
                $(this).remove();
            }
        })
    }
}

//点赞
function like(commentId, e) {
    var likeTagId = "#" + e.getAttribute("id");
    var likeCountId = "#" + $(likeTagId).next().attr("id");
    $.ajax({
        url: "/comment",
        type: "GET",
        data: {
            commentId: commentId
        },
        dataType: "json",
        success: function (result) {
            if (result >= 0) {
                console.log("haha")
                $(likeCountId).text(result);
                $(likeTagId).addClass("active");
            } else if (result == -1) {
                //表示已经点过赞了
                $(likeTagId).addClass("active");
            }
        }
    })
}
/*模态框*/
$(document).on('click', "#login-link", function () {
    init_third_login_button();
    $("#login-link-modal").modal();
});

function init_third_login_button() {
    third_login_button =
        '<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 aw-article-content" style="margin-top: 20px;font-size: 14px">您可以用以下方式免注册登录<br>(部分暂时无法使用)</div>\n' +
        '                            <button name="github-login" class="col-lg-5 col-md-5 col-xs-5 col-sm-5 btn btn-dark third-login-button"><i class="iconfont icon-git margin-center"></i>GitHub</button>\n' +
        '                            <button name="baidu-login" class="col-lg-5 col-md-5 col-xs-5 col-sm-5 btn btn-primary third-login-button" disabled><i class="iconfont icon-baidu margin-center"></i>Baidu</button>\n' +
        '                            <button name="wechat-login" class="col-lg-5 col-md-5 col-xs-5 col-sm-5 btn btn-success third-login-button" disabled><i class="iconfont icon-weixin margin-center"></i>WeChat</button>\n' +
        '                            <button name="qq-login" class="col-lg-5 col-md-5 col-xs-5 col-sm-5 btn btn-info third-login-button" disabled><i class="iconfont icon-QQ margin-center"></i>QQ</button>';
    button = $("div[name='third-login-button']");
    button.empty();
    button.append(third_login_button);
}


