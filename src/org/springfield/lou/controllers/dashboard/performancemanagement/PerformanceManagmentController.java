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
import org.springfield.lou.model.ModelEvent;
import org.springfield.lou.model.ModelEventManager;
import org.springfield.lou.performance.PerformanceManager;
import org.springfield.lou.screen.Screen;
import org.springfield.lou.screen.ScreenManager;
import org.springfield.lou.websocket.LouWebSocketConnection;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class PerformanceManagmentController extends Html5Controller {

	private ScreenManager gm;
	private long starttime;
	
	public PerformanceManagmentController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/performancemanagement/performancemanagement.css");
		fillPage();
		screen.get(selector).on("mouseup","mouseUp", this);
		starttime = new Date().getTime();
		model.onNotify("/shared[timers]/5second","on5SecondTimer",this); 
  	}
	
	public void on5SecondTimer(ModelEvent e) {
		 fillPage();
	}
	
		
	private void fillPage() {
		long endtime = new Date().getTime();
		float uptime = (endtime-starttime)/1000;
		gm = screen.getApplication().getScreenManager();
		JSONObject data = new JSONObject();
		
		data.put("update",""+uptime);
		
		long tt = PerformanceManager.getTotalNetworkTime();
		if (tt<1000) {
			data.put("totalnetworktime",""+tt);
			data.put("totalnetworktimetype","milliseconds");
		} else {
			data.put("totalnetworktime",""+(((double)tt)/1000));
			data.put("totalnetworktimetype","seconds");
		}
		data.put("totalnetworkcalls",""+PerformanceManager.getTotalNetworkCalls());
		long at = PerformanceManager.getAvgNetworkCall();
		if (at<1000) {
			data.put("avgnetworkcall",""+at);
			data.put("avgnetworkcalltype","nanoseconds");
		} else {
			data.put("avgnetworkcall",""+(((double)at)/1000));
			data.put("avgnetworkcalltype","milliseconds");
		}
		
		
		long tsst = getTotalSocketSendTime();
		long tssc = getTotalSocketSendCalls();
		long tssb = getTotalSocketSendBytes();
		
		if (tsst<1000) {
			data.put("totalsocketsendtime",""+tsst);
			data.put("totalsocketsendtimetype","milliseconds");
		} else {
			data.put("totalsocketsendtime",""+(((double)tsst)/1000));
			data.put("totalsocketsendtimetype","seconds");
		}
		data.put("totalsocketsendcalls",""+tssc);
		
		data.put("avgsocketsendcall",""+((tsst*1000)/tssc));

		String st = ""+tssb;
		if (tssb<1024) {
			data.put("totalsocketsendbytestype","bytes");
		} else	if (tssb<(1024*1024)) {
			st = ""+((double)tssb)/1024;
			data.put("totalsocketsendbytestype","kilobytes");
		} else {
			st = ""+((double)tssb)/(1024*1024);
			data.put("totalsocketsendbytestype","megabytes");
		}
		if (st.length()>8) {
			data.put("totalsocketsendbytes",st.substring(0,8));
		} else {
			data.put("totalsocketsendbytes",st);
		}
		
		data.put("avgsocketsendbytes",tssb/tssc);
		data.put("avguptimesocketsendbytes",tssb/uptime);
		
	
		long tsrt = getTotalSocketReceiveTime();
		long tsrc = getTotalSocketReceiveCalls();
		long tsrb = getTotalSocketReceiveBytes();
		
		if (tsrt<1000) {
			data.put("totalsocketreceivetime",""+tsrt);
			data.put("totalsocketreceivetimetype","milliseconds");
		} else {
			data.put("totalsocketreceivetime",""+(((double)tsrt)/1000));
			data.put("totalsocketreceivetimetype","seconds");
		}
		data.put("totalsocketreceivecalls",""+tsrc);
		
		data.put("avgsocketreceivecall",""+((tsrt*1000)/tsrc));

		st = ""+tsrb;
		if (tsrb<1024) {
			data.put("totalsocketreceivebytestype","bytes");
		} else	if (tsrb<(1024*1024)) {
			st = ""+((double)tsrb)/1024;
			data.put("totalsocketreceivebytestype","kilobytes");
		} else {
			st = ""+((double)tsrb)/(1024*1024);
			data.put("totalsocketreceivebytestype","megabytes");
		}
		if (st.length()>8) {
			data.put("totalsocketreceivebytes",st.substring(0,8));
		} else {
			data.put("totalsocketreceivebytes",st);
		}
		
		data.put("avgsocketreceivebytes",tsrb/tsrc);
		data.put("avguptimesocketreceivebytes",tsrb/uptime);

		screen.get(selector).parsehtml(data);
	}
	
	private long getTotalSocketSendTime() {
		Iterator<Screen> it = gm.getAllScreensIterator();
		int time = 0;
		while(it.hasNext()){
			Screen s = it.next();
			if (s!=null) {
				LouWebSocketConnection wc = s.getWebSocketConnection();
				if (wc!=null) {
					time+= wc.getPacketSendTime(); 
				}
			}
		}
		return time;
	}
	
	private long getTotalSocketReceiveTime() {
		Iterator<Screen> it = gm.getAllScreensIterator();
		int time = 0;
		while(it.hasNext()){
			Screen s = it.next();
			if (s!=null) {
				LouWebSocketConnection wc = s.getWebSocketConnection();
				if (wc!=null) {
					time+= wc.getPacketReceiveTime(); 
				}
			}
		}
		return time;
	}
	
	private long getTotalSocketSendBytes() {
		Iterator<Screen> it = gm.getAllScreensIterator();
		int size = 0;
		while(it.hasNext()){
			Screen s = it.next();
			if (s!=null) {
				LouWebSocketConnection wc = s.getWebSocketConnection();
				if (wc!=null) {
					size+= wc.getBytesSendSize();
				}
			}
		}
		return size;
	}
	
	private long getTotalSocketReceiveBytes() {
		Iterator<Screen> it = gm.getAllScreensIterator();
		int size = 0;
		while(it.hasNext()){
			Screen s = it.next();
			if (s!=null) {
				LouWebSocketConnection wc = s.getWebSocketConnection();
				if (wc!=null) {
					size+= wc.getBytesReceiveSize();
				}
			}
		}
		return size;
	}
	
	
	
	private long getTotalSocketSendCalls() {
		Iterator<Screen> it = gm.getAllScreensIterator();
		int calls = 0;
		while(it.hasNext()){
			Screen s = it.next();
			if (s!=null) {
				LouWebSocketConnection wc = s.getWebSocketConnection();
				if (wc!=null) {
					calls+= wc.getPacketSendCount();
				}
			}
		}
		return calls;
	}
	
	private long getTotalSocketReceiveCalls() {
		Iterator<Screen> it = gm.getAllScreensIterator();
		int calls = 0;
		while(it.hasNext()){
			Screen s = it.next();
			if (s!=null) {
				LouWebSocketConnection wc = s.getWebSocketConnection();
				if (wc!=null) {
					calls+= wc.getPacketReceiveCount();
				}
			}
		}
		return calls;
	}
	


	 public void mouseMove(Screen s,JSONObject data) {
		 // lets for fun parse the data again?
		 fillPage();
	 }
	
	
}
