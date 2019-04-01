/*
 * MIT License
 *
 * Copyright (c) 2019-present Zhi-Jie Wang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cc.jerrywang.androidexamples.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import cc.jerrywang.androidexamples.R;
import cc.jerrywang.androidexamples.account.metadata.UserMetadata;
import cc.jerrywang.androidexamples.account.task.AuthTask;

public class GoogleAccount implements Account<Intent, AuthTask>, UserMetadata {

    private static final int GOOGLE_SIGN_IN = 9001;
    private enum RESULT {SUCCESSES, FAILED}

    private Context context;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;


    public GoogleAccount(Context context) {
        this.context = context;
        this.googleSignInOptions = setGoogleSignInOptions();
        this.googleSignInClient = setGoogleSignInClient();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    private GoogleSignInOptions setGoogleSignInOptions() {
        String id = getContext().getResources().getString(R.string.default_web_client_id);
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(id)
                .requestEmail()
                .build();
    }

    private Context getContext() {
        return context;
    }

    private GoogleSignInClient setGoogleSignInClient() {
        return GoogleSignIn.getClient(getContext(), getGoogleSignInOptions());
    }

    private GoogleSignInOptions getGoogleSignInOptions() {
        return this.googleSignInOptions;
    }

    @Override
    public void startActivityForResult () {
        Activity activity = (Activity) getContext();
        Intent intent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(intent, GOOGLE_SIGN_IN);
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    @Override
    public boolean isSignedIn() {
        return getUser() != null;
    }

    @Override
    public void signUp(Intent intentData, AuthTask authTask) {
        return;
    }

    private FirebaseUser getUser() {
        return firebaseAuth.getCurrentUser();
    }

    @Override
    public String getUid() {
        return getUser().getUid();
    }

    @Override
    public String getDisplayName() {
        return getUser().getDisplayName();
    }

    @Override
    public void signIn(Intent intentData, AuthTask authTask) {
        GoogleSignInAccount gsc = getGoogleSignInAccount(intentData);
        if (gsc == null) {
            authTask.onComplete(RESULT.FAILED.hashCode());
            return;
        }
        firebaseAuthWithGoogle(gsc, authTask);
    }

    private GoogleSignInAccount getGoogleSignInAccount(Intent intentData) {
        if (intentData == null) {
            return null;
        }
        return setGoogleSignInAccount(intentData);
    }

    private GoogleSignInAccount setGoogleSignInAccount(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            return task.getResult(ApiException.class);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
        }
        return null;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount gsc, final AuthTask authTask) {
        AuthCredential credential = GoogleAuthProvider.getCredential(gsc.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            authTask.onComplete(RESULT.SUCCESSES.hashCode());
                        } else {
                            authTask.onComplete(RESULT.FAILED.hashCode());
                        }
                    }
                });
    }

    @Override
    public void signOut(final AuthTask authTask) {
        firebaseAuth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                authTask.onComplete(RESULT.SUCCESSES.hashCode());
            }
        });
    }

    @Override
    public boolean isSuccessful(int hashCode) {
        return hashCode == RESULT.SUCCESSES.hashCode() || hashCode == GOOGLE_SIGN_IN;
    }

}
