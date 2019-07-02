package com.bricks.node_selection;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.bricks.node_selection.tree.Attribute_Pair;
import com.bricks.node_selection.tree.Basic_Tree_Node;
import com.bricks.node_selection.tree.Basic_Tree_Node.IFindNodeListener;
import com.bricks.node_selection.tree.HierarchyXML_Loader_UI;
import com.bricks.node_selection.tree.Node_UI;
import com.bricks.tools.Utils_Constants;
/**
 * @author DraLastat
 * @Description parse xpath, search node. business layer
 */
public class Automator_Model_UI {
    private Basic_Tree_Node mRootNode;
    private Node_UI uRootNode;
    private Basic_Tree_Node mSelectedNode;
    private Rectangle mCurrentDrawingRect;
    private List<Rectangle> mNafNodes;

    private boolean mShowNafNodes = false;
    private List<Node_UI> mNodelist;
    private Set<String> mSearchKeySet = new HashSet<String>();

    public Automator_Model_UI(File xmlDumpFile) {
        mSearchKeySet.add("text");
        mSearchKeySet.add("content-desc");
        Utils_Constants.document = null;
        HierarchyXML_Loader_UI loader = new HierarchyXML_Loader_UI();
        Utils_Constants.document = loader.getDocument(xmlDumpFile.getAbsolutePath());
        List<Element> list = Utils_Constants.document.selectNodes("//node");
        for(int i=0; i<list.size(); i++){
        	Element e = list.get(i);
        	e.setName(e.attributeValue("class"));
        }
        Basic_Tree_Node rootNode = loader.parseXml(xmlDumpFile.getAbsolutePath());
        if (rootNode == null) {
            System.err.println("null rootnode after parsing.");
            throw new IllegalArgumentException("Invalid ui automator hierarchy file.");
        }

        mNafNodes = loader.getNafNodes();
        if (mRootNode != null) {
            mRootNode.clearAllChildren();
        }

        mRootNode = rootNode;
//        mExploreMode = true;
        mNodelist = loader.getAllNodes();
    }

    public Basic_Tree_Node getXmlRootNode() {
        return mRootNode;
    }

    public Node_UI getuRootNode(){
        return  uRootNode;
    }

    public Basic_Tree_Node getSelectedNode() {
        return mSelectedNode;
    }

    public List<Node_UI> getmNodelist(){
        return mNodelist;
    }
    /**
     * change node selection in the Model recalculate the rect to highlight,
     * also notifies the View to refresh accordingly
     *
     * @param node
     */
    public void setSelectedNode(Basic_Tree_Node node) {
        mSelectedNode = node;
        if (mSelectedNode instanceof Node_UI) {
        	Node_UI uiNode = (Node_UI) mSelectedNode;
            mCurrentDrawingRect = new Rectangle(uiNode.x, uiNode.y, uiNode.width, uiNode.height);
        } else {
            mCurrentDrawingRect = null;
        }
    }

    public Rectangle getCurrentDrawingRect() {
        return mCurrentDrawingRect;
    }

    /**
     * Do a search in tree to find a leaf node or deepest parent node containing the coordinate
     *
     * @param x
     * @param y
     * @return
     */
    public Basic_Tree_Node updateSelectionForCoordinates(int x, int y) {
    	Basic_Tree_Node node = null;

        if (mRootNode != null) {
            MinAreaFindNodeListener listener = new MinAreaFindNodeListener();
            boolean found = mRootNode.findLeafMostNodesAtPoint(x, y, listener);
            if (found && listener.mNode != null && !listener.mNode.equals(mSelectedNode)) {
                node = listener.mNode;
            }
        }

        return node;
    }

//    public boolean isExploreMode() {
//        return mExploreMode;
//    }
//
//    public void toggleExploreMode() {
//        mExploreMode = !mExploreMode;
//    }

    private static class MinAreaFindNodeListener implements IFindNodeListener {
    	Basic_Tree_Node mNode = null;

        @Override
        public void onFoundNode(Basic_Tree_Node node) {
            if (mNode == null) {
                mNode = node;
            } else {
                if ((node.height * node.width) < (mNode.height * mNode.width)) {
                    mNode = node;
                }
            }
        }
    }

    public List<Rectangle> getNafNodes() {
        return mNafNodes;
    }

    public void toggleShowNaf() {
        mShowNafNodes = !mShowNafNodes;
    }

    public boolean shouldShowNafNodes() {
        return mShowNafNodes;
    }

    public List<Basic_Tree_Node> searchNode(String tofind) {
        List<Basic_Tree_Node> result = new LinkedList<Basic_Tree_Node>();
        for (Basic_Tree_Node node : mNodelist) {
            Object[] attrs = node.getAttributesArray();
            for (Object attr : attrs) {
                if (!mSearchKeySet.contains(((Attribute_Pair) attr).key))
                    continue;
                if (((Attribute_Pair) attr).value.toLowerCase().contains(tofind.toLowerCase())) {
                    result.add(node);
                    break;
                }
            }
        }
        return result;
    }


