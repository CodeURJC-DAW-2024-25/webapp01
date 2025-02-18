// TODO - Review and implement CustomErrorController (Check docs)

/*package es.daw01.savex.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import es.daw01.savex.components.ControllerUtils;


@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    private ControllerUtils controllerUtils;

    private ErrorAttributes errorAttributes = null;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @GetMapping("/error")
    public String handleError(WebRequest webRequest, Model model) {
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, null);
        Integer status = (Integer) errorAttributes.get("status");

        // Set error page title
        if (status == 404) model.addAttribute("title", "SaveX - Página no encontrada");
        else model.addAttribute("title", "SaveX - Error ".concat(status.toString()));

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Set error page attributes
        model.addAttribute("status", status);
        return "error";
    }
}*/