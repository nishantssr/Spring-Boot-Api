package com.humsare.eniro.service;

import java.util.Collection;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.humsare.eniro.model.Greeting;
import com.humsare.eniro.repository.GreetingRepository;

@Service
public class GreetingServiceBean implements GreetingService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
	public Greeting findOne(Long id) {
		logger.info("> findOne id:{}", id);
		Greeting greeting = greetingRepository.findOne(id);
		logger.info("< findOne id:{}", id);
		return greeting;
	}

	@Override
	public Greeting create(Greeting greeting) {
		logger.info("> create");

		if (greeting.getId() != null) {
			logger.error("Attempted to create a Greeting, but id attribute was not null.");
			throw new EntityExistsException("The id attribute must be null to persist a new entity.");
		}
		Greeting savedGreeting = greetingRepository.save(greeting);
		logger.info("< create");
		return savedGreeting;
	}

	@Override
	public Greeting update(Greeting greeting) {
		logger.info("> update id:{}", greeting.getId());
		Greeting greetingToUpdate = findOne(greeting.getId());
		if (greetingToUpdate == null) {
			logger.error("Attempted to update a Greeting, but the entity does not exist.");
			throw new NoResultException("Requested entity not found.");
		}
		greetingToUpdate.setText(greeting.getText());
		Greeting updatedGreeting = greetingRepository.save(greetingToUpdate);
		logger.info("< update id:{}", greeting.getId());
		return updatedGreeting;
	}

	@Override
	public void delete(Long id) {
		logger.info("> delete id:{}", id);
		greetingRepository.delete(id);
		logger.info("< delete id:{}", id);
	}

}
