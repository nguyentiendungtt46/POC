package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CAT_PROVINCE")
public class Province implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ID")
    private String id;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    
	public Province() {
		super();
	}
	
	public Province(String id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
    
