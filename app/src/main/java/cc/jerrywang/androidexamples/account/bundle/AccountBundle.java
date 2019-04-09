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

package cc.jerrywang.androidexamples.account.bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * 用於攜帶帳戶登入資料和框架意圖的簡單類別
 * @param <IntentE>
 */

public class AccountBundle<IntentE> {

    private enum DATA {EMAIL, PASSWORD, INTENT}
    private Map<DATA, Object> map;

    public AccountBundle() {
        map = new HashMap<>();
    }

    public void putEmail(String email) {
        map.put(DATA.EMAIL, email);
    }

    public String getEmail() {
        return (String) map.get(DATA.EMAIL);
    }

    public void putPassword(String password) {
        map.put(DATA.PASSWORD, password);
    }

    public String getPassword() {
        return (String) map.get(DATA.PASSWORD);
    }

    public void putIntent(IntentE intent) {
        map.put(DATA.INTENT, intent);
    }

    public IntentE getIntent() {
        return (IntentE) map.get(DATA.INTENT);
    }

}
