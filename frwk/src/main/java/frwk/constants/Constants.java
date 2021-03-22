package frwk.constants;

import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public static final String DUPLICATE_CODE = "CMM_001";
	public static final String CMM_002 = "CMM_002";
	
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
	
	public static final String BM003 = "BM003";
	public static final String CIC_PARAM = "CIC_PARAM";
	public static final String DATA_TYPE = "DATA_TYPE";
	
	
	//msg login
	public static final String INFO_ERROR = "INFO_ERROR";
	public static final String EXPRIRED = "exprired";
	public static final String FIRSTS_LOGIN = "firstLogin";
	public static final String LOGIN_FAIL = "fail";
	public static final String LOGIN_EXPRIRED = "login_exprired";
	public static final String LOGIN_M6 = "LOGIN_M6";
	public static final String LOGIN_M6_1 = "LOGIN_M6_1";//trong user/pass
	public static final String LOGIN_M6_2 = "LOGIN_M6_2";// sai usser hoac pass
	
	public static final Integer STATUS_LOGIN_M6_0 = 0;// thanh cong
	public static final Integer STATUS_LOGIN_M6_1 = -1;//trong user/pass
	public static final Integer STATUS_LOGIN_M6_2 = -2;// sai usser hoac pass
	
	public static final String QA_CODE = "HOI_TIN";
	
	public static final List<String> lstPhapNhan = Arrays.asList(new String[] {"S10A","R14.DN","R10A","R20","XH51","XH50"});
}
