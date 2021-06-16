package com.example.covid19tracker

//package com.example.Covid19Tracker
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.cardview.widget.CardView
//import com.facebook.CallbackManager
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import kotlinx.android.synthetic.main.activity_login.*
//import com.facebook.FacebookSdk
//import com.facebook.appevents.AppEventsLogger
//import com.facebook.appevents.codeless.internal.EventBinding
//
//class LoginActivity : AppCompatActivity() {
//    private val RC_SIGN_IN: Int = 1
//    private val TAG="Google"
//    lateinit var googleSignInClient: GoogleSignInClient
//    lateinit var googleSignInOptions: GoogleSignInOptions
//    lateinit var mCallback: CallbackManager
//    private lateinit var firebaseAuth: FirebaseAuth
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        supportActionBar?.hide()
//        firebaseAuth = FirebaseAuth.getInstance()
//        mCallback= CallbackManager.Factory.create()
//        configureGoogleSignIn()
//        setupUI()
//        sign_up.setOnClickListener {
//            startActivity(Intent(this,SignUpActivity::class.java))
//        }
//        log_in_button.setOnClickListener {
//            if (firebaseAuth.currentUser == null) {
//                if (editTextTextEmailAddress2.text.isNullOrEmpty()) {
//                    Toast.makeText(this, "Please Enter the Email", Toast.LENGTH_SHORT).show()
//                } else if (edit_text_password.text.isNullOrEmpty()) {
//                    Toast.makeText(this, "Please Enter the Password", Toast.LENGTH_SHORT).show()
//                } else if (!editTextTextEmailAddress2.text.isNullOrEmpty() && !edit_text_password.text.isNullOrEmpty()) {
//                    if (edit_text_password.text.length in 6..12) {
//                        logIn(
//                            editTextTextEmailAddress2.text.toString(),
//                            edit_text_password.text.toString()
//                        )
//                    } else {
//                        if (edit_text_password.text.length < 6) {
//                            edit_text_password.error = "Password length should be greater than 6"
//                        }
//                        if (edit_text_password.text.length > 12) {
//                            edit_text_password.error = "Password length should be less than 12"
//                        }
//                    }
//                }
//            }
//        }
//        fb_button.setOnClickListener {
//            Toast.makeText(this, "This Will be implemented soon", Toast.LENGTH_SHORT).show()
//        }
//        ld.setOnClickListener {
//            Toast.makeText(this, "This Will be implemented soon", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun logIn(email: String, password: String) {
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if(!task.isSuccessful){
//                    firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener{
//                        val b= it.result?.signInMethods?.isNotEmpty()
//                        if (b!!){
//                            Toast.makeText(this, "Incorrect Details", Toast.LENGTH_SHORT).show()
//                        }else{
//                            Toast.makeText(
//                                this,
//                                "No account with this email,Sign up",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//                else{
//                    val intent= Intent(this,MainActivity::class.java)
//                    intent.putExtra("email",email)
//                    intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                    Toast.makeText(this, "LogIn Successful", Toast.LENGTH_LONG).show()
//                }
//
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
//            }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val currentUser = firebaseAuth.currentUser
//        if (currentUser!=null){
//            val email=currentUser.email
//            val intent= Intent(this,MainActivity::class.java)
//            intent.putExtra("email",email)
//            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//        }
//        //updateUI(currentUser)
//    }
//    private fun configureGoogleSignIn() {
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient=GoogleSignIn.getClient(this,googleSignInOptions)
//    }
//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//    private fun setupUI() {
//        google_button.setOnClickListener {
//            signIn()
//        }
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//                // ...
//            }
//        }
//    }
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
//                    task.result?.additionalUserInfo?.isNewUser
//                    val user = firebaseAuth.currentUser
//                    val name=user?.displayName
//                    val email=user?.email
//                    val intent= Intent(this,MainActivity::class.java)
//                    intent.putExtra("email",email)
//                    intent.putExtra("name",name)
//                    startActivity(intent)
//                    //updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    // ...
//                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
//                    //updateUI(null)
//                }
//
//                // ...
//            }
//    }
//}