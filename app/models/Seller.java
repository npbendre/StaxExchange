package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.format.Formats.DateTime;
import play.db.ebean.Model;

@Entity
@Table(name="seller") //TODO: change the table name 
public class Seller extends Model {

	@Id
	public Long id;
	
	public Long quantity;
	
	public Double  price;
	
	public DateTime creationDate;
	
	public DateTime expirationDate;
	
	
}
