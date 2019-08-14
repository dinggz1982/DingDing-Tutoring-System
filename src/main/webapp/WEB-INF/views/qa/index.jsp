<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>智能问答</title>
<link rel="stylesheet" href="/layim/css/layui.css" media="all">
<script src="/layim/layui.js"></script>
<script src="/tag/js/jquery-1.11.3.min.js"></script>

</head>
<body>
	<script>
		layui.use('layim', function(layim) {
			//先来个客服模式压压精
			layim.config({
				//初始化界面
				init : {
					url : '/qa/init', //接口地址（返回的数据格式见下文）
					type : 'get', //默认get，一般可不填
					data : {}
				//额外参数
				}
			});
			layim.on('sendMessage', function(res){
				  var mine = res.mine; //包含我发送的消息及我的信息
				  var to = res.to; //对方的信息
				  var sentId = mine.id;
				  var content = mine.content;
				  var toId = to.id;
				  
				  //通过ajax方式向后台请求问题的答案
				  $.ajax({
					    type : 'GET',
					    url : '/qa/answer',
					    data : {"sentId":sentId,"content":content,"toId":toId},
					    success : function (result) {
					    	//alert("sentId"+sentId+",toId"+toId);
					    	layim.getMessage({
					    		  username: to.username //消息来源用户名
					    		  ,avatar: to.avatar //消息来源用户头像
					    		  ,id:toId  //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
					    		  ,type: "friend" //聊天窗口来源类型，从发送消息传递的to里面获取
					    		  ,content: result.content //消息内容
					    		  ,cid: 0 //消息id，可不传。除非你要对消息进行一些操作（如撤回）
					    		  ,mine: false //是否我发送的消息，如果为true，则会显示在右方
					    		  ,fromid: toId //消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
					    		  ,timestamp: result.timestamp //服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
					    		});

					    },

					    error : function (xhr, errorText, errorStatus) {
					        alert(xhr.status + ':' + xhr.statusText);
					    }
					});
				
				  });
		});
	</script>

</body>
</html>