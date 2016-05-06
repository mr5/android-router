package com.github.mr5.pojodumper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dumper {
    private static Dumper instance;

    static {
        instance = new Dumper();
    }

    public static Dumper getInstance() {
        if (instance == null) {
            instance = new Dumper();
        }

        return instance;
    }

    private Dumper() {
    }

    public <V> Map<String, Object> blackListDump(
            V valueObject,
            BlackListDumpSpec<V> blackListDumpSpec
    ) {
        return blackListDumpSpec(valueObject, blackListDumpSpec, new HashMap<String, Object>());
    }

    public <V> Map<String, Object> blackListDumpSpec(
            V valueObject,
            BlackListDumpSpec<V> blackListDumpSpec,
            Map<String, Object> destMap
    ) {
        return destMap;
    }
    public <V> Map<String,Object> whiteListDump(String[] fieldsWhiteList, V valueObject) {
        HashMap<String,Object> destMap = new HashMap<>();
        return destMap;
    }
    public <V> Map<String,Object> whiteListDump(String[] fieldsWhiteList, V valueObject, Map<String, ValueGenerator<V>> extraFields) {
        HashMap<String,Object> destMap = new HashMap<>();
        return destMap;
    }
    public <V> Map<String,Object> blackListDump(String[] fieldsBlackList, V valueObject) {
        HashMap<String,Object> destMap = new HashMap<>();
        return destMap;
    }
    public <V> Map<String, Object> whiteListDump(
            V valueObject,
            WhiteListDumpSpec<V> whiteListDumpSpec
    ) {
        return whiteListDump(valueObject, whiteListDumpSpec, new HashMap<String, Object>());
    }


    public <V> Map<String, Object> whiteListDump(
            V valueObject,
            WhiteListDumpSpec<V> whiteListDumpSpec,
            Map<String, Object> destMap
    ) {
        if (valueObject == null || destMap == null || whiteListDumpSpec == null) {
            return null;
        }

        Field[] fields = valueObject.getClass().getDeclaredFields();
        List<String> fieldsWhiteList = whiteListDumpSpec.getWhiteListFields();
        for (Field field : fields) {
            boolean originAccessible = field.isAccessible();
            // make field accessible
            if (!originAccessible) {
                field.setAccessible(true);
            }
            if (fieldsWhiteList.contains(field.getName())) {
                try {
                    destMap.put(field.getName(), field.get(valueObject));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            // restore field accessible
            if (!originAccessible) {
                field.setAccessible(false);
            }
        }
        Map<String, ValueGenerator<V>> extraFields = whiteListDumpSpec.getExtraFields();
        getExtraFields(extraFields, valueObject, destMap);


        return destMap;
    }

    protected <V> void getExtraFields(Map<String, ValueGenerator<V>> extraFields, V valueObject, Map<String, Object> destMap) {
        if (destMap == null || extraFields == null) {
            return;
        }
        for (String fieldName : extraFields.keySet()) {
            ValueGenerator<V> valueGenerator = extraFields.get(fieldName);
            Object fieldValue = null;
            if (valueGenerator != null) {
                fieldValue = valueGenerator.generate(valueObject);
            }

            destMap.put(fieldName, fieldValue);
        }
    }
}



