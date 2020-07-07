package com.game.hit_it

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.game.hit_it.AppController.Companion.uid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    internal lateinit var register_btn:Button
    var selectedPhotoUri: Uri? = null
    internal var count:Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_btn= findViewById(R.id.register_btn)
        register_btn.setOnClickListener {
            if (count==0){
                Toast.makeText(this,"Select Username/Email Address/Photo",Toast.LENGTH_LONG).show()

            }else if (count==1){
                performRegister()
                Toast.makeText(this,"Loading....",Toast.LENGTH_LONG).show()

            }
        }



        already_have_account_text_view.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
        selectphoto_button_register.setOnClickListener {
            Log.d("Click_on_imageview", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }


    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/registered_user_images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.e("TAG", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("TAG", "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Failed to upload image to storage: ${it.message}")
                Toast.makeText(this, "No Photo Selected", Toast.LENGTH_SHORT)
                    .show()

            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {

        uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        val ref = FirebaseDatabase.getInstance().getReference("/users/${uid}")

        val user = User(
            uid,
            username_register.text.toString(),
            profileImageUrl

        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.e("Database_created", "Finally we saved the user to Firebase Database")

                Toast.makeText(this, "SuccessFully Created", Toast.LENGTH_SHORT)
                    .show()
//                val dialog = ProgressDialog(this)
//                dialog.setCancelable(false)
//                dialog.setMessage("Please Wait")
//                dialog.show()
//                dialog.dismiss()
                val intent = Intent(this, login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


            }
            .addOnFailureListener {
                Log.e("Failed_Database", "Failed to set value to database: ${it.message}")

            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.e("From Storage", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)

            selectphoto_button_register.alpha = 0f
            count = count+1

//      val bitmapDrawable = BitmapDrawable(bitmap)
//      selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val email = email_register.text.toString()
        val password = password_register.text.toString()


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password: $password")


        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.e("Success", "Successfully created user with uid: ${it.result?.user?.uid}")
                if (selectedPhotoUri != null){
                    register_btn.setOnClickListener {
                        if (selectedPhotoUri==null){
                            Toast.makeText(this,"Select Username/Email Address/Photo",Toast.LENGTH_LONG).show()
                        }else
                            performRegister()
                    }
                    uploadImageToFirebaseStorage()
                }

                else
                    Toast.makeText(this,"Please Select Image",Toast.LENGTH_LONG).show()

            }


            .addOnFailureListener {
                Log.e("Failed", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }

    }
}