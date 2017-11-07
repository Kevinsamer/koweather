package com.koweather.yzc.mykoweather.gson;

/**
 * Created by xk on 2017/11/6.
 */

public class Suggestion {
//    brf	生活指数简介
//    txt	生活指数详细描述
//    comf	舒适度指数
//    cw	洗车指数
//    drsg	穿衣指数
//    flu	感冒指数
//    sport	运动指数
//    trav	旅游指数
//    uv	紫外线指数
//    air	空气质量指数
    public class Air{
        public String brf;
        public String txt;
    }
    public class Comf{
        public String brf;
        public String txt;
    }
    public class Cw{
        public String brf;
        public String txt;
    }
    public class Drsg{
        public String brf;
        public String txt;
    }
    public class Flu{
        public String brf;
        public String txt;
    }
    public class Sport{
        public String brf;
        public String txt;
    }
    public class Trav{
        public String brf;
        public String txt;
    }
    public class Uv{
        public String brf;
        public String txt;
    }

    public Uv uv;
    public Trav trav;
    public Sport sport;
    public Flu flu;
    public Comf comf;
    public Cw cw;
    public Drsg drsg;
    public Air air;

}
