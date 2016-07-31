package com.gaoyehau.watchdog;

import android.test.AndroidTestCase;

import com.gaoyehua.db.BlackNumOpenHelper;
import com.gaoyehua.db.dao.BalackNumDao;

import java.util.Random;

/**
 * Created by gaoyehua on 2016/7/23.
 */
public class Testdb extends AndroidTestCase {
    private BalackNumDao blackNumDao;
    public void testBlackNumOpenHelper(){
        BlackNumOpenHelper blackNumOpenHlper = new BlackNumOpenHelper(getContext());//不能创建数据库
        blackNumOpenHlper.getReadableDatabase();//创建数据库
    }
    //在测试方法之前执行的方法
    @Override
    protected void setUp() throws Exception {
        blackNumDao = new BalackNumDao(getContext());
        super.setUp();
    }
    //在测试方法之后执行的方法
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }
    public void testAddBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        blackNumDao.addBlackNum("110", BalackNumDao.CALL);
    }
    public void testUpdateBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        Random random =new Random();
        for(int i=0;i<200;i++){
            blackNumDao.addBlackNum("12345678"+i,random.nextInt(3));
        }
    }
    public void testQueryBlackNumMode(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        int mode = blackNumDao.queryBlackNum("110");
        //断言   参数1:期望的值     参数2:实际的值,获取的值
        assertEquals(-1, mode);
    }
    public void testDeleteBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        blackNumDao.deleteBlackNum("110");
    }

}
