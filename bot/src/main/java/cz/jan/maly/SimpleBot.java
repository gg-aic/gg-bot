package cz.jan.maly;

import cz.jan.maly.service.AgentUnitFactory;
import cz.jan.maly.service.implementation.BotFacade;

import java.beans.IntrospectionException;
import java.io.IOException;

public class SimpleBot extends BotFacade {

    private SimpleBot() {
        super(new AgentUnitFactory());
    }

    public static void main(String[] args) throws IOException, InterruptedException, IntrospectionException {
        new SimpleBot().run();
    }

}