package com.example.admin.view

import com.example.admin.model.RealPathUtil
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.admin.R
import com.example.admin.adapter.AdapterRecyclerFood
import com.example.admin.api.ApiService
import com.example.admin.databinding.ActivityAddFoodBinding
import com.example.admin.databinding.LayoutCustomDialogDetailFoodBinding
import com.example.admin.model.Food
import com.example.admin.model.IdFood
import com.example.admin.onClickInterface.OnClickFood
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception

class AddFoodActivity : AppCompatActivity() {

    private lateinit var h2o: ActivityAddFoodBinding
    private val MY_RESQUEST_CODE: Int = 10
    private var listFood = mutableListOf<Food>()
    private var listIdFood = mutableListOf<IdFood>()
    private var hashId = mutableListOf<Int>()
    private lateinit var nextIdFood: String
    private lateinit var dialog: AlertDialog
    private lateinit var diaLogBinding: LayoutCustomDialogDetailFoodBinding
    private lateinit var diaLogAddFood: LayoutCustomDialogDetailFoodBinding
    private val url = "http://172.16.0.2:3333//api/images/"
    private var status = false
    private var statusInsert = false
    private lateinit var mUri: Uri
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult?) {
                if (result != null) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        var data = result.data ?: return
                        var uri: Uri? = data.data
                        if (uri != null) {
                            mUri = uri
                        }
                        try {
                            var bitMap: Bitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, uri)
                            diaLogBinding.imgFood.setImageBitmap(bitMap)
                            diaLogAddFood.imgFood.setImageBitmap(bitMap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        h2o = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(h2o.root)
        setSupportActionBar(h2o.toolbarMenu)
        diaLogBinding = LayoutCustomDialogDetailFoodBinding
            .inflate(
                LayoutInflater.from(
                    this@AddFoodActivity
                )
            )
        diaLogAddFood = LayoutCustomDialogDetailFoodBinding.inflate(LayoutInflater.from(this@AddFoodActivity))

        getData()

        h2o.btnAddFood.setOnClickListener {
            showDiaLogAdd()
        }
    }

    private fun showDiaLogAdd() {
        var build = AlertDialog.Builder(this@AddFoodActivity)
        build.setView(diaLogAddFood.root)
        diaLogAddFood.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        diaLogAddFood.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        ApiService.create().getIdFood().enqueue(object : Callback<MutableList<IdFood>> {
            override fun onResponse(
                call: Call<MutableList<IdFood>>,
                response: Response<MutableList<IdFood>>
            ) {
                if (response.isSuccessful) {
                    listIdFood = response.body()!!
                    for (idFood in listIdFood) {
                        var count = idFood.getIdFood().substring(1).toInt()
                        hashId.add(count)
                    }
                    nextIdFood = "F" + (hashId.max() + 1).toString()
                    insertFood()
                }
            }

            override fun onFailure(call: Call<MutableList<IdFood>>, t: Throwable) {
                Toast.makeText(this@AddFoodActivity, "Không lấy được idFood", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        dialog = build.create()
        dialog.show()
    }

    private fun insertFood() {
        var check = false
        diaLogAddFood.txtIdFood.text = nextIdFood
        if (diaLogAddFood.radStatus.isChecked) {
            statusInsert = true
        }
        diaLogAddFood.imgFood.setOnClickListener {
            requetsPermissions()
            check = true
        }
        diaLogAddFood.btnUpdate.text = "Thêm"
        diaLogAddFood.btnUpdate.setOnClickListener {
            var img = listOf<String>()
            img = if (check) {
                var images = RealPathUtil().getRealPath(this, mUri).toString()
                var file = File(images)
                file.name.split(".")
            } else {
                listOf<String>("")
            }
            var nameFood = diaLogAddFood.edtNameFood.text.toString()
            var cost = diaLogAddFood.edtCost.text.toString().toDouble()
            var salePrice = diaLogAddFood.edtSalePrice.text.toString().toDouble()
            var detailFood = diaLogAddFood.edtDetail.text.toString()
            var intro = diaLogAddFood.edtIntro.text.toString()
            var food = Food(
                nextIdFood,
                nameFood,
                cost,
                salePrice,
                img[0],
                detailFood,
                intro,
                statusInsert
            )
            ApiService.create().insertFood(food).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddFoodActivity,
                            "Thêm thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@AddFoodActivity,
                        "Thêm thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }


    private fun getData() {
        ApiService.create().getAllFood().enqueue(object : Callback<MutableList<Food>> {
            override fun onResponse(
                call: Call<MutableList<Food>>,
                response: Response<MutableList<Food>>
            ) {
                if (response.isSuccessful) {
                    listFood = response.body()!!
                    for (i in listFood) {
                        Log.e("id", i.getIdFood())
                    }
                    setAdapterFood()
                }
            }

            override fun onFailure(call: Call<MutableList<Food>>, t: Throwable) {
                Toast.makeText(this@AddFoodActivity, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapterFood() {
        var adapterFood = AdapterRecyclerFood(listFood, object : OnClickFood {
            override fun onClick(pos: Int) {
                onClickFood(pos)
            }
        })
        h2o.recyclerFood.adapter = adapterFood
        h2o.recyclerFood.layoutManager = StaggeredGridLayoutManager(
            1,
            StaggeredGridLayoutManager.VERTICAL
        )
        h2o.recyclerFood.addItemDecoration(
            DividerItemDecoration(
                this@AddFoodActivity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun onClickFood(pos: Int) {
        val build = AlertDialog.Builder(this@AddFoodActivity)

        build.setView(diaLogBinding.root)
        diaLogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        diaLogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        if (listFood[pos].getImages().isNotEmpty()) {
            Glide.with(diaLogBinding.root).load(url + listFood[pos].getImages())
                .into(diaLogBinding.imgFood)
        } else {
            Glide.with(diaLogBinding.root).load(R.drawable.ic_launcher_background)
                .into(diaLogBinding.imgFood)
        }
        diaLogBinding.txtIdFood.text = listFood[pos].getIdFood()
        diaLogBinding.edtNameFood.setText(listFood[pos].getNameFood())
        diaLogBinding.edtCost.setText(listFood[pos].getCost().toString())
        diaLogBinding.edtSalePrice.setText(listFood[pos].getSalePrice().toString())
        diaLogBinding.edtIntro.setText(listFood[pos].getIntro())
        diaLogBinding.edtDetail.setText(listFood[pos].getDetailFood())
        diaLogBinding.radStatus.isChecked = listFood[pos].getStatus()
        var check = false
        diaLogBinding.imgFood.setOnClickListener {
            requetsPermissions()
            check = true
        }

        diaLogBinding.btnUpdate.setOnClickListener {
            if (diaLogBinding.radStatus.isChecked) {
                status = true
            }

            var idFood = diaLogBinding.txtIdFood.text.toString()
            var nameFood = diaLogBinding.edtNameFood.text.toString()
            var cost = diaLogBinding.edtCost.text.toString().toDouble()
            var salePrice = diaLogBinding.edtSalePrice.text.toString().toDouble()
            var detailFood = diaLogBinding.edtDetail.text.toString()
            var intro = diaLogBinding.edtIntro.text.toString()
            var images = if (check){
                RealPathUtil().getRealPath(this, mUri).toString()
            } else{
                listFood[pos].getImages()
            }
            var file = File(images)
            var img = file.name.split(".")
            var food = Food(idFood, nameFood, cost, salePrice, img[0], detailFood, intro, status)

            ApiService.create().updateFoodById(
                idFood, food
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddFoodActivity,
                            "Cập nhật thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@AddFoodActivity,
                        "Cập nhật thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("bug", t.message.toString())
                }

            })
        }

        dialog = build.create()
        dialog.show()
    }

    private fun requetsPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGalley()
            return
        }
        if (checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGalley()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_RESQUEST_CODE)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_RESQUEST_CODE) {
            if (permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults.isNotEmpty() && grantResults[permissions.indexOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )] == PackageManager.PERMISSION_GRANTED
            ) {
                openGalley()
            } else {
                Toast.makeText(this, "Quyền truy cập bộ nhớ ngoài bị từ chối", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openGalley() {
        val intentGalley = Intent()
        intentGalley.type = "image/*"
        intentGalley.action = Intent.ACTION_GET_CONTENT
        activityResult.launch(Intent.createChooser(intentGalley, "Chọn ảnh"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnuBack) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}