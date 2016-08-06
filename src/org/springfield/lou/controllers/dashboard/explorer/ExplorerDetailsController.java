package org.springfield.lou.controllers.dashboard.explorer;

import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5Application;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.screen.Screen;
import org.springfield.lou.user.User;
import org.springfield.lou.user.UserManager;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class ExplorerDetailsController extends Html5Controller {
	
	public ExplorerDetailsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/explorer/explorerdetails/explorerdetails.css");
		fillPage();
 		screen.get(".explorerdetailssubmit").on("keypress","onKey", this);
  	}
	
    public void onKey(Screen s,JSONObject data) {
    	String id = (String)data.get("id");
    	Long keycode = (Long)data.get("which");
    	if (keycode!=13) screen.get("#"+id+"save").css("visibility","visible");
    }
	
	
    public void onSave(Screen s,JSONObject data) {
    	String fieldname = (String)data.get("id");
    	System.out.println("FIELDNAME="+fieldname);
    	String value = (String)data.get("value");
   		String path = model.getProperty("/screen/explorerpath");
    	model.setProperty(path+"/"+fieldname, value);
   		screen.get("#"+fieldname+"save").css("visibility","hidden");
    }
    
    public void onNewSave(Screen s,JSONObject data) {
    	String fieldname = (String)data.get("newname");
    	String value = (String)data.get("newvalue");
   		String path = model.getProperty("/screen/explorerpath");
    	model.setProperty(path+"/"+fieldname, value);
    	fillPage();
    }
    
 	
	private void fillPage() {
    		String id = model.getProperty("/screen/explorerpath");
			JSONObject data = new JSONObject();
    		FsNode node = Fs.getNode(id);
    		if (node!=null) {
    			data = node.toJSONObject("en","*");
    		}
    		data.put("id", id);
    		
			// do we want to delete the node ?
			if (model.getProperty("/screen/deleterequest")!=null && model.getProperty("/screen/deleterequest").equals("true")) {
				data.put("deleterequest","true");
				String dp = model.getProperty("/screen/deleteproperty");
				if (dp!=null && !dp.equals("")) data.put("deleteproperty",dp);
		    	model.setProperty("/screen/deleterequest","false"); // reset the flag
			}
   			screen.get(selector).parsehtml(data);       		
    		
     		screen.get(".explorerdetailssubmit").on("change","onSave", this);
       		screen.get(".explorerpropertydelete").on("mouseup","onDeletePropertyRequest", this);
       		screen.get(".explorerpropertydeleteno").on("mouseup","onDeletePropertyRequestNo", this);
       		screen.get(".explorerpropertydeleteyes").on("mouseup","onDeletePropertyRequestYes", this);
	 		screen.get("#explorerdetailsdelete").on("mouseup","onDeleteRequest", this);
	 		screen.get("#explorerdetailsdeleteyes").on("mouseup","onDeleteRequestYes", this);
	 		screen.get("#explorerdetailsdeleteno").on("mouseup","onDeleteRequestNo", this);
	 		screen.get("#explorerdetailsdelete").on("mouseup","onDeleteRequest", this);
	 		screen.get("#explorerdetailsdeleteyes").on("mouseup","onDeleteRequestYes", this);
	 		screen.get("#explorerdetailsdeleteno").on("mouseup","onDeleteRequestNo", this);
     		screen.get("#newsave").on("mouseup","newname,newvalue","onNewSave", this);
	}
	
    public void onDeletePropertyRequest(Screen s,JSONObject data) {
    	String fieldname = (String)data.get("id");
    	fieldname = fieldname.substring(0,fieldname.length()-6);
    	System.out.println("FIELDNAME="+fieldname);
    	model.setProperty("/screen/deleterequest","true");
    	model.setProperty("/screen/deleteproperty",fieldname);
    	fillPage();
    }
	
    public void onDeleteRequest(Screen s,JSONObject data) {
    	model.setProperty("/screen/deleterequest","true");
    	fillPage();
    }
    
    public void onDeleteRequestNo(Screen s,JSONObject data) {
    	fillPage();
    }
    
    public void onDeletePropertyRequestNo(Screen s,JSONObject data) {
    	fillPage();
    }
    
    public void onDeletePropertyRequestYes(Screen s,JSONObject data) {
    	String path = model.getProperty("/screen/explorerpath")+"/properties/"+model.getProperty("/screen/deleteproperty");
    	Fs.deleteNode(path); // we use delete node since properties are really nodes, not sure if we should build this in model
    	fillPage();
    }
    
    
    
    public void onDeleteRequestYes(Screen s,JSONObject data) {
		String path = model.getProperty("/screen/explorerpath");
		Fs.deleteNode(path);
		path = path.substring(0,path.lastIndexOf("/"));
    	model.setProperty("/screen/explorerpath",path); // should trigger a redraw of the parent
    }
	
}
