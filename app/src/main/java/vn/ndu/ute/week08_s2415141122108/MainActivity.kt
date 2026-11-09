package vn.ndu.ute.week08_s2415141122108

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat





class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
            handleCheckProcess()
        }

        binding.btnClear.setOnClickListener {
            handleClearProcess()
        }
    }

    private fun validateAndGetInputs(): InputData? {
        // Reset lỗi cũ
        binding.tvErrorCourse.visibility = View.GONE
        binding.tvErrorQt.visibility = View.GONE
        binding.tvErrorCk.visibility = View.GONE

        var isValid = true

        val rawName = binding.etCourseName.text.toString()
        val trimmedName = rawName.trim()
        if (trimmedName.length !in 1..40) {
            binding.tvErrorCourse.text = "Tên học phần phải từ 1 đến 40 ký tự."
            binding.tvErrorCourse.visibility = View.VISIBLE
            isValid = false
        }

        val rawQt = binding.etQt.text.toString().trim()
        val qt = rawQt.toDoubleOrNull()
        if (qt == null || qt !in 0.0..10.0) {
            binding.tvErrorQt.text = "Điểm QT phải là số thực từ 0.0 đến 10.0."
            binding.tvErrorQt.visibility = View.VISIBLE
            isValid = false
        }

        val rawCk = binding.etCk.text.toString().trim()
        val ck = rawCk.toDoubleOrNull()
        if (ck == null || ck !in 0.0..10.0) {
            binding.tvErrorCk.text = "Điểm CK phải là số thực từ 0.0 đến 10.0."
            binding.tvErrorCk.visibility = View.VISIBLE
            isValid = false
        }

        return if (isValid) {
            InputData(trimmedName, qt!!, ck!!)
        } else {
            null
        }
    }

    private fun calculateT(qt: Double, ck: Double): Double {
        return 0.4 * qt + 0.6 * ck
    }

    private fun evaluateStatus(t: Double, ck: Double): Boolean {
        val threshold = 5.0
        // Điều kiện: T >= 5.0 và CK >= 3.0 (dùng giá trị T chưa làm tròn để phân loại)
        return (t >= threshold) && (ck >= 3.0)
    }

    private fun handleCheckProcess() {
        val input = validateAndGetInputs() ?: run {
            binding.tvResult.text = "Vui lòng sửa các lỗi trên trước khi kiểm tra."
            return
        }

        val tScore = calculateT(input.qt, input.ck)
        val isPassed = evaluateStatus(tScore, input.ck)
        val statusText = if (isPassed) "Đạt" else "Chưa đạt"

        val resultString = String.format(
            Locale.getDefault(),
            "Học phần: %s\nQT: %.2f | CK: %.2f\nT: %.2f\nKết luận: %s",
            input.courseName, input.qt, input.ck, tScore, statusText
        )
        binding.tvResult.text = resultString
    }

    private fun handleClearProcess() {
        binding.etCourseName.text?.clear()
        binding.etQt.text?.clear()
        binding.etCk.text?.clear()

        binding.tvErrorCourse.visibility = View.GONE
        binding.tvErrorQt.visibility = View.GONE
        binding.tvErrorCk.visibility = View.GONE

        binding.tvResult.text = "Kết quả sẽ hiển thị ở đây"
    }

    data class InputData(val courseName: String, val qt: Double, val ck: Double)
}
