package com.humsare.eniro.web.api;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.humsare.eniro.model.Greeting;
import com.humsare.eniro.service.GreetingService;



/**
 * The GreetingController class is a RESTful web service controller. The
 * <code>@RestController</code> annotation informs Spring that each
 * <code>@RequestMapping</code> method returns a <code>@ResponseBody</code>
 * which, by default, contains a ResponseEntity converted into JSON with an
 * associated HTTP status code.
 * 
 * 
 */
@RestController
public class GreetingController extends BaseController{

    /**
     * The GreetingService business service.
     */
    @Autowired
    private GreetingService greetingService;
    
    @RequestMapping(
            value = "/api/greetings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Greeting>> getGreetings() {
        logger.info("> getGreetings");

        Collection<Greeting> greetings = greetingService.findAll();
    	
        logger.info("< getGreetings");
        return new ResponseEntity<Collection<Greeting>>(greetings,
                HttpStatus.OK);
    }
  
    
    @RequestMapping(
            value = "/api/greetings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> getGreeting(@PathVariable("id") Long id) {
        logger.info("> getGreeting id:{}", id);
        Greeting greeting = greetingService.findOne(id);
        if (greeting == null) {
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }
        logger.info("< getGreeting id:{}", id);
        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/greetings",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> createGreeting(
            @RequestBody Greeting greeting) {
       logger.info("> createGreeting");
       Greeting savedGreeting = greetingService.create(greeting);
       logger.info("< createGreeting");
        return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CREATED);
    }       
     
    @RequestMapping(
            value = "/api/greetings/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> updateGreeting(
            @RequestBody Greeting greeting) {
        logger.info("> updateGreeting id:{}", greeting.getId());
        Greeting updatedGreeting = greetingService.update(greeting);
        if (updatedGreeting == null) {
           return new ResponseEntity<Greeting>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
       logger.info("< updateGreeting id:{}", greeting.getId());
        return new ResponseEntity<Greeting>(updatedGreeting, HttpStatus.OK);
    }

   
    @RequestMapping(
            value = "/api/greetings/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Greeting> deleteGreeting(
            @PathVariable("id") Long id) {
        logger.info("> deleteGreeting id:{}", id);
    	greetingService.delete(id);;
    	//if(!deleted){
    	//	return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR); 
    //	}
       // greetingService.delete(id);

        logger.info("< deleteGreeting id:{}", id);
        return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
    }

    

    
}
