package com.example.earningapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earningapp.R
import com.example.earningapp.Withdrawal
import com.example.earningapp.adaptor.categoryadapter
import com.example.earningapp.adaptor.historyadapter
import com.example.earningapp.databinding.FragmentHistoryBinding
import com.example.earningapp.model.User
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
import java.util.Collections

class HistoryFragment : Fragment() {
    lateinit var adapter:historyadapter
    val binding by lazy{
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    private var listHistory=ArrayList<historymodelclass>()
    private var tempListHistory=ArrayList<historymodelclass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listHistory.clear()
                    tempListHistory.clear()
                    for (datasnapshot in snapshot.children){
                        var data=datasnapshot.getValue((historymodelclass::class.java))
                        tempListHistory.add(data!!)
                    }
                    Collections.reverse(tempListHistory)
                    listHistory.addAll(tempListHistory)
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.coin.setOnClickListener{
            val bottomsheet: BottomSheetDialogFragment = Withdrawal()
            bottomsheet.show(requireActivity().supportFragmentManager,"TEST")
            bottomsheet.enterTransition
        }
        binding.coinicon.setOnClickListener{
            val bottomsheet: BottomSheetDialogFragment = Withdrawal()
            bottomsheet.show(requireActivity().supportFragmentManager,"TEST")
            bottomsheet.enterTransition
        }


        binding.historyRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        adapter= historyadapter(listHistory)
        binding.historyRecyclerView.adapter=adapter
        binding.historyRecyclerView.setHasFixedSize(true)
        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user=snapshot.getValue(User::class.java)
                        binding.name.text=user?.name                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )


        com.google.firebase.Firebase.database.reference.child("playerCoin").child(com.google.firebase.Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        var currentCoin =snapshot.getValue() as Long
                        binding.coin.text=currentCoin.toString()

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}