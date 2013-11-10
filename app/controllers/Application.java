package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import models.Buyer;
import models.Buyer.BuyerComparator;
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
    	ResultSet rs = executeQuery("select * from seller where uid = " + uuid);
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
    
    
    public static ResultSet executeQuery(String query) throws SQLException
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
    
    public static List<Buyer> getBuyers(double price) throws SQLException {
    	
    	ResultSet rs = executeQuery("select * from buyer where price >= " + price);
    	
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
    
    public static void commitTransaction(String uuid, double price, long quantity, Buyer buyer, Seller seller) throws SQLException
    {
    	ResultSet rs = executeQuery("insert into transaction (uuid, seller_id, buyer_id, price, quantity ) values ('"+uuid+"',"+seller.getId()+","+buyer.getId()+","+price+","+quantity+ ")");
    	
    	long remainingQuantity = seller.getQuantity() - quantity;
    	
    	// seller update 
    	ResultSet updatedRs =  executeQuery("update seller set quantity = " + remainingQuantity + "where id = "+ seller.getId());
    	
    }
    
    public static Result tradeTaxCredits(String uuid) throws SQLException
    {
    	Seller seller = getSeller("uuid");
    	double minPriceRequired = seller.getPrice();
    	
    	if(minPriceRequired <=0 ) return ok("Error.");
    	
    	List<Buyer> listBuyer = getBuyers(minPriceRequired);
    	Collections.sort(listBuyer, Buyer.decending(Buyer.getComparator(BuyerComparator.PRICE_SORT, BuyerComparator.QUANTITY_SORT)));
    	
    	UUID transactId = null; 
    	
    	for(Iterator<Buyer> i = listBuyer.iterator(); i.hasNext(); ) {
    	    Buyer prospect = i.next();
    	    if(prospect.getQuantity() < seller.getQuantity())
    	    {
    	    	long quants = seller.getQuantity() - prospect.getQuantity();
    	    	if(transactId == null) transactId= UUID.randomUUID();
    	    	commitTransaction(transactId.toString(), prospect.getPrice(),quants,prospect, seller);
    	    }
    	}
    	
    	return ok(index.render("Your new application is ready."));
    }
    
}
