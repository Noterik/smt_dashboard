package org.springfield.lou.controllers.dashboard.explorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.model.ModelEvent;
import org.springfield.lou.screen.Screen;

import usermanagement.UserManagementDetailsController;


public class ExplorerController extends Html5Controller {

		String path;
		
		public ExplorerController() {
			
		}
		
		public void attach(String s) {
			selector = s;
			
			// some test data
			FsNode node = new FsNode("video","1");
			node.setProperty("value","5");
			node.setProperty("time",new Date().toGMTString());	
			model.putNode("/shared/bla/user/daniel", node);
			
			node = new FsNode("video","2");
			node.setProperty("value","10");
			node.setProperty("time",new Date().toGMTString());	
			model.putNode("/shared/bla/user/daniel", node);
			
			node = new FsNode("audio","1");
			node.setProperty("value","14");
			node.setProperty("time",new Date().toGMTString());	
			model.putNode("/shared/bla/user/daniel", node);
			
			System.out.println("TEST GETPROP2="+model.getProperty("/shared/bla/user/daniel/video/1/time"));
			
		// path = "/domain/"+screen.getApplication().getDomain();
			path = "/";
			screen.loadStyleSheet("dashboard/explorer/explorer.css");
	    	JSONObject data = new JSONObject();	
			model.onPropertyUpdate("/screen/explorerpath","onPathChange", this);
			model.setProperty("/screen/explorerpath",path); // results in a fillPage !
	  	}
		
		private void fillPage(String searchkey) {
			System.out.println("FILLPAGE="+path);
			if (path.equals("/")) {
				fillFromRoot(searchkey);
			} else if (path.indexOf("/shared")!=-1) {
				fillFromModelNew(searchkey);
			} else {
				fillFromModel(searchkey);
			}
		}
	
		private void fillFromRoot(String searchkey) {
			JSONObject data;
			FSList rootlist = new FSList();
			FsNode node = new FsNode("root","1");
			node.setProperty("name","app");
			rootlist.addNode(node);
			node = new FsNode("root","2");
			node.setProperty("name","domain");
			rootlist.addNode(node);
			node = new FsNode("root","3");
			node.setProperty("name","session");
			rootlist.addNode(node);
			node = new FsNode("root","4");
			node.setProperty("name","shared");
			rootlist.addNode(node);
			
			data = FSList.ArrayToJSONObject(rootlist.getNodes(),screen.getLanguageCode(),"name");
			data.put("searchkey",searchkey);
			screen.get(selector).parsehtml(data);
	 		screen.get(".explorersubmit").on("mouseup","onShow", this);
	 		screen.get(".explorerpathsubmit").on("mouseup","onPathChange", this);
		}
		
		private void fillFromModelNew(String searchkey) {
			JSONObject data;
			List<FsNode> nodes;

			FSList list = screen.getModel().getList(path);
			System.out.println("FILLNEW1="+path+" L="+list);
	
			nodes = list.getNodes();
			
			
			if (!model.isMainNode(path)) {
				List<String> dirs = new ArrayList<String>();
				FSList mainlist = new FSList();
				for (int i=0;i<nodes.size();i++) {
					FsNode node = nodes.get(i);
					if (!dirs.contains(node.getName())) {
						FsNode mnode = new FsNode(node.getName(),node.getId());
						mnode.setProperty("name",node.getName());
						mainlist.addNode(mnode);
						dirs.add(node.getName());
					}
				}
				nodes = mainlist.getNodesSorted("name","up");
			} else {
				List<String> dirs = new ArrayList<String>();
				FSList mainlist = new FSList();
				for (int i=0;i<nodes.size();i++) {
					FsNode node = nodes.get(i);
						FsNode mnode = new FsNode(node.getName(),node.getId());
						mnode.setProperty("name",node.getId());
						mainlist.addNode(mnode);
				}
				nodes = mainlist.getNodesSorted("name","up");
			}
			
			data = FSList.ArrayToJSONObject(nodes,screen.getLanguageCode(),"name");
			data.put("searchkey",searchkey);
			
			JSONArray pe = new JSONArray();
			data.put("pathelements",pe);
			String[] pel = path.split("/");
			for (int i=0;i<pel.length;i++) {
				JSONObject n = new JSONObject();
				n.put("element",pel[i]);
				if (i!=0) n.put("divider","/");
				pe.add(n);
			}
			
			// show create node
			data.put("createnode","true");
			
			screen.get(selector).parsehtml(data);
	 		screen.get(".explorersubmit").on("mouseup","onShow", this);
	 		screen.get("#explorercreatenode").on("mouseup","explorernewname","onCreateNode", this);
	 		screen.get(".explorerpathsubmit").on("mouseup","onPathChange", this);
	 		
	    	if (!model.isMainNode(path)) {
	    		screen.get("#explorer").append("div", "explorerdetails", new ExplorerDetailsController());
	    	}
		}

		
		
