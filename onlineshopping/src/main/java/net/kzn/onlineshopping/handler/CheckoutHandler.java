package net.kzn.onlineshopping.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.kzn.onlineshopping.model.CheckoutModel;
import net.kzn.onlineshopping.model.UserModel;
import net.kzn.shoppingbackend.dao.CartLineDAO;
import net.kzn.shoppingbackend.dao.UserDAO;
import net.kzn.shoppingbackend.dto.Address;
import net.kzn.shoppingbackend.dto.Cart;
import net.kzn.shoppingbackend.dto.OrderItem;

@Component
public class CheckoutHandler {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CartLineDAO cartLineDAO;

	
	@Autowired
	private HttpSession session;
	
	public CheckoutModel init() {
		
		CheckoutModel checkoutModel = new CheckoutModel();
		UserModel userModel = (UserModel) session.getAttribute("userModel");
		Cart cart = userModel.getCart();
				
		checkoutModel.setCartLines(cartLineDAO.listAvailable(cart.getId()));
		checkoutModel.setOrderItems(new ArrayList<OrderItem>());
		
		List<Address> addresses = userDAO.listShippingAddresses(userModel.getId());
		checkoutModel.setAddresses(addresses);
		checkoutModel.setBilling(userDAO.getBillingAddress(userModel.getId()));
		
		
		return checkoutModel;
	}
	
	
	
	
	
}
