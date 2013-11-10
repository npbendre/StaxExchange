package models;

import java.sql.Timestamp;
import java.util.Comparator;

public class Buyer {

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

	public enum BuyerComparator implements Comparator<Buyer> {
	    PRICE_SORT {
	        public int compare(Buyer o1, Buyer o2) {
	            return (o1.getPrice()).compareTo(o2.getPrice());
	        }},
	    QUANTITY_SORT {
	        public int compare(Buyer o1, Buyer o2) {
	            return o1.getQuantity().compareTo(o2.getQuantity());
	        }};

	}
	
	public static Comparator<Buyer> decending(final Comparator<Buyer> other) {
		return new Comparator<Buyer>() {
			public int compare(Buyer o1, Buyer o2) {
				return -1 * other.compare(o1, o2);
			}
	    };
	}
	    
	    public static Comparator<Buyer> getComparator(final BuyerComparator... multipleOptions) {
	        return new Comparator<Buyer>() {
	            public int compare(Buyer o1, Buyer o2) {
	                for (BuyerComparator option : multipleOptions) {
	                    int result = option.compare(o1, o2);
	                    if (result != 0) {
	                        return result;
	                    }
	                }
	                return 0;
	            }
	        };
	    }

}

