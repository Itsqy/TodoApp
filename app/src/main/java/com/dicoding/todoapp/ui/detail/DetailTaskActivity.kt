package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private lateinit var tvTitle: TextInputEditText
    private lateinit var btnDelete: Button
    private lateinit var tvdesc: TextInputEditText
    private lateinit var tvDate: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        tvTitle = findViewById(R.id.detail_ed_title)
        tvdesc = findViewById(R.id.detail_ed_description)
        btnDelete = findViewById(R.id.btn_delete_task)
        tvDate = findViewById(R.id.detail_ed_due_date)

        detailTaskViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[DetailTaskViewModel::class.java]

        detailTaskViewModel.setTaskId(intent.getIntExtra(TASK_ID, 0))

        detailTaskViewModel.task.observe(this) { task ->
            if (task != null) {
                Log.d("TAG", "onCreate: ${task.title}")
                tvTitle.setText(task.title)
                tvdesc.setText(task.description)
                tvDate.setText(DateConverter.convertMillisToString(task.dueDateMillis))
            }

        }

        btnDelete.setOnClickListener {
            detailTaskViewModel.deleteTask()
            finish()
        }

    }
}