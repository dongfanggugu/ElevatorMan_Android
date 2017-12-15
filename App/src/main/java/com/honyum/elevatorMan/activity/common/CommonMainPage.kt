package com.honyum.elevatorMan.activity.common

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.adapter.BaseListViewAdapter
import com.honyum.elevatorMan.adapter.BaseViewHolder
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.constant.Constant
import com.honyum.elevatorMan.data.IconInfo
import com.honyum.elevatorMan.utils.Preference
import kotlinx.android.synthetic.main.activity_new_main.*
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.Window
import android.view.WindowManager
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.navisdk.util.common.StringUtils
import com.honyum.elevatorMan.activity.common.ChatActivity.MODE_PROPERTY
import com.honyum.elevatorMan.activity.common.CommonMainPage.Companion.dealViewDispatcher
import com.honyum.elevatorMan.activity.company.CompanyApplyActivity
import com.honyum.elevatorMan.activity.company.InsuranceLookActivity
import com.honyum.elevatorMan.activity.maintenance.LiftActivity
import com.honyum.elevatorMan.activity.maintenance.LiftCompleteActivity
import com.honyum.elevatorMan.activity.maintenance.LiftPlanActivity
import com.honyum.elevatorMan.activity.maintenance.MaintenanceServiceActivity
import com.honyum.elevatorMan.activity.worker.AlarmListActivity
import com.honyum.elevatorMan.activity.worker.EbuyActivity
import com.honyum.elevatorMan.activity.worker.FixOrderListActivity
import com.honyum.elevatorMan.adapter.BannerAdapter
import com.honyum.elevatorMan.base.ListItemCallback
import com.honyum.elevatorMan.data.BannerInfo
import com.honyum.elevatorMan.data.IconInfosTag
import com.honyum.elevatorMan.net.*
import com.honyum.elevatorMan.net.base.*
import com.honyum.elevatorMan.net.base.NewRequestHead
import com.honyum.elevatorMan.utils.IconXmlUtil
import com.honyum.elevatorMan.utils.Utils
import kotlinx.android.synthetic.main.layout_title.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by Star on 2017/12/1.
 */
class CommonMainPage : BaseActivityWraper(), ListItemCallback<ImageView> {
    override fun performItemCallback(data: ImageView?) {
        if (StringUtils.isNotEmpty(data?.tag as String)) {
            requestBannerAdv(data?.tag as String, data)
        }
    }

