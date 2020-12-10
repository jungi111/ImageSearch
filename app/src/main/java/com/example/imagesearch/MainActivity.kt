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
    var scroll : String = "top"

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

        val layoutManager = GridLayoutManager(applicationContext, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(itemDecoration())
        recyclerView.setHasFixedSize(true)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(-1)) {
                    if (check) {
                        if (pagenum > 1){
                            pagenum--
                            scroll = "top"
                            retrofitServicec(text, pagenum, textLayout, recyclerView)
                        }
                    }
                    check = false
                } else if (!recyclerView.canScrollVertically(1)) {
                    if (check) {
                        if (pagenum < 50){
                            pagenum++
                            scroll = "end"
                            retrofitServicec(text, pagenum, textLayout, recyclerView)
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
                        retrofitServicec(text, pagenum, textLayout, recyclerView)
                    }
                }, 1000)
            }
        })
    }

    private fun retrofitServicec(
        text: String,
        pagenum: Int,
        textLayout: LinearLayout,
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
                        if (response.body()!!.meta.totalCount == 0){
                            textLayout.visibility = View.VISIBLE
                        }else{
                            textLayout.visibility = View.GONE

                            myAdapter = MainAdapter(applicationContext, response.body()!!)
                            myAdapter.itemClick = object : MainAdapter.ItemClick {
                                override fun onClick(view: View, position: Int) {
                                    val imageurl =
                                        response.body()!!.documents.get(position).imageUrl.trim()
                                    val datetime =
                                        response.body()!!.documents.get(position).datetime
                                    val displaysitename =
                                        response.body()!!.documents.get(position).displaySiteName

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
                            recyclerView.adapter = myAdapter
                            if (scroll == "top"){
                                recyclerView.smoothScrollToPosition(26)
                            }else{
                                recyclerView.smoothScrollToPosition(0)
                            }
                        }
                    }
                }
            })
    }
}