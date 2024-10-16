package at.ac.fhsalzburg.swd.spring;

import java.io.Serializable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class TestBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int HashCode;

    public TestBean() {
        HashCode = System.identityHashCode(this);
    }

    @Bean(name = "singletonBean") // @Bean is a method-level annotation and a direct analog of the
                                  // XML <bean/> element. The annotation supports most of the
                                  // attributes offered by <bean/>, such as: init-method,
                                  // destroy-method, autowiring, lazy-init, dependency-check,
                                  // depends-on and scope.
                                  // https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html


    public TestBean getSingletonBean() {
        return new TestBean();
    }

    @Bean(name = "prototypeBean")
    @Scope("prototype") // "prototype" scopes a single bean definition to any number of object
                        // instances.
                        // https://docs.spring.io/spring-framework/docs/2.5.x/reference/beans.html#beans-factory-scopes
    public TestBean getPrototypeBean() {
        return new TestBean();
    }

    public int getHashCode() {
        return HashCode;
    }

    @Bean(name = "sessionBean")
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TestBean getSessionBean() {
        return new TestBean();
    }
}
