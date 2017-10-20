package co.devhub.selectcontactslib.utils;

import java.util.Comparator;

/**
 * 排序类
 * //@标签代表A前面的那些，#代表除了A-Z以外的其他标签
 * Created by tian on 16-1-9.
 */
public class CompareSort implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
        /*
        if(user1.getLetter().equals("@") || user2.getLetter().equals("@")){
            //通讯录前面的ietem(公众号，标签......)
            return user1.getLetter().equals("@") ? -1:1;
        }
        */

        //user1属于#标签，放到后面
        if(!user1.getLetter().matches("[A-z]+")){
            return 1;
        //user2属于#标签，放到后面
        }else if(!user2.getLetter().matches("[A-z]+")){
            return -1;
        }

        //Using string compare for normal items
        return user1.getLetter().compareTo(user2.getLetter());
    }
}
