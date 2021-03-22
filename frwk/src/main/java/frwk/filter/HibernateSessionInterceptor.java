package frwk.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import common.util.ResourceException;

public class HibernateSessionInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = LogManager.getLogger(HibernateSessionInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Session sess = sessionFactory.getCurrentSession();

		sess.getTransaction().begin();

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("Entering intercept()");
		try {
			Session sess = sessionFactory.getCurrentSession();

			sess.getTransaction().commit();

		} catch (Exception ex1) {
			Session sess = sessionFactory.getCurrentSession();
			if (sess.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				sess.getTransaction().rollback();
			logger.error(ex1.getMessage(), ex1);
			if (!(ex1 instanceof ResourceException)) {
				super.afterCompletion(request, response, handler, ex1);
				return;
			}

		}
		super.afterCompletion(request, response, handler, ex);
	}

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() throws HibernateException {
		return sessionFactory.getCurrentSession();
	}

	public void end() throws Exception {
		try {
			Session sess = sessionFactory.getCurrentSession();
			sess.getTransaction().commit();

		} catch (Exception ex) {
			Session sess = sessionFactory.getCurrentSession();
			if (sess.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				sess.getTransaction().rollback();
			throw ex;
		}
	}
}
