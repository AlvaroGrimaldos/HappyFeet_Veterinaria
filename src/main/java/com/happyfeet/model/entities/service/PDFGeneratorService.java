package com.happyfeet.model.entities.service;

import com.happyfeet.model.entities.CentroVeterinario;
import com.happyfeet.model.entities.Facturas;
import com.happyfeet.model.entities.ItemsFactura;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class PDFGeneratorService {
    private static final Logger logger = LogManager.getLogger(PDFGeneratorService.class);

    // Fuentes y colores para el diseño del PDF
    private static final Font TITULO_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font SUBTITULO_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY);
    private static final Font LARGE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED);

    // Colores personalizados
    private static final BaseColor COLOR_ENCABEZADO = new BaseColor(70, 130, 180); // Steel Blue
    private static final BaseColor COLOR_TOTAL = new BaseColor(255, 255, 0); // Yellow
    private static final BaseColor COLOR_TABLA_HEADER = new BaseColor(240, 240, 240); // Light Gray

    /**
     * Método principal para generar una factura en PDF
     * @param factura Objeto Facturas con los datos básicos
     * @param items Lista de ItemsFactura con los productos/servicios
     * @param rutaArchivo Ruta donde se guardará el PDF
     * @return true si se generó exitosamente, false en caso contrario
     */
    public boolean generarFacturaPDF(Facturas factura, List<ItemsFactura> items, String rutaArchivo) {
        try {
            // Crear documento en formato A4
            Document document = new Document(PageSize.A4);
            document.setMargins(36, 36, 36, 36); // Márgenes de 1/2 pulgada

            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
            document.open();

            CentroVeterinario centro = CentroVeterinario.getInstancia();

            // Construir el documento por secciones
            agregarEncabezadoCentro(document, centro);
            agregarInfoFactura(document, factura);
            agregarTablaItems(document, items);
            agregarTotalYObservaciones(document, factura);
            agregarPiePagina(document, centro);

            document.close();
            logger.info("PDF de factura #{} generado exitosamente: {}", factura.getId(), rutaArchivo);
            return true;

        } catch (DocumentException | IOException e) {
            logger.error("Error al generar PDF de factura #{}: {}",
                    factura != null ? factura.getId() : "unknown", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error inesperado al generar PDF: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Crea el encabezado con la información del centro veterinario
     */
    private void agregarEncabezadoCentro(Document document, CentroVeterinario centro) throws DocumentException {
        // Título principal del centro
        Paragraph titulo = new Paragraph(centro.getNombre().toUpperCase(), TITULO_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(5);
        document.add(titulo);


        // Tabla con información del centro (2 columnas)
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(20);

        // Columna izquierda - Información legal
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setPadding(5);
        leftCell.addElement(new Phrase("NIT: " + centro.getNit(), BOLD_FONT));
        leftCell.addElement(new Phrase("Teléfono: " + centro.getTelefono(), NORMAL_FONT));
        leftCell.addElement(new Phrase("Email: " + centro.getEmail(), NORMAL_FONT));

        // Columna derecha - Información de ubicación
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setPadding(5);
        rightCell.addElement(new Phrase("Dirección:", BOLD_FONT));
        rightCell.addElement(new Phrase(centro.getDireccion(), NORMAL_FONT));
        rightCell.addElement(new Phrase(" ", NORMAL_FONT)); // Espacio

        infoTable.addCell(leftCell);
        infoTable.addCell(rightCell);
        document.add(infoTable);

        // Línea separadora decorativa
        Paragraph separador = new Paragraph();
        for (int i = 0; i < 60; i++) {
            separador.add(new Chunk("═", SMALL_FONT));
        }
        separador.setAlignment(Element.ALIGN_CENTER);
        separador.setSpacingAfter(10);
        document.add(separador);
    }

    /**
     * Agrega la información específica de la factura
     */
    private void agregarInfoFactura(Document document, Facturas factura) throws DocumentException {
        // Título "FACTURA DE VENTA"
        Paragraph tituloFactura = new Paragraph("FACTURA DE VENTA", SUBTITULO_FONT);
        tituloFactura.setAlignment(Element.ALIGN_CENTER);
        tituloFactura.setSpacingAfter(15);
        document.add(tituloFactura);

        // Tabla con información de la factura
        PdfPTable facturaInfo = new PdfPTable(2);
        facturaInfo.setWidthPercentage(100);
        facturaInfo.setSpacingAfter(20);

        // Información izquierda
        PdfPCell leftInfo = new PdfPCell();
        leftInfo.setBorder(Rectangle.BOX);
        leftInfo.setPadding(10);
        leftInfo.setBackgroundColor(COLOR_TABLA_HEADER);

        Paragraph facturaNum = new Paragraph("FACTURA No.", BOLD_FONT);
        facturaNum.add(Chunk.NEWLINE);
        facturaNum.add(new Chunk(String.format("%06d", factura.getId()), LARGE_FONT));
        leftInfo.addElement(facturaNum);

        // Información derecha
        PdfPCell rightInfo = new PdfPCell();
        rightInfo.setBorder(Rectangle.BOX);
        rightInfo.setPadding(10);

        rightInfo.addElement(new Phrase("FECHA DE EMISIÓN:", BOLD_FONT));
        rightInfo.addElement(new Phrase(factura.getFechaEmision().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")), NORMAL_FONT));
        rightInfo.addElement(new Phrase(" ", NORMAL_FONT));
        rightInfo.addElement(new Phrase("CLIENTE ID: " + factura.getDuenoId(), NORMAL_FONT));
        rightInfo.addElement(new Phrase("CENTRO ID: " + factura.getCentroVeterinarioId(), NORMAL_FONT));

        facturaInfo.addCell(leftInfo);
        facturaInfo.addCell(rightInfo);
        document.add(facturaInfo);
    }

    /**
     * Crea la tabla detallada con todos los items de la factura
     */
    private void agregarTablaItems(Document document, List<ItemsFactura> items) throws DocumentException {
        // Título de la sección
        Paragraph tituloItems = new Paragraph("DETALLE DE PRODUCTOS Y SERVICIOS", SUBTITULO_FONT);
        tituloItems.setSpacingBefore(10);
        tituloItems.setSpacingAfter(10);
        document.add(tituloItems);

        // Crear tabla principal con 6 columnas
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.8f, 3.5f, 1f, 1.5f, 1.5f, 1.7f});
        table.setSpacingAfter(15);

        // Encabezados de la tabla
        agregarCeldaEncabezado(table, "#");
        agregarCeldaEncabezado(table, "DESCRIPCIÓN");
        agregarCeldaEncabezado(table, "ID");
        agregarCeldaEncabezado(table, "CANT.");
        agregarCeldaEncabezado(table, "PRECIO UNIT.");
        agregarCeldaEncabezado(table, "SUBTOTAL");

        // Agregar filas de datos
        BigDecimal totalCalculado = BigDecimal.ZERO;
        int numeroItem = 1;

        for (ItemsFactura item : items) {
            // Número de item
            PdfPCell numCell = new PdfPCell(new Phrase(String.valueOf(numeroItem++), NORMAL_FONT));
            numCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            numCell.setPadding(5);
            table.addCell(numCell);

            // Descripción del producto/servicio
            String descripcion = item.getServicioDescripcion();
            if (descripcion == null || descripcion.trim().isEmpty()) {
                descripcion = "Producto/Servicio ID: " + item.getProductoId();
            }
            PdfPCell descCell = new PdfPCell(new Phrase(descripcion, NORMAL_FONT));
            descCell.setPadding(5);
            table.addCell(descCell);

            // ID del producto
            PdfPCell prodIdCell = new PdfPCell(new Phrase(item.getProductoId().toString(), NORMAL_FONT));
            prodIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            prodIdCell.setPadding(5);
            table.addCell(prodIdCell);

            // Cantidad
            PdfPCell cantCell = new PdfPCell(new Phrase(item.getCantidad().toString(), NORMAL_FONT));
            cantCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cantCell.setPadding(5);
            table.addCell(cantCell);

            // Precio unitario
            PdfPCell precioCell = new PdfPCell(new Phrase("$" + formatearMonto(item.getPrecioUnitario()), NORMAL_FONT));
            precioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            precioCell.setPadding(5);
            table.addCell(precioCell);

            // Subtotal
            PdfPCell subtotalCell = new PdfPCell(new Phrase("$" + formatearMonto(item.getSubtotal()), BOLD_FONT));
            subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            subtotalCell.setPadding(5);
            table.addCell(subtotalCell);

            totalCalculado = totalCalculado.add(item.getSubtotal());
        }

        // Fila de total
        agregarFilaTotal(table, items.size(), totalCalculado);

        document.add(table);
    }

    /**
     * Agrega una celda de encabezado con formato especial
     */
    private void agregarCeldaEncabezado(PdfPTable table, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, BOLD_FONT));
        cell.setBackgroundColor(COLOR_ENCABEZADO);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        Font whiteFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        cell.setPhrase(new Phrase(texto, whiteFont));
        table.addCell(cell);
    }

    /**
     * Agrega la fila final con el total de la factura
     */
    private void agregarFilaTotal(PdfPTable table, int cantidadItems, BigDecimal total) {
        // Celdas vacías para alinear
        for (int i = 0; i < 4; i++) {
            PdfPCell emptyCell = new PdfPCell(new Phrase("", NORMAL_FONT));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(emptyCell);
        }

        // Etiqueta "TOTAL"
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL:", BOLD_FONT));
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setBackgroundColor(COLOR_TOTAL);
        totalLabelCell.setPadding(8);
        table.addCell(totalLabelCell);

        // Valor del total
        PdfPCell totalValueCell = new PdfPCell(new Phrase("$" + formatearMonto(total), LARGE_FONT));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setBackgroundColor(COLOR_TOTAL);
        totalValueCell.setPadding(8);
        table.addCell(totalValueCell);
    }

    /**
     * Agrega información del total y observaciones importantes
     */
    private void agregarTotalYObservaciones(Document document, Facturas factura) throws DocumentException {
        document.add(Chunk.NEWLINE);

        // Resumen de totales (si necesitas agregar IVA, descuentos, etc.)
        PdfPTable resumenTable = new PdfPTable(2);
        resumenTable.setWidthPercentage(60);
        resumenTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        resumenTable.setSpacingAfter(20);

        // Subtotal
        agregarFilaResumen(resumenTable, "Subtotal:", factura.getTotal());
        // Si manejas IVA:
        // agregarFilaResumen(resumenTable, "IVA (19%):", calcularIVA(factura.getTotal()));

        // Total final destacado
        PdfPCell totalFinalLabel = new PdfPCell(new Phrase("TOTAL A PAGAR:", BOLD_FONT));
        totalFinalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalFinalLabel.setPadding(8);
        totalFinalLabel.setBackgroundColor(COLOR_TOTAL);

        PdfPCell totalFinalValue = new PdfPCell(new Phrase("$" + formatearMonto(factura.getTotal()), LARGE_FONT));
        totalFinalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalFinalValue.setPadding(8);
        totalFinalValue.setBackgroundColor(COLOR_TOTAL);

        resumenTable.addCell(totalFinalLabel);
        resumenTable.addCell(totalFinalValue);

        document.add(resumenTable);

        // Sección de observaciones
        Paragraph observacionesTitulo = new Paragraph("TÉRMINOS Y CONDICIONES:", BOLD_FONT);
        observacionesTitulo.setSpacingBefore(10);
        document.add(observacionesTitulo);

        String[] observaciones = {
                "• Esta factura es válida como comprobante de compra y garantía.",
                "• Los productos veterinarios tienen garantía según políticas del establecimiento.",
                "• Para reclamos o garantías, conserve este comprobante.",
                "• Los servicios prestados están respaldados por profesionales certificados.",
                "• Consultas post-venta: " + CentroVeterinario.getInstancia().getTelefono()
        };

        for (String obs : observaciones) {
            Paragraph linea = new Paragraph(obs, SMALL_FONT);
            linea.setSpacingAfter(2);
            document.add(linea);
        }
    }

    /**
     * Agrega una fila al resumen de totales
     */
    private void agregarFilaResumen(PdfPTable table, String etiqueta, BigDecimal valor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(etiqueta, NORMAL_FONT));
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase("$" + formatearMonto(valor), NORMAL_FONT));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    /**
     * Crea el pie de página con información adicional
     */
    private void agregarPiePagina(Document document, CentroVeterinario centro) throws DocumentException {
        document.add(Chunk.NEWLINE);

        // Línea separadora
        Paragraph separador = new Paragraph();
        for (int i = 0; i < 50; i++) {
            separador.add(new Chunk("─", SMALL_FONT));
        }
        separador.setAlignment(Element.ALIGN_CENTER);
        document.add(separador);

        // Información de generación
        String fechaGeneracion = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horaGeneracion = java.time.LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss"));

        Paragraph infoGeneracion = new Paragraph(
                "Documento generado el " + fechaGeneracion + " a las " + horaGeneracion,
                SMALL_FONT
        );
        infoGeneracion.setAlignment(Element.ALIGN_CENTER);
        infoGeneracion.setSpacingBefore(5);
        document.add(infoGeneracion);

        // Información de contacto
        Paragraph contacto = new Paragraph(
                centro.getNombre() + " - " + centro.getEmail() + " - " + centro.getTelefono(),
                SMALL_FONT
        );
        contacto.setAlignment(Element.ALIGN_CENTER);
        document.add(contacto);

        // Mensaje de agradecimiento
        Paragraph gracias = new Paragraph("¡Gracias por confiar en nosotros para el cuidado de sus mascotas!", NORMAL_FONT);
        gracias.setAlignment(Element.ALIGN_CENTER);
        gracias.setSpacingBefore(10);
        document.add(gracias);
    }

    /**
     * Formatea los montos monetarios para mostrar con separadores de miles
     */
    private String formatearMonto(BigDecimal monto) {
        if (monto == null) return "0.00";
        return String.format("%,.2f", monto);
    }

    /**
     * Genera un nombre único para el archivo PDF
     */
    public String generarNombreArchivo(Integer facturaId) {
        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hora = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
        return String.format("Factura_%06d_%s_%s.pdf", facturaId, fechaHoy, hora);
    }

    /**
     * Genera la ruta completa donde se guardará el archivo
     */
    public String generarRutaCompleta(Integer facturaId, String directorioDestino) {
        if (directorioDestino == null || directorioDestino.trim().isEmpty()) {
            // Ruta por defecto: Desktop/Facturas_HappyFeet/
            String userHome = System.getProperty("user.home");
            directorioDestino = userHome + "/Desktop/Facturas_HappyFeet/";
        }

        // Asegurar que el directorio termine con separador
        if (!directorioDestino.endsWith("/") && !directorioDestino.endsWith("\\")) {
            directorioDestino += System.getProperty("file.separator");
        }

        // Crear directorio si no existe
        java.io.File directorio = new java.io.File(directorioDestino);
        if (!directorio.exists()) {
            boolean creado = directorio.mkdirs();
            if (creado) {
                logger.info("Directorio creado: {}", directorioDestino);
            } else {
                logger.warn("No se pudo crear el directorio: {}", directorioDestino);
            }
        }

        return directorioDestino + generarNombreArchivo(facturaId);
    }

    /**
     * Método utilitario para abrir el PDF generado (opcional)
     */
    public boolean abrirPDF(String rutaArchivo) {
        try {
            java.io.File archivo = new java.io.File(rutaArchivo);
            if (archivo.exists()) {
                java.awt.Desktop.getDesktop().open(archivo);
                logger.info("PDF abierto: {}", rutaArchivo);
                return true;
            } else {
                logger.error("Archivo PDF no encontrado: {}", rutaArchivo);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al abrir PDF: {}", e.getMessage());
            return false;
        }
    }
}
