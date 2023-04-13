package com.example.retries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

@Controller
public class RootController {

    private static final String VIEW_FILE = "adminView.html";

    @Autowired
    private RequestMappingHandlerMapping controllerMappings;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @RequestMapping("/retry")
    @ResponseBody
    public Object show() {
        Set<String> urls = new HashSet<>();

        urls.addAll(retrieveControllerUrls());
        urls.addAll(retrieveServletUrls());

        return createView(urls);
    }

    private String createView(Set<String> urls) {

        final Context ctx = new Context();
        ctx.setVariable("urls", urls);

        return this.templateEngine.process(VIEW_FILE, ctx);
    }

    private Set<String> retrieveControllerUrls() {
        Set<String> urls = new HashSet<>();
        for (RequestMappingInfo mappingInfo : controllerMappings.getHandlerMethods().keySet()) {
            for (String pattern : mappingInfo.getPatternsCondition().getPatterns()) {
                urls.add(UriComponentsBuilder.fromPath(pattern).toUriString());
            }
        }
        return urls;
    }

    private Set<String> retrieveServletUrls() {
        Set<String> urls = new HashSet<>();
        for (ServletRegistration registration : servletContext.getServletRegistrations().values()) {
            for (String mapping : registration.getMappings()) {
                if (mapping.startsWith("/retry/")) {
                    urls.add(mapping);
                }
            }
        }
        return urls;
    }
}
