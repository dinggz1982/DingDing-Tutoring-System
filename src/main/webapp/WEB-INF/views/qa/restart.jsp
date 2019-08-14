<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="its" uri="/WEB-INF/tlds/remark.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>服务正在重新启动</title>
<%@include file="/WEB-INF/views/include/top.jsp"%>
   <link rel="stylesheet" type="text/css" href="/static/lib/layerui/2.2.5/css/layui.css">

</head>
<body>
	<nav class="breadcrumb">
	<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
	服务正在重新启动<span class="c-gray en">&gt;</span>  <a
		class="btn btn-success radius r"
		style="line-height:1.6em;margin-top:3px"
		href="javascript:location.replace(location.href);" title="刷新"><i
		class="Hui-iconfont">&#xe68f;</i></a></nav>
		<div class="pd-20 col-xs-10 col-md-offset-1">
 </div>
		 
	<div class="col-xs-10 col-md-offset-1">
	<div style="margin-bottom: 10px">
		<a class="btn btn-primary radius" href="/qa/addIntent" ><i class="Hui-iconfont"></i> 服务正在重新启动</a>  
	 </div>
		<h1>模型训练完，可以重启服务</h1>
						<div class="col-xs-8 col-xs-offset-3" onclick="restart()">
							<input class="btn btn-primary" type="submit"
								value="&nbsp;&nbsp;重启服务&nbsp;&nbsp;">
						</div>
 	</div>
</body>
<script type="text/javascript">
function restart(){
  $.ajax({
		          type: "get",
		          url: "/qa/server",
		          dataType: "json",
		          data: {},
		          success: function (data) {
              if (data.status == "success") {
                  alert("重启成功！");
              }
          }
		      });

}
</script>
</html>
