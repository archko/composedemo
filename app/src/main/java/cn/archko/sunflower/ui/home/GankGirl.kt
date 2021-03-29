package cn.archko.sunflower.ui.home

import GankBean
import GankResponse
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.archko.sunflower.ui.components.JetsnackSurface
import cn.archko.sunflower.viewmodel.VideoViewModel
import com.example.jetsnack.ui.components.JetsnackDivider
import com.example.jetsnack.ui.home.DestinationBar
import com.example.jetsnack.ui.theme.JetsnackTheme
import com.example.jetsnack.ui.theme.Neutral8
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun GankGirl(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    //这里会导致每次都加载，只要重新组合。所以加载更多是正常，但会重复加载第一页
    val viewModel: VideoViewModel = viewModel()
    if (viewModel.gankResponse.value.data.isEmpty()) {
        viewModel.loadGankGirls(1)
    }
    val gankResponse by viewModel.gankResponse.collectAsState()
    val loadMore: (Int) -> Unit = { index: Int ->
        Log.d("loadMore", "$index")
        viewModel.loadMoreGankGirls()
    }
    val refresh: () -> Unit = { ->
        viewModel.loadGankGirls(1)
    }
    Log.d("repo", "GankGirl")
    GankGirl(
        gankResponse = gankResponse,
        onClick = onClick,
        refresh = refresh,
        loadMore = loadMore,
        modifier = modifier
    )
}

@Composable
private fun GankGirl(
    gankResponse: GankResponse<MutableList<GankBean>>,
    onClick: (String) -> Unit,
    loadMore: (Int) -> Unit,
    refresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (gankResponse.data.isNotEmpty()) {
        JetsnackSurface(modifier = modifier.fillMaxSize()) {
            Box {
                ItemList(gankResponse, onClick, loadMore)
                DestinationBar()
            }
        }
    } else {
        JetsnackSurface(modifier = modifier.fillMaxSize()) {
            Box(modifier = modifier.fillMaxSize()) {
                Button(
                    onClick = refresh,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        //.size(36.dp)
                        .background(
                            color = Neutral8.copy(alpha = 0.32f),
                            shape = CircleShape
                        )
                        .align(Alignment.Center)
                ) {
                    /*Icon(
                        imageVector = Icons.Outlined.Refresh,
                        tint = JetsnackTheme.colors.iconInteractive,
                        contentDescription = stringResource(R.string.label_back)
                    )*/
                    Text(
                        text = "Refresh",
                        style = MaterialTheme.typography.h5,
                        color = JetsnackTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemList(
    gankResponse: GankResponse<MutableList<GankBean>>,
    onClick: (String) -> Unit,
    loadMore: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    val size = gankResponse.data.size
    LazyColumn(modifier) {
        item {
            Spacer(Modifier.statusBarsHeight(additional = 56.dp))
        }
        itemsIndexed(gankResponse.data) { index, gankBean ->
            if (index > 0) {
                JetsnackDivider(thickness = 1.dp)
            }
            GankItem(
                gankBean = gankBean,
                onClick = onClick,
                index = index,
                scroll = scroll
            )
            //Log.d("ItemList", "$index")
            if (index == size - 1) {
                Text(
                    text = "Loading",
                    style = MaterialTheme.typography.h6,
                    color = JetsnackTheme.colors.textInteractive,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                LaunchedEffect(index) {
                    loadMore(index)
                }
            }
        }
    }
}

