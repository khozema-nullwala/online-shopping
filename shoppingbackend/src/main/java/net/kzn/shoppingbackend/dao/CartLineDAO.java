package net.kzn.shoppingbackend.dao;

import java.util.List;

import net.kzn.shoppingbackend.dto.CartLine;

public interface CartLineDAO {

	public CartLine get(int id);	
	public CartLine getByCartAndProduct(int cartId, int productId);		
	public boolean add(CartLine cartLine);
	public boolean update(CartLine cartLine);
	public boolean remove(CartLine cartLine);
	public boolean removeAll(int cartId);
	
	public List<CartLine> list(int cartId);
	
}
