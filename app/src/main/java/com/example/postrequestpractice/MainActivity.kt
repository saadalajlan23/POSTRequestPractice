package com.example.postrequestpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var rvMain: RecyclerView
    private lateinit var rvAdapter: RecyclerViewAdapter

    private lateinit var etName: EditText
    private lateinit var etLocation: EditText
    private lateinit var btAdd: Button

    private lateinit var users: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        users = Users()

        rvMain = findViewById(R.id.rvMain)
        rvAdapter = RecyclerViewAdapter(users)
        etName = findViewById(R.id.etName)
        etLocation = findViewById(R.id.etLocation)
        btAdd = findViewById(R.id.btnAdd)
        rvMain.adapter = rvAdapter
        rvMain.layoutManager = LinearLayoutManager(this)

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        apiInterface?.getAll()?.enqueue(object: Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                users = response.body()!!
                rvAdapter.update(users)
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.d("MAIN", "Unable to get data")
            }
        })


        btAdd.setOnClickListener {
            apiInterface!!.addUser(
                UsersInfo(
                    0,
                    etName.text.toString(),
                    etLocation.text.toString(),

                )
            ).enqueue(object : Callback<UsersInfo> {
                override fun onResponse(call: Call<UsersInfo>, response: Response<UsersInfo>) {
                    Toast.makeText(applicationContext, "User added!", Toast.LENGTH_LONG).show()
                    recreate()
                }

                override fun onFailure(call: Call<UsersInfo>, t: Throwable) {
                    Log.d("MAIN", "Something went wrong!")
                }

            })
        }
    }
}