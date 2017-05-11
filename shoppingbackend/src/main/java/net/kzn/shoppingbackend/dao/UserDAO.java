package net.kzn.shoppingbackend.dao;

import java.util.List;

import net.kzn.shoppingbackend.dto.Address;
import net.kzn.shoppingbackend.dto.Cart;
import net.kzn.shoppingbackend.dto.User;

public interface UserDAO {

	// user related operation
	User get(int id);
	User getByEmail(String email);
	boolean add(User user);
	
	// adding and updating a new address
	boolean addAddress(Address address);
	boolean updateAddress(Address address);
	List<Address> listAddresses(User user, boolean isBilling);
	
	// updating the cart
	boolean updateCart(Cart cart);
	
}
