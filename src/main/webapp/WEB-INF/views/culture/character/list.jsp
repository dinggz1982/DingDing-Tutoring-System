<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汉字列表</title>
<link rel="stylesheet" href="/layui-v2.4.5/layui/css/layui.css">
</head>
<body>
	<div class="layui-container">
		<div class="layui-row">
			<h1>汉字列表</h1>
			<div class="layui-col-lg12">
				<table class="layui-table">
					<tr>
						<th>id</th>
						<th>汉字</th>
						<th>解释</th>
						<th>详细解释</th>
						<th>偏旁</th>
						<th>笔画</th>
					</tr>
					<c:forEach items="${pageModel.list }" var="character">
						<tr>
							<td width="5%">${character.id }</td>
							<td width="5%">
							<a href="/character/view/${character.id }">${character.hanzi }</a>
							</td>
							<td>${character.analysis }</td>
							<td>${character.detailExplain }</td>
							<td>${character.radical }</td>
							<td>${character.strokeNum }</td>
						</tr>
					</c:forEach>
				</table>
				<div id="page"></div>
			</div>
		</div>
	</div>
</body>
<script src="/layui-v2.4.5/layui/layui.js"></script>
<script>
layui.use('laypage', function(){
  var laypage = layui.laypage;
  
  //执行一个laypage实例
  laypage.render({
    elem: 'page' //注意，这里的 test1 是 ID，不用加 # 号
    ,count: ${pageModel.total} //数据总数，从服务端得到
    ,limit:${pageModel.size}
    ,limits:[10, 20, 30, 40, 50,100]
    ,curr:${pageModel.page}
    ,groups:10
    ,prev:'上一页'
    ,next:'下一页'
    ,first:'首页'
    ,last:'最后一页'
    ,layout:['first','prev', 'page', 'next','last','limit','refresh','skip']
  	,jump: function(obj, first){
  	    //obj包含了当前分页的所有参数，比如：
  	    console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
  	    console.log(obj.limit); //得到每页显示的条数
  	    
  	    //首次不执行
  	    if(!first){
  	      window.location.href='/character/list?page='+obj.curr+"&size="+obj.limit;
  	    }
  	  }
  });
});
</script>
</html>