package export;

import data.db.Car;
import data.db.Dealer;
import jxl.CellView;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import logger.Logger;

import java.util.Map;
import java.util.Set;

public class CarsSheet {

    private final String CITY_COLUMN = "Miejscowość";
    private final String DEALERNAME_COLUMN = "Dealer name";
    private final String MARK_COLUMN = "Brand";
    private final String MODEL_COLUMN = "Model";
    private final String YEAR_COLUMN = "Year";
    private final String MILEAGE_COLUMN = "Mileage [km]";
    private final String PRICE_COLUMN = "Price";
    private final String PRICECUR_COLUMN = "Currency";

    public void export(WritableWorkbook workbook, Map<Dealer, Set<Car>> offersMap) throws CrawlerExportException {
        WritableSheet sheet = workbook.createSheet("Offers", 1);
        createHeader(sheet);
        try {
            fillWithData(offersMap, sheet, sheet.getRows());
        } catch (RowsExceededException e) {
            Logger.log(e.getMessage());
        }
        expandColumns(sheet, 8);
    }

    private void createHeader(WritableSheet sheet) throws CrawlerExportException {
        WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 13, jxl.write.WritableFont.BOLD);
        WritableCellFormat arial14format = new WritableCellFormat(arial14font);
        try {
            arial14format.setBackground(Colour.LIGHT_GREEN);
            arial14format.setBorder(Border.ALL, BorderLineStyle.THIN);
            sheet.setRowView(1, 400);
            sheet.addCell(new Label(1, 1, CITY_COLUMN, arial14format));
            sheet.addCell(new Label(2, 1, DEALERNAME_COLUMN, arial14format));
            sheet.addCell(new Label(3, 1, MARK_COLUMN, arial14format));
            sheet.addCell(new Label(4, 1, MODEL_COLUMN, arial14format));
            sheet.addCell(new Label(5, 1, YEAR_COLUMN, arial14format));
            sheet.addCell(new Label(6, 1, MILEAGE_COLUMN, arial14format));
            sheet.addCell(new Label(7, 1, PRICE_COLUMN, arial14format));
            sheet.addCell(new Label(8, 1, PRICECUR_COLUMN, arial14format));
        } catch (RowsExceededException e) {
            throw new CrawlerExportException("Row exceeded exception", e);
        } catch (WriteException e) {
            throw new CrawlerExportException("Error while trying to create xls file table header.", e);
        }
    }

    private void fillWithData(Map<Dealer, Set<Car>> offersMap, WritableSheet sheet, int startRow) throws RowsExceededException, CrawlerExportException {
        try {
            WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat arial10format = new WritableCellFormat(arial10font);
            arial10format.setWrap(true);
            arial10format.setBackground(Colour.GRAY_25);
            arial10format.setBorder(Border.ALL, BorderLineStyle.THIN);
            Set<Dealer> keySet = offersMap.keySet();
            int i = startRow;
            Logger.log(" Filling sheet ...");
            for (Dealer dealer : keySet) {
                if (dealer == null) {
                    continue;
                }
                sheet.setRowView(i, 1000);
                Set<Car> offers = offersMap.get(dealer);
                for (Car car : offers) {
                    sheet.addCell(new Label(1, i, dealer.getZipCity(), arial10format));
                    sheet.addCell(new Label(2, i, dealer.getName(), arial10format));
                    sheet.addCell(new Label(3, i, car.getMark(), arial10format));
                    sheet.addCell(new Label(4, i, car.getModel(), arial10format));
                    sheet.addCell(new Label(5, i, car.getYear(), arial10format));
                    sheet.addCell(new Label(6, i, car.getMileage(), arial10format));
                    sheet.addCell(new Label(7, i, car.getPrice(), arial10format));
                    sheet.addCell(new Label(8, i, car.getPriceCurrency(), arial10format));
                    i++;
                }
            }
        } catch (WriteException e) {
            throw new CrawlerExportException("Error while trying to fill xls file. Try again", e);
        }
    }

    private void expandColumns(WritableSheet sheet, int amountOfColumns) {
        int c = amountOfColumns;

        CellView cell1 = sheet.getColumnView(1);
        cell1.setSize(7 * 256);
        sheet.setColumnView(1, cell1);

        for (int x = 1; x <= c; x++) {

            if (x == 5 || x == amountOfColumns) {
                CellView cell = sheet.getColumnView(x);
                cell.setSize(17 * 256);
                sheet.setColumnView(x, cell);
                continue;
            }

            CellView cell = sheet.getColumnView(x);
            cell.setSize(35 * 256);
            sheet.setColumnView(x, cell);
        }
    }
}
