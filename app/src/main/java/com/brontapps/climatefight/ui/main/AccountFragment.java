package com.brontapps.climatefight.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brontapps.climatefight.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private MainSharedViewModel mViewModel;
    private FirebaseUser user;
    // Choose authentication providers
    private final int RC_SIGN_IN = 420;
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.AnonymousBuilder().build(),
            new AuthUI.IdpConfig.TwitterBuilder().build()
    );


    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(getActivity()).get(MainSharedViewModel.class);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            View v = inflater.inflate(R.layout.account_fragment, container, false);
            MaterialButton button = v.findViewById(R.id.btn_acc_logout);
            button.setOnClickListener(view -> {
                AuthUI.getInstance()
                        .signOut(getContext())
                        .addOnCompleteListener(task -> {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, AccountFragment.newInstance()).commit();
                        });
            });
            return v;
        }else{
            View v = inflater.inflate(R.layout.register_fragment, container, false);
            MaterialButton button = v.findViewById(R.id.btn_acc_auth);
            button.setOnClickListener(view -> {
                startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            });
            return v;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainSharedViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance()).commit();
            } else {
                Snackbar.make(getView(), "No se ha podido iniciar sesión correctamente", Snackbar.LENGTH_SHORT);
            }
        }
    }

}
