package org.springfield.lou.controllers.dashboard.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.controllers.dashboard.availableapps.AppDetailsController;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class ServicesController extends Html5Controller {

	
	public ServicesController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/services/services.css");
		fillPage();
  	}
	
	private void fillPage() {
		FSList list = FSListManager.get("/domain/internal/service",false);
		List<FsNode> nodes = list.getNodes();
		FSList resultlist = new FSList();
		if (nodes!=null) {
 			for(Iterator<FsNode> iter = nodes.iterator() ; iter.hasNext(); ) {
 				
 				FsNode node = (FsNode)iter.next();
 				FsNode rnode = new FsNode(node.getName(),node.getId());
 				rnode.setProperty("name",node.getId());
 				String info = node.getProperty("info");
 				if (info!=null) rnode.setProperty("description",info);
 				// get the subnodes since we need it :( 				
 				FSList nodeslist = FSListManager.get("/domain/internal/service/"+node.getId()+"/nodes",false);
 				List<FsNode> nodes2 = nodeslist.getNodes();
 				if (nodes2!=null) {
 					rnode.setProperty("numberofnodes", ""+nodes2.size());
 					int up = 0;
 					for(Iterator<FsNode> iter2 = nodes2.iterator() ; iter2.hasNext(); ) {
 		 				FsNode nnode = (FsNode)iter2.next();
 		 				if (isUp(nnode)) {
 		 					up++;
 		 				}
 					}
 					rnode.setProperty("available",getPerc(nodes2.size(),up));	
 				} else {
 					rnode.setProperty("numberofnodes","0");
 				}
 				
 				
 				resultlist.addNode(rnode);
 			}
 				
			JSONObject data = resultlist.toJSONObject(screen.getLanguageCode(),"name,numberofnodes,available,description");
			screen.get(selector).parsehtml(data);
	 		screen.get(".showbutton").on("mouseup","onShowNode", this);
		}
	}
	
    public void onShowNode(Screen s,JSONObject data) {
    	String id = (String)data.get("id");
    	model.setProperty("/screen/servicename",id);
    	screen.get("#screen").append("div", "servicesdetails", new ServicesDetailsController());
    }
	
	private String getPerc(int all,int up) {
		if (up==0) return "0%";
		float val = (up/all)*100;
		return (""+(int)val+"%");
		
	}
	
	private boolean isUp(FsNode node) {
		//System.out.println("isUp "+node.asXML());
		return true;
	}
	
	
}