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
 		screen.get(".explorerdetailssubmit").on("change","onSave", this);
 		//screen.get(".usermanagementdetailssubmit").on("keypress","onKey", this);
 		//screen.get(".usermanagementdetailssave").on("mouseup","onSave", this);
  	}
	
    public void onKey(Screen s,JSONObject data) {
    	String id = (String)data.get("id");
    	Long keycode = (Long)data.get("which");
    	if (keycode!=13) screen.get("#"+id+"save").css("visibility","visible");
    }
	
	
    public void onSave(Screen s,JSONObject data) {
    	System.out.println("SAVE SELECTED="+data.toJSONString());
    	String fieldname = (String)data.get("id");
    	String value = (String)data.get("value");
   		String path = model.getProperty("/screen/nodeid");
    	System.out.println("SAVE REAL PATH="+path+" N="+fieldname+" V="+value);
    	model.setProperty(path+"/"+fieldname, value);
   		screen.get("#"+fieldname+"save").css("visibility","hidden");
    }
    
 	
	private void fillPage() {
    		String id = model.getProperty("/screen/nodeid");
    		FsNode node = Fs.getNode(id);
    		if (node!=null) {
    			JSONObject data = node.toJSONObject("en","*");
    			screen.get(selector).parsehtml(data);
    		} else {
    			JSONObject data = new JSONObject();
       			screen.get(selector).parsehtml(data);   			
    		}
	}
	
}
