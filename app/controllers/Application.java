package controllers;

import play.db.jpa.JPA;
import play.mvc.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import services.CtripAPIService;
import services.dtos.MappingStatus;

import javax.persistence.Query;

public class Application extends Controller {
    private CtripAPIService ctripAPIService=new CtripAPIService();

    public void index() {
        Query query= JPA.em().createQuery("select distinct country from Hotel");

        List countryList = query.getResultList();
//        List<String> countryList=new ArrayList<String>();
//        if (result!=null){
//            Iterator iterator = result.iterator();
//            while( iterator.hasNext() ){
//                String countryName=iterator.next().toString();
//                countryList.add("<option value=\"" + countryName + "\">" + countryName + "</option>");
//            }
//        }

        render(countryList);
    }

    public String getCityList(String country){
        Query query= JPA.em().createQuery("select distinct city from Hotel where country=:country");
        query.setParameter("country",country);
        List<String> cityList = query.getResultList();
        StringBuilder hotelString=new StringBuilder();
        for(String city : cityList){
            hotelString.append(city);
            hotelString.append(",");
        }
        String result=hotelString.toString();

        return result.substring(0,result.length()-1);
    }
    public String getCountryResult(String country){
        Query query= JPA.em().createQuery("select distinct hotelId from Hotel where country=:country");
        query.setParameter("country",country);
        List hotelIdList = query.getResultList();
        List<MappingStatus> mappingStatusList=ctripAPIService.getMappingStatusReportByHotelIds(hotelIdList);
        for(MappingStatus mappingStatus : mappingStatusList){
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

        return "Done";
    }

    public String getCheckResult(String country,String city){
        Query query= JPA.em().createQuery("select distinct hotelId from Hotel where country=:country and city=:city");
        query.setParameter("country",country);
        query.setParameter("city",city);
        List hotelIdList = query.getResultList();
        List<MappingStatus> mappingStatusList=ctripAPIService.getMappingStatusReportByHotelIds(hotelIdList);
        //List<MappingStatus> mappingStatusList=new ArrayList<MappingStatus>();
//        MappingStatus a=new MappingStatus();
//        a.setHotelId("abc");
//        mappingStatusList.add(a);
        StringBuilder result=new StringBuilder();
        result.append("<table class=\"table table-bordered\">");
        for(MappingStatus m : mappingStatusList){
            result.append("<tr>");
            result.append("<td>");
            result.append("<a href=\"");
            result.append("http://hotels.ctrip.com/international/");
            result.append(m.getHotelId());
            result.append(".html\" target=\"_blank\">");
            result.append(m.getHotelId());
            result.append("</a>");
            result.append("</td>");
            result.append("<td>");
            result.append(m.getHotelName());
            result.append("</td>");
            result.append("<td>");
            result.append(m.getMappingCount());
            result.append("</td>");
            result.append("</tr>");
        }
        result.append("</table>");

        return result.toString();
    }

}