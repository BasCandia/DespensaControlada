package com.example.xpiration;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import entidades.Lotes;
import entidades.Productos;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelExporter {


    public static void buttonCreateExcel(Context context){
        File filePath = new File(Environment.getExternalStorageDirectory() + "/Test.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Productos y LotesX");


        ArrayList<Productos> listaProductos;
        Productos p = new Productos();
        listaProductos = new ArrayList<>(p.mostrarProductos());

        HSSFRow TituloRow = hssfSheet.createRow(0);
        HSSFCell TituloCell = TituloRow.createCell(0);

        TituloCell.setCellValue("Nombre Producto");
        TituloCell = TituloRow.createCell(1);
        TituloCell.setCellValue("ID Lote");
        TituloCell = TituloRow.createCell(2);
        TituloCell.setCellValue("Nombre Lote");
        TituloCell = TituloRow.createCell(3);
        TituloCell.setCellValue("Fecha caducidad Lote");

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

                x++;
            }
            y= 0;
        }

        ArrayList<Lotes> listaMerma ;

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
}
