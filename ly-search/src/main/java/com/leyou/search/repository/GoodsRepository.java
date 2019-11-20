package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Auther: tianchao
 * @Date: 2019/11/20 21:25
 * @Description:
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
