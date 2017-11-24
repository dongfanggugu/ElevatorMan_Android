package com.honyum.elevatorMan.activity.company

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.activity.PicturePickActivity
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.net.*
import com.honyum.elevatorMan.net.base.*
import com.honyum.elevatorMan.net.base.NewRequestHead
import com.honyum.elevatorMan.utils.Utils
import kotlinx.android.synthetic.main.activity_company_apply.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.act
import org.jetbrains.anko.browse
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

/**
 * Created by Star on 2017/10/17.
 */


//TODO 如果=1 审核成功，进入查询保单列表页面 = 0和=1 已经处理好
class CompanyApplyActivity : BaseActivityWraper() {

    private var imageUrl = ""
    private var imageUrl1 = ""
    private var id = ""


    override fun getTitleString(): String {
        return "企业申请"
    }

    override fun initView() {
        iv_insurance_rule.onClick {
            browse(config.pcServer+NetConstant.COMPANY_APPLY);
        }
        var data = intent.extras.getSerializable("data") as GetApplyResponseBody
        dealState(data)

    }

    fun BaseActivityWraper.showToast1(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }


//    private fun requestApply() {
//
//        var server = config.server + NetConstant.GET_APPLIY
//        var request = RequestBean()
//        var head = RequestHead()
//        head.accessToken= config.token
//        head.userId = config.userId
//        request.head = head
//
//        val netTask = object : NetTask(server, request) {
//            override fun onResponse(task: NetTask, result: String) {
//
//                val response :GetApplyResponse= GetApplyResponse.getResponse(result)
//                dealResult(response)
//            }
//
//        }
//        addTask(netTask)
//
//    }

//    private fun dealResult(response: GetApplyResponse) {
//
//        if (response.body != null) {
//            dealState(response.body)
//        }
//    }

    private fun dealState(body: GetApplyResponseBody) {
        when (body.state) {


            "0" -> {
                inItData(body)

            }
            "1" -> dealIntent()
            "2" -> {
                // inItListener()
                dealUpdate(body)
            }
            "99" -> {
                inItListener()
            }
        }
    }

    private fun inItListener() {
        iv_image_license.setTag(R.id.image_tag, "license1")
        iv_image_license2.setTag(R.id.image_tag, "license2")
        iv_image_license.setOnClickListener {
            var intent = Intent(act, PicturePickActivity::class.java)
            intent.putExtra("tag", iv_image_license.getTag(R.id.image_tag) as String)
            startActivityForResult(intent, 1)
        }
        iv_image_license2.setOnClickListener {
            var intent = Intent(act, PicturePickActivity::class.java)
            intent.putExtra("tag", iv_image_license2.getTag(R.id.image_tag) as String)
            startActivityForResult(intent, 1)
        }
        tv_submit.setOnClickListener { submitApply() }
    }

    private fun inItData(body: GetApplyResponseBody) {
        tv_order_num.visibility = View.VISIBLE
        et_apply_num.isFocusable = false;
        et_apply_num.isFocusableInTouchMode = false;
        et_apply_num.setText(body.code)
        et_company_name.isFocusable = false;
        et_company_name.isFocusableInTouchMode = false;
        et_company_name.setText(body.name)
        et_insurance_address.isFocusable = false;
        et_insurance_address.isFocusableInTouchMode = false;
        et_insurance_address.setText(body.address)
        et_insurance_license_num.isFocusable = false;
        et_insurance_license_num.isFocusableInTouchMode = false;
        et_insurance_license_num.setText(body.licenceCode)
        et_insurance_contact_name.isFocusable = false;
        et_insurance_contact_name.isFocusableInTouchMode = false;
        et_insurance_contact_name.setText(body.userName)
        et_insurance_tel.isFocusable = false;
        et_insurance_tel.isFocusableInTouchMode = false;
        et_insurance_tel.setText(body.userTel)
        et_insurance_email.isFocusable = false;
        et_insurance_email.isFocusableInTouchMode = false;
        et_insurance_email.setText(body.email)
        et_insurance_mark.isFocusable = false;
        et_insurance_mark.isFocusableInTouchMode = false;
        et_insurance_mark.setText(body.remarks)
        imageUrl = body.licenceImg
        imageUrl1 = body.licenceImgb

        id = body.id


        iv_image_license.loadUrl(imageUrl)
        //   GetPicture(imageUrl, iv_image_license).execute()
        iv_image_license2.loadUrl(imageUrl1)

//        Picasso.with(act).load(imageUrl).into(iv_image_license)
//        Picasso.with(act).load(imageUrl1).into(iv_image_license2)
//        Glide.with(act).load(imageUrl).into(iv_image_license)
//        Glide.with(act).load(imageUrl1).into(iv_image_license2)
        tv_rule.visibility = View.GONE
        with(tv_submit)
        {
            this.text = "审核中"
            //this.setOnClickListener { updateApply() }

        }


    }

