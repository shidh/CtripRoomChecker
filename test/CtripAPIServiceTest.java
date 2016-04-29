import org.junit.Before;
import org.junit.Test;
import play.test.UnitTest;
import services.CtripAPIService;
import services.MappingStatus;
import services.dtos.BookingComRoomInfoRS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class CtripAPIServiceTest extends UnitTest {
    SimpleDateFormat dateFormat;
    Calendar calendar;
    Date date;
    CtripAPIService ctripAPIService;
    @Before
    public void setUp() {
        dateFormat = new SimpleDateFormat("yyyy-M-dd");
        calendar = Calendar.getInstance();
        date = new Date();
        ctripAPIService = new CtripAPIService();

    }

    @Test
    public void testGetBookingcomRoomInfoRS() {
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date end = calendar.getTime();
        BookingComRoomInfoRS bookingComRoomInfoRS = ctripAPIService.getBookingcomRoomInfoRS("904373",
                dateFormat.format(start), dateFormat.format(end));
        assertNotNull(bookingComRoomInfoRS);
    }

    @Test
    public void testGetMappingStatusReportByHotelId() {
        List<String> hotelIds = new ArrayList<String>();
        hotelIds.add("4403170");
        hotelIds.add("4403353");
        hotelIds.add("4403413");
        hotelIds.add("4403431");
        hotelIds.add("4403450");
        hotelIds.add("4403460");
        hotelIds.add("4403477");
        List<MappingStatus> mappingStatusList = ctripAPIService.getMappingStatusReportByHotelIds(hotelIds);
        assertNotNull(mappingStatusList);
    }

}
