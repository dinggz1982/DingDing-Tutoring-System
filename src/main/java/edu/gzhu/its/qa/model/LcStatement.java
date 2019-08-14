package edu.gzhu.its.qa.model;

/**
 * 学习元新增三元组知识说明
 * 
 * @author : 丁国柱
 * @date : 2014-9-23 下午9:15:53
 */
public class LcStatement {
	private String subject;
	private String predicate;
	private String object;
	private String content;
	private String download;
	private String iri;//当前节点iri
	private int hasContent;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHasContent() {
		return hasContent;
	}

	public void setHasContent(int hasContent) {
		this.hasContent = hasContent;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

}
