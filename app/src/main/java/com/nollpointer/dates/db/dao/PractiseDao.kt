package com.nollpointer.dates.db.dao

import androidx.room.*
import com.nollpointer.dates.db.entity.PractiseEntity


/**
 * Дао для практики
 *
 * @author Onanov Aleksey (@onanov)
 */
@Dao
interface PractiseDao {

    // TODO: 13.03.2021 Решить, стоит ли переводить в Flowable 
    @Query("SELECT * FROM practise")
    fun getAll(): List<PractiseEntity>

    @Query("SELECT * FROM practise WHERE id = :id")
    fun getById(id: Long): PractiseEntity

    @Insert
    fun insert(practise: PractiseEntity)

    @Update
    fun update(practise: PractiseEntity)

    @Delete
    fun delete(practise: PractiseEntity)

}