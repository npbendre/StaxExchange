package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.format.Formats.DateTime;
import play.db.ebean.Model;

@Entity
@Table(name="account") //TODO: change the table name 
public class Seller extends Model {

	@Id
	public Long id;
	
	public Long quantity;
	
	public Double  price;
	
	public DateTime available_date;
	
}
