package frwk.dao.hibernate;

import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;

public class EscapingLikeExpression extends LikeExpression {
	public EscapingLikeExpression(String propertyName, String value, MatchMode matchMode, Boolean ignoreCase) {
		super(propertyName, escapeString("%" + value + "%"), matchMode,'!', ignoreCase);
	}

	static String escapeString(String inputString) {
		inputString = inputString.replace("_", "!_");
		return inputString;
	}
}
