package com.example.taller_1

class DestinoTuristico {

    private var nombreDestino: String? = null
    private var paisDestino: String? = null
    private var categoriaDestino: String? = null
    private var planDestino: String? = null
    private var precioDestino: String? = null

    constructor(
        nombreDestino: String?,
        paisDestino: String?,
        categoriaDestino: String?,
        planDestino: String?,
        precioDestino: String?
    ) {
        this.nombreDestino = nombreDestino
        this.paisDestino = paisDestino
        this.categoriaDestino = categoriaDestino
        this.planDestino = planDestino
        this.precioDestino = precioDestino
    }


    fun getNombreDestino(): String? {
        return nombreDestino
    }

    fun getPaisDestino(): String? {
        return paisDestino
    }

    fun getCategoriaDestino(): String? {
        return categoriaDestino
    }

    fun getPlanDestino(): String? {
        return planDestino
    }

    fun getPrecioDestino(): String? {
        return precioDestino
    }

}