package org.springfield.lou.controllers.dashboard.memorymanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5ApplicationInterface;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.model.ModelBindObject;
import org.springfield.lou.model.ModelEventManager;
import org.springfield.lou.screen.Screen;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class FsBindsController extends Html5Controller {

	
	public FsBindsController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/memorymanagement/fsbinds/fsbinds.css");
		fillPage();
  	}
	
    public void onClose(Screen s,JSONObject data) {
    	screen.removeContent(selector.substring(1));
    }
    
    public void onReload(Screen s,JSONObject data) {
    	fillPage();
    }

	
	private void fillPage() {
//		FSList list = ApplicationManager.instance().getOpenApplicationsList();
//		JSONObject data = list.toJSONObject(screen.getLanguageCode(),"id,screencount,screenidcount,usercount");
		FSList list = new FSList();
		
		String bindtype = model.getProperty("/screen/bindtype");
		
		ModelEventManager mm = model.getEventManager();
		Map<String, ArrayList<ModelBindObject>> binds;
		if (bindtype.equals("property")) {
			binds = mm.getPropertyBinds();
		} else {
			binds = mm.getPropertiesBinds();	
		}
		
		Iterator<String> it = binds.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			List<ModelBindObject> l = (List)binds.get(key);
			for (int i=0;i<l.size();i++) {
				ModelBindObject bind = l.get(i);
				FsNode node = new FsNode("bind",key);
				node.setProperty("bindid", key);
				String oname = bind.obj.toString();
				oname = oname.substring(oname.lastIndexOf(".")+1);
				String sname = bind.screenid;
				sname = sname.substring(sname.lastIndexOf("/")+1);
				
				node.setProperty("obj",oname+","+sname);
				node.setProperty("bindtype",bindtype);
				node.setProperty("method",bind.method);
				list.addNode(node);
			}
		}
		JSONObject data = list.toJSONObject(screen.getLanguageCode(),"bindid,bindtype,obj,method");
		screen.get(selector).parsehtml(data);
		
 		screen.get("#fsbinds_closebutton").on("mouseup","onClose", this);
 		screen.get("#fsbinds_reloadbutton").on("mouseup","onReload", this);
	}

	
	
}
