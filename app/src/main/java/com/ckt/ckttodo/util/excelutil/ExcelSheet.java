package com.ckt.ckttodo.util.excelutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应Excel里的表名
 * Created by zhiwei.li on 2017/3/3.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelSheet {
    public String sheetName();
}
