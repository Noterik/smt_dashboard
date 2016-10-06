package org.springfield.lou.controllers.dashboard.usermanagement;

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

public class UserManagementDetailsController extends Html5Controller {
	
	public UserManagementDetailsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/usermanagement/usermanagementdetails/usermanagementdetails.css");
		fillPage();
 		screen.get(".usermanagementdetailssubmit").on("change","onSave", this);
 		screen.get(".usermanagementdetailssubmit").on("keypress","onKey", this);
 		screen.get(".usermanagementdetailssave").on("mouseup","onSave", this);
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
   		String id = model.getProperty("/screen/nodeid");
		if (fieldname.equals("password")) {
			System.out.println("SETTING PASSWORD");
			ServiceInterface barney = ServiceManager.getService("barney");
			if (barney!=null) {
				System.out.println("Barney interface = "+barney);			
				String result = barney.put("setpassword("+screen.getApplication().getDomain()+","+id+")", value, null);
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+id+"/account/default/"+fieldname,"$shadow");
				System.out.println("ReSULT="+result);
			}
		} else {
			model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+id+"/account/default/"+fieldname, value);
		}
   		screen.get("#"+fieldname+"save").css("visibility","hidden");
    }
    
 	
	private void fillPage() {
    		String id = model.getProperty("/screen/nodeid");
    		FsNode node = Fs.getNode("/domain/"+screen.getApplication().getDomain()+"/user/"+id+"/account/default");
    		if (node!=null) {
    			JSONObject data = node.toJSONObject("en","firstname,lastname,email,phonenum,birthdata,state,role");
    			data.put("domain", screen.getApplication().getDomain());
    			data.put("id",id);
    			data.put("password","");
    			screen.get(selector).parsehtml(data);
    		}
	}
	
}
