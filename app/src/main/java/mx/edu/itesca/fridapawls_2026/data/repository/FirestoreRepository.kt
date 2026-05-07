package mx.edu.itesca.fridapawls_2026.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost

object FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getPostsPaginated(
        lastDoc: DocumentSnapshot?,
        limit: Long = 10,
        onResult: (List<MascotaPost>, DocumentSnapshot?) -> Unit
    ) {

        var query = db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit)

        if (lastDoc != null) {
            query = query.startAfter(lastDoc)
        }

        query.get()
            .addOnSuccessListener { result ->

                val posts = result.documents.map { doc ->

                    MascotaPost(
                        id = doc.id,
                        nombreMascota = doc.getString("nombreMascota") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        ubicacion = doc.getString("ubicacion") ?: "",
                        estado = doc.getString("estado") ?: "",
                        tipoAnimal = doc.getString("tipoAnimal") ?: "",
                        imagenes = doc.get("imagenes") as? List<String> ?: emptyList(),
                        uid = doc.getString("uid") ?: "",
                        timestamp = doc.getTimestamp("timestamp")
                    )
                }

                onResult(posts, result.documents.lastOrNull())
            }
            .addOnFailureListener {
                onResult(emptyList(), null)
            }
    }
}