    private fun ImageView.loadUrl(url: String, width: Int = 60, height: Int = 80) {
        Glide.with(context).load(url).override(width, height).into(this)
    }

    private fun downLoadImage(url: String, path1: String = "license1.jpg", index: Int = 1) {
        if (index == 3) {
            tv_submit.isClickable = true
            return
        }
        async(UI) {

            var bitmap: Deferred<Bitmap> = bg {
                // Runs in background
                Glide.with(act)
                        .load(url)
                        .asBitmap().into(600, 800)
                        .get();
            }
            Utils.saveBitmapWithQuality(bitmap.await(), Utils.getTempPath(), path1, 100)
            downLoadImage(imageUrl1, "license2.jpg", index + 1)
        }
    }

    private fun dealUpdate(body: GetApplyResponseBody) {


        tv_order_num.visibility = View.VISIBLE
        et_apply_num.setText(body.code)
        et_apply_num.isFocusable = false
        et_apply_num.isFocusableInTouchMode = false
        et_company_name.setText(body.name)
        et_insurance_address.setText(body.address)
        et_insurance_license_num.setText(body.licenceCode)
        et_insurance_contact_name.setText(body.manager)
        et_insurance_tel.setText(body.userTel)
        et_insurance_email.setText(body.email)
        et_insurance_mark.setText(body.remarks)
        imageUrl = body.licenceImg
        imageUrl1 = body.licenceImgb
        iv_image_license.loadUrl(imageUrl)
        iv_image_license2.loadUrl(imageUrl1)
        downLoadImage(imageUrl)
        // Log.e("BITMAP!!!!",bitmap.toString())
        downLoadImage(imageUrl1)
        id = body.id
        var path1 = Utils.getTempPath() + "/" + "license1.jpg"
        var path2 = Utils.getTempPath() + "/" + "license2.jpg"
        // Utils.saveBitmapWithQuality(bitmap, Utils.getTempPath(), "license1.jpg", 100)
        //  Utils.saveBitmapWithQuality(bitmap1, Utils.getTempPath(), "license2.jpg", 100)

        Log.e("URL!!!!", path1)
        image1.visibility = View.VISIBLE
        image2.visibility = View.VISIBLE
        loadUpdateListener(image1, iv_image_license, "license1")
        loadUpdateListener(image2, iv_image_license2, "license2")
        iv_image_license.setTag(R.id.file_path, path1)
        iv_image_license2.setTag(R.id.file_path, path2)
        with(tv_submit)
        {
            this.text = "修改"
            tv_submit.isClickable = false
            this.setOnClickListener { updateApply() }

        }
        ck_insure.visibility = View.GONE
        iv_insurance_rule.text = "${body.reason}!"
    }

    fun loadUpdateListener(delete: ImageView, img: ImageView, tag: String) {

        delete.onClick {
            delete.visibility = View.GONE
            img.setImageResource(R.drawable.defaut_image)
            img.setTag(R.id.image_tag, tag)
            img.setOnClickListener {
                var intent = Intent(act, PicturePickActivity::class.java)
                intent.putExtra("tag", img.getTag(R.id.image_tag) as String)
                startActivityForResult(intent, 1)
            }
            if (tag == "license1")
                imageUrl = ""
            else
                imageUrl1 = ""
        }

        img.onClick {
            ll_full_screen.visibility = View.VISIBLE
            if (tag == "license1")
                iv_overview.loadUrl(imageUrl, 600, 800)
            else
                iv_overview.loadUrl(imageUrl1, 600, 800)
            ll_full_screen.onClick { ll_full_screen.visibility = View.GONE }
        }

    }


    private fun updateApply() {

        submitApply(true)
    }

    private fun dealIntent() {


        startActivity<InsuranceLookActivity>()

    }

