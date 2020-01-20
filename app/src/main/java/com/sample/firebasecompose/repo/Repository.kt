package com.sample.firebasecompose.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

fun getData(): LiveData<List<DocumentSnapshot>> {
    val mutableLiveData = MutableLiveData<List<DocumentSnapshot>>()
    FirebaseFirestore.getInstance().collection("services")
        .orderBy("type")
        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.apply {

                if (isEmpty.not()) {
                    mutableLiveData.postValue(querySnapshot.documents)
                }
            }
        }

    return mutableLiveData
}