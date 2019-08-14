package edu.gzhu.its.qa.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import edu.gzhu.its.base.model.PageData;
import edu.gzhu.its.qa.entity.Entity;
import edu.gzhu.its.qa.entity.Intent;
import edu.gzhu.its.qa.service.IEntityService;
import edu.gzhu.its.qa.service.IIntentService;
import edu.gzhu.its.qa.utils.LinuxCommandUtil;

/**
 * 服务启动与训练
 * 
 * @author dingguozhu
 *
 */
@Controller
public class RasaController {

	@Resource
	private IEntityService entityService;
	@Resource
	private IIntentService intentService;

	/**
	 * 设置意图
	 * 
	 * @return
	 */
	@GetMapping("/qa/setting")
	public String setting(Integer pageIndex, Integer pageSize, Model model) {
		pageIndex = pageIndex == null ? 1 : pageIndex < 1 ? 1 : pageIndex;
		pageSize = 10;
		PageData<Intent> pageData = this.intentService.getPageData(pageIndex, pageSize, "");
		model.addAttribute("dataList", pageData.getPageData());
		model.addAttribute("total", pageData.getTotalCount());
		model.addAttribute("pages", pageData.getTotalPage());
		model.addAttribute("pagesize", pageData.getPageSize());
		model.addAttribute("pageIndex", pageIndex);
		return "qa/setting";
	}

	/**
	 * 保存用户的意图
	 * 
	 * @return
	 */
	@PostMapping("/qa/saveIntent")
	@ResponseBody
	public String saveIntent(Intent intent, String[] value, String[] entity) {
		if (entity != null && value != null && value.length == entity.length) {
			Set<Entity> entities = new HashSet<Entity>();
			for (int i = 0; i < entity.length; i++) {
				Entity newEntity = new Entity();
				newEntity.setEntity(entity[i]);
				newEntity.setValue(value[i]);
				int start = intent.getText().indexOf(value[i]);
				newEntity.setStart(start);
				newEntity.setEnd(start+value[i].length());
				this.entityService.save(newEntity);
				entities.add(newEntity);
			}
			intent.setEntities(entities);
		}
		this.intentService.save(intent);
		try {
			//1.生成json
			createJson();
			//2.训练和重启服务
			
			String cd = "sh";
			String trainAndRestartServer = "/data/nlp/trainAndRestartServer.sh";
			String[] cmds = { cd, trainAndRestartServer };
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				// 执行训练
				String info = LinuxCommandUtil.run(cmds);
				map.put("status", "success");
				map.put("info", info);
			} catch (IOException e) {
				e.printStackTrace();
				map.put("status", "failed");
				map.put("info", e.toString());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "qa/restart";
	}

	@GetMapping("/qa/train")
	@ResponseBody
	public Map<String, Object> train() {
		String cd = "sh";
		String train = "/data/nlp/train.sh";
		String[] cmds = { cd, train };
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 执行训练
			String info = LinuxCommandUtil.run(cmds);
			map.put("status", "success");
			map.put("info", info);
		} catch (IOException e) {
			e.printStackTrace();
			map.put("status", "failed");
			map.put("info", e.toString());
		}
		return map;
	}

	@GetMapping("/qa/server")
	@ResponseBody
	public Map<String, Object> server() {
		String cd = "sh";
		String train = "/data/nlp/server.sh";
		String[] cmds = { cd, train };
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 开启服务
			String info = LinuxCommandUtil.run(cmds);
			map.put("status", "success");
			map.put("info", info);
		} catch (IOException e) {
			e.printStackTrace();
			map.put("status", "failed");
			map.put("info", e.toString());
		}
		return map;
	}

	public void createJson() throws SQLException {
		List<Intent> intents = this.intentService.findAll();
		JSONArray intentArray = new JSONArray();
		for (Iterator iterator = intents.iterator(); iterator.hasNext();) {
			Intent intent = (Intent) iterator.next();
			JSONObject intentObject = new JSONObject();
			Set<Entity> entities = intent.getEntities();
			intentObject.put("text", intent.getText());
			intentObject.put("intent", intent.getIntent());
			JSONArray entityArray = new JSONArray();
			if (entities != null && entities.size() > 0) {
				for (Iterator iterator2 = entities.iterator(); iterator2.hasNext();) {
					Entity entity = (Entity) iterator2.next();
					JSONObject entityObject = new JSONObject();
					entityObject.put("start", entity.getStart());
					entityObject.put("end", entity.getEnd());
					entityObject.put("value", entity.getValue());
					entityObject.put("entity", entity.getEntity());
					entityArray.add(entityObject);
				}
			}
			intentObject.put("entities", entityArray);
			intentArray.add(intentObject);
		}
		JSONObject common_examples = new JSONObject();
		common_examples.put("common_examples", intentArray);

		JSONObject rasa_nlu_data = new JSONObject();
		rasa_nlu_data.put("rasa_nlu_data", common_examples);

		String jsonFile ="/data/nlp/rasa_nlu/rasa_nlu_chi/data/examples/rasa/hongrui.json";
		
		File file = new File(jsonFile);
		if(file.exists()){
			file.delete();
		}
		BufferedWriter writer = null;
			try {
				file.createNewFile();
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(jsonFile, true), "UTF-8"));
				String writeString = JSON.toJSONString(rasa_nlu_data, SerializerFeature.PrettyFormat);
				writer.write(writeString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

}
