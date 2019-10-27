package org.share.jpa.adds.mapper;

import org.springframework.data.repository.query.Param;

import java.util.HashMap;

public class MapperTest {
    interface Mapper {
        void insert(@Param("id") String id, @Param("size") long size);

        void insertUsingHashMap(HashMap<String, Object> params);

        long selectSize(@Param("id") String id);

    }
}
