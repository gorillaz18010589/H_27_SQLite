package tw.org.iii.appps.h_27_sqlite;
//在系統上面有哪些資料庫,聯絡不,相簿,先玩自訂的,在玩共用得
//資料庫放在內存空間,因此你的使用者按下清楚也會刪掉
//資料室內存放在data/data/檔案名底下
//*可以設計代辦事項,把今天要做的事情儲存
//認識一下SQLlite:https://en.softonic.com/download/sqlitemanager/windows/post-download?ex=BB-1006.3瀏覽器外掛,下載
//sqlite manager下載:https://sqlitemanager.en.softonic.com/
//1.先寫一個類別繼承SQLiteOpenHelper,建構式,跟時作要處理,輔助城市
//2.準備增刪修查四個按鈕
//select,where,group by,having ,order by .limit
//android content provider:
//*你可以提供讓使用者存取得動作
//*provider :https://developer.android.com/guide/topics/providers/content-provider-basics.html?hl=zh-tw

//insert(String table, String nullColumnHack, ContentValues values): //新增sql(1.表明,2.未知.3.ContentValue)
//delete(String table, String whereClause, String[] whereArgs)://刪除sql(1.表明 2.刪除條件 3.陣列裡面刪除條件的參數)
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase database; //重點是他
    private MyDBHelper myDBHelper; //自己寫的sql輔助程式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLiteDatabase拿到物件實體
        myDBHelper = new MyDBHelper(this,"hank",null,1);//呼叫寫好的資料庫(1.這個頁面2.庫名3.游標,4.版本 )
        database =  myDBHelper.getReadableDatabase(); //從這個寫好酷表取得物件實體,如果用getWrite出現錯誤可以拿到例外
    }

    //增
    public void insert(View view) {
//        String sql ="INSERT INTO cust (cname,birthday) VALUES ('Brad','1990-04-05')";
//        database.execSQL(sql);

        //新的增法防止名瑪攻擊
        //insert(String table, String nullColumnHack, ContentValues values): //新增sql(1.表明,2.未知.3.ContentValue)
        ContentValues value = new ContentValues();
        value.put("cname","kidd");
        value.put("birthday","1990-03-27");
        database.insert("cust",null,value); //新增sql(1.表明,2.未知.3.ContentValue)

        query(null);//按下新增按鈕時,同時呼叫查詢按鈕
    }

    //刪除搭配指定刪除,刪除了第1比的資料
    public void delete(View view) {
        //delete from cust where xxxx
        //delete(String table, String whereClause, String[] whereArgs)://刪除(1.表明 2.刪除條件 3.陣列裡面刪除條件的參數)
        database.delete("cust","id = ? and cname = ?",new String[]{"1","Brad"});//刪除(1.表明 2.刪除條件 3.陣列裡面刪除條件的參數)
    }

    //修
    public void update(View view) {
        //updat cust set cname'LBJ' ,birthday='1992-01-03' where id=4;
        //update(String table, ContentValues values, String whereClause, String[] whereArgs)
        ContentValues values = new ContentValues();
        values.put("cname","carter");
        values.put("birthday","1995");
        database.update("cust",values,"id = ?",new String[]{"4"});
    }

    //查
    public void query(View view) {
     //  原始寫法
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)


        Cursor cursorc = database.query("cust",//表名
                new  String[]{"id","cname","birthday"},//欄位
                "cname like?",//where條件查詢
                new String[]{"K%"},//where帶的參數
                null,
                null,
                null);//(庫表,select,where,group by,having ,order by .limit,)取得查詢游標

        //只查詢有k開頭的資料,條件式查詢示範
        if(cursorc != null){ //如果cursorc有東西時
            while (cursorc.moveToNext()){//當這個查詢下一筆時
                String id =  cursorc.getString(cursorc.getColumnIndex("id"));//取得這個欄位用String型態(取得欄位名)
                String cname = cursorc.getString(cursorc.getColumnIndex("cname"));
                String birthday = cursorc.getString(cursorc.getColumnIndex("birthday"));
                Log.v("brad", id + ":" + cname +":" +birthday) ;
            }
        }

        // 1.String sql = "SELECT * FROM cust" =>寫成以下,這是一次查詢所以有的表
//     Cursor cursorc = database.query(
//                 "cust",//表名
//                null,//欄位
//                null,//where條件查詢
//                null,//where帶的參數
//                null,
//                null,
//                null);//(庫表,select,where,group by,having ,order by .limit,)取得查詢游標

        Log.v("brad","==============================");
        //-----------------------------
        //3.這次用紙查id4的子查詢另一種用法,這種方法比較直觀,且可以用innerjoin,
        //rawQuery(String sql, String[] selectionArgs)://查詢sql(1."sql語法" 2.要帶的參數)
        cursorc = database.rawQuery("select * from cust where id in(select id from cust where id = ?)",new String[]{"4"});//查詢sql(1."sql語法" 2.要帶的參數)
        if(cursorc != null){ //如果cursorc有東西時
            while (cursorc.moveToNext()){//當這個查詢下一筆時
                String id =  cursorc.getString(cursorc.getColumnIndex("id"));//取得這個欄位用String型態(取得欄位名)
                String cname = cursorc.getString(cursorc.getColumnIndex("cname"));
                String birthday = cursorc.getString(cursorc.getColumnIndex("birthday"));
                Log.v("brad", id + ":" + cname +":" +birthday) ;
            }
        }
    }

}
