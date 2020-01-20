package com.sample.firebasecompose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.sample.firebasecompose.repo.getData

class InfoViewModel: ViewModel() {

    fun retrieveDataFromFirestore(): LiveData<List<DocumentSnapshot>> {
        return getData()
    }
}