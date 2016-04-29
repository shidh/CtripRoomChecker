package services.dtos;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class HotelSubRoom extends HotelRoom{
    private String baseRoomId;
    //private Price price;


    public String getBaseRoomId() {
        return baseRoomId;
    }

    public void setBaseRoomId(String baseRoomId) {
        this.baseRoomId = baseRoomId;
    }
}
