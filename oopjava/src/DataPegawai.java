import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class Pegawai {

    private String nip;
    private String nama;
    private String unitKerja;
    private String fakultas;

    public Pegawai() {
    }

    public Pegawai(String nip, String nama) {
        this.nip = nip;
        this.nama = nama;
        this.unitKerja = "unitkerja";
        this.fakultas = "fakultas";
    }

    public Pegawai(String nip, String nama, String unitKerja, String fakultas) {
        this.nip = nip;
        this.nama = nama;
        this.unitKerja = unitKerja;
        this.fakultas = fakultas;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(String unitKerja) {
        this.unitKerja = unitKerja;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

}

class ManajemenPegawai {

    private final ArrayList<Pegawai> daftarPegawai = new ArrayList<>();
    private final Scanner input = new Scanner(System.in);
    private final String FILE_NAME = "DataPegawai.txt";

    public ManajemenPegawai() {
        muatDataDariFile();
    }

    public void tampilMenu() {
        int pilihan;
        do {
            System.out.println("===== Sistem Informasi Kepegawaian =====");
            System.out.println("1. Lihat semua pegawai");
            System.out.println("2. Tambah pegawai");
            System.out.println("3. Edit pegawai");
            System.out.println("4. Hapus pegawai");
            System.out.println("5. Cari pegawai");
            System.out.println("0. Keluar");
            System.out.print("Pilihan: ");
            while (!input.hasNextInt()) {
                System.out.print("Harus angka. Coba lagi: ");
                input.next();
            }
            pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {
                case 1 ->
                    lihatDaftar();
                case 2 ->
                    tambahPegawai();
                case 3 ->
                    editPegawai();
                case 4 ->
                    hapusPegawai();
                case 5 ->
                    cariPegawai();
                case 0 -> {
                    System.out.println("Data disimpan. Terima kasih!");
                }
                default ->
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    private void lihatDaftar() {
        if (daftarPegawai.isEmpty()) {
            System.out.println("Belum ada data pegawai.");
            return;
        }
        System.out.println("\n== Daftar Pegawai ==");
        for (Pegawai p : daftarPegawai) {
            System.out.println("NIP: " + p.getNip() + " | Nama: " + p.getNama());
        }
        System.out.println();
    }

    private void tambahPegawai() {
        System.out.print("Masukkan NIP: ");
        String nip = input.nextLine();
        if (cariByNip(nip) != null) {
            System.out.println("NIP sudah terdaftar.");
            return;
        }

        System.out.print("Masukkan Nama: ");
        String nama = input.nextLine();
        daftarPegawai.add(new Pegawai(nip, nama));
        simpanDataKeFile();
        System.out.println("Pegawai berhasil ditambahkan.");
    }

    private void editPegawai() {
        lihatDaftar();
        System.out.print("Masukkan NIP pegawai yang ingin diedit: ");
        String nipLama = input.nextLine();
        Pegawai p = cariByNip(nipLama);
        if (p == null) {
            System.out.println("Pegawai tidak ditemukan.");
            return;
        }

        System.out.println("Data ditemukan: NIP: " + p.getNip() + " | Nama: " + p.getNama());
        System.out.print("Masukkan NIP baru: ");
        String nipBaru = input.nextLine();

        if (!nipBaru.equals(nipLama) && cariByNip(nipBaru) != null) {
            System.out.println("NIP baru sudah terpakai!");
            return;
        }

        System.out.print("Masukkan Nama baru: ");
        String namaBaru = input.nextLine();
        p.setNip(nipBaru);
        p.setNama(namaBaru);
        simpanDataKeFile();
        System.out.println("Data berhasil diperbarui.");
    }

    private void hapusPegawai() {
        lihatDaftar();
        System.out.print("Masukkan NIP pegawai yang ingin dihapus: ");
        String nip = input.nextLine();
        Pegawai p = cariByNip(nip);
        if (p == null) {
            System.out.println("Pegawai tidak ditemukan.");
            return;
        }

        System.out.println("Data ditemukan: NIP: " + p.getNip() + " | Nama: " + p.getNama());
        System.out.print("Yakin ingin menghapus data ini? (y/n): ");
        String konfirmasi = input.nextLine();
        if (konfirmasi.equalsIgnoreCase("y")) {
            daftarPegawai.remove(p);
            simpanDataKeFile();
            System.out.println("Data berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    private void cariPegawai() {
        System.out.print("Cari berdasarkan NIP atau Nama: ");
        String keyword = input.nextLine().toLowerCase();
        boolean ditemukan = false;

        for (Pegawai p : daftarPegawai) {
            if (p.getNip().toLowerCase().contains(keyword) || p.getNama().toLowerCase().contains(keyword)) {
                System.out.println("NIP: " + p.getNip() + " | Nama: " + p.getNama());
                ditemukan = true;
            }
        }

        if (!ditemukan) {
            System.out.println("Tidak ada pegawai yang cocok.");
        }
    }

    private Pegawai cariByNip(String nip) {
        for (Pegawai p : daftarPegawai) {
            if (p.getNip().equalsIgnoreCase(nip)) {
                return p;
            }
        }
        return null;
    }

    private void simpanDataKeFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Pegawai p : daftarPegawai) {
                writer.println(p.getNip() + "|" + p.getNama());
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data ke file.");
        }
    }

    private void muatDataDariFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String baris;
            while ((baris = reader.readLine()) != null) {
                String[] data = baris.split("\\|");
                if (data.length == 2) {
                    daftarPegawai.add(new Pegawai(data[0], data[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file.");
        }
    }
}
