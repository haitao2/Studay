package com.yjp.schema.change;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlSchema extends Schema {
    private static Map<String, String> schemaTypeMap = new HashMap<String, String>();

    static {
        try {
            // 从文件mysqlAndKuduSchemaType中获取到mysql和kudu对应的文件类型，然后写入map中。
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "mysqlAndKuduSchemaType")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] mysqlAndKuduSchemaType = line.split(",");
                schemaTypeMap.put(mysqlAndKuduSchemaType[0], mysqlAndKuduSchemaType[1]);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getKuduSchema(String schema) {
        String ty = schema.split("\\(")[0];
        String schemaType = schemaTypeMap.get(ty);
        return schemaType;

    }
}