    private fun requestBannerAdv(Id: String, iv: ImageView?) {
        val server = config.server + NetConstant.GET_ADVERTISEMENT_DETAIL


        val request = AdvDetailRequest()
        request.head = NewRequestHead().setuserId(config.userId).setaccessToken(config.token)
        request.body = request.AdvDetailBody().setId(Id)


        val netTask = object : NetTask(server, request) {
            override fun onResponse(task: NetTask, result: String) {
                val response = AdvDetailResponse.getAdvDetail(result)
                val i = response.body.content
                if (iv != null) {
                    val intent = Intent(this@CommonMainPage, NousDetailActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("kntype", "详情")
                    bundle.putString("content", i)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }

            }
        }

        addBackGroundTask(netTask)
    }

    private lateinit var mIconInfos: List<IconInfosTag>
    var dm: DisplayMetrics? = null

    private var colWidth: Int = 0//每列宽度
    //private val LIE = 12//列数


    private var bannerHeight = 0
    //common icon data list
    private var commonList: MutableList<IconInfo>?  by Preference(act, Constant.WORK_ICON, ArrayList())

    override fun getTitleString(): String {

        return "首页"
    }

    override fun onResume() {
        super.onResume()
        setValue()
    }

    private var prePos: Int = 0

    private lateinit var vp: ViewPager

    private var curItemPos: Int = 0
    private fun initPageIndicator(pics: List<BannerInfo>) {

        val view = findViewById(R.id.main_page_indicator)
        val bannerLayout = view.layoutParams
        bannerLayout.height = bannerHeight
        view.layoutParams = bannerLayout
        vp = view.findViewById(R.id.viewPager) as ViewPager
        val adapter = BannerAdapter(this, pics)
        vp.adapter = adapter
        vp.currentItem = adapter.count / 2
        curItemPos = adapter.count / 2

        val llIndicator = view.findViewById(R.id.ll_indicator) as LinearLayout
        for (pic in pics) {
            val iv = ImageView(this)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.leftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt()
            iv.layoutParams = params
            iv.setBackgroundResource(R.drawable.sel_page_indicator)
            llIndicator.addView(iv)
        }
        llIndicator.getChildAt(0).isEnabled = false

        vp.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                llIndicator.getChildAt(position % pics.size).isEnabled = false
                llIndicator.getChildAt(prePos).isEnabled = true
                prePos = position % pics.size
                curItemPos = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


    }

    private fun requestBanner() {
        val server = config.server + NetConstant.GET_BANNER

        //String request = Constant.EMPTY_REQUEST;

        val netTask = object : NetTask(server, EmptyRequest()) {
            override fun onResponse(task: NetTask, result: String) {
                //Log.e("TAG", "onResponse: "+result );
                val response = BannerResponse.getResult(result)
                initPageIndicator(response.body)
            }
        }

        addBackGroundTask(netTask)
    }

    private fun fullScreen(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var window = activity.window
                var decorView = window.decorView
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
                window.navigationBarColor = Color.TRANSPARENT
            } else {
                val window = activity.window
                val att = window.attributes
                val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                att.flags = att.flags or flagTranslucentStatus
                att.flags = att.flags or flagTranslucentNavigation
                window.attributes = att
            }
        }
    }

    private fun setValue() {

        if (Utils.isEmpty(commonList))
            commonList = mIconInfos[0].iconsInfo
        gl_main_common.adapter = MainPageIconAdapter(commonList, ctx)

        val params = LinearLayout.LayoutParams(gl_main_common.adapter.count * colWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        gl_main_common.layoutParams = params
        gl_main_common.columnWidth = colWidth
        gl_main_common.stretchMode = GridView.NO_STRETCH
        gl_main_common.numColumns = gl_main_common.adapter.count
    }

    private fun getScreenDen() {
        dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        colWidth = dm?.widthPixels!! / 4
    }

    private lateinit var eSaveDialog: Dialog
    private lateinit var eManagerDialog: Dialog
    override fun initView() {
        var iconUtil = IconXmlUtil(ctx)
        mIconInfos = iconUtil.getIconByRoleId(config.roleId)

        horizontalScrollView?.isHorizontalScrollBarEnabled = false// 隐藏滚动条

        getScreenDen()
        requestBanner()
        setValue()
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        bannerHeight = dm.widthPixels / 2


        initEsaveDialog()
        initEmanagerDialog()
        prepare_list.onClick {
            startActivity<ToDoListActivity>()
        }

        val bannerLayout1 = image_banner.layoutParams
        bannerLayout1.height = bannerHeight*2/3
        image_banner.layoutParams = bannerLayout1
        //findViewById(R.id.title).visibility = GONE

        // tv_e_ave.background.alpha = 50
        tv_e_ave.onClick {
            eSaveDialog.show()
        }

        e_manager.onClick {
            eManagerDialog.show()
        }
        //e_manager.background.alpha = 50
        initCommonIcon1(R.drawable.e_manager, e_manager)
        initCommonIcon1(R.drawable.e_save, tv_e_ave)
        initCommonIcon(R.drawable.poistion_icon, pos_img, 20.00f, 0)

        tv_more.onClick {
            startActivity<EditIconActivity>()
        }

        btn_back.visibility = GONE
        initCommonIcon(R.drawable.question, question)
        question.onClick {

            startActivity<NousActivity>("kntype" to "常见问题")
        }
        initCommonIcon(R.drawable.id_num, id_num)
        id_num.onClick {
            startActivity<NousActivity>("kntype" to "故障码")
        }
        initCommonIcon(R.drawable.handle_book, handle_book)
        handle_book.onClick {
            startActivity<NousActivity>("kntype" to "操作手册")

        }
        initCommonIcon(R.drawable.safe_rule, safe_rule)
        safe_rule.onClick {
            startActivity<NousActivity>("kntype" to "安全法规")
        }


        //电梯商城
        initCommonIcon1(R.drawable.ele_mall, ele_mall)
        ele_mall.onClick {
            startActivity<EbuyActivity>()
        }

        //怡墅维保
        initCommonIcon1(R.drawable.nice_hose_pro, nice_hose_pro)
        nice_hose_pro.onClick {
            startActivity<MaintenanceServiceActivity>()
        }
        //怡墅维修
        initCommonIcon1(R.drawable.nice_hose_fix, nice_hose_fix)
        nice_hose_fix.onClick {
            startActivity<FixOrderListActivity>()
        }

        //救援交流
        initCommonIcon(R.drawable.save_contract, save_contract)
        save_contract.onClick {
            val it1 = Intent(this@CommonMainPage, ChatActivity::class.java)
            it1.putExtra("enter_mode", MODE_PROPERTY)
            startActivity(it1)
        }

        //报警处置
        initCommonIcon(R.drawable.warn_deal, warn_deal)
        warn_deal.onClick {
            startActivity<AlarmListActivity>("newCode" to 0)
        }
        //报警汇总
        initCommonIcon(R.drawable.warn_detail, warn_detail)
        warn_detail.onClick {
            startActivity<AlarmListActivity>("newCode" to 1)

        }

        //我的电梯
        initCommonIcon(R.drawable.my_ele, my_ele)
        my_ele.onClick {
            startActivity<LiftActivity>()

        }
//        initCommonIcon(R.drawable.e_save, tv_e_ave)
//        initCommonIcon(R.drawable.e_manager, e_manager)

        //制定计划
        initCommonIcon(R.drawable.make_plan, make_plan)
        make_plan.onClick {
            startActivity<LiftPlanActivity>()

        }
        //维保计划
        initCommonIcon(R.drawable.pro_plan, pro_plan)
        pro_plan.onClick {
            startActivity<LiftCompleteActivity>()

        }
        initCommonIcon1(R.drawable.ele_insurance, ele_insurance)
        ele_insurance.onClick {

            if (config.branchId == "0000000000") {
                //        Intent intent = new Intent(this, CompanyApplyActivity.class);
                //        startActivity(intent);
                requestApply()
            } else {
                val b = GetApplyResponseBody()
                b.state = "1"
                dealState(b)
            }

        }
    }

    private fun initEsaveDialog() {
        eSaveDialog = Dialog(this, R.style.MyDialog)
        eSaveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        eSaveDialog.setContentView(R.layout.e_save_dialog)
        eSaveDialog.setCanceledOnTouchOutside(true)
        var layoutparams = eSaveDialog.window.attributes
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT
        eSaveDialog.window.attributes = layoutparams

        eSaveDialog.find<TextView>(R.id.alert_deal).onClick {
            startActivity<AlarmListActivity>("newCode" to 0)
        }
        eSaveDialog.find<TextView>(R.id.alert_detail).onClick {
            startActivity<AlarmListActivity>("newCode" to 1)
        }
        eSaveDialog.find<TextView>(R.id.alert_contract).onClick {
            val it1 = Intent(this@CommonMainPage, ChatActivity::class.java)
            it1.putExtra("enter_mode", MODE_PROPERTY)
            startActivity(it1)
        }
    }

    private fun initEmanagerDialog() {
        eManagerDialog = Dialog(this, R.style.MyDialog)
        eManagerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        eManagerDialog.setContentView(R.layout.e_manager_dialog)
        eManagerDialog.setCanceledOnTouchOutside(true)
        var layoutparams = eSaveDialog.window.attributes
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT
        eManagerDialog.window.attributes = layoutparams
        eManagerDialog.find<TextView>(R.id.alert_my_ele).onClick {
            startActivity<LiftActivity>()
        }
        eManagerDialog.find<TextView>(R.id.alert_make_plan).onClick {
            startActivity<LiftPlanActivity>()
        }
        eManagerDialog.find<TextView>(R.id.alert_save_plan).onClick {
            startActivity<LiftCompleteActivity>()
        }
    }

    private fun dealState(body: GetApplyResponseBody) {

        when (body.state) {

            "0" -> {
                val it = Intent(this, CompanyApplyActivity::class.java)
                it.putExtra("data", body)
                startActivity(it)
            }
            "1" -> {
                val it = Intent(this, InsuranceLookActivity::class.java)
                startActivity(it)
            }
            "2" -> {
                val it = Intent(this, CompanyApplyActivity::class.java)
                it.putExtra("data", body)
                startActivity(it)
            }
            else -> {
                val gb = GetApplyResponseBody()
                gb.state = "99"
                val it = Intent(this, CompanyApplyActivity::class.java)
                it.putExtra("data", gb)
                startActivity(it)
            }
        }

    }

    private fun dealResult(response: GetApplyResponse) {

        if (response.body != null) {
            dealState(response.body)
        }
    }

    private fun requestApply() {

        val server = config.server + NetConstant.GET_APPLIY
        val request = RequestBean()
        val head = RequestHead()
        head.accessToken = config.token
        head.userId = config.userId
        request.head = head

        val netTask = object : NetTask(server, request) {
            override fun onResponse(task: NetTask, result: String) {
                val response = GetApplyResponse.getResponse(result)
                dealResult(response)
            }
        }

        addTask(netTask)

    }

    private fun initCommonIcon1(id: Int, textView: TextView, sizeValue: Float = 40.00f) {
        val drawable1 = ContextCompat.getDrawable(this, id)
        var size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeValue, displayMetrics)
        drawable1.setBounds(0, 0, size.toInt(), size.toInt())//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        // drawable1.alpha= 50
        val drawable2 = ContextCompat.getDrawable(this, id)
        drawable2.mutate().alpha = 20
        textView.background = drawable2
        textView.setCompoundDrawables(null, drawable1, null, null)//只放左边

    }

    private fun initCommonIcon(id: Int, textView: TextView, sizeValue: Float = 30.00f, pos: Int = 1) {
        val drawable1 = ContextCompat.getDrawable(this, id)
        var size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeValue, displayMetrics)
        drawable1.setBounds(0, 0, size.toInt(), size.toInt())//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        //drawable1.alpha= 0
        when (pos) {
            0 -> textView.setCompoundDrawables(drawable1, null, null, null)//只放左边
            1 -> textView.setCompoundDrawables(null, drawable1, null, null)//只放左边
            2 -> textView.setCompoundDrawables(null, null, drawable1, null)//只放左边
            3 -> textView.setCompoundDrawables(null, null, null, drawable1)//只放左边
        }
        //  textView.setCompoundDrawables(null, drawable1, null, null)//只放左边

    }


    override fun getLayoutID(): Int {

        return R.layout.activity_new_main
    }

    companion object {
        fun dealViewDispatcher(context: Context, action: String, name: String) {
            val it1 = Intent(context, Class.forName(action))
            when (action) {
                "com.honyum.elevatorMan.activity.common.ChatActivity" ->
                    it1.putExtra("enter_mode", MODE_PROPERTY)
                "com.honyum.elevatorMan.activity.worker.AlarmListActivity" -> {
                    if ("报警处置" == name) it1.putExtra("newCode", 0) else it1.putExtra("newCode", 1)
                }
            }
            context.startActivity(it1)
        }
    }
}

class MainPageIconAdapter(data: List<IconInfo>?, context: Context?, layoutId: Int = R.layout.item_iamge_tag) : BaseListViewAdapter<IconInfo>(data, context, layoutId) {

    override fun bindData(holder: BaseViewHolder?, t: IconInfo?, index: Int) {

        val dwLeft = ContextCompat.getDrawable(mContext, t!!.image)
        holder?.setText(R.id.tv_icon, t.name)?.setBoundsCompoundDrawables(R.id.tv_icon, dwLeft)?.setOnClickListener(R.id.tv_icon, {
            dealViewDispatcher(mContext, t?.action, t?.name)
        }
        )
    }
}