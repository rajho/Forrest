package com.example.android.forrest.data

import androidx.lifecycle.LiveData
import com.example.android.forrest.data.model.Exercise

class FakeExercisesLocalDataSource() : ExercisesDataSource {
	override fun insertExercise(exercise: Exercise) {
		TODO("Not yet implemented")
	}

	override fun getExercisesByUserId(userId: String): LiveData<MutableList<Exercise>> {
		TODO("Not yet implemented")
	}

	override fun getLastExerciseByUserId(userId: String): Exercise {
		TODO("Not yet implemented")
	}

}