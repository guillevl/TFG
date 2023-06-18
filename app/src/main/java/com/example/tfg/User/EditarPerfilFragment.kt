package com.example.tfg.User

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.tfg.MainActivity
import com.example.tfg.R
import com.example.tfg.RegistroFragment
import com.example.tfg.api.ApiRest
import com.example.tfg.api.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class EditarPerfilFragment : Fragment() {
    lateinit var user: UserData
    lateinit var imgProfile: ImageView
    var imgURLFirebase: String = ""
    lateinit var imgProfilePoster: ImageView
    var imgURLFirebasePoster: String = ""
    //PARA ACCEDER A GALERIA Y CAMARA
    private val REQUEST_CODE_PERMISSIONS = 1
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )
    private var latestTmpUri: Uri? = null
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            if (ok) {
                latestTmpUri?.let { uri ->
                    imgProfile.setImageURI(uri)
                    uploadIMG(uri)
                }
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = false
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)?.isVisible = false
        val mainActivity = activity as MainActivity
        mainActivity.setStatusBarColor("#000000")
        //coger dato de usuario loggeado
        val sharedPreferencesGet =
            requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val getID = sharedPreferencesGet.getInt("userID", 0)
        ApiRest.initService()
        getUser(getID.toString(),view)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Editar usuario")
            .setMessage("¿Desea guardar los cambios de la cuenta?")
            .setPositiveButton("Sí") { dialog, which ->
                user.name = view?.findViewById<TextView>(R.id.etEditNombre)?.text.toString()
                user.username = view?.findViewById<TextView>(R.id.etEditarUsername)?.text.toString()
                user.apellido = view?.findViewById<TextView>(R.id.etEditApellido)?.text.toString()
                if (view?.findViewById<RadioButton>(R.id.ManoDerecha)?.isChecked == true){
                    user.mano_dominante = "Diestro"
                }else if (view?.findViewById<RadioButton>(R.id.ManoIzquierda)?.isChecked == true){
                    user.mano_dominante = "Zurdo"
                }else{
                    user.mano_dominante = "Ambidiestro"
                }
                if (imgURLFirebase != ""){
                    user.foto_perfil = imgURLFirebase
                }
                updateUser(user.id.toString(), user)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, PerfilFragment())?.addToBackStack(null)?.commit()
                Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()

        view.findViewById<Button>(R.id.btnGuardarDatosUser).setOnClickListener {
            alerta.show()
        }
        imgProfile = view.findViewById(R.id.imgPerfilEP)
        imgProfilePoster = view.findViewById(R.id.imgPoster)
        val builder2 = AlertDialog.Builder(context)
        builder2.setTitle("Subir imagen")
            .setMessage("Selecciona origen de la imagen")
            .setPositiveButton("Camara") { dialog, which ->
                checkPermissions()
                startCamera()
            }
            .setNegativeButton("Galeria") { dialog, which ->
                checkPermissions()
                startGallery()
            }
        val alerta2 = builder2.create()

        view.findViewById<ImageView>(R.id.btnFotoPerfil).setOnClickListener {
            alerta2.show()
        }
    }

    /**override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.isVisible = true
    }**/

    private fun getUser(id: String,view: View) {
        val call = ApiRest.service.getUserById(id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i("EditProfileFragment", body.toString())
                    user = body
                    view?.findViewById<TextView>(R.id.etEditNombre)?.text = user.name
                    view?.findViewById<TextView>(R.id.etEditarUsername)?.text = user.username
                    view?.findViewById<TextView>(R.id.etEditApellido)?.text = user.apellido
                    if (user.mano_dominante == "Diestro"){
                        view?.findViewById<RadioButton>(R.id.ManoDerecha)?.isChecked = true
                    }else if (user.mano_dominante == "Zurdo"){
                        view?.findViewById<RadioButton>(R.id.ManoIzquierda)?.isChecked = true
                    }else if (user.mano_dominante == "Ambidiestro"){
                        view?.findViewById<RadioButton>(R.id.Ambidiestro)?.isChecked = true
                    }
                    Glide.with(view)
                        .load(user.foto_perfil)
                        .into(imgProfile)
                    Glide.with(view)
                        .load(user.foto_poster)
                        .into(imgProfilePoster)

                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    private fun updateUser(id: String, updatedUser: UserData) {
        val call = ApiRest.service.updateUser(updatedUser, id)
        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // procesa la respuesta aquí
                } else {
                    Log.e("EditProfileFragment", response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditProfileFragment", "Error: ${t.message}")
            }
        })
    }
    //verificamos si los permisos se han otorgado correctamente
    fun permissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    //verifica los premisos llamando al de arriba y sino los tiene se solicitan
    private fun checkPermissions() {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }
    //coge la imagen de la galeria
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imgProfile.setImageURI(uri)
        uploadIMG(uri)
    }
    //carga una imagen a el almacenamiento de firebase
    private fun uploadIMG(file: Uri?) {
        file?.let { //Comprueba de forma boleana si es nulo
            //crea una referencia a la ubicación en Firebase Storage donde se almacenará la imagen
            val extension = getFileExtension(file)
            val imageRef =
                FirebaseStorage.getInstance().reference.child("notes/images/${UUID.randomUUID()}.$extension")
            val riversRef = imageRef.child("images/${file.lastPathSegment}")
            //carga con la referencia y se manejan los eventos de éxito y falla de la tarea
            val uploadTask = riversRef.putFile(file)

            uploadTask.addOnFailureListener {
                Log.e("uploadFile", it.toString())
            }.addOnSuccessListener { taskSnapshot ->
                getUrl(taskSnapshot)
            }

        }
    }
    //obtener la URL de descarga de la imagen cargada en Firebase Storage
    private fun getUrl(taskSnapshot: UploadTask.TaskSnapshot?) {
        taskSnapshot?.storage?.downloadUrl?.addOnSuccessListener {
            imgURLFirebase = it.toString()
        }?.addOnFailureListener {
            Log.e("getUrl", it.toString())
        }
    }
    //obtener la extensión del archivo a partir de una URI
    fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(uri)) ?: "png"
    }
    //obtener la URI de un archivo temporal en la caché de la aplicación para la camara
    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(
            requireContext().applicationContext, "${requireContext().packageName}.provider", tmpFile
        )
    }
    fun startCamera() {
        if (permissionsGranted()) {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        } else {
            Log.e(
                "startCamera", "Error while accesing the camera, Check the required permissions"
            )
        }
    }
    private fun startGallery() {
        getContent.launch("image/*")
    }
}