package org.springfield.lou.controllers.dashboard;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.controllers.dashboard.debugger.DebuggerController;
import org.springfield.lou.controllers.dashboard.login.LoginController;
import org.springfield.lou.model.ModelEvent;
import org.springfield.lou.screen.Screen;

public class DashboardController extends Html5Controller {
	
	private String selected;
	private String username=null;

	public DashboardController() {
	}
	
	public void attach(String sel) {
		System.out.println("dashboard controller attached called");
		selector = sel;
		screen.loadStyleSheet("dashboard/dashboard.css");
		fillPage();
		model.onPropertyUpdate("/screen/username","onLogin",this);
	}
	
	private void fillPage() {
		JSONObject data = new JSONObject();
		if (selected!=null) {
			data.put(selected,"true");
		}
		if (username!=null) {
			data.put("username",username);
		}
 		screen.get(selector).parsehtml(data);
		screen.get("#dashboard_openapps").on("mouseup","selectOpenApps",this);
		screen.get("#dashboard_availapps").on("mouseup","selectAvailApps",this);
		screen.get("#dashboard_usermanagement").on("mouseup","selectUserManagement",this);
		screen.get("#dashboard_debugger").on("mouseup","selectDebugger",this);
	}
	
	public void onLogin(ModelEvent e) {
		FsNode node = e.getTargetFsNode();
		username = node.getProperty("username");
		System.out.println("LOGIN EVENT ! "+username);
		fillPage();
	}
	
	public void selectOpenApps(Screen s,JSONObject data) {
		selected = "openapps";
		fillPage();
	}
	
	public void selectAvailApps(Screen s,JSONObject data) {
		selected = "availapps";
		fillPage();
	}
	
	public void selectUserManagement(Screen s,JSONObject data) {
		selected = "usermanagement";
		fillPage();
	}
	
	public void selectDebugger(Screen s,JSONObject data) {
		selected = "debugger";
		fillPage();
		if (selected!=null) s.removeContent(selected);
		s.get("#dashboard").append("div","debugger",new DebuggerController());
		
	}
 	 
}
