package com.kamak.skripsweetnewwes.data;

/**
 * Created by chukamak on 26/01/2018.
 */

public class DataModel {

        private String id, nama,isi,komentar,tglkomen;

        public DataModel() {
        }

        public DataModel(String id, String nama,String tglkomen,String komentar) {
            this.id = id;
            this.nama = nama;
            this.isi = isi;
            this.komentar = komentar;
            this.tglkomen = tglkomen;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

         public String getIsi() {
            return isi;
        }

        public void setIsi(String isi) {
            this.isi = isi;
        }
        public String getTglkomen() {
            return tglkomen;
        }

        public void setTglkomen(String tglkomen) {
            this.tglkomen = tglkomen;
        }
}
