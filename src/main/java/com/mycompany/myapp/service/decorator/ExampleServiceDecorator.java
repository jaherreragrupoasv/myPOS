package com.mycompany.myapp.service.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ExampleServiceDecorator implements IExampleService {

    @Autowired
    @Qualifier("delegate")
    private IExampleService delegate;

    @Override
    public String startExample() {
        String started = delegate.startExample();
        return started + " and decorated with decorator example";
    }

}
