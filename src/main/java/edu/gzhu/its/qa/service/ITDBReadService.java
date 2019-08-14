package edu.gzhu.its.qa.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import edu.gzhu.its.base.model.PageData;
import edu.gzhu.its.qa.model.InstanceData;
import edu.gzhu.its.qa.model.LcStatement;
import edu.gzhu.its.qa.model.TypeData;

/**
 * 语义数据库操作接口
 * @author : 丁国柱
 */
public interface ITDBReadService {

	/**
	 * 获取语义条目数
	 * @Title: IBaseDAO.java
	 * @Package bnu.edu.lcell.repository
	 * @Description: TODO
	 * @author 丁国柱 dinggz1982@gmail.com
	 * @date 2014-10-9 下午5:24:41
	 * @version V1.0
	 */
	public int queryDataCount(String sparql);

	/**
	 * 用jena-text-index
	 * 
	 * @author : 丁国柱
	 * @date : 2014-11-4 下午9:22:20
	 */
	public int queryDataCountByIndex(String sparql);
	

	/**
	 * 根据url返回三元组
	 * 
	 * @Title: ITDBReadService.java
	 * @Package bnu.edu.lcell.services
	 * @Description: TODO
	 * @author 丁国柱 dinggz1982@gmail.com
	 * @date 2014-10-30 下午12:47:20
	 * @version V1.0
	 */
	public String getTriple(String iri);

	/**
	 * 获取全部记录数
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-21 下午5:28:30
	 */
	public int queryAllCount();

	/**
	 * 判断一个subject是否存在，即是否存在subject-->rdf:type-->object
	 * @author : 丁国柱
	 * @date : 2014-11-5 上午10:51:01
	 */
	public boolean isExit(String subject, String object);

	/**
	 * 获取语义分页
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-9 下午5:28:47
	 */
	public List<LcStatement> queryPageList(final String sparql,
			final int start, final int maxSize, String orderBy);

	/**
	 * 获取实例分页
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-9 下午5:28:47
	 */
	public List<LcStatement> queryInstancesPageList(final String sparql,
			final int start, final int maxSize, String orderBy);

	/**
	 * 获取单个实例
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-9 下午5:28:47
	 */
	public List<LcStatement> queryInstancePageList(final String sparql,
			final int start, final int maxSize, String orderBy);

	/**
	 * jean-text 获取实例分页
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-9 下午5:28:47
	 */
	public List<LcStatement> queryInstancesPageListByIndex(final String sparql,
			final int start, final int maxSize, String orderBy);

	/**
	 * 获取语义分页
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-9 下午5:28:47
	 */
	public List<LcStatement> queryTypePageList(final String sparql,
			final int maxSize);
	
	/**
	 * 获取语义分页
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-9 下午5:28:47
	 */
	public Map<String, List<LcStatement>>  queryRelationPageList(final String sparql,
			final int maxSize);
	

	/**
	 * 得到语义分页数据并封装成pagedata
	 * 
	 * @param start
	 * @param maxSize
	 * @param paramMap
	 * @return
	 */
	public PageData<LcStatement> getPageData(String sparqlCount, String sparql,
			int pageIndex, int pageSize, String orderBy);


	/**
	 * 获取相关三元组
	 * 
	 * @Title: ITDBReadService.java
	 * @Package bnu.edu.lcell.services
	 * @Description: TODO
	 * @author 丁国柱 dinggz1982@gmail.com
	 * @date 2014-10-21 上午10:38:48
	 * @version V1.0
	 */
	public PageData<LcStatement> getTriples(String sparqlCount, String sparql,
			int pageIndex, int pageSize, String orderBy);

	/**
	 * 获取节点作为主语的数据
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-21 下午12:39:01
	 */
	public PageData<LcStatement> getNodeAsSubject(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy);

	/**
	 * 获取节点作为谓语的数据
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-21 下午12:39:01
	 */
	public PageData<LcStatement> getNodeAsPredicate(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy);

	/**
	 * 获取节点作为宾语的数据
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-21 下午12:39:01
	 */
	public PageData<LcStatement> getNodeAsObject(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy);

