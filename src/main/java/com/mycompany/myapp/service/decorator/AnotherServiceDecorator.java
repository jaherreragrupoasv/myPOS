package com.mycompany.myapp.service.decorator;

/**
 * Created by jaherrera on 27/03/2016.
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("2ndDecorator")
public class AnotherServiceDecorator implements IExampleService {

    @Autowired
    private IExampleService delegate;

    @Override
    public String startExample() {
        String decorated = delegate.startExample();
        return decorated + " , with another too";
    }

}

