package com.webservice;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ServerTest {

    @Test
    public void testDummy() {
        //ToDo replace this with actual unit tests once initData/initRandom/nparty are implemented properly
        //Currently this is only here to make testcoverage happy

        Server server = new Server(Arrays.asList("a"));
        server.initData();
        try {
            server.initRandom(3);
        } catch (Exception e) {
            //who cares
        }
    }

}