/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.List;

/**
 *
 * @author alexs
 */
public class DocflowPage {
  private Long skip = null;
  private Long take = null;
  private Long totalCount = null;
  private List<DocflowPageItem> docflowsPageItem = null;

	public Long getSkip() {
		return skip;
	}

	public void setSkip(Long skip) {
		this.skip = skip;
	}

	public Long getTake() {
		return take;
	}

	public void setTake(Long take) {
		this.take = take;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<DocflowPageItem> getDocflowsPageItem() {
		return docflowsPageItem;
	}

	public void setDocflowsPageItem(List<DocflowPageItem> docflowsPageItem) {
		this.docflowsPageItem = docflowsPageItem;
	}

}
