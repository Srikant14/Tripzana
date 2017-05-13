package co.tripzana.tripzana;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class register_activity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{


    private LinearLayout Prof_section;
    private SignInButton SignIn;
    private TextView Name,Email;
    private ImageView Prof_pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE =9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        Prof_section = (LinearLayout)findViewById(R.id.user_prof);
        Button signOut = (Button) findViewById(R.id.bn_logout);
        SignIn = (SignInButton)findViewById(R.id.bn_login);
        Name = (TextView)findViewById(R.id.name);
        Email = (TextView)findViewById(R.id.email);
        Prof_pic = (ImageView)findViewById(R.id.prof_pic);
        SignIn.setOnClickListener(this);
        signOut.setOnClickListener(this);
        Prof_section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.bn_login :
                signIn();
                break;
            case R.id.bn_logout:
                signOut();
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn()
    {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void signOut()
    {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInResult result)
    {

        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName(); //user name
            String email = account.getEmail();  //user email
            Uri img_url = account.getPhotoUrl(); //profile pic
            Name.setText(name);
            Email.setText(email);
            if(img_url != null)
            {
                Glide.with(this).load(img_url).into(Prof_pic);
            }
            updateUI(true);
        }
        else
        {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogin)
    {

        if(isLogin)
        {
            Prof_section.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        }
        else
        {
            Prof_section.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
