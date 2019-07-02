package com.bricks.node_selection.tree;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bricks.tools.Utils_Constants;

/**
 * 
 * @author DraLastat
 * @Description 
 */

public class HierarchyXML_Loader_UI {

    private Basic_Tree_Node mRootNode;
    private List<Rectangle> mNafNodes;
    private List<Node_UI> mNodeList;

    public HierarchyXML_Loader_UI() {
    }
    
    public Document getDocument(String filePath){
//    	Document document = null;
//    	File file = new File(filePath);
//        if (file.exists()) {
//            SAXReader saxReader = new SAXReader();
//            try {
//                document = saxReader.read(file);
//            } catch (DocumentException e) {    
//                System.out.println("文件加载异常：" + filePath);       
//                e.printStackTrace();
//            }
//        } else{
//            System.out.println("文件不存在 : " + filePath);
//        }  
//        return document;
    	Document dom = null;

    	try{
			StringBuffer content = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
			String line = null;
			while((line = br.readLine()) != null ) {
				content.append(line+"\n");
			}
			br.close();
			dom = DocumentHelper.parseText(content.toString());
    	}catch(Exception e){
    		System.out.println("File loading error：" + filePath);
    		e.printStackTrace();
    	}
    	
    	return dom;
    }
    
    @SuppressWarnings("unchecked")
    public static List<Element> getElementObjects(Document document,String elementPath) {
        return document.selectNodes(elementPath);
    }

