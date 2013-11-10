package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Buyer;
import models.Seller;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
  
    public static Connection getDatabaseConnection() throws SQLException {
    	String db_url = Play.application().configuration().getString("db.default.url");
    	String uname = Play.application().configuration().getString("db.default.user");
    	String password = Play.application().configuration().getString("db.default.password");
    	Connection conn = null;
    	
    	try {
    		conn = DriverManager.getConnection(db_url,uname, password);
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return conn;
    }
    
    
    public static Seller getSeller(String uuid) throws SQLException
    {
    	ResultSet rs = getResultSet("select * from seller where uid = " + uuid);
    	Seller seller = new Seller();
    	
    	while(rs.next()){
            //Retrieve by column name
    		
    		seller.setId(rs.getLong("id"));
    		seller.setPrice(rs.getDouble("price"));
    		seller.setQuantity(rs.getLong("quantity"));
    		seller.setCreationDate(rs.getTimestamp("creationDate"));
    		seller.setExpirationDate(rs.getTimestamp("creationDate"));
    		seller.setYear(rs.getInt("year"));
    		
         }
    	
         rs.close();
         return seller;
    }
    
    
    public static ResultSet getResultSet(String query) throws SQLException
    {
    	Connection conn = getDatabaseConnection();
    	
    	ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;	
    }
    
    
    public static List<Buyer> getBuyers() throws SQLException {
    	
    	ResultSet rs = getResultSet("select * from buyer");
    	
		List<Buyer> buyerList = new ArrayList<Buyer>();
		
    	while(rs.next()){
            //Retrieve by column name
    		Buyer buyer = new Buyer();
    		buyer.setId(rs.getLong("id"));
    		buyer.setPrice(rs.getDouble("price"));
    		buyer.setQuantity(rs.getLong("quantity"));
    		buyer.setCreationDate(rs.getTimestamp("creationDate"));
    		buyer.setExpirationDate(rs.getTimestamp("creationDate"));
    		buyer.setYear(rs.getInt("year"));
    		buyerList.add(buyer);
         }
         rs.close();
         
         return buyerList; 
         
    }
    
    
}
