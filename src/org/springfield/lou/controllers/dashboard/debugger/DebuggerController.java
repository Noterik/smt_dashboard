package org.springfield.lou.controllers.dashboard.debugger;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.screen.Screen;
import org.springfield.lou.screen.ScreenManager;

public class DebuggerController extends Html5Controller {
	
	List<String> buffer = new ArrayList<String>();
	int maxlines = 60;
	String currentpath = "/domain/mecanex/";
	String curline = "";
	private enum commandlist { ls,dir,cp,cat,put,pwd,cd,clear,quit,peek,poke,start,mkdir,rm,save,load,ln,filecomplete,history,next,prev,trace,dpost; }
	private String[] ignorelist = {"depth","start","limit","totalResultsAvailable","totalResultsReturned"};
	private String[] silentcommands = {"start","prev","next","history","filecomplete"};

	public DebuggerController() {
	}
	
	public void attach(String sel) {
		System.out.println("debugger controller attached called");
		selector = sel;
		screen.loadStyleSheet("dashboard/debugger/debugger.css");
		JSONObject data = new JSONObject();
		//data.put("location",model.getProperty("/screen/location"));
 		screen.get(selector).parsehtml(data);
 		
 		screen.get("#debugger_console").on("keypress","debugger_console","keyPressed", this);
 		
 		// lets say hi, by the start command
 		handleCommand("start");
	}
	
	private void handleCommand(String msg) {
		String[] cmds = msg.split(" ");
		String command = cmds[0];
		String newcommand = null;
		try {
			switch (commandlist.valueOf(command)) {
				case prev: newcommand = History.prev(); break;
				case next: newcommand = History.next(); break;
				case history: History.history(buffer,cmds); break;
				case filecomplete: newcommand = Filecomplete.execute(buffer, currentpath, cmds); break;
				case dir: Ls.execute(buffer, currentpath,cmds,ignorelist); break;
				case ls : Ls.execute(buffer, currentpath,cmds,ignorelist); break;
				case pwd : Pwd.execute(buffer, currentpath, cmds); break;
				case cd : currentpath = Cd.execute(buffer, currentpath, cmds); break;
				case dpost : Dpost.execute(buffer, currentpath, cmds); break;
				case cp : Cp.execute(buffer, currentpath, cmds); break;
				case cat : Cat.execute(buffer, currentpath, cmds); break;
				//case poke : Poke.execute(sm, buffer, from, cmds); break;
				case clear : buffer = Clear.execute(); break;
				case mkdir : Mkdir.execute(buffer, currentpath, cmds); break;
				case start : buffer = Start.execute();break;
				//case quit : Quit.execute(sm, buffer, from);break;
				case save : Save.execute(buffer, currentpath, cmds);break;
				case load : Load.execute(buffer, currentpath, cmds);break;
				case put : Put.execute(buffer, currentpath, cmds);break;
				case rm : Rm.execute(buffer, currentpath, cmds);break;
				case ln : Ln.execute(buffer, currentpath, cmds);break;
				//case trace : Trace.execute(buffer, this, cmds);break;
			} 
		} catch(Exception e) {
			buffer.add("> illegal command !");	
			e.printStackTrace();
		}
		if (newcommand==null && !Arrays.asList(silentcommands).contains(command)) {
			History.add(command);
			screen.get("#debugger_console").val(currentpath+"$");
		} else if (newcommand!=null) {
			screen.get("#debugger_console").val(currentpath+"$"+newcommand);
		} else {
			screen.get("#debugger_console").val(currentpath+"$");
		}
		int j = 0;
		if (buffer.size()>maxlines) j = buffer.size()-maxlines;
		String body ="";
		for (int i=j;i<buffer.size();i++) {
			String line = buffer.get(i);
			body += line+"\n";
		}
		screen.get("#debugger_output").html(body);

	}	

	
    public void keyPressed(Screen s,JSONObject data) {	
    	Long which = (Long)data.get("which");
		switch (which.intValue()) {
			case 13: 
				String cmd = (String)data.get("debugger_console");
				if (cmd.length()>currentpath.length()) {
					cmd = cmd.substring(currentpath.length()+1);
					handleCommand(cmd);
				}
				break;
			case 38:
				handleCommand("prev");
				break;
			case 40:
				handleCommand("next");
				break;
			case 9:
				cmd = (String)data.get("debugger_console");
				if (cmd.length()>currentpath.length()) {
					cmd = cmd.substring(currentpath.length()+1);
					handleCommand("filecomplete "+cmd);
				}
				break;
			default:
				break;
		}	
    }
 	 
}
