package entity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.Formater;
import entity.frwk.SysUsers;
public class Document {
	 private Short status;
	    private Templates templates;

	    public void setStatus(Short status) {
	        this.status = status;
	        if (Document.TMP_STS_REJECT.equals(status) || Document.TMP_STS_DRAFT.equals(status)) {
	            this.setUploadId(null);
	            this.setUploadDt(null);
	            this.setChkUpldDt(null);
	            this.setChkUpldId(null);
	            this.setAppUpldFileSts(null);
	            this.setObjectId(null);
	            this.setObjectIdPdf(null);
	        }

	    }

	    public Short getStatus() {
	        return status;
	    }
	    private Date lchUpdt, chkUpldDt, uploadDt, checkerDt, submitDt;
	    private String maintainId, submitId, checkerId, uploadId, chkUpldId;
	    @JsonIgnore
	    private SysUsers createUser;
	    private Date createDt;
	    private String createDtStr;

	    public void setChkUpldDt(Date chkUpldDt) {
	        this.chkUpldDt = chkUpldDt;
	    }

	    public Date getChkUpldDt() {
	        return chkUpldDt;
	    }

	    public void setUploadDt(Date uploadDt) {
	        this.uploadDt = uploadDt;
	    }

	    public Date getUploadDt() {
	        return uploadDt;
	    }

	    public void setCheckerDt(Date checkerDt) {
	        this.checkerDt = checkerDt;
	    }

	    public Date getCheckerDt() {
	        return checkerDt;
	    }

	    public void setSubmitDt(Date submitDt) {
	        this.submitDt = submitDt;
	    }

	    public Date getSubmitDt() {
	        return submitDt;
	    }

	    public void setMaintainId(String maintainId) {
	        this.maintainId = maintainId;
	    }

	    public String getMaintainId() {
	        return maintainId;
	    }

	    public void setSubmitId(String submitId) {
	        this.submitId = submitId;
	    }

	    public String getSubmitId() {
	        return submitId;
	    }

	    public void setCheckerId(String checkerId) {
	        this.checkerId = checkerId;
	    }

	    public String getCheckerId() {
	        return checkerId;
	    }

	    public void setUploadId(String uploadId) {
	        this.uploadId = uploadId;
	    }

	    public String getUploadId() {
	        return uploadId;
	    }

	    public void setChkUpldId(String chkUpldId) {
	        this.chkUpldId = chkUpldId;
	    }

	    public String getChkUpldId() {
	        return chkUpldId;
	    }
	    public static final Short TMP_STS_DRAFT = 0;
	    public static final Short TMP_STS_PDNG_APPROVE = 1;
	    public static final Short TMP_STS_APPROVE = 2;
	    public static final Short TMP_STS_REJECT = 3;

	    public static final Short TMP_STS_PDNG_APPROVE_FILE = 4;
	    public static final Short TMP_STS_APPROVE_FILE = 5;
	    public static final Short TMP_STS_NOT_APPROVE_FILE = 6;


	    public static final String TMP_STS_DRAFT_NAME = "\u0110ang so\u1EA1n";
	    public static final String TMP_STS_PDNG_APPROVE_NAME = "Ch\u1EDD ph\u00EA duy\u00EAt";
	    public static final String TMP_STS_APPROVE_NAME = "\u0110\u00E3 duy\u1EC7t";
	    public static final String TMP_STS_REJECT_NAME = "T\u1EEB ch\u1ED1i";
	    public static final String TMP_STS_PDNG_APPROVE_FILE_NAME = "Ch\u1EDD duy\u1EC7t file";
	    public static final String TMP_STS_APPROVE_FILE_NAME = "\u0110\u00E3 duy\u1EC7t file";
	    public static final String TMP_STS_NOT_APPROVE_FILE_NAME = "T\u1EEB ch\u1ED1i file";


	    private static final Map MAP_ALL_STS = new HashMap<Short, String>();
	    public static final Map MAP_RM_STS = new HashMap<Short, String>();
	    public static final Map MAP_SP_STS = new HashMap<Short, String>();
	    
