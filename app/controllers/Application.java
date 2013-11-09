package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Seller;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
  
    public static Result getSeller(String id) throws SQLException {
    	String db_url = Play.application().configuration().getString("db.default.url");
    	String uname = Play.application().configuration().getString("db.default.user");
    	String password = Play.application().configuration().getString("db.default.password");
    	Connection conn = DriverManager.getConnection(db_url,uname, password);
    	
    	String temp = "";
    	
    	ResultSet rs = conn.createStatement().executeQuery("select * from seller where id="+id);
    	 
    	while(rs.next()){
            //Retrieve by column name
            int sellerId  = rs.getInt("id");
            int price = rs.getInt("price");
            
            //Display values
            System.out.print("ID: " + sellerId);
            System.out.print(", Name: " + price);
             
         }
         rs.close(); 
    	
          return ok((temp));
         
    }
}
