package site.chniccs.basefrm.protocal.api;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import site.chniccs.basefrm.entity.CategoryEntity;

public  interface GankApi
{
    @GET("data/{category}/{number}/{page}")
    public abstract Observable<CategoryEntity> getCategoryDate(@Path("category") String paramString, @Path("number") int paramInt1, @Path("page") int paramInt2);

    @GET("random/data/福利/{number}")
    public abstract Observable<CategoryEntity> getRandomBeauties(@Path("number") int paramInt);

//    @GET("search/query/{key}/category/all/count/{count}/page/{page}")
//    public abstract Observable<SearchEntity> getSearchResult(@Path("key") String paramString, @Path("count") int paramInt1, @Path("page") int paramInt2);
}
