package org.springfield.lou.controllers.dashboard.login;

import java.util.Date;

import org.json.simple.JSONObject;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class LoginController extends Html5Controller {

	
	public LoginController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("login/login.css");
    	JSONObject data = new JSONObject();	
    	data.put("feedback", "");
    	screen.get(selector).parsehtml(data);
    	screen.get("#loginsubmitbutton").on("mouseup","loginname,loginpassword","checkName",this);
  	}
	
	
	
    public void checkName(Screen s,JSONObject data) {
 		String name = (String)data.get("loginname");
		String password = (String)data.get("loginpassword");
		
		ServiceInterface barney = ServiceManager.getService("barney");
		System.out.println("BARNEY="+barney);
		if (barney!=null) {
			String ticket = barney.get("login("+s.getApplication().getDomain()+","+name+","+password+")", null, null);
			if (!ticket.equals("-1")) {
				model.setProperty("/screen/username", name);
		    	screen.get(selector).html("logged in : "+name);
			} else {
				screen.get("#feedback").html("wrong account or password");
			}
		}

    }
}
