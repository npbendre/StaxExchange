package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
  
    public static Result getCauses(String causeId) throws SQLException {
    	String db_url = Play.application().configuration().getString("db.default.url");
    	String uname = Play.application().configuration().getString("db.default.user");
    	String password = Play.application().configuration().getString("db.default.password");
    	Connection conn = DriverManager.getConnection(db_url,uname, password);
    	
    	String temp = "";
    	
    	/*ResultSet rs = conn.createStatement().executeQuery("select * from account where cause_id="+causeId);
    	;
    	while(rs.next()){
            //Retrieve by column name
            int id  = rs.getInt("cause_id");
            String name = rs.getString("cause_name");
            
            //Display values
            System.out.print("ID: " + id);
            System.out.print(", Name: " + name);
            temp = id + name;
         }
         rs.close(); */
    	
          return ok((temp));
         
    }
}
