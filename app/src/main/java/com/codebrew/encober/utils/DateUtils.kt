package com.codebrew.encober.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*


object DateUtils {
    private  var dpd:DatePickerDialog?=null

    fun openDateDialog(context :Context,onDateSelectedListener: OnDateSelectedListener) {
        val c = Calendar.getInstance()
        val yearCal = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener {  _, year, monthOfYear, dayOfMonth ->
            val mCurrentTimeSelected = Calendar.getInstance()
            mCurrentTimeSelected.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            mCurrentTimeSelected.set(Calendar.MONTH,monthOfYear)
            mCurrentTimeSelected.set(Calendar.YEAR,year)
            onDateSelectedListener.dateTimeSelected(mCurrentTimeSelected)
        }, yearCal, month, day)
        dpd.datePicker.maxDate=c.timeInMillis
        dpd.show()
    }


    interface OnDateSelectedListener{
        fun dateTimeSelected(dateCal: Calendar)
    }

    fun openDOBDateDialog(context :Context, onDateSelectedListener: OnDateSelectedListener) {
        val c = Calendar.getInstance()
        val yearCal = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.add(Calendar.YEAR,-12)

        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener {  _, year, monthOfYear, dayOfMonth ->
            val mCurrentTimeSelected = Calendar.getInstance()
            mCurrentTimeSelected.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            mCurrentTimeSelected.set(Calendar.MONTH,monthOfYear)
            mCurrentTimeSelected.set(Calendar.YEAR,year)
            onDateSelectedListener.dateTimeSelected(mCurrentTimeSelected)
        }, yearCal, month, day)


        dpd.datePicker.maxDate=c.timeInMillis
        dpd.show()
    }

     fun getAgeFromDOB(dobString: String?): String {
        var ageInt = 0
        try {
            val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val date = format.parse(dobString)
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()
            dob.time = date
            var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            ageInt = age
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ageInt.toString()
    }

    fun getFormatFromDate(date: Date?, format: String): String {
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat(format, locale)

        return try {
            sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }


    fun getFormatFromMillis(date: Long?, format: String): String {
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat(format, locale)

        return try {
            sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }

}