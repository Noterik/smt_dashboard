package org.springfield.lou.controllers.dashboard.login;

import java.util.Date;

import org.json.simple.JSONObject;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.homer.LazyHomer;
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
		
		// check for admin override
		if (1==1) {
			if (name.equals("admin") && LazyHomer.getDashboardPassword()!=null && password.equals(LazyHomer.getDashboardPassword())) {
				model.setProperty("/screen/username", name);
				screen.onNewUser(name);
				screen.get(selector).html("Logged in as : "+name);
				screen.get(selector).css("width","170px");
				return;
			}
		}
		
		ServiceInterface barney = ServiceManager.getService("barney");
		if (barney!=null) {
			String ticket = barney.get("login("+s.getApplication().getDomain()+","+name+","+password+")", null, null);
			if (!ticket.equals("-1")) {
				if (name.equals("admin")) {
					model.setProperty("/screen/username", name);
					screen.onNewUser(name);
					screen.get(selector).html("Logged in as : "+name);
					screen.get(selector).css("width","170px");
				} else {
					screen.get("#feedback").html("sorry tools only for user admin");
				}
			} else {
				screen.get("#feedback").html("wrong account or password");
			}
		}

    }
}