    private fun submitApply(isUpdate: Boolean = false) {
        var insure = ck_insure.isChecked
        if (!insure && !isUpdate) {
            showToast1("请阅读协议")
            return
        }
        var applyId = ""
        if (isUpdate) {
            applyId = id
        }
        var name = et_company_name?.text.toString()
        var address = et_insurance_address.text.toString()
        var num = et_insurance_license_num.text.toString()
        var contact = et_insurance_contact_name.text.toString()
        var tel = ""
        if (Utils.isMobileNumber(et_insurance_tel.text.toString())) {
            tel = et_insurance_tel.text.toString()
        } else {
            showToast1("请输入正确的手机号码")
            return
        }
        var email = ""


        if (Utils.checkEmail(et_insurance_email.text.toString())) {
            email = et_insurance_email.text.toString()
        }
        else {
            showToast1("请输入正确的邮箱地址")
            return
        }
        var type = et_insurance_type.text.toString()
        var mark = et_insurance_mark.text.toString()


        var requestBody = CompanyApplyRequestBody(applyId, name, address, num, contact,
                tel, email, type, mark, imageUrl, imageUrl1)
        var head = RequestHead()
        head.userId = config.userId
        head.accessToken = config.token

        var companyRequest = CompanyApplyRequest(requestBody)
        companyRequest.head = head

        if (iv_image_license.getTag(R.id.file_path) == null) {
            showToast1("请拍摄营业执照片")
            return
        }
        if (iv_image_license2.getTag(R.id.file_path) == null) {
            showToast1("请拍摄营业执(带公章)照片")
            return
        }

        imageUrl = ""
        imageUrl1 = ""
        requestUploadImage(Utils.imgToStrByBase64(iv_image_license.getTag(R.id.file_path) as String), 1, companyRequest, isUpdate)
        requestUploadImage(Utils.imgToStrByBase64(iv_image_license2.getTag(R.id.file_path) as String), 2, companyRequest, isUpdate)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != -1) {
            return
        }

        val tag = data?.getStringExtra("tag")


        if (tag == "license1") {
            dealImageReturn(data, iv_image_license, image1);
        } else if (tag == "license2") {
            dealImageReturn(data, iv_image_license2, image2);
        }


    }

    private fun dealImageReturn(data: Intent?, license: ImageView, deleteImg: ImageView) {
        val photo = data?.getStringExtra("result")
        val bitmap = Utils.getBitmapBySize(photo, 60, 80) ?: return
        license.setImageBitmap(bitmap)
        license.setTag(R.id.file_path, photo)
        deleteImg.visibility = View.VISIBLE
        license.setOnClickListener { v ->
            val filePath = v.getTag(R.id.file_path) as String
            val bitmap1 = BitmapFactory.decodeFile(filePath)
            iv_overview.setImageBitmap(bitmap1)
            ll_full_screen.visibility = View.VISIBLE
            ll_full_screen.setOnClickListener { ll_full_screen.visibility = View.GONE }
        }
        deleteImg.setOnClickListener {
            deleteImg.visibility = View.GONE
            license.setImageResource(R.drawable.defaut_image)
            license.setOnClickListener {
                var intent = Intent(act, PicturePickActivity::class.java)
                intent.putExtra("tag", license.getTag(R.id.image_tag) as String)
                startActivityForResult(intent, 1)
            }
            imageUrl = ""
            imageUrl1 = ""

        }


    }

    private fun getImageRequestBean(userId: String, token: String, path: String): RequestBean {
        val request = UploadImageRequest()
        request.head = NewRequestHead().setuserId(userId).setaccessToken(token)
        request.body = request.UploadImageBody().setImg(path)
        return request
    }


    private fun requestUploadImage(base64: String, index: Int, body: CompanyApplyRequest, isUpdate: Boolean) {
        val task = object : NetTask(config.server + NetConstant.UP_LOAD_IMG,
                getImageRequestBean(config.userId, config.token, base64)) {
            override fun onResponse(task: NetTask, result: String) {
                val response = UploadImageResponse.getUploadImageResponse(result)
                if (response.head != null && response.head.rspCode == "0") {
                    val url = response.body.url

                    if (index == 1) {
                        imageUrl = url
                    } else {
                        imageUrl1 = url
                    }
                    if (imageUrl != "" && imageUrl1 != "") {
                        body.body.licenceImg = imageUrl
                        body.body.licenceImgb = imageUrl1
                        requestSubmitInfo(body, isUpdate)

                    }

                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        }

        addTask(task)
    }

    private fun requestSubmitInfo(companyRequest: CompanyApplyRequest, isUpdate: Boolean = false) {


        var server: String = if (!isUpdate)
            config.server + NetConstant.ADD_APPLY
        else
            config.server + NetConstant.UPDATE_APPLIY
        var netTask = object : NetTask(server, companyRequest) {
            override fun onResponse(task: NetTask?, result: String?) {
                showToast1("操作成功，请等待审核")
                finish()
            }
        }
        addTask(netTask)

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_company_apply
    }

}