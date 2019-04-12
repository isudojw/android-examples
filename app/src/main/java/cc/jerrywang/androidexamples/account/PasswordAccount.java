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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cc.jerrywang.androidexamples.account.bundle.AccountBundle;
import cc.jerrywang.androidexamples.account.metadata.UserMetadata;
import cc.jerrywang.androidexamples.account.task.AuthTask;

public class PasswordAccount implements Account<AuthTask>{

    private enum RESULT {SUCCESSES, FAILED}

    private Context context;
    private FirebaseAuth firebaseAuth;

    public PasswordAccount(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean isSignedIn() {
        return getAuth().getCurrentUser() != null;
    }

    private FirebaseAuth getAuth() {
        return this.firebaseAuth;
    }

    @Override
    public void signIn(AccountBundle bundle, final AuthTask authTask) {
        getAuth().signInWithEmailAndPassword(bundle.getEmail(), bundle.getPassword())
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

    private Activity getActivity() {
        return (Activity) this.context;
    }

    @Override
    public void signUp(AccountBundle bundle, final AuthTask authTask) {
        getAuth().createUserWithEmailAndPassword(bundle.getEmail(), bundle.getPassword())
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
    public void signOut(AuthTask authTask) {
        getAuth().signOut();
        authTask.onComplete(RESULT.SUCCESSES.hashCode());
    }

    @Override
    public boolean isSuccessful(int resultCode) {
        return resultCode == RESULT.SUCCESSES.hashCode();
    }

    @Override
    public void startActivityForResult() {
        return;
    }

    @Override
    public String getUid(String defValue) {
        return isSignedIn() ? getUser().getUid() : defValue;
    }

    private FirebaseUser getUser() {
        return getAuth().getCurrentUser();
    }

    @Override
    public String getDisplayName(String defValue) {
        return isSignedIn() ? getUser().getDisplayName() : defValue;
    }
}
