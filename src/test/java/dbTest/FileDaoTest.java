package dbTest;

import com.ym.dao.FileDao;
import com.ym.dao.impl.FileDaoImpl;
import com.ym.domain.FileDO;
import com.ym.utils.DateUtil;


import java.util.Date;
import java.util.List;

public class FileDaoTest {

    public static void main(String[] args) {

        FileDO file=new FileDO("51fea", "8VB89B", "jpg", 110L, "D:/FileCenter/20191119/", "Flr", new Date());
        FileDao dao=new FileDaoImpl();
        if (file!=null) {

        }
        List<FileDO> list=dao.list(0,1);
        for (FileDO a:list
                ) {
            System.out.println(a.getName());
        }
    }

}
