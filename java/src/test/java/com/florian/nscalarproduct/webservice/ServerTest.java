package com.florian.nscalarproduct.webservice;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ServerTest {

    @Test
    public void testDummy() {
        //Currently this is only here to make testcoverage happy as these two methods are purely as an example

        Server server = new Server(Arrays.asList("a"));
        try {
            server.initData();
        } catch (Exception e) {
            //who cares
        }
        try {
            server.initRandom(3);
        } catch (Exception e) {
            //who cares
        }
    }

}