/**
 * 
 */
package vn.com.cmc.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author nvtiep
 *
 *         created: Dec 26, 2019 9:33:52 AM
 */
public class Constants {
	public static final Long LOG_STATUS_SUCCESS = new Long(1);
	public static final Long LOG_STATUS_BLOCK = new Long(0);
	public static final Long LOG_STATUS_ERROR = new Long(-1);
	public final static String LOG_MESSAGE_SUCCESS = "Success";
	public static final int PN_END_INT = 30;
	public static final int TN_START_INT = 31;
	public static final String LOG_STATUS_DI = "1";
	public static final String LOG_STATUS_DEN = "2";
	
	public final static String KT_IP_TCTD = "KT_IP_TCTD";
	public final static String PW_EXPIRED_IN = "PW_EXPIRED_IN";
	public final static String PW_ERROR_TIMES = "BM003";
	
	public static class PERIOD_CONFIG {
		public static final BigDecimal PERIOD_SECONDS = new BigDecimal("1");
		public static final BigDecimal PERIOD_MINUTES = new BigDecimal("2");
		public static final BigDecimal PERIOD_HOURS = new BigDecimal("3");
		public static final BigDecimal PERIOD_DAY = new BigDecimal("4");
		public static final BigDecimal PERIOD_MONTH = new BigDecimal("5");
	}
	
	public static class SESSION_KEY {
		public static final String USER_INFO = "USER_INFO";
		public static final String LOGIN_ERROR_MESSAGE = "LOGIN_ERROR_MESSAGE";
	}

	public static class Oauth2Exception {
		public static final String ACCESS_DENIED = "access_denied";
		public static final String DESCRIPTION = "error_description";
		public static final String ERROR = "error";
		public static final String INSUFFICIENT_SCOPE = "insufficient_scope";
		public static final String INVALID_CLIENT = "invalid_client";
		public static final String INVALID_GRANT = "invalid_grant";
		public static final String INVALID_REQUEST = "invalid_request";
		public static final String INVALID_SCOPE = "invalid_scope";
		public static final String INVALID_TOKEN = "invalid_token";
		public static final String REDIRECT_URI_MISMATCH = "redirect_uri_mismatch";
		public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
		public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
		public static final String UNSUPPORTED_RESPONSE_TYPE = "unsupported_response_type";
		public static final String URI = "error_uri";
	}

	public static class RESPONSE {
		public static final int SUCCESS_STATUS = 200;
		public static final String SUCCESS_CODE = "SUCCESS";
		public static final String SUCCESS_MESSAGE = "Thực hiện thành công!";

		public static final int EXCEPTION_STATUS = 0;
		public static final String EXCEPTION_CODE = "EXCEPTION";
		public static final String EXCEPTION_MESSAGE = "Thực hiện không thành công!";

		public static final String FILE_ERROR_MESSAGE = "Không được đặt tên File Sheet Excel khác <b>DANHSACHVPOINT</b> hoặc không được để <b>Trống</b> data trong file!";
		public static final String FILE_ERROR = "Đã có lỗi trong quá trình xử lý file, vui lòng tải lại theo đúng mẫu quy định.";
		public static final String FILE_ERROR_CHAR = "Mã Vpoint phải > 4 ký tự và <= 16 ký tự ! ";
		public static final String FILE_SUCCESS = "Thêm mới thành công ";
		public static final String FILE_FAIL = "Thêm mới thất bại ";
		public static final String FILE_TOTAL_DATA = " bản ghi ";
		public static final String FILE_SPACE = " ";
		public static final String FILE_LOADING = "Đang tải...";
		public static final String MA_NHIEM_KY_EXIST = "File không đúng quy định hoặc - Mã nhiệm kỳ đã tồn tại";
		public static final String FILE_ERROR_SIZE = "File Excel không được quá 1000 bản ghi ";
		public static final String FILE_ERROR_LENGTH = "File Excel không được quá 1MB ";

