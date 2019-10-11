package demo.xy.com.xytdcq.demo1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import demo.xy.com.xytdcq.R;

public class RedPagerMainActivity extends Activity {

	private EditText total;
	private EditText people;
	private EditText max;
	private EditText min;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_red_pager_main);
		initView();
	}
	private void initView() {
		total =  findViewById(R.id.total);
		people =  findViewById(R.id.people);
		max =  findViewById(R.id.max);
		min =  findViewById(R.id.min);
	}

	public void btnClick(View v){
		if(v.getId() == R.id.calculate){
			Intent mIntent = new Intent(RedPagerMainActivity.this,ShowResultActivity.class);
			mIntent.putExtra("total", total.getText().toString());
			mIntent.putExtra("people", people.getText().toString());
			mIntent.putExtra("max", max.getText().toString());
			mIntent.putExtra("min", min.getText().toString());
			startActivity(mIntent);
		}
	}
	

}
