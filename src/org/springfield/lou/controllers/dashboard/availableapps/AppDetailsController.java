package org.springfield.lou.controllers.dashboard.availableapps;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.application.Html5AvailableApplication;
import org.springfield.lou.application.Html5AvailableApplicationVersion;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class AppDetailsController extends Html5Controller {

	
	public AppDetailsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/availapps/appdetails/appdetails.css");
		fillPage();
 		screen.get("#appdetails_done").on("mouseup","onClose", this);
 		screen.get("#appdetails_select").on("change","onSelectChange", this);
 		screen.get(".appdetails_develsubmit").on("mouseup","onDevelSubmit", this);
 
  	}
	   public void onSelectChange(Screen s,JSONObject data) {
		   String value = (String)data.get("value");
			String appname = model.getProperty("/screen/appname");
		    ApplicationManager.instance().setAutoDeploy(appname, value);
	    }

	
    public void onClose(Screen s,JSONObject data) {
    	screen.removeContent(selector.substring(1));
    }

    public void onDevelSubmit(Screen s,JSONObject data) {
	    System.out.println("DEVEL SUBMIT="+data.toJSONString());
    }
    

	
	private void fillPage() {
		String appname = model.getProperty("/screen/appname");
		Html5AvailableApplication vapp = ApplicationManager.instance().getAvailableApplication(appname);
		
		Iterator<Html5AvailableApplicationVersion> it = vapp.getOrderedVersions();
		FSList list =new FSList();
		while(it.hasNext()){
			FsNode node = new FsNode();
			Html5AvailableApplicationVersion version = it.next();
			node.setProperty("id", vapp.getId());
			node.setProperty("version",version.getId());
			node.setProperty("synced","100%");
			node.setProperty("status",version.getStatus());
			list.addNode(node);
		}
		
		JSONObject data = list.toJSONObject(screen.getLanguageCode(),"version,synced,status");

		// add some values for the first table too;
		data.put("appname", appname);
		data.put("appid", vapp.getId());
		data.put("versions",vapp.getVersionsCount());
		data.put("autodeploy",vapp.getAutoDeploy());		
    	screen.get(selector).parsehtml(data);
	}
	
	
}
