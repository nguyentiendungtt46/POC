package frwk.filter.ws;

import javax.xml.transform.TransformerException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.ws.context.MessageContext;

import common.util.ResourceException;

public class HibernateWSSessionInterceptor
		extends org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor {
	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint) throws TransformerException {
		Session sess = sessionFactory.getCurrentSession();
		sess.getTransaction().begin();
		return super.handleRequest(messageContext, endpoint);
	}

	/**
	 * Logs the response message payload. Logging only occurs if {@code logResponse} is set to {@code true}, which is
	 * the default.
	 *
	 * @param messageContext the message context
	 * @return {@code true}
	 * @throws TransformerException when the payload cannot be transformed to a string
	 */
	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {

		logger.debug("Entering intercept()");
		try {
			Session sess = sessionFactory.getCurrentSession();

			sess.getTransaction().commit();

		} catch (Exception ex1) {
			Session sess = sessionFactory.getCurrentSession();
			if (sess.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				sess.getTransaction().rollback();
			logger.error(ex1);
			if (!(ex1 instanceof ResourceException)) {
				return super.handleResponse(messageContext, endpoint);
			}

		}
		return super.handleResponse(messageContext, endpoint);
	}

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
