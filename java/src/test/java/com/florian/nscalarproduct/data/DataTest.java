package com.florian.nscalarproduct.data;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.florian.nscalarproduct.data.Parser.parseCsv;
import static com.florian.nscalarproduct.webservice.Server.checkHorizontalSplit;
import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    @Test
    public void testLoadData() {
        Data d = parseCsv("resources/Experiments/k2/smallK2Example_firsthalf.csv", 0);
        assertEquals(d.getIndividualRow("1"), 0);
        assertFalse(d.hasHorizontalSplit());
        BigInteger[] localData = new BigInteger[10];
        for (int i = 0; i < localData.length; i++) {
            localData[i] = BigInteger.ZERO;
        }
        checkHorizontalSplit(d, localData);
        for (int i = 0; i < localData.length; i++) {
            // locallyPresent is not present in this dataset so it won't change the localData array
            assertEquals(localData[i], BigInteger.ZERO);
        }

        Data d2 = parseCsv("resources/Experiments/k2/horizontalsplit.csv", 0);
        assertEquals(d2.getIndividualRow("1"), 0);
        assertTrue(d2.hasHorizontalSplit());

        checkHorizontalSplit(d2, localData);
        for (int i = 0; i < localData.length; i++) {
            // All values have locallyPresent set to false, so all values in localData switch to true
            assertEquals(localData[i], BigInteger.ONE);
        }
    }
}