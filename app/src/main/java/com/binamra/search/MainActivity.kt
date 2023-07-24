package com.binamra.search

import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {
   private val list=mutableListOf<Model>();
  private  val dislist=mutableListOf<Model>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
     Handler().postDelayed({
         listApp()
     },150)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val mn:MenuItem=menu!!.findItem(R.id.searchapp);
        if(mn!=null){
            val search=mn.actionView as SearchView;
            search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return  true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText !!.isNotEmpty()){
                        if(newText.length>1){
                            dislist.clear();
                            list.forEach { it
                                if(it.name.toLowerCase().contains(newText.toLowerCase())){
                                    dislist.add(it);
                                }
                            }
                            searchap();
                    }else{
                            dislist.clear();
                            dislist.addAll(list);
                            searchap();
                    }}
                   return  true
                }

            } )

        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun listApp() {

        val ap: ArrayList<String> = ArrayList();
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apList: List<PackageInfo> = packageManager.getInstalledPackages(0)
        for (i in 0 until apList.size) {
            //  p.applicationInfo.loadLabel(getPackageManager()).toString();
            //apList[i].applicationInfo.loadIcon(packageManager).toString()
            list.add(
                Model(
                    apList[i].applicationInfo.loadLabel(getPackageManager()).toString(),
                    apList[i].packageName,
                    apList[i].applicationInfo.loadIcon(packageManager)
                )
            );
        }
        // adding all app list
        dislist.addAll(list);
        searchap()
    }
    private fun searchap(){
        val lst: ListView = listAll;
        lst.adapter= MyListAdapter(this,R.layout.applayout,dislist);
        lst.setOnItemClickListener() { parent, view, position, id ->
          //  Toast.makeText(this,"opening app "+list[position].name,Toast.LENGTH_LONG).show();
            try{
                val launchIntent = packageManager.getLaunchIntentForPackage(dislist[position].pack)
                startActivity(launchIntent)
            }catch(e:Exception) {
                Toast.makeText(this,"Cannot open this app",Toast.LENGTH_LONG).show();
            }

        }

    }
}


class Model(val name:String, val pack:String, val photo: Drawable)

