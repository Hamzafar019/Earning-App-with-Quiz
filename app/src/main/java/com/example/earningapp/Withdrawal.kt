package com.example.earningapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.databinding.FragmentWithdrawalBinding
import com.example.earningapp.model.historymodelclass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Withdrawal : BottomSheetDialogFragment () {
    var currentCoin=0L
    private val binding:FragmentWithdrawalBinding by lazy{
        FragmentWithdrawalBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        com.google.firebase.Firebase.database.reference.child("playerCoin").child(com.google.firebase.Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        currentCoin =snapshot.getValue() as Long

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        binding.transfer.setOnClickListener{
            if(binding.amount.text.toString().toDouble()<=currentCoin){
                Firebase.database.reference.child("playerCoin").child(com.google.firebase.Firebase.auth.currentUser!!.uid).setValue(currentCoin-binding.amount.text.toString().toDouble())
                var history=historymodelclass(System.currentTimeMillis().toString(),binding.amount.text.toString(),true)

                Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid)
                    .push()
                    .setValue(history)

            }
            else{
                Toast.makeText(requireContext(), "Out of Money!!!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {

    }
}