    /**
     * Uses a SAX parser to process XML dump
     * @param xmlPath
     * @return
     */
    public Basic_Tree_Node parseXml(String xmlPath) {
        mRootNode = null;
        mNafNodes = new ArrayList<Rectangle>();
        mNodeList = new ArrayList<Node_UI>();
        // standard boilerplate to get a SAX parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }

        DefaultHandler handler = new DefaultHandler(){
        	
        	Basic_Tree_Node mParentNode;
        	Basic_Tree_Node mWorkingNode;
            Node_UI temp;
            @Override
            public void startElement(String uri, String localName, String qName,
                    Attributes attributes) throws SAXException {
                
            	
                boolean nodeCreated = false;
                // starting an element implies that the element that has not yet been closed
                // will be the parent of the element that is being started here
                mParentNode = mWorkingNode;

                if ("hierarchy".equals(qName)) {
                    int rotation = 0;
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if ("rotation".equals(attributes.getQName(i))) {
                            try {
                                rotation = Integer.parseInt(attributes.getValue(i));
                            } catch (NumberFormatException nfe) {
                                // do nothing
                            }
                        }
                    }
                    mWorkingNode = new Root_Window_Node(attributes.getValue("windowName"), rotation);
                    nodeCreated = true;
                } else if ("node".equals(qName)) {
                    Node_UI tmpNode = new Node_UI();
                    for (int i = 0; i < attributes.getLength(); i++) {
                        tmpNode.addAtrribute(attributes.getQName(i), attributes.getValue(i));
                    }
                    
                    tmpNode.addAtrribute("xpath",tmpNode.getXpath());
                    mWorkingNode = tmpNode;
                    nodeCreated = true;
                    // check if current node is NAF
                    String naf = tmpNode.getAttribute("NAF");
                    if ("true".equals(naf)) {
                        mNafNodes.add(new Rectangle(tmpNode.x, tmpNode.y,
                                tmpNode.width, tmpNode.height));
                    }
                }
                // nodeCreated will be false if the element started is neither
                // "hierarchy" nor "node"
                if (nodeCreated) {
                    if (mRootNode == null) {
                        // this will only happen once
                        mRootNode = mWorkingNode;
                    }
                    if (mParentNode != null) {
                        mParentNode.addChild(mWorkingNode);
                        if(mWorkingNode.getParent()!=null){
                        	String fullIndexXpath;
                        	String PfullIndexXpath;
                        	String xpath;
//                        	String xpath2 = null;
                        	String uiaStr = null;
                        	Basic_Tree_Node  parent = null;
                        	Node_UI myNode = null;
                    		myNode = (Node_UI)mWorkingNode;
                    		parent = mWorkingNode.getParent();
                        	if(!Root_Window_Node.class.isInstance(parent)){
                        		xpath = "/" + myNode.getXpath();
                        		PfullIndexXpath = ((Node_UI)parent).getAttribute("fullIndexXpath");
                        		fullIndexXpath = PfullIndexXpath +myNode.getIndexXpath();
                        		
//                        		String pXpath2 = ((UiNode)parent).getAttribute("xpath2");
//                        		xpath2 = pXpath2 + myNode.getXpath2();
//                        		System.out.println(xpath);
                        		List<Element> list = HierarchyXML_Loader_UI.getElementObjects(Utils_Constants.document, xpath.replaceAll("\\\\\"", "\""));
                        		if(list.size()>1){
                        			xpath = getXpathByParent(mWorkingNode);
                        		}else{
                        			uiaStr = getUiSelector(myNode);
                        		}
                        	}else{
//                        		e.printStackTrace();
                        		fullIndexXpath = "/"+ myNode.getIndexXpath();
                        		xpath = "/" + myNode.getXpath();
//                        		xpath2 = xpath;
                        	}
//                    		myNode.addAtrribute("xpath2",xpath2);
                        	myNode.addAtrribute("fullIndexXpath",fullIndexXpath);
                        	myNode.addAtrribute("xpath",xpath);
                    		myNode.addAtrribute("uiaSelector",uiaStr);
                  
//                        	System.out.println(uiaStr);
                        }
                        temp = (Node_UI)mWorkingNode;
                        mNodeList.add(temp);
                    }
                }
            }
            public String getXpathByParent(Basic_Tree_Node mWorkingNode){
            	
            	//the current node cannot be root when this called
            	if(Root_Window_Node.class.isInstance(mWorkingNode)){
            		return "";
            	}else{
            		Node_UI thisNode = (Node_UI)mWorkingNode;
            		String thisIndexPath = thisNode.getIndexXpath();
            		String xpath = thisIndexPath;
            		Basic_Tree_Node parent;
            		parent = mWorkingNode.getParent();
            		while(true){
            			String xpathTemp = "";
            			if(Root_Window_Node.class.isInstance(parent)){
            				break;
            			}else{
            				xpathTemp = "/"+((Node_UI)parent).getXpath()+xpath;
            				if(HierarchyXML_Loader_UI.getElementObjects(Utils_Constants.document, xpathTemp.replaceAll("\\\\\"", "\"")).size()==1){
            					return xpathTemp;
            				}else{
            					xpath = ((Node_UI)parent).getIndexXpath()+xpath;
            				}
            			}
            			parent = parent.getParent();
            		}
            		return "/"+xpath;
            	}
            	
            }
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                //mParentNode should never be null here in a well formed XML
                if (mParentNode != null) {
                    // closing an element implies that we are back to working on
                    // the parent node of the element just closed, i.e. continue to
                    // parse more child nodes
                    mWorkingNode = mParentNode;
                    mParentNode = mParentNode.getParent();
                }
            }
        };
        
        try {
            parser.parse(new File(xmlPath), handler);
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return mRootNode;
    }
    
    public static String getUiSelector(Node_UI node){
    	String uiSelector = null;
    	uiSelector = "new UiSelector().className(\\\""+ node.getAttribute("class") + "\\\")";
    	String text = node.getAttribute("text");
    	if(text!=null && !text.equals("")){
			text = text.replaceAll("'", "\\\\'");
			text = text.replaceAll("\"", "\\\\\"");
    		uiSelector = uiSelector + ".textContains(\\\""+ text + "\\\")";
    	}
    	if(node.getAttribute("index")!=null && !node.getAttribute("index").equals("")){
//    		uiSelector = uiSelector + ".index("+ node.getAttribute("index") + ")";
    	}
    	if(node.getAttribute("resource-id")!=null && !node.getAttribute("resource-id").equals("")){
    		uiSelector = uiSelector + ".resourceId(\\\""+ node.getAttribute("resource-id") + "\\\")";
    	}
    	return uiSelector;
    }

    /**
     * Returns the list of "Not Accessibility Friendly" nodes found during parsing.
     *
     * Call this function after parsing
     *
     * @return
     */
    public List<Rectangle> getNafNodes() {
        return Collections.unmodifiableList(mNafNodes);
    }

    public List<Node_UI> getAllNodes(){
        return mNodeList;
    }
}

