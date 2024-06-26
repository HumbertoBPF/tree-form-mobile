package com.example.formtreeapp.models

import com.google.gson.annotations.SerializedName

class Form (
    val id: String,
    val name: String,
    val description: String?,
    val owner: String,
    @SerializedName("form_tree") val formTree: List<TreeNode>?
)