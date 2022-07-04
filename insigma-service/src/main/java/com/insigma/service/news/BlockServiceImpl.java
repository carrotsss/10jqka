package com.insigma.service.news;

import org.springframework.cache.annotation.Cacheable;

/**
 * @ClassName BlockServiceImpl
 * @Description
 * @Author carrots
 * @Date 2022/6/30 13:17
 * @Version 1.0
 */
public class BlockServiceImpl {
    @Cacheable(value = "2m", key = "share_industry_'+#level+'_#shareQuery.code+'_'+#shareQuery.market")
    public ShareBlockInfoDto getShareIndustry(ShareQuery shareQuery, Integer level) {
        List<ShareBlockInfoDto> shareBlockInfoDtoList = blockInfoMapper.getShareBlockByType(shareQuery.getCode(), shareQuery.getMarket(), type);
        return shareBlockInfoDtoList.get(0);
    }


}
