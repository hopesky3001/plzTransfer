package com.omapp.plztransfer

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.omapp.plztransfer.databinding.ActivityAddTfactivityBinding
import com.omapp.plztransfer.databinding.ActivityCheckTfactivityBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.omapp.plztransfer.db.AppDatabase
import com.omapp.plztransfer.db.TFDao
import com.omapp.plztransfer.db.TFEntity

class CheckTFActivity : AppCompatActivity(), OnItemLongClickListener {

    lateinit var binding: ActivityCheckTfactivityBinding //activity 묶기
    lateinit var db: AppDatabase
    lateinit var tfDao: TFDao // db dao
    lateinit var tfList : ArrayList<TFEntity> //db를 담을 arraylist
    lateinit var adapter: TFRecyclerViewAdapter //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckTfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        tfDao = db.getTFDao()

        //성적 분석 Activity로 이동
        binding.btnAnalyze.setOnClickListener {
                val intent = Intent(this, AnalyzeTFActivity::class.java)
                startActivity(intent)
        }

        //Activity 종료. - mainActivity로 이동
        binding.btnCheckBack.setOnClickListener {
            finish()
        }

        getAllTFList()

    }


    private fun getAllTFList(){
        Thread{
            tfList = ArrayList(tfDao.getAll()) //db 내 모든 항목들을 가져오기
            setRecyclerView() // recycler set
        }.start()
    }

    //recyclerView 셋팅
    private fun setRecyclerView() {
        runOnUiThread{
            adapter = TFRecyclerViewAdapter(tfList, this)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    //재시작
    override fun onRestart() {
        super.onRestart()
        getAllTFList()
    }

    //길게 누르면 항목 삭제
    override fun onLongClick(position: Int) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("해당 시험 삭제")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("네",
            object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteTF(position)
                }
            }
        )
        builder.show()
    }

    //삭제함수
    private fun deleteTF(position: Int){
        Thread{
            tfDao.deleteTF(tfList[position]) //db에서 삭제
            tfList.removeAt(position)
            runOnUiThread{
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}