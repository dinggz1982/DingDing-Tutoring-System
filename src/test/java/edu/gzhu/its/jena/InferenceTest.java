package edu.gzhu.its.jena;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.gzhu.its.qa.service.ITDBReadService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InferenceTest {

	@Autowired
	private ITDBReadService tdbReadService;

	@Test
	public void test() {
		Map<String, String> map = this.tdbReadService.getType("<http://lcell.bnu.edu.cn/ontologies/chinese/poetry#李白>");
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		}

	}

}
