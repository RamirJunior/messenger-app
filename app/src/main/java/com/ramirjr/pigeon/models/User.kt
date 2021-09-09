package com.ramirjr.pigeon.models

class User(
    val uid: String,
    val username: String,
    val profileImageUrl: String
) {
    constructor() : this("", "", "")
}