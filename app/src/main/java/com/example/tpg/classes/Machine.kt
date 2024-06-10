package com.example.tpg.classes

class Machine(private var id: String) {
    private var name: String = "";

    fun setId(id: String) {
        this.id = id;
    }

    fun getId(): String {
        return this.id;
    }

    fun setName(name: String) {
        this.name = name;
    }

    fun getName(): String {
        return this.name;
    }

}