    public List<Basic_Tree_Node> searchByXpath(String xpath){
        Map<String,String> xMap = parseXpath(xpath);
        String class0 = xMap.get("class0");
        String resourceId = xMap.get("resource-id");
        String text  = xMap.get("text");
        String contentDesc = xMap.get("content-desc");
        int length = xMap.size()-4;


        if (resourceId !="" || text != "" || contentDesc != ""){
            List<Basic_Tree_Node> parents = new ArrayList<Basic_Tree_Node>();
            List<Basic_Tree_Node> childList = new ArrayList<Basic_Tree_Node>();
            int index =1;
            for (Basic_Tree_Node node :mNodelist){
                Node_UI uNode = (Node_UI)node;
                String uText="";
                String uResourceId = "";
                String uContentDesc = "";
                if (resourceId != ""){
                    uResourceId = uNode.getAttributes().get("resource-id");
                }
                if (text != ""){
                    uText = uNode.getAttributes().get("text");
                }
                if (contentDesc != ""){
                    uContentDesc = uNode.getAttributes().get("content-desc");
                }

                if (uResourceId.equals(resourceId) && uText.equals(text) && uContentDesc.equals(contentDesc)){
                    parents.add(node);
                }
            }

            if (xMap.size() == 4){
            	if (parents.size()==0){
            		return null;
            	} else {
            		return parents;
            	}
            } else {
            	while(index <= length){
            		String classNameStr = xMap.get("class"+index);
            		childList.clear();
            		for (Basic_Tree_Node node : parents){
            			List<Basic_Tree_Node> childs = node.getChildrenList();
            			if (classNameStr.indexOf("[")>=0){
            				String className = classNameStr.substring(0,classNameStr.indexOf("["));
            				int indexStr = Integer.parseInt(classNameStr.substring(classNameStr.indexOf("[")+1,classNameStr.indexOf("]")));

            				int number = 0;
            				for (int i =0;i<childs.size();i++){
            					Node_UI uNode = (Node_UI)childs.get(i);

            					if (uNode.getAttributes().get("class").equals(className)){
            						number++;
            						if (number == indexStr){
            							childList.add(childs.get(i));
            						}
            					}
            				}
            			} else {
            				for (Basic_Tree_Node child : childs){
            					childList.add(child);
            				}
            			}
            		}
                    
            		if (classNameStr.indexOf("[")>=0){
            			String className = classNameStr.substring(0,classNameStr.indexOf("["));

            			parents.clear();

            			for (Basic_Tree_Node node : childList){
            				Node_UI child = (Node_UI)node;
            				if (child.getAttributes().get("class").equals(className)){
            					parents.add(node);
            				}
            			}
                    } else {
                    	parents.clear();

                    	for (Basic_Tree_Node node : childList){
                    		Node_UI child = (Node_UI)node;
                    		if (child.getAttributes().get("class").equals(classNameStr)){
                    			parents.add(node);
                    		}
                    	}
                    }
                    index ++;
                }
            	if (parents.size()==0){
                    return null;
                } else {
                    return parents;
                }
            }
        } else {
            List<Basic_Tree_Node> parents = new ArrayList<Basic_Tree_Node>();
            List<Basic_Tree_Node> childList = new ArrayList<Basic_Tree_Node>();
            int index = 1;
            for (Basic_Tree_Node node : mNodelist){
                Node_UI uNode = (Node_UI)node;
                if (uNode.getAttributes().get("class").equals(class0)){
                    parents.add(node);
                }
            }

            if (xMap.size() == 4){
                if (parents.size()==0){
                    return null;
                } else {
                    return parents;
                }
            } else {
                while(index <= length){
                    String classNameStr = xMap.get("class"+index);
                    childList.clear();
                    for (Basic_Tree_Node node : parents){
                        List<Basic_Tree_Node> childs = node.getChildrenList();
                        if (classNameStr.indexOf("[")>=0){
                            String className = classNameStr.substring(0,classNameStr.indexOf("["));
                            int indexStr = Integer.parseInt(classNameStr.substring(classNameStr.indexOf("[")+1,classNameStr.indexOf("]")));

                            int number = 0;
                            for (int i =0;i<childs.size();i++){
                            	Node_UI uNode = (Node_UI)childs.get(i);

                                if (uNode.getAttributes().get("class").equals(className)){
                                    number++;
                                    if (number == indexStr){
                                        childList.add(childs.get(i));
                                    }
                                }
                            }
                        } else {
                            for (Basic_Tree_Node child : childs){
                                childList.add(child);
                            }
                        }
                    }
                    if (classNameStr.indexOf("[")>=0){
                        String className = classNameStr.substring(0,classNameStr.indexOf("["));

                        parents.clear();

                        for (Basic_Tree_Node node : childList){
                        	Node_UI child = (Node_UI)node;
                            if (child.getAttributes().get("class").equals(className)){
                                parents.add(node);
                            }
                        }
                    } else {
                        parents.clear();

                        for (Basic_Tree_Node node : childList){
                            Node_UI child = (Node_UI)node;
                            if (child.getAttributes().get("class").equals(classNameStr)){
                                parents.add(node);
                            }
                        }
                    }
                    index ++;
                }
                if (parents.size()==0){
                    return null;
                } else {
                    return parents;
                }
            }
        }
    }