	    public static final Map MAP_RM_DEP = new HashMap<Short, String>();
	    static {
	        // MAP_ALL_STS
	        MAP_ALL_STS.put(TMP_STS_DRAFT, TMP_STS_DRAFT_NAME);
	        MAP_ALL_STS.put(TMP_STS_PDNG_APPROVE, TMP_STS_PDNG_APPROVE_NAME);
	        MAP_ALL_STS.put(TMP_STS_APPROVE, TMP_STS_APPROVE_NAME);
	        MAP_ALL_STS.put(TMP_STS_REJECT, TMP_STS_REJECT_NAME);
	        MAP_ALL_STS.put(TMP_STS_PDNG_APPROVE_FILE, TMP_STS_PDNG_APPROVE_FILE_NAME);
	        MAP_ALL_STS.put(TMP_STS_APPROVE_FILE, TMP_STS_APPROVE_FILE_NAME);
	        MAP_ALL_STS.put(TMP_STS_NOT_APPROVE_FILE, TMP_STS_NOT_APPROVE_FILE_NAME);

	        // MAP_RM_STS
	        MAP_RM_STS.put(TMP_STS_PDNG_APPROVE, TMP_STS_PDNG_APPROVE_NAME);
	        MAP_RM_STS.put(TMP_STS_APPROVE, TMP_STS_APPROVE_NAME);
	        MAP_RM_STS.put(TMP_STS_REJECT, TMP_STS_REJECT_NAME);
	        MAP_RM_STS.put(TMP_STS_PDNG_APPROVE_FILE, TMP_STS_PDNG_APPROVE_FILE_NAME);
	        MAP_RM_STS.put(TMP_STS_APPROVE_FILE, TMP_STS_APPROVE_FILE_NAME);
	        MAP_RM_STS.put(TMP_STS_NOT_APPROVE_FILE, TMP_STS_NOT_APPROVE_FILE_NAME);

	        // MAP_SP_STS
	        MAP_SP_STS.put(TMP_STS_APPROVE, TMP_STS_APPROVE_NAME);
	        MAP_SP_STS.put(TMP_STS_PDNG_APPROVE_FILE, TMP_STS_PDNG_APPROVE_FILE_NAME);
	        MAP_SP_STS.put(TMP_STS_APPROVE_FILE, TMP_STS_APPROVE_FILE_NAME);
	        MAP_SP_STS.put(TMP_STS_NOT_APPROVE_FILE, TMP_STS_NOT_APPROVE_FILE_NAME);

	        //MAP_RM_DEP
	        MAP_RM_DEP.put(TMP_STS_PDNG_APPROVE, TMP_STS_PDNG_APPROVE_NAME);
	        MAP_RM_DEP.put(TMP_STS_APPROVE, TMP_STS_APPROVE_NAME);
	        MAP_RM_DEP.put(TMP_STS_REJECT, TMP_STS_REJECT_NAME);
	    }


	    public String getStatusName() {
	        if (!TMP_STS_APPROVE.equals(this.status))
	            return (String)MAP_ALL_STS.get(this.status);

	        if (!Formater.isNull(this.getUploadId())) {
	            if (this.getAppUpldFileSts() == null)
	                return TMP_STS_PDNG_APPROVE_FILE_NAME;
	            if (this.getAppUpldFileSts())
	                return TMP_STS_APPROVE_FILE_NAME;
	            if (!this.getAppUpldFileSts())
	                return TMP_STS_NOT_APPROVE_FILE_NAME;
	        } else {
	            return TMP_STS_APPROVE_NAME;
	        }
	        return "Khong xac dinh";

	    }

	    public static String getStatusName(Short status) {
	        if (status == null)
	            return (String)MAP_ALL_STS.get(TMP_STS_DRAFT);
	        return (String)MAP_ALL_STS.get(status);
	    }
	    private String objectId;
	    private String objectIdPdf;

	    public String getObjectId() {
	        return this.objectId;
	    }

	    public void setObjectId(String pathFile) {
	        this.objectId = pathFile;
	    }

	    public void setObjectIdPdf(String objectIdPdf) {
	        this.objectIdPdf = objectIdPdf;
	    }

	    public String getObjectIdPdf() {
	        return objectIdPdf;
	    }

	    public Date getCreateDt() {
	        return this.createDt;
	    }

	    public void setCreateDt(Date createDt) {
	        this.createDt = createDt;
	        this.createDtStr = Formater.date2str(this.createDt);
	    }


	    public void setCreateDtStr(String createDtStr) {
	        this.createDtStr = createDtStr;
	    }

	    public String getCreateDtStr() {
	        return createDtStr;
	    }

	    public Date getLchUpdt() {
	        return this.lchUpdt;
	    }

	    public void setLchUpdt(Date lchUpdt) {
	        this.lchUpdt = lchUpdt;
	    }


	    public void setCreateUser(SysUsers createUser) {
	        this.createUser = createUser;
	    }

	    public SysUsers getCreateUser() {
	        return createUser;
	    }
	    private Boolean appUpldFileSts;

	    public void setAppUpldFileSts(Boolean appUpldFileSts) {
	        this.appUpldFileSts = appUpldFileSts;
	    }

	    public Boolean getAppUpldFileSts() {
	        return appUpldFileSts;
	    }

	    public void next(Templates template) {
	        String[] appDesc = template.getApproveRole().getValue().split(";");

	        if (this.getStatus() == null)
	            this.setStatus((short)0);

	        Short tempSts = this.getStatus().equals(Short.valueOf((short)3)) ? Short.valueOf((short)0) : this.getStatus();

	        for (int i = 0; i < appDesc.length - 1; i++) {
	            if (tempSts.equals(Short.valueOf(appDesc[i]))) {
	                this.setStatus(Short.valueOf(appDesc[i + 1]));
	                break;
	            }

	        }
	    }

	    public void setTemplates(Templates templates) {
	        this.templates = templates;
	    }

	    public Templates getTemplates() {
	        return templates;
	    }
}
