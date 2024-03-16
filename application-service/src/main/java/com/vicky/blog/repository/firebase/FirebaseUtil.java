package com.vicky.blog.repository.firebase;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FirebaseUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(FirebaseUtil.class);

    static long getUniqueLong() {
        long randomPositiveLong = Math.abs(
        UUID.randomUUID().getMostSignificantBits()
        );
        return randomPositiveLong;
    }

    static long getNextOrderedId(String collection) {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collectionRef = firestore.collection(collection);

        Query query = collectionRef.orderBy("id", Query.Direction.DESCENDING).limit(1);
        try {
            QuerySnapshot querySnapshot = query.get().get();
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot lastDocument = querySnapshot.getDocuments().get(0);
                return (long) lastDocument.getData().get("id") + 1;
            }
            else {
                // No documents yet;
                return 1L;
            }
        } 
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return -1L;
    }
}
