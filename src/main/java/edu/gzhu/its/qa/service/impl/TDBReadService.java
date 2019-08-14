package edu.gzhu.its.qa.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.OWL;
import org.springframework.stereotype.Service;

import edu.gzhu.its.base.model.PageData;
import edu.gzhu.its.base.util.LCPROPERTY;
import edu.gzhu.its.base.util.WebDirUtil;
import edu.gzhu.its.qa.model.InstanceData;
import edu.gzhu.its.qa.model.LcStatement;
import edu.gzhu.its.qa.model.SparqlPrefix;
import edu.gzhu.its.qa.model.TypeData;
import edu.gzhu.its.qa.service.ITDBReadService;

/**
 * 本体术语读取层实现
 * 
 * @author : 丁国柱
 * @date : 2014-11-6 下午1:01:07
 */
@Service("tdbReadService")
public class TDBReadService implements ITDBReadService {

	private static Dataset dataset;

	private static final ThreadLocal<Dataset> threadLocal = new ThreadLocal<Dataset>();

	public static Dataset getDataset() {
		dataset = threadLocal.get();
		if (dataset == null || !dataset.isInTransaction()) {
			if (dataset == null) {
				rebuildDataset();
			}
			threadLocal.set(dataset);
		}
		return dataset;
	}


	public static void rebuildDataset() {
		try {
			String filePath = "E:\\Intelligent Tutoring System\\Intelligent-Tutoring-System\\src\\main\\resources\\tdb";
			dataset = TDBFactory.createDataset(filePath);
			dataset.begin(ReadWrite.READ);
		} catch (Exception e) {
			System.err
					.println("%%%% Error Creating JENA TDB DATASET---dinggz1982@gmail.com %%%%");
			e.printStackTrace();
		}
	}

	public static void closeDataset() {
		if (dataset != null) {
			// dataset.commit();
			// dataset.close();
			dataset.end();
			dataset = null;
		}
		threadLocal.set(null);
	}

	@Override
	public int queryDataCount(String sparql) {
		getDataset();// 查询模式，为false
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		int count = results.next().getLiteral("?count").getInt();
		qexec.close();
		closeDataset();// 关闭dataset
		return count;
	}

