package com.example.julian.homebrewjournal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*

private const val RC_SIGN_IN = 123
private var TAG = LoginActivity::class.simpleName

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            //already signed
        } else {
            //not signed
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(
                            Arrays.asList(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                          AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                    .build(),
                    RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK){
                startActivity(BeerList.createIntent(this, response))
                finish()
                return
            } else {
                if(response == null){
                    showSnackbar(R.string.sign_in_cancelled)
                    return
                }
                if(response.errorCode == ErrorCodes.NO_NETWORK){
                    showSnackbar(R.string.no_internet_connection)
                    return
                }
                if(response.errorCode == ErrorCodes.UNKNOWN_ERROR){
                    showSnackbar(R.string.unknown_error)
                    return
                }
            }
            showSnackbar(R.string.unknown_sign_in_response)
        }
    }
}

fun showSnackbar(text: String){
    var snackbar = Snackbar.make(findViewById, text, Snackbar.LENGTH_LONG).show()

}
