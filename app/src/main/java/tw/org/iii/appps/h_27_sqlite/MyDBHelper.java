package tw.org.iii.appps.h_27_sqlite;
//這只是輔助程式
//1.繼承SQLiteOpenHelper
//2.給建構式,跟實作方法
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private final  String createTable=
            "CREATE TABLE cust (id INTEGER primary key autoincrement,"+
                    "cname TEXT, birthday DATE)"; //自己定義這表如何創

    public MyDBHelper(Context context, //活在contexy,acti,跟service底下都可以

                      String name, //資料庫表名字
                      SQLiteDatabase.CursorFactory factory,//游標方向
                      int version) {//版本數
        super(context, name, factory, version);
    }
    //當資料庫開始時,就引用這張表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     sqLiteDatabase.execSQL(createTable); //執行寫好的表
    }

    //上船版本更新
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
