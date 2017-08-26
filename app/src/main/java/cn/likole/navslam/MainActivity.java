package cn.likole.navslam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.action.IMoveAction;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.discovery.DeviceManager;
import com.slamtec.slamware.geometry.PointF;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    //definition
    private TextView tv_info;
    private TextView tv_log;
    private Button btn;
    private Button btn_fd;
    private Button btn_bk;
    private Button btn_l;
    private Button btn_r;
    private EditText et_x;
    private EditText et_y;
    private ToggleButton btn_nav;
    private AbstractSlamwarePlatform slamwarePlatform;
    private Handler handler;
    private int STATUS_NAVING=0;
    private int STATUS_MANUAL=1;
    private int status=STATUS_MANUAL;
    private Nav nav_pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slamwarePlatform= DeviceManager.connect("192.168.11.1",1445);
        bindView();
        setListener();
        updateInfo();
    }

    /**
     * 绑定视图
     */
    private void bindView() {
        tv_info= (TextView) findViewById(R.id.info);
        tv_log= (TextView) findViewById(R.id.textView_log);
        btn_fd= (Button) findViewById(R.id.button_fd);
        btn_bk= (Button) findViewById(R.id.button_bk);
        btn_l= (Button) findViewById(R.id.button_l);
        btn_r= (Button) findViewById(R.id.button_r);
        btn= (Button) findViewById(R.id.button);
        et_x= (EditText) findViewById(R.id.editText_x);
        et_y= (EditText) findViewById(R.id.editText_y);
        btn_nav= (ToggleButton) findViewById(R.id.nav);
    }

    /**
     * 定时器
     */
    private void updateInfo(){
        handler=new Handler();
        handler.postDelayed(runnable,100);
        handler.postDelayed(nav,100);
    }

    /**
     * 设置监听器
     */
    private void setListener()
    {
        btn_fd.setOnClickListener(MainActivity.this);
        btn_bk.setOnClickListener(MainActivity.this);
        btn_l.setOnClickListener(MainActivity.this);
        btn_r.setOnClickListener(MainActivity.this);
        btn.setOnClickListener(MainActivity.this);
        btn_nav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    nav_pro=new Nav();
                    Location location=slamwarePlatform.getLocation();
                    float x=Float.parseFloat(et_x.getText().toString());
                    float y=Float.parseFloat(et_y.getText().toString());
                    nav_pro.run(slamwarePlatform,new PointF(x,y));
                    status=STATUS_NAVING;
                }
                else status=STATUS_MANUAL;

            }
        });
    }

    /**
     * 点击监听事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_fd:
                slamwarePlatform.moveBy(MoveDirection.FORWARD);
                break;

            case R.id.button_bk:
                slamwarePlatform.moveBy(MoveDirection.BACKWARD);
                break;

            case R.id.button_l:
                slamwarePlatform.moveBy(MoveDirection.TURN_LEFT);
                break;

            case R.id.button_r:
                slamwarePlatform.moveBy(MoveDirection.TURN_RIGHT);
                break;

            case R.id.button:
                slamwarePlatform.clearMap();
                slamwarePlatform.setPose(new Pose(new Location(0,0,0),new Rotation(0)));
                break;
        }
    }

    /**
     * 数据更新
     */
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {

            //行为，坐标，电量
            String info="{{action}} now({{x}},{{y}},{{r}}) 电{{battery}}%";


            Pose pose=slamwarePlatform.getPose();
            IMoveAction moveAction= slamwarePlatform.getCurrentAction();

            float x=pose.getLocation().getX();
            float y=pose.getLocation().getY();
            float r=pose.getYaw();

            java.text.DecimalFormat decimalFormat=new java.text.DecimalFormat("0.00");

            info=info.replace("{{action}}",moveAction!=null?"run":"");
            info=info.replace("{{x}}",decimalFormat.format(x));
            info=info.replace("{{y}}",decimalFormat.format(y));
            info=info.replace("{{r}}",decimalFormat.format(r));

            info=info.replace("{{battery}}",String.valueOf(slamwarePlatform.getBatteryPercentage()));

            final String info_f=info;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_info.setText(info_f);
                }
            });

            //路径点
            try{
                List<Location> points= slamwarePlatform.getCurrentAction().getRemainingMilestones().getPoints();
                tv_log.setText("");
                for (Location location:points)
                {
                    tv_log.setText(tv_log.getText()+"("+location.getX()+","+location.getY()+")\n");
                }
            }catch (Exception e){

            }

            handler.postDelayed(runnable,50);
        }
    };


    //导航
    private Runnable nav=new Runnable() {
        @Override
        public void run() {
//            if(status==STATUS_NAVING)
//            {
//
//                Location location=slamwarePlatform.getLocation();
//                float x=Float.parseFloat(et_x.getText().toString());
//                float y=Float.parseFloat(et_y.getText().toString());
//
//                //x
//                if(x-location.getX()>1) x=location.getX()+1;
//                else if (x-location.getX()<-1) x=location.getX()-1;
//
//                //y
//                if(y-location.getY()>1) y=location.getY()+1;
//                else if (y-location.getY()<-1) y=location.getY()-1;
//
//                slamwarePlatform.moveTo(new Location(x,y,0));
//
//
//            }
//            handler.postDelayed(nav,3000);
        }
    };
}
