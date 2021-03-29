/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetsnack.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetsnack.model.Filter
import com.example.jetsnack.model.Snack
import com.example.jetsnack.model.SnackRepo
import com.example.jetsnack.ui.components.FeedItem
import com.example.jetsnack.ui.components.FilterBar
import com.example.jetsnack.ui.components.JetsnackDivider
import cn.archko.sunflower.ui.components.JetsnackSurface
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun FeedList(
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snacks = remember { SnackRepo.allSnacks() }
    val filters = remember { SnackRepo.getFilters() }
    Feed(
        snacks,
        filters,
        onSnackClick,
        modifier
    )
}

@Composable
private fun Feed(
    snackCollections: List<Snack>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    JetsnackSurface(modifier = modifier.fillMaxSize()) {
        Box {
            SnackCollectionList(snackCollections, filters, onSnackClick)
            DestinationBar()
        }
    }
}

@Composable
private fun SnackCollectionList(
    snackCollections: List<Snack>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    LazyColumn(modifier) {
        item {
            Spacer(Modifier.statusBarsHeight(additional = 56.dp))
            FilterBar(filters)
        }
        itemsIndexed(snackCollections) { index, snackCollection ->
            if (index > 0) {
                JetsnackDivider(thickness = 1.dp)
            }
            FeedItem(
                snack = snackCollection,
                onSnackClick = onSnackClick,
                index = index,
                scroll=scroll
            )
        }
    }
}

