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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Query query2= JPA.em().createQuery("update Hotel set mapped=:mapped, name=:name where hotelId=:hotelId");
            query2.setParameter("mapped",mappingStatus.getMappingCount());
            String reg = "[\u4e00-\u9fa5]";
            Pattern pat = Pattern.compile(reg);
            Matcher mat=pat.matcher(mappingStatus.getHotelName());
            String name = mat.replaceAll("");
            query2.setParameter("name",name);
            query2.setParameter("hotelId",Integer.parseInt(mappingStatus.getHotelId()));
            query2.executeUpdate();
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
            mappingStatus.setHotelName("BMP Hotel?");
            mappingStatus.setMappingCount(0);
        }else {
            HotelRoomData hotelRoomData = bookingComRoomInfoRS.getHotelRoomData();
            mappingStatus.setHotelName(hotelRoomData.getName());
            mappingStatus.setMappingCount(hotelRoomData.getSubRoomList().size());
            hotelRoomData.getRoomList();
        }
        Logger.info(mappingStatus.getHotelId()+": "+mappingStatus.getHotelName()+": "+mappingStatus.getMappingCount());
        return mappingStatus;
    }

    public BookingComRoomInfoRS getBookingcomRoomInfoRS(String hotelId, String startDate, String endDate){
        String ctripEndpointRQ1 = "http://hotels.ctrip.com/international/Tool/AjaxHotelRoomInfoDetailPart.aspx?" +
                "hotel="+hotelId+"&EDM=F&urlReferedForOtherSeo=False&isApartment=F&StartDate="+startDate+
                "&DepDate="+endDate+"&RoomNum=2&IsNoLocalRoomHotel=T&UserUnicode=-1534711878&requestTravelMoney=F" +
                "&abt=B&promotionid=&t=1461814358558&childNum=2&FixSubHotel=F";

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
