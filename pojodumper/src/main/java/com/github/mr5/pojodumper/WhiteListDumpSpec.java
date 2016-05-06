package com.github.mr5.pojodumper;

import java.util.List;
import java.util.Map;

public interface WhiteListDumpSpec<V> {
    public List<String> getWhiteListFields();

    public Map<String, ValueGenerator<V>> getExtraFields();
}