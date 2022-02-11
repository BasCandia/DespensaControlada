package com.example.xpiration;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entidades.Lotes;
import entidades.Productos;

public class ExcelExporter {

File filePath = null;
    String fileName;

    public void buttonCreateExcel(Context context){
        Date now = new Date();
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        String vDateYMD = dateFormatYMD.format(now);

        Date nuevo=null;
        Date comparado=null;
//******************* Creacion de archivo si no existe o reemplazo si existe ***********************

        fileName ="Reporte_a_"+vDateYMD.replaceAll("/","_")+".xls";
        filePath = new File(Environment.getExternalStorageDirectory() + "/" + fileName);


        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();



//********************************* Creacion de pagina *********************************************
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Productos y Lotes");

        ArrayList<Productos> listaProductos;
        Productos p = new Productos();
        listaProductos = new ArrayList<>(p.mostrarProductos());

        HSSFRow TituloRow = hssfSheet.createRow(0);
        HSSFCell TituloCell = TituloRow.createCell(0);


//********************************* Encabezado *****************************************************
        TituloCell.setCellValue("Nombre Producto");
        hssfSheet.setColumnWidth(0,("Nombre Producto").length() * 250 );
        TituloCell = TituloRow.createCell(1);
        TituloCell.setCellValue("ID Lote");
        hssfSheet.setColumnWidth(1,("ID Lote").length() * 250 );
        TituloCell = TituloRow.createCell(2);
        TituloCell.setCellValue("Nombre Lote");
        hssfSheet.setColumnWidth(2,("Nombre Lote").length() * 250 );
        TituloCell = TituloRow.createCell(3);
        TituloCell.setCellValue("Fecha caducidad Lote");
        hssfSheet.setColumnWidth(3,("Fecha caducidad Lote").length() * 250 );
        TituloCell = TituloRow.createCell(4);
        TituloCell.setCellValue("Dias para caducar");
        hssfSheet.setColumnWidth(4,("Dias para caducar").length() * 250 );

//********* Se insertan datos en las filas y columnas correspondientes a productos y lotes *********
        int x = 1;
        int y = 0;
        for (int i= 0;i<listaProductos.size();i++){
            HSSFRow productoRow = hssfSheet.createRow(x);
            HSSFCell productoCell = productoRow.createCell(y);
            productoCell.setCellValue(listaProductos.get(i).getPRODUCTO_NOMBRE());

            x++;
            ArrayList<Lotes> listaLotes ;
            Lotes l = new Lotes();
            listaLotes = new ArrayList<>(l.mostrarLotes(listaProductos.get(i).getPRODUCTO_ID()));
            for(int j = 0; j < listaLotes.size();j++){
                y=1;
                HSSFRow loteRow = hssfSheet.createRow(x);
                HSSFCell loteCellA = loteRow.createCell(y);
                loteCellA.setCellValue(listaLotes.get(j).getLOTE_ID());

                y++;
                loteCellA = loteRow.createCell(y);
                loteCellA.setCellValue(listaLotes.get(j).getLOTE_NOMBRE());

                y++;
                loteCellA = loteRow.createCell(y);
                loteCellA.setCellValue(listaLotes.get(j).getLOTE_FECHA_CADUCIDAD().toString());

                try {
                    nuevo = dateFormatYMD.parse(vDateYMD);
                    comparado = listaLotes.get(j).getLOTE_FECHA_CADUCIDAD();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000);

                y++;
                loteCellA = loteRow.createCell(y);
                loteCellA.setCellValue(diff);

                x++;
            }
            y= 0;
        }

//**************************************** Segunda pagina **************************************************************
        HSSFSheet hssfSheetp2 = hssfWorkbook.createSheet("Lotes por Caducar");

        HSSFRow TituloRowp2 = hssfSheetp2.createRow(0);
        HSSFCell TituloCellp2 = TituloRowp2.createCell(0);

//********************************* Encabezado *****************************************************
        TituloCellp2.setCellValue("ID Lote");
        hssfSheetp2.setColumnWidth(0,("ID Lote").length() * 250 );
        TituloCellp2 = TituloRowp2.createCell(1);
        TituloCellp2.setCellValue("Nombre Lote");
        hssfSheetp2.setColumnWidth(1,("Nombre Lote").length() * 250 );
        TituloCellp2 = TituloRowp2.createCell(2);
        TituloCellp2.setCellValue("Fecha caducidad Lote");
        hssfSheetp2.setColumnWidth(2,("Fecha caducidad Lote").length() * 250 );
        TituloCellp2 = TituloRowp2.createCell(3);
        TituloCellp2.setCellValue("Dias para caducar");
        hssfSheetp2.setColumnWidth(3,("Dias para caducar").length() * 250 );

//********* Se insertan datos en las filas y columnas correspondientes a lotes por caducar *********
        x=1;
        y=0;
        Lotes lp2 = new Lotes();
        ArrayList<Lotes> listaLotesp2 = new ArrayList<>(lp2.mostrarTodoLotes());
        for (int i=0 ; i < listaLotesp2.size();i++) {
            HSSFRow loteRowp2 = hssfSheetp2.createRow(x);
            HSSFCell loteCellp2 = loteRowp2.createCell(y);

            loteCellp2.setCellValue(listaLotesp2.get(i).getLOTE_ID());

            y++;
            loteCellp2 = loteRowp2.createCell(y);
            loteCellp2.setCellValue(listaLotesp2.get(i).getLOTE_NOMBRE());

            y++;
            loteCellp2 = loteRowp2.createCell(y);
            loteCellp2.setCellValue(listaLotesp2.get(i).getLOTE_FECHA_CADUCIDAD().toString());

            try {
                nuevo = dateFormatYMD.parse(vDateYMD);
                comparado = listaLotesp2.get(i).getLOTE_FECHA_CADUCIDAD();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000);

            y++;
            loteCellp2 = loteRowp2.createCell(y);
            loteCellp2.setCellValue(diff);

            x++;
            y= 0;
        }

//**************************************** Tercera pagina ******************************************
        Lotes lp3 = new Lotes();
        ArrayList<Lotes> listaMerma = new ArrayList<>(lp3.MermaLotes());

        HSSFSheet hssfSheetp3 = hssfWorkbook.createSheet("Lotes en Merma");
//********************************* Encabezado *****************************************************
        HSSFRow TituloRowp3 = hssfSheetp3.createRow(0);
        HSSFCell TituloCellp3 = TituloRowp3.createCell(0);
        TituloCellp3.setCellValue("ID Lote");
        hssfSheetp3.setColumnWidth(0,("ID Lote").length() * 250 );
        TituloCellp3 = TituloRowp3.createCell(1);
        TituloCellp3.setCellValue("Nombre Lote");
        hssfSheetp3.setColumnWidth(1,("Nombre Lote").length() * 250 );
        TituloCellp3 = TituloRowp3.createCell(2);
        TituloCellp3.setCellValue("Fecha caducidad Lote");
        hssfSheetp3.setColumnWidth(2,("Fecha caducidad Lote").length() * 250 );
        TituloCellp3 = TituloRowp3.createCell(3);
        TituloCellp3.setCellValue("Dias para caducar");
        hssfSheetp3.setColumnWidth(3,("Dias para caducar").length() * 250 );

//********* Se insertan datos en las filas y columnas correspondientes a lotes en merma ************
        x=1;
        y=0;
        for (int i=0 ; i < listaMerma.size();i++) {
            HSSFRow loteRowp3 = hssfSheetp3.createRow(x);
            HSSFCell loteCellp3 = loteRowp3.createCell(y);

            loteCellp3.setCellValue(listaLotesp2.get(i).getLOTE_ID());

            y++;
            loteCellp3 = loteRowp3.createCell(y);
            loteCellp3.setCellValue(listaLotesp2.get(i).getLOTE_NOMBRE());

            y++;
            loteCellp3 = loteRowp3.createCell(y);
            loteCellp3.setCellValue(listaLotesp2.get(i).getLOTE_FECHA_CADUCIDAD().toString());

            try {
                nuevo = dateFormatYMD.parse(vDateYMD);
                comparado = listaLotesp2.get(i).getLOTE_FECHA_CADUCIDAD();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000);

            y++;
            loteCellp3 = loteRowp3.createCell(y);
            loteCellp3.setCellValue(diff);

            x++;
            y= 0;
        }


        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            Toast.makeText(context, "A ocurrido un error al crear el excel", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public String getPath(){
        return this.filePath.getPath();
    }

    public  String getFileName(){
        return this.fileName;
    }

}
