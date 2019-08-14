package edu.gzhu.its.qa.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import edu.gzhu.its.qa.model.Entity;
import edu.gzhu.its.qa.model.QaIntent;

public class QaUtils {

	public static QaIntent getResult(String question) throws ClientProtocolException, IOException {
		String url = "http://139.159.191.123:5000/parse";
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();
		String respContent = null;

		// json方式
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("q", question);
		jsonParam.put("project", "");
		jsonParam.put("model", "bot");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		System.out.println();

		HttpResponse resp = client.execute(httpPost);

		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity he = resp.getEntity();
			respContent = EntityUtils.toString(he, "UTF-8");
		}
		JSONObject object = JSONObject.parseObject(respContent);

		QaIntent qaIntent = new QaIntent();

		// 意图
		JSONObject intent = JSONObject.parseObject(object.get("intent").toString());
		System.out.println("intent=" + intent.get("name"));
		qaIntent.setIntent(intent.get("name").toString());

		// 槽
		JSONArray entities = JSONObject.parseArray(object.get("entities").toString());
		List<Entity> entities1 = new ArrayList<>();

		for (Iterator iterator = entities.iterator(); iterator.hasNext();) {
			JSONObject object2 = (JSONObject) iterator.next();
			Entity entity2 = new Entity();

			System.out.println(object2.get("entity"));

			entity2.setEntity(object2.get("entity").toString());

			System.out.println(object2.get("value"));
			entity2.setValue(object2.get("value").toString());
			entities1.add(entity2);
		}
		qaIntent.setEntities(entities1);
		
		return qaIntent;

	}
}
