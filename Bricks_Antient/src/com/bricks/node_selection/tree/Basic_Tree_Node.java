package com.bricks.node_selection.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Basic_Tree_Node {
	
    private static final Basic_Tree_Node[] CHILDREN_TEMPLATE = new Basic_Tree_Node[] {};

    protected Basic_Tree_Node mParent;

    protected final List<Basic_Tree_Node> mChildren = new ArrayList<Basic_Tree_Node>();

    public int x, y, width, height;
    //nums of child added
    public int childOffset = 0;
    //index of the child
    public int index;
    public int classNameIndex = 1;

    public HashMap<String,Integer> classNameMap = new HashMap<String, Integer>();
	// whether the boundary fields are applicable for the node or not
    // RootWindowNode has no bounds, but UiNodes should
    protected boolean mHasBounds = false;

    public void addChild(Basic_Tree_Node child) {
        if (child == null) {
            throw new NullPointerException("Cannot add null child");
        }
        if (mChildren.contains(child)) {
            throw new IllegalArgumentException("node already a child");
        }
        mChildren.add(child);
        childOffset++;
        child.mParent = this;
        child.index = childOffset;
        if(!Root_Window_Node.class.isInstance(child)){
        	String className = ((Node_UI)child).getNodeClassAttribute();
        	if(classNameMap.get(className)==null){
            	classNameMap.put(className, 1);
            }else{
            	classNameMap.put(className, classNameMap.get(className)+1);
            }
            child.classNameIndex = classNameMap.get(className);
        }
    }

    public List<Basic_Tree_Node> getChildrenList() {
        return Collections.unmodifiableList(mChildren);
    }

    public Basic_Tree_Node[] getChildren() {
        return mChildren.toArray(CHILDREN_TEMPLATE);
    }

    public Basic_Tree_Node getParent() {
        return mParent;
    }

    public boolean hasChild() {
        return mChildren.size() != 0;
    }

    public int getChildCount() {
        return mChildren.size();
    }

    public void clearAllChildren() {
        for (Basic_Tree_Node child : mChildren) {
            child.clearAllChildren();
        }
        mChildren.clear();
    }

    /**
     *
     * Find nodes in the tree containing the coordinate
     *
     * The found node should have bounds covering the coordinate, and none of its children's
     * bounds covers it. Depending on the layout, some app may have multiple nodes matching it,
     * the caller must provide a {@link IFindNodeListener} to receive all found nodes
     *
     * @param px
     * @param py
     * @return
     */
    public boolean findLeafMostNodesAtPoint(int px, int py, IFindNodeListener listener) {
        boolean foundInChild = false;
        for (Basic_Tree_Node node : mChildren) {
            foundInChild |= node.findLeafMostNodesAtPoint(px, py, listener);
        }
        // checked all children, if at least one child covers the point, return directly
        if (foundInChild) return true;
        // check self if the node has no children, or no child nodes covers the point
        if (mHasBounds) {
            if (x <= px && px <= x + width && y <= py && py <= y + height) {
                listener.onFoundNode(this);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Object[] getAttributesArray () {
        return null;
    };

    public static interface IFindNodeListener {
        void onFoundNode(Basic_Tree_Node node);
    }
}

