package com.example.earningapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.earningapp.R
import com.example.earningapp.Withdrawal
import com.example.earningapp.adaptor.categoryadapter
import com.example.earningapp.databinding.FragmentHomeBinding
import com.example.earningapp.model.User
import com.example.earningapp.model.categoryModelClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var categoryList=ArrayList<categoryModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            val bottomsheet:BottomSheetDialogFragment=Withdrawal()
            bottomsheet.show(requireActivity().supportFragmentManager,"TEST")
            bottomsheet.enterTransition
        }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryList.clear()
        categoryList.add(categoryModelClass(R.drawable.scince1,"Science"))
        categoryList.add(categoryModelClass(R.drawable.geography,"Geography"))
        categoryList.add(categoryModelClass(R.drawable.english1,"English"))
        categoryList.add(categoryModelClass(R.drawable.math,"Mathematics"))
        binding.categoryRecyclerView.layoutManager=GridLayoutManager(requireContext(),2)
        var adapter=categoryadapter(categoryList,requireActivity())
        binding.categoryRecyclerView.adapter=adapter
        binding.categoryRecyclerView.setHasFixedSize(true)
    }
    companion object {

    }
}