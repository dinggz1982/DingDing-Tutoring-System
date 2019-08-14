package edu.gzhu.its.qa.model;

import java.util.List;

/**
 * 分页模型
 * @author Administrator
 * @param <T>
 *
 */
public class PageModel<T> {
	
	private List<T> list;
	
	private int pages;
	
	private long total;
	
	private int size;
	
	private int page;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	

}
