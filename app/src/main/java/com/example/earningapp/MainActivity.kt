package com.example.earningapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.earningapp.databinding.ActivityMainBinding
import com.example.earningapp.model.User
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUp.setOnClickListener{
            if(binding.signupname.text.toString().equals("")||
                binding.signupemail.text.toString().equals("")||
                binding.signupage.text.toString().equals("")||
                binding.signuppassword.text.toString().equals("")){
                Toast.makeText(this, "please fill al the details", Toast.LENGTH_SHORT).show()
            }
            else{
                Firebase.auth.createUserWithEmailAndPassword(binding.signupemail.text.toString(),
                    binding.signuppassword.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            var user= User(binding.signupname.text.toString(),
                                binding.signupage.text.toString().toInt(),
                                binding.signupemail.text.toString(),
                                binding.signuppassword.text.toString())

                            Firebase.database.reference.child("Users")
                                .child(Firebase.auth.currentUser!!.uid).setValue(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this,HomeActivity::class.java))
                                    finish()
                                }


                        }
                       else{
                            Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }
            }
                
        }
    }

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser!=null){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
}