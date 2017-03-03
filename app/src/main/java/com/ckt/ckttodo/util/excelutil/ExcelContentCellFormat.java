package com.ckt.ckttodo.util.excelutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应内容的单元格的格式
 * Created by zhiwei.li on 2017/3/3.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcelContentCellFormat {
    public String titleName();
}
