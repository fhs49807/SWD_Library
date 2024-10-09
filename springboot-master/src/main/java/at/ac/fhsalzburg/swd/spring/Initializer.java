package at.ac.fhsalzburg.swd.spring;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import at.ac.fhsalzburg.swd.spring.config.sessionDataSourceConfiguration;

public class Initializer extends AbstractHttpSessionApplicationInitializer {

	public Initializer() {
		super(sessionDataSourceConfiguration.class);
	}
}
