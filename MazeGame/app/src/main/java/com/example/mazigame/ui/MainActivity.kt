package com.example.mazigame.ui

import android.content.Intent
import android.view.View
import com.example.mazigame.R
import com.example.mazigame.base.BaseActivity
import com.example.mazigame.bean.GameBeam
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        intiGameBean()
//    }
//
//    fun intiGameBean(){
//        var text = ArchiveModel.readFile(this,StringUtil.FILE_SET_UP)
//        if (text != null){
//            var json = JSONObject(text)
//            GameBeam.getInstance().degree = (json[StringUtil.KEY_DEGREE] as Int?) ?: 10
//            GameBeam.getInstance().type = (json[StringUtil.KEY_TYPE] as String?) ?: StringUtil.TYPE_TRADITION
//        }else{
//            GameBeam.getInstance().degree =10
//            GameBeam.getInstance().type = StringUtil.TYPE_TRADITION
//        }
//        GameBeam.getInstance().name = "user"
//        ArchiveModel.saveSetUp(this)
//    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onClick(v: View?) {
        when(v){
            main_newGame ->{
                startActivity(NewGameActivity::class.java)
            }
            main_continueGame -> {
                GameBeam.getInstance().name = null
                var intent = Intent(this,PlayGameActivity::class.java)
                intent.putExtra("jixuyouxi",true)
                startActivity(intent)
            }
            main_archive -> startActivity(ArchiveActivity::class.java)
            main_score -> startActivity(HisScoreActivity::class.java)
        }
    }
}
