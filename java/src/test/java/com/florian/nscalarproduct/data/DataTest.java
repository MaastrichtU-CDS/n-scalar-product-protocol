package com.florian.nscalarproduct.data;

import org.junit.jupiter.api.Test;

import static com.florian.nscalarproduct.data.Parser.parseCsv;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataTest {

    @Test
    public void testCreateNetwork() {
        Data d = parseCsv("resources/Experiments/k2/smallK2Example_firsthalf.csv", 0);
        assertEquals(d.getIndividualRow("1"), 0);
    }
}