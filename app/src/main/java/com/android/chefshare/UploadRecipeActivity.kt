package com.android.chefshare

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.chefshare.databinding.ActivityUploadRecipeBinding
import java.io.InputStream

class UploadRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadRecipeBinding

    private var selectedMainImageUri: Uri? = null
    private val stepImageUris = mutableListOf<Uri?>()
    private var currentStepIndex = -1

    private lateinit var mainImagePicker: ActivityResultLauncher<Intent>
    private lateinit var stepImagePicker: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupImagePickers()
        setupEventListeners()

        addIngredientField() // Thêm 1 ô nguyên liệu mặc định
        addStepView() // Thêm bước đầu tiên
    }

    private fun setupEventListeners() {
        binding.imageContainer.setOnClickListener { pickMainImage() }
        binding.btnAddIngredient.setOnClickListener { addIngredientField() }
        binding.btnAddStep.setOnClickListener { addStepView() }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnSaveDraft.setOnClickListener {
            Toast.makeText(this, "Lưu nháp thành công", Toast.LENGTH_SHORT).show()
        }
        binding.btnPublish.setOnClickListener { uploadRecipe() }
    }

    private fun setupImagePickers() {
        mainImagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let { uri ->
                selectedMainImageUri = uri
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.imgRecipe.setImageBitmap(bitmap)
                binding.imgRecipe.visibility = ImageView.VISIBLE
            }
        }

        stepImagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let { uri ->
                if (currentStepIndex in stepImageUris.indices) {
                    stepImageUris[currentStepIndex] = uri

                    val frameIndex = currentStepIndex * 2 + 1
                    val frameLayout = binding.stepsContainer.getChildAt(frameIndex) as FrameLayout
                    frameLayout.removeAllViews()

                    val imageView = ImageView(this).apply {
                        val inputStream: InputStream? = contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        setImageBitmap(bitmap)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                    }

                    frameLayout.addView(imageView)
                }
            }
        }
    }

    private fun pickMainImage() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        mainImagePicker.launch(intent)
    }

    private fun pickStepImage(index: Int) {
        currentStepIndex = index
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        stepImagePicker.launch(intent)
    }

    private fun addIngredientField() {
        val editText = EditText(this).apply {
            hint = "Ví dụ: 2 quả trứng gà"
            setBackgroundResource(R.drawable.bg_rounded_border_grey)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 0)
            }
        }

        binding.ingredientContainer.addView(editText)
    }

    private fun addStepView() {
        val stepIndex = stepImageUris.size + 1

        val stepLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 0) }
        }

        val stepLabel = TextView(this).apply {
            text = stepIndex.toString()
            setTextColor(resources.getColor(android.R.color.white, null))
            setBackgroundResource(R.drawable.bg_circle_red)
            textSize = 16f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(90, 90).apply {
                setMargins(0, 0, 16, 0)
            }
        }

        val stepEditText = EditText(this).apply {
            hint = "Ví dụ: Trộn đều các nguyên liệu"
            setBackgroundResource(R.drawable.bg_rounded_border_grey)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                weight = 1f
            }
        }

        stepLayout.addView(stepLabel)
        stepLayout.addView(stepEditText)

        val stepImageFrame = FrameLayout(this).apply {
            setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 300
            ).apply { setMargins(0, 8, 0, 16) }
        }

        val chooseImageLayout = LinearLayout(this@UploadRecipeActivity).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#EEEEEE")) // nền xám nhạt

            val icon = ImageView(this@UploadRecipeActivity).apply {
                setImageResource(R.drawable.ic_add_a_photo)
                setColorFilter(Color.parseColor("#555555")) // icon xám đậm hơn
                layoutParams = LinearLayout.LayoutParams(64, 64) // icon size
            }

            val label = TextView(this@UploadRecipeActivity).apply {
                text = "Chọn ảnh"
                setTextColor(Color.parseColor("#555555")) // chữ xám đậm hơn
                textSize = 14f
                setPadding(0, 8, 0, 0)
                gravity = Gravity.CENTER
            }

            addView(icon)
            addView(label)
        }


        stepImageFrame.addView(chooseImageLayout)

        val stepIndexToPick = stepImageUris.size
        stepImageFrame.setOnClickListener {
            pickStepImage(stepIndexToPick)
        }

        stepImageUris.add(null)

        binding.stepsContainer.addView(stepLayout)
        binding.stepsContainer.addView(stepImageFrame)
    }

    private fun uploadRecipe() {
        val name = binding.etRecipeName.text.toString().trim()
        val desc = binding.etDescription.text.toString().trim()
        val portion = binding.etPortion.text.toString().trim()
        val time = binding.etCookingTime.text.toString().trim()

        val ingredients = mutableListOf<String>()
        for (i in 0 until binding.ingredientContainer.childCount) {
            val view = binding.ingredientContainer.getChildAt(i)
            if (view is EditText) {
                val text = view.text.toString().trim()
                if (text.isNotEmpty()) ingredients.add(text)
            }
        }

        val steps = mutableListOf<String>()
        for (i in 0 until binding.stepsContainer.childCount step 2) {
            val stepLayout = binding.stepsContainer.getChildAt(i) as LinearLayout
            val stepEdit = stepLayout.getChildAt(1) as EditText
            val text = stepEdit.text.toString().trim()
            if (text.isNotEmpty()) steps.add(text)
        }

        Toast.makeText(
            this,
            "✅ Tên: $name\n📄 Mô tả: $desc\n🥗 ${ingredients.size} nguyên liệu\n👨‍🍳 ${steps.size} bước",
            Toast.LENGTH_LONG
        ).show()

        // TODO: Gửi dữ liệu lên server
    }
}
