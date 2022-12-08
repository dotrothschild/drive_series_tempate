package com.dotrothschild.drive

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LendBookManagerTest() {
    @Mock
    lateinit var mockBookService : BookService
    lateinit var lendBookManager: LendBookManager
    @Before
    fun doSetup() {
        mockBookService = Mockito.mock(BookService::class.java)
        lendBookManager = LendBookManager(mockBookService)
    }
    @Test
    fun whenBookisAvailableThenLendMethodIsCalled() {
        Mockito.`when`(mockBookService.inStock(101)).thenReturn(true)
        lendBookManager.checkout(101, 1)
    }

    @Test(expected = IllegalStateException::class)
    fun whenBookisNotAvailable() {
        lendBookManager.checkout(100, 1)
    }
}