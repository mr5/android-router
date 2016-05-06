package com.github.mr5.pojodumper.valuegenerator;

import com.github.mr5.pojodumper.Dumper;
import com.github.mr5.pojodumper.Entity.Post;
import com.github.mr5.pojodumper.Entity.User;
import com.github.mr5.pojodumper.ValueGenerator;
import com.github.mr5.pojodumper.dumpspecs.UserCardDumpSpec;

public class PostUserGenerator implements ValueGenerator<Post> {
    protected UserCardDumpSpec userCardDumpSpec = new UserCardDumpSpec();

    @Override
    public Object generate(Post value) {
        Dumper dumper = Dumper.getInstance();
        User user = new User();
        user.setId(value.getUid());
        user.setUsername("username xxx");
        user.setPassword("user password");
        return dumper.blackListDump(user, userCardDumpSpec);
    }
}