	@Override
	public List<LcStatement> queryPageList(String sparql, int start,
			int maxSize, String orderBy) {
		Query contentQuery = null;
		QueryExecution qexecQuery = null;
		ResultSet resultsContent = null;
		getDataset();
		sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
				+ " LIMIT " + maxSize;
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		// List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<LcStatement> list = new ArrayList<LcStatement>();
		QuerySolution querySolution;
		LcStatement statement;
		for (; results.hasNext();) {
			querySolution = results.next();
			statement = new LcStatement();
			// querySolution.
			if (querySolution.get("?p") != null) {
				statement.setPredicate(getIRIName(querySolution.get("?p")
						.toString()));
			}
			if (querySolution.get("?o") != null) {
				if (querySolution.get("?o").canAs(Resource.class)) {
					String object = querySolution.get("?o").toString();
					String object2 = "";
					try {
						object2 = URLEncoder.encode(object, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statement.setObject("<a href='/lcontology/semantic/baseInfo/?iri="
							+ object2 + "'>" + getIRIName(object) + "</a>");

					// 增加内容
					contentQuery = QueryFactory.create("select  ?content {{ <"
							+ object + "> <" + LCPROPERTY.content
							+ "> ?content} UNION { <" + object + "> <"
							+ LCPROPERTY.description + "> ?content}}");
					qexecQuery = QueryExecutionFactory.create(contentQuery,
							dataset);
					resultsContent = qexecQuery.execSelect();
					if (resultsContent.hasNext()) {
						statement.setHasContent(1);
						statement.setContent(resultsContent.next()
								.get("?content").toString());
					}
				} else {
					statement.setObject(querySolution.get("?o").toString());
				}
			}

			if (querySolution.get("?s") != null) {
				if (querySolution.get("?s").canAs(Resource.class)) {
					String subject = querySolution.get("?s").toString();
					String subject2 = "";
					try {
						subject2 = URLEncoder.encode(subject, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statement.setSubject("<a href='/lcontology/semantic/baseInfo/?iri="
							+ subject2 + "'>" + getIRIName(subject) + "</a>");
				}
			}

			list.add(statement);

		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	@Override
	public List<LcStatement> queryInstancesPageList(String sparql, int start,
			int maxSize, String orderBy) {
		Query contentQuery = null;
		QueryExecution qexecQuery = null;
		ResultSet resultsContent = null;
		getDataset();
		sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
				+ " LIMIT " + maxSize;
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		// List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<LcStatement> list = new ArrayList<LcStatement>();
		QuerySolution querySolution;
		LcStatement statement;
		for (; results.hasNext();) {
			querySolution = results.next();
			statement = new LcStatement();
			if (querySolution.get("?i").canAs(Resource.class)) {
				String object = querySolution.get("?i").toString();
				String object2 = "";
				try {
					object2 = URLEncoder.encode(object, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement.setObject("<a href='/lcontology/semantic/baseInfo/?iri="
						+ object2 + "'>" + getIRIName(object) + "</a>");
				statement.setIri(object2);
				// 下载信息
				statement.setDownload("<a href='/lcontology/semantic/download?iri="
						+ object2 + "'>下载</a>");

				// 增加内容
				contentQuery = QueryFactory.create("select  ?content {{ <"
						+ object + "> <" + LCPROPERTY.content
						+ "> ?content} UNION { <" + object + "> <"
						+ LCPROPERTY.description + "> ?content}}");
				qexecQuery = QueryExecutionFactory
						.create(contentQuery, dataset);
				resultsContent = qexecQuery.execSelect();
				if (resultsContent.hasNext()) {
					statement.setHasContent(1);
					statement.setContent(resultsContent.next().get("?content")
							.toString());
				}
			} else {
				statement.setObject(querySolution.get("?i").toString());
			}
			if (querySolution.get("?x").canAs(Resource.class)) {
				String url = querySolution.get("?x").toString();
				String url2 = "";
				try {
					url2 = URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement
						.setSubject("<a href='/lcontology/evolution/getInstanceByclass?iri="
								+ url2 + "'>" + getIRIName(url) + "</a>");
			} else {
				statement.setSubject(querySolution.get("?x").toString());
			}
			list.add(statement);
		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	@Override
	public PageData<LcStatement> getPageData(String sparqlCount, String sparql,
			int pageIndex, int pageSize, String orderBy) {
		// TODO Auto-generated method stub
		PageData<LcStatement> pageData = new PageData<LcStatement>(pageIndex,
				pageSize);

		int totalCount = this.queryDataCount(sparqlCount);

		List<LcStatement> list = this.queryPageList(sparql,
				pageData.getStartRow(), pageSize, orderBy);
		pageData.setTotalCount(totalCount);

		pageData.setPageData(list);
		closeDataset();// 关闭dataset
		return pageData;
	}

	/**
	 * 根据iri截取name
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-8 下午9:04:52
	 */
	public String getIRIName(String iri) {
		return iri.substring(iri.lastIndexOf("#") + 1, iri.length());
	}

	/**
	 * 将课程url转成课程id
	 * 
	 * @Title: TDBReadService.java
	 * @Package bnu.edu.lcell.services.semantic
	 * @Description: TODO
	 * @author 丁国柱 dinggz1982@gmail.com
	 * @date 2014-10-28 下午1:08:34
	 * @version V1.0
	 */
	public int getCourseId(String source) {
		source = source.substring(source.lastIndexOf("/") + 1, source.length());
		return Integer.parseInt(source);
	}

	@Override
	public List<LcStatement> queryTypePageList(String sparql, int maxSize) {
		List<LcStatement> list = null;
		try {
			getDataset();
			sparql = sparql + " LIMIT " + maxSize;
			Query query = QueryFactory.create(sparql);
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			// 获取结果
			ResultSet results = qexec.execSelect();
			// List<QuerySolution> list = ResultSetFormatter.toList(results);
			list = new ArrayList<LcStatement>();
			QuerySolution querySolution;
			LcStatement statement;
			for (; results.hasNext();) {
				querySolution = results.next();
				statement = new LcStatement();
				if (querySolution.get("?s").canAs(Resource.class)) {
					String url = querySolution.get("?s").toString();
					String url2 = "";
					try {
						url2 = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statement.setSubject("<a href='/lcontology/semantic/baseInfo/?iri="
							+ url2 + "'>" + getIRIName(url) + "</a>");

				} else {
					statement.setSubject(getIRIName(querySolution.get("?s")
							.toString()));
				}
				list.add(statement);
			}
			qexec.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return list;
	}

	@Override
	public PageData<LcStatement> getInstancesPageData(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy) {
		PageData<LcStatement> pageData = null;
		try {
			pageData = new PageData<LcStatement>(pageIndex, pageSize);

			int totalCount = this.queryDataCount(sparqlCount);

			List<LcStatement> list = this.queryInstancesPageList(sparql,
					pageData.getStartRow(), pageSize, orderBy);

			pageData.setTotalCount(totalCount);

			pageData.setPageData(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return pageData;
	}

	@Override
	public void closeTDB() {
		try {
			if (threadLocal.get() != null) {
				dataset.end();
				dataset = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			threadLocal.set(null);
		}

	}

	@Override
	public Model getJenaDefaultModel() {
		getDataset();
		return dataset.getDefaultModel();
	}

	@Override
	public ResultSet getResultSet(String queryString) {
		getDataset();
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		return qexec.execSelect();

	}

	@Override
	public Map<String, String> getType(String queryString) {
		getDataset();
		String sparqlQueryString2 = SparqlPrefix.prefix + "select * { "
				+ queryString + " a  ?o}";
		String nodeStr;
		Map<String, String> map = new HashMap<String, String>();
		ResultSet results = this.getResultSet(sparqlQueryString2);
		for (; results.hasNext();) {
			QuerySolution soln = results.nextSolution();
			if (soln.get("?o") != null) {
				Node node2 = soln.get("?o").asNode();
				if (!node2.toString().equals(OWL.Class.toString())) {
					nodeStr = node2.toString();
					// 获取查询语句的类别
					if (nodeStr.length() > 0 && nodeStr.indexOf("#") != -1) {
						map.put(nodeStr, deleteURIBracket(queryString));
					}
				}
			}
		}
		return map;
	}

	@Override
	public String getDescription(String queryString) {
		String decription = null;
		try {
			getDataset();
			String sparqlQueryString = SparqlPrefix.prefix + "select ?o { "
					+ queryString
					+ " <http://lcell.bnu.edu.cn/owl/property#description>  ?o}";
			ResultSet results = this.getResultSet(sparqlQueryString);
			if (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				if (soln.get("?o") != null) {
					decription = soln.get("?o").toString();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return decription;
	}

	@Override
	public PageData<LcStatement> getTriples(String sparqlCount, String sparql,
			int pageIndex, int pageSize, String orderBy) {
		PageData<LcStatement> pageData = null;
		try {
			pageData = new PageData<LcStatement>(pageIndex, pageSize);

			int totalCount = this.queryDataCount(sparqlCount);

			List<LcStatement> list = this.getStatementPageList(sparql,
					pageData.getStartRow(), pageSize, orderBy);

			pageData.setTotalCount(totalCount);

			pageData.setPageData(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return pageData;
	}

	@Override
	public List<LcStatement> getStatementPageList(String sparql, int start,
			int maxSize, String orderBy) {
		getDataset();
		sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
				+ " LIMIT " + maxSize;
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		// List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<LcStatement> list = new ArrayList<LcStatement>();
		QuerySolution querySolution;
		LcStatement statement;
		for (; results.hasNext();) {
			querySolution = results.next();
			statement = new LcStatement();

			// 获取subject
			// 如果主语是一个资源
			if (querySolution.get("?s").canAs(Resource.class)) {
				String subject = querySolution.get("?s").toString();
				String subject2 = "";
				try {
					subject2 = URLEncoder.encode(subject, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement.setSubject("<a href='/lcontology/catalogs/node/?iri=" + subject2
						+ "'>" + getIRIName(subject) + "</a>");
			} else {
				statement.setSubject(querySolution.get("?s").toString());
			}
			// 处理谓语
			if (querySolution.get("?p").canAs(Resource.class)) {
				String predicate = querySolution.get("?p").toString();
				String predicate2 = "";
				try {
					predicate2 = URLEncoder.encode(predicate, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement.setPredicate("<a href='/lcontology/catalogs/node/?iri="
						+ predicate2 + "'>" + getIRIName(predicate) + "</a>");
			} else {
				statement.setPredicate(querySolution.get("?p").toString());
			}
			// 处理宾语
			statement.setPredicate(getIRIName(querySolution.get("?p")
					.toString()));
			if (querySolution.get("?o").canAs(Resource.class)) {
				String object = querySolution.get("?o").toString();
				String object2 = "";
				try {
					object2 = URLEncoder.encode(object, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement.setObject("<a href='/lcontology/catalogs/node/?iri=" + object2
						+ "'>" + getIRIName(object) + "</a>");

			} else {
				statement.setObject(querySolution.get("?o").toString());
			}
			list.add(statement);

		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	@Override
	public PageData<LcStatement> getNodeAsSubject(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy) {
		PageData<LcStatement> pageData = null;
		try {
			pageData = new PageData<LcStatement>(pageIndex, pageSize);

			int totalCount = this.queryDataCount(sparqlCount);

			List<LcStatement> list = this.getStatementPageListByNodeAsSubjct(
					sparql, pageData.getStartRow(), pageSize, orderBy);

			pageData.setTotalCount(totalCount);

			pageData.setPageData(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return pageData;
	}

	@Override
	public PageData<LcStatement> getNodeAsPredicate(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy) {
		PageData<LcStatement> pageData = null;
		try {
			pageData = new PageData<LcStatement>(pageIndex, pageSize);

			int totalCount = this.queryDataCount(sparqlCount);

			List<LcStatement> list = this
					.getStatementPageListByNodeAsPredicate(sparql,
							pageData.getStartRow(), pageSize, orderBy);

			pageData.setTotalCount(totalCount);

			pageData.setPageData(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return pageData;
	}

	@Override
	public PageData<LcStatement> getNodeAsObject(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy) {
		PageData<LcStatement> pageData = null;
		try {
			pageData = new PageData<LcStatement>(pageIndex, pageSize);

			int totalCount = this.queryDataCount(sparqlCount);

			List<LcStatement> list = this.getStatementPageListByNodeAsObject(
					sparql, pageData.getStartRow(), pageSize, orderBy);

			pageData.setTotalCount(totalCount);

			pageData.setPageData(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return pageData;
	}

	@Override
	public List<LcStatement> getStatementPageListByNodeAsSubjct(String sparql,
			int start, int maxSize, String orderBy) {
		getDataset();
		sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
				+ " LIMIT " + maxSize;
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		// List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<LcStatement> list = new ArrayList<LcStatement>();
		QuerySolution querySolution;
		LcStatement statement;
		for (; results.hasNext();) {
			querySolution = results.next();
			statement = new LcStatement();
			// 处理谓语
			if (querySolution.get("?p").canAs(Resource.class)) {
				String predicate = querySolution.get("?p").toString();
				String predicate2 = "";
				try {
					predicate2 = URLEncoder.encode(predicate, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement
						.setPredicate("<a target='_blank' href='/lcontology/catalogs/node/?iri="
								+ predicate2
								+ "'>"
								+ getIRIName(predicate)
								+ "</a>");
			} else {
				statement.setPredicate(querySolution.get("?p").toString());
			}
			// 处理宾语
			statement.setPredicate(getIRIName(querySolution.get("?p")
					.toString()));
			if (querySolution.get("?o").canAs(Resource.class)) {
				String object = querySolution.get("?o").toString();
				String object2 = "";
				try {
					object2 = URLEncoder.encode(object, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement
						.setObject("<a target='_blank' href='/lcontology/catalogs/node/?iri="
								+ object2 + "'>" + getIRIName(object) + "</a>");

			} else {
				statement.setObject(querySolution.get("?o").toString());
			}
			list.add(statement);

		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	@Override
	public List<LcStatement> getStatementPageListByNodeAsPredicate(
			String sparql, int start, int maxSize, String orderBy) {
		getDataset();
		sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
				+ " LIMIT " + maxSize;
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		// List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<LcStatement> list = new ArrayList<LcStatement>();
		QuerySolution querySolution;
		LcStatement statement;
		for (; results.hasNext();) {
			querySolution = results.next();
			statement = new LcStatement();

			// 获取subject
			// 如果主语是一个资源
			if (querySolution.get("?s").canAs(Resource.class)) {
				String subject = querySolution.get("?s").toString();
				String subject2 = "";
				try {
					subject2 = URLEncoder.encode(subject, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement
						.setSubject("<a target='_blank' href='/lcontology/catalogs/node/?iri="
								+ subject2
								+ "'>"
								+ getIRIName(subject)
								+ "</a>");
			} else {
				statement.setSubject(querySolution.get("?s").toString());
			}
			// 处理宾语
			if (querySolution.get("?o").canAs(Resource.class)) {
				String object = querySolution.get("?o").toString();
				String object2 = "";
				try {
					object2 = URLEncoder.encode(object, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement
						.setObject("<a target='_blank' href='/lcontology/catalogs/node/?iri="
								+ object2 + "'>" + getIRIName(object) + "</a>");

			} else {
				statement.setObject(querySolution.get("?o").toString());
			}
			list.add(statement);

		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	@Override
	public List<LcStatement> getStatementPageListByNodeAsObject(String sparql,
			int start, int maxSize, String orderBy) {
		getDataset();
		sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
				+ " LIMIT " + maxSize;
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		// List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<LcStatement> list = new ArrayList<LcStatement>();
		QuerySolution querySolution;
		LcStatement statement;
		for (; results.hasNext();) {
			querySolution = results.next();
			statement = new LcStatement();

			// 获取subject
			// 如果主语是一个资源
			if (querySolution.get("?s").canAs(Resource.class)) {
				String subject = querySolution.get("?s").toString();
				String subject2 = "";
				try {
					subject2 = URLEncoder.encode(subject, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement
						.setSubject("<a target='_blank' href='/lcontology/catalogs/node/?iri="
								+ subject2
								+ "'>"
								+ getIRIName(subject)
								+ "</a>");
			} else {
				statement.setSubject(querySolution.get("?s").toString());
			}
			// 处理谓语
			if (querySolution.get("?p").canAs(Resource.class)) {
				String predicate = querySolution.get("?p").toString();
				String predicate2 = "";
				try {
					predicate2 = URLEncoder.encode(predicate, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement
						.setPredicate("<a target='_blank' href='/lcontology/catalogs/node/?iri="
								+ predicate2
								+ "'>"
								+ getIRIName(predicate)
								+ "</a>");
			} else {
				statement.setPredicate(querySolution.get("?p").toString());
			}
			list.add(statement);
		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	@Override
	public int queryAllCount() {
		getDataset();// 查询模式，为false
		Query query = QueryFactory
				.create("select (COUNT(*) AS ?count) {?s ?p ?o}");
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();

		int count = results.next().getLiteral("?count").getInt();
		qexec.close();
		closeDataset();// 关闭dataset
		return count;
	}

	@Override
	public String getLinkedInfo(String queryString) {
		String decription = null;
		try {
			getDataset();
			String sparqlQueryString = SparqlPrefix.prefix + "select ?o { "
					+ queryString
					+ " <http://lcell.bnu.edu.cn/owl/property#linkedInfo>  ?o}";
			ResultSet results = this.getResultSet(sparqlQueryString);
			if (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				if (soln.get("?o") != null) {
					decription = soln.get("?o").toString();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return decription;
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

	@Override
	public String getTriple(String iri) {
		getDataset();
		Model model = dataset.getDefaultModel();

		Resource iriResource = model.getResource(iri);

		Property iriProperty = model.getProperty(iri);

		Model model2 = ModelFactory.createDefaultModel();

	StmtIterator iterator1 = model
				.listStatements(iriResource, null, (RDFNode) null);

		StmtIterator iterator2 = model
				.listStatements(null, iriProperty, (RDFNode) null);

		StmtIterator iterator3 = model
				.listStatements(null, null, iriResource);

		model2.add(iterator1.toList());
		model2.add(iterator2.toList());
		model2.add(iterator3.toList());

		WebDirUtil webDirUtil = new WebDirUtil();

		String uuid = UUID.randomUUID().toString();
		String outputFile = webDirUtil.getWebDir() + "/lcrdf/" + uuid + ".rdf";
		java.io.FileOutputStream outputStream = null;
		try {
			outputStream = new java.io.FileOutputStream(outputFile);
			model2.write(outputStream, "RDF/XML");

		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		closeDataset();// 关闭dataset
		return outputFile;
	}

	@Override
	public PageData<LcStatement> getInstancesPageDataByIndex(
			String sparqlCount, String sparql, int pageIndex, int pageSize,
			String orderBy) {
		PageData<LcStatement> pageData = null;
		try {
			pageData = new PageData<LcStatement>(pageIndex, pageSize);

			int totalCount = this.queryDataCountByIndex(sparqlCount);

			List<LcStatement> list = this.queryInstancesPageListByIndex(sparql,
					pageData.getStartRow(), pageSize, orderBy);

			pageData.setTotalCount(totalCount);

			pageData.setPageData(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return pageData;
	}

	@Override
	public int queryDataCountByIndex(String sparql) {
		Dataset dataset = null;

		int count = 0;
		try {
			dataset = getDataset();// 查询模式，为false
			Query query = QueryFactory.create(sparql);
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			// 获取结果
			ResultSet results = qexec.execSelect();
			count = results.next().getLiteral("?count").getInt();
			qexec.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// dataset.end();
		}
		return count;
	}

	@Override
	public List<LcStatement> queryInstancesPageListByIndex(String sparql,
			int start, int maxSize, String orderBy) {
		Query contentQuery = null;
		QueryExecution qexecQuery = null;
		ResultSet resultsContent = null;
		Dataset dataset = null;
		QueryExecution qexec = null;
		List<LcStatement> list = new ArrayList<LcStatement>();
		try {
			dataset = getDataset();// 查询模式，为false
			sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
					+ " LIMIT " + maxSize;
			Query query = QueryFactory.create(sparql);
			qexec = QueryExecutionFactory.create(query, dataset);
			// 获取结果
			ResultSet results = qexec.execSelect();
			// List<QuerySolution> list = ResultSetFormatter.toList(results);

			QuerySolution querySolution;
			LcStatement statement;
			for (; results.hasNext();) {
				querySolution = results.next();
				statement = new LcStatement();
				if (querySolution.get("?s").canAs(Resource.class)) {
					String object = querySolution.get("?s").toString();
					String object2 = "";
					try {
						object2 = URLEncoder.encode(object, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statement.setObject("<a href='/lcontology/semantic/baseInfo/?iri="
							+ object2 + "'>" + getIRIName(object) + "</a>");
					// 下载信息
					statement.setDownload("<a href='/lcontology/semantic/download?iri="
							+ object2 + "'>下载</a>");

					// 增加内容
					contentQuery = QueryFactory.create("select  ?content {{ <"
							+ object + "> <" + LCPROPERTY.content
							+ "> ?content} UNION { <" + object + "> <"
							+ LCPROPERTY.description + "> ?content}}");
					qexecQuery = QueryExecutionFactory.create(contentQuery,
							dataset);
					resultsContent = qexecQuery.execSelect();
					if (resultsContent.hasNext()) {
						statement.setHasContent(1);
						statement.setContent(resultsContent.next()
								.get("?content").toString());
					}
				}
				list.add(statement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (qexecQuery != null) {
				qexecQuery.close();
			}
			qexec.close();
			dataset.end();
			closeDataset();
		}

		return list;
	}

	@Override
	public List<LcStatement> queryInstancePageList(String sparql, int start,
			int maxSize, String orderBy) {
		Query contentQuery = null;
		QueryExecution qexecQuery = null;
		ResultSet resultsContent = null;
		getDataset();
		sparql = sparql + " ORDER BY " + orderBy + " OFFSET  " + start
				+ " LIMIT " + maxSize;
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		// 获取结果
		ResultSet results = qexec.execSelect();
		// List<QuerySolution> list = ResultSetFormatter.toList(results);
		List<LcStatement> list = new ArrayList<LcStatement>();
		QuerySolution querySolution;
		LcStatement statement;
		for (; results.hasNext();) {
			querySolution = results.next();
			statement = new LcStatement();
			if (querySolution.get("?s").canAs(Resource.class)) {
				String object = querySolution.get("?s").toString();
				String object2 = "";
				try {
					object2 = URLEncoder.encode(object, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				statement.setObject("<a href='/lcontology/semantic/baseInfo/?iri="
						+ object2 + "'>" + getIRIName(object) + "</a>");
				// 设置iri
				statement.setIri(object2);

				// 下载信息
				statement.setDownload("<a href='/lcontology/semantic/download?iri="
						+ object2 + "'>下载</a>");

				// 增加内容
				contentQuery = QueryFactory.create("select  ?content {{ <"
						+ object + "> <" + LCPROPERTY.content
						+ "> ?content} UNION { <" + object + "> <"
						+ LCPROPERTY.description + "> ?content}}");
				qexecQuery = QueryExecutionFactory
						.create(contentQuery, dataset);
				resultsContent = qexecQuery.execSelect();
				if (resultsContent.hasNext()) {
					statement.setHasContent(1);
					statement.setContent(resultsContent.next().get("?content")
							.toString());
				}
			}
			list.add(statement);
		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	@Override
	public PageData<LcStatement> getInstancePageData(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy) {
		PageData<LcStatement> pageData = null;
		try {
			pageData = new PageData<LcStatement>(pageIndex, pageSize);

			int totalCount = this.queryDataCount(sparqlCount);

			List<LcStatement> list = this.queryInstancePageList(sparql,
					pageData.getStartRow(), pageSize, orderBy);

			pageData.setTotalCount(totalCount);

			pageData.setPageData(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return pageData;
	}

	@Override
	public boolean isExit(String subject, String object) {
		getDataset();
		ResultSet results = null;
		String sparql = SparqlPrefix.prefix
				+ " select  (COUNT(*) AS ?count) WHERE {" + subject + " a "
				+ object + "} ";
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);

		results = qexec.execSelect();

		int count = results.next().getLiteral("?count").getInt();

		qexec.close();
		closeDataset();// 关闭dataset
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getInstanceDescription(String instanceIRI) {
		String InstanceDescription = null;
		getDataset();
		ResultSet results = null;
		String sparql = " select  ?description WHERE {" + instanceIRI + "<"
				+ LCPROPERTY.description + "> ?description } ";
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);

		results = qexec.execSelect();
		if (results.hasNext()) {
			QuerySolution querySolution = results.next();
			InstanceDescription = querySolution.get("?description").toString();
		}

		qexec.close();
		closeDataset();// 关闭dataset
		return InstanceDescription;
	}

	@Override
	public List<String> getObjects(String subject, String predicate) {

		List<String> objectList = new ArrayList<String>();
		getDataset();
		ResultSet results = null;
		String sparql = " select  ?object WHERE {" + subject + " " + predicate
				+ " ?object } ";
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);

		results = qexec.execSelect();
		for (; results.hasNext();) {
			QuerySolution querySolution = results.next();
			if (querySolution.get("?object").canAs(Resource.class)) {
				String object = querySolution.get("?object").toString();
				String object2 = "";
				try {
					object2 = URLEncoder.encode(object, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				objectList.add("<a href='/lcontology/semantic/baseInfo/?iri=" + object2
						+ "'>" + getIRIName(object) + "</a>");
			} else {
				String object = querySolution.getLiteral("object").getString();
				objectList.add(object);

			}
		}

		qexec.close();
		closeDataset();// 关闭dataset
		return objectList;
	}

	@Override
	public List<String> getLabels(String subject) {
		List<String> labelList = new ArrayList<String>();
		getDataset();
		ResultSet results = null;
		String sparql = SparqlPrefix.prefix + " select  ?lable WHERE {"
				+ subject + " rdfs:label ?lable } ";
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);

		results = qexec.execSelect();

		for (; results.hasNext();) {
			QuerySolution querySolution = results.next();
			labelList.add(querySolution.get("?lable").toString());
		}
		qexec.close();
		closeDataset();// 关闭dataset
		return labelList;
	}

	@Override
	public List<String> queryByString(String sparql) {
		List<String> list = new ArrayList<String>();
		getDataset();
		ResultSet results = null;
		//Query query = QueryFactory.create(sparql + "LIMIT 10");
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);

		results = qexec.execSelect();
		String reslut = "";
		for (; results.hasNext();) {
			QuerySolution querySolution = results.next();
			// 主语的情况
			if (querySolution.get("?answer") != null) {
				if (querySolution.get("?answer").canAs(Resource.class)) {
					String object = querySolution.get("?answer").toString();
					String object2 = "";
					try {
						object2 = URLEncoder.encode(object, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					reslut = "<a href='/lcontology/semantic/baseInfo/?iri=" + object2
							+ "'>" + getIRIName(object) + "</a>";
				} else {
					reslut = querySolution.getLiteral("answer").getString();
				}
			}
			list.add(reslut);
		}
		qexec.close();
		closeDataset();// 关闭dataset
		return list;
	}

	// label是文字要加上引号
	/*
	 * @Override public List<InstanceData> getInstanceByLabel(String label) {
	 * getDataset(); List<InstanceData> instanceDatas = new
	 * ArrayList<InstanceData>(); ResultSet results = null; String sparql =
	 * SparqlPrefix.prefix +
	 * " select  ?instance ?type WHERE {?instance rdfs:label '" + label +
	 * "'. ?instance a ?type } "; Query query = QueryFactory.create(sparql);
	 * QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
	 * results = qexec.execSelect(); String instance; String type; InstanceData
	 * instanceData = new InstanceData(); for (; results.hasNext();) {
	 * QuerySolution querySolution = results.next(); type =
	 * querySolution.get("?type").toString(); // 类型名
	 * instanceData.setTypeName(getIRIName(type)); // 类型iri try { type =
	 * URLEncoder.encode(type, "UTF-8"); } catch (UnsupportedEncodingException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * instanceData.setTypeIRI(type);
	 * 
	 * instance = querySolution.get("?instance").toString(); // 实例名
	 * instanceData.setInstanceName(getIRIName(instance)); // 实例IRI try {
	 * instance = URLEncoder.encode(instance, "UTF-8"); } catch
	 * (UnsupportedEncodingException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } instanceData.setInstanceIRI(instance);
	 * instanceDatas.add(instanceData); } qexec.close(); closeDataset();//
	 * 关闭dataset return instanceDatas; }
	 */

	@Override
	public Map<InstanceData, Set<TypeData>> getInstanceAndTypeByLabel(
			String label) {
		getDataset();
		Map<InstanceData, Set<TypeData>> map = new HashMap<InstanceData, Set<TypeData>>();
		ResultSet results = null;
		String sparql = SparqlPrefix.prefix
				+ " select  ?instance ?type WHERE {?instance rdfs:label '"
				+ label + "'. ?instance a ?type } ";
		Query query = QueryFactory.create(sparql);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		results = qexec.execSelect();
		String instance;
		String type;
		InstanceData instanceData;
		for (; results.hasNext();) {
			instanceData = new InstanceData();
			QuerySolution querySolution = results.next();
			instance = querySolution.get("?instance").toString();
			type = querySolution.get("?type").toString();
			instanceData.setName(getIRIName(instance));
			instanceData.setIri(instance);
			InstanceData data = isExit(map.keySet(), instanceData.getIri());
			if (data != null) {
				TypeData typeData = new TypeData();
				typeData.setTypeName(getIRIName(type));
				typeData.setTypeIRI(type);
				map.get(data).add(typeData);
			} else {
				Set<TypeData> typeDatas = new HashSet<TypeData>();
				TypeData typeData = new TypeData();
				typeData.setTypeName(getIRIName(type));
				typeData.setTypeIRI(type);
				typeDatas.add(typeData);
				map.put(instanceData, typeDatas);
			}
		}
		qexec.close();
		closeDataset();// 关闭dataset
		return map;
	}

	/**
	 * 判断实例是否已经存在
	 * @author : 丁国柱
	 * @date : 2014-11-26 下午9:56:21
	 */
	public static InstanceData isExit(Set<InstanceData> datas, String iri) {
		for (Iterator iterator = datas.iterator(); iterator.hasNext();) {
			InstanceData instanceD = (InstanceData) iterator.next();
			if (instanceD.getIri().equals(iri)) {
				return instanceD;
			}
		}
		return null;
	}

	
	
	@Override
	public Map<String, List<LcStatement>> queryRelationPageList(String sparql,
			int maxSize) {
		List<LcStatement> list = null;
		Map<String, List<LcStatement>> map = new HashMap<String, List<LcStatement>>();
		try {
			getDataset();
			sparql = sparql + " LIMIT " + maxSize;
			Query query = QueryFactory.create(sparql);
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			// 获取结果
			ResultSet results = qexec.execSelect();
			// List<QuerySolution> list = ResultSetFormatter.toList(results);
			list = new ArrayList<LcStatement>();
			QuerySolution querySolution;
			LcStatement statement;
			for (; results.hasNext();) {
				querySolution = results.next();
				statement = new LcStatement();
				if (querySolution.get("?s").canAs(Resource.class)) {
					String url = querySolution.get("?s").toString();
					String url2 = "";
					try {
						url2 = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					statement.setSubject("<a href='/lcontology/semantic/baseInfo/?iri="
							+ url2 + "'>" + getIRIName(url) + "</a>");
				} else {
					statement.setSubject(getIRIName(querySolution.get("?s")
							.toString()));
				}
				// 类型
				String type = "";
				if (querySolution.get("?x").canAs(Resource.class)) {
					String url = querySolution.get("?x").toString();
					String url2 = "";
					try {
						url2 = URLEncoder.encode(url, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					type = "<a href='/lcontology/semantic/baseInfo/?iri=" + url2 + "'>"
							+ getIRIName(url) + "</a>";
				} else {
					String url = getIRIName(querySolution.get("?x").toString());
					type = getIRIName(url);
				}
				if (map.containsKey(type)) {
					map.get(type).add(statement);
				} else {
					list.add(statement);
					map.put(type, list);
					list = new ArrayList<LcStatement>();
				}
			}
			qexec.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDataset();// 关闭dataset
		}
		return map;
	}
}
