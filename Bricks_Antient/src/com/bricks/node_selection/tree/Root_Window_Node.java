package com.bricks.node_selection.tree;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class Root_Window_Node extends Basic_Tree_Node {

    private final String mWindowName;
    private Object[] mCachedAttributesArray;
    private int mRotation;

    public Root_Window_Node(String windowName) {
        this(windowName, 0);
    }

    public Root_Window_Node(String windowName, int rotation) {
        mWindowName = windowName;
        mRotation = rotation;
    }

    @Override
    public String toString() {
        return mWindowName;
    }

    @Override
    public Object[] getAttributesArray() {
        if (mCachedAttributesArray == null) {
            mCachedAttributesArray = new Object[]{new Attribute_Pair("window-name", mWindowName)};
        }
        return mCachedAttributesArray;
    }

    public int getRotation() {
        return mRotation;
    }
}