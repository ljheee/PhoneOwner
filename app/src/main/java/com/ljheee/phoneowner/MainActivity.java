package com.ljheee.phoneowner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText phone;
    Button btn;
    TextView textView;
    ProgressDialog mPDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phone.getText().toString();
                if(isMobile(number)){
                    btn.setEnabled(false);
                    //创建ProgressDialog对象
                    mPDialog = new ProgressDialog(MainActivity.this);
                    // 设置进度条风格，风格为圆形，旋转的
                    mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mPDialog.show();

                    Volley_Get(number);
                }else {
                    Toast.makeText(MainActivity.this,"请输入合法手机号",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 解析接口
     */
    private void Volley_Get(String phone) {

        String url = "http://apis.juhe.cn/mobile/get?phone=" + phone + "&key=22a6ba14995ce26dd0002216be51dabb";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Method.PUBLIC,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {// 成功
                        Volley_Json(json);
                    }
                },
                new Response.ErrorListener() {
                 @Override
                public void onErrorResponse(VolleyError errorLog) {// 失败
                       setShowError(errorLog.getMessage());
                }
             });
        queue.add(request);
    }


    /**
     * 查询出错
     */
    private void setShowError(String str){
        btn.setEnabled(true);
        textView.setText("查询出错"+str);
    }

    /**
     * 解析Json
     * @param json
     */
    private void Volley_Json(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject object = jsonObject.getJSONObject("result");

            mPDialog.hide();
            btn.setEnabled(true);
            textView.setText("归属地:" + object.getString("province") + "-" + object.getString("city") + "\n" +
                    "区号:" + object.getString("areacode") + "\n" +
                    "运营商:" + object.getString("company") + "\n" +
                    "用户类型:" + object.getString("card") + "\n" +
                    "邮政编码:"+object.getString("zip"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 正则表达式匹配手机号
     * @param str
     * @return
     */
    public boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        return m.matches();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 选项菜单--点击事件处理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                break;

            case R.id.action_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }




}
