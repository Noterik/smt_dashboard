package org.springfield.lou.controllers.dashboard.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
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

public class ServicesDetailsController extends Html5Controller {

	
	public ServicesDetailsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/services/servicesdetails/servicesdetails.css");
		fillPage();
 		//screen.get("#appdetails_done").on("mouseup","onClose", this);
 		//screen.get("#appdetails_select").on("change","onSelectChange", this);
 		//screen.get(".appdetails_develsubmit").on("mouseup","onDevelSubmit", this);
 
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
		String servicename = model.getProperty("/screen/servicename");

		FSList resultlist = new FSList();
		
		FSList nodeslist = FSListManager.get("/domain/internal/service/"+servicename+"/nodes",false);
		List<FsNode> nodes = nodeslist.getNodes();
		if (nodes!=null) {
	
 			for(Iterator<FsNode> iter = nodes.iterator() ; iter.hasNext(); ) {
 				
 				FsNode node = (FsNode)iter.next();
 				FsNode rnode = new FsNode();
 				rnode.setProperty("name",node.getProperty("name"));
 				rnode.setProperty("lastseen",node.getProperty("lastseen"));
 				rnode.setProperty("status",node.getProperty("status"));
 				rnode.setProperty("debuglevel",node.getProperty("defaultloglevel"));
				rnode.setProperty("smithers",node.getProperty("activesmithers"));
 				rnode.setProperty("ipnumber",node.getId());
 				resultlist.addNode(rnode);
 			}
		}
		
		JSONObject data = resultlist.toJSONObject(screen.getLanguageCode(),"name,ipnumber,status,lastseen,smithers,debuglevel");
		data.put("numberofnodes",nodes.size());
		data.put("available","100%");
		screen.get(selector).parsehtml(data);
 		screen.get("#servicesdetails_done").on("mouseup","onClose", this);
 		//screen.get(".servicesdetails_develsubmit").on("mouseup","onDevelSubmit", this);
	}

	
}
