package com.gaoyehua.bean;

/**
 * Created by gaoyehua on 2016/7/24.
 */
public class BlackNumInfo  {
    //黑名单号码
    private String blacknum;
    //拦截模式
    private int mode;

    public String getBlacknum(){
        return blacknum;
    }

    public void setBlacknum(String blacknum) {
        this.blacknum = blacknum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        //0,1,2
        if(mode>=0 &&mode<=2) {
            this.mode = mode;
        }else {
            this.mode = 0;
        }

    }


    public BlackNumInfo(){

        super();

    }
    public BlackNumInfo(String blacknum,int mode){
        super();
        this.blacknum=blacknum;
        //0,1,2
        if(mode>=0 &&mode<=2) {
            this.mode = mode;
        }else {
            this.mode = 0;
        }
    }

    //方便测试
    @Override
    public String toString() {
        return "BlackNumInfo [blacknum=" + blacknum + ", mode=" + mode + "]";
    }
}
