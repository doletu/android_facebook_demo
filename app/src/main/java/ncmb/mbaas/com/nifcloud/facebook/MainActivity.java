package ncmb.mbaas.com.nifcloud.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.facebook.FacebookSdk;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBFacebookParameters;
import com.nifcloud.mbaas.core.NCMBUser;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this.getApplicationContext(),
                "543e6ee053794c0ebbce6e668e4e86cf17a96dd2e841d3a99a6bc32576d314e0",
                "6c53e766837d00a8c4c7254c39c6536d1e1455aeb2dd30a0ee40ba0502375fba");

        // Facebook settings
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppEventsLogger.activateApp(getApplication());

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        //Login to NIFCLOUD mobile backend
                        NCMBFacebookParameters parameters = new NCMBFacebookParameters(
                                loginResult.getAccessToken().getUserId(),
                                loginResult.getAccessToken().getToken(),
                                loginResult.getAccessToken().getExpires()
                        );
                        try {
                            NCMBUser.loginWith(parameters);
                            Toast.makeText(getApplicationContext(), "Login to NIFCLOUD mbaas with Facebook account", Toast.LENGTH_LONG).show();
                        } catch (NCMBException e) {
                            Log.d("tag", e.getMessage().toString());
                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("tag", "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("tag", "onError:" + exception);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
