<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="its" uri="/WEB-INF/tlds/remark.tld"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>${user.username }的标注</title>
<%@include file="/WEB-INF/views/include/top.jsp"%>
<script type="text/javascript" src="static/echarts/echarts.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="/static/lib/layerui/2.2.5/css/layui.css">

</head>
<body>
	<nav class="breadcrumb"> <i class="Hui-iconfont">&#xe67f;</i> 首页
	<span class="c-gray en">&gt;</span> 用户评论语料库<span class="c-gray en">&gt;</span>
	标注任务总体进展 <a class="btn btn-success radius r"
		style="line-height:1.6em;margin-top:3px"
		href="javascript:location.replace(location.href);" title="刷新"><i
		class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="pd-20 col-xs-10 col-md-offset-1"></div>
	
      
      
    
    <div style="clear: both;width: 80%;margin: 0 auto;">
    <fieldset class="layui-elem-field layui-field-title">
        <legend style="text-align: center;" id="course">课程名：${comment.course }(${comment.courseType })</legend>
        <p>${comment.content }</p>
      </fieldset>
    </div>  
	<div class="col-xs-10 col-md-offset-1">
		<table
			class="table table-border table-bordered table-hover table-bg table-sort">
			<tr>
				<th>姓名</th>
				<th>评论内容</th>
				<th>内容相关</th>
				<th>情感相关</th>
				<th>其他类</th>
				<th>标注时间</th>
				<th>操作</th>
				<c:forEach items="${remarks }" var="remark" varStatus="status">
					<tr>
						<td>${remark.user.username}</td>
						<td>${remark.userComment.content}</td>
						<td>${its:content(remark.contentRelated)}</td>
						<td>${its:emotion(remark.emotionRelated)}</td>
						<td>${its:other(remark.otherRelated)}</td>
						<td><fmt:formatDate value="${remark.createTime}"
								pattern="yyyy-MM-dd HH:mm" /></td>
						<td><button  onclick="save_submit(${remark.id});"
								class="btn btn-primary radius" type="submit">
								<i class="Hui-iconfont"></i> 同意这条标注
							</button></td>
					</tr>
				</c:forEach>
			</tr>
		</table>

	</div>
	<script type="text/javascript"
		src="${ctx }/static/lib/layerui/2.2.5/layui.js"></script>
	<script type="text/javascript">
		function save_submit(id) {
			$.ajax({
				url : '/corpus/updatecorpus/'+id,
				success : function(data) {
					if(data=="1"){
						alert("更新成功！");
						 $(".btn-primary").each(function (e) {
						 var me = $(this);
						 me.attr("disabled",true);
						 me.css('display','none'); 
						 });
						 $("#course").append("已更新");
					}else{
						alert("更新失败！请联系管理员");
					}
				},
				fail: function(e){
					alert("更新失败！请联系管理员");
				}
			});
		}
	</script>
</body>
</html>
