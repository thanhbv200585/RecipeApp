package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.adapter.MainCategoryAdapter
import com.example.recipeapp.adapter.SubCategoryAdapter
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.databinding.ActivityHomeBinding
import com.example.recipeapp.entities.CategoryItems
import com.example.recipeapp.entities.MealsItems
import kotlinx.coroutines.launch

class   HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    var arrMainCategory = ArrayList<CategoryItems>()
    var arrSubCategory = ArrayList<MealsItems>()

    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFromDb()

        mainCategoryAdapter.setClickListener(onCLicked)
        subCategoryAdapter.setClickListener(onCLickedSubItem)
    }

    private val onCLicked  = object : MainCategoryAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            getMealDataFromDb(categoryName)
        }
    }

    private val onCLickedSubItem  = object : SubCategoryAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@HomeActivity,DetailActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }

    private fun getDataFromDb(){
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()
                arrMainCategory = cat as ArrayList<CategoryItems>
                arrMainCategory.reverse()

                getMealDataFromDb(arrMainCategory[0].strcategory)
                mainCategoryAdapter.setData(arrMainCategory)
                binding.rvMainCategory.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                binding.rvMainCategory.adapter = mainCategoryAdapter
            }


        }
    }

    private fun getMealDataFromDb(categoryName:String){
        binding.tvCategory.text = "$categoryName Category"
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealList(categoryName)
                arrSubCategory = cat as ArrayList<MealsItems>
                subCategoryAdapter.setData(arrSubCategory)
                binding.rvSubCategory.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                binding.rvSubCategory.adapter = subCategoryAdapter
            }
        }
    }
}