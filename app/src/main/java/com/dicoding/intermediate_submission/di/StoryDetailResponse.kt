package com.dicoding.intermediate_submission.di

import com.google.gson.annotations.SerializedName
import android.os.Parcel
import android.os.Parcelable

data class StoryDetailResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("story")
	val story: Story
)



data class Story(
	val photoUrl: String,
	val createdAt: String,
	val name: String,
	val description: String,
	val lon: Double,
	val id: String,
	val lat: Double
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readDouble() ?: 0.0,
		parcel.readString() ?: "",
		parcel.readDouble() ?: 0.0,
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(photoUrl)
		parcel.writeString(createdAt)
		parcel.writeString(name)
		parcel.writeString(description)
		parcel.writeDouble(lon)
		parcel.writeString(id)
		parcel.writeDouble(lat)
	}


	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Story> {
		override fun createFromParcel(parcel: Parcel): Story {
			return Story(parcel)
		}

		override fun newArray(size: Int): Array<Story?> {
			return arrayOfNulls(size)
		}
	}
}
