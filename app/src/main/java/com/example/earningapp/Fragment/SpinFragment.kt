package com.example.earningapp.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.R
import com.example.earningapp.Withdrawal
import com.example.earningapp.databinding.FragmentSpinBinding
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
import java.util.Random

//import kotlin.random.Random

class SpinFragment : Fragment() {
  var currentChance=0L
    var currentCoin=0L
  private lateinit var binding:FragmentSpinBinding
  private lateinit var timer:CountDownTimer
  private val itemTitles=arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding=FragmentSpinBinding.inflate(inflater,container, false)
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
        com.google.firebase.Firebase.database.reference.child("PlayChance").child(com.google.firebase.Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        currentChance=snapshot.getValue() as Long
                        binding.chance.text = (snapshot.getValue() as Long).toString()

                    }
                    else{
                        binding.chance.text ="0"
                        Toast.makeText(requireContext(), "Out of Spin!!!", Toast.LENGTH_SHORT).show()
                        binding.Spin.isEnabled=false
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        com.google.firebase.Firebase.database.reference.child("playerCoin").child(com.google.firebase.Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        currentCoin =snapshot.getValue() as Long
                        binding.coin.text=currentCoin.toString()

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        return binding.root
    }
    private fun showResult(itemTitle:String,spin:Int){
        if(spin%2==0){
            var wincoin=itemTitle.toInt()
            var history=historymodelclass(System.currentTimeMillis().toString(),wincoin.toString(),false)
            Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
                .setValue(wincoin+currentCoin)
            Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid)
                .push()
                .setValue(history)

            binding.coin.text=(wincoin+currentCoin).toString()

        }
        Toast.makeText(requireContext(), itemTitle, Toast.LENGTH_SHORT).show()
        currentChance=currentChance-1
        com.google.firebase.Firebase.database.reference.child("PlayChance").child(com.google.firebase.Firebase.auth.currentUser!!.uid)
            .setValue(currentChance)
        binding.Spin.isEnabled=true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.Spin.setOnClickListener{
            binding.Spin.isEnabled=false
            if(currentChance>0){

                val spin = Random().nextInt(6)
                val degree=60f*spin

                timer=object:CountDownTimer(5000,50){
                    var rotation=0f
                    override fun onTick(millisUntilFinished: Long) {
                        rotation+=5f
                        if(rotation>=degree){
                            rotation=degree
                            timer.cancel()
                            showResult(itemTitles[spin],spin)
                        }
                        binding.wheel.rotation=rotation
                    }

                    override fun onFinish() {}
                }.start()
            }
            else{
                Toast.makeText(requireContext(), "Out of Spin!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}