    public Map<String,String> parseXpath(String xpath){
        xpath = xpath.substring(2,xpath.length());
        String first="";
        String[] other =null;
        String[] xlist = null;
        String[] attributeList = null;
        Map<String,String> map = new HashMap<String, String>();
        map.put("class0","");
        map.put("text","");
        map.put("resource-id","");
        map.put("content-desc","");

        if (xpath.indexOf("@resource-id")>=0){
            int end = xpath.indexOf("']")+2;
            first = xpath.substring(0,end);
            if (end<xpath.length()){
                other = xpath.substring(end+1,xpath.length()).split("/");
            }
        } else {
            xlist = xpath.split("/");
        }
        if (first != ""){
            String xclass = first.substring(0,first.indexOf("[@"));
            map.remove("class0");
            map.put("class0",xclass);
            int begin = first.indexOf("[@")+1;
            int end = first.indexOf("']");
            String firstNode = first.substring(begin,end);
            if (firstNode.indexOf("and")>=0){
                attributeList = firstNode.split("\\s*and\\s*");
                for (int i =0 ;i<attributeList.length;i++){
                    if (attributeList[i].indexOf("resource-id")>=0){
                        String resourceId  = attributeList[i].substring(attributeList[i].indexOf("=")+2,attributeList[i].lastIndexOf("'"));
                        map.remove("resource-id");
                        map.put("resource-id",resourceId);
                    } else if (attributeList[i].indexOf("text")>=0){
                        String text  = attributeList[i].substring(attributeList[i].indexOf("=")+2,attributeList[i].lastIndexOf("'"));
                        map.remove("text");
                        map.put("text",text);
                    } else if (attributeList[i].indexOf("content-desc")>=0){
                        String contentDesc  = attributeList[i].substring(attributeList[i].indexOf("=")+2,attributeList[i].lastIndexOf("'"));
                        map.remove("content-desc");
                        map.put("content-desc",contentDesc);
                    }
                }
            } else {
                map.remove("resource-id");
                map.put("resource-id",firstNode.substring(firstNode.indexOf("=")+2,firstNode.length()));
            }

            if (other != null){
                for(int i =0;i< other.length; i++){
                    map.put("class"+(i+1),other[i]);
                }
            }
        } else {
            if (xlist[0].indexOf("@")>=0){
                String xclass = xlist[0].substring(0,xlist[0].indexOf("[@"));
                map.remove("class0");
                map.put("class0",xclass);

                String firstNode = xlist[0].substring(xlist[0].indexOf("[@")+1,xlist[0].indexOf("']")+2);

                if (firstNode.indexOf("and")>=0){
                    attributeList = firstNode.split("\\s*and\\s*");
                    for (int i =0;i<attributeList.length;i++){
                        if (attributeList[i].indexOf("text")>0){
                            String text = attributeList[i].substring(attributeList[i].indexOf("=")+2,attributeList[i].indexOf("'"));
                            map.remove("text");
                            map.put("text",text);
                        } else if (attributeList[i].indexOf("content-desc")>=0){
                            String contentDesc = attributeList[i].substring(attributeList[i].indexOf("=")+2,attributeList[i].indexOf("'"));
                            map.remove("content-desc");
                            map.put("content-desc",contentDesc);
                        }
                    }
                } else {
                    if (firstNode.indexOf("text")>=0){
                        String text = firstNode.substring(firstNode.indexOf("=")+2,firstNode.indexOf("']"));
                        map.remove("text");
                        map.put("text",text);
                    } else {
                        int begin = firstNode.indexOf("=")+2;
                        int end = firstNode.indexOf("']");
                        String contentDesc = firstNode.substring(begin,end);
                        map.remove("content-desc");
                        map.put("content-desc",contentDesc);
                    }
                }

                for(int i =1;i< xlist.length; i++){
                    map.put("class"+(i),xlist[i]);
                }
            } else {
                map.remove("class0");
                map.put("class0",xlist[0]);
                for(int i =1;i< xlist.length; i++){
                    map.put("class"+(i),xlist[i]);
                }
            }
        }
        return map;
    }


}
