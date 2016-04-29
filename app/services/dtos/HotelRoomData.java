package services.dtos;

import java.util.List;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class HotelRoomData {
    private String id;
    private String name;
    private List<HotelRoom> roomList;
    private List<HotelSubRoom> subRoomList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HotelRoom> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<HotelRoom> roomList) {
        this.roomList = roomList;
    }

    public List<HotelSubRoom> getSubRoomList() {
        return subRoomList;
    }

    public void setSubRoomList(List<HotelSubRoom> subRoomList) {
        this.subRoomList = subRoomList;
    }
}
