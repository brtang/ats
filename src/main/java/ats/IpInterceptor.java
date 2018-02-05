package ats;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ats.constants.Constants;

@Component
public class IpInterceptor implements HandlerInterceptor {
	
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws IOException {
		System.out.println("In IpInterceptor.");
		if(!Constants.VALID_IPS.contains(req.getRemoteAddr())) {
			System.out.println("Invalid IP address");
			resp.getWriter().write("Invalid IP address.");
			resp.setStatus(404);
			return false;
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {	
		// TODO Auto-generated method stub	
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub	
	}
}
