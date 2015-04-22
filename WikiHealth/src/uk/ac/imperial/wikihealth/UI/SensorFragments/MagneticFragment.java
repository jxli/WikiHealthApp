package uk.ac.imperial.wikihealth.UI.SensorFragments;

import java.text.DecimalFormat;

import uk.ac.imperial.wikihealth.R;
import uk.ac.imperial.wikihealth.Database.Contracts.MagneticContract;
import uk.ac.imperial.wikihealth.Database.Contracts.Contract;
import uk.ac.imperial.wikihealth.Utils.PreferenceUtils;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

 
public class MagneticFragment extends Fragment implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mAcc;
    private View mView;
    private TextView valuex,valuey,valuez;
	private TextView mDescription;
    private CheckBox mCheck;
    private EditText mPeriodText;
    private Button mSave;
    public static Bundle createBundle( String title ) {
        Bundle bundle = new Bundle();
        return bundle;
    }
 

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
		    mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			mSensorManager.registerListener(this,mAcc, Integer.MAX_VALUE);
			mView= inflater.inflate(R.layout.general_sensor_layout, container, false);
			
			 valuex=(TextView)mView.findViewById(R.id.valuex);
			 valuey=(TextView)mView.findViewById(R.id.valuey);
			 valuez=(TextView)mView.findViewById(R.id.valuez);
				mDescription=(TextView)mView.findViewById(R.id.sensorsDescription);
				mDescription.setText(getResources().getText(R.string.magnetic_description));
			
			 ((TextView)mView.findViewById(R.id.title)).setText("Magnetic field");

			 ((TextView)mView.findViewById(R.id.labelx)).setText("X-axis:");
			 ((TextView)mView.findViewById(R.id.labely)).setText("Y-axis");
			 ((TextView)mView.findViewById(R.id.labelz)).setText("Z-axis");
			 
				mCheck=(CheckBox)mView.findViewById(R.id.sensorCheck);
				
				final Contract c=new MagneticContract();
				mCheck.setChecked(PreferenceUtils.isChecked(mView.getContext(), c));
				mCheck.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(mCheck.isChecked()){
							MagneticContract.sMonitor=true;
							PreferenceUtils.updateMonitoringPreference(mView.getContext(),c, true);

						}
						else{
							MagneticContract.sMonitor=false;
							PreferenceUtils.updateMonitoringPreference(mView.getContext(),c, false);

						}
					}
				});
				mPeriodText=(EditText)mView.findViewById(R.id.sensorsPeriod);
				mPeriodText.setText(String.valueOf(PreferenceUtils.getPeriod(mView.getContext(), c)));
				 mSave=(Button)mView.findViewById(R.id.sensorsSave);
				 
				 mSave.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(mPeriodText.getText()==null || mPeriodText.getText().toString().equals("")||  (mPeriodText.getText().toString().length()>6) || Integer.parseInt(mPeriodText.getText().toString())<1000 ){
									Toast.makeText(getActivity(), "Invalid period value",Toast.LENGTH_LONG).show();
							}
							else{
								MagneticContract.sPeriod=Integer.parseInt(mPeriodText.getText().toString());
								PreferenceUtils.updatePeriodPreference(mView.getContext(),c,Integer.parseInt(mPeriodText.getText().toString()));

								Toast.makeText(getActivity(), "New period value updated",Toast.LENGTH_LONG).show();
							}
							
						}
					});
		    return mView;
		}


		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    FragmentManager fm = getFragmentManager();
		    fm.beginTransaction().commit();
		}

		@Override
		public void onResume() {
		    super.onResume();
		
		}


		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onSensorChanged(SensorEvent event) {
			valuex.setText(String.valueOf(new DecimalFormat("#.##").format(event.values[0])));
			valuey.setText(String.valueOf(new DecimalFormat("#.##").format(event.values[1])));
			valuez.setText(String.valueOf(new DecimalFormat("#.##").format(event.values[2])));

				
		}
	
		@Override
		public void onDestroy(){
			mSensorManager.unregisterListener(this);
	        super.onDestroy();
		}
	
}