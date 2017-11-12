package model;

public class Cart {
    public int getItemId() {
        return itemId;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isForSale() {
        return forSale;
    }

    private int itemId;
    private int userId;
    private int quantity;
    private boolean forSale;

	public Cart(int itemId, int userId, int quantity, boolean forSale) {
	    this.itemId = itemId;
	    this.userId = userId;
	    this.quantity = quantity;
	    this.forSale = forSale;
    }
}
