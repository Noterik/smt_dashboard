package org.springfield.lou.controllers.dashboard.memorymanagement;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.controllers.dashboard.availableapps.AppDetailsController;
import org.springfield.lou.model.ModelEventManager;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class MemoryManagmentController extends Html5Controller {

	
	public MemoryManagmentController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/memorymanagement/memorymanagement.css");
		fillPage();
 		screen.get("#memorymanagement_propertysubmit").on("mouseup","onPropertyShow", this);
 		screen.get("#memorymanagement_propertiessubmit").on("mouseup","onPropertiesShow", this);
 		screen.get("#memorymanagement_notifysubmit").on("mouseup","onNotifyShow", this);
 		screen.get("#memorymanagement_timelinenotifysubmit").on("mouseup","onTimeLineNotifyShow", this);
 		screen.get("#memorymanagement_pathsubmit").on("mouseup","onPathShow", this);
  	}
	
    public void onPropertyShow(Screen s,JSONObject data) {
    	model.setProperty("/screen/bindtype","property");
    	screen.get("#screen").append("div", "fsbinds", new FsBindsController());
    }

    public void onPropertiesShow(Screen s,JSONObject data) {
    	model.setProperty("/screen/bindtype","properties");
    	screen.get("#screen").append("div", "fsbinds", new FsBindsController());
    }
    
    public void onPathShow(Screen s,JSONObject data) {
    	model.setProperty("/screen/bindtype","path");
    	screen.get("#screen").append("div", "fsbinds", new FsBindsController());
    }
	
    public void onNotifyShow(Screen s,JSONObject data) {
    	model.setProperty("/screen/bindtype","notify");
    	screen.get("#screen").append("div", "fsbinds", new FsBindsController());
    }
    
    public void onTimeLineNotifyShow(Screen s,JSONObject data) {
    	model.setProperty("/screen/bindtype","timelinenotify");
    	screen.get("#screen").append("div", "fsbinds", new FsBindsController());
    }
    
	private void fillPage() {
		FSList list = ApplicationManager.instance().getOpenApplicationsList();
//		JSONObject data = list.toJSONObject(screen.getLanguageCode(),"id,screencount,screenidcount,usercount");
		JSONObject data = new JSONObject();
		Runtime r =  Runtime.getRuntime();
		data.put("freemem",r.freeMemory()/(1024*1024));
		data.put("totalmem",r.totalMemory()/(1024*1024));
		data.put("maxmem",r.maxMemory()/(1024*1024));
		data.put("usedmem",(r.totalMemory()-r.freeMemory())/(1024*1024));
		
		ModelEventManager mm = model.getEventManager();
		data.put("totalbinds",mm.getTotalBindsCount());
		data.put("propertybinds",mm.getPropertyBindsCount());
		data.put("propertiesbinds",mm.getPropertiesBindsCount());
		data.put("notifybinds",mm.getNotifyBindsCount());
		data.put("timelinenotifybinds",mm.getTimeLineNotifyBindsCount());
		data.put("pathbinds",mm.getPathBindsCount());
		screen.get(selector).parsehtml(data);
	}

	
	
}
