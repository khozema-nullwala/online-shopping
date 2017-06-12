package net.kzn.onlineshopping.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.kzn.onlineshopping.model.UserModel;
import net.kzn.shoppingbackend.dao.CartLineDAO;
import net.kzn.shoppingbackend.dao.ProductDAO;
import net.kzn.shoppingbackend.dao.UserDAO;
import net.kzn.shoppingbackend.dto.Cart;
import net.kzn.shoppingbackend.dto.CartLine;
import net.kzn.shoppingbackend.dto.Product;

@Service("cartService")
public class CartService {

	@Autowired
	private CartLineDAO cartLineDAO;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ProductDAO productDAO;
		
	@Autowired
	private HttpSession session;
	
	public List<CartLine> getCartLines() {		
		return cartLineDAO.list(getCart().getId());
	}
	

	public String manageCartLine(int cartLineId, int count) {
		
		CartLine cartLine = cartLineDAO.get(cartLineId);
		
		double oldTotal = cartLine.getTotal();

		// update the cart line
		cartLine.setProductCount(count);		
		cartLine.setTotal(cartLine.getProduct().getUnitPrice() * cartLine.getProductCount());
		cartLineDAO.update(cartLine);

		// update the cart
		Cart cart = this.getCart();
		cart.setGrandTotal(cart.getGrandTotal() - oldTotal + cartLine.getTotal());
		userDAO.updateCart(cart);
		this.setCart(cart);
		
				
		return "updated";
	}



	public String addCartLine(int productId) {		
		Cart cart = this.getCart();
		String response = null;
		CartLine cartLine = cartLineDAO.getByCartAndProduct(cart.getId(), productId);
		if(cartLine==null) {
			// add a new cartLine
			cartLine = new CartLine();
			Product product = productDAO.get(productId);
			
			cartLine.setCartId(cart.getId());
			cartLine.setProduct(product);
			cartLine.setProductCount(1);
			cartLine.setTotal(product.getUnitPrice());

			cartLineDAO.add(cartLine);
			
			// update the cart
			cart.setGrandTotal(cart.getGrandTotal() + cartLine.getTotal());
			cart.setCartLines(cart.getCartLines() + 1);
			userDAO.updateCart(cart);
			this.setCart(cart);	
			response = "added";
						
		} 
		else {
			// check if the cartLine has maxed out product count
			if(cartLine.getProductCount() == 5) {
				response = "maxed";				
			}
			
			else {				
				response = this.manageCartLine(cartLine.getId(), cartLine.getProductCount() + 1);				
			}
						
		}
		
		return response;
	}
	
	private Cart getCart() {
		return ((UserModel)session.getAttribute("userModel")).getCart();
	}

	private void setCart(Cart cart) {
		((UserModel)session.getAttribute("userModel")).setCart(cart);		
	}


	public String removeCartLine(int cartLineId) {
		
		CartLine cartLine = cartLineDAO.get(cartLineId);

		// update the cart
		Cart cart = this.getCart();	
		cart.setGrandTotal(cart.getGrandTotal() - cartLine.getTotal());
		cart.setCartLines(cart.getCartLines() - 1);		
		userDAO.updateCart(cart);		
		this.setCart(cart);
		
		cartLineDAO.remove(cartLine);
				
		return "deleted";
	}


	public String validateCartLine() {
		Cart cart = this.getCart();
		List<CartLine> cartLines = cartLineDAO.list(cart.getId());
		double grandTotal = 0.0;
		int lineCount = 0;
		String response = "confirmed";
		Product product = null;
		for(CartLine cartLine : cartLines) {		
			
			product = cartLine.getProduct();
			
			if(product.isActive()) {

				/* added afterwards */
				if(!cartLine.isAvailable()) {
					cartLine.setAvailable(true);
					response = "validated";
				}
				
				if(cartLine.getProductCount() > product.getQuantity()) {
					cartLine.setProductCount(product.getQuantity());										
					cartLine.setTotal(cartLine.getProductCount() * product.getUnitPrice());
					response = "validated";					
				}
				
			}
			
			else {
				cartLine.setAvailable(false);
				response = "validated";
			}
			//update the cartLine
			cartLineDAO.update(cartLine);
			grandTotal += cartLine.getProductCount() * product.getUnitPrice();
			lineCount++;
		}
		
		cart.setCartLines(lineCount++);
		cart.setGrandTotal(grandTotal);
		userDAO.updateCart(cart);
		this.setCart(cart);
				
		return response;
	}

	
}
