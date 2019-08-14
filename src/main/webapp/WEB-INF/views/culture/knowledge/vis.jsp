<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>知识关系可视化</title>
<link rel="stylesheet" href="/vis/vis-network.min.css">
<link rel="stylesheet" href="/layui-v2.4.5/layui/css/layui.css">
<style type="text/css">
#mynetwork {
	width: 100%;
	height: 1000px;
	border: 1px solid lightgray;
}
</style>
</head>
<body>
	<!-- 可视化 -->
	<div id="mynetwork"></div>

</body>
<script type="text/javascript" src="/vis/vis.js"></script>
<script src="/layui-v2.4.5/layui/layui.js"></script>
<script type="text/javascript">
layui.use('layer', function(){ //独立版的layer无需执行这一句
 var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
  // create an array with nodes
  var nodes = new vis.DataSet([${nodes}]);

  // create an array with edges
  var edges = new vis.DataSet([${edges}]);

  // create a network
  var container = document.getElementById('mynetwork');
  var data = {
    nodes: nodes,
    edges: edges
  };
  var options = {};
 
  var network = new vis.Network(container, data, options);
  //单击事件
 	network.on("selectNode", function (params) {
          var clickedNode =this.body.nodes[params.nodes];
         // console.log('查询的node:', clickedNode.options);
         layer.open({
           type: 2, 
           area:["800px","600px"],
           content: '/knowledge/addNode/'+clickedNode.options.id //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
         });
      });
  //弹出窗口
	
	
});	
</script>
</html>