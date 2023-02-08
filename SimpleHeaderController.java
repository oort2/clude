package controller;

import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimpleHeaderController {
//read Cookies
	@RequestMapping("/header/simple")
	public String simple(@RequestHeader(value = "Accept", defaultValue = "text/html") String acceptType,
			@CookieValue(value = "auth", required = false) Integer authValue, Model model) {
		model.addAttribute("acceptType", acceptType);
		if (authValue != null)
			model.addAttribute("auth", authValue);
		return "/header/simpleValue"; // jsp
	}

	// create Cookies
	@RequestMapping("/header/createauth")
	public String creatAuth(HttpServletResponse response, Model model) {
		Random random = new Random();
		String authValue = Integer.toString(random.nextInt());
		response.addCookie(new Cookie("auth", authValue));

		return "redirect:simple"; // url
	}

}