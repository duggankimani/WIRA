package com.duggan.workflow.client.ui.addDoc;

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.data.shared.TreeStore;

public class Folder implements TreeStore.TreeNode<Folder> {

	private List<Folder> children = new ArrayList<Folder>();

	public List<Folder> getChildren() {
		if (children == null || children.size() == 0)
			return null;
		return children;
	}

	@Override
	public Folder getData() {
		return this;
	}

}
