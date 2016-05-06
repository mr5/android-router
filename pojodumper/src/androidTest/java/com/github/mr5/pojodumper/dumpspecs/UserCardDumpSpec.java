package com.github.mr5.pojodumper.dumpspecs;

import com.github.mr5.pojodumper.BlackListDumpSpec;
import com.github.mr5.pojodumper.Entity.User;
import com.github.mr5.pojodumper.ValueGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wscn on 16/4/27.
 */
public class UserCardDumpSpec implements BlackListDumpSpec<User> {

    @Override
    public List<String> getBlackListFields() {
        String[] fields = {
                "password"
        };
        return Arrays.asList(fields);
    }

    @Override
    public Map<String, ValueGenerator<User>> getExtraFields() {
        HashMap<String, ValueGenerator<User>> extraFields = new HashMap<>();
        return null;
    }
}
