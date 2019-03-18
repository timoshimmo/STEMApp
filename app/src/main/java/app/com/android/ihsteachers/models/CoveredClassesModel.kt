package app.com.android.ihsteachers.models

data class CoveredClassesModel(val id: String, val schoolTerm: String, val subject: String,
                               val topic: String, val classes: String, val infoBoxBackground: Int)

//data class CoveredClassesModel(val id: String, val dayOfWeek: String, val calDate: String, val schoolTerm: String,
     //                          val subjectClasses: List<ClassDetailsItem> = listOf())