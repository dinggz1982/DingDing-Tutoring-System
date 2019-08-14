<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="its" uri="/WEB-INF/tlds/remark.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>语料库标注</title>
<%@include file="/WEB-INF/views/include/top.jsp"%>
<script type="text/javascript" src="static/echarts/echarts.min.js"></script>
   <link rel="stylesheet" type="text/css" href="/static/lib/layerui/2.2.5/css/layui.css">

</head>
<body>
	<nav class="breadcrumb">
	<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
	用户评论语料库<span class="c-gray en">&gt;</span> 标注任务总体进展 <a
		class="btn btn-success radius r"
		style="line-height:1.6em;margin-top:3px"
		href="javascript:location.replace(location.href);" title="刷新"><i
		class="Hui-iconfont">&#xe68f;</i></a></nav>
		<div class="pd-20 col-xs-10 col-md-offset-1">
 </div>
			 
	<div class="col-xs-10 col-md-offset-1">
		<table class="table table-border table-bordered table-hover table-bg table-sort" style="width: 100%">
 		<tr>
 			<th>
 				课程类型
 			</th>
 			<th>
 				课程
 			</th>
 			<th>
 				评论内容
 			</th>
 			<th>
 				内容相关
 			</th>
 			<th>
 				情感相关
 			</th>
 			<th>
 				其他类
 			</th>
 			<th>
 				标注情况
 			</th>
 			<th>
 				是否已标注成功
 			</th>
 			<c:forEach items="${dataList }" var="comment" varStatus="status">
 				<c:choose>
 							<c:when test="${ comment.annotationed}">
 				<tr>
 					<td>${comment.courseType}</td>
 					<td>${comment.course}</td>
 					<td>${comment.content}</td>
 					<td>${its:content(comment.contentRelated)}</td>
 					<td>${its:emotion(comment.emotionRelated)}</td>
 					<td>${its:other(comment.otherRelated)}</td>
 					<td><a href="/corpus/commentdetail/${comment.id}">标注情况</a></td>
 					<td>
 						<c:choose>
 							<c:when test="${ comment.annotationed}">成功</c:when>
 							<c:otherwise>未成功</c:otherwise>
 						</c:choose>
 					</td>
 				</tr>
 			</c:when>
 			<c:otherwise>
 			<tr style="background-color: red">
 					<td>${comment.courseType}</td>
 					<td>${comment.course}</td>
 					<td>${comment.content}</td>
 					<td>${its:content(comment.contentRelated)}</td>
 					<td>${its:emotion(comment.emotionRelated)}</td>
 					<td>${its:other(comment.otherRelated)}</td>
 					<td><a href="/corpus/commentdetail/${comment.id}">标注情况</a></td>
 					<td>
 						<c:choose>
 							<c:when test="${ comment.annotationed}">成功</c:when>
 							<c:otherwise>未成功</c:otherwise>
 						</c:choose>
 					</td>
 				</tr>
 			</c:otherwise>
 			</c:choose>
 			</c:forEach>
 		</tr>
		</table>
		<div style="float: right;margin-right: 50px;" id="page" class="page"></div>
 	</div>
 	  
 	<script type="text/javascript" src="${ctx }/static/lib/layerui/2.2.5/layui.js"></script>
<script type="text/javascript" src="${ctx }/static/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
layui.use(['laypage', 'layer'], function(){
  var laypage = layui.laypage
  ,layer = layui.layer;
 	laypage.render({
    elem: 'page'
    ,count: ${total}
    ,curr :${pageIndex}
    ,layout: ['count', 'prev', 'page', 'next',  'refresh', 'skip']
    , jump:function(obj,first){
                    if(!first) {
    　　　　　　　　　　　　//***第一次不执行,一定要记住,这个必须有,要不然就是死循环,哈哈
                        var pageIndex = obj.curr;
                      self.location='/corpus/list/${userId}?pageIndex='+pageIndex; 
                    }
                }
  });
  });
</script>
 	
	
</body>
</html>
