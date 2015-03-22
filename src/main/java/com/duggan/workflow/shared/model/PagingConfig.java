package com.duggan.workflow.shared.model;

public class PagingConfig {

	private int limit=30;
	private int offset=0;
	private int totalCount=0;
	private int currentPage=0;

	public PagingConfig() {
	}

	public PagingConfig(int offset) {
	}

	public int getPageCount(){
		if(limit==0 || totalCount==0){
			return 0;
		}
		
		if(totalCount%limit ==0){
			return totalCount/limit;
		}
		
		return totalCount/limit+1;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
