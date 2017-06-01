package net.kzn.onlineshopping.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.kzn.onlineshopping.util.FileUtil;
import net.kzn.onlineshopping.validator.ProductValidator;
import net.kzn.shoppingbackend.dao.CategoryDAO;
import net.kzn.shoppingbackend.dao.ProductDAO;
import net.kzn.shoppingbackend.dto.Category;
import net.kzn.shoppingbackend.dto.Product;

@Controller
@RequestMapping("/manage")
public class ManagementController {

	private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;		

	@RequestMapping("/product")
	public ModelAndView manageProduct(@RequestParam(name="success",required=false)String success) {		

		ModelAndView mv = new ModelAndView("page");	
		mv.addObject("title","Product Management");		
		mv.addObject("userClickManageProduct",true);
		
		Product nProduct = new Product();
		
		// assuming that the user is ADMIN
		// later we will fixed it based on user is SUPPLIER or ADMIN
		nProduct.setSupplierId(1);
		nProduct.setActive(true);

		mv.addObject("product", nProduct);

		
		if(success != null) {
			if(success.equals("product")){
				mv.addObject("message", "Product submitted successfully!");
			}	
			else if (success.equals("category")) {
				mv.addObject("message", "Category submitted successfully!");
			}
		}
			
		return mv;
		
	}

	
	@RequestMapping("/{id}/product")
	public ModelAndView manageProductEdit(@PathVariable int id) {		

		ModelAndView mv = new ModelAndView("page");	
		mv.addObject("title","Product Management");		
		mv.addObject("userClickManageProduct",true);
		
		// Product nProduct = new Product();		
		mv.addObject("product", productDAO.get(id));

			
		return mv;
		
	}
	
	
	@RequestMapping(value = "/product", method=RequestMethod.POST)
	public String managePostProduct(@Valid @ModelAttribute("product") Product mProduct, 
			BindingResult results, Model model, HttpServletRequest request) {
		
		// mandatory file upload check
		if(mProduct.getId() == 0) {
			new ProductValidator().validate(mProduct, results);
		}
		else {
			// edit check only when the file has been selected
			if(!mProduct.getFile().getOriginalFilename().equals("")) {
				new ProductValidator().validate(mProduct, results);
			}			
		}
		
		if(results.hasErrors()) {
			model.addAttribute("message", "Validation fails for adding the product!");
			model.addAttribute("userClickManageProduct",true);
			return "page";
		}			

		
		if(mProduct.getId() == 0 ) {
			productDAO.add(mProduct);
		}
		else {
			productDAO.update(mProduct);
		}
	
		 //upload the file
		 if(!mProduct.getFile().getOriginalFilename().equals("") ){
			FileUtil.uploadFile(request, mProduct.getFile(), mProduct.getCode()); 
		 }
		
		return "redirect:/manage/product?success=product";
	}

	
	@RequestMapping(value = "/product/{id}/activation", method=RequestMethod.GET)
	@ResponseBody
	public String managePostProductActivation(@PathVariable int id) {		
		Product product = productDAO.get(id);
		boolean isActive = product.isActive();
		product.setActive(!isActive);
		productDAO.update(product);		
		return (isActive)? "Product Dectivated Successfully!": "Product Activated Successfully";
	}
			

	@RequestMapping(value = "/category", method=RequestMethod.POST)
	public String managePostCategory(@ModelAttribute("category") Category mCategory, HttpServletRequest request) {					
		categoryDAO.add(mCategory);		
		return "redirect:" + request.getHeader("Referer") + "?success=category";
	}
			
	
	
	@ModelAttribute("categories") 
	public List<Category> modelCategories() {
		return categoryDAO.list();
	}
	
	@ModelAttribute("category")
	public Category modelCategory() {
		return new Category();
	}
	
	
}

	
