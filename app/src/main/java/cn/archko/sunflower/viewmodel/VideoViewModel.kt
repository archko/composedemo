package cn.archko.sunflower.viewmodel

import GankBean
import GankResponse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.archko.sunflower.model.ACategory
import cn.archko.sunflower.repo.VideoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 */
class VideoViewModel : ViewModel() {

    private val _categories = MutableStateFlow(CategoryReponse<ACategory>())

    val categoryResponse: StateFlow<CategoryReponse<ACategory>>
        get() = _categories

    private val _gankResponse =
        MutableStateFlow(GankResponse<MutableList<GankBean>>(0, 0, 0, 0, "", "", mutableListOf()))

    val gankResponse: StateFlow<GankResponse<MutableList<GankBean>>>
        get() = _gankResponse

    init {
        //loadCategories(0)
    }

    fun loadCategories(pageIndex: Int) {
        Log.d("loadCategories", "$pageIndex")
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                val list: List<ACategory>? = VideoRepo.getACategory()
                if (null != list) {
                    CategoryReponse(
                        pageIndex,
                        categories = list
                    )
                } else {
                    null
                }
            }
            if (null != result) {
                _categories.value = result
            }
        }
    }

    var _pageIndex: Int = 1;

    fun loadMoreGankGirls() {
        loadGankGirls(_pageIndex + 1)
    }

    fun loadGankGirls(pageIndex: Int) {
        Log.d("loadGankGirls", "$pageIndex")
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                val response: GankResponse<MutableList<GankBean>>? =
                    VideoRepo.getGankGirls(pageIndex)
                var dataList: MutableList<GankBean> = _gankResponse.value.data
                if (response != null) {
                    dataList.addAll(response.data)
                    response.data = dataList
                }
                response
            }
            if (result != null) {
                _gankResponse.value = result
                if (result.data.size > 0) {
                    _pageIndex++
                }
            }
        }
    }
}

data class CategoryReponse<T>(
    val page: Int = 0,
    val categories: List<T> = emptyList(),
)
