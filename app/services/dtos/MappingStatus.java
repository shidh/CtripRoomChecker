package services.dtos;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class MappingStatus {
    private String hotelId;
    private String hotelName;
    private boolean isMapped;
    private int mappingCount;
    private int totalRoomTypeCount;

    public int getTotalRoomTypeCount() {
        return totalRoomTypeCount;
    }

    public void setTotalRoomTypeCount(int totalRoomTypeCount) {
        this.totalRoomTypeCount = totalRoomTypeCount;
    }

    public MappingStatus() {
    }

    public MappingStatus(String hotelId, String hotelName, boolean isMapped, int mappingCount) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.isMapped = isMapped;
        this.mappingCount = mappingCount;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public boolean isMapped() {
        return isMapped;
    }

    public void setMapped(boolean mapped) {
        isMapped = mapped;
    }

    public int getMappingCount() {
        return mappingCount;
    }

    public void setMappingCount(int mappingCount) {
        this.mappingCount = mappingCount;
    }
}
