package at.ac.fhsalzburg.swd.spring.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.AbstractCondition;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import at.ac.fhsalzburg.swd.spring.dto.UserDTO;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.UserService;

// initial version from https://stackoverflow.com/questions/47929674/modelmapper-mapping-list-of-entites-to-list-of-dto-objects
public class ObjectMapperUtils {
	
	private final static ModelMapper modelMapper;
	
	/**
    * Hide from public usage.
    */
    private ObjectMapperUtils() {
    }
	
	static
	{
		modelMapper = new ModelMapper();
        
        // https://github.com/modelmapper/modelmapper/issues/319
        // string blank condition
		Condition<?, ?> isStringBlank = (Condition<?, ?>) new AbstractCondition<Object, Object>() {
		    @Override
			public boolean applies(MappingContext<Object, Object> context) {
			if(context.getSource() instanceof String) {
				return null!=context.getSource() && !"".equals(context.getSource());
			} else {
				return context.getSource() != null;
			}
		    }
		};
        
        // initial configuration
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
        								.setSkipNullEnabled(true) // skip null fields
        								.setPropertyCondition(isStringBlank); // skip empty strings 
        
        
        // create a typemap to override default behaviour for DTO to entity mapping
        TypeMap<UserDTO, User> typeMap = modelMapper.getTypeMap(UserDTO.class, User.class);
    	if (typeMap == null) {	    		
    		typeMap = modelMapper.createTypeMap(UserDTO.class, User.class);
    	}	    	
    	// create a provider to be able to merge the dto data with the data in the database:
    	// whenever we are mapping UserDTO to User, the data from UserDTO and the existing User in the database are merged    
    	Provider<User> userDelegatingProvider = new Provider<User>() {
       	
            public User get(ProvisionRequest<User> request) {
            	// it is also possible to get a service instance from the application context programmatically
            	UserService userService = (UserService) WebApplicationContextUtils.getWebApplicationContext(
            			((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServletContext())
            			.getBean("userService");
           	 	return userService.getByUsername(((UserDTO)request.getSource()).getUsername());
            }
    	};  
    	    	    	    	
    	// a provider to fetch a user instance from a repository
        typeMap.setProvider(userDelegatingProvider);
    	
        
	}
	    

    
    
    //public static ModelMapper getModelMapper() {
    //	return modelMapper;
    //}

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param entity   entity that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param entityList list of entities that needs to be mapped
     * @param outCLass   class of result list element
     * @param <D>        type of objects in result list
     * @param <T>        type of entity in <code>entityList</code>
     * @return list of mapped object with <code><D></code> type.
     */
    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }

    /**
     * Maps {@code source} to {@code destination}.
     *
     * @param source      object to map from
     * @param destination object to map to
     */
    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }

        
    
}
