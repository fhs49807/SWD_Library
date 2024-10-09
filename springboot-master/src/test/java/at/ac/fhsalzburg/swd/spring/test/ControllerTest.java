package at.ac.fhsalzburg.swd.spring.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import at.ac.fhsalzburg.swd.spring.controller.TemplateController;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.UserRepository;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    // in these tests we focus on the controller, so we don't test the repo and mock the needed
    // behavior
    @MockBean
    private UserRepository repo;

    @Autowired
    TemplateController myController;

    @BeforeEach // this method will be run before each test
    public void setup() {
        // init webcontext
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        		.apply(SecurityMockMvcConfigurers.springSecurity())
        		.build();       
	
    }


    @Test
    public void whenControllerInjected_thenNotNull() throws Exception {
        assertThat(myController).isNotNull();
    }

    // Test /
    @Test
    @WithAnonymousUser
    public void givenNothing_whenHome_thenIndex() throws Exception {

        MockHttpSession session = new MockHttpSession(); // set session attribute (mocked session)
        session.setAttribute("count", 3);


        mvc.perform(MockMvcRequestBuilders.get("/").session(session) // set the mocked session
                .contentType(MediaType.TEXT_HTML))        
        		.andExpect(status().isOk())
                .andExpect(model().attributeExists("users")).andExpect(view().name("index"))
                .andExpect(request().sessionAttribute("count", is(4))); // check if session
                                                                        // attribute has changed
                                                                        // properly

    }

    // HTTP add customer test
    @Test
    @WithMockUser(username="test", roles = {"ADMIN"})
    public void givenCustomerForm_whenAddCustomer_thenRedirect() throws Exception {
        /*
         * submitting the form object itself does not work in the unit test (even flashAttr does not
         * work :() CustomerForm form = new CustomerForm(); form.setFirstName("Max");
         * form.setLastName("MusterMann"); form.setEMail("max@musterm.ann"); form.setTel("123");
         */

        mvc.perform(MockMvcRequestBuilders.post("/admin/addUser").param("username", "Max")
                .param("fullname", "Mustermann").param("password","Max").param("eMail", "max@musterm.ann")
                .param("tel", "1234").param("birthDate", "2000-01-01")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

    }


    // REST Service Test
    @Test
    @WithMockUser(username="test", roles = {"USER"})
    public void givenCustomer_whenGetCustomer_thenReturnJsonArrayTest() throws Exception {

        User customer = new User("Max", "Mustermann", "max@muster.com", "123", new Date(),"","USER",null);

        List<User> allCustomers = Arrays.asList(customer);

        // mock the repo: whenever findAll is called, we will get our predefined customer
        given(repo.findAll()).willReturn(allCustomers);

        // call REST service and check
        mvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].username", is(customer.getUsername())));


    }



}
