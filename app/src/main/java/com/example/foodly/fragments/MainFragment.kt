package com.example.foodly.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodly.R
import com.example.foodly.adapters.MainAdapter
import com.example.foodly.data.DataList
import com.example.foodly.util.Connection
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.Comparator


class MainFragment : Fragment() {
    lateinit var recl_view:RecyclerView
    lateinit var progress_bar:RelativeLayout
     var list =ArrayList<DataList>()
    lateinit var no_net:ImageView
    lateinit var refresh:SwipeRefreshLayout
    lateinit var search_bar:EditText
    lateinit var appbar:AppBarLayout
    lateinit var nav_view:NavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)
        search_bar = view.findViewById(R.id.main_search_bar)
        refresh = view.findViewById(R.id.main_frg_swipe_refresh)
        search_bar.setHint("Search Restaurants")
        no_net = view.findViewById(R.id.main_frg_no_net)
        appbar = view.findViewById(R.id.main_frg_appbar)
        progress_bar = view.findViewById(R.id.frg_main_prgs)
        nav_view = requireActivity().findViewById(R.id.main_navigation_view) as NavigationView
        nav_view.setCheckedItem(R.id.home_select)
        recl_view = view.findViewById(R.id.frg_main_rec_view)
        if(Connection().checkConnectivity(activity as Context)) {

            no_net.visibility = View.GONE
            appbar.visibility = View.VISIBLE
            val q = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            try{
                progress_bar.visibility = View.VISIBLE
                val jsonreq = object : JsonObjectRequest(
                    Request.Method.GET,url,null,
                    Response.Listener {
                        if(it.getJSONObject("data").getBoolean("success")){
                            list = arrayListOf<DataList>()
                            val data = it.getJSONObject("data").getJSONArray("data")
                            for(i in 0 until data.length()){
                                list.add(
                                    DataList(
                                        data.getJSONObject(i).getString("id"),
                                        data.getJSONObject(i).getString("name"),
                                        data.getJSONObject(i).getString("rating"),
                                        data.getJSONObject(i).getString("cost_for_one"),
                                        data.getJSONObject(i).getString("image_url")
                                    )
                                )
                            }
                            recl_view.layoutManager = LinearLayoutManager(activity)
                            recl_view.adapter = MainAdapter(activity ?: return@Listener,list)
                            progress_bar.visibility = View.GONE
                        }
                    },
                    Response.ErrorListener {
                        appbar.visibility = View.GONE
                        progress_bar.visibility = View.GONE
                        Toast.makeText(activity as Context,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "c3acf1e14c21f9"
                        return headers
                    }
                }
                q.add(jsonreq)
            }catch (e: Exception){
                appbar.visibility = View.GONE
                progress_bar.visibility = View.GONE
                Toast.makeText(activity as Context,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            appbar.visibility = View.GONE
            Toast.makeText(activity as Context,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show()
            no_net.visibility = View.VISIBLE
        }
        search_bar.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val listData = arrayListOf<DataList>()
                for (i in 0 until list.size) {
                    if (list[i].name.toLowerCase()
                            .contains(search_bar.text.toString().toLowerCase()) ||
                        list[i].cost_for_one.contains(search_bar.text.toString()) ||
                        list[i].rating.contains(search_bar.text.toString())
                    ) {
                        listData.add(list[i])
                    }
                }
                recl_view.adapter = MainAdapter(activity as Context, listData)
                (recl_view.adapter as MainAdapter).notifyDataSetChanged()
            }


        }
        )
        refresh.setOnRefreshListener {
            getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit()

        }
        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(Connection().checkConnectivity(activity as Context)) {
            MenuInflater(activity as Context).inflate(R.menu.app_bar_menu, menu)
            return super.onCreateOptionsMenu(menu, inflater)
        }

    }
    


}