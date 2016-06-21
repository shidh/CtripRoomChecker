package services;

import play.Logger;
import play.db.jpa.JPA;
import services.dtos.BookingComRoomInfoRS;
import services.dtos.HotelRoomData;
import services.dtos.MappingStatus;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Allen Shi on 4/28/16.
 */
public class CtripAPIService {

    public List<MappingStatus> getMappingStatusReportByHotelIds(List<Integer> hotelIds, String checkIn, String checkOut){
        List<MappingStatus> mappingStatusList = new ArrayList<MappingStatus>();
        for(int hotelId: hotelIds) {
            MappingStatus mappingStatus = getMappingStatusReportByHotelId(hotelId+"",checkIn,checkOut);
            mappingStatusList.add(mappingStatus);
            Query query2= JPA.em().createQuery("update Hotel set mapped=:mapped, totalroomtype=:total where hotelId=:hotelId");
            query2.setParameter("mapped",mappingStatus.getMappingCount());
            query2.setParameter("total",mappingStatus.getTotalRoomTypeCount());
            query2.setParameter("hotelId",Integer.parseInt(mappingStatus.getHotelId()));
            query2.executeUpdate();

        }
        return mappingStatusList;
    }

    public MappingStatus getMappingStatusReportByHotelId(String hotelId, String checkIn, String checkOut){
        MappingStatus mappingStatus = new MappingStatus();
        mappingStatus.setHotelId(hotelId);



        BookingComRoomInfoRS bookingComRoomInfoRS = getBookingcomRoomInfoRS(hotelId, checkIn, checkOut);
        int totalRoomTypeCount = getTotalRoomTypeCount(hotelId, checkIn, checkOut);
        if(bookingComRoomInfoRS.getHotelRoomData() == null) {
            mappingStatus.setMapped(false);
            mappingStatus.setHotelName("BMP Hotel?");
            mappingStatus.setMappingCount(0);
        }else {
            HotelRoomData hotelRoomData = bookingComRoomInfoRS.getHotelRoomData();
            mappingStatus.setHotelName(hotelRoomData.getName());
            mappingStatus.setMappingCount(hotelRoomData.getSubRoomList().size());
            mappingStatus.setTotalRoomTypeCount(totalRoomTypeCount);
            hotelRoomData.getRoomList();
        }
        Logger.info(mappingStatus.getHotelId()+": "+mappingStatus.getHotelName()+": "+mappingStatus.getMappingCount()+": "+mappingStatus.getTotalRoomTypeCount());
        return mappingStatus;
    }

    private int getTotalRoomTypeCount(String hotelId, String startDate, String endDate){
        String ctripEndpointRQ = "http://hotels.ctrip.com/international/Tool/AjaxHotelRoomInfoDetailPart.aspx?" +
                "hotel="+hotelId+"&EDM=F&urlReferedForOtherSeo=False&isApartment=F&StartDate="+startDate+
                "&DepDate="+endDate+"&RoomNum=2&IsNoLocalRoomHotel=T&UserUnicode=-1534711878&requestTravelMoney=F" +
                "&abt=B&promotionid=&t=1461814358558&childNum=2&FixSubHotel=F&userCouponPromoId=&SegmentationNo=2,3";
        try {
            BookingComRoomInfoRS bookingComRoomInfoRS = new BookingComRoomInfoRS();
            String bookingcomRoomInfoStringRS = Utils.httpGet(ctripEndpointRQ);
            bookingComRoomInfoRS = Utils.fromJson(bookingcomRoomInfoStringRS, BookingComRoomInfoRS.class);
            if(bookingComRoomInfoRS.getHotelRoomData() == null || bookingComRoomInfoRS.getHotelRoomData().getRoomList() == null) return -1;
            int count=bookingComRoomInfoRS.getHotelRoomData().getRoomList().size();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }


    }

    public BookingComRoomInfoRS getBookingcomRoomInfoRS(String hotelId, String startDate, String endDate){
        String ctripEndpointRQ1 = "http://hotels.ctrip.com/international/Tool/AjaxHotelRoomInfoDetailPart.aspx?" +
                "hotel="+hotelId+"&EDM=F&urlReferedForOtherSeo=False&isApartment=F&StartDate="+startDate+
                "&DepDate="+endDate+"&RoomNum=2&IsNoLocalRoomHotel=T&UserUnicode=-1534711878&requestTravelMoney=F" +
                "&abt=B&promotionid=&t=1461814358558&childNum=2&FixSubHotel=F";
        //http://hotels.ctrip.com/international/Tool/AjaxHotelRoomInfoDetailPart.aspx?city=359&hotel=996628&EDM=F&urlReferedForOtherSeo=False&isApartment=F&StartDate=2016-06-24&DepDate=2016-06-25&RoomNum=2&IsNoLocalRoomHotel=T&UserUnicode=-1556634512&requestTravelMoney=F&abt=B&promotionid=&t=1466128675225&childNum=2&FixSubHotel=F&userCouponPromoId=&ShowFlightSource=F&SegmentationNo=3&SegmentationRPH=4516061709000101453&Coupons=&t2=1466128677783&eleven=n7q3wyijgMlBzE4Bq%2FRcVA%3D%3D
        String ctripEndpointRQ2 = "http://hotels.ctrip.com/international/Tool/AjaxHotelRoomInfoDetailPart.aspx?" +
                "hotel="+hotelId+"&EDM=F&urlReferedForOtherSeo=False&isApartment=F&StartDate="+startDate+
                "&DepDate="+endDate+"&RoomNum=2&IsNoLocalRoomHotel=T&UserUnicode=-1534711878&requestTravelMoney=F" +
                "&abt=B&promotionid=&t=1461814358558&childNum=2&FixSubHotel=F&userCouponPromoId=&SegmentationNo=2,3";
        BookingComRoomInfoRS bookingComRoomInfoRS = new BookingComRoomInfoRS();
        try {
            String bookingcomRoomInfoStringRS1 = Utils.httpGet(ctripEndpointRQ1);
            String bookingcomRoomInfoStringRS2 = Utils.httpGet(ctripEndpointRQ2);
            BookingComRoomInfoRS bookingComRoomInfoRS1 = Utils.fromJson(bookingcomRoomInfoStringRS1, BookingComRoomInfoRS.class);
            BookingComRoomInfoRS bookingComRoomInfoRS2 = Utils.fromJson(bookingcomRoomInfoStringRS2, BookingComRoomInfoRS.class);

            if(bookingComRoomInfoRS1.getHotelRoomData() == null){
                return bookingComRoomInfoRS1;
            }

            if(bookingComRoomInfoRS2.getHotelRoomData() == null){
                return bookingComRoomInfoRS2;
            }

            bookingComRoomInfoRS = bookingComRoomInfoRS1.getHotelRoomData().getSubRoomList().size() <
                    bookingComRoomInfoRS2.getHotelRoomData().getSubRoomList().size()?
                    bookingComRoomInfoRS1: bookingComRoomInfoRS2;

        } catch (Exception e) {
            Logger.error("getBookingcomRoomInfoRS exception", e);
        }
        return bookingComRoomInfoRS;
    }

}
