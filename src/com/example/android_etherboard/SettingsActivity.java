package com.example.android_etherboard;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.BaseSavedState;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String CONFIG_URL_KEY = "pref_url";
	public static final String CONFIG_DEFAULT_URL = "http://github.com";
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        
        Preference connectionPref = findPreference(CONFIG_URL_KEY);
        connectionPref.setSummary(preferences.getString(CONFIG_URL_KEY, CONFIG_DEFAULT_URL));
 /*       EditTextPreference myEditText = (EditTextPreference) findPreference("pref_url");
        setTextListenerOnEditTextPreference(myEditText); */
    }
    
/*    private void setTextListenerOnEditTextPreference(final EditTextPreference myEditText) {
    	
    	myEditText.getEditText().addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.length() == 0)
					myEditText.getEditText().setError("No empty URL allowed");
				else {
					String regex = "https?://";
		    	    Pattern pattern = Pattern.compile(regex);
		    	    Matcher matcher = pattern.matcher(arg0.toString());
		    	    if (!matcher.find())
		    	    	myEditText.getEditText().setError("Does not match necessary format - http:// or https://");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence chars, int start, int lengthBefore, int count) {
				// TODO Auto-generated method
			}
    	});
	}*/

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(CONFIG_URL_KEY)) {
            Preference connectionPref = findPreference(key);
            String outcome;
            String value = sharedPreferences.getString(key, "");
            // Set summary to be the user-description for the selected value
            // IFF the summary is different from the value passed in
            if (!value.equals(connectionPref.getSummary().toString())){
            	Boolean validationOutcome = validate(value);
            	if (validationOutcome){
            		connectionPref.setSummary(sharedPreferences.getString(key,""));
            		outcome = "Url has been updated";
            	} else {  
            		outcome = "Url could not be updated (bad link provided)";
            	}
            	Toast toast = Toast.makeText(getApplicationContext(), outcome, Toast.LENGTH_SHORT);
            	toast.show();

            	//Reset the EditText widget to the last saved point (saved in the summary property)
            	if (!validationOutcome){
            		EditTextPreference myEditText = (EditTextPreference) findPreference(CONFIG_URL_KEY);
            		myEditText.setText(connectionPref.getSummary().toString());
            	}
            }
        }
    }
    
    public boolean validate(String value){
    	if (value.length() == 0)
    		return false;
    	else {
    		String regex = "https?://";
    	    Pattern pattern = Pattern.compile(regex);
    	    Matcher matcher = pattern.matcher(value);
    	    if (matcher.find(0))
    	    	return true;
    	    else return false;
    	}
    }
}