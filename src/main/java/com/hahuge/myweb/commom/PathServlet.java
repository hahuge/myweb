package com.hahuge.myweb.commom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * @author Hahuge
 * @since 2017/12/12
 */
public class PathServlet extends HttpServlet{

	private static final long serialVersionUID = -5430682942565748910L;
	
	public static String PATH ;
	
	@Override
	public void init() throws ServletException {
		PATH = this.getServletContext().getRealPath("/");
	}
	
}
