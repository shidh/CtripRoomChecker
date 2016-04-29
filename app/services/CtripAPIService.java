package services;

import play.Logger;
import services.dtos.BookingComRoomInfoRS;
import services.dtos.HotelRoomData;
import services.dtos.MappingStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class CtripAPIService {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();

    public List<MappingStatus> getMappingStatusReportByHotelIds(List<Integer> hotelIds){
        List<MappingStatus> mappingStatusList = new ArrayList<MappingStatus>();
        for(int hotelId: hotelIds) {
            MappingStatus mappingStatus = getMappingStatusReportByHotelId(hotelId+"");
            mappingStatusList.add(mappingStatus);
        }
        return mappingStatusList;
    }

    public MappingStatus getMappingStatusReportByHotelId(String hotelId){
        MappingStatus mappingStatus = new MappingStatus();
        mappingStatus.setHotelId(hotelId);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date end = calendar.getTime();

        BookingComRoomInfoRS bookingComRoomInfoRS = getBookingcomRoomInfoRS(hotelId, dateFormat.format(start), dateFormat.format(end));

        if(bookingComRoomInfoRS.getHotelRoomData() == null) {
            mappingStatus.setMapped(false);
            mappingStatus.setMappingCount(0);
        }else {
            HotelRoomData hotelRoomData = bookingComRoomInfoRS.getHotelRoomData();
            mappingStatus.setHotelName(hotelRoomData.getName());
            mappingStatus.setMappingCount(hotelRoomData.getSubRoomList().size());
            hotelRoomData.getRoomList();
        }
        Logger.info(mappingStatus.getHotelId()+":"+mappingStatus.getHotelName()+":"+mappingStatus.getMappingCount());
        return mappingStatus;
    }

    public BookingComRoomInfoRS getBookingcomRoomInfoRS(String hotelId, String startDate, String endDate){
        String ctripEndpoint = "http://hotels.ctrip.com/international/Tool/AjaxHotelRoomInfoDetailPart.aspx?" +
                "hotel="+hotelId+"&EDM=F&urlReferedForOtherSeo=False&isApartment=F&StartDate="+startDate+
                "&DepDate="+endDate+"&RoomNum=2&IsNoLocalRoomHotel=T&UserUnicode=-1534711878&requestTravelMoney=F" +
                "&abt=B&promotionid=&t=1461814358558&childNum=2&FixSubHotel=F&userCouponPromoId=&SegmentationNo=2,3";
        BookingComRoomInfoRS bookingComRoomInfoRS = new BookingComRoomInfoRS();
        try {
            String bookingcomRoomInfoString = Utils.httpGet(ctripEndpoint);
            bookingComRoomInfoRS = Utils.fromJson(bookingcomRoomInfoString, BookingComRoomInfoRS.class);
        } catch (Exception e) {
            Logger.error("getBookingcomRoomInfoRS exception", e);
        }
        return bookingComRoomInfoRS;
    }

}
