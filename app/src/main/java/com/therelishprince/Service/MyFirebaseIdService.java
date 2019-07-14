package com.therelishprince.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.therelishprince.Common.Common;
import com.therelishprince.model.Token;


public class MyFirebaseIdService extends FirebaseInstanceIdService {
    String mCurrentUser;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String tokenRefreshed= FirebaseInstanceId.getInstance().getToken();

        if (FirebaseAuth.getInstance().getCurrentUser()==null) {

        }else {
            updateTokenToFirebase(tokenRefreshed);
        }
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Tokens");
        Token token= new Token(tokenRefreshed,false);
        tokens.child(mCurrentUser).setValue(token);
    }
}
