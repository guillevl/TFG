package com.example.tfg.Admin

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.tfg.*
import com.example.tfg.User.PerfilFragment
import com.example.tfg.api.ApiRest
import com.example.tfg.api.EventsResponse
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class CrearEventoFragment : Fragment() {
    val TAG = "MainActivity"
    lateinit var imgEvento: ImageView
    var imgURLFirebase: String = ""

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
                    imgEvento.setImageURI(uri)
                    uploadIMG(uri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_evento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()
        view.findViewById<EditText>(R.id.etFechaEvento).setOnClickListener() {
            showDatePickerDialog()
        }
        view.findViewById<EditText>(R.id.etHoraInicioEvento).setOnClickListener() {
            showTimePickerDialog(1)
        }
        view.findViewById<EditText>(R.id.etHoraFinEvento).setOnClickListener() {
            showTimePickerDialog(2)
        }
        imgEvento = view.findViewById(R.id.ivFotoEventoCrear)
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

        view.findViewById<ImageView>(R.id.btnFotoEventoCrear).setOnClickListener {
            alerta2.show()
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Crear Evento")
            .setMessage("¿Desea crear el evento?")
            .setPositiveButton("Sí") { dialog, which ->
                var titulo_evento = view?.findViewById<TextView>(R.id.etTituloEvento)?.text.toString()
                var nivel = view?.findViewById<TextView>(R.id.etNivelEvento)?.text.toString()
                var sexo:String = ""
                var hora_inicio = view?.findViewById<TextView>(R.id.etHoraInicioEvento)?.text.toString()
                var hora_fin = view?.findViewById<TextView>(R.id.etHoraFinEvento)?.text.toString()
                var fecha_evento = view?.findViewById<TextView>(R.id.etFechaEvento)?.text.toString()
                var foto_evento: String = ""
                if (view?.findViewById<RadioButton>(R.id.mixto)?.isChecked == true) {
                    sexo = "Mixto"
                } else if (view?.findViewById<RadioButton>(R.id.femenino)?.isChecked == true) {
                    sexo = "Femenino"
                } else {
                    sexo = "Masculino"
                }
                if (imgURLFirebase != "") {
                    foto_evento = imgURLFirebase
                }

                crearEvento(foto_evento, titulo_evento, nivel , sexo, hora_inicio, hora_fin, fecha_evento)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, CrearEventoFragment())?.addToBackStack(null)?.commit()
                Toast.makeText(context, "Evento creado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, which ->
                // Acciones a realizar si el usuario presiona el botón "No"
            }
        val alerta = builder.create()
        view?.findViewById<Button>(R.id.btnCrearEvento)?.setOnClickListener {
            alerta.show()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker =
            DatePickerEventoFragment { day, month, year -> onDateSelected(day, month, year) }
        getActivity()?.let { datePicker.show(it.getSupportFragmentManager(), "datePicker") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        view?.findViewById<EditText>(R.id.etFechaEvento)
            ?.setText("$day/$month/$year")
    }

    private fun showTimePickerDialog(i: Int) {
        if (i == 1) {
            val timePicker = TimePickerFragment { onTimeSelectedInicio(it) }
            getActivity()?.let { timePicker.show(it.getSupportFragmentManager(), "datePicker") }
        } else {
            val timePicker2 = TimePickerFragment { onTimeSelectedFin(it) }
            getActivity()?.let { timePicker2.show(it.getSupportFragmentManager(), "datePicker") }
        }
    }

    private fun onTimeSelectedInicio(time: String) {
        view?.findViewById<EditText>(R.id.etHoraInicioEvento)
            ?.setText("$time")
    }

    private fun onTimeSelectedFin(time: String) {
        view?.findViewById<EditText>(R.id.etHoraFinEvento)
            ?.setText("$time")
    }

    private fun crearEvento(
        foto_evento: String,
        titulo_evento: String,
        nivel: String,
        sexo: String,
        hora_inicio: String,
        hora_fin: String,
        fecha_evento: String
    ) {
        val data = EventsResponse(
            EventsResponse.Data.Attributes(
                fecha_evento, foto_evento, hora_fin, hora_inicio, false, nivel, sexo, titulo_evento
            )
        )
        val call = ApiRest.service.crearEvento(data)
        call.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                // maneja la respuesta exitosa aquí
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var registroResponse = response.body()
                    print(registroResponse)
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, MainAdminFragment())?.commit()
                    Toast.makeText(context, "Evento creado", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    //meter fotos
    fun permissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions() {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imgEvento.setImageURI(uri)
        uploadIMG(uri)
    }

    private fun uploadIMG(file: Uri?) {
        file?.let { //Comprueba de forma boleana si es nulo
            val extension = getFileExtension(file)
            val imageRef =
                FirebaseStorage.getInstance().reference.child("notes/images/${UUID.randomUUID()}.$extension")
            val riversRef = imageRef.child("images/${file.lastPathSegment}")
            val uploadTask = riversRef.putFile(file)

            uploadTask.addOnFailureListener {
                Log.e("uploadFile", it.toString())
            }.addOnSuccessListener { taskSnapshot ->
                getUrl(taskSnapshot)
            }

        }
    }

    private fun getUrl(taskSnapshot: UploadTask.TaskSnapshot?) {
        taskSnapshot?.storage?.downloadUrl?.addOnSuccessListener {
            imgURLFirebase = it.toString()
        }?.addOnFailureListener {
            Log.e("getUrl", it.toString())
        }
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver?.getType(uri)) ?: "png"
    }

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