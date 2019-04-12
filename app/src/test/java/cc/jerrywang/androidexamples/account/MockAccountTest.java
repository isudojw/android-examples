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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.jerrywang.androidexamples.account.bundle.AccountBundle;
import cc.jerrywang.androidexamples.account.task.AuthTask;

import static org.junit.Assert.*;

/**
 * MockAccount測試類別
 */
public class MockAccountTest {

    // TODO : AssertionError
    // 錯誤情況 : 使用斷點觀察結果OK,但不設斷點時有AssertionError錯誤發生,初判是使用Callback導致的問題.

    private Account account;
    private String uid = "Jay";
    private String password = "pass";

    @Before
    public void setUp() throws Exception {
        account = new MockAccount().addDelayMS(500);
        // addNewAccount();
    }

    // @Test(expected = AssertionError.class)
    @Test
    public void addNewAccount() {
        AccountBundle bundle = setBundle(uid, password);
        account.signUp(bundle, new AuthTask() {
            @Override
            public void onComplete(int resultCode) {
                assertTrue(account.isSuccessful(resultCode));
            }
        });
    }

    private AccountBundle setBundle(String userName, String password) {
        AccountBundle bundle = new AccountBundle();
        bundle.putEmail(userName);
        bundle.putPassword(password);
        return bundle;
    }

    @Test
    public void testSignOut() {
        account.signOut(new AuthTask() {
            @Override
            public void onComplete(int resultCode) {
                assertFalse(account.isSuccessful(resultCode));
            }
        });
    }

    @Test
    public void testAddNewAccountFailded() {
        AccountBundle bundle = setBundle(uid, "failed");
        account.signUp(bundle, new AuthTask() {
            @Override
            public void onComplete(int resultCode) {
                assertFalse(account.isSuccessful(resultCode));
            }
        });
    }


    @Test
    public void testSignIn() {
        AccountBundle bundle = setBundle(uid, "pass001");
        account.signIn(bundle, new AuthTask() {
            @Override
            public void onComplete(int resultCode) {
                assertTrue(account.isSuccessful(resultCode));
            }
        });
    }

    @Test
    public void testGetUid() {
        assertEquals(uid, account.getUid("failed"));
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("user", account.getDisplayName("user"));
    }
}