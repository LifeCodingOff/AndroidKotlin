package com.example.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var db : AppDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // AppDataBase의 getInstance를 사용하여서 db를 초기화 한다.
        db = AppDataBase.getInstance(this)
    }

    override fun onDestroy() {
        db = null
        AppDataBase.deleteInstance()
        super.onDestroy()
    }

    // insert, delete, update 함수들은 각각 삽입, 삭제, 업데이트 기능을 구현 한 것이다

    fun insert(view: View){
        val name = if(insertName.text.isBlank()){
            "Empty"
        }else{
            insertName.text.toString()
        }

        val writer = if (insertWriter.text.isBlank()){
            "Empty"
        }else{
            insertWriter.text.toString()
        }

        val price = if (insertPrice.text.isBlank()){
            0
        }else{
            insertPrice.text.toString().toInt()
        }

        // 다음과 같이 Database의 접근하기 위해서 CoroutineScope를 감싸준 것이다. 이렇게 감싸주지 않는다면
        // 에러가 발생하며 이는 main thread에서는 DB에 접근할 수 없다는 것이다.
        // 이같은 문제는 CoroutineScope를 사용하여 Dispatchers.IO Thread로 접근하여 해결할 수 있다.
        // 결국 문제는 Database의 접근하기 위해서는 Main Thread에서는 불가한 것이므로 꼭, CoroutineScope를 사용하지 않아여도
        // 다른 방법으로도 해결 가능하다.
        CoroutineScope(Dispatchers.IO).launch {
            db?.bookDao()?.insertBook(Book(name, writer, price))
        }

        Toast.makeText(this, "삽입 성공!", Toast.LENGTH_SHORT).show()
        insertName.setText("")
        insertWriter.setText("")
        insertPrice.setText("")
    }

    fun delete(view: View) {
        val id = if (deleteID.text.isBlank()) {
            0L
        } else {
            deleteID.text.toString().toLong()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val check = db?.bookDao()?.isBook(id)

            CoroutineScope(Dispatchers.Main).launch {
                if (check == 1){
                    Toast.makeText(applicationContext, "삭제 성공!", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(applicationContext, "해당 ID 없음!", Toast.LENGTH_SHORT).show()
                }
            }
            db?.bookDao()?.deleteBook(id)
        }
        deleteID.setText("")
    }

    fun update(view: View){
        val id = updateID.text.toString().toLong()
        val value = updateValue.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            when(view.id){
                R.id.unBtn -> db?.bookDao()?.updateName(id, value)
                R.id.uwBtn -> db?.bookDao()?.updateWriter(id, value)
                R.id.upBtn -> {
                    if(value.isDigitsOnly())
                        db?.bookDao()?.updatePrice(id, value.toInt())
                    else
                        db?.bookDao()?.updatePrice(id, 0)
                }
            }
        }
        updateID.setText("")
        updateValue.setText("")
    }

    // listUpdate 함수는 현재 DB에 저장된 값들을 가져와 View에 보여줄 수 있도록 처리한다.
    // 위와 같은 이유에서 DB에 접근하기 위하여 CoroutineScope으로 감싸주었는데 그 안에 또
    // CoroutineScope를 사용하였다.
    //이는 왜냐하면 DB에 접근하기 위해서 Dispatchers.IO Thread로 접근하고 있는 상태에서
    // Main Thread에서 현재 UI를 보여주고 있는 textView를 수정하기 위해서는 Main Thread에서 접근하여야 하기 때문이다.
    //따라서 현재 UI를 수정하기 위해서 CoroutineScope(Dispatchers.Main) 를 사용하여 Main Thread으로 접근하여 textView를 수정하였다.
    fun listUpdate(view: View){
        CoroutineScope(Dispatchers.IO).launch {
            val count = db?.bookDao()?.getCount()
            val list = db?.bookDao()?.getAll()

            CoroutineScope(Dispatchers.Main).launch {
                if(count == 0 ) {
                    textView.text = "데이터가 없습니다."
                } else {
                    list?.forEach { Log.d("Testing", it.toString()) }
                    textView.text = list?.joinToString("\n")
                }
            }
        }
    }


}