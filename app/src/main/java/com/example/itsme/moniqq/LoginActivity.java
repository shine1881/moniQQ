package com.example.itsme.moniqq;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    private TextView textView;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_password);
        textView = (TextView) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        db = SQLiteDatabase.openOrCreateDatabase(LoginActivity.this.getFilesDir().toString() +
                "/test."
                + "dbs", null);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String account=accountEdit.getText().toString();
//                String password=passwordEdit.getText().toString();
//                if(account.equals("182571721")&&password.equals("123456")){
//                    editor=pref.edit();
//                    if(rememberPass.isChecked()){
//                        editor.putBoolean("remember_password",true);
//                        editor.putString("account",account);
//                        editor.putString("password",password);
//                    }else{
//                        editor.clear();
//                    }
//                    editor.apply();
//                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else {
//                    Toast.makeText(LoginActivity.this,"account or password is invalid",Toast
// .LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String name = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (name.equals("") || password.equals("")) {
                    // 弹出消息框
                    new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
                            .setMessage("帐号或密码不能空").setPositiveButton("确定", null)
                            .show();
                } else {
                    isUserinfo(name, password);
                    editor=pref.edit();
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",name);
                        editor.putString("password",password);
                    }else{
                        editor.clear();
                    }
                    editor.apply();
                }
            }

            // 判断输入的用户是否正确
            public Boolean isUserinfo(String name, String password) {
                try {
                    String str = "select * from tb_user where name=? and password=?";
                    Cursor cursor = db.rawQuery(str, new String[]{name, password});
                    if (cursor.getCount() <= 0) {
                        new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
                                .setMessage("帐号或密码错误！").setPositiveButton("确定", null)
                                .show();
                        return false;
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }

                } catch (SQLiteException e) {
                    createDb();
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        db.close();
    }

    public static void createDb() {
        db.execSQL("create table tb_user( name varchar(30) primary key,password varchar(30))");
    }
}
