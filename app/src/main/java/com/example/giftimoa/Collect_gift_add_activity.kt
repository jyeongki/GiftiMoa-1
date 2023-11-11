package com.example.giftimoa

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.giftimoa.ViewModel.Gificon_ViewModel
import com.example.giftimoa.databinding.LayoutCollectGiftAddBinding
import com.example.giftimoa.dto.Collect_Gift

class Collect_gift_add_activity : AppCompatActivity() {
    private lateinit var binding : LayoutCollectGiftAddBinding

    private lateinit var giftViewModel: Gificon_ViewModel
    private val REQUEST_READ_EXTERNAL_STORAGE = 1000
    private var imageUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutCollectGiftAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        giftViewModel = ViewModelProvider(this).get(Gificon_ViewModel::class.java)
        //액션바 활성화
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "기프티콘 등록"

        galleryClickEvent()
        giftAdd_Btn()

    }

    //이미지 클릭시 갤러리 권한 요청 및
    private fun galleryClickEvent(){
        binding.uploadImage.setOnClickListener ({
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                //권한이 허용되지 않음
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ){
                    //이전에 이미 권한이 거부되었을 때 설명
                    var dlg = AlertDialog.Builder(this)
                    dlg.setTitle("권한이 필요한 이유")
                    dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다.")
                    dlg.setPositiveButton("확인") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this@Collect_gift_add_activity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                    dlg.setNegativeButton("취소", null)
                    dlg.show()
                }else {
                    //권한 요청
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
            } else {

                loadImage()

            }
        })
    }
    private fun loadImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activityResult.launch(intent)
    }
    //갤러리 호출
    private val activityResult:ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if(it.resultCode == RESULT_OK && it.data != null){
            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(binding.uploadImage)

            // 이미지 URI를 문자열로 변환하여 저장
            imageUrl = uri.toString()
        }
    }

    override fun onSupportNavigateUp(): Boolean { // 액션바 뒤로가기
        onBackPressed()
        return true
    }


    private fun giftAdd() {
        var giftName = binding.textGiftName.text.toString()
        var effectiveDate = binding.textEffectiveDate.text.toString()
        var barcode = binding.textBarcode.text.toString()
        var usage = binding.textUsage.text.toString()

        if(giftName.isEmpty() || effectiveDate.isEmpty() || barcode.isEmpty() || usage.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
        } else {
            val collectGift = Collect_Gift(giftName, effectiveDate, barcode, usage, imageUrl, 0)
            collectGift.state = Utils.calState(collectGift)  // state 값에 calState의 결과를 할당
            val resultIntent = Intent()
            resultIntent.putExtra("gift", collectGift)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }
    }

    private fun giftAdd_Btn() {
        binding.addBtn.setOnClickListener {
            giftAdd()
        }
    }
}