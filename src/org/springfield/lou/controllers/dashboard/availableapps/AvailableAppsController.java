package org.springfield.lou.controllers.dashboard.availableapps;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class AvailableAppsController extends Html5Controller {

	
	public AvailableAppsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/availapps/availapps.css");
		fillPage();
 		screen.get(".availappsubmit").on("mouseup","onShow", this);

  	}
	
    public void onShow(Screen s,JSONObject data) {
    	String id = (String)data.get("id");
    	id = id.substring(id.indexOf("_")+1);
    	model.setProperty("/screen/appname",id);
    	screen.get("#screen").append("div", "appdetails", new AppDetailsController());
    }
	
	private void fillPage() {
		FSList list = ApplicationManager.instance().getAvailableApplicationsList();
		List<FsNode> nodes = list.getNodesSorted("id","DOWN");
		JSONObject data = list.toJSONObject(screen.getLanguageCode(),"id,versioncount,productionversion,developmentversion,status");
    	screen.get(selector).parsehtml(data);
	}
	
	
}
