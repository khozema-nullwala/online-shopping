package net.kzn.onlineshopping.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.kzn.onlineshopping.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	private final static Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private CartService cartService;
	@RequestMapping("/show")
	public ModelAndView showCart(@RequestParam(name = "updated", required = false) String updated,
			@RequestParam(name = "maxed", required = false) String maxed,
			@RequestParam(name = "added", required = false) String added,
			@RequestParam(name = "deleted", required = false) String deleted,
			@RequestParam(name = "validated", required = false) String validated
			) {
		
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Shopping Cart");
		mv.addObject("userClickShowCart", true);
		mv.addObject("cartLines", cartService.getCartLines());
		if(updated!=null) {
			mv.addObject("updated", "The cart has been successfully updated!");
		}
		if(maxed!=null) {
			mv.addObject("maxed", "The product has reached its maximum limit!");
		}
		if(added!=null) {
			mv.addObject("added", "The product has been successfully added inside the cart!");
		}
		if(deleted!=null) {
			mv.addObject("deleted", "The product has been successfully deleted from the cart!");
		}
		if(validated!=null) {
			mv.addObject("validated", "One or more products inside the cart has been changed!");
		}
							
		
		return mv;
		
	}
	

	@RequestMapping("/{cartLineId}/update")
	public String udpateCartLine(@PathVariable int cartLineId, @RequestParam int count) {
		
		logger.info(String.valueOf(count));
		
		String response = cartService.manageCartLine(cartLineId, count);
		
		return "redirect:/cart/show?"+response;		
	}

	
	@RequestMapping("/add/{productId}/product")
	public String addCartLine(@PathVariable int productId) {
	
		String response = cartService.addCartLine(productId);
		
		return "redirect:/cart/show?"+response;
	}
	
	@RequestMapping("/{cartLineId}/remove")
	public String removeCartLine(@PathVariable int cartLineId) {
	
		String response = cartService.removeCartLine(cartLineId);
		
		return "redirect:/cart/show?"+response;
	}
		
	@RequestMapping("/validate")
	public String validateCart() {	
		String response = cartService.validateCartLine();
		if(response.equals("validated")) {
			return "redirect:/cart/show?"+response;
		}
		else {
			return "redirect:/cart/checkout";
		}
	}
			
	
}
