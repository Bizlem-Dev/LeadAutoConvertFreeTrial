/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
		@Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/Email" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "ServletEmail" }) })
@SuppressWarnings("serial")


public class EmailCheck extends SlingAllMethodsServlet {
	
	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		PrintWriter out= response.getWriter();

		try {
		out.print("in servlet");

		if (request.getRequestPathInfo().getExtension().equals("ServletEmail")) {
			out.println("hello first line");
             String email="siva@bizlem.com";
    //  String url="http://35.199.12.190:8082/test/EmailCheck?email="+request.getParameter(email);
     // out.println("Email id :"+email);
	validate_code_source obj_validate_code_source=new validate_code_source();
		boolean exists=validate_code_source.isAddressValid(email);
		out.println("exists ::: "+exists);
		if(exists==true){
			out.println("Email Exists");
	}
		else {
			
		out.println("Email Doesnot Exists");
		}
		
        	out.println("Inside If");
		}
		}
		catch(Exception ex) {
			
			out.println("Exception ex  "+ex.getMessage());
			
		}
	}

}
