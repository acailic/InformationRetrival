package app.controller;

import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.CurrentUser;
import app.model.User;
import app.service.user.UserService;
import app.util.Role;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final String HOME_URL = "http://localhost:8080/";
	
	@Autowired
	private UserService userService;
	
	@Autowired
    JavaMailSender mailSender;
	
	@RequestMapping("/afterRegister")
	public String afterRegister(HttpServletRequest request,
			HttpServletResponse response) { 
		return "afterRegister";
	}
	@RequestMapping("/register")
	public String registerRedirection(ModelMap model,
			Authentication authentication) {
		if (authentication != null) {
			CurrentUser currentUser = (CurrentUser) authentication
					.getPrincipal();
			if (currentUser != null) {

				if (currentUser.getUser().getRole() == Role.EDITOR)
					model.addAttribute("adminAdd", true);
			}
		}
		return "register";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public String journalistRegister(HttpServletRequest request,
			HttpServletResponse response) {
		String message = "";
		System.out.println("REGISTRACIJA U TOKU:");
		String confirmLink = "http://localhost:8080/user/registerConfirmation?email=" + request.getParameter("email") + 
        		"?ime=" + request.getParameter("ime") + "?lozinka=" + request.getParameter("lozinka");
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(request.getParameter("email"));
        //email.setFrom("uddspring@gmail.com");
        email.setSubject("Confirm your account registration");
        email.setText("Please click on the following link to confirm your registration: " + confirmLink);
        email.setSubject("Confirmation"); 
       
        // sends the e-mail
        System.out.println("Slanje pocinje.");
        try{
        mailSender.send(email);
        }catch(MailException e ){
        	e.printStackTrace();
        	message="Sending mail failed. Your registration did not continue. Sorry";
        	return "redirect:"+HOME_URL+ "user/afterRegister";	
        }  
        System.out.println("Slanje zavrseno.");
        message="Sending mail success. Your registration did not continue. Sorry";
        return "redirect:"+HOME_URL+"user/afterRegister";
	}
	
	@RequestMapping(value = "registerConfirmation", method = RequestMethod.GET, consumes = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE }) 
	public String registrationConfirmation(HttpServletRequest request,
			HttpServletResponse response) {
		String message = "";
		try {
			userService.create(request.getParameter("email"),
					request.getParameter("ime"),
					request.getParameter("lozinka"));

		} catch (DataIntegrityViolationException e) {
			message = "E-mail adress is already in use.";
			return "redirect:" + HOME_URL + "/register";
		}

		return "redirect:" + HOME_URL;
	}

	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "add", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public String addJournalist(HttpServletRequest request) {
		String message = "";

		try {
			userService.create(request.getParameter("email"),
					request.getParameter("ime"),
					request.getParameter("lozinka"));

		} catch (DataIntegrityViolationException e) {
			message = "E-mail adress is already in use.";
			return "redirect:" + HOME_URL + "/register";
		}

		return "redirect:" + HOME_URL + "/user/journalists";
	}

	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "journalists", method = RequestMethod.GET)
	public String listAllJournalists(Model model) {
		ArrayList<User> journalists = (ArrayList<User>) userService
				.getAllUsersByRole(Role.JOURNALIST.toString());
		model.addAttribute("allJournalists", journalists);
		return "allUsers";
	}

	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "edit/{userId}", method = RequestMethod.GET)
	public String editUser(@PathVariable("userId") Long userId, Model model) {
		User user = userService.getUserById(userId);
		model.addAttribute("editUser", user);
		return "userEdit";
	}

	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String editUser(HttpServletRequest request) {
		String idS = request.getParameter("id").toString();
		Long id = Long.parseLong(idS);
		userService.edit(id, request.getParameter("name").toString());
		return "redirect:" + HOME_URL + "/user/journalists";

	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping("/myProfile")
	public String myProfileRedirection(ModelMap model,
			Authentication authentication) {
		if (authentication != null) {
			CurrentUser currentUser = (CurrentUser) authentication
					.getPrincipal();
			if (currentUser != null) {
				if (currentUser.getUser().getRole() == Role.JOURNALIST)
					model.addAttribute("currentUser", currentUser.getUser());
			}
		}
		return "myProfile";
	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request, ModelMap model, Authentication authentication) {
		if (authentication != null) {
			CurrentUser currentUser = (CurrentUser) authentication
					.getPrincipal();
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String confirmNewPassword = request.getParameter("confirmNewPassword");
			boolean indicator = userService.changePassword(currentUser.getUser().getId(), oldPassword, newPassword, confirmNewPassword);
			if(indicator)
				return "redirect:" + HOME_URL + "/user/myProfile?success";
			else
				return "redirect:" + HOME_URL + "/user/myProfile?error";
		}
		else
			return "redirect:" + HOME_URL + "/user/myProfile?error";
	}

}
