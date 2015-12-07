package com.duggan.workflow.shared.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sencha.gxt.data.shared.TreeStore.TreeNode;

public abstract class IsProcessDisplay extends SerializableObj implements Listable, TreeNode<IsProcessDisplay>{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<IsProcessDisplay> children = new ArrayList<IsProcessDisplay>();
	
	@Override
	public List<? extends TreeNode<IsProcessDisplay>> getChildren() {
		sort();
		
		if(children==null || children.size()==0)
			return null;
		
		return children;
	}
	
	@Override
	public IsProcessDisplay getData() {
		return this;
	}

	private void sort() {
		Collections.sort(children,new Comparator<IsProcessDisplay>() {
			@Override
			public int compare(IsProcessDisplay o1, IsProcessDisplay o2) {
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
	}
	
	public boolean isDirectory(){
		return this instanceof ProcessCategory;
	}
}
