package dbTest;


import com.ym.utils.*;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ConnectionTest {


//    private Integer id;
//    private String size;
//    private String type;
//    private String oldname;
//    private Date createtime;
//    private String path;

    public static void main(String[] args) {

        Connection connection = derbyConectUtil.GetConnection();

      String sql="CREATE TABLE file_info  ( " +
                "  id varchar(255) ," +
                "  name varchar(255) ," +
                "  type varchar(11) ," +
                "  size bigint ," +
                "  url varchar(200) ," +
                "  content varchar(800) ," +
                "  create_date TIMESTAMP ," +
                "  PRIMARY KEY (id) " +
                ")";
       // String sql="DROP TABLE file_info";
        String newFileName = FileUtil.renameToUUID("123.jpg");
        //String sql="INSERT INTO file_info (id, name, type, size, url, content, create_date) VALUES('"+newFileName+"', '8VB89B', 'jpg', 110, 'D:/FileCenter/20191119/', 'Flr',CURRENT_TIMESTAMP)";
        int i=0;
        try{
            Statement st=connection.createStatement();
            st.execute(sql);

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("失败");
        }
    }

}
