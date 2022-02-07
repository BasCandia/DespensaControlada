package com.example.xpiration;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        Date now = new Date();
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        String vDateYMD = dateFormatYMD.format(now);

        Date nuevo=null;
        Date comparado=null;

        File filePath = null;

            filePath = new File(Environment.getExternalStorageDirectory() + "/Reporte_a_"+vDateYMD.replaceAll("/","_")+".xls");

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Productos y Lotes");

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
        TituloCell = TituloRow.createCell(4);
        TituloCell.setCellValue("Dias para caducar");



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

        HSSFSheet hssfSheetp2 = hssfWorkbook.createSheet("Lotes por Caducar");

        HSSFRow TituloRowp2 = hssfSheetp2.createRow(0);
        HSSFCell TituloCellp2 = TituloRowp2.createCell(0);

        TituloCellp2.setCellValue("ID Lote");
        TituloCellp2 = TituloRowp2.createCell(1);
        TituloCellp2.setCellValue("Nombre Lote");
        TituloCellp2 = TituloRowp2.createCell(2);
        TituloCellp2.setCellValue("Fecha caducidad Lote");

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

            x++;
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
