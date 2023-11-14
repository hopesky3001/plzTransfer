package com.omapp.plztransfer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.omapp.plztransfer.databinding.ActivityAddTfactivityBinding
import com.omapp.plztransfer.db.AppDatabase
import com.omapp.plztransfer.db.TFDao
import com.omapp.plztransfer.db.TFEntity

//db추가 클래스
class AddTFActivity : AppCompatActivity(){

    lateinit var binding: ActivityAddTfactivityBinding
    lateinit var db: AppDatabase
    lateinit var tfDao: TFDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //db를 activity에서 쓸 수 있도록 함
        db = AppDatabase.getInstance(this)!!
        tfDao = db.getTFDao()

        //db에 입력된 성적 넣기
        binding.btnExamInsert.setOnClickListener {
            insertTF()
        }

        //activity 종료 - mainActivity로 돌아감
        binding.btnInsertBack.setOnClickListener {
            finish()
        }
    }

    //성적 추가 함수
    private fun insertTF(){
        //시험 이름
        val examTitle = binding.examTitleInsert.text.toString()

        //시험 점수(string -> double 형변환)
        val examScoreString = binding.examScoreInsert.text.toString()
        var examScoreDouble = 0.0

        //시험 백분율(string -> double 형변환)
        val examPercentString = binding.examPercentInsert.text.toString()
        var examPercentDouble = 0.0


        if (examTitle.isBlank() || examScoreString.isBlank() || examPercentString.isBlank()) { //시험 이름과 점수가 작성되지 않았다면
            Toast.makeText(this, "모든 항목은 반드시 채워져야합니다.", Toast.LENGTH_SHORT).show() //에러 출력
        } else if (examScoreString.toDouble() > 100.0 || examScoreString.toDouble() < 0.0 ||
            examPercentString.toDouble() > 100.0 || examPercentString.toDouble() < 0.0 ) {
            // 입력된 score와 percent가 0 미만, 100 초과라면
            Toast.makeText(this, "숫자는 0에서 100 사이의 숫자로 채워주시기 바랍니다.", Toast.LENGTH_SHORT).show() //에러 출력
        } else {
            examPercentDouble = examPercentString.toDouble()
            examScoreDouble = examScoreString.toDouble()
            Thread {

                tfDao.insertTF(TFEntity(null, examTitle, examScoreDouble, examPercentDouble)) //데이터 추가
                runOnUiThread{
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show() //추가되었다는 메시지

                    //입력된 텍스트 지우기
                    binding.examTitleInsert.setText(null)
                    binding.examScoreInsert.setText(null)
                    binding.examPercentInsert.setText(null)
                }
            }.start()
        }
    }
}