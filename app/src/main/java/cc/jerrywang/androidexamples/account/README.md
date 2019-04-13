## Account
1. 將整個目錄複製到專案裡
2. 在 Android Studio 裡使用 Tool -> Firebase -> Authentication 建立專案並取得 client ID
## Google
### Sign In
* ```GoogleAccount(Context)```
* ```AccountBundle<Intent>```
* ```Auth```

登入時須另外使用 ```startActivityForResult()``` 和 ```@Override onActivityResult()``` 取得資料,並使用```AccountBundle<Intent>``` 將帶有登入資料的 ```Intent``` 傳入
```java
// 取得本機帳戶資料意圖
private void addSignEvent() {
    ...
        getAccount().startActivityForResult();
    ...
}

// 接收登入帳戶資訊
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    ...
        AccountBundle<Intent> bundle = new AccountBundle<>();
        bundle.putIntent(data);
        startSignInEvent(bundle);
    ...    
}
```

#### Android Java Example
```java  
private Account account;

// TODO : 適合加入 onCreated() 
private void initAccount() {
    setAccount();
    updateAccount();
    addSignEvent();
}

// TODO : Google or Facebook or Twitter
private void setAccount() {
    account = new GoogleAccount(this);
}

// TODO : 適合加入 onStart()    
private void updateAccount() {
    if (getAccount().isSignedIn()) {
        updateAccountItems()
    }
}  
  
private Account getAccount() {
    if (account == null) {
        setAccount();
    }  
    return account;  
}

private void updateAccountItems() {
    // TODO : 將需要更新的元件放在這裡, 例如 userID 和 image...等
}

// TODO : 按下按鈕後觸發,如果是Layout觸發需改寫
private void addSignEvent(Button button) {
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addSignInAnimation();
            getAccount().startActivityForResult();
        }
    });
}

private void addSignInAnimation() {
    // TODO : 加入過場用元件
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (getAccount().isSuccessful(requestCode)) {
        AccountBundle<Intent> bundle = new AccountBundle<>();
        bundle.putIntent(data);
        startSignInEvent(bundle);
    }
}

private void startSignInEvent(AccountBundle bundle) {
    getAccount().signIn(bundle, new AuthTask() {
        @Override
        public void onComplete(int resultCode) {
            // TODO : 登入成功,需要更新或關閉的動作放在這裡
            updateAccountItems();
            removeSignInAnimation();
            }
        });
}

private void removeSignInAnimation() {
    // TODO : 移除過場用元件
}
```