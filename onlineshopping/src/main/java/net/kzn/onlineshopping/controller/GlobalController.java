package net.kzn.onlineshopping.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import net.kzn.shoppingbackend.dao.UserDAO;
import net.kzn.shoppingbackend.dto.Cart;
import net.kzn.shoppingbackend.dto.User;

@ControllerAdvice
public class GlobalController {

	
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private HttpSession session;
	
	private User user;
	private Cart cart = null;
	
	@ModelAttribute("cart")
	public Cart getCart() {		
		if(session.getAttribute("cart")==null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			user = userDAO.getByEmail(authentication.getName());
			if(user!=null && user.getRole().equals("USER")) {
				cart = user.getCart();
				session.setAttribute("cart", cart);
				return cart;
			}			
			return null;
		}
		return (Cart) session.getAttribute("cart");		
	}
		
}
