package com.github.mr5.pojodumper.valuegenerator;

import com.github.mr5.pojodumper.Entity.Post;
import com.github.mr5.pojodumper.ValueGenerator;

/**
 * Created by wscn on 16/4/27.
 */
public class UrlGenerator implements ValueGenerator<Post> {
    @Override
    public Object generate(Post value) {
        return "/node/" + value.getId();
    }
}

/**
 {

 "title":"标题",
 "content":"",
 "user" : {

    }
 }

 */