package com.dicoding.cvierapp.response

import com.google.gson.annotations.SerializedName

data class CVResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Status? = null
)

data class Status(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("massage")
	val massage: String? = null
)

data class Data(

	@field:SerializedName("improve")
	val improve: List<String?>? = null,

	@field:SerializedName("percentage")
	val percentage: Int? = null
)
