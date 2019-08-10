package com.climate.fight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private FirebaseUser user;
    // Choose authentication providers
    private final int RC_SIGN_IN = 420;
    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.AnonymousBuilder().build(),
            new AuthUI.IdpConfig.TwitterBuilder().build()
    );

    private ImageView profilePic;
    private TextView accountData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .enableAnonymousUsersAutoUpgrade()
                            .setIsSmartLockEnabled(true)
                            .build(), RC_SIGN_IN);
        } else {
            MaterialButton button = findViewById(R.id.btn_acc_logout),
                    upgrade = findViewById(R.id.upgrade_acc);
            if(user.isAnonymous()) {
                upgrade.setVisibility(View.VISIBLE);
                upgrade.setOnClickListener(view -> startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder().setAvailableProviders(providers)
                                .enableAnonymousUsersAutoUpgrade().setIsSmartLockEnabled(true)
                                .build(), RC_SIGN_IN));
            }
            accountData = findViewById(R.id.account_data);
            String displayName = user.getDisplayName() != null ? user.getDisplayName() : "",
            email = user.getEmail() != null ? user.getEmail() : "",
            phone = user.getPhoneNumber() != null ? user.getPhoneNumber() : "";
            accountData.setText(displayName + "\n" + email + "\n" + phone);
            button.setOnClickListener(view -> AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> recreate()));
            profilePic = findViewById(R.id.imgUserProfile);
            Glide.with(this).load(user.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(profilePic);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                recreate();
            } else {
                finish();
            }
        }
    }
}
