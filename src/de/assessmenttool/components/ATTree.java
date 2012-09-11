package de.assessmenttool.components;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class ATTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = -98756233586260990L;

	public ATTree() {
		// TODO Auto-generated constructor stub
	}

	public ATTree(Object[] value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public ATTree(Vector<?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public ATTree(Hashtable<?, ?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public ATTree(TreeNode root) {
		super(root);
		// TODO Auto-generated constructor stub
	}

	public ATTree(TreeModel newModel) {
		super(newModel);
		// TODO Auto-generated constructor stub
	}

	public ATTree(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		// TODO Auto-generated constructor stub
	}

}
