package org.springfield.lou.controllers.dashboard.usermanagement;

import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5Application;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.controllers.dashboard.availableapps.AppDetailsController;
import org.springfield.lou.screen.Screen;
import org.springfield.lou.user.User;
import org.springfield.lou.user.UserManager;

public class UserManagementController extends Html5Controller {
	
	public UserManagementController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/usermanagement/usermanagement.css");
		fillPage("*");
 		screen.get(".usermanagementsubmit").on("mouseup","onShow", this);
 		screen.get("#usermanagementsearchkey").on("change","onSearchkeyChange", this);
  	}
	
    public void onShow(Screen s,JSONObject data) {
    	String id = (String)data.get("id");
    	System.out.println("ID="+id);
    	model.setProperty("/screen/nodeid",id);
    	screen.get("#usermanagementdetails").remove();
    	screen.get("#usermanagement").append("div", "usermanagementdetails", new UserManagementDetailsController());
    }
    
    public void onSearchkeyChange(Screen s,JSONObject data) {
    	String value = (String)data.get("usermanagementsearchkey.value"); // will change to value only soon
		fillPage(value);
 		screen.get(".usermanagementsubmit").on("mouseup","onShow", this);
 		screen.get("#usermanagementsearchkey").on("change","onSearchkeyChange", this);
    }
	
	private void fillPage(String searchkey) {
		JSONObject data;
		List<FsNode> nodes;
		if (!searchkey.equals("*")) {
			FSList list = FSListManager.get("/domain/"+screen.getApplication().getDomain()+"/user",true);
			nodes = list.getNodesByIdMatch(searchkey);
		} else {
			FSList list = FSListManager.get("/domain/"+screen.getApplication().getDomain()+"/user",true);
			nodes = list.getNodesSorted("firstname","DOWN");
		}
		data = FSList.ArrayToJSONObject(nodes,screen.getLanguageCode(),"");
		data.put("searchkey",searchkey);
		data.put("domain", screen.getApplication().getDomain());
		screen.get(selector).parsehtml(data);
	}
	
}
