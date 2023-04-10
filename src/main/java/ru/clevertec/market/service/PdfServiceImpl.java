package ru.clevertec.market.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ru.clevertec.market.dao.api.DiscountCardDao;
import ru.clevertec.market.dao.api.ProductDao;
import ru.clevertec.market.data.Order;
import ru.clevertec.market.data.receipt.Receipt;
import ru.clevertec.market.exception.PdfServiceException;
import ru.clevertec.market.factory.ReceiptFactory;
import ru.clevertec.market.service.api.DiscountCardService;
import ru.clevertec.market.service.api.PdfService;
import ru.clevertec.market.service.api.ProductService;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class PdfServiceImpl extends ReceiptServiceImpl implements PdfService {

    public static final Path RESOURCES_TEMP;
    public static final Path CLEVERTEC_TITLE;

    static {
        final URL tempDirectory = PdfServiceImpl.class.getResource("/temp");
        final URL clevertecTitle = PdfServiceImpl.class.getResource("/Clevertec_Template.pdf");
        if (Objects.isNull(tempDirectory)) {
            throw new PdfServiceException("resources/temp not found");
        }
        if (Objects.isNull(clevertecTitle)) {
            throw new PdfServiceException("resources/Clevertec_Template.pdf not found");
        }
        RESOURCES_TEMP = Paths.get(tempDirectory.getPath());
        CLEVERTEC_TITLE = Paths.get(clevertecTitle.getPath());
    }

    public PdfServiceImpl(ProductService productService,
                          DiscountCardService discountCardService,
                          ReceiptFactory receiptFactory) {
        super(productService, discountCardService, receiptFactory);
    }

    @Override
    public Path getPath(Order order) {
        final Receipt receipt = super.getReceipt(order);
        final Document document = new Document(PageSize.A4);
        final PdfDiv emptyDiv = new PdfDiv();
        final PdfPTable titleTable = getTitleTable(receipt);
        final PdfPTable bodyTable = getBodyTable(receipt);
        final PdfPTable totalTable = getTotalTable(receipt);
        final Path receipt1 = getPathToTempFile();
        final PdfWriter writer = getPdfWriter(document, receipt1);
        final PdfReader pdfReader = getPdfReader();
        final PdfImportedPage importedPage = writer.getImportedPage(pdfReader, 1);
        document.open();

        final PdfContentByte cb = writer.getDirectContent();
        document.newPage();
        cb.addTemplate(importedPage, 0, 0);
        emptyDiv.setHeight(220f);
        documentAddAll(document, emptyDiv, titleTable, bodyTable, totalTable);

        document.close();
        return receipt1;
    }

    /**
     * Creates a temp file
     *
     * @return path to file
     */
    private Path getPathToTempFile() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        try {
            return Files.createTempFile(RESOURCES_TEMP, "receipt:" + now + ":", ".pdf");
        } catch (IOException e) {
            throw new PdfServiceException(e.getMessage());
        }
    }

    /**
     * Add all elements to the document
     *
     * @param document current doc
     * @param elements elements to add to the doc
     */
    private void documentAddAll(Document document, Element... elements) {
        Arrays.stream(elements).forEachOrdered(e -> {
            try {
                document.add(e);
            } catch (DocumentException exception) {
                throw new PdfServiceException(exception.getMessage());
            }
        });
    }

    /**
     * Created pdf table with the receipt's total block
     *
     * @param receipt current receipt
     * @return created table
     */
    private PdfPTable getTotalTable(Receipt receipt) {
        PdfPTable totalTable = new PdfPTable(2);
        AtomicInteger i = new AtomicInteger(0);
        receipt.getTotal().lines()
                .map(s -> s.split(" {2,}"))
                .flatMap(Arrays::stream)
                .map(s -> new PdfPCell(Phrase.getInstance(s)))
                .peek(c -> c.setHorizontalAlignment(i.getAndIncrement() % 2 == 0 ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT))
                .forEach(totalTable::addCell);
        return totalTable;
    }

    /**
     * Created the pdf table with the receipt's title block
     *
     * @param receipt current receipt
     * @return created table
     */
    private PdfPTable getTitleTable(Receipt receipt) {
        PdfPTable titleTable = new PdfPTable(1);
        receipt.getTitle().replaceAll(" +", " ").lines()
                .forEach(s -> {
                    PdfPCell cell = new PdfPCell(new Phrase(s));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    titleTable.addCell(cell);
                });

        return titleTable;
    }

    /**
     * Created the pdf table with sales
     *
     * @param receipt current receipt
     * @return created table
     */
    private PdfPTable getBodyTable(Receipt receipt) {
        Pattern pattern = Pattern.compile("(\\d+)|(([a-zA-Z]+ ?)+)|(\\$\\d+\\.\\d+)");
        PdfPTable bodyTable = new PdfPTable(4);
        AtomicInteger bodyIndex = new AtomicInteger(1);
        receipt.getBody().lines()
                .flatMap(s -> pattern.matcher(s).results())
                .map(MatchResult::group)
                .map(s -> new PdfPCell(Phrase.getInstance(s)))
                .peek(c -> c.setHorizontalAlignment(bodyIndex.getAndIncrement() % 4 == 0 ?
                        Element.ALIGN_RIGHT : Element.ALIGN_CENTER))
                .forEach(bodyTable::addCell);
        return bodyTable;
    }

    private PdfWriter getPdfWriter(Document document, Path path) {
        try {
            return PdfWriter.getInstance(document, Files.newOutputStream(path));
        } catch (DocumentException | IOException e) {
            throw new PdfServiceException(e.getMessage());
        }
    }

    private PdfReader getPdfReader() {
        try {
            return new PdfReader(Files.newInputStream(CLEVERTEC_TITLE));
        } catch (IOException e) {
            throw new PdfServiceException(e.getMessage());
        }
    }
}
