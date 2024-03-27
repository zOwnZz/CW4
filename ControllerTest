import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Test class for Controller to ensure data loading, filtering, and calculations are correctly implemented.
 */
public class ControllerTest {

    private Controller controller;

    /**
     * Sets up the test environment before each test case.
     * Initializes a new instance of the Controller class.
     */
    @Before
    public void setUp() {
        controller = new Controller();
    }

    /**
     * Tests that data is successfully loaded into the controller.
     * Asserts that the data map is not empty after loading.
     */
    @Test
    public void testLoadData() {
        assertFalse("Data should be loaded", controller.boroughAndData().isEmpty());
    }

    /**
     * Tests selecting data for a single date.
     * Updates data for the specified date and asserts that the data map is not empty.
     */
    @Test
    public void testSelectingSameDate() {
        LocalDate sameDate = LocalDate.of(2022, 2, 14);
        controller.updateData(new LocalDate[]{sameDate, sameDate});
        assertFalse("Data should be filtered for a single date", controller.boroughAndData().isEmpty());
    }

    /**
     * Tests selecting data between two different dates.
     * Updates data for the specified date range and asserts that the data map is not empty.
     */
    @Test
    public void testSelectingDifferentDates() {
        LocalDate startDate = LocalDate.of(2020, 2, 3);
        LocalDate endDate = LocalDate.of(2023, 2, 9);
        controller.updateData(new LocalDate[]{startDate, endDate});
        assertFalse("Data should be filtered between two dates", controller.boroughAndData().isEmpty());
    }

    /**
     * Tests that the calculated total deaths and average total cases are positive.
     * Asserts positivity for both metrics.
     */
    @Test
    public void testPositiveDeathsAndCases() {
        assertTrue("Total deaths should be positive", controller.calculateTotalDeaths() > 0);
        assertTrue("Average total cases should be positive", controller.calculateAverageTotalCases() > 0);
    }

    /**
     * Tests data and calculations for a specific date.
     * Updates data for the specified date and asserts expected values for deaths, average cases, parks GMR, and transit GMR.
     */
    @Test
    public void testDataForSpecificDate() {
        LocalDate specificDate = LocalDate.of(2022, 2, 14);
        controller.updateData(new LocalDate[]{specificDate, specificDate});
        
        assertEquals("Deaths should be 22 on 2022-02-14", 22, controller.calculateTotalDeaths());
        assertEquals("Average cases should be 189.52 on 2022-02-14", 189.52, controller.calculateAverageTotalCases(), 0.01);
        assertEquals("Parks GMR should be 23.03 on 2022-02-14", 23.03, controller.calculateAverageParksGMR(), 0.01);
        assertEquals("Transit GMR should be -38.7 on 2022-02-14", -38.7, controller.calculateAverageTransitGMR(), 0.01);
    }

    /**
     * Tests filtering of data between two dates.
     * Updates data for the specified date range and asserts that each data point is within the range.
     */
    @Test
    public void testDataFiltering() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 3, 31);
        controller.updateData(new LocalDate[]{startDate, endDate});
        assertTrue("Filtered data should not be empty", !controller.boroughAndData().isEmpty());

        // For each borough, iterate through its data and assert that each data point's date is within the specified range.
        controller.boroughAndData().forEach((borough, dataList) -> {
            dataList.forEach(data -> {
                LocalDate dataDate = LocalDate.parse(data.getDate());
                assertFalse("Data date should be within range", dataDate.isBefore(startDate) || dataDate.isAfter(endDate));
            });
        });
    }
}
