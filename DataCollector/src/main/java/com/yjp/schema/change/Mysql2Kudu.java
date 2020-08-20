package com.yjp.schema.change;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql的schema转换为kudu的schema，并且将转换后的sql存储到文本，并且将表信息存储到excel文件中。
 */
public class Mysql2Kudu {
    public static void main(String[] args) {
        try {
            // 以字节流的形式获取sql文件。字符流+字符缓冲流，可以提高效率
            String path = System.getProperty("user.dir");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path + File.separator + "data" + File.separator + "mysqlSchema.sql")));
            StringBuffer sb = new StringBuffer();
            List<String> schemaList = new ArrayList<String>();
            Schema schema = new MysqlSchema();
            // 列数
            int column = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 处理mysql的schema信息，并且生成kudu的信息。
                schemaList.add(line);
                String[] words = line.split(" ");
                String[] words1 = new String[words.length];
                int i = 0;
                for (String word : words) {
                    if (!word.equals("")) {
                        words1[i] = word;
                        i++;
                    }
                }
                int j = 0;
                for (String commentStation:words1){
                    if (commentStation!=null&&!commentStation.toLowerCase().equals("comment")){
                        j++;
                    }
                }
                System.out.println(j);
                String ty = schema.getKuduSchema(words1[1]);
                if (line.toLowerCase().startsWith("create"))
                    sb.append(line+"\n");
                else if (ty==null)
                    continue;
                else {
                    // 生成kudu的schema信息。
                    String newLine = line.replace(words1[1],ty);
                    sb.append(newLine+"\n");
                }
                if (line.contains("NULL")) column += 1;
            }
            System.out.println(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
