package com.example.tubes.entity

class Pelanggan(var nama:String, var workout: String,  var jamMulai: String, var jamAkhir: String) {

    companion object{
        @JvmField
        var listofPelanggan = arrayOf(
            Pelanggan("Angela", "20", "10/01/2010", "10/03/2010"),
            Pelanggan("Bobi", "21",  "11/04/2010", "11/06/2010"),
            Pelanggan("Cicilia", "22",  "12/07/2010", "12/09/2010"),
            Pelanggan("Danny", "23",  "13/10/2010", "13/12/2010"),
            Pelanggan("Hanny", "24",  "14/01/2011", "14/03/2011"),
            Pelanggan("Imanuel", "25",  "15/04/2011", "15/06/2011"),
            Pelanggan("Jessica", "26",  "16/07/2011", "16/09/2011"),
            Pelanggan("Kenny", "27",  "17/10/2011", "17/12/2011"),
            Pelanggan("Lisa", "28",  "18/01/2012", "18/03/2012"),
            Pelanggan("Michael", "29",  "19/04/2012", "19/06/2012")
        )
    }
}