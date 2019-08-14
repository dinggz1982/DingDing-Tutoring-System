<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>中华优秀传统文化可视化</title>
<link rel="stylesheet" href="/vis/vis-network.min.css">
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
<script type="text/javascript">
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
 /*  network.on("click", function (params) {
      console.log('返回的节点信息: ' + JSON.stringify(params.pointer.DOM));
  }); */
      network.on("selectNode", function (params) {
          var clickedNode =this.body.nodes[params.nodes];
          console.log('查询的type:', clickedNode.options.type);
          /* console.log('查询的知识点点:', ${knowledge}); */
          window.location.href ="/knowledgeView/${knowledge}/"+clickedNode.options.type;
      });
</script>

</html>