		public static final String ORG_CODE_01 = "Mã nhiệm kỳ không được để trống !";
		public static final String ORG_CODE_02 = "Mã nhiệm kỳ không được > 32 ký tự !";
		public static final String ORG_CODE_03 = "CĐ NGÀNH TW, CĐ TỔNG CÔNG TY TRỰC THUỘC TLĐ(TLĐ giao chỉ tiêu PTĐV là chỉ tiêu phát triển mới)";

		public static final String KPI_01 = "Kpi không được để trống !";
		public static final String KPI_02 = "Chỉ tiêu nhiệm kỳ không được > 15 ký tự !";
		public static final String KPI_03 = "chỉ tiêu nhiệm kỳ phải là số nguyên!";

	}
	
	public static class DATATABLE_PARAM {
		public static final String DATATABLE_DATA = "data";
		public static final String DATATABLE_REQUEST = "dataTableInRQ";
		public static final String DATA_PARAM = "paramData";
	}
	
	public static class LOG_CHECK_CONNECT {
		public static final Long STATUS_CONNECT_FAIL = 0L;
		public static final Long STATUS_CONNECT_SUCCESS = 1L;
	}
	
	public static class QuestionAnswer {

		// public static final String[] PHAP_NHAN = { "16", "15", "31", "14" };
		public static final String[] PHAP_NHAN = { "S10A", "R10A", "R14.DN", "R20", "XH50", "XH51" };
		public static final String OPTION_GET_ALL_NOT_RESPONSE_AND_NOT_REPLY = "0";
		public static final String OPTION_GET_ALL_NOT_RESPONSE_AND_REPLY = "1";
		public static final String OPTION_GET_ALL = "2";
	}
	
	public static class ValidateCodeService {

		public static final String STATUS_SUCCESS = "0";
		public static final String STATUS_ERROR = "1";

		public static final String LGI_001 = "LGI_001";
		public static final String LGI_002 = "LGI_002";
		public static final String LGI_003 = "LGI_003";
		public static final String LGI_004 = "LGI_004";
		public static final String LGI_005 = "LGI_005";
		public static final String LGI_006 = "LGI_006";
		public static final String LGI_007 = "LGI_007";
		public static final String LGI_008 = "LGI_008";
		public static final String LGI_999 = "LGI_999";
		
		public static final String CPW_001 = "CPW_001";
		public static final String CPW_002 = "CPW_002";
		public static final String CPW_003 = "CPW_003";
		public static final String CPW_004 = "CPW_004";
		public static final String CPW_005 = "CPW_005";
		public static final String CPW_006 = "CPW_006";
		public static final String CPW_007 = "CPW_007";
		public static final String CPW_008 = "CPW_008";
		public static final String CPW_999 = "CPW_999";
		
		public static final String CMM_000 = "CMM_000";
		public static final String CMM_002 = "CMM_002";
		public static final String CMM_003 = "CMM_003";
		public static final String CMM_004 = "CMM_004";
		public static final String CMM_005 = "CMM_005";
		public static final String CMM_006 = "CMM_006";
		public static final String CMM_007 = "CMM_007";
		public static final String CMM_008 = "CMM_008";
		public static final String CMM_009 = "CMM_009";
		public static final String CMM_010 = "CMM_010";
		public static final String CMM_999 = "CMM_999";

