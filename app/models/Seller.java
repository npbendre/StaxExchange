package models;

import java.sql.Timestamp;

public class Seller {

	public Long id;
	
	public Long quantity;
	
	public Double  price;
	
	public Timestamp creationDate;
	
	public Timestamp expirationDate;

	public int year; 
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	
	
}
