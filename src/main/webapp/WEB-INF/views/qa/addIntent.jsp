<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>新增意图</title>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<%@include file="/WEB-INF/views/include/top.jsp"%>
</head>
<body>
	<nav class="breadcrumb"> <i class="Hui-iconfont">&#xe67f;</i> 首页
	<span class="c-gray en">&gt;</span> 新增意图<span class="c-gray en">&gt;</span>
	<a class="btn btn-success radius r"
		style="line-height: 1.6em; margin-top: 3px"
		href="javascript:location.replace(location.href);" title="刷新"><i
		class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="pd-20 col-xs-10 col-md-offset-1"></div>

	<div class="container ui-sortable">
		<h1>新增意图（新增意图后要重新训练模型，大概需要1~5分钟）</h1>
		<div class="panel panel-default">
			<div class="panel-header"></div>
			<div class="panel-body">
				<form method="post" id="intentForm"
					class="form form-horizontal responsive">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />

					<div class="row cl">
						<label class="form-label col-xs-3">意图：</label>
						<div class="formControls col-xs-8">
							<input type="text" class="input-text" required placeholder="用英文描述语句表达的意图"
								name="intent" id="intent" aria-required="true">
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-3">自然语言：</label>
						<div class="formControls col-xs-8">
							<input type="text" class="input-text" required placeholder="自然语言表达"
								name="text" id="text" aria-required="true">
						</div>
					</div>
					<div class="row cl">
							<label class="form-label col-xs-3">槽位填充：</label>
							<div class="formControls col-xs-8">
						<table class="table table-border table-bordered radius" id="tagTable">
							<thead>
								<tr>
									<th width="20%">value</th>
									<th width="20%">entity</th>
									<th width="20%"><input id="addTag" class="btn btn-success radius" type="button" value="新增"></th>
								</tr>
							</thead>
						</table>
						</div>
					</div>
					<div class="row cl">
						<div class="col-xs-8 col-xs-offset-3">
							<input class="btn btn-primary" type="button"
								value="&nbsp;&nbsp;提交&nbsp;&nbsp;" onclick="submitIntnet()">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="/validation/jquery.validate.min.js"></script>
<script type="text/javascript" src="/validation/validate-methods.js"></script>
<script type="text/javascript" src="/validation/messages_zh.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){ 
	$("#addTag").click(function(){
		var tr="<tr><td><input aria-required='true' class='input-text'  type='text' name='value' required placeholder='自然语言中的实体'/></td><td><input aria-required='true' class='input-text'	 type='text' name='entity' required placeholder='实体对应的类型,用英文表示'/></td>"+
       "<td><input onclick='delThis(this)' class='btn btn-danger radius' type='button' value='删除'></td></tr>";
		$("#tagTable").append(tr);
	});
}); 
//删除一行
function delThis(obj){
	var tr = obj.parentNode.parentNode;
	console.log(tr);
    tr.parentNode.removeChild(tr);
}
//ajax形式提交
function submitIntnet(){
	

			//保存意图
			var header = $("meta[name='_csrf_header']").attr("content");
			var token =$("meta[name='_csrf']").attr("content");
			  $.ajax({
		          type: "POST",
		          url: "/qa/saveIntent",
		          dataType: "json",
		          data: $('#intentForm').serialize(),
		          beforeSend : function(xhr) {
		              xhr.setRequestHeader(header, token);
		          },success: function (data) {
              if (data.status == "success") {
                  alert("保存成功！");
              }
          }
		      });
}


</script>
</html>
