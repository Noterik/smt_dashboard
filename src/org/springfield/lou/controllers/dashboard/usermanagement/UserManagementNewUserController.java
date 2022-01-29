package org.springfield.lou.controllers.dashboard.usermanagement;

import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.springfield.fs.FSList;
import org.springfield.fs.FSListManager;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.ApplicationManager;
import org.springfield.lou.application.Html5Application;
import org.springfield.lou.controllers.Html5Controller;
import org.springfield.lou.screen.Screen;
import org.springfield.lou.user.User;
import org.springfield.lou.user.UserManager;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class UserManagementNewUserController extends Html5Controller {
	
	public UserManagementNewUserController() {
		
	}
	
	public void attach(String s) {
		selector = s;
		screen.loadStyleSheet("dashboard/usermanagement/usermanagementnewuser/usermanagementnewuser.css");
		fillPage();
 		screen.get("#createuser").on("mouseup","account,firstname,lastname,password,email,phonenum,role,birthdata,state","onCreateUser", this);
	}

    public void onCreateUser(Screen s,JSONObject data) {
    	System.out.println("CREATE USER CALLED D="+data.toJSONString());
    	
    	//private String createAccount(String domain,String account,String email,String password) {
		ServiceInterface barney = ServiceManager.getService("barney");
		if (barney!=null) {
			System.out.println("Barney interface = "+barney);	
			
			String account = ((String)data.get("account")).toLowerCase();
			String email = (String)data.get("email");
			String firstname = (String)data.get("firstname");
			String lastname = (String)data.get("lastname");
			String phonenum = (String)data.get("phonenum");
			String role = (String)data.get("role");
			String birthdata = (String)data.get("birthdata");
			String state = (String)data.get("state");
			
			String password = (String)data.get("password");
			String domain = screen.getApplication().getDomain();
			
			String result = barney.get("userexists("+domain+","+account+","+email+","+password+")", null, null);
			if (result.equals("true")) {
				screen.get("#feedback").html("user already exists");
				return;
			}
			/*
			result = barney.get("approvedaccountname("+domain+","+account+")", null, null);
			if (result.equals("false")) {
				screen.get("#feedback").html("user name invalid");
				return;
			}
			*/
			
			result = barney.get("createaccount("+domain+","+account+","+email+","+password+")", null, null);
			if (result!=null) {
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+account+"/account/default/firstname",firstname);
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+account+"/account/default/lastname",lastname);
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+account+"/account/default/phonenum",phonenum);
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+account+"/account/default/role",role);
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+account+"/account/default/birthdata",birthdata);
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+account+"/account/default/state",state);

				screen.get(selector).remove();
			}
		}
	
    	
    }
	
	
	/*
    public void onKey(Screen s,JSONObject data) {
    	String id = (String)data.get("id");
    	Long keycode = (Long)data.get("which");
    	if (keycode!=13) screen.get("#"+id+"save").css("visibility","visible");
    }
	
	
    public void onSave(Screen s,JSONObject data) {
    	System.out.println("SAVE SELECTED="+data.toJSONString());
    	String fieldname = (String)data.get("id");
    	String value = (String)data.get("value");
   		String id = model.getProperty("/screen/nodeid");
		if (fieldname.equals("password")) {
			System.out.println("SETTING PASSWORD");
			ServiceInterface barney = ServiceManager.getService("barney");
			if (barney!=null) {
				System.out.println("Barney interface = "+barney);			
				String result = barney.put("setpassword("+screen.getApplication().getDomain()+","+id+")", value, null);
				model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+id+"/account/default/"+fieldname,"$shadow");
				System.out.println("ReSULT="+result);
			}
		} else {
			model.setProperty("/domain/"+screen.getApplication().getDomain()+"/user/"+id+"/account/default/"+fieldname, value);
		}
   		screen.get("#"+fieldname+"save").css("visibility","hidden");
    }
    */
    
 	
	private void fillPage() {
		JSONObject data = new JSONObject();
		screen.get(selector).parsehtml(data);
	}
	
}
