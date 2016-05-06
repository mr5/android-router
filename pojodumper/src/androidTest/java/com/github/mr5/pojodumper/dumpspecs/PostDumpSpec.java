package com.github.mr5.pojodumper.dumpspecs;

import com.github.mr5.pojodumper.Dumper;
import com.github.mr5.pojodumper.Entity.Post;
import com.github.mr5.pojodumper.Entity.User;
import com.github.mr5.pojodumper.ValueGenerator;
import com.github.mr5.pojodumper.WhiteListDumpSpec;
import com.github.mr5.pojodumper.valuegenerator.PostUserGenerator;
import com.github.mr5.pojodumper.valuegenerator.UrlGenerator;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDumpSpec implements WhiteListDumpSpec<Post> {

    protected PostUserGenerator postUserGenerator = new PostUserGenerator();


    @Override
    public List<String> getWhiteListFields() {
        String[] fields = {
                "id",
                "title",
                "content"
        };
        return Arrays.asList(fields);
    }

    @Override
    public Map<String, ValueGenerator<Post>> getExtraFields() {
        HashMap<String, ValueGenerator<Post>> extraFields = new HashMap<>();


//        Dumper.getInstance().blackListDump(new String[]{"password"}, new User());


        extraFields.put("user", postUserGenerator);
//        extraFields.put("url", new UrlGenerator());
        // extraFields.put("htmlContent", new HtmlContentGenerator());

        extraFields.put("url", new ValueGenerator<Post>() {
            @Override
            public Object generate(Post value) {
                return "/node" + value.getId();
            }
        });


        Dumper.getInstance().whiteListDump(new String[]{"title", "content"}, new Post(), extraFields);

        Dumper.getInstance().whiteListDump(new Post(), new PostDumpSpec());



        return extraFields;
    }
}

/**
 * array(
 * 'id',
 * 'title',
 * 'content',
 * 'url' => 'getUrl',
 * 'htmlContent' => 'getHtmlContent'
 * )
 */
