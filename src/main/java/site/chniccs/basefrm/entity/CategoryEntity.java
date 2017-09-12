package site.chniccs.basefrm.entity;

import java.util.List;

import site.chniccs.basefrm.base.BaseEntity;

/**
 * Created by chniccs on 2017/6/15 15:04.
 */

public class CategoryEntity
        extends BaseEntity
{
    public List<ResultsBean> results;

    public static class ResultsBean
    {
        public String _id;
        public String createdAt;
        public String desc;
        public List<String> images;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public boolean used;
        public String who;
    }
}
