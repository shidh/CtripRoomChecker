package controllers;

import play.db.jpa.JPA;
import play.mvc.*;

import java.util.*;

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
            result.append(m.getHotelId());
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