		public static final String QNA_101 = "QNA_101";
		public static final String QNA_102 = "QNA_102";
		public static final String QNA_103 = "QNA_103";
		public static final String QNA_104 = "QNA_104";
		public static final String QNA_105 = "QNA_105";
		public static final String QNA_106 = "QNA_106";
		public static final String QNA_107 = "QNA_107";
		public static final String QNA_108 = "QNA_108";
		public static final String QNA_109 = "QNA_109";
		public static final String QNA_110 = "QNA_110";
		public static final String QNA_111 = "QNA_111";
		public static final String QNA_112 = "QNA_112";
		public static final String QNA_113 = "QNA_113";
		public static final String QNA_114 = "QNA_114";
		public static final String QNA_115 = "QNA_115";
		public static final String QNA_116 = "QNA_116";
		public static final String QNA_117 = "QNA_117";
		public static final String QNA_118 = "QNA_118";
		public static final String QNA_119 = "QNA_119";
		public static final String QNA_120 = "QNA_120";
		public static final String QNA_121 = "QNA_121";
		public static final String QNA_122 = "QNA_122";
		public static final String QNA_126 = "QNA_126";
		public static final String QNA_127 = "QNA_127";
		public static final String QNA_128 = "QNA_128";
		public static final String QNA_129 = "QNA_129";
		public static final String QNA_130 = "QNA_130";
		public static final String QNA_131 = "QNA_131";
		public static final String QNA_132 = "QNA_132";
		public static final String QNA_133 = "QNA_133";
		public static final String QNA_134 = "QNA_134";
		public static final String QNA_135 = "QNA_135";
		
		public static final String QAF_001 = "QAF_001";
		public static final String QAF_003 = "QAF_003";
		public static final String QAF_005 = "QAF_005";
		public static final String QAF_002 = "QAF_002";
		public static final String QAF_006 = "QAF_006";
		public static final String QAF_007 = "QAF_007";
		public static final String QAF_008 = "QAF_008";
		
		public static final String QAC_001 = "QAC_001";
		public static final String QAC_002 = "QAC_002";
		public static final String QAC_003 = "QAC_003";
		public static final String QAC_999 = "QAC_999";
		
		public static final String TK11_001 = "TK11_001";
		public static final String TK11_002 = "TK11_002";
		public static final String TK11_003 = "TK11_003";
		public static final String TK11_004 = "TK11_004";
		
		public static final String ERROR_CHECK_SUM = "SUM_999";
		
		public static final String FORMAT_DATA_TYPE = "FORMAT_DATA_TYPE";
		public static final String MAX_LENGTH = "MAX_LENGTH";
		public static final String REQUIRED = "REQUIRED";
		public static final String REQUIRED_01 = "REQUIRED_01";
		public static final String CHECK_MAPPING = "CHECK_MAPPING";
		public static final String CHECK_MAPPING_DEP = "CHECK_MAPPING_DEP";
		
		public static final String CIC_001 = "CIC_001";
		public static final String CIC_002 = "CIC_002";
	}
	public static class SYS_JOB_CONFIG {
		public static final Long STATUS_ACTIVE = 1L;
		public static final Long STATUS_DEACTIVE = 0L;
		public static final String JOB_CREATE_ALERT_EMAIL = "CREATE_ALERT_EMAIL";
		public static final List<String> lstResCode = Arrays.asList(new String[]{"CMM_002", "CMM_003", "CMM_004", "CMM_006", "CMM_007", "CMM_008"});
		public static final String JOB_01 = "01";
		public static final String JOB_02 = "02";
		public static final String JOB_53 = "53";
		public static final String JOB_54 = "54";
		public static final String JOB_55 = "55";
		public static final String JOB_56 = "56";
		public static final String JOB_57 = "57";
		public static final String JOB_58 = "58";
		public static final String JOB_59 = "59";
		public static final String JOB_60 = "60";
		public static final String JOB_86 = "86";
		public static final String JOB_87 = "87";
		public static final String JOB_CIC_REPORT = "CIC_REPORT";
		public static final String JOB_FX_QNA_ANSWER = "FX_QNA_ANSWER";
		public static final String JOB_QNA_FILE_ANSWER = "QNA_FILE_ANSWER";
		public static final String JOB_EXCHANGE_OUT = "EXCHANGE_OUT";
		public static final String JOB_SEND_EMAIL = "SEND_EMAIL";
	}
}
