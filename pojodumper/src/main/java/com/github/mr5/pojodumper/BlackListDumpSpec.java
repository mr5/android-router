package com.github.mr5.pojodumper;

import java.util.List;
import java.util.Map;

public interface BlackListDumpSpec<V> {
    public List<String> getBlackListFields();

    public Map<String, ValueGenerator<V>> getExtraFields();
}