package edu.gzhu.its.jena;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import edu.gzhu.its.qa.model.SparqlPrefix;

public class TDBRead {

	public static String ns = "http://lcell.bnu.edu.cn/ontologies/chinese/";

	public static void main(String[] args) {

		String filePath = "E:\\Intelligent Tutoring System\\Intelligent-Tutoring-System\\src\\main\\resources\\tdb";

		Dataset dataset = TDBFactory.createDataset(filePath);

		// dataset.begin(ReadWrite.READ);

		Model model = dataset.getDefaultModel();
		
	//	String sparql = "SELECT ?o WHERE { <http://www.gzhu.edu.cn/culture/一> <http://www.gzhu.edu.cn/culture/笔画> ?o }";
		/*String sparql  ="SELECT ?s ?p ?o WHERE { ?s ?p ?o }";
				
		ResultSet resultSet = selectQuery(model,sparql);
		while(resultSet.hasNext()) {
			QuerySolution querySolution =  resultSet.next();
			System.out.println(querySolution.get("?s")+"   " +querySolution.get("?o")+"   " + querySolution.get("?o"));
			//System.out.println(querySolution);
		}*/
		String queryString ="李白";
		String sparqlQueryString2 =  "SELECT ?s ?p ?o WHERE { <http://lcell.bnu.edu.cn/ontologies/chinese/poetry#李白> ?p ?o }";
		Query query = QueryFactory.create(sparqlQueryString2);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		ResultSet results = qexec.execSelect();
		String jsonFile ="e:/tdb.txt";
		
		StringBuffer buffer = new StringBuffer();
		for (; results.hasNext();) {
			QuerySolution querySolution = results.nextSolution();
			buffer.append(querySolution.get("?s")+"   " +querySolution.get("?p")+"   " + querySolution.get("?o")+"\n");
		}
		
		File file = new File(jsonFile);
		if(file.exists()){
			file.delete();
		}
		
		
		BufferedWriter writer = null;
			try {
				file.createNewFile();
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(jsonFile, true), "UTF-8"));
				writer.write(buffer.toString());
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
			dataset.close();
		

		/*SELECT ?o

				WHERE

				{ '偏' '偏旁' ?o .

				}*/
		
		
	}

	/**
	 * 根据sparql返回查询结果
	 * @param model
	 * @param queryString
	 * @return
	 */
	public static ResultSet selectQuery(Model model, String queryString) {
		QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
		ResultSet resultSet = qexec.execSelect();
		return resultSet;
	}
	
	/**
	 * 删除url中的尖括号
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-26 下午7:14:08
	 */
	public static String deleteURIBracket(String url) {
		int last = url.lastIndexOf(">");
		return url.substring(1, last).trim();
	}

}
