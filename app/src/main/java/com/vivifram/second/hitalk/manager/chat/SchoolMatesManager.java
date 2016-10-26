package com.vivifram.second.hitalk.manager.chat;

import android.text.TextUtils;

import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.vivifram.second.hitalk.bean.address.SchoolMate;
import com.zuowei.dao.greendao.User;
import com.zuowei.utils.common.Md5Utils;
import com.zuowei.utils.common.NLog;
import com.zuowei.utils.common.TagUtil;
import com.zuowei.utils.helper.HiTalkHelper;
import com.zuowei.utils.helper.UserBeanCacheHelper;
import com.zuowei.utils.pinyin.CharacterParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * 项目名称：Hitalk
 * 类描述：
 * 创建人：zuowei
 * 创建时间：16-10-26 下午8:39
 * 修改人：zuowei
 * 修改时间：16-10-26 下午8:39
 * 修改备注：
 */
public class SchoolMatesManager {

    private static SchoolMatesManager sInstance;

    private SchoolMatesManager(){}

    public static SchoolMatesManager getInstance(){
        if (sInstance == null) {
            synchronized (SchoolMatesManager.class){
                if (sInstance == null) {
                    sInstance = new SchoolMatesManager();
                }
            }
        }
        return sInstance;
    }

    public Task<List<SchoolMate>> queryAllSchoolMates(){
        return Task.callInBackground(()->{
            List<SchoolMate> results = new ArrayList<>();

            AVQuery<AVUser> avQuery = AVUser.getQuery();

            String collegeCode = Md5Utils.stringToMD5(HiTalkHelper.$().getCurrentUserCollege());

            avQuery.whereEqualTo("collegeCode",collegeCode);

            List<AVUser> list = avQuery.find();
            if (list != null) {
                for (AVUser avUser : list) {
                    User user = new User();
                    UserBeanCacheHelper.AvUserToUser(avUser,user);
                    SchoolMate schoolMate  = new SchoolMate();
                    schoolMate.setNickName(user.getNick())
                            .setUserId(user.getObjectId());
                    fillLetters(schoolMate);
                    results.add(schoolMate);
                }
            }

            return results;
        });
    }

    public void fillLetters(SchoolMate schoolMate){
        if (schoolMate == null || schoolMate.getNickName() == null){
            return;
        }

        if (TextUtils.isEmpty(schoolMate.getNickName())){
            schoolMate.setSortLetters("#");
            return;
        }

        String pinyin = CharacterParser.getInstance().getSelling(schoolMate.getNickName());
        String sortString = pinyin.substring(0, 1).toUpperCase();

        if (sortString.matches("[A-Z]")) {
            schoolMate.setSortLetters(sortString.toUpperCase());
        } else {
            schoolMate.setSortLetters("#");
        }
    }
}