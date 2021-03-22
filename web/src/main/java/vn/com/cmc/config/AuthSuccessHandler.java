/**
 * 
 */
package vn.com.cmc.config;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import entity.frwk.SysSecurity;
import entity.frwk.SysUsers;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import vn.com.cmc.utils.Constants;
import vn.com.cmc.utils.DateUtils;
import vn.com.cmc.utils.Utils;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	MessageSource messageSource;

	@Autowired
	SysUsersDao sysUsersDao;

	@Autowired
	SysParamDao sysParamDao;
	
	@Autowired
	SysSecurityDao sysSecurityDao;

	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {

		boolean isError = false;
		User user = (User) authentication.getPrincipal();
		SysUsers sysUsers = sysUsersDao.getByUserName(user.getUsername());
//		SysUsers sysUsers = (SysUsers) authentication.getPrincipal();
		HttpSession session = httpServletRequest.getSession();
		// validate active or deactive
		if (sysUsers.isActive()) {
			isError = true;
			buildResponse(httpServletResponse, Constants.ValidateCodeService.LGI_002);
		}
		
		// validate password status
		if(Utils.isNullObject(sysUsers.getPasswordStatus()) || !(new Long(1)).equals(sysUsers.getPasswordStatus())) {
			isError = true;
			buildResponse(httpServletResponse, Constants.ValidateCodeService.LGI_004);
		}

		// validate date change password
		Date pwdDate = sysUsers.getPwdDate();
		SysSecurity security = sysSecurityDao.findByCodeAndActive(Constants.PW_EXPIRED_IN,true);
		if (!Utils.isNullObject(security)) {
			Date validateDate = DateUtils.addDate(pwdDate, Integer.valueOf(security.getValue()));
			if (validateDate.before(new Date())) {
				isError = true;
				buildResponse(httpServletResponse, Constants.ValidateCodeService.LGI_002);
			}
		}
		if (!isError) {
			// update user
			sysUsers.setLoginErrorTimes(new Long(0));
			sysUsers.setBlockTime(null);
			//sysUsersRepository.save(sysUsers);
			session.setAttribute(Constants.SESSION_KEY.USER_INFO, sysUsers);
		}
	}
	
	private void buildResponse (HttpServletResponse httpServletResponse, String message) throws NoSuchMessageException, IOException {
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServletResponse.setContentType("text/html; charset=UTF-8");
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.getWriter()
				.print(messageSource.getMessage(message, null, null));
		httpServletResponse.getWriter().flush();
	}
}