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


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import cc.jerrywang.androidexamples.account.bundle.AccountBundle;
import cc.jerrywang.androidexamples.account.task.AuthTask;
import cc.jerrywang.androidexamples.account.url.AccountUri;

/**
 * {@link Account} 測試用替身樁(mock)，初始化時可加入額外測試情況{@link #addDelayMS(int)}
 */
public class MockAccount implements Account<AuthTask> {

    private enum RESULT {SUCCESSED, FAILED}
    private enum COLUMN {PASSWORD, DISPLAY_NAME}

    private MockAccountHolder holder;
    private int delayMS;
    private Map<String, Object> authMap;
    private Map<String, Object> dataMap;

    /**
     *
     */
    public MockAccount() {
        this(0);
    }

    public MockAccount addDelayMS(int ms) {
        return new MockAccount(ms);
    }

    private MockAccount(int delayMS) {
        this.holder = MockAccountHolder.getInstance();
        this.delayMS = delayMS;
    }

    @Override
    public boolean isSignedIn() {
        return  getCurrentUid() != null && !getCurrentUid().isEmpty();
    }

    private String getCurrentUid() {
        return getHolder().getUid();
    }

    private MockAccountHolder getHolder() {
        return holder;
    }

    private Map getAuthMap() {
        authMap = holder.getAuthMap();
        return holder.getAuthMap();
    }

    @Override
    public void signUp(AccountBundle bundle, AuthTask task) {
        if (!isAccountCreated(bundle.getEmail())) {
            addAccount(bundle.getEmail(), bundle.getPassword());
            setSignIn(bundle.getEmail());
            putTaskAndStart(task, RESULT.SUCCESSED);
            return;
        } else {
            putTaskAndStart(task, RESULT.FAILED);
        }
    }

    private boolean isAccountCreated(String uid) {
        String mapKey = AccountUri.getKey(uid, COLUMN.PASSWORD.name());
        return getAuthMap().containsKey(mapKey);
    }

    private RESULT addAccount(String uid, String password) {
        String mapKey = AccountUri.getKey(uid, COLUMN.PASSWORD.name());
        getAuthMap().put(mapKey, password);
        return RESULT.SUCCESSED;
    }

    private void setSignIn(String uid) {
        getHolder().setUid(uid);
    }

    private void putTaskAndStart(final AuthTask task, RESULT result) {
        final int resultCode = getResultCode(result);
        // startTaskUseThread(task, result);
        startTaskUseExecutor(task, resultCode);
    }

    private void startTaskUseThread(final AuthTask task, final int resultCode) {
        new Thread(getRunnable(task, resultCode)).start();
    }

    private void startTaskUseExecutor(final AuthTask task, final int resultCode) {
        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
        executor.execute(getRunnable(task, resultCode));
    }

    private Runnable getRunnable(final AuthTask task, final int resultCode) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayMS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.onComplete(resultCode);
            }
        };
    }

    private int getResultCode(RESULT result) {
        switch (result) {
            case SUCCESSED:
                return RESULT.SUCCESSED.hashCode();
            case FAILED:
                return RESULT.FAILED.hashCode();
            default:
                return RESULT.FAILED.hashCode();
        }
    }

    @Override
    public void signIn(AccountBundle bundle, AuthTask task) {
        String uid = bundle.getEmail();
        String mapKey = AccountUri.getKey(bundle.getEmail(), COLUMN.PASSWORD.name());
        if (isAccountCreated(uid) && isAuthSuccess(mapKey, bundle.getPassword())) {
            setSignIn(bundle.getEmail());
            putTaskAndStart(task, RESULT.SUCCESSED);
            return;
        } else {
            putTaskAndStart(task, RESULT.FAILED);
        }

    }

    private boolean isAuthSuccess(String mapKey, String password) {
        return getAuthMap().get(mapKey).equals(password);
    }

    @Override
    public void signOut(AuthTask accountTask) {
        setSignOut();
        putTaskAndStart(accountTask, RESULT.SUCCESSED);
    }

    private void setSignOut() {
        getHolder().setUid("");
    }

    @Override
    public boolean isSuccessful(int resultCode) {
        return resultCode == RESULT.SUCCESSED.hashCode();
    }

    @Override
    public void startActivityForResult() {
        // TODO
        return;
    }

    @Override
    public String getUid(String defValue) {
        return isSignedIn() ? getCurrentUid() : defValue;
    }

    @Override
    public String getDisplayName(String defValue) {
        String mapKey = AccountUri.getKey(getCurrentUid(), COLUMN.DISPLAY_NAME.name());
        Object displayName = getDataMap().get(mapKey);
        return displayName == null ? defValue : (String) displayName;
    }

    private Map getDataMap() {
        dataMap = holder.getDataMap();
        return holder.getDataMap();
    }

    /**
     * 用於建與保存帳戶資料,其內部資料將以<code>account://{user}:{column} | {object}</code>型態保存
     */
    private static class MockAccountHolder {

        private static MockAccountHolder holder;

        private Map<String, Object> authMap;
        private Map<String, Object> dataMap;
        private String id;

        private MockAccountHolder() {}

        /**
         * 取得 MockAccountHolder
         * @return 回傳 MockAccountHolder
         */
        public static MockAccountHolder getInstance() {
            if (holder == null) {
                holder = new MockAccountHolder();
            }
            return holder;
        }

        public Map<String, Object> getAuthMap() {
            if (authMap == null) {
                authMap = setNewMap();
            }
            return authMap;
        }

        private Map<String, Object> setNewMap() {
            return new HashMap<>();
        }

        public Map<String, Object> getDataMap() {
            if (dataMap == null) {
                dataMap = setNewMap();
            }
            return dataMap;
        }

        public void setUid(String uid) {
            id = uid;
        }

        public String getUid() {
            return id;
        }
    }
}
