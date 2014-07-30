package com.duggan.workflow.client.ui.task;

import com.duggan.workflow.client.ui.task.SearchPresenter.ISearchView;
import com.google.inject.Inject;

public class SearchView extends AbstractTaskView implements ISearchView{

	@Inject
	public SearchView(Binder binder) {
		super(binder);
	}

}
