package com.tenetmind.loans.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

/*
* The code for the class BigDecimalConverter* is taken from:
* http://vvratha.blogspot.com/2016/08/supporting-hibernate-precisionscale-for.html
* */

@Converter
public class BigDecimalConverter implements AttributeConverter<BigDecimal, Double> {

    @Override
    public Double convertToDatabaseColumn(BigDecimal bigDecimalValue) {
        if (bigDecimalValue == null) {
            return null;
        }

        return bigDecimalValue.doubleValue();
    }

    @Override
    public BigDecimal convertToEntityAttribute(Double doubleValue) {
        if (doubleValue == null) {
            return null;
        }

        return BigDecimal.valueOf(doubleValue);
    }

}
