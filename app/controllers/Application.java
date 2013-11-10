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
    
    	Connection conn = getDatabaseConnection();

    	String queryStatement = "select * from seller where uid = '" + uuid+"'";
    	System.out.println(queryStatement);
    	ResultSet rs = executeSQLQuery(queryStatement, conn);
    	Seller seller = new Seller();
    	
    	while(rs.next()){
            //Retrieve by column name
    		
    		seller.setId(rs.getLong("id"));
    		seller.setQuantity(rs.getLong("quantity"));
    		seller.setPrice(rs.getDouble("price"));
    		// seller.setCreationDate(rs.getTimestamp("creationDate"));
    		// seller.setExpirationDate(rs.getTimestamp("creationDate"));
    		// seller.setYear(rs.getInt("year"));
    		
         }
    	
    	System.out.println("S Id - " + seller.getId());
    	System.out.println("S Quant - " + seller.getQuantity());
    	System.out.println("S Price - " + seller.getPrice());
    	
         rs.close();
         conn.close();
         
         return seller;
    }
    
    
    public static ResultSet executeSQLQuery(String query, Connection conn) throws SQLException
    {
    	
    	ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;	
    }
    
    public static int executeSQLUpdateQuery(String query, Connection conn) throws SQLException
    {
    	
    	int rs = 0;
		try {
			rs = conn.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;	
    }
    
    public static List<Buyer> getBuyers(double price, String uuid) throws SQLException {
    	
    	Connection conn = getDatabaseConnection();

    	ResultSet rs = executeSQLQuery("select * from buyer where price >= " + price + " and uid = " + uuid, conn);
    	
		List<Buyer> buyerList = new ArrayList<Buyer>();
		
    	while(rs.next()){
            //Retrieve by column name
    		Buyer buyer = new Buyer();
    		buyer.setId(rs.getLong("id"));
    		buyer.setPrice(rs.getDouble("price"));
    		buyer.setQuantity(rs.getLong("quantity"));
    		/* buyer.setCreationDate(rs.getTimestamp("creationDate"));
    		buyer.setExpirationDate(rs.getTimestamp("creationDate"));
    		buyer.setYear(rs.getInt("year")); */
    		buyerList.add(buyer);
         }
         rs.close();
         conn.close();
         
         return buyerList; 
         
    }
    
    public static void commitTransaction(String uuid, double price, long quantity, Buyer buyer, Seller seller) throws SQLException
    {
    	System.out.println("commitTransaction 1");
    	Connection conn = getDatabaseConnection();
    	int rs = executeSQLUpdateQuery("insert into transaction (uuid, seller_id, buyer_id, price, quantity ) values ('"+uuid+"',"+seller.getId()+","+buyer.getId()+","+price+","+quantity+ ")", conn );
    	if(rs == 1) System.out.println("commit success.");
    	
    	conn.close();
    
    	long remainingQuantity = seller.getQuantity() - quantity;
    	
    	System.out.println("commitTransaction 2");
    	// seller update 
    	Connection conn1 = getDatabaseConnection();
    	String updateQuery = "update seller set quantity = " + remainingQuantity + "where uid = '"+ uuid + "'";
    	System.out.println("Update Query i s- " + updateQuery);
    	//int updatedRs =  executeSQLUpdateQuery(updateQuery, conn1);
    	// if(updatedRs == 1) System.out.println("commit 2 success.");
    	conn1.close();
    	
    }
    
    public static Result tradeTaxCredits(String uuid) throws SQLException
    {
    	
    	String transactUUID = uuid;
    	
    	if(uuid == null) return ok("param is empty");
    	
    	Seller seller = getSeller(uuid);
    	double minPriceRequired = seller.getPrice();
    	// System.out.println("1.");
    	//System.out.println(minPriceRequired);
    	
    	if(minPriceRequired <=0 ) return ok("Error.");
    	
    	List<Buyer> listBuyer = getBuyers(minPriceRequired, uuid);
    	Collections.sort(listBuyer, Buyer.decending(Buyer.getComparator(BuyerComparator.PRICE_SORT, BuyerComparator.QUANTITY_SORT)));
    	
    	for(Iterator<Buyer> i = listBuyer.iterator(); i.hasNext(); ) {
    	    Buyer prospect = i.next();
    	    System.out.println("P - " + prospect.getQuantity());
    	    System.out.println("S - " + seller.getQuantity());
    	    if(prospect.getQuantity() < seller.getQuantity())
    	    {
    	    	long quants = seller.getQuantity() - prospect.getQuantity();
    	    	//if(transactId == null) transactId= UUID.randomUUID();
    	    	commitTransaction(transactUUID, prospect.getPrice(),quants,prospect, seller);
    	    }
    	    seller = getSeller(transactUUID);
    	    
    	}
    	
    	
    	return ok("success");
    }
    
}
