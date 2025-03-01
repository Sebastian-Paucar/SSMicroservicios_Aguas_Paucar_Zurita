package com.espe.app.soap.endpoints;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
    public static XMLGregorianCalendar toXMLGregorianCalendar(LocalDate localDate) {
        try {
            if (localDate == null) {
                return null;
            }
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            javax.xml.datatype.DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            return datatypeFactory.newXMLGregorianCalendar(
                    new java.text.SimpleDateFormat("yyyy-MM-dd").format(date)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error converting LocalDate to XMLGregorianCalendar", e);
        }
    }
}
