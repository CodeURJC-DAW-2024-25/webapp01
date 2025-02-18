// TODO - Review and implement CustomErrorController (Check docs)

package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.daw01.savex.components.ControllerUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    private ControllerUtils controllerUtils;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Error " + status);
        model.addAttribute("status", status);
        return "error";
    }
}