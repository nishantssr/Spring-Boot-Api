package com.humsare.eniro.service;
import java.util.Collection;
import com.humsare.eniro.model.Greeting;
public interface GreetingService {
	
    Collection<Greeting> findAll();

    Greeting findOne(Long id);

    Greeting create(Greeting greeting);

    Greeting update(Greeting greeting);

    void delete(Long id);
    
}