		private void fillFromModel(String searchkey) {
			JSONObject data;
			List<FsNode> nodes;
			if (!searchkey.equals("*")) {
				FSList list = FSListManager.get(path,false);
				nodes = list.getNodes();
			} else {
				FSList list = FSListManager.get(path,false);
				nodes = list.getNodes();
			}
			
			if (!model.isMainNode(path)) {
				List<String> dirs = new ArrayList<String>();
				FSList mainlist = new FSList();
				for (int i=0;i<nodes.size();i++) {
					FsNode node = nodes.get(i);
					if (!dirs.contains(node.getName())) {
						FsNode mnode = new FsNode(node.getName(),node.getId());
						mnode.setProperty("name",node.getName());
						mainlist.addNode(mnode);
						dirs.add(node.getName());
					}
				}
				nodes = mainlist.getNodesSorted("name","up");
			} else {
				List<String> dirs = new ArrayList<String>();
				FSList mainlist = new FSList();
				for (int i=0;i<nodes.size();i++) {
					FsNode node = nodes.get(i);
						FsNode mnode = new FsNode(node.getName(),node.getId());
						mnode.setProperty("name",node.getId());
						mainlist.addNode(mnode);
				}
				nodes = mainlist.getNodesSorted("name","up");
			}
			
			data = FSList.ArrayToJSONObject(nodes,screen.getLanguageCode(),"name");
			data.put("searchkey",searchkey);
			
			JSONArray pe = new JSONArray();
			data.put("pathelements",pe);
			String[] pel = path.split("/");
			for (int i=0;i<pel.length;i++) {
				JSONObject n = new JSONObject();
				n.put("element",pel[i]);
				if (i!=0) n.put("divider","/");
				pe.add(n);
			}
			
			// show create node
			data.put("createnode","true");
			
			screen.get(selector).parsehtml(data);
	 		screen.get(".explorersubmit").on("mouseup","onShow", this);
	 		screen.get("#explorercreatenode").on("mouseup","explorernewname","onCreateNode", this);
	 		screen.get(".explorerpathsubmit").on("mouseup","onPathChange", this);
	 		
	    	if (!model.isMainNode(path)) {
	    		screen.get("#explorer").append("div", "explorerdetails", new ExplorerDetailsController());
	    	}
		}
		
	    public void onPathChange(Screen s,JSONObject data) {
	    	String id = (String)data.get("id");
	    	if (id.equals("/")) {
	    		path = "/";
		    	model.setProperty("/screen/explorerpath",path);
		    	return;
	    	}
	    	
	    	int pos = path.indexOf("/"+id);
	    	if (pos!=-1) {
	    		path = path.substring(0,pos)+"/"+id;
	    	}
	    	model.setProperty("/screen/explorerpath",path);
	    }
	    
	    public void onCreateNode(Screen s,JSONObject data) {
	    	String name = (String)data.get("explorernewname");
	    	if (name==null && name.equals("")) return;
	    	
	    	if (model.isMainNode(path)) {
	    		System.out.println("MADE NODE INSERT EXPLORER");
	    		String type = path.substring(path.lastIndexOf("/")+1);
	    		String shortpath = path.substring(0,path.lastIndexOf("/"));
	    		FsNode node = new FsNode(type,name);
	    		if (model.insertNode(node,shortpath)) {
	    			fillPage("*");
	    		}
	    	} else {
	    		System.out.println("SUBNODE INSERT NOT IMPLEMETED IN EXPLORER");
	    		
	    		String type = path.substring(path.lastIndexOf("/")+1);
	    
	    		FsNode node = new FsNode(name,"1"); // default name to 1
	    		System.out.println("path="+path+" type="+name);
	    		if (model.insertNode(node,path)) {
	    			fillPage("*");
	    		}
	    	}
	    }
		
	    public void onShow(Screen s,JSONObject data) {
	    	String id = (String)data.get("id");
	    	if (path.equals("/")) {
	    		path = "/"+id;
	    	} else {
	    		path +="/"+id;
	    	}
	    	model.setProperty("/screen/explorerpath",path);
	    }
	    
	    public void onPathChange(ModelEvent e) {
	    	path = e.getTargetFsNode().getProperty("explorerpath");
	    	fillPage("*");
	    }

}
