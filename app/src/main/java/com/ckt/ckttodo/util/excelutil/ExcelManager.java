package com.ckt.ckttodo.util.excelutil;

import android.util.Log;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * Created by zhiwei.li on 2017/3/3.
 */

public class ExcelManager {

    Map<String, Field> fieldCache = new HashMap<>();
    // private Map<String, Method> contentMethodsCache;
    private Map<Integer, String> titleCache = new HashMap<>();


    public <T> List<T> fromExcel(InputStream excelStream, Class<T> dataType) throws Exception {
        String sheetName = getSheetName(dataType);
        // 获取键值对儿
        List<Map<String, String>> title_content_values = getMapFromExcel(excelStream, sheetName);
        if (title_content_values == null || title_content_values.size() == 0) {
            return null;
        }

        Map<String, String> value0 = title_content_values.get(0);
        List<ExcelClassKey> keys = getKeys(dataType);
        // 判断在实体里的注解标题在excel表里是否存在，不存在就说明表格不能格式化为所需实体
        boolean isExist = false;
        for (int kIndex = 0; kIndex < keys.size(); kIndex++) {
            String title = keys.get(kIndex).getTitle();
            if (value0.containsKey(title)) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            return null;
        }

        List<T> datas = new ArrayList<>();
        fieldCache.clear();
        // 键值对儿映射数据
        for (int n = 0; n < title_content_values.size(); n++) {
            Map<String, String> title_content = title_content_values.get(n);
            T data = dataType.newInstance();
            for (int k = 0; k < keys.size(); k++) {
                // 根据title和字段值映射
                String title = keys.get(k).getTitle();
                String fieldName = keys.get(k).getFieldName();
                Field field = getField(dataType, fieldName);
                field.set(data, title_content.get(title));
            }
            datas.add(data);
        }
        return datas;
    }


    private String getSheetName(Class<?> clazz) {
        ExcelSheet sheet = clazz.getAnnotation(ExcelSheet.class);
        String sheetName = sheet.sheetName();
        return sheetName;
    }


    public List<Map<String, String>> getMapFromExcel(InputStream excelStream, String sheetName)
        throws Exception {
        // Workbook workBook = Workbook.getWorkbook(excelStream);
        Workbook workBook = Workbook.getWorkbook(excelStream);
        Sheet sheet = workBook.getSheet(sheetName);

        int yNum = sheet.getRows();// 行数
        // 只有标题或者什么都没有
        if (yNum <= 1) {
            return null;
        }
        int xNum = sheet.getColumns();// 列数
        // 一个字段都没有
        if (xNum <= 0) {
            return null;
        }
        List<Map<String, String>> values = new LinkedList<>();

        titleCache.clear();
        // yNum-1是数据的大小，去掉第一行标题
        for (int y = 0; y < yNum - 1; y++) {
            Map<String, String> value = new LinkedHashMap<>();
            for (int x = 0; x < xNum; x++) {
                // 读标题
                String title = getExcelTitle(sheet, x);
                // 读数据,读数据从第2行开始读
                String content = getContent(sheet, x, y + 1);
                value.put(title, content);
            }
            values.add(value);
        }

        workBook.close();
        return values;
    }


    private List<ExcelClassKey> getKeys(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<ExcelClassKey> keys = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            ExcelContent content = fields[i].getAnnotation(ExcelContent.class);
            if (content != null) {
                keys.add(new ExcelClassKey(content.titleName(), fields[i].getName()));
            }
        }
        return keys;

    }


    private Field getField(Class<?> type, String fieldName) throws Exception {
        Field f = null;

        if (fieldCache.containsKey(fieldName)) {
            f = fieldCache.get(fieldName);
        } else {
            f = type.getDeclaredField(fieldName);
            fieldCache.put(fieldName, f);
        }
        f.setAccessible(true);
        return f;
    }


    private String getExcelTitle(Sheet sheet, int x) {
        String title;
        if (titleCache.containsKey(x)) {
            title = titleCache.get(x);
        } else {
            title = getContent(sheet, x, 0);
            titleCache.put(x, title);
        }
        return title;
        // return getContent(sheet, x, 0);
    }


    private String getContent(Sheet sheet, int x, int y) {
        Cell contentCell = sheet.getCell(x, y);
        String content = contentCell.getContents();
        return content != null ? content : "";
    }
}
