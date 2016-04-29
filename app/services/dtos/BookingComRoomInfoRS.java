package services.dtos;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class BookingComRoomInfoRS {
    private HotelRoomData HotelRoomData;

    private String LoadStatus;

    private String ResultCode;

    public services.dtos.HotelRoomData getHotelRoomData() {
        return HotelRoomData;
    }

    public void setHotelRoomData(services.dtos.HotelRoomData hotelRoomData) {
        HotelRoomData = hotelRoomData;
    }

    public String getLoadStatus() {
        return LoadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        LoadStatus = loadStatus;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }
}
