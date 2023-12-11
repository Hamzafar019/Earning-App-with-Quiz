package com.example.earningapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentProfileBinding
import com.example.earningapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
   private val binding by lazy{
       FragmentProfileBinding.inflate(layoutInflater)
   }
    var isExpand=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.imageButton.setOnClickListener{

            if(isExpand==true){
                binding.constraintLayout.visibility=View.VISIBLE
                Toast.makeText(requireContext(), "up", Toast.LENGTH_SHORT).show()
                binding.imageButton.setImageResource(R.drawable.arrowup)
            }
            else{
                binding.constraintLayout.visibility=View.GONE
                Toast.makeText(requireContext(), "down", Toast.LENGTH_SHORT).show()
                binding.imageButton.setImageResource(R.drawable.downarrow)
            }
            isExpand=!isExpand
        }
        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user=snapshot.getValue(User::class.java)
                        binding.name.text=user?.name
                        binding.nameup.text=user?.name
                        binding.email.text=user?.email
                        binding.age.text=user?.age.toString()
                        binding.password.text=user?.password
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}