package com.florian.nscalarproduct.data;

import com.florian.nscalarproduct.error.InvalidDataFormatException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;

import static com.florian.nscalarproduct.data.Parser.parseData;
import static com.florian.nscalarproduct.webservice.Server.checkHorizontalSplit;
import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    @Test
    public void testLoadData() throws IOException, InvalidDataFormatException {
        Data d = parseData("resources/Experiments/k2/smallK2Example_firsthalf.csv", 0);
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

        Data d2 = parseData("resources/Experiments/k2/horizontalsplit.csv", 0);
        assertEquals(d2.getIndividualRow("1"), 0);
        assertTrue(d2.hasHorizontalSplit());

        checkHorizontalSplit(d2, localData);
        for (int i = 0; i < localData.length; i++) {
            // All values have locallyPresent set to false, so all values in localData switch to true
            assertEquals(localData[i], BigInteger.ONE);
        }
    }

    @Test
    public void testLoadParquet() throws IOException, InvalidDataFormatException {
        //This will spit out a crapton of logging
        //This only happens when testing, does not happen when actually running
        //Can't figure out how the hell to turn that off
        Data d2 = parseData("resources/Experiments/k2/tabelletje.parquet", 0);
        BigInteger[] localData = new BigInteger[10];
        for (int i = 0; i < localData.length; i++) {
            localData[i] = BigInteger.ZERO;
        }

        assertEquals(d2.getIndividualRow("1"), 0);
        assertTrue(d2.hasHorizontalSplit());

        checkHorizontalSplit(d2, localData);
        for (int i = 0; i < localData.length; i++) {
            // All values have locallyPresent set to false, so all values in localData switch to true
            assertEquals(localData[i], BigInteger.ONE);
        }
    }


    @Test
    public void testLoadParquetUnknown() throws IOException, InvalidDataFormatException {
        //This will spit out a crapton of logging
        //This only happens when testing, does not happen when actually running
        //Can't figure out how the hell to turn that off
        Data d2 = parseData("resources/Experiments/k2/unknown.parquet", 0);

        //the first row contains unknown values
        assertTrue(d2.getData().get(1).get(0).isUnknown());
        assertTrue(d2.getData().get(2).get(0).isUnknown());
        assertTrue(d2.getData().get(3).get(0).isUnknown());
        assertTrue(d2.getData().get(4).get(0).isUnknown());
    }

    @Test
    public void testLoadArff() throws IOException, InvalidDataFormatException {
        Data d2 = parseData("resources/Experiments/k2/allTypes.arff", 0);
        BigInteger[] localData = new BigInteger[10];
        for (int i = 0; i < localData.length; i++) {
            localData[i] = BigInteger.ZERO;
        }

        assertEquals(d2.getIndividualRow("1"), 0);
        assertTrue(d2.hasHorizontalSplit());

        checkHorizontalSplit(d2, localData);
        for (int i = 0; i < localData.length; i++) {
            // All values have locallyPresent set to false, so all values in localData switch to true
            assertEquals(localData[i], BigInteger.ONE);
        }
    }

    @Test
    public void testLoadException() {
        assertThrows(InvalidDataFormatException.class, () -> {
            parseData("resources/Experiments/k2/allTypes.nonsense", 0);
        });
    }
}