package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {

    @Id
	private String username;
    private String fullname;
    private String eMail;
    private String tel;
    private Long credit;
    private Date birthDate;
    private String password;
    private String role;
    private String jwttoken;
    
    
    protected User() {}

    public User(String username, String fullname, String eMail, String tel, Date birth, String password, String role, String jwtToken) {
        this.username = username;
        this.fullname = fullname;
        this.eMail = eMail;
        this.tel = tel;
        this.birthDate = birth;
        this.setCredit((long) 0);
        this.password = password;
        this.role = role;
        this.jwttoken = jwtToken;        

    }

    public String getFullname() {
        return fullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getEMail() {
        return this.eMail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getCredit() {
        return credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
	}

}
