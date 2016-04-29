package services;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class MappingStatus {
    private String hotelId;
    private String hotelName;
    private boolean isMapped;
    private int mappingCount;

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
