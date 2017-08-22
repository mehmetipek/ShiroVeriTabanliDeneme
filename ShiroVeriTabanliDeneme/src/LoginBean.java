import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

@ManagedBean
@SessionScoped
public class LoginBean {
    
	
	
    private String adi, password;
    private boolean rememberMe;

    
    
    
    public LoginBean() {
		super();
	}

	public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
    public String Login(){
        
        String returnPage = "/guest/index.xhtml?faces-redirect=true";
        
        Subject currentUser         = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(adi, password);
        
        token.setRememberMe(rememberMe);
        
        try{
            
            currentUser.login(token);
            if(currentUser.hasRole("admin"))
                
                returnPage = "/admin/index.xhtml?faces-redirect=true";
            
        } catch (UnknownAccountException uae ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Giriþ baþarýsýz", "kullanýcý adýnýz yanlýþ"));
            return null;
        } catch (IncorrectCredentialsException ice ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Giriþ baþarýsýz", "parolanýz yanlýþ"));
            return null;
        } catch (LockedAccountException lae ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Giriþ baþarýsýz", "Bu kullanýcý adý kilitli"));
            return null;
        } catch(AuthenticationException aex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Giriþ baþarýsýz", aex.toString()));
            System.out.println(aex);
            return null;
        }
        
        return returnPage;
    }
    public String logout() {
 
        Subject currentUser = SecurityUtils.getSubject();
        
        try {
            currentUser.logout();
        } catch (Exception ex) {
            
        }
        
        return "/login.xhtml?faces-redirect=true";
    }
    
    
    
}