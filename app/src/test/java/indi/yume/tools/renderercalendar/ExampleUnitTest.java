package indi.yume.tools.renderercalendar;

import org.junit.Test;

import indi.yume.tools.renderercalendar.model.DayDate;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testDayDateHash() {
        DayDate dayDate = new DayDate();
        DayDate dayDate2 = new DayDate();
        dayDate2.addMonth(-10);

        for(int i = 0; i < 300; i++) {
            if(dayDate.hashCode() == dayDate2.hashCode()
                    && !dayDate.equals(dayDate2))
                assertTrue(false);
        }
    }
}