	/**
	 * 获取到三元组信息
	 * 
	 * @Title: ITDBReadService.java
	 * @Package bnu.edu.lcell.services
	 * @Description: TODO
	 * @author 丁国柱 dinggz1982@gmail.com
	 * @date 2014-10-21 上午10:40:32
	 * @version V1.0
	 */
	public List<LcStatement> getStatementPageList(final String sparql,
			final int start, final int maxSize, String orderBy);

	/**
	 * 获取节点作为Subject的数据
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-21 下午12:41:13
	 */
	public List<LcStatement> getStatementPageListByNodeAsSubjct(
			final String sparql, final int start, final int maxSize,
			String orderBy);

	/**
	 * 获取节点作为Predicate的数据
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-21 下午12:41:13
	 */
	public List<LcStatement> getStatementPageListByNodeAsPredicate(
			final String sparql, final int start, final int maxSize,
			String orderBy);

	/**
	 * 获取节点作为Object的数据
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-21 下午12:41:13
	 */
	public List<LcStatement> getStatementPageListByNodeAsObject(
			final String sparql, final int start, final int maxSize,
			String orderBy);

	/**
	 * 得到实例分页数据并封装成pagedata
	 * 
	 * @param start
	 * @param maxSize
	 * @param paramMap
	 * @return
	 */
	public PageData<LcStatement> getInstancesPageData(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy);

	/**
	 * 得到单个实例分页数据并封装成pagedata
	 * 
	 * @param start
	 * @param maxSize
	 * @param paramMap
	 * @return
	 */
	public PageData<LcStatement> getInstancePageData(String sparqlCount,
			String sparql, int pageIndex, int pageSize, String orderBy);

	/**
	 * 得到实例分页数据并封装成pagedata--用到jena text index
	 * 
	 * @param start
	 * @param maxSize
	 * @param paramMap
	 * @return
	 */
	public PageData<LcStatement> getInstancesPageDataByIndex(
			String sparqlCount, String sparql, int pageIndex, int pageSize,
			String orderBy);

	/**
	 * 关闭TDB数据库
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-12 上午11:56:00
	 */
	public void closeTDB();

	/**
	 * 获得默认模型
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-10 上午11:06:06
	 */
	public Model getJenaDefaultModel();

	/**
	 * 根据query获取ResultSet
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-14 下午3:18:41
	 */
	public ResultSet getResultSet(String queryString);

	/**
	 * 获取检索的类型
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-17 下午1:05:09
	 */
	public Map<String, String> getType(String queryString);

	/**
	 * 获得描述信息
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-17 下午4:19:11
	 */
	public String getDescription(String queryString);

	/**
	 * 显示关联信息
	 * 
	 * @author : 丁国柱
	 * @date : 2014-10-17 下午4:19:11
	 */
	public String getLinkedInfo(String queryString);
	
	/**
	 * 根据实例iri获取实例的描述信息
	 * @author : 丁国柱
	 * @date : 2014-11-6 下午12:47:45
	 */
	public String getInstanceDescription(String instanceIRI);
	
	/**
	 * 根据主语谓语获取Object信息
	 * @author : 丁国柱
	 * @date : 2014-11-6 下午1:00:28
	 */
	public List<String> getObjects(String subject,String predicate);
	
	/**
	 * 根据主语获取标签信息
	 * @author : 丁国柱
	 * @date : 2014-11-6 下午10:37:02
	 */
	public List<String> getLabels(String subject);
	
	/**
	 * 问答系统根据sparql，返回结果
	 * @author : 丁国柱
	 * @date : 2014-11-14 下午5:19:07
	 */
	public List<String> queryByString(String sparql);
	
	/**
	 * 根据标签获取类和实例
	* @Title: ITDBReadService.java 
	* @Package bnu.edu.lcell.services 
	* @Description: TODO
	* @author 丁国柱   dinggz1982@gmail.com   
	* @date 2014-11-25 下午7:15:34 
	* @version V1.0
	 */
	//public List<InstanceData> getInstanceByLabel(String label);
	
	/**
	 * 获取实例iri和类别list
	 * @author : 丁国柱
	 */
	public Map<InstanceData,Set<TypeData>> getInstanceAndTypeByLabel(String label);
}
