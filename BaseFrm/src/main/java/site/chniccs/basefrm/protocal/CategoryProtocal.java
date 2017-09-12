package site.chniccs.basefrm.protocal;


import site.chniccs.basefrm.protocal.api.GankApi;
import site.chniccs.basefrm.protocal.base.BaseProtocal;

public class CategoryProtocal extends BaseProtocal<GankApi>
{
    protected Class<GankApi> getApiClass()
    {
        return GankApi.class;
    }
}
