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
import org.springfield.lou.application.Html5AvailableApplication;
import org.springfield.lou.application.Html5AvailableApplicationVersion;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class ServicesDetailsController extends Html5Controller {

	
	public ServicesDetailsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/services/servicesdetails/servicesdetails.css");
		fillPage();
 		screen.get(selector).on("keypress","onKey", this);
  	}
	
    public void onKey(Screen s,JSONObject data) {
    	String id = (String)data.get("id");
    	Long keycode = (Long)data.get("which");
    	if (keycode==27) {
    		closeEdit();
			fillPage();
    		
    	}
    }
    
	   public void onSelectChange(Screen s,JSONObject data) {
		   String value = (String)data.get("value");
			String appname = model.getProperty("/screen/appname");
		    ApplicationManager.instance().setAutoDeploy(appname, value);
	    }

	
    public void onClose(Screen s,JSONObject data) {
    	screen.removeContent(selector.substring(1));
    }

	
	private void fillPage() {
		String servicename = model.getProperty("/screen/servicename");

		FSList resultlist = new FSList();
		
		FSList nodeslist = FSListManager.get("/domain/internal/service/"+servicename+"/nodes",false);
		List<FsNode> nodes = nodeslist.getNodes();
		if (nodes!=null) {
	
 			for(Iterator<FsNode> iter = nodes.iterator() ; iter.hasNext(); ) {
 				
 				FsNode node = (FsNode)iter.next();
 				FsNode rnode = new FsNode();
 				rnode.setProperty("name",node.getProperty("name"));
 				rnode.setProperty("lastseen",node.getProperty("lastseen"));
 				rnode.setProperty("status",node.getProperty("status"));
 				rnode.setProperty("debuglevel",node.getProperty("defaultloglevel"));
				rnode.setProperty("smithers",node.getProperty("activesmithers"));
				rnode.setProperty("preferedsmithers",node.getProperty("preferedsmithers"));
 				rnode.setProperty("ipnumber",node.getId());
 				String editmode = model.getProperty("/screen/editmode");
 				if (editmode!=null && !editmode.equals("")) {
 	 				String ipnumber = model.getProperty("/screen/nodename");
 	 				if (ipnumber.equals(node.getId())) {
 	 					rnode.setProperty(editmode,"true");
 	 				}
 				}
 				resultlist.addNode(rnode);
 			}
		}
		
		JSONObject data = resultlist.toJSONObject(screen.getLanguageCode(),"name,ipnumber,status,lastseen,smithers,preferedsmithers,debuglevel,editname,editstatus,editsmithers,editdebuglevel");
		data.put("numberofnodes",nodes.size());
		data.put("available","100%");
		screen.get(selector).parsehtml(data);
 		screen.get("#servicesdetails_done").on("mouseup","onClose", this);
 		
 		// binds for starting editors
 		screen.get(".servicesdetails_editname").on("mouseup","onEditName", this);
 		screen.get(".servicesdetails_status").on("mousedown","onEditStatus", this);
 		screen.get(".servicesdetails_smithers").on("mouseup","onEditSmithers", this);
 		screen.get(".servicesdetails_debuglevel").on("mouseup","onEditDebugLevel", this);
 		
 		// binds for input from editors
 		screen.get(".servicesdetails_namechange").on("change","onNameChange", this);
 		screen.get(".servicesdetails_statuschange").on("change","onStatusChange", this);
 		screen.get(".servicesdetails_debuglevelchange").on("change","onDebugLevelChange", this);
	}
	
    public void onEditName(Screen s,JSONObject data) {
    	setEditMode(data,"editname");
    }
    
    public void onEditStatus(Screen s,JSONObject data) {
    	setEditMode(data,"editstatus");
    }
    
    public void onEditSmithers(Screen s,JSONObject data) {
    	setEditMode(data,"editsmithers");
    }
    
    public void onEditDebugLevel(Screen s,JSONObject data) {
    	setEditMode(data,"editdebuglevel");
    }
    
    public void onNameChange(Screen s,JSONObject data) {
		String servicename = model.getProperty("/screen/servicename");
		model.setProperty("/domain/internal/service/"+servicename+"/nodes/"+(String)data.get("id")+"/name",(String)data.get("value"));
    	closeEdit();
    	fillPage();
    }
    
    public void onStatusChange(Screen s,JSONObject data) {
		String servicename = model.getProperty("/screen/servicename");
		model.setProperty("/domain/internal/service/"+servicename+"/nodes/"+(String)data.get("id")+"/status",(String)data.get("value"));
    	closeEdit();
		fillPage();
    }
    
    public void onDebugLevelChange(Screen s,JSONObject data) {
		String servicename = model.getProperty("/screen/servicename");
		model.setProperty("/domain/internal/service/"+servicename+"/nodes/"+(String)data.get("id")+"/defaultloglevel",(String)data.get("value"));
    	closeEdit();
		fillPage();
    }
    
    private void closeEdit() {
		// cancel edit mode if needed, we should implement a removeProperty
		model.setProperty("/screen/editmode","");
		model.setProperty("/screen/nodename","");
    }
    
    private void setEditMode(JSONObject data,String mode) {
		String editmode = model.getProperty("/screen/editmode");
		if (editmode==null || editmode.equals("")) {
			model.setProperty("/screen/editmode",mode);
			model.setProperty("/screen/nodename",(String)data.get("id"));
			fillPage();
		}
    }

	
}
