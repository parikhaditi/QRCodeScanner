package itu.demo.qrcodedemo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View.*
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import kotlinx.android.synthetic.main.activity_qr_code.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)


        btn_generate_qrcode.setOnClickListener() {
            if(!TextUtils.isEmpty(et_text.text) ) {
                img_qr_code.setImageBitmap(generateQRCode(et_text.text.toString()))
            }
            else
            {
                Toast.makeText(baseContext,"Please provide Text",Toast.LENGTH_SHORT).show()
            }
        }

        btn_scan_qrcode.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setRequestCode(REQUEST_CODE)
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

    }

    /* Process the result from the zxing QR code scan */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if(result != null) {

            if(result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
            }
            else
            {   /* Update the textview with the scanned URL result */

                tv_scanned_qr_code.setText(result.getContents())
                Toast.makeText(this, "Content: ${result.getContents()}",Toast.LENGTH_LONG ).show()
            }

        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d("TAG", "generateQRCode: ${e.message}")
        }
        return bitmap
    }

}


