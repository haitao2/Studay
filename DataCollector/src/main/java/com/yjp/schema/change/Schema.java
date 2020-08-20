package com.yjp.schema.change;

import java.util.Map;

public abstract class Schema {
    // 获取分区数据,通用的方法，mysql和sqlServer都适用该规则。
    public int getPartitions(int row, int line) {
        int partition = 1;
        if (line > 90) partition += 3;
        if (row >= 100000 && row < 10000000)
            partition = 3;
        else if (row >= 10000000 && row < 40000000)
            partition = 6;
        else if (row >= 40000000 && row < 70000000)
            partition = 9;
        else if (row >= 70000000 && row < 100000000)
            partition = 12;
        else if (row >= 100000000)
            partition = 15;
        return partition;

    }

    // 获取Kudu的Schema信息
    public abstract String getKuduSchema(String schema);
    //

}
