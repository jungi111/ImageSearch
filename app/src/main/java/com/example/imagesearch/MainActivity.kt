package com.example.imagesearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.image.model.data.SearchImageResponse
import com.kakao.sdk.common.util.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter: MainAdapter
    lateinit var dataList: ArrayList<SearchImageResponse.Documents>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val keyHash = Utility.getKeyHash(this);

        val recyclerView = this.findViewById<RecyclerView>(R.id.recyclerView)
        val textLayout = this.findViewById<LinearLayout>(R.id.textLayout)
        val editText = this.findViewById<EditText>(R.id.EditText)
        var text: String = ""
        var pagenum: Int = 1
        var check: Boolean = false
        dataList = ArrayList()

        val layoutManager = GridLayoutManager(applicationContext, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(itemDecoration())
        recyclerView.setHasFixedSize(true)

        myAdapter = MainAdapter(applicationContext, dataList)
        recyclerView.adapter = myAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(-1)) {
                    if (check) {
                        if (pagenum > 1) {
                            pagenum--
                            retrofitServicec(text, pagenum, recyclerView)
                        }
                    }
                    check = false
                } else if (!recyclerView.canScrollVertically(1)) {
                    if (check) {
                        if (pagenum < 50) {
                            pagenum++
                            retrofitServicec(text, pagenum, recyclerView)
                        }
                    }
                    check = false
                } else {
                    check = true
                }
            }
        })

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val string: String = s.toString()
                Handler().postDelayed({
                    text = editText.text.toString()

                    if (string.equals(text) == true) {
                        retrofitServicec(text, pagenum, recyclerView)
                    }
                    if (s.toString().length == 0) {
                        textLayout.visibility = View.VISIBLE
                        dataList.clear()
                    } else {
                        textLayout.visibility = View.GONE
                    }
                }, 1000)
            }
        })
    }

    private fun retrofitServicec(
        text: String,
        pagenum: Int,
        recyclerView: RecyclerView
    ) {
        SearchRetrofit.getService().getSearchImage(searchText = text, page = pagenum)
            .enqueue(object :
                Callback<SearchImageResponse> {
                override fun onFailure(call: Call<SearchImageResponse>, t: Throwable) {
                    Log.w("TAG", "error: " + t.message);
                }

                override fun onResponse(
                    call: Call<SearchImageResponse>,
                    response: Response<SearchImageResponse>
                ) {
                    if (response.isSuccessful) {
                        for (str in response.body()!!.documents) {
                            dataList.add(str)
                        }
                        myAdapter.notifyDataSetChanged()

//                        myAdapter = MainAdapter(applicationContext, response.body()!!)
                        myAdapter.itemClick = object : MainAdapter.ItemClick {
                            override fun onClick(view: View, position: Int) {
                                val imageurl =
                                    dataList.get(position).imageUrl.trim()
                                val datetime =
                                    dataList.get(position).datetime
                                val displaysitename =
                                    dataList.get(position).displaySiteName

                                val nextIntent = Intent(
                                    applicationContext,
                                    ImageActivity::class.java
                                )
                                nextIntent.putExtra("imageurl", imageurl)
                                nextIntent.putExtra("datetime", datetime)
                                nextIntent.putExtra("displaysitename", displaysitename)
                                startActivity(nextIntent)
                            }
                        }
                    }
                }
            })
    }
}