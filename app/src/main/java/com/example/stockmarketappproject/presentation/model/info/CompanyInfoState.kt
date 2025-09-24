package com.example.stockmarketappproject.presentation.model.info

import com.example.stockmarketappproject.presentation.model.intraday.CompanyIntradayInfoPresentation

data class CompanyInfoState(
    val company: CompanyInfoPresentation? = null,
    val stockInfoList: List<CompanyIntradayInfoPresentation> = emptyList(),
    val isRefreshing: Boolean = false
) {
    companion object {
        fun createDefault(): CompanyInfoState = CompanyInfoState()
    }
}
