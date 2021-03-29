package com.iftekhar.ahmed.comicviewer.util

import android.view.View
import com.iftekhar.ahmed.comicviewer.databinding.LayoutEmptyStateBinding

interface EmptyStatePresenter {
    fun getEmptyStateLayout(): LayoutEmptyStateBinding
    fun showEmptyStat(msg: String = "No Data Available") {
        val emptyStateLayout = getEmptyStateLayout()
        emptyStateLayout.root.visibility = View.VISIBLE
        emptyStateLayout.empty.visibility = View.VISIBLE
        emptyStateLayout.textEmptyState.text = msg
        emptyStateLayout.progress.visibility = View.GONE
        emptyStateLayout.error.visibility = View.GONE
    }

    fun showNetworkError(onRetry: () -> Unit) {
        val emptyStateLayout = getEmptyStateLayout()
        emptyStateLayout.root.visibility = View.VISIBLE
        emptyStateLayout.empty.visibility = View.GONE
        emptyStateLayout.progress.visibility = View.GONE
        emptyStateLayout.error.visibility = View.VISIBLE
        emptyStateLayout.buttonRetryEmptyState.setOnClickListener { onRetry() }
    }

    fun showProgress() {
        val emptyStateLayout = getEmptyStateLayout()
        emptyStateLayout.root.visibility = View.VISIBLE
        emptyStateLayout.empty.visibility = View.GONE
        emptyStateLayout.progress.visibility = View.VISIBLE
        emptyStateLayout.error.visibility = View.GONE
    }

    fun showContent() {
        val emptyStateLayout = getEmptyStateLayout()
        emptyStateLayout.root.visibility = View.GONE
    }
}
