package org.springfield.lou.controllers.dashboard.openapps;

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
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class OpenAppsController extends Html5Controller {

	
	public OpenAppsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/openapps/openapps.css");
		fillPage();
  	}
	
	private void fillPage() {
		FSList list = ApplicationManager.instance().getOpenApplicationsList();
		JSONObject data = list.toJSONObject(screen.getLanguageCode(),"id,screencount,screenidcount,usercount");
    	screen.get(selector).parsehtml(data);
	}
	
	
}
