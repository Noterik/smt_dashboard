package org.springfield.lou.controllers.dashboard.performancemanagement;

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
import org.springfield.lou.performance.PerformanceManager;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class PerformanceManagmentController extends Html5Controller {

	
	public PerformanceManagmentController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/performancemanagement/performancemanagement.css");
		fillPage();
 		//screen.get("#memorymanagement_propertysubmit").on("mouseup","onPropertyShow", this);
 		//screen.get("#memorymanagement_propertiessubmit").on("mouseup","onPropertiesShow", this);
		screen.get(selector).draggable();
		screen.get(selector).track("mousemove","mouseMove", this);
  	}
		
	private void fillPage() {
		JSONObject data = new JSONObject();
		long tt = PerformanceManager.getTotalNetworkTime();
		if (tt<1000) {
			data.put("totalnetworktime",""+tt);
			data.put("totalnetworktimetype","milliseconds");
		} else {
			data.put("totalnetworktime",""+(tt/1000));
			data.put("totalnetworktimetype","seconds");
		}
		data.put("totalnetworkcalls",""+PerformanceManager.getTotalNetworkCalls());
		long at = PerformanceManager.getAvgNetworkCall();
		if (at<1000) {
			data.put("avgnetworkcall",""+at);
			data.put("avgnetworkcalltype","nanoseconds");
		} else {
			data.put("avgnetworkcall",""+(at/1000));
			data.put("avgnetworkcalltype","milliseconds");
		}
		screen.get(selector).parsehtml(data);
	}

	 public void mouseMove(Screen s,JSONObject data) {
		 // lets for fun parse the data again?
		 fillPage();
	 }
	
	
}
