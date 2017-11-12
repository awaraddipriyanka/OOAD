package model;

import java.util.Date;

public class Item {
	public int item_id;
    public String item_name;
	public String user_name;
	public int price;
	public String category;
	public int quantity;
	public String description;
    public String post_date;
    public boolean forSale;
    public boolean forRent;
    public int availability;
    public Date rentStartDate;
    public Date rentEndDate;

    public Item(String item_name, String user_name, int price, String category, int quantity,
                String description, String post_date, boolean forSale, boolean forRent, int availability,
                Date rentStartDate, Date rentEndDate) {
        this.item_name = item_name;
        this.user_name = user_name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.description = description;
        this.post_date = post_date;
        this.forSale = forSale;
        this.forRent = forRent;
        this.availability = availability;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
    }
    
    public Item() {
        
    }
    
	public int getItem_id() {
		return item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public int getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getDescription() {
		return description;
	}

	public String getPost_date() {
		return post_date;
	}

	public boolean isForSale() {
		return forSale;
	}

	public boolean isForRent() {
		return forRent;
	}

	public int getAvailability() {
		return availability;
	}

	public Date getRentStartDate() {
		return rentStartDate;
	}

	public Date getRentEndDate() {
		return rentEndDate;
	}
	
	
}
