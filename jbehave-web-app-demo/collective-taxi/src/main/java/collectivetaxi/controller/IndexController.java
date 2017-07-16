package collectivetaxi.controller;

import collectivetaxi.controller.validator.AuthenticationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class IndexController {

    @Autowired
    private AuthenticationValidator authenticationValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Principal principal, Model model, HttpSession session) {
        return login(principal, model, session);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Principal principal, Model model, HttpSession session) {
        if (authenticationValidator.isAuthenticated(principal, model)) {
            return "redirect:reservation";
        }
        model.addAttribute("registration", session.getAttribute("registration"));
        model.addAttribute("username", session.getAttribute("username"));
        return "login";
    }

    @RequestMapping(value = "/reservation", method = RequestMethod.GET)
    public String reservation(Principal principal, Model model) {
        if (authenticationValidator.isAuthenticated(principal, model)) {
            return "reservation";
        }
        return "redirect:login";
    }

}