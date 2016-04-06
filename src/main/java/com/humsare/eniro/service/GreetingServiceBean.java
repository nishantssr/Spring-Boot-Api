package com.humsare.eniro.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import com.humsare.eniro.model.Greeting;
import com.humsare.eniro.repository.GreetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GreetingServiceBean implements GreetingService {
	private static BigInteger nextId;
    private static Map<BigInteger,Greeting> greetingMap;
    
    private static Greeting save(Greeting greeting){
    	if(greetingMap == null){
    		greetingMap= new HashMap<BigInteger,Greeting>();
    		nextId=BigInteger.ONE;
    	}
    	//If Update...
    	if(greeting.getId() !=null){
    		Greeting oldGreeting=greetingMap.get(BigInteger.valueOf(greeting.getId()));
    		if(oldGreeting ==null){
    			return null;
    		}
    		greetingMap.remove(BigInteger.valueOf(greeting.getId()));
    		greetingMap.put(BigInteger.valueOf(greeting.getId()),greeting);
    	}
    	//If Create..
    	greeting.setId(nextId.longValue());
    	nextId= nextId.add(BigInteger.ONE);
    	greetingMap.put(BigInteger.valueOf(greeting.getId()), greeting);
    	return greeting;
    	
    }
    private static boolean remove(BigInteger id){
    	Greeting deleteGreeting =greetingMap.remove(id);
    	if(deleteGreeting !=null){
    		return false;
    	}
    	return true;
    }
    
    
    static{
    	Greeting g1= new Greeting();
    	g1.setText("Helooo World");
    	save(g1);
    	
    	Greeting g2= new Greeting();
    	g2.setText("Helooo Aliens");
    	save(g2);
    }
	@Override
	public Collection<Greeting> findAll() {
		 Collection<Greeting> greetings =greetingMap.values();
		return greetings;
	}

	@Override
	public Greeting findOne(BigInteger id) {
		Greeting greeting = greetingMap.get(id);
		return greeting;
	}

	@Override
	public Greeting create(Greeting greeting) {
		Greeting savedGreeting = save(greeting);
		return savedGreeting;
	}

	@Override
	public Greeting update(Greeting greeting) {
		Greeting updatedGreeting = save(greeting);
		return null;
	}

	@Override
	public boolean delete(Long id) {
		boolean deleted=remove(BigInteger.valueOf(id));
    	
		return deleted;
	}
	
}











/*
@Service
//@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GreetingServiceBean implements GreetingService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The Spring Data repository for Greeting entities.
	
	@Autowired
	private GreetingRepository greetingRepository;

	@Override
	public Collection<Greeting> findAll() {
		logger.info("> findAll");

		Collection<Greeting> greetings = greetingRepository.findAll();

		logger.info("< findAll");
		return greetings;
	}

	@Override
	@Cacheable(value = "greetings", key = "#id")
	public Greeting findOne(Long id) {
		logger.info("> findOne id:{}", id);

		Greeting greeting = greetingRepository.findOne(id);

		logger.info("< findOne id:{}", id);
		return greeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greetings", key = "#result.id")
	public Greeting create(Greeting greeting) {
		logger.info("> create");

		// Ensure the entity object to be created does NOT exist in the
		// repository. Prevent the default behavior of save() which will update
		// an existing entity if the entity matching the supplied id exists.
		if (greeting.getId() != null) {
			// Cannot create Greeting with specified ID value
			logger.error("Attempted to create a Greeting, but id attribute was not null.");
			throw new EntityExistsException("The id attribute must be null to persist a new entity.");
		}

		Greeting savedGreeting = greetingRepository.save(greeting);

		logger.info("< create");
		return savedGreeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greetings", key = "#greeting.id")
	public Greeting update(Greeting greeting) {
		logger.info("> update id:{}", greeting.getId());

		// Ensure the entity object to be updated exists in the repository to
		// prevent the default behavior of save() which will persist a new
		// entity if the entity matching the id does not exist
		Greeting greetingToUpdate = findOne(greeting.getId());
		if (greetingToUpdate == null) {
			// Cannot update Greeting that hasn't been persisted
			logger.error("Attempted to update a Greeting, but the entity does not exist.");
			throw new NoResultException("Requested entity not found.");
		}

		greetingToUpdate.setText(greeting.getText());
		Greeting updatedGreeting = greetingRepository.save(greetingToUpdate);

		logger.info("< update id:{}", greeting.getId());
		return updatedGreeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "greetings", key = "#id")
	public void delete(Long id) {
		logger.info("> delete id:{}", id);

		greetingRepository.delete(id);

		logger.info("< delete id:{}", id);
	}

}*/
