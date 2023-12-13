package online.soumya.weatherapp.databse

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityName: String
)
