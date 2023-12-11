package com.example.earningapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.earningapp.databinding.ActivityQuizBinding
import com.example.earningapp.model.Question
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class QuizActivity : AppCompatActivity() {
    private val binding:ActivityQuizBinding by lazy{
        ActivityQuizBinding.inflate(layoutInflater)
    }
    var currentChance=0L
    var score=0
    var currentQuestion=0
    private lateinit var questionList:ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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

        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        currentChance = snapshot.getValue() as Long
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        questionList= ArrayList<Question>()
        var image=intent.getIntExtra("categoryimage",0)
        var catText=intent.getStringExtra("categoryType")
        Firebase.firestore.collection("Questions")
            .document(catText.toString())
            .collection("questions")
            .get().addOnSuccessListener {
                questionData->
                questionList.clear()
                for (data in questionData){
                    var question: Question?=data.toObject(Question::class.java)
                    questionList.add(question!!)
                }
                if(questionList.size>0){
                    binding.question.text=questionList.get(currentQuestion).question
                    binding.option1.text=questionList.get(currentQuestion).option1
                    binding.option2.text=questionList.get(currentQuestion).option2
                    binding.option3.text=questionList.get(currentQuestion).option3
                    binding.option4.text=questionList.get(currentQuestion).option4
                }

            }
        binding.categoryimage.setImageResource(image)
        binding.coin.setOnClickListener{
            val bottomsheet: BottomSheetDialogFragment = Withdrawal()
            bottomsheet.show(this.supportFragmentManager,"TEST")
            bottomsheet.enterTransition
        }
        binding.coinicon.setOnClickListener{
            val bottomsheet: BottomSheetDialogFragment =Withdrawal()
            bottomsheet.show(this.supportFragmentManager,"TEST")
            bottomsheet.enterTransition
        }
        binding.option1.setOnClickListener{
            nextQuestionAndScoreUpdate(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener{
            nextQuestionAndScoreUpdate(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener{
            nextQuestionAndScoreUpdate(binding.option3.text.toString())
        }
        binding.option4.setOnClickListener{
            nextQuestionAndScoreUpdate(binding.option4.text.toString())
        }
        setContentView(binding.root)
    }

    private fun nextQuestionAndScoreUpdate(s:String) {
        if(s.equals(questionList.get(currentQuestion).answer)){
            score+=10
        }

        currentQuestion++
        if(currentQuestion>=questionList.size){
            if(score>=30){
                binding.winner.visibility= View.VISIBLE

                Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentChance+1)

            }
            else{
                binding.loss.visibility=View.VISIBLE
            }
        }
        else{

            binding.question.text=questionList.get(currentQuestion).question
            binding.option1.text=questionList.get(currentQuestion).option1
            binding.option2.text=questionList.get(currentQuestion).option2
            binding.option3.text=questionList.get(currentQuestion).option3
            binding.option4.text=questionList.get(currentQuestion).option4
        }

    }
}