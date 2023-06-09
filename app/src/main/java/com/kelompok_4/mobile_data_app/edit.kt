package com.kelompok_4.mobile_data_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.kelompok_4.mobile_data_app.databinding.ActivityEditBinding
import org.json.JSONObject

class edit : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    var ab = "hadir"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cari_data(intent.getStringExtra("id").toString())
        binding.btSimpan.setOnClickListener {
            val tanggal = binding.etTanggal.text.toString()
            val nama = binding.etNama.text.toString()
            val nim = binding.etNpm.text.toString()
            val matakuliah = binding.etMatakuliah.text.toString()
            val dosen = binding.etDosen.text.toString()
            val jurusan = binding.etJurusan.text.toString()
            if (tanggal.isEmpty()) {
                binding.etTanggal.error = "Kosong"
                binding.etTanggal.requestFocus()
            } else if (nama.isEmpty()) {
                binding.etNama.error = "Kosong"
                binding.etNama.requestFocus()
            } else if (nim.isEmpty()) {
                binding.etNpm.error = "Kosong"
                binding.etNpm.requestFocus()
            }else if (matakuliah.isEmpty()) {
                binding.etMatakuliah.error = "Kosong"
                binding.etMatakuliah.requestFocus()
            }else if (dosen.isEmpty()){
                binding.etDosen.error = "Kosong"
                binding.etDosen.requestFocus()
            }else if (jurusan.isEmpty()){
                binding.etJurusan.error = "Kosong"
                binding.etJurusan.requestFocus()
            }else{
                AndroidNetworking.post("http://192.168.100.13/data_api/edit.php")
                    .addBodyParameter("id", intent.getStringExtra("id"))
                    .addBodyParameter("tanggal", tanggal)
                    .addBodyParameter("nama", nama)
                    .addBodyParameter("nim", nim)
                    .addBodyParameter("matakuliah", matakuliah)
                    .addBodyParameter("dosen", dosen)
                    .addBodyParameter("jurusan", jurusan)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            if(response.getInt("success")==1){
                                Toast.makeText(this@edit,response.getString("pesan"), Toast.LENGTH_SHORT).show()
                                finish()
                            }else{
                                Toast.makeText(this@edit,response.getString("pesan"), Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onError(error: ANError) {
                            Toast.makeText(this@edit, error.toString(), Toast.LENGTH_SHORT).show()
                            // handle error
                        }
                    })
            }
        }
        binding.rgAb.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.abP.id){
                ab = "hadir"
            }else if (checkedId == binding.abA.id){
                ab= "sakit"
            }else{
                ab = "alfa"
            }
        }
    }
    fun cari_data (id : String){
        AndroidNetworking.get("http://192.168.100.13/data_api/cari.php")
            .addQueryParameter("id", id)
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (response.getInt("success") == 1) {
                        val jsonObject = response.optJSONObject("data")
                        binding.etTanggal.setText(jsonObject.getString("tanggal"))
                        binding.etNama.setText(jsonObject.getString("nama"))
                        binding.etNpm.setText(jsonObject.getString("nim"))
                        binding.etMatakuliah.setText(jsonObject.getString("matakuliah"))
                        binding.etDosen.setText(jsonObject.getString("dosen"))
                        if (jsonObject.getString("absensi") == "present"){
                            binding.abP.isChecked = true
                            ab = "hadir"
                        }else if (jsonObject.getString("absensi") == "absent") {
                            binding.abA.isChecked = true
                            ab = "sakit"
                        }else{
                            binding.abL.isChecked = true
                            ab = "alfa"
                        }
                        binding.etJurusan.setText(jsonObject.getString("jurusan"))
                    } else {

                        Toast.makeText(this@edit, response.getString("pesan"), Toast.LENGTH_LONG).show()
                    }

                }
                override fun onError(error: ANError){
                    Toast.makeText(this@edit, error.toString(), Toast.LENGTH_SHORT).show()
                }

            })
    }

}

