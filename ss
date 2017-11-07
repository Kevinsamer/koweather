[33mcommit 9ec5a77ac81b91cd3d5468496d654ae85524de5c[m[33m ([m[1;36mHEAD -> [m[1;32mmaster[m[33m)[m
Author: yzc <yzcacl@gmail.com>
Date:   Tue Nov 7 09:25:05 2017 +0800

    2nd commit

[1mdiff --git a/app/src/main/java/com/koweather/yzc/mykoweather/fragment/ChooseAreaFragment.java b/app/src/main/java/com/koweather/yzc/mykoweather/fragment/ChooseAreaFragment.java[m
[1mindex 7b6d1ad..db183ab 100644[m
[1m--- a/app/src/main/java/com/koweather/yzc/mykoweather/fragment/ChooseAreaFragment.java[m
[1m+++ b/app/src/main/java/com/koweather/yzc/mykoweather/fragment/ChooseAreaFragment.java[m
[36m@@ -57,6 +57,8 @@[m [mpublic class ChooseAreaFragment extends Fragment {[m
     private View view;[m
     private static String weatherURL = "http://guolin.tech/api/weather?cityid=";[m
     private static String key = "7a6c0c69b869474da3c3471de2bcf82c";//这条key使用次数1000次/天[m
[32m+[m[32m    private int ChangeContent = 0;[m
[32m+[m
 [m
     @Nullable[m
     @Override[m
