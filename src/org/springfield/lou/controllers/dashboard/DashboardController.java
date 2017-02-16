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
import org.springfield.lou.controllers.dashboard.availableapps.AvailableAppsController;
import org.springfield.lou.controllers.dashboard.debugger.DebuggerController;
import org.springfield.lou.controllers.dashboard.explorer.ExplorerController;
import org.springfield.lou.controllers.dashboard.login.LoginController;
import org.springfield.lou.controllers.dashboard.memorymanagement.MemoryManagmentController;
import org.springfield.lou.controllers.dashboard.openapps.OpenAppsController;
import org.springfield.lou.controllers.dashboard.performancemanagement.PerformanceManagmentController;
import org.springfield.lou.controllers.dashboard.services.ServicesController;
import org.springfield.lou.controllers.dashboard.usermanagement.UserManagementController;
import org.springfield.lou.model.ModelEvent;
import org.springfield.lou.screen.Screen;

public class DashboardController extends Html5Controller {
	
	private String selected;
	private String username=null;

	public DashboardController() {
	}
	
	public void attach(String sel) {
		selector = sel;
		screen.loadStyleSheet("dashboard/dashboard.css");
		fillPage();
		model.onPropertyUpdate("/screen/username","onLogin",this);
		model.onPropertyUpdate("/shared['test']/title","onHashCode",this);
	}
	
	 public void onHashCode(ModelEvent e) {
		 System.out.println("ME2="+e.getTargetFsNode().asXML());
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
 		if (username!=null) {
 			screen.get("#dashboard_services").on("mouseup","selectServices",this);
 			screen.get("#dashboard_openapps").on("mouseup","selectOpenApps",this);
 			screen.get("#dashboard_availapps").on("mouseup","selectAvailApps",this);
 			screen.get("#dashboard_usermanagement").on("mouseup","selectUserManagement",this);
 			screen.get("#dashboard_memorymanagement").on("mouseup","selectMemoryManagement",this);
 			screen.get("#dashboard_performancemanagement").on("mouseup","selectPerformanceManagement",this);
 			screen.get("#dashboard_explorer").on("mouseup","selectExplorer",this);
 			screen.get("#dashboard_debugger").on("mouseup","selectDebugger",this);
 		}
	}
	
	public void onLogin(ModelEvent e) {
		FsNode node = e.getTargetFsNode();
		username = node.getProperty("username");
		selected = "openapps";
		fillPage();
		screen.get("#dashboard").append("div","openapps",new OpenAppsController());	
	}
	
	
	public void selectExplorer(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "explorer";
		fillPage();
		s.get("#dashboard").append("div","explorer",new ExplorerController());	
	}
	
	public void selectServices(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "services";
		fillPage();
		s.get("#dashboard").append("div","services",new ServicesController());	
	}
	
	public void selectOpenApps(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "openapps";
		fillPage();
		s.get("#dashboard").append("div","openapps",new OpenAppsController());	
	}
	
	public void selectUserManagement(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "usermanagement";
		fillPage();
		s.get("#dashboard").append("div","usermanagement",new UserManagementController());	
	}
	
	public void selectMemoryManagement(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "memorymanagement";
		fillPage();
		s.get("#dashboard").append("div","memorymanagement",new MemoryManagmentController());
	}
	
	public void selectPerformanceManagement(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "performancemanagement";
		fillPage();
		s.get("#dashboard").append("div","performancemanagement",new PerformanceManagmentController());
	}
	
	public void selectAvailApps(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "availapps";
		fillPage();
		s.get("#dashboard").append("div","availapps",new AvailableAppsController());
	}
	
	
	public void selectDebugger(Screen s,JSONObject data) {
		if (selected!=null) s.removeContent(selected);
		selected = "debugger";
		fillPage();
		s.get("#dashboard").append("div","debugger",new DebuggerController());
		
	}
 	 
}
