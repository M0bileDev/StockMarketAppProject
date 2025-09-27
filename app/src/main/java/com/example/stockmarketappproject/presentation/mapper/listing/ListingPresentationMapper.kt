package com.example.stockmarketappproject.presentation.mapper.listing

import com.example.stockmarketappproject.data.model.listing.CompanyListingData
import com.example.stockmarketappproject.presentation.mapper.PresentationMapper
import com.example.stockmarketappproject.presentation.model.listing.CompanyListingPresentation

interface ListingPresentationMapper :
    PresentationMapper<CompanyListingData, CompanyListingPresentation>