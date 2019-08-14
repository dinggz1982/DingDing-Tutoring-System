<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增节点</title>
<link rel="stylesheet" href="/layui-v2.4.5/layui/css/layui.css">
</head>
<body>
<div style="width: 50%">
<form class="layui-form" action="/people/saveNode">
  <div class="layui-form-item">
    <label class="layui-form-label">人物</label>
    <div class="layui-input-block">
      <input type="text" name="peopleA"  placeholder="${people.name }" autocomplete="off" class="layui-input">
      <input type="hidden" name="peopleAId"  value="${people.id }" autocomplete="off" class="layui-input">
      
    </div>
  </div>
   <div class="layui-form-item">
    <label class="layui-form-label">关系</label>
    <div class="layui-input-block">
      <input type="text" name="relation"  placeholder="人物关系" autocomplete="off" class="layui-input">
    </div>
  </div>
 <div class="layui-form-item">
    <label class="layui-form-label">关联人物</label>
    <div class="layui-input-block">
      <input type="text" name="peopleB"  placeholder="关联人物" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
</form>
</div>
</body>
</html>